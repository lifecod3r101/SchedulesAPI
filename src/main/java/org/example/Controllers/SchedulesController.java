package org.example.Controllers;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.example.Misc.AppStuff;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SchedulesController {
    AppStuff appStuff = new AppStuff();

    @Value("${twilio.property.account_sid}")
    String twilioSid;

    @Value("${twilio.property.auth_token}")
    String twilioAuthToken;


    @PostMapping("/message/send")
    public String sendSmsMessage(@RequestParam("recipientPhoneNumber") String recipientPhoneNumber) {
        appStuff.initialiseTwilioService(twilioSid, twilioAuthToken);
        Message message = Message.creator(new PhoneNumber("+".concat(recipientPhoneNumber)), new PhoneNumber("+12562903806"), "A test to see something").create();
        return message.getBody();
    }

}
