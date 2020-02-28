package vn.siten.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class StudentFormRequest {
    private String id;
    @NotBlank
    private String fullname;
    @NotBlank
    private String birthDate;
    @NotBlank
    private String idCard;
    @NotBlank
    private String address;
    @NotNull
    private Long facultyId;
    private boolean isMale;
    @NotNull
    private String classname;
    private String avatarPhotoPath;

    public boolean getIsMale() {
        return isMale;
    }

    public void setIsMale(boolean male) {
        isMale = male;
    }
}
