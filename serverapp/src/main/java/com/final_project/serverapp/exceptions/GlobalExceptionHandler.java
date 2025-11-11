package com.final_project.serverapp.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.final_project.serverapp.dto.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle Validasi Error
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(err -> {
            errors.put(err.getField(), err.getDefaultMessage());
        });

        ApiResponse<Object> res = new ApiResponse<>(
                false,
                null,
                errors,
                "Validasi gagal");

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(res);
    }

    // Handle Runtime Error
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeErrors(RuntimeException ex) {

        ApiResponse<Object> res = new ApiResponse<>(
                false,
                null,
                ex.getMessage(),
                "Terjadi kesalahan pada server");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException ex) {
        ApiResponse<Object> res = new ApiResponse<>(
                false, null, ex.getMessage(), "Data tidak ditemukan");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequest(BadRequestException ex) {
        ApiResponse<Object> res = new ApiResponse<>(
                false, null, ex.getMessage(), "periksa kembali request anda");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}
