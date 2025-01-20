# CleanSlate Backend

CleanSlate Backend is a Java-based application designed to support the CleanSlate platform, enabling users to remotely wipe their devices in case they are lost or stolen. The backend is implemented using Spring Boot and comes with an associated Docker image for easy deployment.

## Features

- **Remote Device Wipe**: Provides secure APIs to initiate remote factory resets on devices.
- **No Personal Data Collection**: Prioritizes user privacy by not storing any personal information.
- **Secure Communication**: Ensures data is transmitted securely using industry-standard protocols.
- **Containerized Deployment**: Offers a pre-configured Docker image for rapid deployment.

---

## Prerequisites

Before you begin, ensure you have the following installed:

- Java 11 or higher
- Maven 3.6 or higher
- Docker
- Git

---

## Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/khalifarsm/botox-web.git
cd botox-web
```

### 2. Build the Project

Use Maven to build the project:
```bash
mvn clean install
```

### 3. Run Locally

Run the application locally using the Spring Boot plugin:
```bash
mvn spring-boot:run
```

The backend will be accessible at `http://localhost:8200`.

---

## Docker Deployment

### Build the Docker Image

If you need to build the Docker image:
```bash
docker build -t <your-dockerhub-username>/cleanslate-backend .
```

### Pull the Docker Image

Alternatively, pull the pre-built image from GitHub Container Registry:
```bash
docker pull ghcr.io/khalifarsm/botox:master
```

### Run the Docker Container

Start the container using:
```bash
docker run -d -p 8200:8200 --name cleanslate-backend ghcr.io/khalifarsm/botox:master
```

---

## API Documentation

API documentation is available at `/swagger-ui.html` once the backend is running. It provides detailed information about available endpoints and their usage.

---

## Configuration

### Environment Variables

The backend can be configured using the following environment variables:

| Variable              | Description                  | Default Value        |
|-----------------------|------------------------------|----------------------|
| `SERVER_PORT`         | Port for the backend server | `8200`               |
| `DATABASE_URL`        | JDBC URL for the database   | `jdbc:h2:mem:testdb` |
| `LOGGING_LEVEL`       | Logging level for the app   | `INFO`               |

### External Configuration File

You can also provide an external configuration file by mounting it as a volume in Docker or specifying the `--spring.config.location` property when starting the application.

---

## Contributing

Contributions are welcome! To contribute:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m 'Add your feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request

---

## License

CleanSlate Backend is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.

---

## Contact

For questions or support, please contact:

- **Email**: khalifarsm@gmail.com
- **GitHub Issues**: [https://github.com/khalifarsm/botox-web/issues](https://github.com/<your-username>/cleanslate-backend/issues)

