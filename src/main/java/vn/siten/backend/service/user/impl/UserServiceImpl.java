package vn.siten.backend.service.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.siten.backend.model.RoleName;
import vn.siten.backend.model.bean.ResponseErrorBean;
import vn.siten.backend.model.bean.ResponseSuccessBean;
import vn.siten.backend.model.entity.*;
import vn.siten.backend.model.bean.ResponseBean;
import vn.siten.backend.model.dto.JwtResponse;
import vn.siten.backend.model.dto.UserLoginFormRequest;
import vn.siten.backend.model.dto.UserRegisterFormRequest;
import vn.siten.backend.model.util.MessageUtil;
import vn.siten.backend.repository.*;
import vn.siten.backend.service.jwt.JwtProvider;
import vn.siten.backend.service.security.CustomAuthenticationProvider;
import vn.siten.backend.service.user.UserService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomAuthenticationProvider authenticationProvider;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private MessageUtil messageUtil;

    private final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public ResponseBean registerUser(UserRegisterFormRequest userRegisterFormRequest) {
        try {
            if (accountRepository.existsByUsername(userRegisterFormRequest.getUsername()))
                return new ResponseErrorBean(messageUtil.getMessageDefaultEmpty("error.username.exist"));
            Set<Role> roles = new HashSet<>();
            Set<String> strRoleFromRequest = userRegisterFormRequest.getRoles();
            for (String role : strRoleFromRequest) {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("FAIL: USER ROLE IS NOT FOUNT!"));
                        roles.add(adminRole);
                        break;
                    default:
                        Role teacherRole = roleRepository.findByName(RoleName.ROLE_TEACHER).orElseThrow(() -> new RuntimeException("FAIL: USER ROLE IS NOT FOUNT!"));
                        roles.add(teacherRole);
                        break;
                }
            }
            Date date = SIMPLE_DATE_FORMAT.parse(userRegisterFormRequest.getBirthDate());
            Faculty faculty = facultyRepository.findById(userRegisterFormRequest.getFacultyId()).orElseThrow(() -> new RuntimeException("FACULTY NOT FOUNT."));
            Subject subject = subjectRepository.findById(userRegisterFormRequest.getSubjectId()).orElseThrow(() -> new RuntimeException("FACULTY NOT FOUNT."));
            Teacher teacher = new Teacher(userRegisterFormRequest.getFullname(), date, null, null, userRegisterFormRequest.getEmail(), faculty, subject, userRegisterFormRequest.getIsMale(), null);
            Account account = new Account(userRegisterFormRequest.getUsername(), passwordEncoder.encode(userRegisterFormRequest.getPassword()), teacher, roles);
            String jwt = jwtProvider.generateTokenByUsername(userRegisterFormRequest.getUsername());
            VerificationToken verificationToken = new VerificationToken(jwt, account);
            verificationTokenRepository.save(verificationToken);
            return new ResponseSuccessBean(userRegisterFormRequest);
        } catch (Exception e) {
            return new ResponseErrorBean(e.getMessage());
        }
    }


    @Override
    public ResponseBean loginUser(UserLoginFormRequest userLoginFormRequest) {
        Authentication authentication = authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(userLoginFormRequest.getUsername(), userLoginFormRequest.getPassword()));
        VerificationToken verificationToken = verificationTokenRepository.findByAccount_Username(userLoginFormRequest.getUsername());
        String jwt = verificationToken.getToken();
        if (!jwtProvider.validateJwtToken(jwt)) {
            jwt = jwtProvider.generateTokenByAuthentication(authentication);
            verificationToken.setToken(jwt);
            verificationTokenRepository.save(verificationToken);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseSuccessBean(new JwtResponse(jwt, authentication.getName(), authentication.getAuthorities()));
    }

    @Override
    public ResponseBean confirmUserRegister(String jwt) {
        if (!jwtProvider.validateJwtToken(jwt) || jwt == null)
            return new ResponseErrorBean(messageUtil.getMessageDefaultEmpty("error.token.valid"));
        try {
            String username = jwtProvider.getUserNameFromJwtToken(jwt);
            Account account = accountRepository.findByUsername(username).get();
            account.setEnable(true);
            accountRepository.save(account);
            return new ResponseSuccessBean();
        } catch (Exception e) {
            return new ResponseErrorBean(e.getMessage());
        }
    }

}
