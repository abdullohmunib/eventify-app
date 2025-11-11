package com.final_project.clientapp.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        String nama = (String) session.getAttribute("nama");

        model.addAttribute("user", nama);
        model.addAttribute("isActive", "dashboard");
        return "admin/dashboard";
    }
}
