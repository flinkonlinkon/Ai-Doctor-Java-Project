# DermaVision AI

## Intro

DermaVision AI is a Java 21 JavaFX semester project for an AI-powered skincare assistant desktop application. It allows a user to register/login, upload a photo of their skin, receive an AI-driven skin analysis, view personalized recommendations, chat with an AI assistant for follow-up advice, track their analysis history, and export results as a PDF report — all backed by a MySQL database.

This software is built for educational purposes and does not provide medical diagnosis.

## Key Features

- Secure user authentication (register, login, forgot password, session handling)
- Skin image upload and AI-powered skin analysis
- OpenCV-based image preprocessing
- OpenAI-powered chatbot for skincare Q&A
- Personalized skincare recommendations engine
- Analysis history tracking per user
- PDF report generation and export
- Dashboard with profile, settings, and dark mode
- MySQL/JDBC persistence layer
- Built with Java 21, JavaFX (FXML + CSS), Maven, MVC architecture

## Team Task Distribution

| Part | Description | Member | ID |
|------|--------------|--------|----|
| Part 1 | Foundation & Data Models — project setup, config, core models | Fahim Shahriar Linkon | 251-15-765 |
| Part 2 | Database Layer — DAOs, DB connection & initialization | Mahfuz Ahmed Mahin | 251-15-923 |
| Part 3 | Authentication & Utilities — login, register, session, helpers | Jobayer Ahmed | 251-15-813 |
| Part 4 | AI Skin Analysis Engine — image analysis, OpenCV, OpenAI, chatbot | Dip Samadder | 251-15-646 |
| Part 5 | Reports & Recommendations — PDF reports, recommendation engine, history | Soumik Sarker | 251-15-285 |
| Part 6 | Dashboard UI & Assets — navigation shell, home, profile, settings, styling | Abrar Shahriar | 251-15-826 |

### Part 1 — Fahim Shahriar Linkon (251-15-765)
Foundation & Data Models

This part lays the groundwork that every other part depends on. It sets up the Maven project configuration and the JavaFX application entry point that launches the whole app, and defines the core data model classes used throughout the system.

- Configured the project build (`pom.xml`) with all required dependencies (JavaFX, MySQL connector, PDFBox, OpenCV) and set up `Main.java` as the application's entry point
- Designed and implemented the core data model classes: `User` (account details), `SkinIssue` (individual detected skin problem), `Severity` (enum for issue severity levels), `ChatMessage` (chatbot conversation entries), `SkinAnalysisResult` (full analysis output), `Report` (a saved analysis session), and `Recommendation` (suggested skincare advice)
- Wrote the database schema (`schema.sql`) defining all tables and relationships used by the app
- Authored the project documentation explaining the system design

### Part 2 — Mahfuz Ahmed Mahin (251-15-923)
Database Layer

This part handles all communication between the application and the MySQL database, following the DAO (Data Access Object) design pattern to keep SQL logic isolated from business logic.

- Built `DatabaseConnection` and `DatabaseConfig` to manage JDBC connections and configuration settings, and `DatabaseInitializer` to set up the schema on first run
- Implemented DAO classes — `UserDAO` (user accounts), `ReportDAO` (saved analysis reports), `RecommendationDAO` (skincare recommendations), and `ChatHistoryDAO` (chatbot conversation logs) — each responsible for reading and writing its respective data to MySQL
- Ensured all other parts (auth, reports, chatbot) can persist and retrieve data without writing raw SQL themselves

### Part 3 — Jobayer Ahmed (251-15-813)
Authentication & Utilities

This part manages user identity — registration, login, password recovery, and session tracking — plus shared helper utilities used across the whole app.

- Implemented `AuthService` to handle registration and login logic, verifying credentials against the database
- Built utility classes: `PasswordHasher` (secure password hashing), `SessionManager` (tracks the currently logged-in user across screens), `ValidationUtil` (input validation for forms), `ViewLoader` (loads FXML screens), `AlertUtil` (popup alerts/messages), and `AnimationUtil` (UI transition animations like fade effects)
- Designed the Login, Register, and Forgot Password screens and their controllers, giving users a complete entry flow into the app

### Part 4 — Dip Samadder (251-15-646)
AI Skin Analysis Engine

This part is the core intelligence of the app — turning an uploaded photo into a skin analysis, and powering the AI chatbot for follow-up questions.

- Defined the `SkinAnalyzer` interface as a contract for analysis logic, with `MockSkinAnalyzer` as a working offline implementation used when no live AI service is available
- Built `OpenCVSkinPreprocessor` to prepare and clean uploaded images (using OpenCV) before analysis
- Implemented `OpenAIService` to connect to the OpenAI API for AI-driven analysis, and `ChatbotService` to power an interactive skincare chatbot
- Created the Upload screen (photo submission and analysis trigger) and Chatbot screen (conversational Q&A) along with their controllers

### Part 5 — Soumik Sarker (251-15-285)
Reports & Recommendations

This part turns raw analysis results into something useful for the user — downloadable reports, tailored advice, and a browsable history.

- Built `PdfReportService` to generate downloadable PDF reports summarizing a user's skin analysis using Apache PDFBox
- Implemented `ReportService` to manage saving, fetching, and organizing a user's analysis reports
- Designed `RecommendationEngine` to generate personalized skincare recommendations based on detected issues and severity
- Created the Recommendations screen (shows suggested skincare actions) and History screen (past analysis sessions) along with their controllers

### Part 6 — Abrar Shahriar (251-15-826)
Dashboard UI & Assets

This part is the main shell of the application — everything the user sees and how they move between features after logging in, plus the app's visual identity.

- Built `DashboardController`, the navigation shell that lets users switch between Home, Profile, Upload, Chatbot, History, Recommendations, and Settings within a single window, and handles logout
- Implemented `HomeController`, the dashboard landing page that pulls the user's latest analysis summary (score, severity, top skin issues) from Part 5's `ReportService` and displays it dynamically
- Built `ProfileController` (displays account details) and `SettingsController` (app preferences, including a dark mode toggle and AI connection status)
- Designed the app's overall visual styling (`styles.css`) covering colors, fonts, and component design used across every screen, plus visual assets like the app logo and illustrations

## Citation + Report Drive Link

- **Citation:** DermaVision AI, [Object Oriented Programming], [5th Semester].
- **Report Drive Link:** _https://drive.google.com/file/d/1G-PDbwaYrn59z_QrLYAQcGdCYuh986yA/view?usp=drivesdk_

## Instruction

### Tech Stack

- Java 21
- JavaFX with FXML and CSS
- Maven
- MySQL and JDBC
- OpenCV Java dependency
- OpenAI REST API integration
- PDFBox
- MVC architecture

### Project Structure

```text
src/main/java/com/dermavisionai/
  Main.java
  controller/
  model/
  service/
  database/
  utils/
src/main/resources/
  fxml/
  css/
  images/
  sql/
docs/
```

### Running

```powershell
mvn clean javafx:run
```

Optional environment variables:

```powershell
$env:OPENAI_API_KEY="your_openai_api_key"
$env:DERMAVISION_DB_URL="jdbc:mysql://localhost:3306/dermavision_ai"
$env:DERMAVISION_DB_USER="root"
$env:DERMAVISION_DB_PASSWORD="your_password"
```

### Notes

The current image analyzer is a mock implementation. It is intentionally isolated behind `SkinAnalyzer` so a real model can be added later without changing the JavaFX GUI.
