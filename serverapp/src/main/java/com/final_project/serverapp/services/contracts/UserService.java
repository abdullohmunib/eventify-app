package com.final_project.serverapp.services.contracts;

import java.util.List;

import com.final_project.serverapp.dto.response.UserResponse;
import com.final_project.serverapp.models.Role;

public interface UserService {
    List<UserResponse> getAllUsers();
    UserResponse getUserById(Integer id);
    UserResponse createUser(String namaLengkap, String email, String password, Role role);
    UserResponse updateUser(Integer id, String namaLengkap, String email, Role role);
    void deleteUser(Integer id);
}
