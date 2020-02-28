package vn.siten.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
public class TeacherFormRequest {
    private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private Long id;
    @NotBlank
    private String fullname;
    @NotBlank
    @Pattern(regexp = "^[0-9]{2}[/][0-9]{2}[/][1-9]{4}$") //dd/MM/yyyy
    private String birthdate;
    @NotBlank
    private String idCard;
    @NotBlank
    private String address;
    @NotBlank
    private String facultyName;
    @NotBlank
    private String subjectName;
    private boolean isMale;

    public boolean getIsMale() {
        return isMale;
    }

    public void setIsMale(boolean male) {
        isMale = male;
    }

    public TeacherFormRequest(Long id, String fullname, Date birthdate, String idCard, String address, boolean isMale) {
        this.id = id;
        this.fullname = fullname;
        this.birthdate = simpleDateFormat.format(birthdate);
        this.idCard = idCard;
        this.address = address;
        this.isMale = isMale;
    }
}
