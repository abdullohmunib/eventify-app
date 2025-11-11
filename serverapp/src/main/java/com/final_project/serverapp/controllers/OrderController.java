package com.final_project.serverapp.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.final_project.serverapp.dto.request.OrderRequest;
import com.final_project.serverapp.dto.response.ApiResponse;
import com.final_project.serverapp.dto.response.ETicketResponse;
import com.final_project.serverapp.dto.response.OrderResponse;
import com.final_project.serverapp.services.contracts.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/events/{eventId}")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@PathVariable Integer eventId,
            @Valid @RequestBody OrderRequest request) {
        OrderResponse response = orderService.createOrder(eventId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, response, null, "Order dibuat, menunggu pembayaran"));
    }

    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<ApiResponse<OrderResponse>> confirmPayment(@PathVariable Integer orderId) {
        OrderResponse response = orderService.confirmPayment(orderId);
        return ResponseEntity
                .ok(new ApiResponse<>(true, response, null, "Pembayaran dikonfirmasi dan e-ticket terbit"));
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(@PathVariable Integer orderId) {
        OrderResponse response = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(new ApiResponse<>(true, response, null, "Order dibatalkan"));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@PathVariable Integer orderId) {
        OrderResponse response = orderService.getOrderById(orderId);
        return ResponseEntity.ok(new ApiResponse<>(true, response, null, "OK"));
    }

    @GetMapping("/{orderId}/tickets")
    public ResponseEntity<ApiResponse<List<ETicketResponse>>> getTickets(@PathVariable Integer orderId) {
        List<ETicketResponse> tickets = orderService.getTicketsByOrder(orderId);
        return ResponseEntity.ok(new ApiResponse<>(true, tickets, null, "OK"));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getMyOrders() {
        List<OrderResponse> orders = orderService.getOrdersForCurrentUser();
        return ResponseEntity.ok(new ApiResponse<>(true, orders, null, "OK"));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(new ApiResponse<>(true, orders, null, "OK"));
    }
    
    @PostMapping("/{orderId}/upload-payment-proof")
    public ResponseEntity<ApiResponse<OrderResponse>> uploadPaymentProof(
            @PathVariable Integer orderId,
            @RequestParam("file") MultipartFile file) {
        OrderResponse response = orderService.uploadPaymentProof(orderId, file);
        return ResponseEntity.ok(new ApiResponse<>(true, response, null, "Bukti pembayaran berhasil diupload"));
    }
}
