package org.example.Controllers;


import com.infobip.ApiCallback;
import com.infobip.ApiException;
import com.infobip.api.SmsApi;
import com.infobip.model.SmsAdvancedTextualRequest;
import com.infobip.model.SmsDestination;
import com.infobip.model.SmsResponse;
import com.infobip.model.SmsTextualMessage;
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
import java.util.Map;

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

    @Value("${infobip.property.api_key}")
    String infobipApiKey;

    @Value("${infobip.property.base_url}")
    String infobipBaseUrl;


    @PostMapping("/send")
    public ResponseEntity<?> sendSmsMessage(@RequestParam("recipientId") String[] recipientUserIdList, @RequestParam("userMessageId") String messageId) {
        SmsApi smsApi = new SmsApi(appStuff.initialiseInfobipService(infobipApiKey, infobipBaseUrl));
        ArrayList<SmsDestination> smsDestinationList = new ArrayList<>();
        for (String recipientUserId : recipientUserIdList) {
            if (teamRepository.findById(recipientUserId).isPresent() && messageRepository.findById(messageId).isPresent()) {
                SmsDestination userDestination = new SmsDestination();
                String userPhoneNumber = teamRepository.findById(recipientUserId).get().getUserPhoneNumber();
                SchedulesTeamMessagesModel teamMessagesModel = new SchedulesTeamMessagesModel();
                teamMessagesModel.setMessageId(messageId);
                teamMessagesModel.setUserId(recipientUserId);
                userDestination.setTo(userPhoneNumber);
                teamMessageRepository.save(teamMessagesModel);
                smsDestinationList.add(userDestination);
            }
        }
        String sendingMessageContent = messageRepository.findById(messageId).get().getMessageContent();
        SmsTextualMessage smsMessage = new SmsTextualMessage().from("TeamDream").destinations(smsDestinationList).text(sendingMessageContent);
        SmsAdvancedTextualRequest smsMessageRequest = new SmsAdvancedTextualRequest()
                .messages(List.of(smsMessage));
        smsApi.sendSmsMessage(smsMessageRequest).executeAsync(new ApiCallback<>() {
            @Override
            public void onSuccess(SmsResponse result, int responseStatusCode, Map<String, List<String>> responseHeaders) {
                System.out.println("Message Sent");
            }

            @Override
            public void onFailure(ApiException exception, int responseStatusCode, Map<String, List<String>> responseHeaders) {
                System.out.println("Message Failed to be Sent");
            }
        });
        return ResponseEntity.status(HttpStatus.OK).body(smsDestinationList);
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
    public ResponseEntity<SchedulesMessageModel> storeSmsMessage(@Valid @RequestParam("messageTitle") String messageTitle, @RequestParam("messageString") String messageString, @ModelAttribute SchedulesTeamModel teamModel, BindingResult bindingResult) {
        SchedulesMessageModel messageModel = new SchedulesMessageModel();
        messageModel.setMessageTitle(messageTitle);
        messageModel.setMessageContent(messageString);
        messageRepository.save(messageModel);
        return ResponseEntity.status(HttpStatus.OK).body(messageModel);
    }

    @PatchMapping("/update/{messageId}")
    public ResponseEntity<SchedulesMessageModel> updateSmsMessage(@PathVariable("messageId") String messageId, @RequestParam(name = "messageTitle", required = false) String messageTitle, @RequestParam(name = "messageContent", required = false) String messageContent) {
        SchedulesMessageModel messageModel = messageRepository.findById(messageId).get();
        if (messageTitle != null) {
            messageModel.setMessageTitle(messageTitle);
        }
        if (messageContent != null) {
            messageModel.setMessageContent(messageContent);
        }
        messageRepository.save(messageModel);
        return ResponseEntity.status(HttpStatus.OK).body(messageModel);
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
