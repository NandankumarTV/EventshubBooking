document.addEventListener("DOMContentLoaded", async () => {
  const username = localStorage.getItem("loggedInUser");
  const bookingsList = document.getElementById("bookingsList");

  if (!username) {
    bookingsList.innerHTML = "<p style='color:red;'>⚠️ Please log in to view your bookings.</p>";
    return;
  }

  try {
    const res = await fetch(`http://localhost:8080/api/events/bookings/user/${username}`);
    if (!res.ok) throw new Error("Failed to fetch bookings");

    const bookings = await res.json();
    console.log("📦 Bookings received:", bookings);

    if (!bookings || bookings.length === 0) {
      bookingsList.innerHTML = "<p>No bookings yet.</p>";
      return;
    }

    // 🟢 No sorting — show in the same order from backend
    bookingsList.innerHTML = bookings.map(renderBookingCard).join("");

    // Attach cancel button handlers
    document.querySelectorAll(".cancel-btn").forEach(btn => {
      btn.addEventListener("click", async (e) => {
        const ticketId = e.target.dataset.ticketId;
        await cancelBooking(ticketId);
      });
    });

  } catch (err) {
    console.error("❌ Error loading bookings:", err);
    bookingsList.innerHTML = "<p style='color:red;'>Failed to load your bookings.</p>";
  }
});

// 🧩 Render each booking card
function renderBookingCard(booking) {
  // ✅ Fixed: Access eventDateTime directly
  const eventDate = new Date(booking.eventDateTime);
  const now = new Date();
  const isPast = eventDate < now;

  const formattedDate = eventDate.toLocaleString("en-IN", {
    dateStyle: "medium",
    timeStyle: "short"
  });

  return `
    <div class="booking-card">
      <div class="booking-details">
        <h3>${booking.eventName}</h3>
        <p>📍 ${booking.location}</p>
        <p>🕓 ${formattedDate}</p>
        <p>🎫 ${booking.numberOfTickets} tickets | 💰 ₹${booking.totalAmount.toFixed(2)}</p>
        <p>🧾 Ticket ID: ${booking.ticketId}</p>
      </div>
      ${
        isPast
          ? `<button class="cancel-btn" disabled>Expired</button>`
          : `<button class="cancel-btn" data-ticket-id="${booking.ticketId}">Cancel</button>`
      }
    </div>
  `;
}

// 🗑️ Cancel booking API
async function cancelBooking(ticketId) {
  try {
    const res = await fetch(`http://localhost:8080/api/events/bookings/cancel/${ticketId}`, {
      method: "DELETE"
    });

    if (res.ok) {
      showNotification("✅ Booking cancelled successfully.", "success");
      const card = document.querySelector(`[data-ticket-id='${ticketId}']`).closest(".booking-card");
      if (card) card.remove();
    } else {
      showNotification("❌ Failed to cancel booking.", "error");
    }
  } catch (err) {
    console.error("Cancel booking error:", err);
    showNotification("⚠️ Error while cancelling booking.", "error");
  }
}

// 🔔 Notification toast system
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
