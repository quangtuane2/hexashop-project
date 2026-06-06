# Hexashop Project

Hexashop-project is an e-commerce web application designed for selling clothing, including pants, shirts, and costumes for men, women, and children.

## 🚀 Technologies Used

- **Backend:** Java 21, Spring Boot 3.2.4
- **Database:** MySQL, Spring Data JPA
- **Frontend/Template Engine:** Thymeleaf (integrated with Spring Security)
- **Security:** Spring Security
- **Email Service:** Spring Boot Mail (SMTP)
- **Build Tool:** Maven

## 📁 Project Structure

- `src/main/java/.../hexashop_project/`: Core Java source code (Controllers, Services, Repositories, Entities, etc.).
- `src/main/resources/`: Configuration files (`application.properties`), Thymeleaf templates (`templates/`), and static assets (`static/`).
- `UploadFiles/`: Local directory designated for storing user-uploaded files (e.g., product images).
- `hexashop-project.sql`: Database schema and sample data dump.
- `pom.xml`: Maven configuration file managing project dependencies.

## ⚙️ Prerequisites

Before you begin, ensure you have met the following requirements:
- **Java Development Kit (JDK):** Version 21 or higher.
- **Database:** MySQL Server installed and running.
- **Maven:** Installed (Alternatively, you can use the included `mvnw` wrapper).

## 🛠️ Setup & Installation

### 1. Clone the repository
```bash
git clone <repository_url>
cd hexashop-project
```

### 2. Database Setup
1. Create a new MySQL database for the project (e.g., `hexashop`).
2. Import the provided SQL dump to initialize the database schema and sample data:
   ```bash
   mysql -u root -p hexashop < hexashop-project.sql
   ```
3. Update the database connection credentials in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/hexashop
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```

### 3. Email Configuration
To utilize email features (e.g., sending verification emails), configure the SMTP settings in `application.properties`:
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
```
*(Refer to the comments in `application.properties` for instructions on how to generate a Gmail App Password).*

### 4. Build and Run the Project
You can run the application directly using the Maven wrapper:

**On Windows:**
```cmd
.\mvnw.cmd spring-boot:run
```

**On Linux/Mac:**
```bash
./mvnw spring-boot:run
```

Alternatively, you can package the application into a `.war` file and deploy it to a servlet container like Tomcat.

### 5. Access the Application
Once the application has started successfully, open your web browser and navigate to:
```text
http://localhost:9090
```
*(Note: Ensure port 9090 is available, or modify `server.port` in `application.properties` if needed).*
