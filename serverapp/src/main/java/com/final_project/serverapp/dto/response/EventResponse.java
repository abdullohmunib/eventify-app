package com.final_project.serverapp.dto.response;

import java.time.LocalDateTime;

public record EventResponse(
        Integer eventId,
        String judul,
        String slug,
        String deskripsi,
        LocalDateTime tanggalAcara,
        String lokasi,
        String posterUrl,
        Double hargaTiket,
        Integer kuotaTotal,
        Integer kuotaTersisa) {
}
