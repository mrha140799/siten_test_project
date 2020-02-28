package vn.siten.backend.service.user;

import vn.siten.backend.model.bean.ResponseBean;
import vn.siten.backend.model.dto.JwtResponse;
import vn.siten.backend.model.dto.UserLoginFormRequest;
import vn.siten.backend.model.dto.UserRegisterFormRequest;

public interface UserService {
    ResponseBean registerUser(UserRegisterFormRequest userRegisterFormRequest);
    ResponseBean loginUser(UserLoginFormRequest userLoginFormRequest);
    ResponseBean confirmUserRegister(String jwt);
}
