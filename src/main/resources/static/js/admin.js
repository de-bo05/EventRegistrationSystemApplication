document.addEventListener('DOMContentLoaded', async function() {
    updateNavigation();
    
    // Check if user is admin
    if (getUserRole() !== 'ADMIN') {
        window.location.href = 'dashboard.html';
        return;
    }
    
    await loadUsers();
    await loadAttendances();
});

async function loadUsers() {
    try {
        const users = await api.getAllUsers();
        displayUsers(users);
    } catch (error) {
        console.error('Error loading users:', error);
        document.getElementById('usersList').innerHTML = 
            '<p class="text-danger">Error loading users.</p>';
    }
}

function displayUsers(users) {
    const usersList = document.getElementById('usersList');
    
    if (users.length === 0) {
        usersList.innerHTML = '<p>No users found.</p>';
        return;
    }
    
    usersList.innerHTML = `
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Full Name</th>
                    <th>Role</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                ${users.map(user => `
                    <tr>
                        <td>${user.id}</td>
                        <td>${user.username}</td>
                        <td>${user.email}</td>
                        <td>${user.fullName}</td>
                        <td><span class="badge ${user.role === 'ADMIN' ? 'bg-danger' : 'bg-primary'}">${user.role}</span></td>
                        <td><span class="badge ${user.enabled ? 'bg-success' : 'bg-secondary'}">${user.enabled ? 'Active' : 'Inactive'}</span></td>
                    </tr>
                `).join('')}
            </tbody>
        </table>
    `;
}

async function markAttendance() {
    const ticketNumber = document.getElementById('ticketNumber').value.trim();
    const messageDiv = document.getElementById('attendanceMessage');
    
    if (!ticketNumber) {
        messageDiv.innerHTML = '<div class="alert alert-warning">Please enter a ticket number.</div>';
        return;
    }
    
    try {
        const response = await api.markAttendance(ticketNumber);
        
        if (response.message) {
            messageDiv.innerHTML = '<div class="alert alert-success">Attendance marked successfully!</div>';
            document.getElementById('ticketNumber').value = '';
            await loadAttendances();
        } else {
            messageDiv.innerHTML = `<div class="alert alert-danger">${response.error || 'Failed to mark attendance'}</div>`;
        }
    } catch (error) {
        messageDiv.innerHTML = '<div class="alert alert-danger">An error occurred. Please try again.</div>';
    }
}

async function loadAttendances() {
    try {
        const attendances = await api.getAllAttendances();
        displayAttendances(attendances);
    } catch (error) {
        console.error('Error loading attendances:', error);
        document.getElementById('attendanceList').innerHTML = 
            '<p class="text-danger">Error loading attendance records.</p>';
    }
}

function displayAttendances(attendances) {
    const attendanceList = document.getElementById('attendanceList');
    
    if (attendances.length === 0) {
        attendanceList.innerHTML = '<p>No attendance records yet.</p>';
        return;
    }
    
    attendanceList.innerHTML = `
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Ticket Number</th>
                    <th>Event</th>
                    <th>User</th>
                    <th>Marked At</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                ${attendances.map(attendance => `
                    <tr>
                        <td>${attendance.id}</td>
                        <td>${attendance.registration.ticketNumber}</td>
                        <td>${attendance.registration.event.name}</td>
                        <td>${attendance.registration.user.fullName}</td>
                        <td>${formatDateTime(attendance.markedAt)}</td>
                        <td><span class="badge bg-success">Attended</span></td>
                    </tr>
                `).join('')}
            </tbody>
        </table>
    `;
}

function formatDateTime(dateTimeString) {
    if (!dateTimeString) return 'N/A';
    const date = new Date(dateTimeString);
    return date.toLocaleString();
}

