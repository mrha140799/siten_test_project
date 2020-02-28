package vn.siten.backend.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
public class UserUpdateFormRequest {
    @NotBlank
    private String fullname;
    private String birthDate;
    private String idCard;
    private String address;
    @NotBlank
    private String email;
    private Long facultyId;
    private Long subjectId;
    private boolean isMale;
    private String className;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private Set<String> roles;

    public boolean getIsMale() {
        return isMale;
    }

    public void setIsMale(boolean male) {
        isMale = male;
    }
}
