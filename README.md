# Vehicle Service Portal — Backend

A **Spring Boot REST API** that powers the Vehicle Service Management System.

The backend provides authentication, service management, vehicle management, booking system, invoice generation, and notification handling.

---

## Tech Stack

* Java
* Spring Boot
* Spring Security
* JWT Authentication
* Hibernate / JPA
* MySQL
* Maven

---

## Features

### Authentication

* User registration
* JWT login
* Role-based access control

### Roles

* CUSTOMER
* ADMIN
* SERVICE_ADVISOR

### Modules

* Vehicle management
* Service master management
* Booking system
* Invoice generation
* Notifications

---

## Project Architecture

```
Controller
     ↓
Service
     ↓
Repository
     ↓
Database (MySQL)
```

---

## Database

MySQL database hosted on **Railway**.

Main tables:

* users
* vehicles
* bookings
* services
* invoices
* notifications

---

## Running Locally

Clone the repository:

```
git clone https://github.com/Siddhiamup/vehicle-service-backend
```

Build project:

```
mvn clean install
```

Run application:

```
mvn spring-boot:run
```

---

## API Base URL

```
https://vehicle-service-backend-production.up.railway.app
```

---

## Security

Authentication handled using **Spring Security + JWT**.

---

## Deployment

Backend is deployed on **Railway**.

---

## Author

Siddhi Amup
