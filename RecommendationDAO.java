package com.dermavisionai.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RecommendationDAO {
    public void save(int reportId, String recommendationText) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement("""
                     INSERT INTO recommendations(report_id, recommendation_text) VALUES (?, ?)
                     """)) {
            ps.setInt(1, reportId);
            ps.setString(2, recommendationText);
            ps.executeUpdate();
        } catch (SQLException ignored) {
            // ReportDAO memory fallback already preserves the recommendation for demo mode.
        }
    }
}
