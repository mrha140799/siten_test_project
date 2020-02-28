package vn.siten.backend.service.user;

import vn.siten.backend.model.bean.ResponseBean;
import vn.siten.backend.model.dto.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public interface AdminService {
    ArrayList<TeacherResponse> getListTeacher(String jwtBearer);

    TeacherResponse getTeacherDetailById(Long id);

    ResponseBean deleteTeacher(Long id);

    ResponseBean updateTeacher(TeacherFormRequest teacherFormRequest) throws ParseException;

    ResponseBean createFaculty(FacultyFormRequest facultyFormRequest);

    ResponseBean deleteFaculty(Long id);

    List<FacultyResponse> getListFaculty();

    List<String> getListFacultyName();

    List<String> getListSubjectName();
    List<SubjectForm> getListSubject();
    ResponseBean createSubject(SubjectForm subjectForm);

    ResponseBean deleteSubject(Long id);

    ResponseBean createTimetable(TimetableFormRequest timeTableFormRequest) throws ParseException;

    ResponseBean updateTimetableByStudentIdAndSubjectId(Long studentId, Long subjectId, TimetableFormRequest timetableFormRequest) throws ParseException;
}
