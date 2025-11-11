package com.final_project.serverapp.dto.response;

import java.time.LocalDateTime;

import com.final_project.serverapp.models.Role;

public record UserResponse(
        Integer id,
        String namaLengkap,
        String email,
        Role role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
