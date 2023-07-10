# QazPay Application

## Overview
QazPay is a rest application that allows users to register themselves, create accounts and make transfers between each other. Application is highly secured, so no need to worry about your data and money.

## Technologies Used
- Java
- Spring Boot
- MySQL
- Spring Security
- Spring Data JPA
- Maven

## Getting Started
Follow these steps to set up and run the QazPay application on your local machine:

1. Prerequisites
   - Java Development Kit (JDK) installed
   - MySQL
   - Maven

2. You need to create a mysql database called "qazpay". Credentials are "root" as an username and "password" as a password. Now you are good to go.

## Architecture
Architecture of an application called "Layered Architecture". In this architecture, the application is divided into separate layers, each with a specific responsibility. Here's a brief overview of each layer:

Models: This layer represents the data structures used in your application, typically in the form of classes or objects. These models encapsulate the data and define the relationships between entities.

Controllers: The controller layer handles the incoming requests from clients and acts as an intermediary between the client and other layers. It receives the requests, performs any necessary validations or transformations, and delegates the business logic to the service layer.

Service: The service layer contains the business logic of your application. It encapsulates complex operations and implements the core functionality of the application. It may use repositories to interact with the data layer and perform CRUD (Create, Read, Update, Delete) operations.

Repository: The repository layer is responsible for data access and persistence. It provides an abstraction over the data storage (e.g., a database) and handles operations such as querying, saving, updating, and deleting data.

## Available endpoints

1. Get User by ID
   - URL: GET /qazpay/api/users/{id}
   - Description: Retrieves an user by its ID.
   - Parameters: id (path variable): ID of the user.
   - Returns: A JSON response containing the retrieved user.

2. Create User
   - URL: POST /qazpay/api/users/register
   - Description: Creates a new user
   - Request Body: JSON representation of the user to be created.
   - Returns: A JSON response containing the created user.

3. Get All Users
   - URL: GET /qazpay/api/users
   - Description: Retrieves all users from a database.
   - Returns: A JSON response containing the list of users.

4. Create Account
   - URL: POST /qazpay/api/accounts
   - Description: Creates new account for user.
   - Request Body: JSON representation of the account to be created.
   - Returns: A JSON response containing the account.
 
5. Make Transfer
   - URL: POST /qazpay/api/transfer
   - Description: Makes transfer between users or accounts.
   - Request Body: JSON representation of the transfer to be maken.
   - Returns: A JSON response containing a transfer.

6. Get Account by ID
    - URL: GET /qazpay/api/accounts/{id}
    - Description: Retrieves an account by it's id
    - Parameters: ID of an account to be retrieved
    - Returns: a JSON response containing account.

7. Get Transfer by ID
   - URL: GET /qazpay/api/transfers/{id}
   - Description: Retrieves a transfer by its ID.
   - Parameters: id (path variable): ID of the transfer.
   - Returns: A JSON response containing the transfer.
