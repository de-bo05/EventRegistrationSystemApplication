package com.auca.event_registration_system.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendances")
public class Attendance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_id", nullable = false, unique = true)
    private Registration registration;
    
    @Column(nullable = false)
    private Boolean attended = false;
    
    @Column(name = "marked_at")
    private LocalDateTime markedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marked_by")
    private User markedBy;
    
    // Constructors
    public Attendance() {}
    
    public Attendance(Registration registration, User markedBy) {
        this.registration = registration;
        this.markedBy = markedBy;
        this.attended = true;
        this.markedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Registration getRegistration() {
        return registration;
    }
    
    public void setRegistration(Registration registration) {
        this.registration = registration;
    }
    
    public Boolean getAttended() {
        return attended;
    }
    
    public void setAttended(Boolean attended) {
        this.attended = attended;
        if (attended && markedAt == null) {
            markedAt = LocalDateTime.now();
        }
    }
    
    public LocalDateTime getMarkedAt() {
        return markedAt;
    }
    
    public void setMarkedAt(LocalDateTime markedAt) {
        this.markedAt = markedAt;
    }
    
    public User getMarkedBy() {
        return markedBy;
    }
    
    public void setMarkedBy(User markedBy) {
        this.markedBy = markedBy;
    }
}

