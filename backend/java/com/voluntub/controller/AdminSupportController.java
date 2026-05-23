package com.voluntub.controller;

import com.voluntub.dto.TicketResponseDTO;
import com.voluntub.service.SupportTicketService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/support")
@CrossOrigin
public class AdminSupportController {

    private final SupportTicketService ticketService;

    public AdminSupportController(SupportTicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public List<TicketResponseDTO> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @PutMapping("/{ticketId}/{status}")
    public void updateStatus(
            @PathVariable Long ticketId,
            @PathVariable String status
    ) {
        ticketService.updateTicketStatus(ticketId, status);
    }
}
