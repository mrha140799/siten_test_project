package vn.siten.backend.model.bean;

import lombok.Data;
import vn.siten.backend.model.util.Constants;

@Data
public class ResponseErrorBean extends ResponseBean {

    public ResponseErrorBean(String message) {
        super(Constants.STATUS_CODE_500, Constants.CONTENT_500, message);
    }

    public ResponseErrorBean(String code, String content, String message) {
        super(code, content, message);
    }
}
