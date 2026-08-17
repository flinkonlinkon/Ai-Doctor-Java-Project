package com.dermavisionai.utils;

import com.dermavisionai.model.Report;
import com.dermavisionai.model.SkinAnalysisResult;
import com.dermavisionai.model.User;
import java.io.File;

public final class SessionManager {
    private static User currentUser;
    private static File selectedImage;
    private static SkinAnalysisResult latestAnalysis;
    private static Report latestReport;

    private SessionManager() {
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        SessionManager.currentUser = currentUser;
    }

    public static File getSelectedImage() {
        return selectedImage;
    }

    public static void setSelectedImage(File selectedImage) {
        SessionManager.selectedImage = selectedImage;
    }

    public static SkinAnalysisResult getLatestAnalysis() {
        return latestAnalysis;
    }

    public static void setLatestAnalysis(SkinAnalysisResult latestAnalysis) {
        SessionManager.latestAnalysis = latestAnalysis;
    }

    public static Report getLatestReport() {
        return latestReport;
    }

    public static void setLatestReport(Report latestReport) {
        SessionManager.latestReport = latestReport;
    }

    public static void clear() {
        currentUser = null;
        selectedImage = null;
        latestAnalysis = null;
        latestReport = null;
    }
}
