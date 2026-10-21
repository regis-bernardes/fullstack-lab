# Fullstack Lab

## A Full-Stack Query Executor Management Application

Fullstack Lab is a full-stack web application designed to demonstrate modern software development practices through a query executor management system built with Angular 22, Spring Boot 4, and PostgreSQL.

The project serves as a technical portfolio, showcasing frontend and backend development, REST API integration, database persistence, software architecture, and automated testing.

## 🎯 Project Objectives

- Apply modern Angular features and reactive programming concepts.
- Build a RESTful backend using Spring Boot.
- Implement persistent data storage with PostgreSQL.
- Follow software engineering best practices.
- Develop a maintainable and testable application architecture.
- Demonstrate full-stack development skills through a practical project.

## 🛠️ Technology Stack

| Technology | Purpose |
|---|---|
| Angular 22 | Frontend development |
| TypeScript | Frontend programming language |
| Spring Boot 4 | Backend application and REST APIs |
| Java | Backend programming language |
| PostgreSQL | Relational database |
| Git | Version control |
| Docker and Docker Compose | Local infrastructure and container management |
| Automated testing | Application quality and reliability |

## ✨ Features

- Create, list, edit, and delete database server configurations.
- Create and execute queries, and cancel query execution.
- Track query execution status.
- Integrate the Angular frontend with REST APIs.
- Persist application data in PostgreSQL.
- Use Angular Signals for reactive state management.
- Handle loading states and errors during asynchronous operations.
- Plan and implement automated tests for application components and services.

Features are subject to implementation and verification as the project evolves.

## 🏗️ Architecture

The application follows a layered client-server architecture:

```text
┌──────────────────────────┐
│        Angular 22        │
│         Frontend         │
└────────────┬─────────────┘
             │ HTTP / REST
             ▼
┌──────────────────────────┐
│       Spring Boot 4      │
│         Backend          │
└────────────┬─────────────┘
             │ JPA / JDBC
             ▼
┌──────────────────────────┐
│        PostgreSQL        │
│         Database         │
└──────────────────────────┘
```

The frontend communicates with the backend through HTTP requests. The backend handles business logic and data access, while PostgreSQL stores application data.

## 🚀 Getting Started

### Prerequisites

Make sure the following tools are installed:

- Node.js and npm
- Angular CLI 22
- A Java version compatible with the selected Spring Boot version
- Maven
- Git
- Docker and Docker Compose

### 1. Clone the repository

```bash
git clone https://github.com/regis-bernardes/fullstack-lab.git
```

### 2. Start the infrastructure with Docker Compose

Navigate to the backend directory:

```bash
cd fullstack-lab/backend
docker compose up -d
docker ps -a
```

The containers should include services such as `postgres_controle` and `keycloak_Server`, depending on the names defined in your Docker Compose configuration.

### 3. Configure the database

Now, using terminal let´s create database:

```sql
docker exec -it postgres_controle createdb -U postgres fullstack_lab;
```

The Spring Boot application's YAML configuration should contain the appropriate database connection settings. Configure the database credentials using environment variables before starting the backend.

### 4. Configure Keycloak

The following steps describe an example local Keycloak configuration. Menu labels may vary slightly by Keycloak version.

#### Create a realm

1. Open `http://localhost:8180` in your browser.
2. Open the Keycloak Admin Console and sign in with your configured administrator credentials.(admin/admin)
3. In the top-left corner, open the Realm dropdown, where `master` is displayed.
4. Click **Create realm**.
5. Set **Realm name** to `dbulinkedin-realm`.
6. Ensure the realm is enabled.
7. Click **Create**.

#### Create a client

1. In the left-hand menu, select **Clients**.
2. Click **Create client**.
3. Select **OpenID Connect** as the client type, if prompted.
4. Set **Client ID** to `sql-client`.
5. Click **Next**.
6. Leave Client Authentication OFF, Authorization OFF, check Standard flow and Direct access grants.
7. Click **Next**
8. Configure the valid redirect URIs, such as `http://localhost:4200/*`, if this matches the frontend configuration.
9. Click **Save**.

#### Create a user

1. In the left-hand menu, select **Users**.
2. Click **Create new user**.
3. Enter the following details:
   - **Username:** `admin.dbulinkedin`
   - **Email:** Enter a test email address if required.
   - **First name:** `Test`
   - **Last name:** `User`
   - **Email verified:** Enable this only if the email address has actually been verified.
   - **Enabled:** ON
4. Click **Create**.
5. Open the **Credentials** tab.
6. Click **Set password**.
7. Enter and confirm a password. For purpose of this project use [admin] as password.
8. Set **Temporary** to **OFF** if the user should not be required to change the password at the first login.
9. Save the changes and confirm if prompted.

#### Test the Keycloak configuration

The following example requests a token using the Resource Owner Password Credentials flow (Direct Access Grants). Use it only if this flow is explicitly enabled and required by your local application. For new applications, Authorization Code Flow with PKCE is generally preferred.

```bash
curl -X POST "http://localhost:8180/realms/dbulinkedin-realm/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=sql-client" \
  -d "username=admin.dbulinkedin" \
  -d "password=YOUR_TEST_PASSWORD" \
  -d "grant_type=password"
```

A successful response contains fields similar to the following. The values below are placeholders, not real credentials or tokens:

```json
{
  "access_token": "<ACCESS_TOKEN>",
  "expires_in": 300,
  "refresh_expires_in": 1800,
  "refresh_token": "<REFRESH_TOKEN>",
  "token_type": "Bearer",
  "not-before-policy": 0,
  "session_state": "<SESSION_ID>",
  "scope": "email profile"
}
```

If the request fails, verify the realm name, client ID, user credentials, enabled authentication flow, and client settings.

### 5. Run the backend

From the Spring Boot project directory, run:

```bash
./mvnw spring-boot:run
```

On Windows, use:

```bat
mvnw.cmd spring-boot:run
```

### 6. Run the frontend

From the Angular project directory, install dependencies for WebSockets:

```bash
npm install stompjs
npm install --save-dev @types/stompjs

npm install sockjs-client
npm install --save-dev @types/sockjs-client
```

From the Angular project directory, install dependencies and start the development server:

```bash
npm install
ng serve
```

You can also use the shorthand command:

```bash
ng s
```

Open the application in your browser:

http://localhost:4200

Adjust the commands and directory names to match the actual repository structure.

## 🧪 Testing

Automated testing is planned as part of the project's quality assurance strategy.

**Status:** In progress. Automated tests and test coverage documentation will be added in upcoming updates.

The testing strategy will include:

- **Frontend:** Unit tests for Angular components and services.
- **Backend:** Unit and integration tests for Spring Boot services and REST APIs.
- **Database:** Validation of data persistence and database interactions where applicable.

Test execution instructions will be documented here as the test suites become available.

## 🔐 Security and Configuration

- Keep credentials, tokens, and secrets out of source control.
- Use environment variables for sensitive configuration.
- Validate incoming requests on the backend.
- Apply appropriate authentication and authorization controls as these features are implemented.
- Use development-only credentials for local environments.

## 📌 Project Status

**Status:** Under development.

Fullstack Lab is an evolving portfolio project. New features, tests, architectural improvements, and documentation will be added as development progresses.

## 👨‍💻 Author

**Régis Antonio Bernardes**  
Software Developer | Full-Stack Development

- GitHub: [regis-bernardes](https://github.com/regis-bernardes)
- LinkedIn: [Régis Antonio Bernardes](https://www.linkedin.com/in/regis-antonio-bernardes/)

## 📄 License

This project is available under the MIT License. See the [`LICENSE`](LICENSE) file for details.

---

*Built to demonstrate practical full-stack development skills and continuous learning in modern software engineering.*
