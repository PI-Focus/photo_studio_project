# Focus — Photo studio booking system

![Java 21](https://img.shields.io/badge/Java-21-blue?style=flat)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3-green?style=flat)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-blue?style=flat)
![Redis](https://img.shields.io/badge/Redis-%23DD0031.svg?=flat)
![Gradle](https://img.shields.io/badge/Gradle-Build-lightgrey?style=flat)
![Docker](https://img.shields.io/badge/Docker-Ready-blue?style=flat)
![Frontend](https://img.shields.io/badge/Frontend-HTML_|_CSS_|_JS-orange?style=flat)

A web service for automating photo studio rentals. The project provides an interface for customers, allowing them to view available rooms, choose the right time, and supplement their bookings with the necessary equipment and services from professional photographers. The system features full-fledged user registration and authorization.

## Basic functionality

- Hall catalog: view the interiors, characteristics, and rental prices.
- Booking: select a date and time interval.
- Additional services: add various equipment and photographer services to your order.
- Personal account: securely register, authorize, and manage your bookings.
- Occupancy management: prevent booking a occupied hall or equipment.

## Technology stack

**Backend:**

- Java 21
- Spring Boot
- PostgreSQL
- Redis
- Gradle

**Frontend:**

- HTML5
- CSS3
- JavaScript

**Infrastructure:**

- Docker и Docker Compose
- Traefik
- GitHub Actions

## Necessary dependencies

To run the project, you must have the following installed on your computer **JDK 21**, **Docker** and **Docker Compose**.

## Launch

1. To collect JAR archives, use the following command:
   ```bash
   ./gradlew bootJar
   ```
2. Build a Docker image of the application with the command:
   ```bash
   docker build -t poer1/photostudio-app:latest .
   ```
3. Change env.example according to the configuration and write it to .env
4. Deploy the infrastructure as a team:
   ```bash
   docker compose -f docker-compose.prod.yml up
   ```
5. The project will be accessed via port 80 for **http** and port 443 for **https** when the TLS certificate is successfully obtained
