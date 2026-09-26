# StockSense
Build a modular Inventory Management System (IMS) that digitizes and streamlines all stock-related operations within a business. The goal is to replace manual registers, Excel sheets, and scattered tracking methods with a centralized, real-time, easy-to-use app.
<<<<<<< HEAD
#This is login page
=======

#StockSense - Login Page:
=========================

StockSense is a modern Inventory Management System designed to digitize and streamline stock-related operations for businesses.

This module contains the Login Page of StockSense, which provides secure access to the Inventory Management Dashboard.

Features

Modern and responsive login UI

Email / username based login

Password field with show/hide option

Remember Me option

Forgot Password navigation

Sign Up navigation for new users

Form validation

Clean enterprise-style design

Responsive layout for desktop, tablet, and mobile

Prepared for backend authentication integration

Login Flow

User
  |
  v
Login Page
  |
  +--> Enter Email / Username
  |
  +--> Enter Password
  |
  v
Validate Input
  |
  v
Backend Authentication
  |
  +--> Invalid credentials -> Show error
  |
  +--> Valid credentials
          |
          v
      Inventory Dashboard
    
#Dashboard

The dashboard provides a real-time overview of inventory operations, including:

Total products in stock
Low and out-of-stock items
Pending receipts
Pending deliveries
Scheduled internal transfers
Stock status by warehouse and category

StockSense improves inventory visibility, reduces manual tracking, and maintains a complete history of stock movements.

# Validation

The dashboard should:

Show accurate stock quantities
Display correct KPI values
Prevent unauthorized users from accessing restricted data
Apply filters correctly
Show appropriate empty states
Handle API errors gracefully
Refresh data after stock-changing operations
Never display negative stock unless explicitly supported by business rules
Responsive Design

The dashboard should work across:

Desktop
Laptop
Tablet
Mobile 
>>>>>>> bbb2666428d7a1b9922d4ca635b311f75277a033

