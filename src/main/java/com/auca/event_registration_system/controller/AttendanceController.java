package com.auca.event_registration_system.controller;

import com.auca.event_registration_system.model.Attendance;
import com.auca.event_registration_system.service.AttendanceService;
import com.auca.event_registration_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "*")
public class AttendanceController {
    
    @Autowired
    private AttendanceService attendanceService;
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/mark/ticket/{ticketNumber}")
    public ResponseEntity<?> markAttendanceByTicket(@PathVariable String ticketNumber, Authentication authentication) {
        try {
            String username = authentication.getName();
            Long markedByUserId = userService.findByUsername(username)
                .map(user -> user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
            Attendance attendance = attendanceService.markAttendance(ticketNumber, markedByUserId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Attendance marked successfully");
            response.put("attendanceId", attendance.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PostMapping("/mark/registration/{registrationId}")
    public ResponseEntity<?> markAttendanceByRegistration(@PathVariable Long registrationId, Authentication authentication) {
        try {
            String username = authentication.getName();
            Long markedByUserId = userService.findByUsername(username)
                .map(user -> user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
            Attendance attendance = attendanceService.markAttendanceByRegistrationId(registrationId, markedByUserId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Attendance marked successfully");
            response.put("attendanceId", attendance.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<Attendance>> getEventAttendance(@PathVariable Long eventId) {
        return ResponseEntity.ok(attendanceService.getEventAttendance(eventId));
    }
    
    @GetMapping("/registration/{registrationId}")
    public ResponseEntity<?> getAttendanceByRegistration(@PathVariable Long registrationId) {
        try {
            Attendance attendance = attendanceService.getAttendanceByRegistrationId(registrationId);
            return ResponseEntity.ok(attendance);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<Attendance>> getAllAttendances() {
        return ResponseEntity.ok(attendanceService.getAllAttendances());
    }
}

