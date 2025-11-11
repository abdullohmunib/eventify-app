package com.final_project.serverapp.services.implementation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.final_project.serverapp.dto.request.OrderRequest;
import com.final_project.serverapp.dto.response.ETicketResponse;
import com.final_project.serverapp.dto.response.OrderResponse;
import com.final_project.serverapp.exceptions.BadRequestException;
import com.final_project.serverapp.exceptions.NotFoundException;
import com.final_project.serverapp.models.CheckInStatus;
import com.final_project.serverapp.models.ETicket;
import com.final_project.serverapp.models.Event;
import com.final_project.serverapp.models.Order;
import com.final_project.serverapp.models.StatusPembayaran;
import com.final_project.serverapp.models.User;
import com.final_project.serverapp.repositories.ETicketRepository;
import com.final_project.serverapp.repositories.EventRepository;
import com.final_project.serverapp.repositories.OrderRepository;
import com.final_project.serverapp.repositories.UserRepository;
import com.final_project.serverapp.services.contracts.OrderService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ETicketRepository eTicketRepository;
    
    @Value("${file.upload-dir:src/main/resources/static/uploads/payment-proofs}")
    private String uploadDir;

    @Override
    @Transactional
    public OrderResponse cancelOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order tidak Ditemukan"));
        if (order.getStatusPembayaran() == StatusPembayaran.PAID) {
            throw new BadRequestException("Order sudah dibayar");
        }

        if (order.getStatusPembayaran() == StatusPembayaran.FAILED) {
            throw new BadRequestException("Order Sudah Dibatalkan");
        }

        order.setStatusPembayaran(StatusPembayaran.FAILED);
        
        // Kembalikan kuota event
        Event event = order.getEvent();
        event.setKuotaTersisa(event.getKuotaTersisa() + order.getJumlahTiket());
        eventRepository.save(event);
        
        // Simpan perubahan order
        Order savedOrder = orderRepository.save(order);
        return toOrderResponse(savedOrder);
    }

    @Override
    @Transactional
    public OrderResponse confirmPayment(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order tidak Ditemukan"));
        if (order.getStatusPembayaran() == StatusPembayaran.PAID) {
            throw new BadRequestException("Order sudah dibayar");
        }

        if (order.getStatusPembayaran() == StatusPembayaran.FAILED) {
            throw new BadRequestException("Order Sudah Dibatalkan");
        }

        order.setStatusPembayaran(StatusPembayaran.PAID);
        Order savedOrder = orderRepository.save(order);
        generateTickets(savedOrder);
        return toOrderResponse(savedOrder);
    }

    @Override
    @Transactional
    public OrderResponse createOrder(Integer eventId, OrderRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event tidak ditemukan"));
        if (event.getKuotaTersisa() < request.quantity()) {
            throw new BadRequestException("Kouta tiket tidak Mencukupi");
        }
        User user = getCurrentUser();
        Order order = new Order();
        order.setEvent(event);
        order.setUser(user);
        order.setJumlahTiket(request.quantity());
        order.setTotalHarga(event.getHargaTiket() * request.quantity());
        order.setStatusPembayaran(StatusPembayaran.UNPAID);
        event.setKuotaTersisa(event.getKuotaTersisa() - request.quantity());

        Order saved = orderRepository.save(order);
        return toOrderResponse(saved);
    }

    @Override
    public OrderResponse getOrderById(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order tidak ditemukan"));
        return toOrderResponse(order);
    }

    @Override
    public List<OrderResponse> getOrdersByUserId(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User tidak ditemukan"));

        List<Order> orders = orderRepository.findByUserOrderByCreatedAtDesc(user);
        if (orders.isEmpty()) {
            throw new NotFoundException("Order tidak ditemukan");
        }

        return orders.stream()
                .map(this::toOrderResponse)
                .toList();
    }

    @Override
    public List<ETicketResponse> getTicketsByOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order tidak ditemukan"));

        return order.getETickets().stream()
                .map(this::toTicketResponse)
                .toList();
    }

    @Override
    public List<OrderResponse> getOrdersForCurrentUser() {
        User user = getCurrentUser();
        return orderRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(this::toOrderResponse)
                .toList();
    }
    
    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toOrderResponse)
                .toList();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new BadRequestException("User belum login");
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new NotFoundException("User tidak ditemukan"));
    }

    private void generateTickets(Order order) {
        if (order.getETickets() != null && !order.getETickets().isEmpty()) {
            return;
        }

        for (int i = 0; i < order.getJumlahTiket(); i++) {
            ETicket ticket = new ETicket();
            ticket.setOrder(order);
            ticket.setKodeUnik(createTicketCode(order));
            ticket.setStatusCheckIn(CheckInStatus.NOT_USED);
            eTicketRepository.save(ticket);
        }
    }

    private String createTicketCode(Order order) {
        return "EVT-" + order.getEvent().getEventId() + "-"
                + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    @Override
    @Transactional
    public OrderResponse uploadPaymentProof(Integer orderId, MultipartFile file) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order tidak ditemukan"));
        
        // Validasi status order
        if (order.getStatusPembayaran() != StatusPembayaran.UNPAID) {
            throw new BadRequestException("Tidak dapat mengupload bukti pembayaran untuk order ini");
        }
        
        // Validasi file
        if (file.isEmpty()) {
            throw new BadRequestException("File tidak boleh kosong");
        }
        
        // Validasi tipe file (hanya gambar)
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BadRequestException("File harus berupa gambar");
        }
        
        try {
            // Buat direktori jika belum ada
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // Generate nama file unik
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = "payment-" + order.getOrderId() + "-" + 
                    System.currentTimeMillis() + fileExtension;
            
            // Simpan file
            Path filePath = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            // Generate full URL menggunakan ServletUriComponentsBuilder
            String baseUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .build()
                    .toUriString();
            
            String paymentProofPath = "/uploads/payment-proofs/" + newFilename;
            String paymentProofUrl = baseUrl + paymentProofPath;
            
            // Update order dengan full URL bukti pembayaran
            order.setBuktiPembayaran(paymentProofUrl);
            orderRepository.save(order);
            
            return toOrderResponse(order);
            
        } catch (IOException e) {
            throw new BadRequestException("Gagal menyimpan file: " + e.getMessage());
        }
    }

    private OrderResponse toOrderResponse(Order order) {
        return new OrderResponse(
                order.getOrderId(),
                order.getEvent().getEventId(),
                order.getUser().getId(),
                order.getJumlahTiket(),
                order.getTotalHarga(),
                order.getStatusPembayaran(),
                order.getBuktiPembayaran(),
                order.getCreatedAt());
    }

    private ETicketResponse toTicketResponse(ETicket ticket) {
        return new ETicketResponse(
                ticket.getTicketId(),
                ticket.getKodeUnik(),
                ticket.getStatusCheckIn(),
                ticket.getCreatedAt());
    }
}
