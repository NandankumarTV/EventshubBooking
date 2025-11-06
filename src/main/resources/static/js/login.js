const loginForm = document.getElementById('loginForm');
const responseMsg = document.getElementById('responseMsg');

loginForm.addEventListener('submit', (e) => {
    e.preventDefault();

    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value;

    if (!username || !password) {
        responseMsg.style.color = 'red';
        responseMsg.textContent = 'Please enter both username and password.';
        return;
    }

    fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password })
    })
    .then(res => res.json())
    .then(data => {
        // Use message from backend
        responseMsg.textContent = data.message || 'Unexpected response from server.';
        responseMsg.style.color = data.success ? 'green' : 'red';

        if (data.success) {

         localStorage.setItem('loggedInUser', username);

            setTimeout(() => {
                window.location.href = 'loggedHome.html';
            }, 1200);
        }
    })
    .catch(err => {
        console.error(err);
        responseMsg.style.color = 'red';
        responseMsg.textContent = 'Error connecting to server.';
    });
});
