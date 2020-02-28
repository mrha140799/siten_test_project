package vn.siten.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacultyResponse {
    private Long id;
    private String facultyName;
    private String CreatedDate;
}
