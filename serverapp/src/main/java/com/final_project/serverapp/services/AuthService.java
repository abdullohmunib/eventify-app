package com.final_project.serverapp.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.final_project.serverapp.dto.request.LoginRequest;
import com.final_project.serverapp.dto.request.RegisterRequest;
import com.final_project.serverapp.dto.response.ApiResponse;
import com.final_project.serverapp.models.Role;
import com.final_project.serverapp.models.User;
import com.final_project.serverapp.repositories.UserRepository;
import com.final_project.serverapp.security.JwtUtil;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<?> register(RegisterRequest req) {
        try {
            if (userRepository.existsByEmail(req.getEmail())) {
                ApiResponse<Object> res = new ApiResponse<>(
                        false,
                        null,
                        null,
                        "Email sudah terdaftar");

                return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
            }

            User user = new User();
            user.setNamaLengkap(req.getNamaLengkap());
            user.setEmail(req.getEmail());
            user.setPassword(passwordEncoder.encode(req.getPassword()));
            user.setRole(Role.USER); // Set default rolenya ke User

            userRepository.save(user);

            ApiResponse<Object> res = new ApiResponse<>(
                    true,
                    null,
                    null,
                    "Registrasi berhasil");

            return ResponseEntity.ok(res);
        } catch (Exception e) {
            throw new RuntimeException("Terjadi kesalahan saat registrasi");
        }
    }

    public ResponseEntity<?> login(LoginRequest req) {
        try {
            User user = userRepository.findByEmail(req.getEmail()).orElse(null);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(
                                false,
                                null,
                                null,
                                "Kredensial tidak valid"));
            }

            if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(
                                false,
                                null,
                                null,
                                "Kredensial tidak valid"));
            }

            // Generate Token
            String accessToken = jwtUtil.generateAccessToken(user.getEmail());

            Map<String, Object> data = new HashMap<>();
            data.put("access_token", accessToken);
            data.put("nama", user.getNamaLengkap());
            data.put("email", user.getEmail());
            data.put("role", user.getRole());

            ApiResponse<Object> res = new ApiResponse<>(
                    true,
                    data,
                    null,
                    "Login berhasil");

            return ResponseEntity.ok(res);

        } catch (Exception e) {
            throw new RuntimeException("Terjadi kesalahan saat login");
        }
    }

    public ResponseEntity<?> checkLogin(String email) {
        try {
            if (email == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(
                                false,
                                null,
                                null,
                                "User belum login"));
            }

            User user = userRepository.findByEmail(email).orElse(null);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(
                                false,
                                null,
                                null,
                                "Token tidak valid"));
            }

            Map<String, Object> data = new HashMap<>();
            data.put("nama", user.getNamaLengkap());
            data.put("email", user.getEmail());
            data.put("role", user.getRole());

            ApiResponse<Object> res = new ApiResponse<>(
                    true,
                    data,
                    null,
                    "User sudah login");

            return ResponseEntity.ok(res);

        } catch (Exception e) {
            throw new RuntimeException("Gagal mengecek status login");
        }
    }
}
