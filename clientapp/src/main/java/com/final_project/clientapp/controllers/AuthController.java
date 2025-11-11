package com.final_project.clientapp.controllers;

import com.final_project.clientapp.dto.request.LoginRequest;
import com.final_project.clientapp.dto.request.RegisterRequest;
import com.final_project.clientapp.services.AuthService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public String login(HttpSession session) {
        return "auth/login";
    }

    @PostMapping("/auth/login")
    @ResponseBody
    public ResponseEntity<String> handleLogin(@RequestBody LoginRequest request, HttpSession session) {
        ResponseEntity<String> response = authService.login(request, session);

        return ResponseEntity
                .status(response.getStatusCode())
                .body(response.getBody());
    }

    @GetMapping("/register")
    public String register(HttpSession session) {
        return "auth/register";
    }

    @PostMapping("/auth/register")
    @ResponseBody
    public ResponseEntity<String> handleRegister(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
