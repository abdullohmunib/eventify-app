package com.final_project.serverapp.models;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "orders")
@Entity
@Data
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;

    @ManyToOne 
    @JoinColumn(name = "customer_id", nullable = false) 
    private User user; 

    @ManyToOne 
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "jumlah_tiket", nullable = false)
    private Integer jumlahTiket;

    @Column(name = "total_harga", nullable = false)
    private Double totalHarga;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_pembayaran", nullable = false)
    private StatusPembayaran statusPembayaran;
    
    @Column(name = "bukti_pembayaran")
    private String buktiPembayaran;

    @OneToMany(mappedBy = "order") 
    private List<ETicket> eTickets; 

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

