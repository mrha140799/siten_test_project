package vn.siten.backend.service.otp;


import com.twilio.Twilio;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Random;
@Service
public class TwilioOTP {
    private final static String ACCOUNT_SID = "AC2da96b2af2de0af1eb7ff7aea1917c14";
    private final static String AUTH_TOKEN = "2ef52541fc102d0586e4626d58556d2e";
    private HashMap<String, Integer> otpStorage = new HashMap<>();

    public TwilioOTP() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public Boolean sendOTP(String phoneNumber) {
        Random random = new Random();
        int otpValue = 100000 + random.nextInt(900000);
        otpStorage.put(phoneNumber,otpValue);
        return false;
    }
}
