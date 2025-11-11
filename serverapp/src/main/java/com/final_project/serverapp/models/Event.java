package com.final_project.serverapp.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "events")
@Entity
@Data
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Integer eventId;

    @Column(name = "judul", nullable = false, length = 255)
    private String judul;

    @Column(name = "slug")
    private String slug;

    @Column(name = "deskripsi", columnDefinition = "TEXT")
    private String deskripsi;

    @Column(name = "tanggal_acara", nullable = false)
    private LocalDateTime tanggalAcara;

    @Column(name = "lokasi", length = 255)
    private String lokasi;

    @Column(name = "poster_url")
    private String posterUrl;

    @Column(name = "harga_tiket", nullable = false)
    private Double hargaTiket;

    @Column(name = "kuota_total", nullable = false)
    private Integer kuotaTotal;

    @Column(name = "kuota_tersisa", nullable = false)
    private Integer kuotaTersisa;

    @OneToMany(mappedBy = "event")
    @JsonIgnore
    private List<Order> orders;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public String getFormattedTanggalAcara() {
        if (tanggalAcara == null) {
            return "-";
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy - HH:mm", new Locale("id", "ID"));
        return tanggalAcara.format(formatter) + " WITA";
    }
}
