# Microshop

Simple web shop backend using microservices architecture. 

## Services

#### Product Service

Manages information about existing products.

Endpoints:
- create product
- get all products

#### Order Service

Manages client orders.

Endpoints:
- place order

#### Inventory Service

Manages shops stock.

Endpoints:
- is in stock

## Stack

Database:
- PostgreSQL
- MongoDB

Microservice Patterns:
- Spring Cloud Gateway
- Spring Cloud Circuit Breaker

Security:
- Spring Security
- Keycloak

Discovery:
- Netflix Eureka

Observability:
- Grafana Tempo
- Prometheus
- Loki
- Zipkin

Testing:
- JUnit5
- Testcontainers

Async communication:
- Kafka
