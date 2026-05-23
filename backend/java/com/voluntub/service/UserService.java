package com.voluntub.service;

import com.voluntub.dto.LoginRequest;
import com.voluntub.dto.LoginResponse;
import com.voluntub.dto.RegisterRequest;
import com.voluntub.entity.*;

public interface UserService {
    User registerUser(RegisterRequest request);
    LoginResponse login(LoginRequest request);

}
