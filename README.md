# Auth-Service-Using-Spring-boot-3-and-spring-security-6
#### This is a simple authentication service using spring boot 3 and spring security 6. The service provides the following features:
### Technologies used:
- Java 17
- Spring Boot 3
- Spring Security 6
- Maven
- Postgresql
- Docker
- Docker-compose
- Junit 5
- CI/CD (Github Actions).
- Swagger (openApi 3.0)
- Lombok
- JPQL (java Persistence Query Language).
- Hibernate (JPA).
- JWT (Json Web Token).

### Features:
- User registration.
- User login.
- forgot password.
- reset password.
- guard routes based on user roles.
- get Current Authenticated user.
- continues integration and deployment (CI/CD).
   - compile stage.
   - test stage.
   - build docker image stage, and ensure up and running.
   - deploy to cloud vps (Digital Ocean Droplet).
### How to run the app:
- Clone the repository to your local machine `git clone repo-url`.
- navigate to the root directory of the project.
- create .env file from .example.
- so, open terminal and run `cp .env.example .env`.
- open the .env file and set the environment variables.
- run `docker-compose up -d ` to start the server and the database.
- open the browser and navigate to http://localhost:8081/swagger-ui.html .