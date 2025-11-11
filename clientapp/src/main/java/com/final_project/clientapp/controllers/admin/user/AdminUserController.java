package com.final_project.clientapp.controllers.admin.user;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.final_project.clientapp.services.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public String index(Model model, HttpSession session) {
        model.addAttribute("isActive", "users");
        return "admin/users/index";
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> create(
            @RequestParam("namaLengkap") String namaLengkap,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("role") String role) {
        
        return userService.create(namaLengkap, email, password, role);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> update(
            @PathVariable Integer id,
            @RequestParam("namaLengkap") String namaLengkap,
            @RequestParam("email") String email,
            @RequestParam("role") String role) {
        
        return userService.update(id, namaLengkap, email, role);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return userService.delete(id);
    }
}
