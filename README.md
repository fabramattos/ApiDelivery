*Mude o idioma:* [![pt-br](https://img.shields.io/badge/lang-pt--br-green.svg)](https://github.com/fabramattos/ApiDelivery/blob/main/README_pt-BR.md)
🚧 WORK IN PROGRESS 🚧

# Restaurant Delivery Control
A simplified API for controlling restaurant delivery services.<br>
This project, originally developed by me in Java, was part of a selection process for a Java BackEnd position.<br>
I decided to redo it in Kotlin, slightly changing the stack and adding some functionalities as part of my studies.<br>

## Stack
### RESTful API:
- Kotlin
- Spring Boot 3
- Spring (Security, Data, Web)
- Docker
- PostgresSQL
- Gradle
- OpenAPI 3.0 (Swagger)

## Features
### Security:
- Allow user registration and login with JWT token authentication. The methods of the APIs below can only be executed if the user is logged in.
### Customer:
- Allow registration, modification, deletion, and querying of customers.
### Order:
- Allow registration, modification, deletion, and querying of orders. An order must have a customer, and a customer can have multiple orders.
### Delivery:
- Allow registration, modification, deletion, and querying of deliveries. A delivery must be linked to an order.
### Note on Deletion:
- When deleting a user, checks are performed. Users and orders can only be deleted if there is no ongoing delivery.<br>
- If a user is deleted, all associated data is deleted.<br>
- If an order is deleted, the delivery is deleted.<br>
- If a delivery is deleted, the order is dissociated from the delivery.<br>

## Execution Instructions
ℹ️ JWT authentication, login and details will be available at application's Swagger-UI endpoint ℹ️

### Locally, without Docker:
1. Ensure you have installed on your machine: Java 19, PostgreSQL.
2. In PostgreSQL, create a database for the application.
3. Clone this repository to your local environment.
4. Configure the database credentials in the application.yml file.
5. Run the command ```gradle bootRun```
6. (in development) The API will be available at: http://localhost:8080/swagger-ui.html


### Locally, with Docker:
😎 No need for configuration in project files 😎

1. Ensure you have installed and running on your machine: Docker, Java 19.
2. Clone this repository to your local environment.
3. Run the following commands in the IDE terminal (at the project root):
- Gradle: ```gradle build``` (Generates the artifact)
- Docker: ```docker build -t delivery-api -f Dockerfile-dev .``` (Uses the DockerFile-dev to generate the Docker image)
- Docker: ```docker-compose up``` (Uses Docker-Compose to configure and run the API and PostgreSQL)
4. (in development) The API will be available at: http://localhost:8080/swagger-ui.html


## Future Improvements:
### In the app:
- Better refinement to handle exceptions due to validations, showing more coherent error messages to the front-end.
- Tests for Controllers with mockMvc.
- Creating an architecture for "Items," replacing the item description for inventory control, adding items, removing items, calculating order price.
- Role-based control to allow an admin to register items in the inventory.
### CI/CD:
- Restructure branch architecture and workflows in GitHub Actions.
