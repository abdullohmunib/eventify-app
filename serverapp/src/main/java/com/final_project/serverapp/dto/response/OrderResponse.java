package com.final_project.serverapp.dto.response;

import java.time.LocalDateTime;

import com.final_project.serverapp.models.StatusPembayaran;

public record OrderResponse(
        Integer orderId,
        Integer eventId,
        Integer userId,
        Integer quantity,
        Double totalPrice,
        StatusPembayaran statusPembayaran,
        String buktiPembayaran,
        LocalDateTime createdAt) {}
