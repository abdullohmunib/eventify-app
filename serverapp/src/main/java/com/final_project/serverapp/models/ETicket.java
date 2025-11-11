package com.final_project.serverapp.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "e_tickets")
@Entity
@Data
@NoArgsConstructor
public class ETicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Integer ticketId;

    // --- Relasi ke Order (Best Practice: LAZY) ---
    // Banyak E-Ticket dimiliki oleh Satu Order
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore 
    private Order order;

    @Column(name = "kode_unik", nullable = false, unique = true, length = 50)
    private String kodeUnik;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_check_in", nullable = false)
    private CheckInStatus statusCheckIn;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
