const editEventForm = document.getElementById("editEventForm");
const eventNameHidden = document.getElementById("eventName");
const responseMsg = document.getElementById("responseMsg");
const submitBtn = document.getElementById("updateEventBtn");
const totalSeatsInput = document.getElementById("totalSeats");
const availableSeatsInput = document.getElementById("availableSeats");
const eventDateInput = document.getElementById("eventDateTime");
const descriptionInput = document.getElementById("description");

// ✅ Load event details by eventName from URL param
window.addEventListener("DOMContentLoaded", () => {
  const params = new URLSearchParams(window.location.search);
  const eventName = params.get("eventName");

  if (!eventName) {
    responseMsg.textContent = "❌ No event specified";
    responseMsg.style.color = "red";
    submitBtn.disabled = true;
    return;
  }

  eventNameHidden.value = eventName;

  fetch(`http://localhost:8080/api/admin/event/get?eventName=${encodeURIComponent(eventName)}`)
    .then(res => res.json())
    .then(event => {
      document.getElementById("imageUrl").value = event.imageUrl;
      document.getElementById("location").value = event.location;
      document.getElementById("eventType").value = event.eventType;
      eventDateInput.value = event.eventDateTime;
      totalSeatsInput.value = event.totalSeats;
      availableSeatsInput.value = event.availableSeats;
      document.getElementById("price").value = event.price;
      descriptionInput.value = event.description;
    })
    .catch(err => {
      console.error(err);
      responseMsg.textContent = "⚠️ Failed to load event details";
      responseMsg.style.color = "red";
    });
});

// ✅ Validate seats
function validateSeats() {
  const total = parseInt(totalSeatsInput.value);
  const available = parseInt(availableSeatsInput.value);

  if (available > total) {
    responseMsg.textContent = "⚠️ Available seats cannot exceed total seats.";
    responseMsg.style.color = "red";
    submitBtn.disabled = true;
    return false;
  }
  responseMsg.textContent = "";
  submitBtn.disabled = false;
  return true;
}
totalSeatsInput.addEventListener("input", validateSeats);
availableSeatsInput.addEventListener("input", validateSeats);

// ✅ Validate date/time
function validateDateTime() {
  const selected = new Date(eventDateInput.value);
  const now = new Date();
  if (selected < now) {
    responseMsg.textContent = "⚠️ Event date/time cannot be in the past.";
    responseMsg.style.color = "red";
    submitBtn.disabled = true;
    return false;
  }
  responseMsg.textContent = "";
  submitBtn.disabled = false;
  return true;
}
eventDateInput.addEventListener("input", validateDateTime);

// ✅ Handle form submission
editEventForm.addEventListener("submit", (e) => {
  e.preventDefault();
  if (!validateSeats() || !validateDateTime()) return;

  const eventData = {
    eventName: eventNameHidden.value.trim(),
    imageUrl: document.getElementById("imageUrl").value.trim(),
    location: document.getElementById("location").value.trim(),
    eventType: document.getElementById("eventType").value.trim(),
    eventDateTime: eventDateInput.value,
    totalSeats: parseInt(totalSeatsInput.value),
    availableSeats: parseInt(availableSeatsInput.value),
    price: parseFloat(document.getElementById("price").value),
    description: descriptionInput.value.trim()
  };

  fetch("http://localhost:8080/api/admin/event/update", {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(eventData)
  })
    .then(async res => {
      if (!res.ok) {
        const error = await res.json();
        throw new Error(error.message || "Error updating event");
      }
      return res.text();
    })
    .then(() => {
      responseMsg.textContent = "✅ Event updated successfully!";
      responseMsg.style.color = "green";

      // Optional: briefly disable button to prevent double-clicks
      submitBtn.disabled = true;
      setTimeout(() => (submitBtn.disabled = false), 1500);
    })
    .catch(err => {
      console.error(err);
      responseMsg.textContent = "⚠️ " + err.message;
      responseMsg.style.color = "red";
    });
});
