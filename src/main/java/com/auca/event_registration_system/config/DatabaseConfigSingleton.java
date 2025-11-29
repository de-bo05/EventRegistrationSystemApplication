package com.auca.event_registration_system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Singleton Pattern Implementation for Database Configuration
 * Ensures a single instance of database configuration throughout the application
 */
@Component
public class DatabaseConfigSingleton {
    
    private static DatabaseConfigSingleton instance;
    
    @Value("${spring.datasource.url}")
    private String url;
    
    @Value("${spring.datasource.username}")
    private String username;
    
    @Value("${spring.datasource.password}")
    private String password;
    
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
    
    // Private constructor to prevent instantiation
    private DatabaseConfigSingleton() {
        if (instance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }
    
    // Thread-safe singleton with double-checked locking
    public static synchronized DatabaseConfigSingleton getInstance() {
        if (instance == null) {
            synchronized (DatabaseConfigSingleton.class) {
                if (instance == null) {
                    instance = new DatabaseConfigSingleton();
                }
            }
        }
        return instance;
    }
    
    // Getters
    public String getUrl() {
        return url;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getDriverClassName() {
        return driverClassName;
    }
    
    // Prevent cloning
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Singleton instance cannot be cloned");
    }
    
    // Prevent deserialization
    protected Object readResolve() {
        return getInstance();
    }
}

