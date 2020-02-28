package vn.siten.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vn.siten.backend.model.bean.ResponseBean;
import vn.siten.backend.model.bean.ResponseErrorBean;
import vn.siten.backend.model.dto.UserLoginFormRequest;
import vn.siten.backend.model.dto.UserRegisterFormRequest;
import vn.siten.backend.model.util.Constants;
import vn.siten.backend.repository.FacultyRepository;
import vn.siten.backend.repository.SubjectRepository;
import vn.siten.backend.service.thread.AsyncService;
import vn.siten.backend.service.user.UserService;

import javax.validation.Valid;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private AsyncService asyncService;
    @PostMapping("/register")
    public ResponseEntity<ResponseBean> userRegister(@Valid @RequestBody UserRegisterFormRequest userRegisterFormRequest, BindingResult bindingResult) throws InterruptedException, ExecutionException {
        if (bindingResult.hasFieldErrors())
            return new ResponseEntity<>(new ResponseErrorBean("value input incorrect!"), HttpStatus.INTERNAL_SERVER_ERROR);
        if (!facultyRepository.existsById(userRegisterFormRequest.getFacultyId())) return new ResponseEntity<>(new ResponseErrorBean("Create user fail! Faculty id Incorrect!"), HttpStatus.INTERNAL_SERVER_ERROR);
        if (!subjectRepository.existsById(userRegisterFormRequest.getSubjectId())) return new ResponseEntity<>(new ResponseErrorBean( "Create user fail! Subject id Incorrect!"), HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseBean responseBean = userService.registerUser(userRegisterFormRequest);
        if (responseBean.getCode().equals(Constants.STATUS_CODE_200)) asyncService.waitMilliSecondToSendEmail(userRegisterFormRequest,30000);
        return new ResponseEntity<>(responseBean, HttpStatus.OK);
    }
    @PutMapping("/confirm-account")
    public ResponseEntity<ResponseBean> confirmAccount(@RequestParam("token") String jwt) {
        return new ResponseEntity<>(userService.confirmUserRegister(jwt),HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseBean> userLogin( @RequestBody UserLoginFormRequest userLoginFormRequest) {
        return new ResponseEntity<>(userService.loginUser(userLoginFormRequest), HttpStatus.OK);
    }

}
