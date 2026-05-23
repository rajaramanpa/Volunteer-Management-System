package com.voluntub.controller;

import com.voluntub.dto.CreateTicketDTO;
import com.voluntub.dto.TicketResponseDTO;
import com.voluntub.service.SupportTicketService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/support")
@CrossOrigin
public class SupportTicketController {

    private final SupportTicketService ticketService;

    public SupportTicketController(SupportTicketService ticketService) {
        this.ticketService = ticketService;
    }

    // CREATE TICKET
    @PostMapping("/{userId}/{role}")
    public void createTicket(
            @PathVariable Long userId,
            @PathVariable String role,
            @RequestBody CreateTicketDTO dto
    ) {
        ticketService.createTicket(userId, role, dto);
    }

    // VIEW MY TICKETS
    @GetMapping("/my/{userId}")
    public List<TicketResponseDTO> getMyTickets(
            @PathVariable Long userId
    ) {
        return ticketService.getMyTickets(userId);
    }
}
