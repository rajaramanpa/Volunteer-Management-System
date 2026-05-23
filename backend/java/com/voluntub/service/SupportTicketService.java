package com.voluntub.service;

import com.voluntub.dto.CreateTicketDTO;
import com.voluntub.dto.TicketResponseDTO;

import java.util.List;

public interface SupportTicketService {

    void createTicket(Long userId, String role, CreateTicketDTO dto);

    List<TicketResponseDTO> getMyTickets(Long userId);

    List<TicketResponseDTO> getAllTickets();

    void updateTicketStatus(Long ticketId, String status);
}
