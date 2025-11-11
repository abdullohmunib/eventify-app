package com.final_project.serverapp.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.final_project.serverapp.dto.response.ApiResponse;
import com.final_project.serverapp.dto.response.ETicketResponse;
import com.final_project.serverapp.services.contracts.ETicketService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/etickets")
@RequiredArgsConstructor
public class ETicketController {
    private final ETicketService eTicketService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ETicketResponse>>> getAllTickets() {
        List<ETicketResponse> tickets = eTicketService.getAllTickets();
        return ResponseEntity.ok(new ApiResponse<>(true, tickets, null, "OK"));
    }

    @GetMapping("/{ticketId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ETicketResponse>> getTicketById(@PathVariable Integer ticketId) {
        ETicketResponse ticket = eTicketService.getTicketById(ticketId);
        return ResponseEntity.ok(new ApiResponse<>(true, ticket, null, "OK"));
    }

    @PatchMapping("/{ticketId}/use")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ETicketResponse>> useTicket(@PathVariable Integer ticketId) {
        ETicketResponse ticket = eTicketService.useTicket(ticketId);
        return ResponseEntity.ok(new ApiResponse<>(true, ticket, null, "E-ticket berhasil digunakan"));
    }

    @PatchMapping("/{ticketId}/unuse")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ETicketResponse>> unuseTicket(@PathVariable Integer ticketId) {
        ETicketResponse ticket = eTicketService.unuseTicket(ticketId);
        return ResponseEntity.ok(new ApiResponse<>(true, ticket, null, "E-ticket berhasil dikembalikan ke status tidak terpakai"));
    }
}
