package com.final_project.clientapp.controllers.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.final_project.clientapp.models.DashboardSummary;
import com.final_project.clientapp.services.DashboardService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/dashboard")
public class RestAdminDashboardController {
    
    private final DashboardService dashboardService;
    
    @GetMapping("/summary")
    public DashboardSummary getSummary() {
        return dashboardService.getSummary();
    }
}
