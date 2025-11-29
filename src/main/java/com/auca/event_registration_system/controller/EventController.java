package com.auca.event_registration_system.controller;

import com.auca.event_registration_system.dto.EventDTO;
import com.auca.event_registration_system.model.Event;
import com.auca.event_registration_system.service.EventService;
import com.auca.event_registration_system.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventController {
    
    @Autowired
    private EventService eventService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/public/active")
    public ResponseEntity<List<EventDTO>> getActiveEvents() {
        return ResponseEntity.ok(eventService.getActiveEvents());
    }
    
    @GetMapping("/public/upcoming")
    public ResponseEntity<List<EventDTO>> getUpcomingEvents() {
        return ResponseEntity.ok(eventService.getUpcomingEvents());
    }
    
    @GetMapping("/public/{id}")
    public ResponseEntity<?> getEventById(@PathVariable Long id) {
        try {
            EventDTO event = eventService.getEventById(id);
            return ResponseEntity.ok(event);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents(Authentication authentication) {
        return ResponseEntity.ok(eventService.getAllEvents());
    }
    
    @PostMapping
    public ResponseEntity<?> createEvent(@Valid @RequestBody EventDTO eventDTO, Authentication authentication) {
        try {
            String username = authentication.getName();
            Long userId = userService.findByUsername(username)
                .map(user -> user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
            Event event = eventService.createEvent(eventDTO, userId);
            EventDTO createdEvent = eventService.getEventById(event.getId());
            return ResponseEntity.ok(createdEvent);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @Valid @RequestBody EventDTO eventDTO) {
        try {
            Event event = eventService.updateEvent(id, eventDTO);
            EventDTO updatedEvent = eventService.getEventById(event.getId());
            return ResponseEntity.ok(updatedEvent);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        try {
            eventService.deleteEvent(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Event deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/my-events")
    public ResponseEntity<List<EventDTO>> getMyEvents(Authentication authentication) {
        String username = authentication.getName();
        Long userId = userService.findByUsername(username)
            .map(user -> user.getId())
            .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(eventService.getEventsByCreator(userId));
    }
}

