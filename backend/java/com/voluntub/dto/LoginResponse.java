package com.voluntub.dto;

import com.voluntub.entity.Role;

public class LoginResponse {

    private Long userId;
    private String name;
    private String email;
    private Role role;

    // --------- Constructors ---------

    public LoginResponse() {
    }

    public LoginResponse(Long userId, String name, String email, Role role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    // --------- Getters ---------

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    // --------- Setters ---------

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
