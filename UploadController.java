package com.dermavisionai.controller;

import com.dermavisionai.model.Recommendation;
import com.dermavisionai.model.Report;
import com.dermavisionai.model.SkinAnalysisResult;
import com.dermavisionai.model.SkinIssue;
import com.dermavisionai.service.MockSkinAnalyzer;
import com.dermavisionai.service.RecommendationEngine;
import com.dermavisionai.service.ReportService;
import com.dermavisionai.service.SkinAnalyzer;
import com.dermavisionai.utils.AlertUtil;
import com.dermavisionai.utils.SessionManager;
import com.dermavisionai.utils.ValidationUtil;
import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class UploadController {
    @FXML private VBox dropZone;
    @FXML private ImageView previewImage;
    @FXML private Label imageNameLabel;
    @FXML private Label scoreLabel;
    @FXML private Label confidenceLabel;
    @FXML private Label severityLabel;
    @FXML private Label skinTypeLabel;
    @FXML private ProgressBar scoreProgress;
    @FXML private FlowPane issuesPane;
    @FXML private VBox recommendationsBox;

    private final SkinAnalyzer skinAnalyzer = new MockSkinAnalyzer();
    private final RecommendationEngine recommendationEngine = new RecommendationEngine();
    private final ReportService reportService = new ReportService();
    private File selectedImage;

    @FXML
    private void initialize() {
        configureDragAndDrop();
        selectedImage = SessionManager.getSelectedImage();
        if (selectedImage != null) {
            showPreview(selectedImage);
        }
    }

    @FXML
    private void browseImage() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose facial image");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.png"));
        File file = chooser.showOpenDialog(dropZone.getScene().getWindow());
        if (file != null) {
            chooseImage(file);
        }
    }

    @FXML
    private void analyzeImage() {
        try {
            ValidationUtil.validateImage(selectedImage);
            SkinAnalysisResult result = skinAnalyzer.analyzeSkin(selectedImage);
            Recommendation recommendation = recommendationEngine.generate(result);
            int userId = SessionManager.getCurrentUser() == null ? 0 : SessionManager.getCurrentUser().getId();
            Report report = reportService.saveReport(userId, selectedImage, result, recommendation);
            SessionManager.setLatestAnalysis(result);
            SessionManager.setLatestReport(report);
            renderResult(result, recommendation);
        } catch (RuntimeException ex) {
            AlertUtil.error("Analysis failed", ex.getMessage());
        }
    }

    private void configureDragAndDrop() {
        dropZone.setOnDragOver(event -> {
            Dragboard board = event.getDragboard();
            if (board.hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });
        dropZone.setOnDragDropped(event -> {
            Dragboard board = event.getDragboard();
            if (board.hasFiles()) {
                chooseImage(board.getFiles().getFirst());
                event.setDropCompleted(true);
            }
            event.consume();
        });
    }

    private void chooseImage(File file) {
        try {
            ValidationUtil.validateImage(file);
            selectedImage = file;
            SessionManager.setSelectedImage(file);
            showPreview(file);
        } catch (RuntimeException ex) {
            AlertUtil.error("Invalid image", ex.getMessage());
        }
    }

    private void showPreview(File file) {
        previewImage.setImage(new Image(file.toURI().toString(), 360, 260, true, true));
        imageNameLabel.setText(file.getName());
    }

    private void renderResult(SkinAnalysisResult result, Recommendation recommendation) {
        scoreLabel.setText(result.getSkinScore() + "/100");
        confidenceLabel.setText(String.format("%.1f%%", result.getConfidence()));
        severityLabel.setText(result.getSeverity().name());
        skinTypeLabel.setText(result.getSkinType());
        scoreProgress.setProgress(result.getSkinScore() / 100.0);
        issuesPane.getChildren().clear();
        for (SkinIssue issue : result.getIssues()) {
            issuesPane.getChildren().add(issueCard(issue));
        }
        recommendationsBox.getChildren().setAll(
                section("Morning Routine", recommendation.getMorningRoutine()),
                section("Night Routine", recommendation.getNightRoutine()),
                section("Suggested Ingredients", recommendation.getSuggestedIngredients()),
                section("Avoid Ingredients", recommendation.getAvoidIngredients()),
                section("Daily Tips", recommendation.getDailyTips()),
                textLine("Water Intake", recommendation.getWaterIntake()),
                textLine("Sleep", recommendation.getSleepHours()),
                textLine("Sun Protection", recommendation.getSunProtectionAdvice())
        );
    }

    private VBox issueCard(SkinIssue issue) {
        Label title = new Label(issue.getName());
        title.getStyleClass().add("card-title");
        Label severity = new Label(issue.getSeverity().name() + " severity");
        severity.getStyleClass().add("muted-label");
        ProgressBar bar = new ProgressBar(issue.getScore() / 100.0);
        bar.setPrefWidth(180);
        Label confidence = new Label(String.format("Confidence %.1f%%", issue.getConfidence()));
        confidence.getStyleClass().add("muted-label");
        VBox box = new VBox(8, title, severity, bar, confidence);
        box.getStyleClass().add("mini-card");
        return box;
    }

    private VBox section(String title, java.util.List<String> items) {
        VBox box = new VBox(6);
        Label heading = new Label(title);
        heading.getStyleClass().add("card-title");
        box.getChildren().add(heading);
        for (String item : items) {
            Label label = new Label("- " + item);
            label.setWrapText(true);
            box.getChildren().add(label);
        }
        box.getStyleClass().add("recommendation-section");
        return box;
    }

    private VBox textLine(String title, String value) {
        return section(title, java.util.List.of(value));
    }
}
