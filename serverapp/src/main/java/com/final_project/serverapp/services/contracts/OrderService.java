package com.final_project.serverapp.services.contracts;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.final_project.serverapp.dto.request.OrderRequest;
import com.final_project.serverapp.dto.response.ETicketResponse;
import com.final_project.serverapp.dto.response.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(Integer eventId, OrderRequest request);

    OrderResponse getOrderById(Integer orderId);

    OrderResponse confirmPayment(Integer orderId);

    List<ETicketResponse> getTicketsByOrder(Integer orderId);

    List<OrderResponse> getOrdersByUserId(Integer userId);

    List<OrderResponse> getOrdersForCurrentUser();
    
    List<OrderResponse> getAllOrders();

    OrderResponse cancelOrder(Integer orderId);
    
    OrderResponse uploadPaymentProof(Integer orderId, MultipartFile file);
}
