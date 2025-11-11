package com.final_project.clientapp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.final_project.clientapp.dto.response.ApiResponse;
import com.final_project.clientapp.models.ETicket;

@Service
public class ETicketService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.api.base-url}")
    private String serverBaseUrl;

    public List<ETicket> getAll() {
        String url = serverBaseUrl + "/api/etickets";

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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return List.of(); // kosong kalau gagal
    }

    public ETicket getById(Integer id) {
        String url = serverBaseUrl + "/api/etickets/" + id;

        try {
            ResponseEntity<ApiResponse<ETicket>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<ApiResponse<ETicket>>() {
                    });

            ApiResponse<ETicket> apiResponse = response.getBody();
            if (apiResponse != null && apiResponse.isStatus()) {
                return apiResponse.getData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public ResponseEntity<?> useTicket(Integer id) {
        String url = serverBaseUrl + "/api/etickets/" + id + "/use";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<ApiResponse<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.PATCH,
                    entity,
                    new ParameterizedTypeReference<ApiResponse<Map<String, Object>>>() {
                    });

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return ResponseEntity.ok(response.getBody());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("status", false, "message", "Gagal menggunakan e-ticket"));
            }

        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map.of("status", false, "message", e.getResponseBodyAsString()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }

    public ResponseEntity<?> unuseTicket(Integer id) {
        String url = serverBaseUrl + "/api/etickets/" + id + "/unuse";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<ApiResponse<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.PATCH,
                    entity,
                    new ParameterizedTypeReference<ApiResponse<Map<String, Object>>>() {
                    });

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return ResponseEntity.ok(response.getBody());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("status", false, "message", "Gagal mengembalikan status e-ticket"));
            }

        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map.of("status", false, "message", e.getResponseBodyAsString()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", false, "message", e.getMessage()));
        }
    }
}
