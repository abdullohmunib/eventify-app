package com.final_project.serverapp.services.contracts;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.final_project.serverapp.dto.request.EventRequest;
import com.final_project.serverapp.dto.response.EventResponse;

public interface EventService {
    EventResponse createEvent(EventRequest request, MultipartFile posterFile);

    EventResponse getEventById(Integer id);

    EventResponse getEventBySlug(String slug);

    List<EventResponse> getAllEvents();

    EventResponse updateEvent(Integer id, EventRequest request, MultipartFile posterFile);

    void deleteEvent(Integer id);
}
