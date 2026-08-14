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

| Description | Member | ID |
|------|--------------|--------|
| Foundation & Data Models — project setup, config, core models | Fahim Shahriar Linkon | 251-15-765 |
| Database Layer — DAOs, DB connection & initialization | Mahfuz Ahmed Mahin | 251-15-923 |
| Authentication & Utilities — login, register, session, helpers | Jobayer Ahmed | 251-15-813 |
| AI Skin Analysis Engine — image analysis, OpenCV, OpenAI, chatbot | Dip Samadder | 251-15-646 |
| Reports & Recommendations — PDF reports, recommendation engine, history | Soumik Sarker | 251-15-285 |
| Dashboard UI & Assets — navigation shell, home, profile, settings, styling | Abrar Shahriar | 251-15-826 |

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
