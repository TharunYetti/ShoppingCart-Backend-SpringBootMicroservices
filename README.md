# ShoppingCart-Backend-SpringBootMicroservices

## Overview
This repository contains the backend implementation for a shopping cart application. It is built using the microservices architecture with Spring Boot and deployed using Docker and Kubernetes. The backend services include:

- **Product Service**: Manages product details and inventory.
- **Payment Service**: Handles payment processing and transactions.
- **Order Service**: Manages order creation and status.
- **Config Server**: Centralized configuration management.
- **Service Registry**: Service discovery using Eureka.
- **Cloud Gateway**: API gateway for routing requests.

## Features
- Microservices architecture with Spring Boot.
- Centralized configuration management.
- Service discovery and load balancing using Eureka.
- Docker containerization for individual services.
- Kubernetes for orchestration and deployment.
- RESTful APIs for interaction between services.

## Technologies Used
- **Programming Language**: Java
- **Framework**: Spring Boot
- **Microservices Tools**: Spring Cloud Config, Eureka Server, Cloud Gateway
- **Database**: MySQL (or specify other DBs used)
- **Build Tool**: Maven
- **Containerization**: Docker (using Jib plugin )
- **Orchestration**: Kubernetes
- **Monitoring**: Zipkin

## Prerequisites
To run this application locally, you need:

- **Java** (JDK 17 or above)
- **Maven** (latest version)
- **Docker** (latest version)
- **Kubernetes** (minikube or other Kubernetes tools)

## Installation and Setup
### 1. Clone the Repository
```bash
$ git clone https://github.com/TharunYetti/ShoppingCart-Backend-SpringBootMicroservices.git
$ cd ShoppingCart-Backend-SpringBootMicroservices
```

### 2. Build the Application
Build all services using Maven:
```bash
$ mvn clean install
```

### 3. Push Docker Images with Jib Plugin
The Jib plugin is configured in the `pom.xml` of each service to build and push Docker images directly to a Docker registry. Use the following command:
```bash
$ mvn jib:build
```
Ensure that the Docker registry credentials are properly configured in your environment or Maven settings.

### 4. Kubernetes Deployment
- Create Kubernetes YAML files for each service (deployment and service specs).
- Apply Kubernetes configurations:
  ```bash
  $ kubectl apply -f k8s/
  ```

### 5. Access the Application
- **API Gateway**: Access all services through the API gateway at `http://<your-kubernetes-node>:<gateway-port>`

## Directory Structure
```
ShoppingCart-Backend-SpringBootMicroservices/
├── ProductService/          # Product service source code
├── PaymentService/          # Payment service source code
├── OrderService/            # Order service source code
├── ConfigServer/            # Configuration server source code
├── service-registry/         # Eureka server source code
├── CloudGateway/            # API gateway source code
├── k8s/               # Kubernetes YAML configuration files
├── docker-compose.yml        # Docker Compose configuration
└── README.md                 # Project documentation
```

## API Endpoints
| Service        | Endpoint                        | Description                        |
|----------------|---------------------------------|------------------------------------|
| Product        | `/api/products`                | Get all products                  |
| Payment        | `/api/payments`                | Process payments                  |
| Order          | `/api/orders`                  | Create and fetch orders           |

## Deployment Notes
1. Ensure that all services are properly configured with environment variables for database connections, Eureka server, and Config server.
2. Use Kubernetes ConfigMaps and Secrets for managing sensitive configurations.
3. Monitor services using Kubernetes dashboard or other monitoring tools.

## Contributing
As this is all part of my learning, Contributions are not allowed.

## License
This project is licensed under the Apache License. See the LICENSE file for details.

## Contact
For questions or feedback, feel free to contact:
- **Name**: Tharun Yetti
- **GitHub**: [TharunYetti](https://github.com/TharunYetti)
- **LinkedIn**: [TharunYetti](https://linkedin.com/in/tharun-yetti-9775a0280/)

