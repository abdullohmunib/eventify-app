package com.final_project.serverapp.services.implementation;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.final_project.serverapp.dto.response.UserResponse;
import com.final_project.serverapp.exceptions.BadRequestException;
import com.final_project.serverapp.exceptions.NotFoundException;
import com.final_project.serverapp.models.Role;
import com.final_project.serverapp.models.User;
import com.final_project.serverapp.repositories.UserRepository;
import com.final_project.serverapp.services.contracts.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toUserResponse)
                .toList();
    }

    @Override
    public UserResponse getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User tidak ditemukan"));
        return toUserResponse(user);
    }

    @Override
    public UserResponse createUser(String namaLengkap, String email, String password, Role role) {
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email sudah terdaftar");
        }

        User user = new User();
        user.setNamaLengkap(namaLengkap);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);

        userRepository.save(user);
        return toUserResponse(user);
    }

    @Override
    public UserResponse updateUser(Integer id, String namaLengkap, String email, Role role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User tidak ditemukan"));

        // Check if email is already used by another user
        if (!user.getEmail().equals(email) && userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email sudah digunakan oleh user lain");
        }

        user.setNamaLengkap(namaLengkap);
        user.setEmail(email);
        user.setRole(role);

        userRepository.save(user);
        return toUserResponse(user);
    }

    @Override
    public void deleteUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User tidak ditemukan"));
        userRepository.delete(user);
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getNamaLengkap(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }
}
