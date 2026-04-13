<h1 align="center">
    <center>
        Borrowly | Library Management System
    </center>
</h1>


<p align="center">
  <a href="https://spring.io/projects/spring-boot">
    <img src="https://img.shields.io/badge/Spring%20Boot-4.0.4-brightgreen" alt="Spring Boot">
  </a>

  <a href="https://spring.io/projects/spring-boot">
    <img src="https://img.shields.io/badge/angular-17-blue?logo=angular&logoColor=white" alt="Angular">
  </a>
  
  <a href="https://www.java.com/">
    <img src="https://img.shields.io/badge/Java-17-blue" alt="Java 17">
  </a>
  <a href="https://www.postgresql.org/">
    <img src="https://img.shields.io/badge/PostgreSQL-14-blue?logo=postgresql&logoColor=white" alt="PostgreSQL">
  </a>
  <a href="https://jwt.io/">
    <img src="https://img.shields.io/badge/JWT-Secure-orange" alt="JWT">
  </a>
  <a href="https://stripe.com/">
    <img src="https://img.shields.io/badge/Stripe-Payments-6772e5" alt="Stripe">
  </a>
  <a href="https://www.lombok.org/">
    <img src="https://img.shields.io/badge/Lombok-Red" alt="Lombok">
  </a>
  <a href="https://www.mapstruct.org/">
    <img src="https://img.shields.io/badge/MapStruct-DTO-lightgrey" alt="MapStruct">
  </a>
</p>

---

**Borrowly-LMS** is a comprehensive Library Management System built with Spring Boot, designed to digitize and automate library operations. It provides a full platform for managing books, user subscriptions, book loans, payments, penalties, reservations, reviews, and bookmarks with modern, scalable architecture.

> [!NOTE]
> This project is currently **UNDER DEVELOPMENT**. Additional features will be added, existing functionalities will be enhanced, and **FRONT-END DEVELOPMENT IS PLANNED**.

---

## Database Design
![Database Design](./ERD/ERD.png)

- Organized and simple design for easy reference  
- Key tables: Users, Books, Categories, Subscriptions, BookLoans, Penalties, Payments, Reservations, BookReviews, Bookmarks

---

## Key Features

### 1. Book & Category Management
- Organize and catalog books by categories
- Manage book inventory and availability
- Track book details and metadata

### 2. User Management & Authentication
- Secure registration and login using JWT
- Role-based access control
- User profile management
- Password reset via email

### 3. Subscription Management
- Flexible subscription plans
- Subscription lifecycle tracking (renewal, cancellation)
- Renewal and cancellation management

### 4. Book Loan Management
- Borrow and return books with automated tracking
- Borrow and due date configuration
- Renewal support (default max: 2 times)
- Track overdue books and overdue days
- Support multiple loan states (BORROWED, OVERDUE, RETURNED)

### 5. Book Reservation System
- Reserve books with queue position tracking
- Multiple reservation states (PENDING, AVAILABLE, FULFILLED, CANCELLED, EXPIRED)
- Automatic availability notifications
- Time-bound reservation windows
- Support for expiration handling
- Notes and comments on reservations

### 6. Book Reviews & Ratings
- User-submitted book reviews with star ratings (1-5)
- Review titles and detailed descriptions
- Track review creation and updates
- User-specific reviews per book
- Support for aggregated ratings and reviews

### 7. Bookmark Management
- Create personal bookmarks for favorite books
- Add notes to bookmarked books
- Personal reading list organization
- Track when books were bookmarked

### 8. Penalty & Fine Management
- Automatic penalty generation for overdue or damaged books
- Multiple penalty types: Delay, Damage
- Track penalty states: UNPAID, PARTIALLY_PAID, PAID, CANCELLED
- Partial payment support
- Cancel penalties with reasons and audit trail
- Payment integration for penalty settlement

### 9. Integrated Payment Processing
- Stripe payment gateway for secure transactions
- Support for subscription and penalty payments
- Track payment status: Initiated, Completed, Failed
- Payment provider management and transaction tracking
- Comprehensive payment history and audit trail

### 10. Advanced Search & Filtering
- JPA Specifications-based complex queries
- Filter by multiple criteria (state, type, user, dates)
- Paginated results for efficient retrieval
- Advanced book search and filtering

### 11. Security & Authentication
- JWT-based authentication
- Secure password management
- Role-based access control

### 12. Communication & Notifications
- Email integration for notifications (payment confirmations, password resets, reservation updates)
- Thymeleaf templates for formatted emails

---

## Tech Stack
- **Framework:** Spring Boot 4.0.4
- **Database:** PostgreSQL with JPA/Hibernate  
- **Authentication:** JWT
- **DTO Mapping:** MapStruct  
- **Payment Gateway:** Stripe
- **Email:** Spring Mail + Thymeleaf  
- **Code Quality:** Lombok  
- **API Documentation:** SpringDoc OpenAPI  
- **Language:** Java 17  

---

## Architecture Highlights
- **Modular Design:** Separate modules for each domain (auth, book, loans, payment, penalty, subscription, user, reservation, reviews, bookmark)
- **Service Layer:** Encapsulates business logic
- **Repository Pattern:** Data access with JPA repositories  
- **DTO Pattern:** Separate entity and API models  
- **Exception Handling:** Custom exception handling for business logic errors  
- **Shared Utilities:** Common functionality in `_shared` module (DTOs, configs, email, audit trails)

