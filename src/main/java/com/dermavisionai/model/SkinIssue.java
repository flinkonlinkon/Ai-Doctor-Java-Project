package com.dermavisionai.model;

/**
 * Single detected skin issue with severity and confidence values.
 */
public class SkinIssue {
    private String name;
    private int score;
    private double confidence;
    private Severity severity;

    public SkinIssue(String name, int score, double confidence, Severity severity) {
        this.name = name;
        this.score = score;
        this.confidence = confidence;
        this.severity = severity;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public double getConfidence() {
        return confidence;
    }

    public Severity getSeverity() {
        return severity;
    }
}
