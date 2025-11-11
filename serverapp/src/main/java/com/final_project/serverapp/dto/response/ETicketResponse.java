package com.final_project.serverapp.dto.response;

import java.time.LocalDateTime;

import com.final_project.serverapp.models.CheckInStatus;

public record ETicketResponse(
        Integer ticketId,
        String kodeUnik,
        CheckInStatus statusCheckIn,
        LocalDateTime createdAt) {

}
