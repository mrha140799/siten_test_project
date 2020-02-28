package vn.siten.backend.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import vn.siten.backend.model.bean.ResponseBean;
import vn.siten.backend.model.dto.PointFormRequest;
import vn.siten.backend.model.dto.StudentFormRequest;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface TeacherService {
    ResponseBean createStudent(String jsonStudentFormStr, MultipartFile avatarPhoto) throws ParseException, JsonProcessingException;
    ResponseBean addPoint(PointFormRequest pointFormRequest);
    ResponseBean updateStudent(Long id, StudentFormRequest studentFormRequest);
    ResponseBean deleteStudent(Long id);
    List<StudentFormRequest> getListStudent(String jwt, Pageable pageable);
    ResponseBean uploadExcelStudentToDatabase(MultipartFile excelFile) throws IOException;
}
