package com.final_project.clientapp.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.final_project.clientapp.dto.request.LoginRequest;
import com.final_project.clientapp.dto.request.RegisterRequest;
import com.final_project.clientapp.dto.response.ApiResponse;

import org.springframework.core.ParameterizedTypeReference;

import jakarta.servlet.http.HttpSession;

@Service
public class AuthService {

    private final RestTemplate restTemplate;

    @Value("${app.api.base-url}")
    private String serverBaseUrl;

    public AuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> register(RegisterRequest request) {
        String url = serverBaseUrl + "/api/auth/register";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RegisterRequest> entity = new HttpEntity<>(request, headers);

        try {
            return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        } catch (HttpStatusCodeException ex) {
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(ex.getResponseBodyAsString());
        }
    }

    public ResponseEntity<String> login(LoginRequest request, HttpSession session) {
        String url = serverBaseUrl + "/api/auth/login";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            // Jika login berhasil, simpan session dari JSON data
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());

                if (root.path("status").asBoolean(false) && root.has("data")) {
                    JsonNode data = root.path("data");

                    if (data.has("access_token"))
                        session.setAttribute("token", data.get("access_token").asText());
                    if (data.has("email"))
                        session.setAttribute("email", data.get("email").asText());
                    if (data.has("nama"))
                        session.setAttribute("nama", data.get("nama").asText());
                    if (data.has("role"))
                        session.setAttribute("role", data.get("role").asText());
                }
            }

            return response;
        } catch (HttpStatusCodeException ex) {
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(ex.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }

    public ApiResponse<?> getCurrentUser(HttpSession session) {
        String url = serverBaseUrl + "/api/auth/me";

        try {
            String token = (String) session.getAttribute("token");
            if (token == null) {
                return new ApiResponse<>(false, null, null, "User belum login");
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<ApiResponse<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<ApiResponse<Map<String, Object>>>() {
                    });

            return response.getBody();

        } catch (Exception e) {
            return new ApiResponse<>(false, null, null, "Gagal mengambil data user: " + e.getMessage());
        }
    }
}
