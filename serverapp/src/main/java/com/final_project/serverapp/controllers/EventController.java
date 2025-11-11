package com.final_project.serverapp.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.final_project.serverapp.dto.request.EventRequest;
import com.final_project.serverapp.dto.response.ApiResponse;
import com.final_project.serverapp.dto.response.EventResponse;
import com.final_project.serverapp.services.contracts.EventService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EventResponse>> createEvent(
            @Valid
            @RequestParam("judul") String judul,
            @RequestParam(value = "deskripsi", required = false) String deskripsi,
            @RequestParam("tanggalAcara") String tanggalAcara,
            @RequestParam("lokasi") String lokasi,
            @RequestParam("hargaTiket") Double hargaTiket,
            @RequestParam("kuotaTotal") Integer kuotaTotal,
            @RequestParam("posterFile") MultipartFile posterFile) {
        
        // Validasi file harus ada dan tidak kosong
        if (posterFile == null || posterFile.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, null, null, "Poster gambar wajib diupload!"));
        }
        
        // Validasi tipe file harus gambar
        String contentType = posterFile.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, null, null, "File harus berupa gambar (PNG, JPG, JPEG)!"));
        }
        
        EventRequest request = new EventRequest(
            judul,
            deskripsi,
            LocalDateTime.parse(tanggalAcara),
            lokasi,
            null,
            hargaTiket,
            kuotaTotal
        );
        
        EventResponse response = eventService.createEvent(request, posterFile);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, response, null, "Event berhasil dibuat dengan poster"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EventResponse>> getEventById(@PathVariable Integer id) {
        EventResponse response = eventService.getEventById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, response, null, "OK"));
    }
    
    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse<EventResponse>> getEventBySlug(@PathVariable String slug) {
        EventResponse response = eventService.getEventBySlug(slug);
        return ResponseEntity.ok(new ApiResponse<>(true, response, null, "OK"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EventResponse>>> listEvents() {
        List<EventResponse> response = eventService.getAllEvents();
        return ResponseEntity.ok(new ApiResponse<>(true, response, null, "OK"));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<EventResponse>> updateEvent(
            @Valid
            @PathVariable Integer id,
            @RequestParam("judul") String judul,
            @RequestParam(value = "deskripsi", required = false) String deskripsi,
            @RequestParam("tanggalAcara") String tanggalAcara,
            @RequestParam("lokasi") String lokasi,
            @RequestParam("hargaTiket") Double hargaTiket,
            @RequestParam("kuotaTotal") Integer kuotaTotal,
            @RequestParam(value = "posterFile", required = false) MultipartFile posterFile) {
        
        // Validasi tipe file jika ada yang diupload
        if (posterFile != null && !posterFile.isEmpty()) {
            String contentType = posterFile.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, null, null, "File harus berupa gambar (PNG, JPG, JPEG)!"));
            }
        }
        
        EventRequest request = new EventRequest(
            judul,
            deskripsi,
            LocalDateTime.parse(tanggalAcara),
            lokasi,
            null,
            hargaTiket,
            kuotaTotal
        );
        
        EventResponse response = eventService.updateEvent(id, request, posterFile);
        return ResponseEntity.ok(new ApiResponse<>(true, response, null, "Event berhasil diperbarui"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteEvent(@PathVariable Integer id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok(new ApiResponse<>(true, null, null, "Event dihapus"));
    }
}
