package com.dermavisionai.model;

import java.time.LocalDateTime;

/**
 * Saved analysis report for history and PDF export.
 */
public class Report {
    private int id;
    private int userId;
    private String imagePath;
    private SkinAnalysisResult analysisResult;
    private Recommendation recommendation;
    private LocalDateTime createdAt;

    public Report(int id, int userId, String imagePath, SkinAnalysisResult analysisResult,
                  Recommendation recommendation, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.imagePath = imagePath;
        this.analysisResult = analysisResult;
        this.recommendation = recommendation;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public SkinAnalysisResult getAnalysisResult() {
        return analysisResult;
    }

    public Recommendation getRecommendation() {
        return recommendation;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
