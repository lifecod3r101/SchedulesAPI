package org.example.Misc;

//import com.auth0.jwt.algorithms.Algorithm;

import com.infobip.ApiClient;
import com.infobip.ApiKey;
import com.infobip.BaseUrl;
import com.twilio.Twilio;

public class AppStuff {

    public void initialiseTwilioService(String accountSid, String accountAuthToken) {
        Twilio.init(accountSid, accountAuthToken);
    }

    public ApiClient initialiseInfobipService(String accountApiKey, String accountBaseUrl) {
        return ApiClient.forApiKey(ApiKey.from(accountApiKey))
                .withBaseUrl(BaseUrl.from(accountBaseUrl))
                .build();
    }

//    public Algorithm initialiseJsonWebTokenAlgorithm(){
//        return Algorithm.HMAC256("SchedulesAPI");
//    }

}
