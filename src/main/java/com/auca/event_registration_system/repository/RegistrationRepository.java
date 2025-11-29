package com.auca.event_registration_system.repository;

import com.auca.event_registration_system.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    Optional<Registration> findByTicketNumber(String ticketNumber);
    List<Registration> findByUser_Id(Long userId);
    List<Registration> findByEvent_Id(Long eventId);
    Boolean existsByUser_IdAndEvent_Id(Long userId, Long eventId);
    
    @Query("SELECT r FROM Registration r WHERE r.event.id = :eventId AND r.confirmed = true")
    List<Registration> findConfirmedRegistrationsByEventId(Long eventId);
}

