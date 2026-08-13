package com.dermavisionai.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Aggregate result returned by SkinAnalyzer implementations.
 */
public class SkinAnalysisResult {
    private int skinScore;
    private double confidence;
    private Severity severity;
    private String skinType;
    private LocalDateTime analyzedAt;
    private final List<SkinIssue> issues = new ArrayList<>();

    public SkinAnalysisResult(int skinScore, double confidence, Severity severity, String skinType) {
        this.skinScore = skinScore;
        this.confidence = confidence;
        this.severity = severity;
        this.skinType = skinType;
        this.analyzedAt = LocalDateTime.now();
    }

    public int getSkinScore() {
        return skinScore;
    }

    public double getConfidence() {
        return confidence;
    }

    public Severity getSeverity() {
        return severity;
    }

    public String getSkinType() {
        return skinType;
    }

    public LocalDateTime getAnalyzedAt() {
        return analyzedAt;
    }

    public List<SkinIssue> getIssues() {
        return issues;
    }

    public void addIssue(SkinIssue issue) {
        issues.add(issue);
    }

    public String toSummaryText() {
        StringBuilder builder = new StringBuilder();
        builder.append("Skin Score: ").append(skinScore).append("/100\n");
        builder.append("Confidence: ").append(String.format("%.1f%%", confidence)).append("\n");
        builder.append("Severity: ").append(severity).append("\n");
        builder.append("Skin Type: ").append(skinType).append("\n");
        for (SkinIssue issue : issues) {
            builder.append("- ").append(issue.getName()).append(": ")
                    .append(issue.getSeverity()).append(" (")
                    .append(issue.getScore()).append("%)\n");
        }
        return builder.toString();
    }
}
