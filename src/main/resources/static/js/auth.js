// Check if user is logged in
function isLoggedIn() {
    return localStorage.getItem('token') !== null;
}

// Get user role
function getUserRole() {
    return localStorage.getItem('role');
}

// Get user info
function getUserInfo() {
    return {
        username: localStorage.getItem('username'),
        role: localStorage.getItem('role'),
        userId: localStorage.getItem('userId'),
        fullName: localStorage.getItem('fullName')
    };
}

// Update navigation based on auth status
function updateNavigation() {
    const isLogged = isLoggedIn();
    const role = getUserRole();
    
    const loginNav = document.getElementById('loginNav');
    const registerNav = document.getElementById('registerNav');
    const dashboardNav = document.getElementById('dashboardNav');
    const adminNav = document.getElementById('adminNav');
    const logoutNav = document.getElementById('logoutNav');
    
    if (isLogged) {
        if (loginNav) loginNav.style.display = 'none';
        if (registerNav) registerNav.style.display = 'none';
        if (dashboardNav) dashboardNav.style.display = 'block';
        if (logoutNav) logoutNav.style.display = 'block';
        if (adminNav && role === 'ADMIN') {
            adminNav.style.display = 'block';
        }
    } else {
        if (loginNav) loginNav.style.display = 'block';
        if (registerNav) registerNav.style.display = 'block';
        if (dashboardNav) dashboardNav.style.display = 'none';
        if (adminNav) adminNav.style.display = 'none';
        if (logoutNav) logoutNav.style.display = 'none';
    }
}

// Logout function
function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('role');
    localStorage.removeItem('userId');
    localStorage.removeItem('fullName');
    window.location.href = 'index.html';
}

// Check auth on page load
document.addEventListener('DOMContentLoaded', function() {
    updateNavigation();
    
    // Protect pages that require authentication
    const protectedPages = ['dashboard.html', 'admin.html'];
    const currentPage = window.location.pathname.split('/').pop();
    
    if (protectedPages.includes(currentPage) && !isLoggedIn()) {
        window.location.href = 'login.html';
    }
    
    // Protect admin page
    if (currentPage === 'admin.html' && getUserRole() !== 'ADMIN') {
        window.location.href = 'dashboard.html';
    }
});

