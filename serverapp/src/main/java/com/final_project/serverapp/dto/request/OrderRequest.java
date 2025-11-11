package com.final_project.serverapp.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(
        @NotNull(message = "Jumlah tiket wajib diisi") @Min(value = 1, message = "Minimal pesan 1 tiket") Integer quantity) {

}
