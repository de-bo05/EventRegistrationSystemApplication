package com.auca.event_registration_system.controller;

import com.auca.event_registration_system.model.Registration;
import com.auca.event_registration_system.service.RegistrationService;
import com.auca.event_registration_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/registrations")
@CrossOrigin(origins = "*")
public class RegistrationController {
    
    @Autowired
    private RegistrationService registrationService;
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/event/{eventId}")
    public ResponseEntity<?> registerForEvent(@PathVariable Long eventId, Authentication authentication) {
        try {
            String username = authentication.getName();
            Long userId = userService.findByUsername(username)
                .map(user -> user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
            Registration registration = registrationService.registerForEvent(userId, eventId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Successfully registered for event");
            response.put("registrationId", registration.getId());
            response.put("ticketNumber", registration.getTicketNumber());
            response.put("qrCode", registration.getQrCodeData());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/my-registrations")
    public ResponseEntity<List<Registration>> getMyRegistrations(Authentication authentication) {
        String username = authentication.getName();
        Long userId = userService.findByUsername(username)
            .map(user -> user.getId())
            .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(registrationService.getUserRegistrations(userId));
    }
    
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<Registration>> getEventRegistrations(@PathVariable Long eventId) {
        return ResponseEntity.ok(registrationService.getEventRegistrations(eventId));
    }
    
    @GetMapping("/ticket/{ticketNumber}")
    public ResponseEntity<?> getRegistrationByTicket(@PathVariable String ticketNumber) {
        try {
            Registration registration = registrationService.getRegistrationByTicketNumber(ticketNumber);
            return ResponseEntity.ok(registration);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelRegistration(@PathVariable Long id) {
        try {
            registrationService.cancelRegistration(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Registration cancelled successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}

