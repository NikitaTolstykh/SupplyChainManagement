# Digital Logistics Solution – Backend

🚀 **Backend system for digitizing logistics operations for SMEs**

Digital Logistics Solution is a backend platform designed to help small and medium-sized transport and logistics companies shift from manual tracking (Excel, phone calls, emails) to a fully digital workflow. It centralizes operations, improves efficiency, and enables real-time visibility for administrators, dispatchers, and customers.

---

## 🎯 Problem Statement

Many logistics SMEs in Europe still manage their operations manually. Larger enterprise tools focus mostly on last-mile delivery and are not suitable for internal logistics optimization. This system solves:

- Centralized order management between warehouses, branches, and subcontractors  
- Role-based access for admins, dispatchers, drivers, and clients  
- Event-driven notifications for order status updates  
- Analytics and statistics for performance tracking  
- Secure, scalable backend architecture suitable for growth

---

## ✅ Key Features

- **Order Lifecycle Management:** Track orders by status, from creation to delivery  
- **Admin Panel:** Manage employees, vehicles, and roles  
- **Customer Feedback:** Rate completed deliveries  
- **Notifications:** Event-driven email alerts for key actions  
- **Analytics Module:** Advanced data aggregation using Stream API  
- **Security:** JWT authentication with Spring Security  
- **Performance:** Redis caching for optimized queries  
- **Testing:** Unit and integration tests with JUnit & Mockito  
- **Documentation:** Full OpenAPI/Swagger documentation  

---

## 🛠️ Tech Stack

- **Language:** Java 17, TypeScript  
- **Framework:** Spring Boot (Web, Data JPA, MVC, Security, Validation, Cache)  
- **Database:** MySQL  
- **ORM:** Hibernate  
- **Caching:** Redis  
- **Mapping & Utilities:** MapStruct, Lombok  
- **Containerization:** Docker, Docker Compose  
- **CI/CD:** GitHub Actions  
- **Testing:** JUnit, Mockito  
- **API Docs:** Swagger/OpenAPI
- **Frontend:** React, Zustand, Tanstack query, Tailwind, Axios, React Router

---

## 📦 Architecture & Design

- Modular Maven project with clean code principles (SOLID)  
- Event-driven architecture for notifications  
- Designed for scalability and maintainability  
- REST API with 30+ endpoints  
- CI/CD enabled for automated testing and deployment  

---

## 🚀 Getting Started

### Prerequisites

- Java 17  
- Docker & Docker Compose  
- Maven  
- MySQL database  

### Installation & Run

1. Clone the repository:
   ```bash
   git clone https://github.com/nikitatolstykh/digital-logistics-backend.git
   cd digital-logistics-backend

2. Configure database in application.properties:
   spring.datasource.url=jdbc:mysql://localhost:3306/logistics
   spring.datasource.username=root
   spring.datasource.password=yourpassword

3. Start the application with Docker Compose:
   docker-compose up --build

   ## Gallery
   ![kanban achievement](https://github.com/user-attachments/assets/2233b7d5-e65a-43e1-8a97-10f7a6b244b9)
   ![rating](https://github.com/user-attachments/assets/893c5985-c538-4bc9-8b2d-e2be90f41c51)
   ![orderdetails](https://github.com/user-attachments/assets/4f7451bd-7b13-4e85-ab79-44c30341f20e)
   ![clientdashboard](https://github.com/user-attachments/assets/b7f845f3-6722-455e-bb34-d88af510139a)
   ![admindashboard](https://github.com/user-attachments/assets/a28946f2-d32e-4353-938d-bd0326c0b17c)
   ![driver dashboard](https://github.com/user-attachments/assets/9ed3ee1d-d134-4856-97f8-097bcc71b54b)
   ![orderinfoclient](https://github.com/user-attachments/assets/71b978b5-836d-49f9-85f6-b5b23ef96077)



