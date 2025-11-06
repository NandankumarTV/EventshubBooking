// Redirect if not logged in
const username = localStorage.getItem('loggedInUser');
if (!username) {
    window.location.href = 'login.html';
}

// Fetch user's name using API
fetch(`http://localhost:8080/api/users/name?username=${username}`)
    .then(res => res.json())
    .then(data => {
        document.getElementById('welcomeMsg').textContent = `Welcome, ${data.name}! 👋`;
    })
    .catch(err => {
        console.error('Error fetching name:', err);
        document.getElementById('welcomeMsg').textContent = `Welcome, ${username}!`;
    });

// Handle event type clicks
document.querySelectorAll('.category').forEach(card => {
    card.addEventListener('click', () => {
        const eventType = card.getAttribute('data-type');
        localStorage.setItem('selectedEventType', eventType);
        window.location.href = 'eventsPage.html';
    });
});

// Go to bookings page
document.getElementById('myBookingsBtn').addEventListener('click', () => {
    window.location.href = 'myBookings.html';
});
