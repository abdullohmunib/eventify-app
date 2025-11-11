package com.final_project.clientapp.controllers.admin.eticket;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.final_project.clientapp.services.ETicketService;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/etickets")
public class AdminETicketController {

    private final ETicketService eTicketService;

    @GetMapping
    public String index(Model model, HttpSession session) {
        model.addAttribute("isActive", "etickets");
        return "admin/etickets/index";
    }

    @PatchMapping("/{id}/use")
    @ResponseBody
    public ResponseEntity<?> useTicket(@PathVariable Integer id) {
        return eTicketService.useTicket(id);
    }

    @PatchMapping("/{id}/unuse")
    @ResponseBody
    public ResponseEntity<?> unuseTicket(@PathVariable Integer id) {
        return eTicketService.unuseTicket(id);
    }
}
