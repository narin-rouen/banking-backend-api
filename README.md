# 💳 Digital Bank Management API

A production-ready RESTful Banking Backend built with Spring Boot that supports secure authentication, role-based authorization, and real-time financial transactions, including deposits, withdrawals, and transfers.

This project demonstrates clean architecture, security best practices, and scalable backend design.

---

## 📌 Project Overview

The Digital Bank Management API simulates a real-world banking system with two roles:

- **USER** – Manage personal account, perform transactions, view history.
- **ADMIN** – Manage users, monitor accounts, and oversee all transactions.

The system ensures secure session handling, transaction integrity, and proper role-based access control.

---

## 🚀 Key Features

### 🔐 Authentication & Security
- JWT-based Authentication
- Access & Refresh Token mechanism
- BCrypt password hashing
- Role-based Authorization (USER / ADMIN)
- Session tracking (multi-device login support)
- Input validation & global exception handling

### 👤 User Capabilities
- Register & Login
- View and update profile
- Deposit money
- Withdraw money
- Transfer funds to another account
- View personal transaction history (pagination, sorting, filtering)
- View active sessions & logout from devices

### 🛠️ Admin Capabilities
- Create / Update / Delete accounts
- View all users (paginated & searchable)
- Monitor all transactions
- Suspend or activate user accounts

---

## 🧱 Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 4
- **Security:** Spring Security + JWT
- **Database:** MySQL
- **ORM:** Spring Data JPA (Hibernate)
- **Build Tool:** Maven
- **Server:** Embedded Tomcat

---

## 🗄️ Database Design

### Main Entities
- User
- Account
- Transaction
- Transfer
- Session

### Relationships
- One User → One Account  
- One Account → Many Transactions  
- One Transfer → Sender & Receiver Accounts  
- One User → Many Sessions  

Designed for transactional consistency and audit traceability.

---

## 🔌 API Structure

### Authentication
```

POST   /api/auth/register
POST   /api/auth/login
POST   /api/auth/refresh
POST   /api/auth/logout
POST   /api/auth/logout-all
GET    /api/auth/session

```

### User Endpoints
```

GET    /api/user/accounts
GET    /api/user/transactions
GET    /api/user/transactions/{id}
POST   /api/user/deposit
POST   /api/user/withdraw
POST   /api/user/transfer
GET    /api/user/me
PUT    /api/user/me

```

### Admin Endpoints
```

GET    /api/admin/accounts
POST   /api/admin/accounts
PUT    /api/admin/accounts/{id}
DELETE /api/admin/accounts/{id}
GET    /api/admin/transactions
GET    /api/admin/transactions/{id}

````

---

## ⚙️ Local Setup

### 1️⃣ Clone Repository
```bash
git clone https://github.com/your-username/digital-bank-management.git
cd digital-bank-management
````

### 2️⃣ Configure Database

Create a MySQL database:

```sql
CREATE DATABASE digital_bank;
```

Update `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/digital_bank
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 3️⃣ Run Application

```bash
mvn clean install
mvn spring-boot:run
```

Application runs at:

```
http://localhost:8080
```

---

## 🔍 Sample Request

### Transfer Money

```http
POST /api/user/transfer
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

```json
{
  "receiverAccountId": 2,
  "amount": 150.00
}
```

---

## 🧪 Testing

```bash
mvn test
```

You can test endpoints using Postman or Insomnia.

---

## 🏗️ Architecture Highlights

* Layered Architecture (Controller → Service → Repository)
* DTO pattern for request/response separation
* Global Exception Handling
* Transaction management using `@Transactional`
* Secure configuration via Spring Security filter chain

---

## 📈 Future Improvements

* Two-Factor Authentication (2FA)
* Email notifications
* Currency conversion support
* Admin analytics dashboard
* Docker containerization
* CI/CD integration

---

## 👨‍💻 Author

**Narin Rouen**
Backend Developer – Java & Spring Boot

Focused on building secure, scalable backend systems with production-ready standards.

---

## 📄 License

All rights reserved. This is my personal project showing my skills. Please DO NOT copy or reuse any of this code. THANKS 🙏

