# Inventory Management API

A RESTful inventory management API built with Spring Boot 3. Supports full CRUD operations for products and categories, search/filtering, pagination, stock tracking, and interactive API documentation via Swagger.

## Tech Stack

- **Java 17** + **Spring Boot 3.2**
- **Spring Data JPA** — data access with custom queries
- **H2** — in-memory database for development/demo
- **PostgreSQL** — production database support
- **Bean Validation** — request validation with descriptive error messages
- **SpringDoc OpenAPI** — auto-generated Swagger UI
- **JUnit 5 + Mockito** — unit tests for service and controller layers
- **Maven** — build and dependency management

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+

### Run Locally

```bash
git clone https://github.com/mshayan1620/inventory-api.git
cd inventory-api
./mvnw spring-boot:run
```

The API starts at `http://localhost:8080`. An H2 in-memory database is pre-seeded with sample data.

### Swagger UI

Open [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) to explore and test all endpoints interactively.

### H2 Console

Access the database at [http://localhost:8080/h2-console](http://localhost:8080/h2-console) with:
- JDBC URL: `jdbc:h2:mem:inventorydb`
- Username: `sa`
- Password: *(blank)*

## API Endpoints

### Products

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/products` | List all products (paginated) |
| `GET` | `/api/products/{id}` | Get product by ID |
| `GET` | `/api/products/search` | Search by name, category, price range |
| `GET` | `/api/products/low-stock` | Get low-stock products |
| `POST` | `/api/products` | Create a new product |
| `PUT` | `/api/products/{id}` | Update a product |
| `PATCH` | `/api/products/{id}/stock` | Update stock quantity |
| `DELETE` | `/api/products/{id}` | Delete a product |

### Categories

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/categories` | List all categories (paginated) |
| `GET` | `/api/categories/{id}` | Get category by ID |
| `POST` | `/api/categories` | Create a new category |
| `PUT` | `/api/categories/{id}` | Update a category |
| `DELETE` | `/api/categories/{id}` | Delete a category |

### Example Requests

**Create a product:**
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Wireless Earbuds",
    "description": "Bluetooth 5.0 earbuds with noise cancellation",
    "price": 59.99,
    "stockQuantity": 200,
    "sku": "ELEC-006",
    "categoryId": 1
  }'
```

**Search products by price range:**
```bash
curl "http://localhost:8080/api/products/search?minPrice=20&maxPrice=100&page=0&size=5"
```

**Get low-stock items:**
```bash
curl "http://localhost:8080/api/products/low-stock?threshold=10"
```

## Running Tests

```bash
./mvnw test
```

## Production Configuration

Set the `prod` profile and provide a `DATABASE_URL` environment variable:

```bash
DATABASE_URL=jdbc:postgresql://host:5432/inventorydb \
  java -jar target/inventory-api-1.0.0.jar --spring.profiles.active=prod
```

## Project Structure

```
src/main/java/com/shayan/inventory/
├── config/            # OpenAPI + data seeder configuration
├── controller/        # REST controllers
├── dto/               # Request/response DTOs with validation
├── entity/            # JPA entities
├── exception/         # Custom exceptions + global handler
├── repository/        # Spring Data JPA repositories
└── service/           # Business logic layer
```

## License

MIT
