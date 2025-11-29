package com.auca.event_registration_system.factory;

import com.auca.event_registration_system.model.Role;
import com.auca.event_registration_system.model.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

/**
 * Factory Pattern Implementation for User Roles
 * Creates and manages user roles with appropriate authorities
 */
@Component
public class RoleFactory {
    
    /**
     * Creates a user with the specified role
     * @param role The role to assign
     * @return User with the role set
     */
    public User createUserWithRole(Role role) {
        User user = new User();
        user.setRole(role);
        return user;
    }
    
    /**
     * Gets authorities for a given role
     * @param role The role
     * @return Set of authorities
     */
    public Set<SimpleGrantedAuthority> getAuthorities(Role role) {
        if (role == Role.ADMIN) {
            return Set.of(
                new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_USER")
            );
        } else {
            return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }
    
    /**
     * Checks if a role has admin privileges
     * @param role The role to check
     * @return true if admin, false otherwise
     */
    public boolean isAdmin(Role role) {
        return role == Role.ADMIN;
    }
    
    /**
     * Creates role from string
     * @param roleString String representation of role
     * @return Role enum
     */
    public Role createRoleFromString(String roleString) {
        try {
            return Role.valueOf(roleString.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Role.USER; // Default to USER if invalid
        }
    }
}

