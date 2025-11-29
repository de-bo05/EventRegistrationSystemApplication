document.addEventListener('DOMContentLoaded', async function() {
    updateNavigation();
    
    const userInfo = getUserInfo();
    const role = getUserRole();
    
    // Show/hide tabs based on role
    if (role === 'ADMIN') {
        document.getElementById('createEventTab').style.display = 'block';
        document.getElementById('myEventsTab').style.display = 'block';
    }
    
    // Load registrations
    await loadRegistrations();
    
    // Load my events if admin
    if (role === 'ADMIN') {
        await loadMyEvents();
    }
    
    // Handle create event form
    const createEventForm = document.getElementById('createEventForm');
    if (createEventForm) {
        createEventForm.addEventListener('submit', async function(e) {
            e.preventDefault();
            await createEvent();
        });
    }
});

async function loadRegistrations() {
    try {
        const registrations = await api.getMyRegistrations();
        displayRegistrations(registrations);
    } catch (error) {
        console.error('Error loading registrations:', error);
        document.getElementById('registrationsList').innerHTML = 
            '<p class="text-danger">Error loading registrations.</p>';
    }
}

function displayRegistrations(registrations) {
    const registrationsList = document.getElementById('registrationsList');
    
    if (registrations.length === 0) {
        registrationsList.innerHTML = '<p>You have no registrations yet. <a href="events.html">Browse events</a></p>';
        return;
    }
    
    registrationsList.innerHTML = registrations.map(reg => `
        <div class="card mb-3">
            <div class="card-body">
                <h5 class="card-title">${reg.event.name}</h5>
                <p class="card-text">
                    <strong>Date:</strong> ${formatDateTime(reg.event.eventDate)}<br>
                    <strong>Venue:</strong> ${reg.event.venue}<br>
                    <strong>Ticket Number:</strong> ${reg.ticketNumber}<br>
                    <strong>Status:</strong> ${reg.confirmed ? '<span class="badge bg-success">Confirmed</span>' : '<span class="badge bg-warning">Pending</span>'}
                </p>
                ${reg.qrCodeData ? `
                    <img src="${reg.qrCodeData}" alt="QR Code" class="qr-code">
                    <button class="btn btn-sm btn-secondary" onclick="showQRCode('${reg.qrCodeData}', '${reg.ticketNumber}')">View QR Code</button>
                ` : ''}
                <button class="btn btn-sm btn-danger mt-2" onclick="cancelRegistration(${reg.id})">Cancel Registration</button>
            </div>
        </div>
    `).join('');
}

async function createEvent() {
    const eventData = {
        name: document.getElementById('eventName').value,
        description: document.getElementById('eventDescription').value,
        eventDate: document.getElementById('eventDate').value,
        venue: document.getElementById('venue').value,
        capacity: parseInt(document.getElementById('capacity').value)
    };
    
    // Convert local datetime to ISO format
    const date = new Date(eventData.eventDate);
    eventData.eventDate = date.toISOString();
    
    const errorDiv = document.getElementById('eventError');
    const successDiv = document.getElementById('eventSuccess');
    
    try {
        const response = await api.createEvent(eventData);
        
        if (response.id) {
            successDiv.textContent = 'Event created successfully!';
            successDiv.style.display = 'block';
            errorDiv.style.display = 'none';
            document.getElementById('createEventForm').reset();
            await loadMyEvents();
        } else {
            errorDiv.textContent = response.error || 'Failed to create event';
            errorDiv.style.display = 'block';
            successDiv.style.display = 'none';
        }
    } catch (error) {
        errorDiv.textContent = 'An error occurred. Please try again.';
        errorDiv.style.display = 'block';
        successDiv.style.display = 'none';
    }
}

async function loadMyEvents() {
    try {
        const events = await api.getMyEvents();
        displayMyEvents(events);
    } catch (error) {
        console.error('Error loading my events:', error);
    }
}

function displayMyEvents(events) {
    const myEventsList = document.getElementById('myEventsList');
    
    if (events.length === 0) {
        myEventsList.innerHTML = '<p>You haven\'t created any events yet.</p>';
        return;
    }
    
    myEventsList.innerHTML = events.map(event => `
        <div class="card mb-3">
            <div class="card-body">
                <h5 class="card-title">${event.name}</h5>
                <p class="card-text">${event.description || 'No description'}</p>
                <p class="card-text">
                    <strong>Date:</strong> ${formatDateTime(event.eventDate)}<br>
                    <strong>Venue:</strong> ${event.venue}<br>
                    <strong>Registered:</strong> ${event.registeredCount}/${event.capacity}
                </p>
                <span class="badge ${event.isActive ? 'bg-success' : 'bg-secondary'}">${event.isActive ? 'Active' : 'Inactive'}</span>
            </div>
        </div>
    `).join('');
}

async function cancelRegistration(registrationId) {
    if (!confirm('Are you sure you want to cancel this registration?')) {
        return;
    }
    
    try {
        const response = await api.cancelRegistration(registrationId);
        if (response.message) {
            alert('Registration cancelled successfully');
            await loadRegistrations();
        } else {
            alert(response.error || 'Failed to cancel registration');
        }
    } catch (error) {
        alert('An error occurred. Please try again.');
    }
}

function showQRCode(qrCode, ticketNumber) {
    const modal = document.createElement('div');
    modal.className = 'modal fade';
    modal.innerHTML = `
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Your Ticket</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body text-center">
                    <p><strong>Ticket Number:</strong> ${ticketNumber}</p>
                    <img src="${qrCode}" alt="QR Code" class="modal-qr-code">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    `;
    document.body.appendChild(modal);
    const bsModal = new bootstrap.Modal(modal);
    bsModal.show();
    modal.addEventListener('hidden.bs.modal', () => modal.remove());
}

function formatDateTime(dateTimeString) {
    const date = new Date(dateTimeString);
    return date.toLocaleString();
}

