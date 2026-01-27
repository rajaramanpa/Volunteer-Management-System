package com.voluntub.controller;

import com.voluntub.dto.LoginRequest;
import com.voluntub.dto.LoginResponse;
import com.voluntub.dto.RegisterRequest;
import com.voluntub.entity.User;
import com.voluntub.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.registerUser(request);
        return ResponseEntity.ok(user);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequest request) {

        return ResponseEntity.ok(userService.login(request));
    }

}
