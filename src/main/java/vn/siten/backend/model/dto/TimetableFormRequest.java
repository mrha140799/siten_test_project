package vn.siten.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimetableFormRequest {
    private Long teacherId;
    @Min(2)
    @Max(6)
    private int day;
    private String className;
    private String startDate;
    private String endDate;
    private Long studentId;
}
