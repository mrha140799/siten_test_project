package vn.siten.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherResponse {
    private Long id;
    private String fullname;
    private String birthdate;
    private String idCard;
    private String address;
    private String subjectName;
    private String facultyName;
    private boolean isMale;
}

