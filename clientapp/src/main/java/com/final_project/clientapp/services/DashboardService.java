package com.final_project.clientapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.final_project.clientapp.dto.response.ApiResponse;
import com.final_project.clientapp.models.DashboardSummary;

@Service
public class DashboardService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.api.base-url}")
    private String serverBaseUrl;

    public DashboardSummary getSummary() {
        String url = serverBaseUrl + "/api/dashboard/summary";

        try {
            ResponseEntity<ApiResponse<DashboardSummary>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<ApiResponse<DashboardSummary>>() {
                    });

            ApiResponse<DashboardSummary> apiResponse = response.getBody();

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
}
