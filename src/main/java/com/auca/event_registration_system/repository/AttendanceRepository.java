package com.auca.event_registration_system.repository;

import com.auca.event_registration_system.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByRegistration_Id(Long registrationId);
    List<Attendance> findByAttendedTrue();
    List<Attendance> findByRegistration_Event_Id(Long eventId);
}

