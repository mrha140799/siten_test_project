package vn.siten.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectForm {
    private Long id;
    @NotBlank
    private String name;
    @Min(1)
    @Max(10)
    private int creditNumber;
    @NotBlank
    private String facultyName;
}
