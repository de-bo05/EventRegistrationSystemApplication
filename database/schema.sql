-- Event Registration System Database Schema
-- Oracle Database

-- Users table
CREATE TABLE users (
    id NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR2(50) UNIQUE NOT NULL,
    email VARCHAR2(100) UNIQUE NOT NULL,
    password VARCHAR2(255) NOT NULL,
    full_name VARCHAR2(100) NOT NULL,
    role VARCHAR2(10) NOT NULL DEFAULT 'USER' CHECK (role IN ('USER', 'ADMIN')),
    enabled NUMBER(1) NOT NULL DEFAULT 1 CHECK (enabled IN (0, 1)),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_username ON users(username);
CREATE INDEX idx_email ON users(email);

-- Events table
CREATE TABLE events (
    id NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR2(200) NOT NULL,
    description CLOB,
    event_date TIMESTAMP NOT NULL,
    venue VARCHAR2(200) NOT NULL,
    capacity NUMBER(10) NOT NULL,
    registered_count NUMBER(10) NOT NULL DEFAULT 0,
    is_active NUMBER(1) NOT NULL DEFAULT 1 CHECK (is_active IN (0, 1)),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by NUMBER(19) NOT NULL,
    CONSTRAINT fk_events_created_by FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_event_date ON events(event_date);
CREATE INDEX idx_is_active ON events(is_active);
CREATE INDEX idx_created_by ON events(created_by);

-- Registrations table
CREATE TABLE registrations (
    id NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id NUMBER(19) NOT NULL,
    event_id NUMBER(19) NOT NULL,
    ticket_number VARCHAR2(100) UNIQUE NOT NULL,
    qr_code_data CLOB,
    confirmed NUMBER(1) NOT NULL DEFAULT 0 CHECK (confirmed IN (0, 1)),
    confirmed_at TIMESTAMP NULL,
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_registrations_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_registrations_event FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    CONSTRAINT unique_user_event UNIQUE (user_id, event_id)
);

CREATE INDEX idx_ticket_number ON registrations(ticket_number);
CREATE INDEX idx_user_id ON registrations(user_id);
CREATE INDEX idx_event_id ON registrations(event_id);

-- Attendance table
CREATE TABLE attendances (
    id NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    registration_id NUMBER(19) NOT NULL UNIQUE,
    attended NUMBER(1) NOT NULL DEFAULT 0 CHECK (attended IN (0, 1)),
    marked_at TIMESTAMP NULL,
    marked_by NUMBER(19),
    CONSTRAINT fk_attendances_registration FOREIGN KEY (registration_id) REFERENCES registrations(id) ON DELETE CASCADE,
    CONSTRAINT fk_attendances_marked_by FOREIGN KEY (marked_by) REFERENCES users(id) ON DELETE SET NULL
);

CREATE INDEX idx_registration_id ON attendances(registration_id);
CREATE INDEX idx_marked_by ON attendances(marked_by);

-- Insert default admin user (password: admin123)
-- Password is bcrypt hashed: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
INSERT INTO users (username, email, password, full_name, role, enabled) 
VALUES ('admin', 'admin@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'System Administrator', 'ADMIN', 1)
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

