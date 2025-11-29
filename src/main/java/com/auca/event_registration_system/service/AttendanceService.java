package com.auca.event_registration_system.service;

import com.auca.event_registration_system.model.Attendance;
import com.auca.event_registration_system.model.Registration;
import com.auca.event_registration_system.model.User;
import com.auca.event_registration_system.repository.AttendanceRepository;
import com.auca.event_registration_system.repository.RegistrationRepository;
import com.auca.event_registration_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AttendanceService {
    
    private final AttendanceRepository attendanceRepository;
    private final RegistrationRepository registrationRepository;
    private final UserRepository userRepository;
    
    @Autowired
    public AttendanceService(AttendanceRepository attendanceRepository,
                            RegistrationRepository registrationRepository,
                            UserRepository userRepository) {
        this.attendanceRepository = attendanceRepository;
        this.registrationRepository = registrationRepository;
        this.userRepository = userRepository;
    }
    
    public Attendance markAttendance(String ticketNumber, Long markedByUserId) {
        Registration registration = registrationRepository.findByTicketNumber(ticketNumber)
            .orElseThrow(() -> new RuntimeException("Invalid ticket number"));
        
        if (!registration.getConfirmed()) {
            throw new RuntimeException("Registration not confirmed");
        }
        
        // Check if attendance already marked
        if (attendanceRepository.findByRegistration_Id(registration.getId()).isPresent()) {
            throw new RuntimeException("Attendance already marked");
        }
        
        User markedBy = userRepository.findById(markedByUserId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        Attendance attendance = new Attendance(registration, markedBy);
        return attendanceRepository.save(attendance);
    }
    
    public Attendance markAttendanceByRegistrationId(Long registrationId, Long markedByUserId) {
        Registration registration = registrationRepository.findById(registrationId)
            .orElseThrow(() -> new RuntimeException("Registration not found"));
        
        if (!registration.getConfirmed()) {
            throw new RuntimeException("Registration not confirmed");
        }
        
        if (attendanceRepository.findByRegistration_Id(registrationId).isPresent()) {
            throw new RuntimeException("Attendance already marked");
        }
        
        User markedBy = userRepository.findById(markedByUserId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        Attendance attendance = new Attendance(registration, markedBy);
        return attendanceRepository.save(attendance);
    }
    
    public List<Attendance> getEventAttendance(Long eventId) {
        return attendanceRepository.findByRegistration_Event_Id(eventId);
    }
    
    public Attendance getAttendanceByRegistrationId(Long registrationId) {
        return attendanceRepository.findByRegistration_Id(registrationId)
            .orElseThrow(() -> new RuntimeException("Attendance not found"));
    }
    
    public List<Attendance> getAllAttendances() {
        return attendanceRepository.findAll();
    }
}

