# University Advisor Finder

A web application that helps students find university advisors based on their research interests. Built with Spring Boot backend and React frontend.

## Features

- **For Students:**
  - Browse advisors without registration
  - Search by research interests, department, or keywords
  - Filter by research categories and specific interests
  - Register to access advisor contact information
  - View detailed advisor profiles

- **For Lecturers:**
  - Maintain research profile with categorized interests
  - Organize research areas into categories and subcategories
  - Contact information accessible to registered students only

## Technology Stack

- **Backend:** Spring Boot 3.2.0, Java 17
- **Frontend:** React 18, Bootstrap 5
- **Database:** MySQL 8.0
- **Build Tools:** Maven, npm

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Node.js 16+ and npm
- MySQL 8.0+

## Installation

### Database Setup

1. Install MySQL and create a database:
```sql
CREATE DATABASE advisor_finder_db;
```

2. Update database credentials in `src/main/resources/application.properties`

### Backend Setup

1. Navigate to the project root directory
2. Install dependencies and run the Spring Boot application:
```bash
mvn clean install
mvn spring-boot:run
```

The backend will start on http://localhost:8080

### Frontend Setup

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the React development server:
```bash
npm start
```

The frontend will start on http://localhost:3000

## API Endpoints

### Lecturers
- `GET /api/lecturers/public` - Get all lecturers (public info only)
- `GET /api/lecturers/search?keyword={keyword}` - Search lecturers
- `GET /api/lecturers/by-interests?interestIds={ids}` - Filter by research interests
- `GET /api/lecturers/by-category/{categoryId}` - Filter by research category
- `GET /api/lecturers/{id}/contact?studentEmail={email}` - Get contact info (requires student registration)

### Students
- `POST /api/students/register` - Register a new student
- `GET /api/students/check-email?email={email}` - Check if email exists

### Research
- `GET /api/research/categories` - Get all research categories
- `GET /api/research/interests` - Get all research interests
- `GET /api/research/interests/category/{categoryId}` - Get interests by category

## Project Structure

```
├── src/main/java/com/university/advisorfinder/
│   ├── AdvisorFinderApplication.java
│   ├── config/
│   │   ├── DataInitializer.java
│   │   ├── SecurityConfiguration.java
│   │   └── WebConfig.java
│   ├── controller/
│   │   ├── LecturerController.java
│   │   ├── ResearchController.java
│   │   └── StudentController.java
│   ├── dto/
│   │   ├── LecturerContactDTO.java
│   │   ├── LecturerPublicDTO.java
│   │   ├── ResearchInterestDTO.java
│   │   └── StudentRegistrationDTO.java
│   ├── model/
│   │   ├── Lecturer.java
│   │   ├── ResearchCategory.java
│   │   ├── ResearchInterest.java
│   │   └── Student.java
│   ├── repository/
│   │   ├── LecturerRepository.java
│   │   ├── ResearchCategoryRepository.java
│   │   ├── ResearchInterestRepository.java
│   │   └── StudentRepository.java
│   └── service/
│       ├── LecturerService.java
│       ├── ResearchService.java
│       └── StudentService.java
└── frontend/
    ├── public/
    ├── src/
    │   ├── components/
    │   ├── pages/
    │   ├── services/
    │   └── App.js
    └── package.json
```

## Default Research Categories

The application comes with pre-configured research categories:

- **Engineering:** Civil, Computer, Electrical, Mechanical Engineering
- **Computer Science:** AI, Software Engineering, Data Science, Cybersecurity
- **Business:** Marketing, Finance, Management
- **Sciences:** Biology, Chemistry, Physics, Mathematics

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## License

This project is licensed under the MIT License.
