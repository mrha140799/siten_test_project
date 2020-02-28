package vn.siten.backend.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vn.siten.backend.model.entity.Account;
import vn.siten.backend.repository.AccountRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        String username = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();
        if (!accountRepository.existsByUsername(username)) {
            throw new BadCredentialsException("Username not found!");
        }
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Username not found!"));
        if (account == null) {
            throw new BadCredentialsException("Account is empty!");
        }
        if (!encoder.matches(password, account.getPassword())) {
            throw new BadCredentialsException("Password is incorrect!");
        }
        if (!account.isEnable()) {
            throw new BadCredentialsException("Account is not activated!");
        }
        List<GrantedAuthority> authorities = account.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(username, password, authorities);

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}