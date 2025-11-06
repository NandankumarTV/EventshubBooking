const usernameInput = document.getElementById('username');
const registerBtn = document.getElementById('registerBtn');
const usernameMsg = document.getElementById('usernameMsg');
const registerForm = document.getElementById('registerForm');
const responseMsg = document.getElementById('responseMsg');

let isUsernameAvailable = false;

// Check username availability
usernameInput.addEventListener('input', () => {
    const username = usernameInput.value.trim();
    if (!username) {
        usernameMsg.textContent = '';
        registerBtn.disabled = true;
        isUsernameAvailable = false;
        return;
    }

    fetch(`/api/users/check-username?username=${encodeURIComponent(username)}`)
       .then(res => res.json())
       .then(data => {
           usernameMsg.textContent = data.message;
           usernameMsg.style.color = data.available ? 'green' : 'red';
           isUsernameAvailable = data.available; // <-- FIX: set this
           registerBtn.disabled = !isUsernameAvailable;
       })
       .catch(err => {
           console.error(err);
           usernameMsg.textContent = 'Error checking username';
           usernameMsg.style.color = 'red';
           registerBtn.disabled = true;
           isUsernameAvailable = false;
       });
});

// Handle form submission
registerForm.addEventListener('submit', (e) => {
    e.preventDefault();
    if (!isUsernameAvailable) return;

    const userData = {
        username: usernameInput.value.trim(),
        name: document.getElementById('name').value.trim(),
        password: document.getElementById('password').value
    };

    fetch('http://localhost:8080/api/users/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(userData)
    })
    .then(res => res.json())
    .then(data => {
        responseMsg.style.color = data.success ? 'green' : 'red';
        responseMsg.textContent = data.message;

        if (data.success) {
            setTimeout(() => {
                window.location.href = 'login.html';
            }, 1500);
        }
    })
    .catch(err => {
        console.error(err);
        responseMsg.style.color = 'red';
        responseMsg.textContent = 'Error registering user';
    });
});
