package com.dermavisionai.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Creates tables when MySQL is reachable. The UI still works with memory fallback when it is not.
 */
public final class DatabaseInitializer {
    private DatabaseInitializer() {
    }

    public static void initialize() {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS users (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        full_name VARCHAR(120) NOT NULL,
                        email VARCHAR(160) NOT NULL UNIQUE,
                        password_hash VARCHAR(255) NOT NULL,
                        skin_type VARCHAR(60),
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                    )
                    """);
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS reports (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        user_id INT NOT NULL,
                        image_path VARCHAR(600),
                        skin_score INT,
                        confidence DOUBLE,
                        severity VARCHAR(20),
                        skin_type VARCHAR(60),
                        issues_text TEXT,
                        recommendations_text TEXT,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
                    )
                    """);
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS chat_history (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        user_id INT NOT NULL,
                        sender VARCHAR(20) NOT NULL,
                        message TEXT NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
                    )
                    """);
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS recommendations (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        report_id INT NOT NULL,
                        recommendation_text TEXT NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (report_id) REFERENCES reports(id) ON DELETE CASCADE
                    )
                    """);
        } catch (SQLException ignored) {
            // Demo mode is intentionally available when MySQL is not configured.
        }
    }
}
