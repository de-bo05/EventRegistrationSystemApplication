const API_BASE_URL = '/api';
// API endpoints are prefixed with /api

// Helper function to get auth token
function getAuthToken() {
    return localStorage.getItem('token');
}

// Helper function to set auth headers
function getAuthHeaders() {
    const token = getAuthToken();
    return {
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : ''
    };
}

// API Functions
const api = {
    // Auth
    register: async (userData) => {
        const response = await fetch(`${API_BASE_URL}/auth/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        });
        return response.json();
    },
    
    login: async (credentials) => {
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(credentials)
        });
        return response.json();
    },
    
    // Events
    getActiveEvents: async () => {
        const response = await fetch(`${API_BASE_URL}/events/public/active`);
        return response.json();
    },
    
    getUpcomingEvents: async () => {
        const response = await fetch(`${API_BASE_URL}/events/public/upcoming`);
        return response.json();
    },
    
    getEventById: async (id) => {
        const response = await fetch(`${API_BASE_URL}/events/public/${id}`);
        return response.json();
    },
    
    createEvent: async (eventData) => {
        const response = await fetch(`${API_BASE_URL}/events`, {
            method: 'POST',
            headers: getAuthHeaders(),
            body: JSON.stringify(eventData)
        });
        return response.json();
    },
    
    getMyEvents: async () => {
        const response = await fetch(`${API_BASE_URL}/events/my-events`, {
            headers: getAuthHeaders()
        });
        return response.json();
    },
    
    // Registrations
    registerForEvent: async (eventId) => {
        const response = await fetch(`${API_BASE_URL}/registrations/event/${eventId}`, {
            method: 'POST',
            headers: getAuthHeaders()
        });
        return response.json();
    },
    
    getMyRegistrations: async () => {
        const response = await fetch(`${API_BASE_URL}/registrations/my-registrations`, {
            headers: getAuthHeaders()
        });
        return response.json();
    },
    
    getRegistrationByTicket: async (ticketNumber) => {
        const response = await fetch(`${API_BASE_URL}/registrations/ticket/${ticketNumber}`, {
            headers: getAuthHeaders()
        });
        return response.json();
    },
    
    cancelRegistration: async (registrationId) => {
        const response = await fetch(`${API_BASE_URL}/registrations/${registrationId}`, {
            method: 'DELETE',
            headers: getAuthHeaders()
        });
        return response.json();
    },
    
    // Attendance
    markAttendance: async (ticketNumber) => {
        const response = await fetch(`${API_BASE_URL}/attendance/mark/ticket/${ticketNumber}`, {
            method: 'POST',
            headers: getAuthHeaders()
        });
        return response.json();
    },
    
    getAllAttendances: async () => {
        const response = await fetch(`${API_BASE_URL}/attendance/all`, {
            headers: getAuthHeaders()
        });
        return response.json();
    },
    
    // Admin
    getAllUsers: async () => {
        const response = await fetch(`${API_BASE_URL}/admin/users`, {
            headers: getAuthHeaders()
        });
        return response.json();
    }
};

