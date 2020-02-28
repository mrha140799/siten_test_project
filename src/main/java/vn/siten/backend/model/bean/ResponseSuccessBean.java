package vn.siten.backend.model.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import vn.siten.backend.model.util.Constants;

@Data
public class ResponseSuccessBean extends ResponseBean {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data = null;

    public Object getData() {
        return data;
    }

    public ResponseSuccessBean(Object data) {
        super(Constants.STATUS_CODE_200, Constants.CONTENT_200, null);
        this.data = data;
    }

    public ResponseSuccessBean() {
        super();
    }
}
