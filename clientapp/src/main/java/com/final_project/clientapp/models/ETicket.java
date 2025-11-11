package com.final_project.clientapp.models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ETicket {
    private Integer ticketId;
    private String kodeUnik;
    private String statusCheckIn;
    private LocalDateTime createdAt;
}