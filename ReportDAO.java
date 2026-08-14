package com.dermavisionai.database;

import com.dermavisionai.model.Recommendation;
import com.dermavisionai.model.Report;
import com.dermavisionai.model.Severity;
import com.dermavisionai.model.SkinAnalysisResult;
import com.dermavisionai.model.SkinIssue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ReportDAO {
    private static final List<Report> MEMORY_REPORTS = new CopyOnWriteArrayList<>();
    private static final AtomicInteger MEMORY_IDS = new AtomicInteger(1);

    public Report save(Report report) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement("""
                     INSERT INTO reports(user_id, image_path, skin_score, confidence, severity,
                                         skin_type, issues_text, recommendations_text)
                     VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                     """, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, report.getUserId());
            ps.setString(2, report.getImagePath());
            ps.setInt(3, report.getAnalysisResult().getSkinScore());
            ps.setDouble(4, report.getAnalysisResult().getConfidence());
            ps.setString(5, report.getAnalysisResult().getSeverity().name());
            ps.setString(6, report.getAnalysisResult().getSkinType());
            ps.setString(7, report.getAnalysisResult().toSummaryText());
            ps.setString(8, report.getRecommendation().toReadableText());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return new Report(keys.getInt(1), report.getUserId(), report.getImagePath(),
                            report.getAnalysisResult(), report.getRecommendation(), LocalDateTime.now());
                }
            }
        } catch (SQLException ignored) {
            Report saved = new Report(MEMORY_IDS.getAndIncrement(), report.getUserId(), report.getImagePath(),
                    report.getAnalysisResult(), report.getRecommendation(), LocalDateTime.now());
            MEMORY_REPORTS.add(saved);
            return saved;
        }
        return report;
    }

    public List<Report> findByUser(int userId) {
        List<Report> reports = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement("""
                     SELECT * FROM reports WHERE user_id = ? ORDER BY created_at DESC
                     """)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reports.add(mapReport(rs));
                }
            }
            return reports;
        } catch (SQLException ignored) {
            return MEMORY_REPORTS.stream()
                    .filter(report -> report.getUserId() == userId)
                    .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                    .toList();
        }
    }

    public void delete(int reportId) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM reports WHERE id = ?")) {
            ps.setInt(1, reportId);
            ps.executeUpdate();
        } catch (SQLException ignored) {
            MEMORY_REPORTS.removeIf(report -> report.getId() == reportId);
        }
    }

    private Report mapReport(ResultSet rs) throws SQLException {
        SkinAnalysisResult result = new SkinAnalysisResult(
                rs.getInt("skin_score"),
                rs.getDouble("confidence"),
                Severity.valueOf(rs.getString("severity")),
                rs.getString("skin_type")
        );
        restoreIssues(result, rs.getString("issues_text"));
        Recommendation recommendation = new Recommendation();
        recommendation.getDailyTips().add(rs.getString("recommendations_text"));
        Timestamp created = rs.getTimestamp("created_at");
        return new Report(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getString("image_path"),
                result,
                recommendation,
                created == null ? LocalDateTime.now() : created.toLocalDateTime()
        );
    }

    private void restoreIssues(SkinAnalysisResult result, String issuesText) {
        if (issuesText == null || issuesText.isBlank()) {
            return;
        }
        for (String line : issuesText.split("\\R")) {
            if (!line.startsWith("- ")) {
                continue;
            }
            try {
                int nameEnd = line.indexOf(':');
                int severityStart = nameEnd + 2;
                int severityEnd = line.indexOf(' ', severityStart);
                int scoreStart = line.indexOf('(', severityEnd) + 1;
                int scoreEnd = line.indexOf('%', scoreStart);
                String name = line.substring(2, nameEnd);
                Severity severity = Severity.valueOf(line.substring(severityStart, severityEnd));
                int score = Integer.parseInt(line.substring(scoreStart, scoreEnd));
                result.addIssue(new SkinIssue(name, score, 80.0, severity));
            } catch (RuntimeException ignored) {
                // Keep loading the report even if a legacy issue line has a different format.
            }
        }
    }
}
