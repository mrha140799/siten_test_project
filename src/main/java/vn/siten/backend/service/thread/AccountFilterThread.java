package vn.siten.backend.service.thread;

import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.siten.backend.model.entity.Account;
import vn.siten.backend.repository.AccountRepository;
import vn.siten.backend.service.jwt.JwtProvider;

import java.util.Date;
import java.util.LinkedList;

@NoArgsConstructor
@Service
public class AccountFilterThread {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private JwtProvider jwtProvider;

    private LinkedList<Account> queue = null;

    private final static Logger log = LoggerFactory.getLogger(AccountFilterThread.class);
    private final static long DEFAULT_SLEEP = 300000;

    @Scheduled(fixedDelay = 1)
    public void startInstance() throws InterruptedException {
        log.info("---> Run in: " + new Date());
        queue = accountRepository.findByEnable(false);
        if (queue == null || queue.size() == 0) {
            log.info("---> Thread sleep in:" + DEFAULT_SLEEP + " ms.");
            Thread.sleep(DEFAULT_SLEEP);
        } else {
            for (Account account : queue) {
                if (!jwtProvider.validateJwtToken(account.getVerificationToken().getToken())) {
                    accountRepository.delete(account);
                    log.info("----> Delete account: " + account.getUsername());
                }
            }
            queue.clear();
            queue = null;
            long sleep = 1000 * 45;
            log.info("---> Thread sleep in:" + sleep + " ms.");
            Thread.sleep(sleep);
        }
    }

}
