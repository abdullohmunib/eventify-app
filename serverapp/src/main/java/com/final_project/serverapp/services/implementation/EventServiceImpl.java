package com.final_project.serverapp.services.implementation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.final_project.serverapp.dto.request.EventRequest;
import com.final_project.serverapp.dto.response.EventResponse;
import com.final_project.serverapp.exceptions.NotFoundException;
import com.final_project.serverapp.models.Event;
import com.final_project.serverapp.repositories.EventRepository;
import com.final_project.serverapp.services.contracts.EventService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    @Value("${app.upload-dir:src/main/resources/static/uploads/posters}")
    private String uploadDir;

    @Override
    @Transactional
    public EventResponse createEvent(EventRequest request, MultipartFile posterFile) {
        Event event = new Event();
        setRequest(request, event);

        // Upload file dan set posterUrl
        if (posterFile != null && !posterFile.isEmpty()) {
            String posterPath = handlePosterUpload(posterFile);
            String baseUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .build()
                    .toUriString();

            String posterUrl = baseUrl + posterPath;
            event.setPosterUrl(posterUrl);
        }

        return toResponse(eventRepository.save(event));
    }

    @Override
    @Transactional
    public void deleteEvent(Integer id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event tidak ditemukan"));
        eventRepository.delete(event);
    }

    @Override
    public EventResponse getEventById(Integer id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event tidak ditemukan"));
        return toResponse(event);
    }

    @Override
    public EventResponse getEventBySlug(String slug) {
        return eventRepository.findBySlug(slug)
                .map(this::toResponse)
                .orElseThrow(() -> new NotFoundException("Event dengan slug '" + slug + "' tidak ditemukan"));
    }

    @Override
    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public EventResponse updateEvent(Integer id, EventRequest request, MultipartFile posterFile) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event tidak ditemukan"));

        setRequest(request, event);

        // Jika ada file baru, upload dan update posterUrl
        if (posterFile != null && !posterFile.isEmpty()) {
            String posterPath = handlePosterUpload(posterFile);
            String baseUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .build()
                    .toUriString();

            String posterUrl = baseUrl + posterPath;
            event.setPosterUrl(posterUrl);
        }

        return toResponse(eventRepository.save(event));
    }

    private void setRequest(EventRequest request, Event event) {
        event.setJudul(request.judul());

        // Generate unique slug
        String baseSlug = toSlug(request.judul());
        String uniqueSlug = generateUniqueSlug(baseSlug, event.getEventId());
        event.setSlug(uniqueSlug);

        event.setDeskripsi(request.deskripsi());
        event.setTanggalAcara(request.tanggalAcara());
        event.setLokasi(request.lokasi());
        if (request.posterUrl() != null) {
            event.setPosterUrl(request.posterUrl());
        }
        event.setHargaTiket(request.hargaTiket());
        event.setKuotaTotal(request.kuotaTotal());
        if (event.getKuotaTersisa() == null) {
            event.setKuotaTersisa(event.getKuotaTotal());
        }
    }

    /**
     * Generate unique slug dengan menambahkan suffix number jika sudah ada yang
     * sama
     */
    private String generateUniqueSlug(String baseSlug, Integer eventId) {
        String slug = baseSlug;
        int counter = 1;

        // Cek apakah slug sudah ada (exclude current event ID saat update)
        boolean exists = (eventId == null)
                ? eventRepository.existsBySlug(slug)
                : eventRepository.existsBySlugAndEventIdNot(slug, eventId);

        while (exists) {
            slug = baseSlug + "-" + counter;
            counter++;

            exists = (eventId == null)
                    ? eventRepository.existsBySlug(slug)
                    : eventRepository.existsBySlugAndEventIdNot(slug, eventId);
        }

        return slug;
    }

    private String handlePosterUpload(MultipartFile file) {
        try {
            // Buat direktori jika belum ada
            Path directory = Paths.get(uploadDir).toAbsolutePath().normalize();
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            // Generate filename unik dengan timestamp
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String filename = System.currentTimeMillis() + extension;

            // Simpan file ke disk
            Path filePath = directory.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Return URL path untuk akses file
            return "/uploads/posters/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Gagal mengupload file poster: " + e.getMessage(), e);
        }
    }

    private String toSlug(String str) {
        return str.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");
    }

    private EventResponse toResponse(Event event) {
        return new EventResponse(
                event.getEventId(),
                event.getJudul(),
                event.getSlug(),
                event.getDeskripsi(),
                event.getTanggalAcara(),
                event.getLokasi(),
                event.getPosterUrl(),
                event.getHargaTiket(),
                event.getKuotaTotal(),
                event.getKuotaTersisa());
    }

}
