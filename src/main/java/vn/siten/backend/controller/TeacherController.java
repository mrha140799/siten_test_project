package vn.siten.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.siten.backend.model.bean.ResponseBean;
import vn.siten.backend.model.bean.ResponseErrorBean;
import vn.siten.backend.model.bean.ResponseSuccessBean;
import vn.siten.backend.model.dto.PointFormRequest;
import vn.siten.backend.model.dto.StudentFormRequest;
import vn.siten.backend.model.util.MessageUtil;
import vn.siten.backend.service.user.TeacherService;

import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("/teacher")
@CrossOrigin("*")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private MessageUtil messageUtil;
    @GetMapping("/getListStudent")
    public ResponseEntity<ResponseSuccessBean> getListStudent(@RequestHeader("Authorization") String jwt, Pageable pageable) {
        return new ResponseEntity<>(new ResponseSuccessBean(teacherService.getListStudent(jwt, pageable)), HttpStatus.OK);
    }
    @PostMapping("/createStudent")
    public ResponseEntity<ResponseBean> createStudent(@RequestParam("student") String studentFormRequest, @RequestParam("avatarPhoto") MultipartFile avatarPhoto) throws ParseException, JsonProcessingException {
        return new ResponseEntity<>(teacherService.createStudent(studentFormRequest, avatarPhoto), HttpStatus.CREATED);
    }
    @PutMapping("/updateStudent/{id}")
    public  ResponseEntity<ResponseBean> updateStudent(@RequestBody StudentFormRequest studentFormRequest, @PathVariable Long id) {
        return new ResponseEntity<>(teacherService.updateStudent(id,studentFormRequest), HttpStatus.OK);
    }
    @DeleteMapping("/deleteStudent/{id}")
    public ResponseEntity<ResponseBean> deleteStudent(@PathVariable Long id) {
        return new ResponseEntity<>(teacherService.deleteStudent(id), HttpStatus.OK);
    }
    @PostMapping("/addPoint")
    public  ResponseEntity<ResponseBean> addPoint(@Valid @RequestBody PointFormRequest pointFormRequest, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) return new ResponseEntity<>(new ResponseErrorBean(messageUtil.getMessageDefaultEmpty("error.valid")),HttpStatus.OK);
        return new ResponseEntity<>(teacherService.addPoint(pointFormRequest),HttpStatus.OK);
    }
    @PostMapping("/import/excelStudent")
    public ResponseEntity<ResponseBean> mapReapExcelDataToDB(@RequestParam("file")MultipartFile excelFile) throws IOException {
        return new ResponseEntity<>(teacherService.uploadExcelStudentToDatabase(excelFile),HttpStatus.OK);
    }
}
