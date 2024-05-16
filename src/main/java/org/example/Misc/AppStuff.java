package org.example.Misc;

import com.auth0.jwt.algorithms.Algorithm;
import com.twilio.Twilio;

public class AppStuff {

    public void initialiseTwilioService(String accountSid, String accountAuthToken) {
        Twilio.init(accountSid, accountAuthToken);
    }

    public Algorithm initialiseJsonWebTokenAlgorithm(){
        return Algorithm.HMAC256("SchedulesAPI");
    }

}
