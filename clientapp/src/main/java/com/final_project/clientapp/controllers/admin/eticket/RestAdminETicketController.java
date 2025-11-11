package com.final_project.clientapp.controllers.admin.eticket;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.final_project.clientapp.models.ETicket;
import com.final_project.clientapp.services.ETicketService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/etickets")
public class RestAdminETicketController {

    private final ETicketService eTicketService;

    @GetMapping
    public List<ETicket> getAllTickets() {
        return eTicketService.getAll();
    }

    @GetMapping("/{id}")
    public ETicket getTicketById(@PathVariable Integer id) {
        return eTicketService.getById(id);
    }
}
