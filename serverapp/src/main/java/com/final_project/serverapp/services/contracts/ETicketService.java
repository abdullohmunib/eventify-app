package com.final_project.serverapp.services.contracts;

import java.util.List;

import com.final_project.serverapp.dto.response.ETicketResponse;

public interface ETicketService {
    List<ETicketResponse> getAllTickets();
    ETicketResponse getTicketById(Integer ticketId);
    ETicketResponse useTicket(Integer ticketId);
    ETicketResponse unuseTicket(Integer ticketId);
}
