# Library Management System API

## Overview

The Library Management System API is designed to manage a library's books and patrons efficiently. It provides functionality for adding, updating, retrieving, and deleting books and patrons. Additionally, it allows for tracking the borrowing and returning of books.

## Features

- **Book Management**: Add, update, retrieve, and delete books.
- **Patron Management**: Add, update, retrieve, and delete patrons.
- **Borrowing Management**: Track book borrowing and returning with validation for active borrowing records.

## Technologies Used

- **Spring Boot**: Framework for building the RESTful API with embedded Tomcat server.
- **Spring Data JPA**: For data access and persistence.
- **Hibernate**: ORM tool used for mapping Java objects to database tables.
- **MySQL**: Relational database management system used for data storage (or H2 for in-memory testing).
- **Jakarta Validation**: For validating input data.
- **JUnit & Mockito**: For unit and integration testing.
- **Maven**: Build automation tool used to manage project dependencies and build the application.

## Project Structure

- `src/main/java`: Contains the main application code.
  - `com.mabotalb.library_system_api.controller`: REST controllers handling API requests.
  - `com.mabotalb.library_system_api.entity`: JPA entity classes representing the database schema.
  - `com.mabotalb.library_system_api.repository`: Repository interfaces for data access.
  - `com.mabotalb.library_system_api.service`: Service classes implementing business logic.
- `src/main/resources`: Configuration files.
  - `application.properties`: Application configuration, including database settings.
- `src/test/java`: Contains unit and integration tests.

## API Endpoints

### Books

- **Get All Books**
  - **Endpoint**: `GET /api/books`
  - **Description**: Retrieve a list of all books.
  - **Response**: Success message with the list of books.
- **Get Book by ID**
  - **Endpoint**: `GET /api/books/{id}`
  - **Description**: Retrieve details of a specific book.
  - **Response**: Success message with the book details.
- **Add a New Book**
  - **Endpoint**: `POST /api/books`
  - **Description**: Add a new book to the library.
  - **Request Body**: Book details in JSON.
  - **Response**: Success message with the created book details.
- **Update a Book**
  - **Endpoint**: `PUT /api/books/{id}`
  - **Description**: Update an existing book's information.
  - **Request Body**: Updated book details in JSON.
  - **Response**: Success message with the updated book details.
- **Delete a Book**
  - **Endpoint**: `DELETE /api/books/{id}`
  - **Description**: Remove a book from the library.
  - **Response**: Success message.

### Patrons

- **Get All Patrons**
  - **Endpoint**: `GET /api/patrons`
  - **Description**: Retrieve a list of all patrons.
  - **Response**: Success message with the list of patrons.
- **Get Patron by ID**
  - **Endpoint**: `GET /api/patrons/{id}`
  - **Description**: Retrieve details of a specific patron.
  - **Response**: Success message with the patron details.
- **Add a New Patron**
  - **Endpoint**: `POST /api/patrons`
  - **Description**: Add a new patron to the library.
  - **Request Body**: Patron details in JSON.
  - **Response**: Success message with the created patron details.
- **Update a Patron**
  - **Endpoint**: `PUT /api/patrons/{id}`
  - **Description**: Update an existing patron's information.
  - **Request Body**: Updated patron details in JSON.
  - **Response**: Success message with the updated patron details.
- **Delete a Patron**
  - **Endpoint**: `DELETE /api/patrons/{id}`
  - **Description**: Remove a patron from the library.
  - **Response**: Success message.

### Borrowing Records

- **Borrow a Book**
  - **Endpoint**: `POST /api/borrow/{bookId}/patron/{patronId}`
  - **Description**: Allow a patron to borrow a book.
  - **Response**: Success message with the borrowing record details.
- **Return a Book**
  - **Endpoint**: `PUT /api/return/{bookId}/patron/{patronId}`
  - **Description**: Record the return of a borrowed book.
  - **Response**: Success message the updated borrowing record details.

## Running the Application

1. Clone the Repository:

   ```bash
   git clone https://github.com/MohamedAbotalb/Spring_Library_System_API.git
   cd Spring_Library_System_API
   ```

2. Configure the Database:

   - **MySQL**: Update `src/main/resources/application.properties` with your database settings.

   Example configuration for MySQL:

   ```bash
   spring.datasource.url=jdbc:mysql://localhost:3306/library_db
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   spring.jpa.hibernate.ddl-auto=update
   ```

3. Build and Run:

```bash
mvn clean install
mvn clean spring-boot:run
```

The application will start on `http://localhost:3000`.

## Testing

To run unit and integration tests:

```bash
mvn test
```

## Error Handling

- **400 Bad Request**: Invalid input data or request format.
- **404 Not Found**: Resource not found.
- **500 Internal Server Error**: Unexpected server error.
