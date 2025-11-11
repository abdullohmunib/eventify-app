package com.final_project.clientapp.controllers.admin.event;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.final_project.clientapp.services.EventService;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {

    private final EventService eventService;

    @GetMapping
    public String index(Model model, HttpSession session) {
        model.addAttribute("isActive", "events");
        return "admin/events/index";
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> create(
            @RequestParam("judul") String judul,
            @RequestParam(value = "deskripsi", required = false) String deskripsi,
            @RequestParam("tanggalAcara") String tanggalAcara,
            @RequestParam("lokasi") String lokasi,
            @RequestParam("hargaTiket") Double hargaTiket,
            @RequestParam("kuotaTotal") Integer kuotaTotal,
            @RequestParam("posterFile") MultipartFile posterFile) {
        
        return eventService.create(judul, deskripsi, tanggalAcara, lokasi, 
                hargaTiket, kuotaTotal, posterFile);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestParam("judul") String judul,
            @RequestParam(value = "deskripsi", required = false) String deskripsi,
            @RequestParam("tanggalAcara") String tanggalAcara,
            @RequestParam("lokasi") String lokasi,
            @RequestParam("hargaTiket") Double hargaTiket,
            @RequestParam("kuotaTotal") Integer kuotaTotal,
            @RequestParam(value = "posterFile", required = false) MultipartFile posterFile) {
        
        return eventService.update(id, judul, deskripsi, tanggalAcara, lokasi,
                hargaTiket, kuotaTotal, posterFile);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return eventService.delete(id);
    }
}
