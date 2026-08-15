# Installation Guide

## Prerequisites

- Java 21
- Maven 3.9+
- MySQL 8+ for full persistence

## Database Setup

Run:

```sql
SOURCE src/main/resources/sql/schema.sql;
```

Set environment variables before launching:

```powershell
$env:DERMAVISION_DB_URL="jdbc:mysql://localhost:3306/dermavision_ai"
$env:DERMAVISION_DB_USER="root"
$env:DERMAVISION_DB_PASSWORD="your_password"
```

If MySQL is not available, the application opens in demo memory mode.

## OpenAI Setup

Set the API key:

```powershell
$env:OPENAI_API_KEY="your_openai_api_key"
```

If the key is missing or the internet is unavailable, the chatbot uses educational offline fallback answers.

## Run

```powershell
cd DermaVisionAI
mvn clean javafx:run
```

## Build

```powershell
mvn clean package
```
