document.addEventListener("DOMContentLoaded", async () => {
  const username = localStorage.getItem("loggedInUser");
  const eventName = localStorage.getItem("selectedEventName");

  if (!eventName) {
    showNotification("⚠️ Event not found!", "error");
    return;
  }

  const eventNameElement = document.getElementById("eventName");
  const eventPriceElement = document.getElementById("eventPrice");
  const numTicketsInput = document.getElementById("numTickets");
  const totalPriceInput = document.getElementById("totalPrice");
  const bookBtn = document.getElementById("bookBtn");
  const availabilityMsg = document.getElementById("availabilityMsg");

  let eventDetails = null;

  // 🧠 Fetch event details from API
  try {
    const res = await fetch(`http://localhost:8080/api/events/name/${eventName}`);
    if (!res.ok) throw new Error("Failed to fetch event details");
    eventDetails = await res.json();
  } catch (err) {
    console.error("❌ Error fetching event details:", err);
    showNotification("❌ Could not load event details. Please try again later.", "error");
    return;
  }

  const { price, availableTickets, location } = eventDetails;

  // 🖊️ Fill UI with event details
  eventNameElement.textContent = eventName;
  eventPriceElement.textContent = price.toFixed(2);

  localStorage.setItem("selectedEventPrice", price);
  localStorage.setItem("selectedEventAvailableTickets", availableTickets);
  localStorage.setItem("selectedEventLocation", location);

  function updateTotalAndCheckAvailability() {
    const tickets = parseInt(numTicketsInput.value) || 0;
    const total = tickets * price;
    totalPriceInput.value = total.toFixed(2);

    if (tickets > availableTickets) {
      availabilityMsg.textContent = `⚠️ Only ${availableTickets} tickets available.`;
      availabilityMsg.style.color = "red";
      bookBtn.disabled = true;
    } else if (tickets <= 0) {
      availabilityMsg.textContent = "Enter a valid ticket count.";
      availabilityMsg.style.color = "orange";
      bookBtn.disabled = true;
    } else {
      availabilityMsg.textContent = `✅ Tickets available (${availableTickets - tickets} left)`;
      availabilityMsg.style.color = "lightgreen";
      bookBtn.disabled = false;
    }
  }

  numTicketsInput.addEventListener("input", updateTotalAndCheckAvailability);
  updateTotalAndCheckAvailability();

  // 🪄 Handle booking submission
  document.getElementById("bookingForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const numberOfTickets = parseInt(numTicketsInput.value);
    const totalAmount = parseFloat(totalPriceInput.value);

    try {
      const res = await fetch("http://localhost:8080/api/events/bookings/add", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          username,
          eventName,
          numberOfTickets,
          totalAmount,
          location
        })
      });

      const data = await res.json();

      if (res.ok) {
        // ✅ Show smooth in-page success notification
        showNotification(
          `🎟️ Booking successful for <b>${eventName}</b><br>
           Tickets: <b>${numberOfTickets}</b> | Total: ₹${totalAmount}`,
          "success"
        );

        numTicketsInput.value = "";
        totalPriceInput.value = "";
        updateTotalAndCheckAvailability();
      } else {
        showNotification(data.message || "Booking failed. Please try again.", "error");
      }
    } catch (err) {
      console.error("❌ Booking error:", err);
      showNotification("⚠️ Error while booking ticket.", "error");
    }
  });

  // 🔔 Notification function
  function showNotification(message, type = "success") {
    let notif = document.getElementById("notification");
    if (!notif) {
      notif = document.createElement("div");
      notif.id = "notification";
      document.body.appendChild(notif);
    }

    notif.innerHTML = message;
    notif.className = `show ${type}`;

    setTimeout(() => notif.classList.remove("show"), 4000);
  }
});
