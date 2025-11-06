document.addEventListener("DOMContentLoaded", async () => {
  const eventType = localStorage.getItem("selectedEventType");
  const title = document.getElementById("eventTypeTitle");
  const container = document.getElementById("eventsContainer");
  const noEventsMsg = document.getElementById("noEventsMsg");

  if (!eventType) {
    title.textContent = "No Event Type Selected";
    noEventsMsg.classList.remove("hidden");
    return;
  }

  title.textContent = `🎉 ${eventType} Events`;

  try {
    const response = await fetch(`http://localhost:8080/api/events/type/${eventType}`);
    if (!response.ok) throw new Error("Failed to load events");

    const events = await response.json();

    if (events.length === 0) {
      noEventsMsg.classList.remove("hidden");
      return;
    }

    container.innerHTML = "";
    events.forEach(event => {
      const card = document.createElement("div");
      card.className = "event-card";
      card.innerHTML = `
        <img src="${event.imageUrl}" alt="${event.eventName}" class="event-image">
        <div class="event-content">
          <h3>${event.eventName}</h3>
          <p>📍 ${event.location}</p>
          <p>🗓️ ${new Date(event.localDateTime).toLocaleString("en-IN", {
              dateStyle: "medium",
              timeStyle: "short"
          })}</p>
          <div class="event-meta">
            <span>❤️ ${event.likesCount}</span>
            <span>💬 ${event.commentsCount}</span>
          </div>
        </div>
      `;

      card.addEventListener("click", () => {
        localStorage.setItem("selectedEventName", event.eventName);
        window.location.href = "eventDetails.html"; // or your next page
      });

      container.appendChild(card);
    });

  } catch (err) {
    console.error(err);
    noEventsMsg.classList.remove("hidden");
    noEventsMsg.textContent = "⚠️ Unable to load events. Try again later.";
  }
});
