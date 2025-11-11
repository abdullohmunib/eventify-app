package com.final_project.serverapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean status;
    private T data;
    private Object errors;
    private String message;
}
