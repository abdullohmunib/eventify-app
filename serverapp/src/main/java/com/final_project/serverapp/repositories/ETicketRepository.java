package com.final_project.serverapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.final_project.serverapp.models.ETicket;

@Repository
public interface ETicketRepository extends JpaRepository<ETicket, Integer> {
    long countByOrder_Event_EventId(Integer eventId);
}
