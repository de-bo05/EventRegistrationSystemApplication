package com.auca.event_registration_system.service;

import com.auca.event_registration_system.dto.EventDTO;
import com.auca.event_registration_system.model.Event;
import com.auca.event_registration_system.model.User;
import com.auca.event_registration_system.repository.EventRepository;
import com.auca.event_registration_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventService {
    
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    
    @Autowired
    public EventService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }
    
    public Event createEvent(EventDTO eventDTO, Long createdById) {
        User createdBy = userRepository.findById(createdById)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        Event event = new Event();
        event.setName(eventDTO.getName());
        event.setDescription(eventDTO.getDescription());
        event.setEventDate(eventDTO.getEventDate());
        event.setVenue(eventDTO.getVenue());
        event.setCapacity(eventDTO.getCapacity());
        event.setCreatedBy(createdBy);
        event.setRegisteredCount(0);
        event.setIsActive(true);
        
        return eventRepository.save(event);
    }
    
    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<EventDTO> getActiveEvents() {
        return eventRepository.findByIsActiveTrueOrderByEventDateAsc().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<EventDTO> getUpcomingEvents() {
        return eventRepository.findUpcomingEvents(LocalDateTime.now()).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public EventDTO getEventById(Long id) {
        Event event = eventRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Event not found"));
        return convertToDTO(event);
    }
    
    public List<EventDTO> getEventsByCreator(Long userId) {
        return eventRepository.findByCreatedBy_Id(userId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public Event updateEvent(Long id, EventDTO eventDTO) {
        Event event = eventRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Event not found"));
        
        if (eventDTO.getName() != null) {
            event.setName(eventDTO.getName());
        }
        if (eventDTO.getDescription() != null) {
            event.setDescription(eventDTO.getDescription());
        }
        if (eventDTO.getEventDate() != null) {
            event.setEventDate(eventDTO.getEventDate());
        }
        if (eventDTO.getVenue() != null) {
            event.setVenue(eventDTO.getVenue());
        }
        if (eventDTO.getCapacity() != null) {
            event.setCapacity(eventDTO.getCapacity());
        }
        if (eventDTO.getIsActive() != null) {
            event.setIsActive(eventDTO.getIsActive());
        }
        
        return eventRepository.save(event);
    }
    
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new RuntimeException("Event not found");
        }
        eventRepository.deleteById(id);
    }
    
    private EventDTO convertToDTO(Event event) {
        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setDescription(event.getDescription());
        dto.setEventDate(event.getEventDate());
        dto.setVenue(event.getVenue());
        dto.setCapacity(event.getCapacity());
        dto.setRegisteredCount(event.getRegisteredCount());
        dto.setIsActive(event.getIsActive());
        dto.setCreatedAt(event.getCreatedAt());
        if (event.getCreatedBy() != null) {
            dto.setCreatedById(event.getCreatedBy().getId());
            dto.setCreatedByUsername(event.getCreatedBy().getUsername());
        }
        return dto;
    }
}

