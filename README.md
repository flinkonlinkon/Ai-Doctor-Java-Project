# DermaVision AI

DermaVision AI is a Java 21 JavaFX semester project for an AI-powered skincare assistant desktop app. It includes authentication, image upload, mock skin analysis, recommendations, OpenAI-powered chat, report history, charts, MySQL/JDBC persistence, and PDF export.

## Tech Stack

- Java 21
- JavaFX with FXML and CSS
- Maven
- MySQL and JDBC
- OpenCV Java dependency
- OpenAI REST API integration
- PDFBox
- MVC architecture

## Project Structure

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

## Running

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

## Notes

The current image analyzer is a mock implementation. It is intentionally isolated behind `SkinAnalyzer` so a real model can be added later without changing the JavaFX GUI.

This software is educational and does not provide medical diagnosis.
