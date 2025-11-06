# 🧠 Product Service (Spring Boot + MongoDB)

A **mission-critical Java Spring Boot microservice** for managing product data with **MongoDB**, **Swagger documentation**, **Flyway data migration**, **LRU caching**, and **Dockerized deployment**.

---

## 🚀 Features

✅ Full CRUD REST APIs (Create, Read, Update, Delete)  
✅ Integrated MongoDB Repository layer  
✅ LRU in-memory caching for optimized reads  
✅ Configurable cache strategy via `application.yml`  
✅ Automatic Data Initialization using Flyway migration runner  
✅ Swagger (OpenAPI 3.0) API documentation  
✅ Global Exception Handling & Validation  
✅ JUnit + Mockito test coverage (unit + integration)  
✅ Docker + Docker Compose for one-command environment setup  
✅ Health endpoints via Spring Actuator  

---

## 🧩 Tech Stack

| Layer | Technology |
|-------|-------------|
| Language | Java 17 |
| Framework | Spring Boot 3.5.7 |
| Database | MongoDB |
| ORM | Spring Data MongoDB |
| Validation | Jakarta Validation API |
| Documentation | Swagger / OpenAPI 3 |
| Testing | JUnit 5, Mockito |
| Build Tool | Maven |
| Containerization | Docker, Docker Compose |
| Data Migration | Flyway |

---

## ⚙️ Setup Instructions

### **1️⃣ Clone & Build**
```bash
git clone <your-repo-url>
cd productsvc
mvn clean package -DskipTests
```

### **2️⃣ Run with Docker Compose**
Starts **MongoDB** and **Product Service** together:
```bash
docker-compose up --build
```

### **3️⃣ Verify Containers**
```bash
docker ps
```
You should see:
```
mongodb        -> 27017
productsvc     -> 7070
```

---

## 🌐 API Endpoints

| Method | Endpoint | Description |
|--------|-----------|-------------|
| `POST` | `/api/products` | Create new product |
| `GET` | `/api/products/{id}` | Get product by ID |
| `GET` | `/api/products/type/{type}` | Get products by type |
| `GET` | `/api/products` | Get all products |
| `PUT` | `/api/products/{id}` | Update existing product |
| `DELETE` | `/api/products/{id}` | Delete a product |

### Example Request (POST)
```bash
curl -X POST http://localhost:7070/api/products -H "Content-Type: application/json" -d '{
  "id": "p1",
  "name": "Lego Starship",
  "type": "Toy",
  "category": "Kids",
  "price": 1999,
  "recommendedAgeGroup": "5-10",
  "attributes": {
    "brand": "Lego",
    "pieces": "250"
  }
}'
```

---

## 📘 Swagger API Docs

Swagger UI available at:  
👉 [http://localhost:7070/swagger-ui/index.html](http://localhost:7070/swagger-ui/index.html)

---

## 🧮 Configuration (`application.yml`)

```yaml
server:
  port: 7070

spring:
  data:
    mongodb:
      database: productsvcdb

product:
  cache:
    strategy: KEY_BASED
    capacity: 1000
```

---

## 🧠 Testing

### Run all tests
```bash
mvn test
```

### Test Coverage Includes
- ✅ `ProductControllerIntegrationTest` → Full REST endpoints coverage  
- ✅ `ProductServiceImplTest` → Repository + Cache logic with Mockito  
- ✅ Validation & Exception tests via SpringBootTest  

---

## 🧱 Project Structure

```
productsvc/
├── src/
│   ├── main/java/org/responsive/productsvc/
│   │   ├── controller/          # REST Controllers
│   │   ├── service/             # Business logic
│   │   ├── repository/          # Mongo Repositories
│   │   ├── cache/               # LRU Cache Implementation
│   │   ├── config/              # Swagger, Cache, Mongo Configs
│   │   ├── dto/                 # Data Transfer Objects
│   │   ├── exception/           # Global Exception Handling
│   │   └── ProductsvcApplication.java
│   ├── test/java/org/responsive/productsvc/
│   │   ├── ProductControllerIntegrationTest.java
│   │   └── ProductServiceImplTest.java
│   └── resources/
│       ├── application.yml
│       └── db/migration/        # Flyway scripts
│
├── pom.xml
├── Dockerfile
├── docker-compose.yml
└── README.md
```

---

## 🧰 Docker Commands

| Action | Command |
|--------|----------|
| Build JAR | `mvn clean package -DskipTests` |
| Start containers | `docker-compose up --build` |
| Stop containers | `docker-compose down` |
| Rebuild image | `docker-compose build` |

---

## 🧩 Flyway Data Migration

When the application starts, Flyway auto-runs scripts inside:
```
src/main/resources/db/migration/
```

Example file:
```
V1__init_products.js
```

```js
db.products.insertMany([
  {
    "id": "p1",
    "name": "Lego Starship",
    "type": "Toy",
    "category": "Kids",
    "price": 1999,
    "recommendedAgeGroup": "5-10",
    "attributes": {
      "brand": "Lego",
      "pieces": "250"
    }
  }
]);
```

---

## 📦 Example Response (GET `/api/products`)

```json
[
  {
    "id": "p1",
    "name": "Lego Starship",
    "type": "Toy",
    "category": "Kids",
    "price": 1999,
    "recommendedAgeGroup": "5-10",
    "attributes": {
      "brand": "Lego",
      "pieces": "250"
    }
  }
]
```

---

## ✅ Health Check

```bash
curl http://localhost:7070/actuator/health
```
Response:
```json
{"status":"UP"}
```

---

## 🧠 Developer Information

**Author:** Kumar Gaurav  
**Role:** Java Backend Engineer | Spring Boot | Microservices | MongoDB | AWS | Kafka  
**Location:** Bangalore, India  

📧 Email: [kgauravis016@gmail.com]  
🌐 LinkedIn: [https://www.linkedin.com/in/kumar-gaurav-548531113/]  
Git Hub Profile: [https://github.com/Gaurav1112/] 

---

## 🏷️ GitHub Repository Tags

`spring-boot` `java` `mongodb` `rest-api` `docker` `swagger` `flyway` `microservice` `testing` `backend`

---

## 💡 Future Enhancements

- Add JWT Authentication (Spring Security)
- Integrate API Gateway + Circuit Breaker
- Add Prometheus + Grafana Monitoring
- Implement Redis caching layer
- Enable Kubernetes Deployment (Helm + K8s)

---
