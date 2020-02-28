package vn.siten.backend.service.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.siten.backend.model.TimetableId;
import vn.siten.backend.model.bean.ResponseBean;
import vn.siten.backend.model.bean.ResponseErrorBean;
import vn.siten.backend.model.bean.ResponseSuccessBean;
import vn.siten.backend.model.entity.*;
import vn.siten.backend.model.dto.*;
import vn.siten.backend.model.util.MessageUtil;
import vn.siten.backend.repository.*;
import vn.siten.backend.service.jwt.JwtProvider;
import vn.siten.backend.service.user.AdminService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private TimetableRepository timeTableRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private MessageUtil messageUtil;

    private final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public ArrayList<TeacherResponse> getListTeacher(String jwtBearer) {
        String token = jwtBearer.replace("Bearer ", "");
        String username = jwtProvider.getUserNameFromJwtToken(token);
        ArrayList<TeacherResponse> teacherResponses = new ArrayList<>();
        Iterable<Teacher> listTeacherFormUsername = teacherRepository.findAllByAccount_UsernameNotLike(username);
        for (Teacher teacher : listTeacherFormUsername) {
            TeacherResponse teacherResponse = convertTeacherToTeacherResponse(teacher);
            teacherResponses.add(teacherResponse);
        }
        return teacherResponses;
    }

    @Override
    public TeacherResponse getTeacherDetailById(Long id) {
        return convertTeacherToTeacherResponse(teacherRepository.findById(id).orElseThrow(() -> new RuntimeException("Can not find teacher with id:" + id)));
    }

    @Override
    public ResponseBean deleteTeacher(Long id) {
        if (!teacherRepository.existsById(id))
            return new ResponseErrorBean(messageUtil.getMessageDefaultEmpty("error.teacher.id.not.exist"));
        Teacher teacher = teacherRepository.findById(id).get();
        teacherRepository.delete(teacher);
        return new ResponseSuccessBean();
    }

    @Override
    public ResponseBean updateTeacher(TeacherFormRequest teacherFormRequest) throws ParseException {
        if (!teacherRepository.existsById(teacherFormRequest.getId()))
            return new ResponseErrorBean(messageUtil.getMessageDefaultEmpty("error.teacher.id.not.exist"));
        try {

            Teacher teacher = teacherRepository.findById(teacherFormRequest.getId()).get();
            Subject subject = subjectRepository.findByNameIgnoreCase(teacherFormRequest.getSubjectName());
            Faculty faculty = facultyRepository.findByFacultyNameIgnoreCase(teacherFormRequest.getFacultyName());
            teacher.setFullname(teacherFormRequest.getFullname());
            String birthDateStr = teacherFormRequest.getBirthdate();
            Date birthDate = SIMPLE_DATE_FORMAT.parse(birthDateStr);
            teacher.setBirthdate(birthDate);
            teacher.setAddress(teacherFormRequest.getAddress());
            teacher.setIdCard(teacherFormRequest.getIdCard());
            teacher.setMale(teacherFormRequest.getIsMale());
            teacher.setFaculty(faculty);
            teacher.setSubject(subject);
            teacherRepository.save(teacher);
            return new ResponseSuccessBean();
        } catch (Exception e) {
            return new ResponseErrorBean(e.getMessage());
        }
    }

    @Override
    public ResponseBean createFaculty(FacultyFormRequest facultyFormRequest) {
        try {
            facultyRepository.save(new Faculty(facultyFormRequest.getFacultyName(), new Date()));
            return new ResponseSuccessBean();
        } catch (Exception e) {
            return new ResponseErrorBean(e.getMessage());
        }
    }

    @Override
    public ResponseBean deleteFaculty(Long id) {
        if (!facultyRepository.existsById(id))
            return new ResponseErrorBean(messageUtil.getMessageDefaultEmpty("error.faculty.id.not.exist"));
        facultyRepository.deleteById(id);
        return new ResponseSuccessBean();
    }

    @Override
    public List<FacultyResponse> getListFaculty() {
        Iterable<Faculty> listFaculty = facultyRepository.findAllByIdNotLike(0L);
        List<FacultyResponse> facultyResponses = new ArrayList<>();
        for (Faculty faculty : listFaculty) {
            FacultyResponse facultyResponse = new FacultyResponse(faculty.getId(), faculty.getFacultyName(), SIMPLE_DATE_FORMAT.format(faculty.getCreatedDate()));
            facultyResponses.add(facultyResponse);
        }
        return facultyResponses;
    }

    @Override
    public List<String> getListFacultyName() {
        List<String> listFacultyName = new ArrayList<>();
        Iterable<Faculty> faculties = facultyRepository.findAllByIdNotLike(0L);
        for (Faculty faculty : faculties) listFacultyName.add(faculty.getFacultyName());
        return listFacultyName;
    }

    @Override
    public List<String> getListSubjectName() {
        List<String> listSubjectName = new ArrayList<>();
        Iterable<Subject> subjects = subjectRepository.findAllByIdNotLike(0L);
        for (Subject subject : subjects) listSubjectName.add(subject.getName());
        return listSubjectName;
    }

    @Override
    public List<SubjectForm> getListSubject() {
        Iterable<Subject> subjects = subjectRepository.findAllByIdNotLike(0L);
        List<SubjectForm> listSubjectForm = new ArrayList<>();
        for (Subject subject : subjects) {
            SubjectForm subjectForm = new SubjectForm(subject.getId(), subject.getName(), subject.getCreditNumber(), subject.getFaculty().getFacultyName());
            listSubjectForm.add(subjectForm);
        }
        return listSubjectForm;
    }

    @Override
    public ResponseBean createSubject(SubjectForm subjectForm) {
        try {
            if (!facultyRepository.existsByFacultyNameIgnoreCase(subjectForm.getFacultyName()))
                return new ResponseErrorBean(messageUtil.getMessageDefaultEmpty("error.faculty.name.not.exist"));
            Faculty faculty = facultyRepository.findByFacultyNameIgnoreCase(subjectForm.getFacultyName());
            Subject subject = new Subject(subjectForm.getName(), subjectForm.getCreditNumber(), faculty);
            subjectRepository.save(subject);
            return new ResponseSuccessBean(subjectForm);
        } catch (Exception e) {
            return new ResponseErrorBean(e.getMessage());
        }
    }

    @Override
    public ResponseBean deleteSubject(Long id) {
        if (!subjectRepository.existsById(id))
            return new ResponseErrorBean(messageUtil.getMessageDefaultEmpty("error.subject.id.not.exist"));
        Subject subject = subjectRepository.findById(id).get();
        List<Teacher> teachers = subject.getTeachers();
        Subject subjectUndefined = subjectRepository.findById(0L).get();
        for (Teacher teacher : teachers) {
            teacher.setSubject(subjectUndefined);
            teacherRepository.save(teacher);
        }
        subjectRepository.delete(subject);
        return new ResponseSuccessBean();
    }

    @Override
    public ResponseBean createTimetable(TimetableFormRequest timeTableFormRequest) throws ParseException {
        if (!validateTimetableFormRequest(timeTableFormRequest))
            return new ResponseErrorBean("Create timetable fail. Student id or teacher id not fount!");
        Student student = studentRepository.findById(timeTableFormRequest.getStudentId()).get();
        Teacher teacher = teacherRepository.findById(timeTableFormRequest.getTeacherId()).get();
        Date startDate = SIMPLE_DATE_FORMAT.parse(timeTableFormRequest.getStartDate());
        Date endDate = SIMPLE_DATE_FORMAT.parse(timeTableFormRequest.getEndDate());
        TimetableId timetableId = new TimetableId(teacher, student);

        Timetable timetable = new Timetable(timetableId, timeTableFormRequest.getDay(), timeTableFormRequest.getClassName(), startDate, endDate);
        timeTableRepository.save(timetable);
        return new ResponseSuccessBean();
    }

    @Override
    public ResponseBean updateTimetableByStudentIdAndSubjectId(Long studentId, Long
            subjectId, TimetableFormRequest timeTableFormRequest) throws ParseException {
        if (!validateTimetableFormRequest(timeTableFormRequest))
            return new ResponseErrorBean("Create timetable fail. Student id or teacher id not fount!");
        Timetable timetable = timeTableRepository.findByTimetableId_Student_IdAndTimetableId_Teacher_Subject_Id(studentId, subjectId);
        TimetableId timetableId = timetable.getTimetableId();
        timeTableRepository.deleteById(timetableId);
        Teacher teacher = teacherRepository.findBySubject_Id(studentId);
        timetableId.setTeacher(teacher);
        timetable.setClassName(timeTableFormRequest.getClassName());
        timetable.setDay(timeTableFormRequest.getDay());
        Date startDate = SIMPLE_DATE_FORMAT.parse(timeTableFormRequest.getStartDate());
        Date endDate = SIMPLE_DATE_FORMAT.parse(timeTableFormRequest.getEndDate());
        timetable.setEndDate(endDate);
        timetable.setStartDate(startDate);
        timeTableRepository.save(timetable);
        return new ResponseSuccessBean(timeTableFormRequest);
    }

    private boolean validateTimetableFormRequest(TimetableFormRequest timetableFormRequest) {
        boolean result = false;
        if (timetableFormRequest != null && studentRepository.existsById(timetableFormRequest.getStudentId()) && teacherRepository.existsById(timetableFormRequest.getTeacherId()))
            result = true;
        return result;
    }

    private TeacherResponse convertTeacherToTeacherResponse(Teacher teacher) {
        return new TeacherResponse(teacher.getId(), teacher.getFullname(), SIMPLE_DATE_FORMAT.format(teacher.getBirthdate()), teacher.getIdCard(), teacher.getAddress(), teacher.getSubject().getName(), teacher.getFaculty().getFacultyName(), teacher.isMale());
    }

}
