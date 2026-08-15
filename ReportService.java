package com.dermavisionai.service;

import com.dermavisionai.database.RecommendationDAO;
import com.dermavisionai.database.ReportDAO;
import com.dermavisionai.model.Recommendation;
import com.dermavisionai.model.Report;
import com.dermavisionai.model.SkinAnalysisResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;

public class ReportService {
    private final ReportDAO reportDAO = new ReportDAO();
    private final RecommendationDAO recommendationDAO = new RecommendationDAO();

    public Report saveReport(int userId, File image, SkinAnalysisResult result, Recommendation recommendation) {
        Report report = new Report(0, userId, image.getAbsolutePath(), result, recommendation, LocalDateTime.now());
        Report saved = reportDAO.save(report);
        recommendationDAO.save(saved.getId(), recommendation.toReadableText());
        return saved;
    }

    public List<Report> getReports(int userId) {
        return reportDAO.findByUser(userId);
    }

    public void deleteReport(int reportId) {
        reportDAO.delete(reportId);
    }

    public void exportCsv(List<Report> reports, File file) throws IOException {
        StringBuilder builder = new StringBuilder("ID,Date,Skin Score,Confidence,Severity,Skin Type,Image\n");
        for (Report report : reports) {
            builder.append(report.getId()).append(',')
                    .append(report.getCreatedAt()).append(',')
                    .append(report.getAnalysisResult().getSkinScore()).append(',')
                    .append(String.format("%.1f", report.getAnalysisResult().getConfidence())).append(',')
                    .append(report.getAnalysisResult().getSeverity()).append(',')
                    .append(report.getAnalysisResult().getSkinType()).append(',')
                    .append('"').append(report.getImagePath().replace("\"", "\"\"")).append('"')
                    .append('\n');
        }
        Files.writeString(file.toPath(), builder.toString());
    }
}
