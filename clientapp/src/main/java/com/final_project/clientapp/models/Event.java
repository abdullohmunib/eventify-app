package com.final_project.clientapp.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private Integer eventId;
    private String judul;
    private String slug;
    private String deskripsi;
    private LocalDateTime tanggalAcara;
    private String lokasi;
    private String posterUrl;
    private Double hargaTiket;
    private Integer kuotaTotal;
    private Integer kuotaTersisa;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public String getFormattedTanggalAcara() {
        if (tanggalAcara == null) {
            return "-";
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy - HH:mm", new Locale("id", "ID"));
        return tanggalAcara.format(formatter) + " WITA";
    }
}
