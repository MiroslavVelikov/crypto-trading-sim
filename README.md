# crypto-trading-sim

## 📌 Purpose

A full-stack cryptocurrency trading simulator with real-time WebSocket integration, built using **Spring Boot**, **PostgreSQL**, and **React**. Users can:

- View real-time prices of the top 20 cryptocurrencies using the Kraken WebSocket API.
- Maintain a virtual USD balance to buy and sell cryptocurrencies.
- Track a full history of all transactions made.
- Reset their account balance to the initial value.

---

## 🛠️ Features

### ✅ Display Top 20 Cryptocurrency Prices

- Live updates via Kraken WebSocket API.
- Displayed in a clean table format showing:
  - Crypto name
  - Symbol
  - Current price

### 💰 Virtual Account Management

- Start with a default balance of `$10,000`.
- Buy crypto by specifying amount → balance is deducted.
- Sell crypto by specifying amount → balance increases.
- Profit/loss shown in the selling table.
- Cannot buy more than your current balance allows.

### 🧾 Transaction History

- Every buy/sell operation is recorded.
- Displays:
  - Crypto symbol
  - Amount
  - Price
  - Type (`BUY` or `SELL`)

### 🔁 Reset Functionality

- Resets account balance to `$10,000`
- Clears all holdings and transaction history.

---

## 🖥️ Tech Stack

### Frontend
- React
- TypeScript

### Backend
- Java + Spring Boot (no JPA)
- Direct SQL queries via JdbcTemplate

### Database
- PostgreSQL (Dockerized)

### Project Setup/Migrations
- Flayway

### Real-time Prices
- Kraken WebSocket API

---


## 📋 Requirements

Before running the project, make sure you have the following installed:

- [Docker](https://www.docker.com/products/docker-desktop)
- [Node.js (v18+)](https://nodejs.org/)
- [npm](https://www.npmjs.com/)
- [Java 17+](https://adoptopenjdk.net/) (or higher)
- [Maven Wrapper (`./mvnw`)] — already included in the project
- Flyway (handled via Maven plugin; no global install needed)

---

## 🚀 First-Time Setup

1. **Start PostgreSQL via Docker:**

   This will launch the database container required by the backend:

   ```bash
   npm run docker-container
   ```

2. **Start the full development environment:**

   This runs:
   - Flyway migrations
   - Spring Boot backend
   - React frontend (on Vite)

   ```bash
   npm run dev
   ```

---

## 📄 Documentation

- All endpoints are available under `/api/wallets`.
- Kraken integration is managed in the backend via a WebSocket client.
- Decisions include:
  - Use of WebSocket for minimal latency.
  - Stateless backend operations with DB lookups via tokens.
  - PostgreSQL chosen for relational data and real transaction integrity.

---

## 📸 Screenshots

- Top 20 crypto table
- Buy/Sell modal
- Transaction history
- Reset button

---

## 📂 Project Structure

```
crypto-trading-sim/
├── backend/                            ← Spring Boot app
│   └── src/main/resources/
│       ├── cryptocurrency-trading-platform-postgres/
│       │   └── docker-compose.yml      ← PostgreSQL container
│       └── db/migration                ← SQL scripts 
│           ├── V1__init_traders_table.sql
│           ├── V2__init_wallets_table.sql
│           ├── V3__init_transactions_table.sql
│           └── V4__init_holdings_table.sql
├── frontend/                           ← React + TypeScript + Vite
└── package.json                        ← npm scripts
```

---

## 🧪 Scripts

| Command             | Description                               |
|---------------------|-------------------------------------------|
| `npm run docker-container` | Starts PostgreSQL via Docker Compose    |
| `npm run migrate`          | Applies Flyway database migrations     |
| `npm run devBackend`      | Runs the Spring Boot backend            |
| `npm run devFrontend`     | Starts the frontend using Vite          |
| `npm run dev`             | Runs migrations, backend, and frontend  |

---

## 📍 Notes

- Frontend is accessible at [http://localhost:5173](http://localhost:5173)
- Backend runs on [http://localhost:8080](http://localhost:8080)
- Cookies and CORS are configured for local development.

---

## 🧰 Troubleshooting

- If you see errors during DB access, ensure Docker container is running.
- To rebuild the DB, stop the container, delete the volume, and restart:
  
  ```bash
  docker-compose down -v
  npm run docker-container
  ```

---

## 🔗 Kraken API Docs

- [Kraken WebSocket API](https://docs.kraken.com/websockets-v2/)

---
