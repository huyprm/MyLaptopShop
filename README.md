# LaptopShop

LaptopShop is a modern laptop sales management system built on the Spring Boot platform, supporting product, inventory, order, promotion, user management, security, and payment integration.

## 1. System Architecture

- **Backend:** Spring Boot 3.5.x, Java 21
- **Frontend:** (Integrated via RESTful API, easily extendable)
- **Database:** PostgreSQL, Flyway migration
- **Cache:** Redis (Redisson)
- **Cloud Storage:** Cloudinary (image management)
- **Advanced Search:** PostgreSQL FTS, pg_trgm
- **Security:** Spring Security, JWT, OAuth2, Google Login
- **Realtime:** WebSocket (Spring Boot)
- **Payment:** Integrated Momo, COD
- **CI/CD & Packaging:** Docker, Docker Compose
- **API Documentation:** Swagger (springdoc-openapi)

## 2. Main Modules

- `controller/`: REST APIs for each business domain (products, orders, inventory, users, ...)
- `service/`: Business logic processing, clearly separated by function
- `repository/`: Data access with Spring Data JPA
- `model/entity/`: Database entity definitions (Product, User, Order, ...)
- `model/dto/`: Data Transfer Objects between layers
- `mapper/`: MapStruct for Entity <-> DTO conversion
- `scheduler/`: Scheduled tasks (auto-update promotions, clean up orders...)
- `strategy/`: Strategy pattern for payment methods (Momo, COD...)
- `config/`: Security, CORS, JWT, OAuth2, Redis, ... configuration
- `exception/`: Global exception handling

## 3. Highlight Features

### 3.1. Advanced Product Search
- Full-text search, fuzzy matching, high speed thanks to FTS and pg_trgm indexes on PostgreSQL.

### 3.2. Dynamic Promotion Management
- Automatically apply and update promotions based on time, user, and status conditions.
- Uses scheduler and stored procedures for performance optimization.

### 3.3. Smart Inventory Management
- Real-time inventory tracking, low-stock alerts, auto-update on import/export.
- Manages Goods Receipt Notes and Delivery Notes.

### 3.4. Multi-layer Security & Authorization
- Spring Security, JWT, OAuth2, fine-grained roles (admin, staff, customer).
- Password encryption, 2FA, API protection.

### 3.5. Product Review & Rating
- Users can review and comment; the system auto-calculates average scores and filters spam.

### 3.6. Multi-method Payment
- Integrated Momo, COD, easily extendable with Strategy Pattern.

### 3.7. User Management & Authorization
- Registration, login, Google OAuth2, personal info management, dynamic role assignment.

### 3.8. Order & Cart Management
- Place orders, track status, manage order and cart details in real time.

### 3.9. File & Image Management
- Store images on Cloudinary, upload/download files via API.

## 4. Technologies Used

- **Spring Boot, Spring Data JPA, Spring Security, Spring Web, Spring WebSocket**
- **PostgreSQL, Flyway, Redis, Redisson**
- **MapStruct, Lombok**
- **Swagger (OpenAPI), Springdoc**
- **Cloudinary, Google API, Momo API**
- **Docker, Docker Compose**

## 5. System Configuration (application.yml)

- Database, Redis, Cloudinary, JWT, Google, and Momo are all configured via environment variables.
- Flexible configuration for dev/prod environments.

## 6. Build & Deployment Guide

### Build with Maven
```sh
./mvnw clean package
```

### Run with Docker
```sh
docker build -t laptopshop .
docker run -p 8080:8080 laptopshop
```
Or use Docker Compose:
```sh
docker compose up --build
```

### Access API Documentation
- Swagger UI: https://dev.api.mylaptopshop.me/swagger-ui/index.html

---
**Author:** MaiHuy
**License:** (Not specified)