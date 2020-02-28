package vn.siten.backend.model.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.siten.backend.model.util.Constants;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class ResponseBean implements Serializable {
    private String code;
    private String content;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String message;

    public ResponseBean() {
        this.code = Constants.STATUS_CODE_200;
    }

    public ResponseBean(String content) {
        this.content = content;
    }
}
