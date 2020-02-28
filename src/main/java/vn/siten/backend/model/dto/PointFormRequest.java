package vn.siten.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointFormRequest {
    @NotNull
    private Long subjectId;
    @NotNull
    private Long studentId;
    @Min(0)
    @Max(10)
    private float point;
}
