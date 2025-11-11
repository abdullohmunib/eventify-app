package com.final_project.clientapp.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
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
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.final_project.clientapp.dto.response.ApiResponse;
import com.final_project.clientapp.models.Event;

@Service
public class EventService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.api.base-url}")
    private String serverBaseUrl;

    public List<Event> getAll() {
        String url = serverBaseUrl + "/api/event";

        ResponseEntity<ApiResponse<List<Event>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<ApiResponse<List<Event>>>() {
                });

        ApiResponse<List<Event>> apiResponse = response.getBody();

        if (apiResponse != null && apiResponse.isStatus()) {
            return apiResponse.getData();
        } else {
            return List.of(); // kosong kalau gagal
        }
    }

    public Event getById(Long id) {
        String url = serverBaseUrl + "/api/event/" + id;

        ResponseEntity<ApiResponse<Event>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<ApiResponse<Event>>() {
                });

        ApiResponse<Event> apiResponse = response.getBody();

        if (apiResponse != null && apiResponse.isStatus()) {
            return apiResponse.getData();
        } else {
            return null;
        }
    }

    public Event getBySlug(String slug) {
        String url = serverBaseUrl + "/api/event/slug/" + slug;

        ResponseEntity<ApiResponse<Event>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<ApiResponse<Event>>() {
                });

        ApiResponse<Event> apiResponse = response.getBody();

        if (apiResponse != null && apiResponse.isStatus()) {
            return apiResponse.getData();
        } else {
            return null;
        }
    }

    public ResponseEntity<?> delete(Long id) {
        try {
            restTemplate.exchange(
                    serverBaseUrl + "/api/event/" + id,
                    HttpMethod.DELETE,
                    null,
                    Void.class);
            return ResponseEntity.ok("Event deleted successfully");
        } catch (HttpStatusCodeException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + ex.getMessage());
        }
    }

    public ResponseEntity<?> create(String judul, String deskripsi, String tanggalAcara,
            String lokasi, Double hargaTiket, Integer kuotaTotal, MultipartFile posterFile) {
        try {
            // Buat MultiValueMap untuk multipart/form-data
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("judul", judul);
            body.add("deskripsi", deskripsi != null ? deskripsi : "");
            body.add("tanggalAcara", tanggalAcara);
            body.add("lokasi", lokasi);
            body.add("hargaTiket", hargaTiket);
            body.add("kuotaTotal", kuotaTotal);

            // Tambahkan file sebagai ByteArrayResource
            if (posterFile != null && !posterFile.isEmpty()) {
                ByteArrayResource fileResource = new ByteArrayResource(posterFile.getBytes()) {
                    @Override
                    public String getFilename() {
                        return posterFile.getOriginalFilename();
                    }
                };
                body.add("posterFile", fileResource);
            }

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Forward ke serverapp
            ResponseEntity<String> response = restTemplate.exchange(
                    serverBaseUrl + "/api/event",
                    HttpMethod.POST,
                    requestEntity,
                    String.class);

            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (HttpStatusCodeException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + ex.getMessage());
        }
    }

    public ResponseEntity<?> update(Long id, String judul, String deskripsi, String tanggalAcara,
            String lokasi, Double hargaTiket, Integer kuotaTotal, MultipartFile posterFile) {
        try {
            // Buat MultiValueMap untuk multipart/form-data
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("judul", judul);
            body.add("deskripsi", deskripsi != null ? deskripsi : "");
            body.add("tanggalAcara", tanggalAcara);
            body.add("lokasi", lokasi);
            body.add("hargaTiket", hargaTiket);
            body.add("kuotaTotal", kuotaTotal);

            // Tambahkan file jika ada
            if (posterFile != null && !posterFile.isEmpty()) {
                ByteArrayResource fileResource = new ByteArrayResource(posterFile.getBytes()) {
                    @Override
                    public String getFilename() {
                        return posterFile.getOriginalFilename();
                    }
                };
                body.add("posterFile", fileResource);
            }

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Forward ke serverapp
            ResponseEntity<String> response = restTemplate.exchange(
                    serverBaseUrl + "/api/event/" + id,
                    HttpMethod.PUT,
                    requestEntity,
                    String.class);

            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (HttpStatusCodeException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + ex.getMessage());
        }
    }

    public EventService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Map<String, Object>> getAllEvents() {
        String url = serverBaseUrl + "/api/event";

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());

                if (root.path("status").asBoolean(false) && root.has("data")) {
                    JsonNode dataNode = root.get("data");
                    List<Map<String, Object>> events = new ArrayList<>();

                    if (dataNode.isArray()) {
                        for (JsonNode eventNode : dataNode) {
                            Map<String, Object> eventMap = mapper.convertValue(eventNode, Map.class);

                            if (eventMap.containsKey("tanggalAcara") && eventMap.get("tanggalAcara") != null) {
                                String isoDate = eventMap.get("tanggalAcara").toString();
                                try {
                                    LocalDateTime localDateTime = LocalDateTime.parse(isoDate.replace("T", " "));
                                    String formattedDate = localDateTime.format(
                                            DateTimeFormatter.ofPattern("dd MMMM yyyy"));
                                    eventMap.put("formattedTanggalAcara", formattedDate);
                                } catch (Exception e) {
                                    eventMap.put("formattedTanggalAcara", isoDate);
                                }
                            }
                            events.add(eventMap);
                        }
                    }
                    return events;
                }
            }

            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error fetching events: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Map<String, Object> getEventById(Integer id) {
        String url = serverBaseUrl + "/api/event/" + id;

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());

                if (root.path("status").asBoolean(false) && root.has("data")) {
                    JsonNode dataNode = root.get("data");
                    Map<String, Object> eventMap = mapper.convertValue(dataNode, Map.class);

                    if (eventMap.containsKey("tanggalAcara") && eventMap.get("tanggalAcara") != null) {
                        String isoDate = eventMap.get("tanggalAcara").toString();
                        try {
                            LocalDateTime localDateTime = LocalDateTime.parse(isoDate.replace("T", " "));
                            String formattedDate = localDateTime.format(
                                    DateTimeFormatter.ofPattern("dd MMMM yyyy • HH:mm"));
                            eventMap.put("formattedTanggalAcara", formattedDate);
                        } catch (Exception e) {
                            eventMap.put("formattedTanggalAcara", isoDate);
                        }
                    }

                    return eventMap;
                }
            }

            return null;
        } catch (Exception e) {
            System.err.println("Error fetching event: " + e.getMessage());
            return null;
        }
    }
}
