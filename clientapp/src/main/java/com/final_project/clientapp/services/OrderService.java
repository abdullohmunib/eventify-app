package com.final_project.clientapp.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.final_project.clientapp.dto.request.OrderRequest;
import com.final_project.clientapp.dto.response.ApiResponse;
import com.final_project.clientapp.models.Order;
import com.final_project.clientapp.models.ETicket;

@Service
public class OrderService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.api.base-url}")
    private String serverBaseUrl;

    public List<Order> getMyOrders() {
        String url = serverBaseUrl + "/api/orders/me";

        try {
            ResponseEntity<ApiResponse<List<Order>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<ApiResponse<List<Order>>>() {
                    });

            ApiResponse<List<Order>> apiResponse = response.getBody();

            if (apiResponse != null && apiResponse.isStatus()) {
                return apiResponse.getData();
            } else {
                return List.of(); // Return empty list if request fails
            }
        } catch (HttpStatusCodeException e) {
            // Log the error for debugging
            System.err.println("Error fetching orders: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            return List.of(); // Return empty list if request fails
        } catch (Exception e) {
            // Log any other errors
            System.err.println("Error fetching orders: " + e.getMessage());
            return List.of(); // Return empty list if request fails
        }
    }

    public Order getOrderById(Integer orderId) {
        String url = serverBaseUrl + "/api/orders/" + orderId;

        try {
            ResponseEntity<ApiResponse<Order>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<ApiResponse<Order>>() {
                    });

            ApiResponse<Order> apiResponse = response.getBody();

            if (apiResponse != null && apiResponse.isStatus()) {
                return apiResponse.getData();
            } else {
                return null;
            }
        } catch (HttpStatusCodeException e) {
            // Log the error for debugging
            System.err.println("Error fetching order: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            return null;
        } catch (Exception e) {
            // Log any other errors
            System.err.println("Error fetching order: " + e.getMessage());
            return null;
        }
    }

    public List<ETicket> getTicketsByOrder(Integer orderId) {
        String url = serverBaseUrl + "/api/orders/" + orderId + "/tickets";

        try {
            ResponseEntity<ApiResponse<List<ETicket>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<ApiResponse<List<ETicket>>>() {
                    });

            ApiResponse<List<ETicket>> apiResponse = response.getBody();

            if (apiResponse != null && apiResponse.isStatus()) {
                return apiResponse.getData();
            } else {
                return List.of(); // Return empty list if request fails
            }
        } catch (HttpStatusCodeException e) {
            // Log the error for debugging
            System.err.println("Error fetching tickets: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            return List.of(); // Return empty list if request fails
        } catch (Exception e) {
            // Log any other errors
            System.err.println("Error fetching tickets: " + e.getMessage());
            return List.of(); // Return empty list if request fails
        }
    }
    
    // Method to get order with associated tickets
    public Map<String, Object> getOrderWithTickets(Integer orderId) {
        Order order = getOrderById(orderId);
        if (order != null) {
            List<ETicket> tickets = getTicketsByOrder(orderId);
            
            // Create a map containing both order and tickets
            java.util.HashMap<String, Object> result = new java.util.HashMap<>();
            result.put("order", order);
            result.put("tickets", tickets);
            return result;
        }
        return null;
    }

    public ApiResponse<?> createOrder(OrderRequest request) {
        // Validasi input
        if (request.getEventId() == null) {
            return new ApiResponse<>(false, null, null, "Event ID tidak boleh kosong");
        }
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            return new ApiResponse<>(false, null, null, "Quantity harus lebih dari 0");
        }

        String url = serverBaseUrl + "/api/orders/events/" + request.getEventId();

        // Body untuk dikirim ke serverapp - gunakan HashMap untuk menghindari NPE dengan Map.of()
        Map<String, Integer> requestBody = new HashMap<>();
        requestBody.put("quantity", request.getQuantity());

        try {
            HttpEntity<Map<String, Integer>> entity = new HttpEntity<>(requestBody);
            ResponseEntity<ApiResponse<?>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<ApiResponse<?>>() {}
            );

            return response.getBody();

        } catch (HttpStatusCodeException e) {
            return new ApiResponse<>(false, null, null, "Gagal membuat order: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            return new ApiResponse<>(false, null, null, "Terjadi kesalahan tidak terduga: " + e.getMessage());
        }
    }
    
    public ApiResponse<?> uploadPaymentProof(Integer orderId, org.springframework.web.multipart.MultipartFile file) {
        String url = serverBaseUrl + "/api/orders/" + orderId + "/upload-payment-proof";
        
        try {
            // Buat multipart request
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.MULTIPART_FORM_DATA);
            
            org.springframework.util.LinkedMultiValueMap<String, Object> body = new org.springframework.util.LinkedMultiValueMap<>();
            body.add("file", file.getResource());
            
            HttpEntity<org.springframework.util.LinkedMultiValueMap<String, Object>> entity = 
                    new HttpEntity<>(body, headers);
            
            ResponseEntity<ApiResponse<?>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<ApiResponse<?>>() {}
            );
            
            return response.getBody();
            
        } catch (HttpStatusCodeException e) {
            return new ApiResponse<>(false, null, null, "Gagal upload bukti pembayaran: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            return new ApiResponse<>(false, null, null, "Terjadi kesalahan: " + e.getMessage());
        }
    }
    
    // Admin methods
    public ResponseEntity<?> getAllOrders() {
        String url = serverBaseUrl + "/api/orders";
        
        try {
            return restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<ApiResponse<List<Order>>>() {}
            );
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }
    
    public ResponseEntity<?> getOrderByIdForAdmin(Integer orderId) {
        String url = serverBaseUrl + "/api/orders/" + orderId;
        
        try {
            return restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<ApiResponse<Order>>() {}
            );
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }
    
    public ResponseEntity<?> confirmPayment(Integer orderId) {
        String url = serverBaseUrl + "/api/orders/" + orderId + "/confirm";
        
        try {
            return restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<ApiResponse<?>>() {}
            );
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }
    
    public ResponseEntity<?> rejectPayment(Integer orderId) {
        String url = serverBaseUrl + "/api/orders/" + orderId + "/cancel";
        
        try {
            return restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<ApiResponse<?>>() {}
            );
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }
}