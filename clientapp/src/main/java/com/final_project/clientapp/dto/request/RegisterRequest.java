package com.final_project.clientapp.dto.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String namaLengkap;
    private String email;
    private String password;
}