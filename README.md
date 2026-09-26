# StockSense — Inventory Management System

StockSense is a full-stack Inventory Management System designed to manage stock operations in a simple, centralized, and reliable way.

It helps businesses manage products, warehouses, stock receipts, deliveries, internal transfers, adjustments, and stock history without depending on manual registers or Excel sheets.

## Technology Stack

- Java 21
- Spring Boot 4.1.1
- Spring Security
- JWT Authentication
- PostgreSQL 18
- React
- TypeScript
- Vite
- Docker Compose

## How to Run the Project

### Using Docker

1. Install Docker Desktop.
2. Extract the StockSense project.
3. Open a terminal inside the project folder.
4. Run:

```bash
docker compose up --build
```

5. Open the frontend in your browser:

```text
http://localhost:5173
```

6. Create a demo manager account or register a new account.
7. Login and start using StockSense.

The backend runs at:

```text
http://localhost:8080
```

## Main Features

StockSense currently supports:

- User registration and login
- JWT authentication
- Password hashing using BCrypt
- Product management
- Warehouse management
- Location management
- Stock receipts
- Delivery operations
- Internal stock transfers
- Inventory adjustments
- Stock balance tracking
- Stock movement history
- Dashboard KPIs
- Low-stock calculation
- Multi-location inventory tracking

## Inventory Flow

The main inventory flow is:

```text
Receive Stock
     ↓
Store in Warehouse
     ↓
Internal Transfer
     ↓
Delivery
     ↓
Inventory Adjustment
     ↓
Stock Ledger
```

Every completed stock operation automatically updates the stock quantity and creates a record in the Stock Ledger.

## Example Inventory Flow

### Step 1 — Receive Stock

Receive:

```text
100 KG Steel Rod
```

Stock becomes:

```text
100 KG
```

### Step 2 — Internal Transfer

Transfer:

```text
30 KG
```

From:

```text
Main Warehouse
```

To:

```text
Production Rack
```

The total stock remains:

```text
100 KG
```

### Step 3 — Delivery

Deliver:

```text
20 KG
```

Remaining total stock:

```text
80 KG
```

### Step 4 — Inventory Adjustment

If 3 KG is damaged, the physical stock becomes:

```text
77 KG
```

The system records the adjustment automatically.

Final stock:

```text
77 KG
```

## Important API Endpoints

Protected APIs require a JWT token.

Use the following header:

```text
Authorization: Bearer <token>
```

### Register User

```http
POST /api/auth/register
```

Example:

```json
{
  "name": "Vinay",
  "email": "manager@stocksense.local",
  "password": "Password@123",
  "role": "INVENTORY_MANAGER"
}
```

### Create Warehouse

```http
POST /api/warehouses
```

```json
{
  "name": "Main Warehouse",
  "code": "WH-01",
  "address": "Main Campus"
}
```

### Create Location

```http
POST /api/warehouses/1/locations
```

```json
{
  "name": "Rack A",
  "code": "RACK-A"
}
```

### Create Product

```http
POST /api/products
```

```json
{
  "name": "Steel Rod",
  "sku": "STL-ROD-001",
  "unitOfMeasure": "KG",
  "minimumStock": 50,
  "reorderQuantity": 100
}
```

### Receive Stock

```http
POST /api/receipts/validate
```

```json
{
  "productId": 1,
  "locationId": 1,
  "quantity": 100,
  "supplier": "ABC Metals"
}
```

### Internal Transfer

```http
POST /api/transfers/validate
```

```json
{
  "productId": 1,
  "sourceLocationId": 1,
  "destinationLocationId": 2,
  "quantity": 30
}
```

### Delivery

```http
POST /api/deliveries/validate
```

```json
{
  "productId": 1,
  "locationId": 2,
  "quantity": 20,
  "customer": "Customer A"
}
```

### Inventory Adjustment

```http
POST /api/adjustments/validate
```

```json
{
  "productId": 1,
  "locationId": 2,
  "countedQuantity": 7,
  "reason": "Damaged stock"
}
```

### View Stock Ledger

```http
GET /api/movements
```

### View Dashboard KPIs

```http
GET /api/dashboard/kpis
```

## Validation and Accuracy

StockSense includes important inventory validations such as:

- Duplicate SKU prevention
- Negative quantity prevention
- Zero quantity prevention
- Negative physical stock prevention
- Delivery quantity validation
- Transfer quantity validation
- Same-location transfer prevention
- Adjustment reason validation
- Database transactions for stock operations
- Stock locking to reduce concurrent overselling
- Stock movement history for every inventory transaction
- BCrypt password hashing
- JWT-protected APIs

## Password Reset

StockSense includes OTP-based password reset support.

The local development version prints the generated OTP in the backend console.

Endpoint:

```http
POST /api/auth/forgot-password
```

Example:

```json
{
  "email": "manager@stocksense.local"
}
```

For production deployment, the OTP can be connected to a transactional email service.

## Project Goal

The main goal of StockSense is to provide a simple and reliable inventory management system where businesses can:

- Track stock accurately
- Manage multiple warehouses and locations
- Record incoming and outgoing inventory
- Move stock between locations
- Correct physical stock differences
- View complete stock history
- Reduce manual inventory errors

## Demo Scenario

Use this simple demo during project presentation:

```text
Receive 100 KG Steel
        ↓
Transfer 30 KG
        ↓
Deliver 20 KG
        ↓
Adjust 3 KG Damaged
        ↓
Final Stock = 77 KG
```

Open the Stock Ledger to show that every inventory operation is recorded.

## Future Improvements

The following features can be added in the final production version:

- Real-time dashboard updates
- Email-based OTP verification
- Barcode and QR scanning
- Role-based permissions
- Receipt and delivery status workflow
- Advanced reports
- Low-stock notifications
- Cloud deployment
- Automated testing