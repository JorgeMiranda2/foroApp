# ForoApp

ForoApp is a backend application built with Spring Boot. The application manages entities such as `Topic`, `User`, `Profile`, `Response`, `Course`, and handles JWT tokens for authentication.

## Features

- Management of forum entities: Topic, User, Profile, Response, Course
- Authentication using JWT tokens
- Security implementation with Spring Security
- Data persistence using JPA
- Data validation with Spring Validation
- Database migrations with Flyway

## Requirements

- Java 17
- Maven
- PostgreSQL

## Installation

1. Clone the repository
   ```bash
   git clone https://github.com/your_username/foroApp.git
   ```

2. Navigate to the project directory
  ```bash
   git clone https://github.com/your_username/foroApp.git
  ```
3. Configure the database in application.properties
 ```bash
    spring.datasource.url=jdbc:postgresql://localhost:5432/foroApp
    spring.datasource.username=your_username
    spring.datasource.password=your_password
  ```
4. Compile and run the application
   ```bash
    mvn clean install mvn spring-boot:run
   ```

## Main Dependencies

- Spring Boot Starter Data JPA
- Spring Boot Starter Validation
- Spring Security Core
- Spring Boot Starter Security
- JJWT
- Spring Boot Starter Web
- Flyway Core
- PostgreSQL Driver

## Usage
You can access the application at http://localhost:8080. The application exposes several endpoints to manage topics, users, profiles, responses, and courses.

## Project Structure

src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── foroApp/
│   │           ├── config/
│   │           ├── controller/
│   │           ├── model/
│   │           ├── repository/
│   │           ├── security/
│   │           └── service/
│   ├── resources/
│   │   ├── application.properties
│   │   └── static/
│   │       └── ...
│   └── webapp/
│       └── ...
└── test/
    └── java/
        └── com/
            └── foroApp/
                └── ...

