package com.final_project.clientapp.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.final_project.clientapp.models.Event;
import com.final_project.clientapp.services.EventService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    private final EventService eventService;

    public HomeController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        List<Map<String, Object>> events = eventService.getAllEvents();
        model.addAttribute("events", events);
        model.addAttribute("isActive", "home");
        model.addAttribute("isActive", "event");

        // Add user information to the model if logged in
        if (session.getAttribute("token") != null) {
            model.addAttribute("isLoggedIn", true);
            model.addAttribute("userName", session.getAttribute("nama"));
            model.addAttribute("userEmail", session.getAttribute("email"));
            model.addAttribute("userRole", session.getAttribute("role"));
        } else {
            model.addAttribute("isLoggedIn", false);
        }

        return "index";
    }

    @GetMapping("/detail/{slug}")
    public String detail(Model model, @PathVariable String slug) {
        Event event = eventService.getBySlug(slug);
        if (event != null) {
            model.addAttribute("event", event);
        } else {
            model.addAttribute("error", "Event tidak ditemukan");
        }
        model.addAttribute("isActive", "detail");
        return "detail";
    }
}
