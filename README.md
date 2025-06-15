# OpenBanking API


## Features

- Get account balance by account ID
- Initiate payments via a mocked external API
- JWT-based authentication and authorization
- REST API for accounts and payments management

---

## Technologies

- Java 17+
- Spring Boot 3+
- Spring Security with JWT
- Maven
- PostgreSQL 
- Lombok

---

## Running the Project

1. Clone the repository:
    ```bash
    git clone https://github.com/yourusername/OpenBanking.git
    cd OpenBanking
    ```

2. Configure database connection in `application.properties`

    The application uses **PostgreSQL** as its database.
    ```
    spring.application.name=OpenBanking
    
    spring.datasource.url=jdbc:postgresql://<YOUR_DB_HOST>:<YOUR_DB_PORT>/<YOUR_DB_NAME>
    spring.datasource.username=<YOUR_DB_USERNAME>
    spring.datasource.password=<YOUR_DB_PASSWORD>
    spring.datasource.driver-class-name=org.postgresql.Driver
    spring.jpa.hibernate.ddl-auto=update
    ```

3. Run the application

4. The API will be available at `http://localhost:8080`

---

## Initial Data

On the first run, the application will populate the database with initial data. These entities are configured inside the `DbContext` class (or corresponding Spring component) and include sample accounts, users, payments, and other necessary entities for testing and demonstration.

If the database already contains data, this step will be skipped automatically to prevent overwriting existing data.

---
## API Endpoints

### 1. Get Account Balance
**GET** `/api/accounts/{accountId}/balance`
- Returns the current balance of the specified account.
- **Parameters:**
    - `accountId` — Id of the account (string)
- **Response:**
    - `200 OK` with balance as a decimal number in the body
    - `404 Not Found` if the account does not exist

---

### 2. Get Account Transactions
**GET** `/api/accounts/{accountId}/transactions`
- Returns the last 10 transactions for the specified account.
- **Parameters:**
    - `accountId` — Id of the account (string)
- **Response:**
    - `200 OK` with a JSON array of transactions
    - Each transaction includes fields like `id`, `amount`, `currency`, `date`, `description`)
    - `404 Not Found` if the account does not exist

---

### 3. Initiate Payment
**POST** `/api/payments/initiate`
- Initiates a new payment via a mocked external banking API.
- **Request Body:** JSON object with fields:
    - `consumerIban` — source IBAN (string)
    - `receiverIban` — destination IBAN (string)
    - `amount` — payment amount (decimal)
    - `currency` — currency code (string)
- **Response:**
    - `200 OK` with payment confirmation details
    - `400 Bad Request` if input is invalid
    - `403 Forbidden` if unauthorized
    - Other error codes depending on external API response

---

### Authentication

- All endpoints except `/auth/**` require JWT authorization.
- Send JWT token in `Authorization` header as `Bearer <token>`.
- Use `/auth/signin` and `/auth/signup` for authentication workflows.  
