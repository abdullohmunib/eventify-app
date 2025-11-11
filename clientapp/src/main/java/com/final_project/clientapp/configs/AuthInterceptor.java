package com.final_project.clientapp.configs;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        HttpSession session = request.getSession(false);
        String uri = request.getRequestURI();

        if (session != null && session.getAttribute("token") != null && uri.equals("/login")) {
            String role = (String) session.getAttribute("role");
            if ("admin".equalsIgnoreCase(role)) {
                response.sendRedirect("/admin/dashboard");
            } else if ("user".equalsIgnoreCase(role)) {
                response.sendRedirect("/"); // Redirect user to home page instead of dashboard
            } else {
                response.sendRedirect("/admin/dashboard");
            }
            return false;
        }

        if (uri.startsWith("/admin")) {
            if (session == null || session.getAttribute("token") == null) {
                response.sendRedirect("/login");
                return false;
            }
            String role = (String) session.getAttribute("role");
            if (!"admin".equalsIgnoreCase(role)) {
                response.sendRedirect("/");
                return false;
            }
        }

        if (uri.startsWith("/user")) {
            if (session == null || session.getAttribute("token") == null) {
                response.sendRedirect("/login");
                return false;
            }
            String role = (String) session.getAttribute("role");
            if (!"user".equalsIgnoreCase(role)) {
                response.sendRedirect("/admin/dashboard");
                return false;
            }
        }

        return true;
    }
}
