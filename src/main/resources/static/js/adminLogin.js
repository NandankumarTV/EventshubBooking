document.getElementById("adminLoginForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const adminName = document.getElementById("adminName").value.trim();
    const password = document.getElementById("password").value.trim();
    const msg = document.getElementById("loginMsg");

    msg.textContent = "⏳ Authenticating...";
    msg.style.color = "blue";

    try {
        const res = await fetch("http://localhost:8080/api/admin/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ adminName, password })
        });

        const data = await res.json();

        if (data.success) {
            msg.textContent = data.message;
            msg.style.color = "green";
            setTimeout(() => {
                window.location.href = "adminDashboard.html";
            }, 1000);
        } else {
            msg.textContent = data.message;
            msg.style.color = "red";
        }

    } catch (error) {
        console.error("Login error:", error);
        msg.textContent = "⚠️ Server error. Try again.";
        msg.style.color = "red";
    }
});
