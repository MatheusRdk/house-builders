# House, Architects, and Engineers Management API

This is a Java 17 and Spring Boot 3 API project that enables the management of houses, architects, and engineers. The API provides basic CRUD operations for each entity and use a MySQL database (running in Docker).

## Database config

Feel free to change the database used, as JPA and hibernate are responsible for creating and interacting with the database.
If you want to do this, remember to change the properties in application.yml, and add the correct dependencies in pom.xml

As it stands, you need to configure the available port on your computer to run the mysql image, in docker-compose.yml. And then, update this port in application.yml (datasource:url).

## Requirements

- Java 17
- Spring Boot 3
- MySQL

## Security

API security is effectively managed through the utilization of Spring Security 6 in conjunction with JWT tokens, and our system maintains a user database. Specifically, endpoints responsible for modifying the database state (i.e., HTTP POST and PUT requests) are restricted to administrator-level access only. Additionally, all incoming requests require authentication via user login.

To create a new user, the process involves accessing the following URL: `http://server:port/auth/register`. You must provide a JSON payload containing the fields "login," "password," and "role" (which can be either "ADMIN" or "USER").

To initiate a login session, you need to send a JSON payload containing "login" and "password" to the URL: `http://server:port/auth/login`. Following successful authentication, you will receive a JWT token, which is mandatory for accessing the restricted endpoints.

For testing purposes, anyone is allowed to create a user with an "ADMIN" role. However, for production environments, it is advisable to create an administrator directly within the database. The endpoint should be primarily used for creating "USER" roles.
## Entities and Endpoints

### Houses

#### GETs

- `/houses/all`: List all houses.
- `/houses/{id}`: Search for a house by ID.
- `/houses/find`: Search for houses by name (example: `/houses/find?name=Project+Name`).

#### POST

- `/house`: Create a new house. The ID is generated automatically and requires the house's name, along with optional engineer and architect IDs.

#### PUT

- `/house`: Update a house. All attributes must be provided in the JSON sent, even if they are not being changed (repeat the same values).

#### DELETE

- `/house/{id}`: Delete a house by ID.

### Architects

#### GETs

- `/architects/all`: Return all architects, and also all the house projects each one participates in.
- `/architects/{id}`: Search for an architect by ID.
- `/architects/find`: Search for architects by name.

#### POST

- `/architects`: Register a new architect, requiring only the name.

#### PUT

- `/architects`: Change the name of an architect, requiring the ID.

#### DELETE

- `/architects/{id}`: Delete an architect by ID, provided they are not linked to any house.

### Engineers

#### GETs

- `/engineers/all`: Return all engineers, and also all the home projects each one participates in.
- `/engineers/{id}`: Search for an engineer by ID.
- `/engineers/find`: Search for engineers by name.

#### POST

- `/engineers`: Register a new engineer, requiring only the name.

#### PUT

- `/engineers`: Change the name of an engineer, requiring the ID.

#### DELETE

- `/engineers/{id}`: Delete an engineer by ID, provided they are not linked to any house.

## Using the API

- Ensure that the database is available and configured appropriately.
- Run the Spring Boot project.
- Use the endpoints mentioned above to manage houses, architects, and engineers.
- Access endpoints that require administrator authorization using appropriate credentials.

## Swagger
- To better understand how to use the API, you can access the documentation at `http://server:port/swagger-ui.html`, depending on how you have configured your server and port settings.
- You can test anything in the API using Swagger, including user registration and login functionalities. To access the endpoints, simply insert the JWT token into the "padlock" field located on the right side of each endpoint in swagger, or in the "Authorize" field above the endpoints to authorize all.

## Contributions

Contributions to this project are welcome. Feel free to create pull requests, report issues, or propose improvements.