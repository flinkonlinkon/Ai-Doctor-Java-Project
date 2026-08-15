# DermaVision AI Project Report

## Overview

DermaVision AI is a JavaFX desktop application for educational skincare assistance. Users can register, log in, upload a facial image, run a mock skin analysis, view recommendations, chat with an AI skincare assistant, and export saved reports.

## Scope

The application does not claim medical-grade diagnosis. The current analyzer is a deterministic mock implementation behind the `SkinAnalyzer` interface. This keeps the GUI, database, report generation, and AI-chat architecture complete while allowing future replacement with TensorFlow, PyTorch, OpenCV, or an external computer-vision API.

## Architecture

The project follows MVC:

- `controller`: JavaFX controllers for screens and user actions.
- `model`: Plain domain objects such as `User`, `Report`, `SkinAnalysisResult`, and `Recommendation`.
- `service`: Business logic for authentication, analysis, recommendations, OpenAI chat, reports, and PDF export.
- `database`: JDBC connection, table initializer, and DAO classes.
- `utils`: validation, password hashing, session, alerts, and FXML loading.

## Main Features

- Authentication with PBKDF2 password hashing.
- MySQL persistence through JDBC with a demo in-memory fallback.
- Image upload using file chooser and drag/drop.
- Mock analysis for acne, pimples, wrinkles, dark spots, pigmentation, redness, eye bags, oiliness, dryness, and skin type.
- Recommendation engine for morning routine, night routine, ingredients, lifestyle tips, water, sleep, and sun protection.
- OpenAI chatbot using `OPENAI_API_KEY` without hardcoding secrets.
- Previous report table with search, delete, CSV export, PDF export, and JavaFX line charts.
- Modern JavaFX UI with FXML and CSS.

## Future Work

- Replace `MockSkinAnalyzer` with a trained model implementation.
- Store issue-level scores in normalized database tables.
- Add dermatologist-reviewed educational content.
- Add email-based password reset tokens.
- Add unit tests and integration tests with Testcontainers MySQL.
