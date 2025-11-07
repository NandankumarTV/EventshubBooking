# 🎟️ Events Hub and Bookings

A full-featured **Event Management Platform** built using **Spring Boot (backend)** and **HTML, CSS, JavaScript (frontend)**.  
It allows users to **sign up, log in, post events, like, comment, and book seats** easily.

---

## 📖 Overview

**Events Hub and Bookings** is a web application that connects event organizers and participants.  
Organizers can create and manage events, while users can explore, like, comment, and book seats.  
It’s designed for simplicity, interactivity, and a smooth booking experience.

---

## 🧰 Tech Stack

**Backend:**  
- Spring Boot  
- Spring Data JPA  
- MySQL  
- Lombok  
- Maven  

**Frontend:**  
- HTML  
- CSS  
- JavaScript (Vanilla JS, Fetch API)

---

## ⚙️ Features

✅ **User Authentication** — Secure login and registration  
✅ **Event Management** — Create, update, delete, and view events  
✅ **Image Upload** — Add images to your events  
✅ **Likes & Comments** — Engage with events  
✅ **Seat Booking** — Book and track available seats  
✅ **RESTful APIs** — Clean and modular architecture  

---

## 🗂️ Project Structure

```
EventsHub/
 ├─ src/
 │   ├─ main/
 │   │   ├─ java/com/nandan/EventsHub/
 |   |   |   |-- dto
 │   │   │   ├─ controller/
 │   │   │   ├─ model/
 │   │   │   ├─ repository/
 │   │   │   ├─ service/
 │   │   │   └─ EventsHubApplication.java
 │   │   └─ resources/
 │   │       ├─ application.properties
 │   │       ├─ static/
 │   │       │   ├─ css/
 │   │       │   ├─ js/
 │   │       │   ├─ images/
 │   │       │   └─ uploads/
 │   │       └─ templates/
 │   └─ test/
 ├─ pom.xml
 └─ README.md
```

---

## ⚙️ Setup and Installation

### Prerequisites
Make sure you have installed:
- JDK 17+
- Maven
- MySQL

### Steps
1. **Clone the Repository**
   ```bash
   git clone https://github.com/NandankumarTV/events-hub-and-bookings.git
   cd events-hub-and-bookings
   ```

2. **Configure MySQL**
   Create a database:
   ```sql
   CREATE DATABASE eventshub;
   ```

3. **Update `application.properties`**
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/eventshub
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   ```

4. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

5. **Access the App**
   Open your browser and go to:  
   👉 `http://localhost:8080`

---


## 🚀 Future Enhancements

- Add admin dashboard  
- Implement email notifications  
- Improve frontend design  
- Integrate payment gateway for event booking  

---

## 👨‍💻 Author

**Nandan Kumar T V**  
🔗 [GitHub](https://github.com/NandankumarTV)

---

