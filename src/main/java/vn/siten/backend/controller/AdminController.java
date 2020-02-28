package vn.siten.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vn.siten.backend.model.bean.ResponseBean;
import vn.siten.backend.model.bean.ResponseErrorBean;
import vn.siten.backend.model.dto.*;
import vn.siten.backend.model.util.MessageUtil;
import vn.siten.backend.service.user.AdminService;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private MessageUtil messageUtil;

    @GetMapping("/ListTeacher")
    public ResponseEntity<ArrayList<TeacherResponse>> listTeacher(@RequestHeader("Authorization") String jwtBearer) {
        return new ResponseEntity<>(adminService.getListTeacher(jwtBearer), HttpStatus.OK);
    }

    @GetMapping("/teacher/{id}")
    public ResponseEntity<TeacherResponse> teacherDetail(@PathVariable("id") Long id) {
        return new ResponseEntity<>(adminService.getTeacherDetailById(id), HttpStatus.OK);
    }

    @PutMapping("/update/teacher")
    public ResponseEntity<ResponseBean> updateTeacher(@Valid @RequestBody TeacherFormRequest teacherFormRequest, BindingResult bindingResult) throws ParseException {
        if (bindingResult.hasFieldErrors()) {
            return new ResponseEntity<>(new ResponseErrorBean(messageUtil.getMessageDefaultEmpty("error.valid")), HttpStatus.OK);
        }
        return new ResponseEntity<>(adminService.updateTeacher(teacherFormRequest), HttpStatus.OK);
    }

    @DeleteMapping("/deleteTeacher/{id}")
    public ResponseEntity<ResponseBean> deleteTeacher(@PathVariable Long id) {
        return new ResponseEntity<>(adminService.deleteTeacher(id), HttpStatus.OK);
    }

    @PostMapping("/createFaculty")
    public ResponseEntity<ResponseBean> createFaculty(@Valid @RequestBody FacultyFormRequest facultyFormRequest, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors())
            return new ResponseEntity<>(new ResponseErrorBean( messageUtil.getMessageDefaultEmpty("error.valid")), HttpStatus.OK);
        return new ResponseEntity<>(adminService.createFaculty(facultyFormRequest), HttpStatus.OK);
    }

    @GetMapping("/listFaculty")
    public ResponseEntity<List<FacultyResponse>> getListFaculty() {
        return new ResponseEntity<>(adminService.getListFaculty(), HttpStatus.OK);
    }

    @GetMapping("/listFacultyName")
    public ResponseEntity<List<String>> getListFacultyName() {
        return new ResponseEntity<>(adminService.getListFacultyName(), HttpStatus.OK);
    }

    @GetMapping("/listSubjectName")
    public ResponseEntity<List<String>> getListSubjectName() {
        return new ResponseEntity<>(adminService.getListSubjectName(), HttpStatus.OK);
    }

    @PostMapping("/createSubject")
    public ResponseEntity<ResponseBean> createSubject(@Valid @RequestBody SubjectForm subjectForm, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors())
            return new ResponseEntity<>(new ResponseErrorBean(messageUtil.getMessageDefaultEmpty("error.valid")), HttpStatus.OK);
        return new ResponseEntity<>(adminService.createSubject(subjectForm), HttpStatus.OK);
    }

    @GetMapping("/listSubject")
    public ResponseEntity<List<SubjectForm>> getListSubject() {
        return new ResponseEntity<>(adminService.getListSubject(), HttpStatus.OK);
    }

    @DeleteMapping("/deleteFaculty/{id}")
    public ResponseEntity<ResponseBean> deleteFaculty(@PathVariable Long id) {
        return new ResponseEntity<>(adminService.deleteFaculty(id), HttpStatus.OK);
    }

    @DeleteMapping("/deleteSubject/{id}")
    public ResponseEntity<ResponseBean> deleteSubject(@PathVariable Long id) {
        return new ResponseEntity<>(adminService.deleteSubject(id), HttpStatus.OK);
    }

    @PostMapping("/createTimetable")
    public ResponseEntity<ResponseBean> createTimetable(@Valid @RequestBody TimetableFormRequest timeTableFormRequest, BindingResult bindingResult) throws ParseException {
        if (bindingResult.hasFieldErrors())
            return new ResponseEntity<>(new ResponseErrorBean(messageUtil.getMessageDefaultEmpty("error.valid")), HttpStatus.OK);
        return new ResponseEntity<>(adminService.createTimetable(timeTableFormRequest), HttpStatus.OK);
    }

    @PutMapping("/updateTimetable/byStudentId={studentId}/SubjectId={subjectId}")
    public ResponseEntity<ResponseBean> updateTimetable(@PathVariable("studentId") Long studentId, @PathVariable("subjectId") Long subjectId, @RequestBody TimetableFormRequest timetableFormRequest) throws ParseException {
        return new ResponseEntity<>(adminService.updateTimetableByStudentIdAndSubjectId(studentId, subjectId, timetableFormRequest), HttpStatus.OK);
    }
}
