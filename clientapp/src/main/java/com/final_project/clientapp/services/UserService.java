package com.final_project.clientapp.services;

import java.util.List;

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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.final_project.clientapp.dto.response.ApiResponse;
import com.final_project.clientapp.models.User;

@Service
public class UserService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.api.base-url}")
    private String serverBaseUrl;

    public List<User> getAll() {
        String url = serverBaseUrl + "/api/users";
        
        try {
            ResponseEntity<ApiResponse<List<User>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<ApiResponse<List<User>>>() {
                    });

            ApiResponse<List<User>> apiResponse = response.getBody();

            if (apiResponse != null && apiResponse.isStatus()) {
                return apiResponse.getData();
            } else {
                return List.of();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public User getById(Integer id) {
        String url = serverBaseUrl + "/api/users/" + id;

        try {
            ResponseEntity<ApiResponse<User>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<ApiResponse<User>>() {
                    });

            ApiResponse<User> apiResponse = response.getBody();

            if (apiResponse != null && apiResponse.isStatus()) {
                return apiResponse.getData();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResponseEntity<?> create(String namaLengkap, String email, String password, String role) {
        String url = serverBaseUrl + "/api/users";

        try {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("namaLengkap", namaLengkap);
            formData.add("email", email);
            formData.add("password", password);
            formData.add("role", role);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(response.getBody());
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"status\":false,\"message\":\"" + e.getMessage() + "\"}");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"status\":false,\"message\":\"Gagal membuat user\"}");
    }

    public ResponseEntity<?> update(Integer id, String namaLengkap, String email, String role) {
        String url = serverBaseUrl + "/api/users/" + id;

        try {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("namaLengkap", namaLengkap);
            formData.add("email", email);
            formData.add("role", role);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    requestEntity,
                    String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(response.getBody());
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"status\":false,\"message\":\"" + e.getMessage() + "\"}");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"status\":false,\"message\":\"Gagal mengupdate user\"}");
    }

    public ResponseEntity<?> delete(Integer id) {
        String url = serverBaseUrl + "/api/users/" + id;

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    HttpEntity.EMPTY,
                    String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(response.getBody());
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"status\":false,\"message\":\"" + e.getMessage() + "\"}");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"status\":false,\"message\":\"Gagal menghapus user\"}");
    }
}
