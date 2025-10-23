# CineSync Backend

A Spring Boot backend application for CineSync - a community platform where movie enthusiasts can discover, discuss, and share their thoughts about movies.

## Overview

CineSync backend provides RESTful APIs for movie discovery using The Movie Database (TMDB) API integration, user authentication, and community features like commenting and discussions. Built with Kotlin and Spring Boot, it offers a secure and scalable foundation for the CineSync movie community platform.

## Features

- **User Authentication & Authorization**
  - JWT-based stateless authentication
  - Secure user registration and login
  - BCrypt password encryption
  - Role-based access control (USER, ADMIN)

- **Movie Discovery**
  - Browse popular movies with pagination
  - Detailed movie information (genres, ratings, cast, production details)
  - Multi-language support (default: zh-TW)
  - Integration with TMDB API for up-to-date movie data

- **Community Features**
  - Comment on movies
  - View comments with user information
  - Paginated comment retrieval
  - Real-time updates via WebSocket support

## Tech Stack

- **Language:** Kotlin 1.9.25
- **Framework:** Spring Boot 3.5.6
- **Java Version:** Java 21
- **Build Tool:** Gradle (Kotlin DSL)
- **Database:** MySQL 8.0
- **Security:** Spring Security + JWT (JJWT 0.11.5)
- **ORM:** Spring Data JPA with Hibernate
- **Reactive:** Spring WebFlux, Kotlin Coroutines
- **API Client:** WebClient (non-blocking HTTP)

## Prerequisites

- Java 21 or higher
- MySQL 8.0
- Gradle (or use the included wrapper)
- TMDB API Key

## Installation & Setup

### 1. Clone the repository

```bash
git clone <repository-url>
cd CineSync-backend
```

### 2. Configure MySQL Database

Create a MySQL database:

```sql
CREATE DATABASE cineSync;
```

### 3. Configure Application Properties

Update `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3307/cineSync
spring.datasource.username=root
spring.datasource.password=your_password

# JWT Configuration
jwt.secret=your_jwt_secret_key
jwt.expiration=86400000

# TMDB API Configuration
tmdb.api.key=your_tmdb_api_key
tmdb.api.base-url=https://api.themoviedb.org/3
```

### 4. Build the Project

```bash
./gradlew build
```

### 5. Run the Application

```bash
./gradlew bootRun
```

The server will start on `http://localhost:8080`

## API Endpoints

### Authentication

#### Register a new user
```http
POST /api/auth/register
Content-Type: application/json

{
  "userName": "john_doe",
  "email": "john@example.com",
  "password": "securePassword123"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "securePassword123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1...",
  "user": {
    "id": 1,
    "userName": "john_doe",
    "email": "john@example.com",
    "role": "USER"
  }
}
```

### User Management

#### Get Current User Profile
```http
GET /api/users/me
Authorization: Bearer <jwt_token>
```

### Movies

#### Get Popular Movies
```http
GET /api/movies/popular?page=1&language=zh-TW
```

**Query Parameters:**
- `page` (optional, default: 1) - Page number
- `language` (optional, default: zh-TW) - Language code

#### Get Movie Details
```http
GET /api/movies/detail/{movieId}?language=zh-TW
```

**Path Parameters:**
- `movieId` (required) - TMDB movie ID

### Comments

#### Create a Comment
```http
POST /api/comments
Authorization: Bearer <jwt_token>
Content-Type: application/json

{
  "movieId": "12345",
  "content": "This movie was amazing!"
}
```

#### Get Comments for a Movie
```http
GET /api/comments/movie/{movieId}?page=0&pageSize=5
```

**Query Parameters:**
- `page` (optional, default: 0) - Page number
- `pageSize` (optional, default: 5) - Number of comments per page

## Project Structure

```
src/main/kotlin/org/example/mason/movie/
├── MovieApplication.kt                 # Spring Boot entry point
├── config/
│   ├── AppConfig.kt                   # Security beans configuration
│   └── SecurityConfig.kt              # Spring Security & CORS setup
├── controller/
│   ├── AuthController.kt              # Authentication endpoints
│   ├── UsersController.kt             # User management endpoints
│   ├── TMDataBaseController.kt        # Movie data endpoints
│   └── CommentController.kt           # Comment management endpoints
├── service/
│   ├── TMDBApiService.kt              # TMDB API integration
│   ├── UsersService.kt                # User business logic
│   ├── CommentsServices.kt            # Comment business logic
│   └── UserDetailsServiceImpl.kt       # Spring Security user details
├── security/
│   ├── JwtTokenProvider.kt            # JWT generation & validation
│   ├── JwtAuthenticationFilter.kt     # Request authentication filter
│   └── UsersPrincipal.kt              # Custom user principal
├── model/
│   ├── entity/                        # JPA entities
│   ├── dto/                           # Data Transfer Objects
│   ├── enum/                          # Enumerations
│   └── json/                          # JSON serialization models
├── repo/                              # Spring Data JPA repositories
├── mapper/                            # Entity <-> DTO mappers
└── specification/                     # JPA Specifications
```

## Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    users_role VARCHAR(50) NOT NULL
);
```

### Comments Table
```sql
CREATE TABLE comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    movie_id VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_user_id (user_id),
    INDEX idx_movie_id (movie_id),
    INDEX idx_created_at (created_at)
);
```

## Configuration

### CORS Configuration
The application is configured to accept requests from:
- `http://localhost:5173` (Frontend development server)
- `http://localhost:8080`

To add more origins, update `SecurityConfig.kt`:

```kotlin
configuration.allowedOrigins = listOf(
    "http://localhost:5173",
    "http://localhost:8080",
    "https://your-production-domain.com"
)
```

### JWT Configuration
- **Expiration:** 24 hours (86400000 ms)
- **Algorithm:** HS256
- **Secret:** Configure in `application.properties`

## Development

### Running Tests
```bash
./gradlew test
```

### Building for Production
```bash
./gradlew bootJar
```

The executable JAR will be created in `build/libs/`

### Running the JAR
```bash
java -jar build/libs/movie-0.0.1-SNAPSHOT.jar
```

## Security Notes

- All passwords are encrypted using BCrypt
- JWT tokens are required for protected endpoints
- CSRF protection is disabled for stateless API
- Session management is stateless
- Sensitive configuration should use environment variables in production

## API Integration

### TMDB API
This project uses The Movie Database (TMDB) API for movie data. You need to:

1. Create an account at [TMDB](https://www.themoviedb.org/)
2. Generate an API key from your account settings
3. Add the API key to `application.properties`

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

[Add your license information here]

## Contact

[Add your contact information here]

## Acknowledgments

- [The Movie Database (TMDB)](https://www.themoviedb.org/) for providing the movie data API
- Spring Boot team for the excellent framework
- Kotlin community for the amazing language

---

Built with Kotlin and Spring Boot
