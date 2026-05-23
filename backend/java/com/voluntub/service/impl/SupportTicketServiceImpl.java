package com.voluntub.service.impl;

import com.voluntub.dto.CreateTicketDTO;
import com.voluntub.dto.TicketResponseDTO;
import com.voluntub.entity.SupportTicket;
import com.voluntub.entity.User;
import com.voluntub.repository.SupportTicketRepository;
import com.voluntub.repository.UserRepository;
import com.voluntub.service.SupportTicketService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupportTicketServiceImpl implements SupportTicketService {

    private final SupportTicketRepository ticketRepo;
    private final UserRepository userRepo;

    public SupportTicketServiceImpl(
            SupportTicketRepository ticketRepo,
            UserRepository userRepo
    ) {
        this.ticketRepo = ticketRepo;
        this.userRepo = userRepo;
    }

    @Override
    public void createTicket(Long userId, String role, CreateTicketDTO dto) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        SupportTicket ticket = new SupportTicket();
        ticket.setTitle(dto.getTitle());
        ticket.setDescription(dto.getDescription());
        ticket.setPriority(
                SupportTicket.Priority.valueOf(dto.getPriority())
        );
        ticket.setUser(user);
        ticket.setRole(
                SupportTicket.UserRole.valueOf(role)
        );

        ticketRepo.save(ticket);
    }

    @Override
    public List<TicketResponseDTO> getMyTickets(Long userId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ticketRepo.findByUser(user)
                .stream()
                .map(t -> new TicketResponseDTO(
                        t.getId(),
                        t.getTitle(),
                        t.getDescription(),
                        t.getPriority().name(),
                        t.getStatus().name(),
                        t.getCreatedAt(),
                        user.getName(),
                        user.getEmail(),
                        t.getRole().name()
                ))
                .toList();
    }

    @Override
    public List<TicketResponseDTO> getAllTickets() {

        return ticketRepo.findAll()
                .stream()
                .map(t -> new TicketResponseDTO(
                        t.getId(),
                        t.getTitle(),
                        t.getDescription(),
                        t.getPriority().name(),
                        t.getStatus().name(),
                        t.getCreatedAt(),
                        t.getUser().getName(),
                        t.getUser().getEmail(),
                        t.getRole().name()
                ))
                .toList();
    }

    @Override
    public void updateTicketStatus(Long ticketId, String status) {

        SupportTicket ticket = ticketRepo.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        ticket.setStatus(
                SupportTicket.Status.valueOf(status)
        );

        ticketRepo.save(ticket);
    }
}
