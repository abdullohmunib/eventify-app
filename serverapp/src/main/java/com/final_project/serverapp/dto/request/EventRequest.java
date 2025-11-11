package com.final_project.serverapp.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EventRequest(
        @NotBlank(message = "Judul wajib diisi") String judul,
        String deskripsi,
        @NotNull @Future(message = "Tanggal acara harus di masa depan") LocalDateTime tanggalAcara,
        @NotBlank(message = "Lokasi wajib diisi") String lokasi,
        String posterUrl,
        @NotNull @Min(value = 0, message = "Harga minimal 0") Double hargaTiket,
        @NotNull @Min(value = 1, message = "Kuota minimal 1") Integer kuotaTotal) {
}
