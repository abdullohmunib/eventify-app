package com.final_project.serverapp.services.implementation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.final_project.serverapp.dto.response.ETicketResponse;
import com.final_project.serverapp.exceptions.BadRequestException;
import com.final_project.serverapp.exceptions.NotFoundException;
import com.final_project.serverapp.models.CheckInStatus;
import com.final_project.serverapp.models.ETicket;
import com.final_project.serverapp.repositories.ETicketRepository;
import com.final_project.serverapp.services.contracts.ETicketService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ETicketServiceImpl implements ETicketService {
    private final ETicketRepository eTicketRepository;

    @Override
    public List<ETicketResponse> getAllTickets() {
        return eTicketRepository.findAll().stream()
                .map(this::toTicketResponse)
                .toList();
    }

    @Override
    public ETicketResponse getTicketById(Integer ticketId) {
        ETicket ticket = eTicketRepository.findById(ticketId)
                .orElseThrow(() -> new NotFoundException("E-ticket tidak ditemukan"));
        return toTicketResponse(ticket);
    }

    @Override
    public ETicketResponse useTicket(Integer ticketId) {
        ETicket ticket = eTicketRepository.findById(ticketId)
                .orElseThrow(() -> new NotFoundException("E-ticket tidak ditemukan"));

        if (ticket.getStatusCheckIn() == CheckInStatus.USED) {
            throw new BadRequestException("E-ticket sudah pernah digunakan");
        }

        ticket.setStatusCheckIn(CheckInStatus.USED);
        eTicketRepository.save(ticket);

        return toTicketResponse(ticket);
    }

    @Override
    public ETicketResponse unuseTicket(Integer ticketId) {
        ETicket ticket = eTicketRepository.findById(ticketId)
                .orElseThrow(() -> new NotFoundException("E-ticket tidak ditemukan"));

        if (ticket.getStatusCheckIn() == CheckInStatus.NOT_USED) {
            throw new BadRequestException("E-ticket belum pernah digunakan");
        }

        ticket.setStatusCheckIn(CheckInStatus.NOT_USED);
        eTicketRepository.save(ticket);

        return toTicketResponse(ticket);
    }

    private ETicketResponse toTicketResponse(ETicket ticket) {
        return new ETicketResponse(
                ticket.getTicketId(),
                ticket.getKodeUnik(),
                ticket.getStatusCheckIn(),
                ticket.getCreatedAt());
    }
}
