package com.final_project.clientapp.controllers.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.final_project.clientapp.dto.request.OrderRequest;
import com.final_project.clientapp.dto.response.ApiResponse;
import com.final_project.clientapp.models.ETicket;
import com.final_project.clientapp.models.Event;
import com.final_project.clientapp.models.Order;
import com.final_project.clientapp.services.EventService;
import com.final_project.clientapp.services.OrderService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserOrderController {

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private EventService eventService;

    @GetMapping("/orders")
    public String orders(Model model, HttpSession session) {
        String nama = (String) session.getAttribute("nama");

        // Fetch user's orders from the backend
        List<Order> orders = orderService.getMyOrders();

        model.addAttribute("user", nama);
        model.addAttribute("orders", orders);
        model.addAttribute("isActive", "orders");
        return "user/orders";
    }

    @GetMapping("/orders/{orderId}")
    public String orderDetail(@PathVariable Integer orderId, Model model, HttpSession session) {
        String nama = (String) session.getAttribute("nama");

        // Fetch order details with tickets from the backend
        Order order = orderService.getOrderById(orderId);
        List<ETicket> tickets = orderService.getTicketsByOrder(orderId);
        
        // Fetch event details
        Event event = null;
        if (order != null && order.getEventId() != null) {
            event = eventService.getById(order.getEventId().longValue());
        }

        model.addAttribute("user", nama);
        model.addAttribute("order", order);
        model.addAttribute("event", event);
        model.addAttribute("tickets", tickets);
        model.addAttribute("isActive", "orders");
        return "user/order-detail";
    }

    @PostMapping("/orders/create")
    @ResponseBody
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest request) {
        ApiResponse<?> response = orderService.createOrder(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/orders/{orderId}/upload-payment-proof")
    @ResponseBody
    public ResponseEntity<?> uploadPaymentProof(
            @PathVariable Integer orderId,
            @RequestParam("file") MultipartFile file) {
        ApiResponse<?> response = orderService.uploadPaymentProof(orderId, file);
        return ResponseEntity.ok(response);
    }
}