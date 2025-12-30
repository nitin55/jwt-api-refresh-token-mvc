# 🚀 Java JWT Authentication API (Jetty + Jersey + MySQL)

A lightweight REST API built with **Java**, **Jetty**, **Jersey (JAX-RS)**, **JWT authentication**, and **MySQL**.
It supports **user management**, **JWT access & refresh tokens**, and **token rotation** with database-backed refresh tokens.

---

## 📌 Features

* ✅ User CRUD operations
* 🔐 JWT-based authentication
* ♻️ Refresh token rotation with database persistence
* 🛡️ Servlet filter–based authorization
* 📦 Bean validation (`@NotBlank`, `@Email`, etc.)
* 🌐 RESTful API with consistent JSON responses
* 🧩 Global exception handling
* 🗄️ MySQL database integration
* ⚡ Embedded Jetty server (no external container)

---

## 🏗️ Tech Stack

* **Java 11+**
* **Jetty** (Embedded server)
* **Jersey (JAX-RS)** – REST framework
* **JWT (jjwt)** – Token handling
* **MySQL** – Persistent storage
* **Hibernate Validator / Bean Validation**
* **Maven**

---

## 📂 Project Structure

```
com/example/api
├── App.java
├── config
│   └── JwtAuthFilter.java
├── controller
│   ├── AuthController.java
│   └── UserController.java
├── service
│   ├── AuthService.java
│   └── UserService.java
├── dao
│   ├── RefreshTokenDao.java
│   └── UserDao.java
├── model
│   └── User.java
├── util
│   ├── DbUtil.java
│   ├── JwtUtil.java
│   └── ResponseHandler.java
└── exception
    └── GlobalExceptionMapper.java
```

---

## 🗄️ Database Setup

### 1️⃣ Create Database

```sql
CREATE DATABASE jwt_db;
USE jwt_db;
```

### 2️⃣ Create Tables

```sql
CREATE TABLE users (
  id BIGINT NOT NULL AUTO_INCREMENT,
  username VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  PRIMARY KEY (id)
);

INSERT INTO users (username, password, email)
VALUES ('admin', 'admin123', 'admin@example.com');

CREATE TABLE refresh_tokens (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  jti VARCHAR(255) NOT NULL UNIQUE,
  username VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  expires_at TIMESTAMP NOT NULL
);
```

---

## ⚙️ Configuration

### Database Credentials

Edit `DbUtil.java`:

```java
private static final String URL  = "jdbc:mysql://localhost:3306/jwt_db";
private static final String USER = "root";
private static final String PASS = "root";
```

### JWT Settings

Edit `JwtUtil.java`:

* Access Token: **15 minutes**
* Refresh Token: **30 days**
* HMAC SHA key (32+ chars)

---

## ▶️ Running the Application
### Clone the repository

```bash
git clone <repo-url>
cd jwt-api-refresh-token-mvc
```

### Compile

```bash
mvn clean compile
```

### Run

```bash
mvn exec:java -Dexec.mainClass="com.example.api.App"
```

### Server URL

```
http://localhost:8080/api
```

---

## 🔐 Authentication Flow

1. **Login**

   * Generates **access token** + **refresh token**
2. **Access Token**

   * Used in `Authorization` header
   * Short-lived (15 minutes)
3. **Refresh Token**

   * Stored in DB
   * Rotated on refresh
4. **JWT Filter**

   * Protects all endpoints except `/auth/*`

---

## 🔑 Authorization Header Format

```
Authorization: Bearer <ACCESS_TOKEN>
```

---

## 📡 API Endpoints

### 🔓 Auth

#### Login

```
POST /api/auth/login
```

```json
{
  "username": "john",
  "password": "password123"
}
```

#### Refresh Token

```
POST /api/auth/refresh
```

```json
{
  "refreshToken": "<REFRESH_TOKEN>"
}
```

---

### 👤 Users (Protected)

#### Get All Users

```
GET /api/users
```

#### Add User

```
POST /api/users/add
```

```json
{
  "username": "john",
  "password": "password123",
  "email": "john@example.com"
}
```

#### Update User

```
PUT /api/users/update/{id}
```

#### Delete User

```
DELETE /api/users/delete/{id}
```

---

## 📦 Response Format

All responses follow a consistent structure:

```json
{
  "success": true,
  "message": "Operation successful",
  "data": {}
}
```

---

## ⚠️ Error Handling

* Bean validation errors
* Invalid JSON
* Missing request body
* Unauthorized access
* Global fallback error handler

Handled via:

* `GlobalExceptionMapper`
* `ValidationExceptionMapper`

---

## 🔒 Security Notes

⚠️ **For production use**, you should:

* Hash passwords (e.g., BCrypt)
* Store JWT secret securely (env variables)
* Enable HTTPS
* Add refresh token cleanup jobs
* Implement role-based authorization


---

## ✨ Author

Built with ❤️ for learning and demonstration purposes.



Just tell me 👍

