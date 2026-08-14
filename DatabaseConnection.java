package com.dermavisionai.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {
    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DatabaseConfig.url(), DatabaseConfig.user(), DatabaseConfig.password());
    }
}
