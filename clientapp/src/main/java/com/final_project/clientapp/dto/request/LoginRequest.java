package com.final_project.clientapp.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}