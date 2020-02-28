package vn.siten.backend.model.dto;

import lombok.Data;

@Data
public class UserLoginFormRequest {
    private String username;
    private String password;
}
