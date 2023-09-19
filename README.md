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

API security is managed with Spring Security 6, using in-memory users. Endpoints that alter the database state (POST and PUT) can only be accessed by administrators.
For testing purposes, we have two users:
'admin' and 'user', each with the authorizations corresponding to their name, as well as the username and password being their own names.

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

## Contributions

Contributions to this project are welcome. Feel free to create pull requests, report issues, or propose improvements.


### In progress

- Working on database user authentication