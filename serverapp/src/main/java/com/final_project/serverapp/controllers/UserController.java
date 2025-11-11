package com.final_project.serverapp.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.final_project.serverapp.dto.response.ApiResponse;
import com.final_project.serverapp.dto.response.UserResponse;
import com.final_project.serverapp.models.Role;
import com.final_project.serverapp.services.contracts.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(new ApiResponse<>(true, users, null, "OK"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Integer id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, user, null, "OK"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @RequestParam("namaLengkap") String namaLengkap,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("role") Role role) {
        UserResponse user = userService.createUser(namaLengkap, email, password, role);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, user, null, "User berhasil dibuat"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Integer id,
            @RequestParam("namaLengkap") String namaLengkap,
            @RequestParam("email") String email,
            @RequestParam("role") Role role) {
        UserResponse user = userService.updateUser(id, namaLengkap, email, role);
        return ResponseEntity.ok(new ApiResponse<>(true, user, null, "User berhasil diupdate"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponse<>(true, null, null, "User berhasil dihapus"));
    }
}
