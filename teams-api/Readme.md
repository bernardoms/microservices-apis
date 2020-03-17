## Teams-API

# Technologies

- MongoDB 4
Used because is a database with great replication factor, fast to read on indexed field and able to scale up and down

- Docker
Used for create application image and run on container for be able to run many instances in one machine, and for replicate the same environment between the instances. For local is used to bring up a mongodb instance for be able to run the code in a similar way of the production.

- Spring-Boot 2.2.5
Used Spring because of the abstractions of the framework and the easy and faster to write codes, for example with spring data is really fast to create a confection between the application and database.

- Swagger
Great for document api endpoints showing examples.

- Java 11
Used Java version 11 because of the performance improvements to run JAVA in a instance inside a container.

- Wiremocks
Used for simulate responses from external api, used for integration tests and for run local.

- Hysterix Circuit Breaker
Used for resilience, so if the campaign api is down, it's possible to return other team information to the client.

# How to run

from the root of the project cd deps.
docker compose up -d for startup a mongo in a container and the wiremocks.

# API Documantion endpoints
http://localhost:8081/swagger-ui.html
