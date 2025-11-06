const eventForm = document.getElementById("eventForm");
const eventNameInput = document.getElementById("eventName");
const eventNameMsg = document.getElementById("eventNameMsg");
const responseMsg = document.getElementById("responseMsg");
const submitBtn = document.getElementById("createEventBtn");
const totalSeatsInput = document.getElementById("totalSeats");
const availableSeatsInput = document.getElementById("availableSeats");
const eventDateInput = document.getElementById("eventDateTime");
const descriptionInput = document.getElementById("description");

let isEventNameAvailable = false;

// ✅ Check event name availability
eventNameInput.addEventListener("input", () => {
    const eventName = eventNameInput.value.trim();
    if (!eventName) {
        eventNameMsg.textContent = "";
        isEventNameAvailable = false;
        validateForm();
        return;
    }

    fetch(`http://localhost:8080/api/admin/event/check-availability?eventName=${encodeURIComponent(eventName)}`)
        .then(async res => {
            if (!res.ok) {
                const error = await res.json();
                throw new Error(error.message || "Error checking event name");
            }
            return res.json();
        })
        .then(data => {
            if (data.available) {
                eventNameMsg.textContent = "✅ Event name available!";
                eventNameMsg.style.color = "green";
                isEventNameAvailable = true;
            } else {
                eventNameMsg.textContent = "❌ Event name already exists!";
                eventNameMsg.style.color = "red";
                isEventNameAvailable = false;
            }
            validateForm();
        })
        .catch(err => {
            console.error(err);
            eventNameMsg.textContent = "⚠️ " + err.message;
            eventNameMsg.style.color = "red";
            isEventNameAvailable = false;
            validateForm();
        });
});

// ✅ Validate seats
function validateSeats() {
    const total = parseInt(totalSeatsInput.value);
    const available = parseInt(availableSeatsInput.value);

    if (!total || !available) return false;

    if (available > total) {
        responseMsg.textContent = "⚠️ Available seats cannot exceed total seats.";
        responseMsg.style.color = "red";
        return false;
    }

    responseMsg.textContent = "";
    return true;
}

totalSeatsInput.addEventListener("input", validateForm);
availableSeatsInput.addEventListener("input", validateForm);

// ✅ Validate date/time
function validateDateTime() {
    if (!eventDateInput.value) return false;
    const selected = new Date(eventDateInput.value);
    const now = new Date();
    if (selected < now) {
        responseMsg.textContent = "⚠️ Event date/time cannot be in the past.";
        responseMsg.style.color = "red";
        return false;
    }
    responseMsg.textContent = "";
    return true;
}

eventDateInput.addEventListener("input", validateForm);

// ✅ Enable submit only if all validations pass
function validateForm() {
    submitBtn.disabled = !(isEventNameAvailable && validateSeats() && validateDateTime());
}

// ✅ Handle form submission
eventForm.addEventListener("submit", (e) => {
    e.preventDefault();

    if (!isEventNameAvailable) {
        responseMsg.textContent = "❌ Please use a different event name.";
        responseMsg.style.color = "red";
        return;
    }

    if (!validateSeats() || !validateDateTime()) return;

    const eventData = {
        eventName: eventNameInput.value.trim(),
        imageUrl: document.getElementById("imageUrl").value.trim(),
        location: document.getElementById("location").value.trim(),
        eventType: document.getElementById("eventType").value.trim(),
        eventDateTime: eventDateInput.value,
        totalSeats: parseInt(totalSeatsInput.value),
        availableSeats: parseInt(availableSeatsInput.value),
        price: parseFloat(document.getElementById("price").value),
        description: descriptionInput.value.trim()
    };

    fetch("http://localhost:8080/api/admin/event/create", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(eventData)
    })
    .then(async res => {
        if (!res.ok) {
            const error = await res.json();
            throw new Error(error.message || "Error creating event");
        }
        return res.text();
    })
    .then(message => {
        responseMsg.textContent = "✅ " + message;
        responseMsg.style.color = "green";
        eventForm.reset();
        isEventNameAvailable = false;
        submitBtn.disabled = true;
    })
    .catch(err => {
        console.error(err);
        responseMsg.textContent = "⚠️ " + err.message;
        responseMsg.style.color = "red";
    });
});
