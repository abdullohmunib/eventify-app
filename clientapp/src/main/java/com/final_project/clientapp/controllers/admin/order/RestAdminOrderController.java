package com.final_project.clientapp.controllers.admin.order;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.final_project.clientapp.services.OrderService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/orders")
public class RestAdminOrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Integer id) {
        return orderService.getOrderByIdForAdmin(id);
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<?> confirmPayment(@PathVariable Integer id) {
        return orderService.confirmPayment(id);
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<?> rejectPayment(@PathVariable Integer id) {
        return orderService.rejectPayment(id);
    }
}
