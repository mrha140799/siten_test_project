package vn.siten.backend.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data

public class UserRegisterFormRequest {
    @NotBlank
    private String fullname;
    private String birthDate;
    @NotBlank
    private String email;
    private boolean isMale;
    @NotBlank
    private String username;
    private Long facultyId;
    private Long subjectId;
    @NotBlank
    private String password;
    private Set<String> roles;

    public UserRegisterFormRequest() {
        this.facultyId = 0L;
        this.subjectId = 0L;
    }

    public boolean getIsMale() {
        return isMale;
    }

    public void setIsMale(boolean male) {
        isMale = male;
    }
}
