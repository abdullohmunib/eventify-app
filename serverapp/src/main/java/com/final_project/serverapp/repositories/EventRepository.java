package com.final_project.serverapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.final_project.serverapp.models.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    boolean existsByJudul(String judul);

    boolean existsByJudulAndEventIdNot(String judul, Integer eventId);
    
    Optional<Event> findBySlug(String slug);
    
    boolean existsBySlug(String slug);
    
    boolean existsBySlugAndEventIdNot(String slug, Integer eventId);
}
