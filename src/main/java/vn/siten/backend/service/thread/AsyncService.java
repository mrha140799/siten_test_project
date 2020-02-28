package vn.siten.backend.service.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.siten.backend.model.dto.UserRegisterFormRequest;
import vn.siten.backend.repository.VerificationTokenRepository;
import vn.siten.backend.service.email.EmailSenderService;

import java.util.concurrent.ExecutionException;

@Service
public class AsyncService {
    private static Logger log = LoggerFactory.getLogger(AsyncService.class);

    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Async("asyncEmailExecutor")
    public void waitMilliSecondToSendEmail(UserRegisterFormRequest userRegisterFormRequest, long ms) throws InterruptedException, ExecutionException {
        log.info("Send email starts");
        Thread.sleep(ms);
        String text = "Hello "+ userRegisterFormRequest.getFullname()+"!\nTo confirm your account, please click here : http://localhost:3000/user/confirm-account/token=" + verificationTokenRepository.findByAccount_Username(userRegisterFormRequest.getUsername()).getToken()+"\n\nThank you!";
        log.info(emailSenderService.sendEmail(userRegisterFormRequest.getEmail(), "Confirm Register!", text).get());
        log.info("Send email  completed");
    }

}
