package com.final_project.clientapp.models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Integer orderId;
    private Integer eventId;
    private Integer userId;
    private Integer quantity;
    private Double totalPrice;
    private String statusPembayaran;
    private String kodeUnikPembayaran;
    private String buktiPembayaran;
    private LocalDateTime createdAt;
}