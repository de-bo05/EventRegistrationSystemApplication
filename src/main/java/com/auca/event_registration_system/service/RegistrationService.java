package com.auca.event_registration_system.service;

import com.auca.event_registration_system.model.Event;
import com.auca.event_registration_system.model.Registration;
import com.auca.event_registration_system.model.User;
import com.auca.event_registration_system.repository.EventRepository;
import com.auca.event_registration_system.repository.RegistrationRepository;
import com.auca.event_registration_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RegistrationService {
    
    private final RegistrationRepository registrationRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final QRCodeService qrCodeService;
    private final EmailService emailService;
    
    @Autowired
    public RegistrationService(RegistrationRepository registrationRepository,
                              UserRepository userRepository,
                              EventRepository eventRepository,
                              QRCodeService qrCodeService,
                              EmailService emailService) {
        this.registrationRepository = registrationRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.qrCodeService = qrCodeService;
        this.emailService = emailService;
    }
    
    public Registration registerForEvent(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Event not found"));
        
        if (!event.getIsActive()) {
            throw new RuntimeException("Event is not active");
        }
        
        if (event.isFull()) {
            throw new RuntimeException("Event is full");
        }
        
        if (registrationRepository.existsByUser_IdAndEvent_Id(userId, eventId)) {
            throw new RuntimeException("User is already registered for this event");
        }
        
        Registration registration = new Registration(user, event);
        
        // Generate QR code
        String qrData = qrCodeService.generateTicketQRData(
            registration.getTicketNumber(), eventId, userId);
        String qrCodeBase64 = qrCodeService.generateQRCodeBase64(qrData);
        registration.setQrCodeData(qrCodeBase64);
        
        // Save registration
        registration = registrationRepository.save(registration);
        
        // Update event registered count
        event.setRegisteredCount(event.getRegisteredCount() + 1);
        eventRepository.save(event);
        
        // Confirm registration and send email
        registration.setConfirmed(true);
        registration = registrationRepository.save(registration);
        
        // Send confirmation email
        try {
            emailService.sendRegistrationConfirmation(
                user.getEmail(),
                user.getFullName(),
                event.getName(),
                event.getEventDate().toString(),
                event.getVenue(),
                registration.getTicketNumber(),
                qrCodeBase64
            );
        } catch (Exception e) {
            // Log error but don't fail registration
            System.err.println("Failed to send email: " + e.getMessage());
        }
        
        return registration;
    }
    
    public List<Registration> getUserRegistrations(Long userId) {
        return registrationRepository.findByUser_Id(userId);
    }
    
    public List<Registration> getEventRegistrations(Long eventId) {
        return registrationRepository.findByEvent_Id(eventId);
    }
    
    public Registration getRegistrationByTicketNumber(String ticketNumber) {
        return registrationRepository.findByTicketNumber(ticketNumber)
            .orElseThrow(() -> new RuntimeException("Registration not found"));
    }
    
    public Registration getRegistrationById(Long id) {
        return registrationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Registration not found"));
    }
    
    public void cancelRegistration(Long registrationId) {
        Registration registration = registrationRepository.findById(registrationId)
            .orElseThrow(() -> new RuntimeException("Registration not found"));
        
        Event event = registration.getEvent();
        event.setRegisteredCount(event.getRegisteredCount() - 1);
        eventRepository.save(event);
        
        registrationRepository.delete(registration);
    }
}

