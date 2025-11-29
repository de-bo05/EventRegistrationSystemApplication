document.addEventListener('DOMContentLoaded', async function() {
    try {
        const events = await api.getUpcomingEvents();
        displayEvents(events);
    } catch (error) {
        console.error('Error loading events:', error);
    }
});

function displayEvents(events) {
    const eventsList = document.getElementById('eventsList');
    
    if (!eventsList) return;
    
    if (events.length === 0) {
        eventsList.innerHTML = '<div class="col-12"><p class="text-center">No upcoming events at the moment.</p></div>';
        return;
    }
    
    eventsList.innerHTML = events.map(event => `
        <div class="col-md-4 mb-4">
            <div class="card event-card h-100">
                <div class="card-body">
                    <h5 class="card-title">${event.name}</h5>
                    <p class="card-text">${event.description || 'No description'}</p>
                    <p class="card-text"><small class="text-muted">
                        <strong>Date:</strong> ${formatDateTime(event.eventDate)}<br>
                        <strong>Venue:</strong> ${event.venue}<br>
                        <strong>Capacity:</strong> ${event.registeredCount}/${event.capacity}
                    </small></p>
                    <a href="events.html" class="btn btn-primary">View Details</a>
                </div>
            </div>
        </div>
    `).join('');
}

function formatDateTime(dateTimeString) {
    const date = new Date(dateTimeString);
    return date.toLocaleString();
}

