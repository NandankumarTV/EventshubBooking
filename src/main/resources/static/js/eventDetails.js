document.addEventListener("DOMContentLoaded", async () => {
  const username = localStorage.getItem("loggedInUser");
  const eventName = localStorage.getItem("selectedEventName");

  console.log("🔹 From localStorage:", { username, eventName });

  if (!eventName || !username) {
    alert("No event selected or user not logged in!");
    return;
  }

  try {
    const res = await fetch(
      `http://localhost:8080/api/events/names/${eventName}?username=${username}`
    );
    if (!res.ok) throw new Error("Failed to fetch event details");

    const eventData = await res.json();
    renderEventDetails(eventData);
    setupLikeButton(eventData, username);
    setupCommentBox(eventData, username);
    setupBookButton();

  } catch (err) {
    console.error("❌ Unable to load event details:", err);
    alert("Unable to load event details.");
  }
});

function renderEventDetails(eventData) {
  document.getElementById("eventImage").src = eventData.imageUrl;
  document.getElementById("eventImage").alt = eventData.eventName;
  document.getElementById("eventName").textContent = eventData.eventName;
  document.getElementById("eventLocation").textContent = `📍 ${eventData.location}`;
  document.getElementById("eventDescription").textContent = eventData.description;
  document.getElementById("eventDateTime").textContent = new Date(eventData.eventDateTime).toLocaleString("en-IN", {
    dateStyle: "medium",
    timeStyle: "short"
  });
  document.getElementById("eventType").textContent = eventData.eventType;
  document.getElementById("eventPrice").textContent = eventData.price;

  document.getElementById("likeCount").textContent = eventData.likesCount || 0;
  document.getElementById("commentCount").textContent = eventData.commentsCount || 0;

  document.getElementById("likeBtn").style.color = eventData.likedByUser ? "red" : "black";
  console.log("✅ Rendered event:", eventData.eventName);
}

// Like button
function setupLikeButton(eventData, username) {
  const likeBtn = document.getElementById("likeBtn");
  const likeCount = document.getElementById("likeCount");

  // 🧩 Set initial state
  updateLikeVisual(likeBtn, eventData.likedByUser);

  likeBtn.addEventListener("click", async (e) => {
    e.preventDefault();
    likeBtn.disabled = true;

    try {
      const res = await fetch(`http://localhost:8080/api/events/likes/toggle`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, eventName: eventData.eventName })
      });

      const data = await res.json();
      if (!res.ok || !data.success) throw new Error("Failed to update like");

      // ✅ Update like count and visual
      likeCount.textContent = data.likesCount;
      updateLikeVisual(likeBtn, data.liked);

      // ✨ Small pulse animation
      likeBtn.classList.add("pulse");
      setTimeout(() => likeBtn.classList.remove("pulse"), 300);

    } catch (err) {
      console.error("❌ Like button error:", err);
      showToast("⚠️ Error updating like.", "error");
    } finally {
      likeBtn.disabled = false;
    }
  });
}

// 🔥 Update the heart color and emoji only
function updateLikeVisual(likeBtn, liked) {
  const heart = likeBtn.firstChild; // ❤️ or 🤍
  if (liked) {
    heart.textContent = "❤️ ";
    likeBtn.style.color = "red";
  } else {
    heart.textContent = "🤍 ";
    likeBtn.style.color = "black";
  }
}

// 🔔 Simple toast (optional feedback instead of alert)
function showToast(message, type = "success") {
  const toast = document.createElement("div");
  toast.className = `toast ${type}`;
  toast.textContent = message;
  document.body.appendChild(toast);
  setTimeout(() => toast.remove(), 3000);
}

// --- COMMENTS ---
function setupCommentBox(eventData, username) {
  const commentBtn = document.getElementById("commentBtn");
  const commentSection = document.getElementById("commentSection");
  const closeCommentBox = document.getElementById("closeCommentBox");
  const commentList = document.getElementById("commentList");
  const newComment = document.getElementById("newComment");
  const addCommentBtn = document.getElementById("addCommentBtn");
  const commentCount = document.getElementById("commentCount");

  commentBtn.addEventListener("click", async () => {
    commentSection.classList.remove("hidden");
    await loadComments();
  });

  closeCommentBox.addEventListener("click", () => {
    commentSection.classList.add("hidden");
  });

  addCommentBtn.addEventListener("click", async () => {
    const content = newComment.value.trim();
    if (!content) return;

    const eventName = document.getElementById("eventName")?.textContent?.trim();
    if (!eventName) {
      console.error("⚠️ No event name found in DOM!");
      return;
    }

    addCommentBtn.disabled = true;
    try {
      const res = await fetch(`http://localhost:8080/api/events/comments/add`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, eventName, content })
      });

      const data = await res.json();
      if (data.success) {
        newComment.value = "";
        commentCount.textContent = data.commentsCount;
        await loadComments();
        commentList.scrollTop = commentList.scrollHeight; // auto scroll
      } else {
        alert("Error adding comment.");
      }
    } catch (err) {
      console.error("❌ Add comment error:", err);
    } finally {
      addCommentBtn.disabled = false;
    }
  });

  async function loadComments() {
    try {
      let eventName = document.getElementById("eventName")?.textContent?.trim();
      if (!eventName) eventName = localStorage.getItem("selectedEventName");

      console.log("📦 Fetching comments for:", eventName);

      const res = await fetch(`http://localhost:8080/api/events/comments/event/${eventName}`);
      if (!res.ok) throw new Error("Failed to fetch comments");

      const comments = await res.json();
      commentList.innerHTML = comments.length
        ? comments.map(c => `
            <div class="comment-item">
              <p><strong>${c.user}</strong>: ${c.content}</p>
            </div>
          `).join("")
        : "<p>No comments yet.</p>";
    } catch (err) {
      console.error("❌ Comment load error:", err);
      commentList.innerHTML = "<p style='color:red;'>Unable to load comments</p>";
    }
  }
}

// --- BOOK BUTTON ---
function setupBookButton() {
  const bookBtn = document.getElementById("bookTicketBtn");
  if (!bookBtn) return;
  bookBtn.addEventListener("click", () => {
    window.location.href = "bookTicket.html";
  });
}
