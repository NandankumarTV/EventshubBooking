const createEventBtn = document.getElementById("createEventBtn");
const searchBtn = document.getElementById("searchBtn");
const searchInput = document.getElementById("searchInput");
const resultsContainer = document.getElementById("results");

// Redirect to create event page
createEventBtn.addEventListener("click", () => {
    window.location.href = "createEvent.html";
});

// Search events by name
searchBtn.addEventListener("click", () => {
    const searchTerm = searchInput.value.trim();

    if (!searchTerm) {
        resultsContainer.innerHTML = "<p style='color:red;text-align:center;'>Please enter an event name to search.</p>";
        return;
    }

    fetch(`http://localhost:8080/api/admin/event/search?eventName=${encodeURIComponent(searchTerm)}`)
        .then(res => {
            if (!res.ok) throw new Error("No events found");
            return res.json();
        })
        .then(data => {
            resultsContainer.innerHTML = "";
            const now = new Date();

            data.forEach(event => {
                const eventDate = new Date(event.localDateTime);

                const card = document.createElement("div");
                card.classList.add("event-card");

                card.innerHTML = `
                    <img src="${event.imageUrl}" alt="${event.eventName}">
                    <div class="event-info">
                        <h3>${event.eventName}</h3>
                        <p>📍 ${event.location}</p>
                        <p>🗓 ${eventDate.toLocaleDateString()} ⏰ ${eventDate.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'})}</p>
                        <p>❤️ ${event.likesCount} Likes | 💬 ${event.commentsCount} Comments</p>
                    </div>
                    <div class="event-actions"></div>
                `;

                const actionsDiv = card.querySelector(".event-actions");

                // Only show buttons if event is in the future
                if (eventDate > now) {
                    const editBtn = document.createElement("button");
                    editBtn.textContent = "✏️ Edit";
                    editBtn.classList.add("edit-btn");
                    editBtn.addEventListener("click", () => {
                        window.location.href = `editEvent.html?eventName=${encodeURIComponent(event.eventName)}`;
                    });

                    const cancelBtn = document.createElement("button");
                    cancelBtn.textContent = "❌ Cancel";
                    cancelBtn.classList.add("cancel-btn");
                    cancelBtn.addEventListener("click", () => {
                        if (confirm("Are you sure you want to cancel this event?")) {
                            fetch(`http://localhost:8080/api/admin/event/cancel?eventName=${encodeURIComponent(event.eventName)}`, {
                                method: "DELETE"
                            })
                            .then(res => {
                                if (!res.ok) throw new Error("Failed to cancel event");
                                // Refresh search results
                                searchBtn.click();
                            })
                            .catch(err => alert(err.message));
                        }
                    });

                    actionsDiv.appendChild(editBtn);
                    actionsDiv.appendChild(cancelBtn);
                }

                resultsContainer.appendChild(card);
            });
        })
        .catch(err => {
            resultsContainer.innerHTML = `<p style='color:red;text-align:center;'>${err.message}</p>`;
        });
});
