# Stock Trading Microservices Application

A microservices-based stock trading platform built with Spring Boot and Docker.

## Architecture

The application consists of the following microservices:

- **Eureka Server** (Port: 8761): Service discovery and registration
- **API Gateway** (Port: 9999): Routes requests to appropriate services
- **Exchange Service** (Port: 8080): Handles stock trading operations
- **Portfolio Service** (Port: 8081): Manages user portfolios
- **Registration Service** (Port: 8082): Handles user registration and authentication
- **Trading Service** (Port: 8083): Manages trading operations and stock search

## Prerequisites

- Java 17
- Docker and Docker Compose
- Maven
- PostgreSQL (runs in Docker)

## Quick Start

1. Clone the repository:

```bash
git clone <repository-url>
cd stock-application-microservices
```

2. Build and run using Docker Compose:

```bash
docker-compose up --build
```

## API Documentation

### Exchange Service (Port: 8080)

#### Add Stock (Admin Only)

```http
POST /api/exchange/add
```

Add a new stock to the exchange.

**Parameters**:

- `username`: Admin username
- `password`: Admin password

**Request Body**:

```json
{
  "name": "Apple Inc.",
  "symbol": "AAPL",
  "price": 150.5,
  "quantity": 1000,
  "totalValue": 150500.0,
  "min": 145.0,
  "max": 155.0,
  "minValue": 145000.0,
  "maxValue": 155000.0,
  "lastClosingPrice": 149.75
}
```

### Portfolio Service (Port: 8081)

#### Get User Portfolio

```http
GET /api/portfolio/{userId}
```

Retrieves portfolio details for a specific user.

**Response**:

```json
{
  "success": true,
  "message": "Operation successful",
  "data": [
    {
      "symbol": "AAPL",
      "quantity": 10,
      "averagePrice": 150.5,
      "currentValue": 1505.0
    }
  ]
}
```

#### Get Portfolio Summary

```http
GET /api/portfolio/{userId}/summary
```

Get summary of portfolio including total value and profit/loss.

**Response**:

```json
{
  "success": true,
  "message": "Operation successful",
  "data": {
    "totalValue": 10000.0,
    "totalProfit": 500.0,
    "profitPercentage": 5.0,
    "numberOfStocks": 3
  }
}
```

#### Get Portfolio Performance

```http
GET /api/portfolio/{userId}/performance
```

Get historical performance of the portfolio.

#### Update Portfolio Preferences

```http
PUT /api/portfolio/{userId}/preferences
```

Update user's portfolio preferences.

### Trading Service (Port: 8083)

#### Search Stocks

```http
GET /api/stocks/search?query={symbol}
```

Search stocks by symbol or name using fuzzy search.

**Parameters**:

- `query`: Stock symbol or name (e.g., AAPL)

**Response**:

```json
{
  "success": true,
  "message": "Operation successful",
  "data": [
    {
      "symbol": "AAPL",
      "name": "Apple Inc.",
      "price": 150.5,
      "lastUpdated": "2024-01-20T10:30:00Z"
    }
  ]
}
```

#### Buy Stocks

```http
POST /api/trade/buy
```

Execute a buy order.

**Request Body**:

```json
{
  "userId": 1,
  "stockSymbol": "AAPL",
  "quantity": 10,
  "price": 150.5,
  "tradeType": "BUY"
}
```

#### Sell Stocks

```http
POST /api/trade/sell
```

Execute a sell order.

**Request Body**:

```json
{
  "userId": 1,
  "stockSymbol": "AAPL",
  "quantity": 5,
  "price": 155.75,
  "tradeType": "SELL"
}
```

#### Get User Holdings

```http
GET /api/trade/holdings/{userId}
```

Get user's current stock holdings.

#### Get Portfolio Value

```http
GET /api/trade/portfolio/value/{userId}
```

Get total portfolio value for a user.

#### Get Portfolio By Value

```http
GET /api/trade/portfolio/ordered/{userId}
```

Get user's portfolio ordered by value.

#### Get Stock Price

```http
GET /api/trade/price/{symbol}
```

Get current price for a specific stock.

### Registration Service (Port: 8082)

#### Register User

```http
POST /api/registration/register
```

Register a new user.

**Request Body**:

```json
{
  "username": "john.doe",
  "email": "john.doe@example.com",
  "password": "securePassword123",
  "fullName": "John Doe"
}
```

#### User Login

```http
POST /api/registration/login
```

Authenticate user and get access token.

**Request Body**:

```json
{
  "username": "john.doe",
  "password": "securePassword123"
}
```

#### Update User Profile

```http
PUT /api/registration/profile/{userId}
```

Update user profile information.

#### Reset Password

```http
POST /api/registration/reset-password
```

Request password reset.

### Common Response Format

All APIs follow a standard response format:

```json
{
    "success": boolean,
    "message": "string",
    "data": object
}
```

### Error Responses

Standard error responses across all services:

```json
{
  "success": false,
  "message": "Error description",
  "data": null
}
```

### Authentication

Most endpoints require authentication via JWT token in the Authorization header:

```http
Authorization: Bearer <jwt_token>
```

### Rate Limiting

- API Gateway implements rate limiting of 100 requests per minute per IP
- Burst capacity of 200 requests

## Docker Images

All services are available as Docker images:

```yaml
services:
  - shoyabsiddique/trading-eureka-server:latest
  - shoyabsiddique/trading-gateway:latest
  - shoyabsiddique/trading-exchange:latest
  - shoyabsiddique/trading-portfolio:latest
  - shoyabsiddique/trading-registration:latest
  - shoyabsiddique/trading-trading:latest
```

## Database Configuration

PostgreSQL database configuration in docker-compose.yml:

```yaml
POSTGRES_DB: trading_db
POSTGRES_USER: postgres
POSTGRES_PASSWORD: tiger
```

## Service Dependencies

- All services depend on Eureka Server for service discovery
- Portfolio, Trading, and Exchange services depend on PostgreSQL
- Gateway routes all requests to appropriate services

## Development Setup

1. Install dependencies:

```bash
mvn clean install
```

2. Run services individually:

```bash
# Start Eureka Server first
cd server
mvn spring-boot:run

# Start other services
cd ../gateway
mvn spring-boot:run

# Repeat for other services
```

## Health Checks

The application includes health checks for:

- PostgreSQL database
- Eureka Server
- All microservices

## Service URLs

- Eureka Server: http://localhost:8761
- API Gateway: http://localhost:9999
- Exchange Service: http://localhost:8080
- Portfolio Service: http://localhost:8081
- Registration Service: http://localhost:8082
- Trading Service: http://localhost:8083

## Error Handling

All services implement standard error handling with appropriate HTTP status codes:

- 200: Success
- 400: Bad Request
- 401: Unauthorized
- 403: Forbidden
- 404: Not Found
- 500: Internal Server Error

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit changes
4. Push to the branch
5. Create a Pull Request

## License

[Add your license information here]

## Contact

[Add your contact information here]
