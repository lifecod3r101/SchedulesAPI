package org.example.Misc;

import com.twilio.Twilio;

public class AppStuff {

    public void initialiseTwilioService(String accountSid, String accountAuthToken) {
        Twilio.init(accountSid, accountAuthToken);
    }

}
