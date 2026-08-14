package com.dermavisionai.controller;

import com.dermavisionai.model.Recommendation;
import com.dermavisionai.model.Report;
import com.dermavisionai.model.User;
import com.dermavisionai.service.ReportService;
import com.dermavisionai.utils.SessionManager;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class RecommendationsController {
    @FXML private VBox morningBox;
    @FXML private VBox nightBox;
    @FXML private VBox ingredientsBox;
    @FXML private VBox lifestyleBox;
    @FXML private Label emptyLabel;
    private final ReportService reportService = new ReportService();

    @FXML
    private void initialize() {
        Report report = latestReport();
        if (report == null) {
            emptyLabel.setText("Run an analysis to generate personalized recommendations.");
            return;
        }
        emptyLabel.setText("");
        Recommendation recommendation = report.getRecommendation();
        renderList(morningBox, recommendation.getMorningRoutine());
        renderList(nightBox, recommendation.getNightRoutine());
        renderList(ingredientsBox, recommendation.getSuggestedIngredients());
        renderList(lifestyleBox, recommendation.getDailyTips());
        lifestyleBox.getChildren().addAll(
                line("Water: " + recommendation.getWaterIntake()),
                line("Sleep: " + recommendation.getSleepHours()),
                line("Sun: " + recommendation.getSunProtectionAdvice())
        );
    }

    private void renderList(VBox box, List<String> items) {
        box.getChildren().clear();
        for (int i = 0; i < items.size(); i++) {
            box.getChildren().add(line((i + 1) + ". " + items.get(i)));
        }
    }

    private Label line(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.getStyleClass().add("recommendation-line");
        return label;
    }

    private Report latestReport() {
        Report report = SessionManager.getLatestReport();
        if (report != null) {
            return report;
        }
        User user = SessionManager.getCurrentUser();
        if (user == null) {
            return null;
        }
        List<Report> reports = reportService.getReports(user.getId());
        return reports.isEmpty() ? null : reports.getFirst();
    }
}
