package com.final_project.clientapp.configs;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

@Component
public class TokenInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) 
            throws IOException {
        
        try {
            // Ambil HttpSession dari request context
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            
            if (attributes != null) {
                HttpServletRequest servletRequest = attributes.getRequest();
                HttpSession session = servletRequest.getSession(false);
                
                // Ambil token dari session
                if (session != null) {
                    String token = (String) session.getAttribute("token");
                    
                    // Tambahkan Authorization header jika token ada
                    if (token != null && !token.isEmpty()) {
                        request.getHeaders().setBearerAuth(token);
                    }
                }
            }
        } catch (Exception e) {
            // Log error secara opsional jika perlu debugging
            System.err.println("Error in TokenInterceptor: " + e.getMessage());
        }
        
        return execution.execute(request, body);
    }
}
