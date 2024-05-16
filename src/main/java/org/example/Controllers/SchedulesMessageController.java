package org.example.Controllers;


import com.twilio.rest.api.v2010.account.Message;
import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.type.PhoneNumber;
import jakarta.validation.Valid;
import org.example.Misc.AppStuff;
import org.example.Models.SchedulesMessageModel;
import org.example.Models.SchedulesTeamMessagesModel;
import org.example.Models.SchedulesTeamModel;
import org.example.Repositories.SchedulesMessageRepository;
import org.example.Repositories.SchedulesTeamMessageRepository;
import org.example.Repositories.SchedulesTeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    public String sendSmsMessage(@RequestParam("recipientId") String recipientUserId, @RequestParam("userMessageId") String messageId) {
        appStuff.initialiseTwilioService(twilioSid, twilioAuthToken);
        String userPhoneNumber = teamRepository.findById(recipientUserId).get().getUserPhoneNumber();
        String sendingMessageContent = messageRepository.findById(messageId).get().getMessageContent();
        SchedulesTeamMessagesModel teamMessagesModel = new SchedulesTeamMessagesModel();
        teamMessagesModel.setMessageId(messageId);
        teamMessagesModel.setUserId(recipientUserId);
        teamMessageRepository.save(teamMessagesModel);
        Message message = Message.creator(new PhoneNumber("+".concat(userPhoneNumber)), new PhoneNumber("+".concat(sendingPhoneNumber)), sendingMessageContent).create();
        return message.getBody();
    }

    //Method is to be revisited
    @PostMapping("/reply")
    public String replyWithApproveRequest(@RequestParam("recipientApproveReply") String approveReply) {
        appStuff.initialiseTwilioService(twilioSid, twilioAuthToken);
        String xmlResponse = "";
        if (approveReply.equals("Yes")) {
            Body messageBody = new Body.Builder("Thank you for your response. Looking forward to having you").build();

            com.twilio.twiml.messaging.Message message = new com.twilio.twiml.messaging.Message.Builder().body(messageBody).build();
            MessagingResponse messagingResponse = new MessagingResponse.Builder().message(message).build();
            xmlResponse = messagingResponse.toXml();
        }
        return xmlResponse;
    }

    @PostMapping("/store")
    public ResponseEntity<String> storeSmsMessage(@Valid @RequestParam("messageTitle") String messageTitle, @RequestParam("messageString") String messageString, @ModelAttribute SchedulesTeamModel teamModel, BindingResult bindingResult) {
        SchedulesMessageModel messageModel = new SchedulesMessageModel();
        messageModel.setMessageTitle(messageTitle);
        messageModel.setMessageContent(messageString);
        messageRepository.save(messageModel);
        return ResponseEntity.status(HttpStatus.OK).body("Message Saved");
    }

    @PatchMapping("/update/{messageId}")
    public String updateSmsMessage(@PathVariable("messageId") String messageId, @RequestParam(name = "messageTitle", required = false) String messageTitle, @RequestParam(name = "messageContent", required = false) String messageContent) {
        SchedulesMessageModel messageModel = messageRepository.findById(messageId).get();
        if (messageTitle != null) {
            messageModel.setMessageTitle(messageTitle);
        }
        if (messageContent != null) {
            messageModel.setMessageContent(messageContent);
        }
        messageRepository.save(messageModel);
        return "Message Updated";
    }

    @DeleteMapping("/delete/{messageId}")
    public String deleteSmsMessage(@PathVariable("messageId") String messageId) {
        SchedulesMessageModel messageModel = messageRepository.findById(messageId).get();
        messageRepository.delete(messageModel);
        return "Message Removed";
    }

    @GetMapping("/getAll")
    public List<SchedulesMessageModel> getAllMessages() {
        List<SchedulesMessageModel> messageModelsList = new ArrayList<>();
        for (SchedulesMessageModel model : messageRepository.findAll()) {
            messageModelsList.add(model);
        }
        return messageModelsList;
    }

}
