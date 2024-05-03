package org.example.Controllers;


import com.twilio.rest.api.v2010.account.Message;
import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.type.PhoneNumber;
import org.example.Misc.AppStuff;
import org.example.Models.SchedulesMessageModel;
import org.example.Models.SchedulesTeamMessagesModel;
import org.example.Repositories.SchedulesMessageRepository;
import org.example.Repositories.SchedulesTeamMessageRepository;
import org.example.Repositories.SchedulesTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
public class SchedulesMessageController {
    @Autowired
    SchedulesMessageRepository messageRepository;
    @Autowired
    SchedulesTeamRepository teamRepository;
    @Autowired
    SchedulesTeamMessageRepository teamMessageRepository;
    AppStuff appStuff = new AppStuff();

    @Value("${twilio.property.account_sid}")
    String twilioSid;

    @Value("${twilio.property.auth_token}")
    String twilioAuthToken;

    @Value("${twilio.property.sending_phone_number}")
    String sendingPhoneNumber;


    @PostMapping("/send")
    public String sendSmsMessage(@RequestParam("recipientPhoneNumber") String recipientPhoneNumber, @RequestParam("userMessage") String messageTitle) {
        appStuff.initialiseTwilioService(twilioSid, twilioAuthToken);
        String userPhoneNumber = teamRepository.findOneByUserPhoneNumber(recipientPhoneNumber).getUserPhoneNumber();
        String sendingMessageContent = messageRepository.findOneByMessageTitle(messageTitle).getMessageContent();
        String recipientUserId = teamRepository.findOneByUserPhoneNumber(recipientPhoneNumber).getUserId();
        String sendingMessageId = messageRepository.findOneByMessageTitle(messageTitle).getMessageId();
        SchedulesTeamMessagesModel teamMessagesModel = new SchedulesTeamMessagesModel();
        teamMessagesModel.setMessageId(sendingMessageId);
        teamMessagesModel.setUserId(recipientUserId);
        teamMessageRepository.save(teamMessagesModel);
        Message message = Message.creator(new PhoneNumber("+".concat(userPhoneNumber)), new PhoneNumber("+".concat(sendingPhoneNumber)), sendingMessageContent).create();
        return message.getBody();
    }

    @PostMapping("/reply")
    public String replyWithApproveRequest(@RequestParam("recipientApproveReply") String approveReply) {
        appStuff.initialiseTwilioService(twilioSid, twilioAuthToken);
        Body messageBody = new Body.Builder("Thank you for your response. Looking forward to having you").build();
        com.twilio.twiml.messaging.Message message = new com.twilio.twiml.messaging.Message.Builder().body(messageBody).build();
        MessagingResponse messagingResponse = new MessagingResponse.Builder().message(message).build();
        return messagingResponse.toXml();
    }

    @PostMapping("/store")
    public String storeSmsMessage(@RequestParam("messageTitle") String messageTitle, @RequestParam("messageString") String messageString) {
        SchedulesMessageModel messageModel = new SchedulesMessageModel();
        messageModel.setMessageTitle(messageTitle);
        messageModel.setMessageContent(messageString);
        messageRepository.save(messageModel);
        return "Message Saved";
    }


}
