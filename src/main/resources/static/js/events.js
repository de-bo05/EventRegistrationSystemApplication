document.addEventListener('DOMContentLoaded', async function() {
    updateNavigation();
    
    try {
        const events = await api.getActiveEvents();
        displayEvents(events);
    } catch (error) {
        console.error('Error loading events:', error);
        document.getElementById('eventsList').innerHTML = 
            '<div class="col-12"><p class="text-center text-danger">Error loading events. Please try again later.</p></div>';
    }
});

function displayEvents(events) {
    const eventsList = document.getElementById('eventsList');
    
    if (events.length === 0) {
        eventsList.innerHTML = '<div class="col-12"><p class="text-center">No active events at the moment.</p></div>';
        return;
    }
    
    eventsList.innerHTML = events.map(event => `
        <div class="col-md-6 mb-4">
            <div class="card event-card h-100">
                <div class="card-body">
                    <h5 class="card-title">${event.name}</h5>
                    <p class="card-text">${event.description || 'No description available'}</p>
                    <ul class="list-unstyled">
                        <li><strong>Date:</strong> ${formatDateTime(event.eventDate)}</li>
                        <li><strong>Venue:</strong> ${event.venue}</li>
                        <li><strong>Available Spots:</strong> ${event.capacity - event.registeredCount} / ${event.capacity}</li>
                    </ul>
                    ${event.capacity - event.registeredCount > 0 
                        ? `<button class="btn btn-primary" onclick="registerForEvent(${event.id})" ${!isLoggedIn() ? 'disabled title="Please login to register"' : ''}>Register Now</button>`
                        : '<span class="badge bg-danger">Event Full</span>'
                    }
                </div>
            </div>
        </div>
    `).join('');
}

async function registerForEvent(eventId) {
    if (!isLoggedIn()) {
        alert('Please login to register for events');
        window.location.href = 'login.html';
        return;
    }
    
    if (!confirm('Are you sure you want to register for this event?')) {
        return;
    }
    
    try {
        const response = await api.registerForEvent(eventId);
        
        if (response.message) {
            alert('Registration successful! Check your email for confirmation and QR code.');
            // Show QR code in modal
            showQRCodeModal(response.qrCode, response.ticketNumber);
            // Reload events
            location.reload();
        } else {
            alert(response.error || 'Registration failed');
        }
    } catch (error) {
        alert('An error occurred. Please try again.');
    }
}

function showQRCodeModal(qrCode, ticketNumber) {
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
                    <p class="mt-3">Please save this QR code. You'll need it at the event.</p>
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

