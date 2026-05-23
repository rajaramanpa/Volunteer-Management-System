package com.voluntub.repository;

import com.voluntub.entity.SupportTicket;
import com.voluntub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportTicketRepository
        extends JpaRepository<SupportTicket, Long> {

    List<SupportTicket> findByUser(User user);

    List<SupportTicket> findByStatus(SupportTicket.Status status);

    List<SupportTicket> findByPriority(SupportTicket.Priority priority);
}
