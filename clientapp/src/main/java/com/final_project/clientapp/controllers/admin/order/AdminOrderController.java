package com.final_project.clientapp.controllers.admin.order;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/orders")
public class AdminOrderController {

    @GetMapping
    public String index(Model model) {
        model.addAttribute("isActive", "orders");
        return "admin/orders/index";
    }
}
