package com.dermavisionai.controller;

import com.dermavisionai.model.Report;
import com.dermavisionai.model.User;
import com.dermavisionai.service.PdfReportService;
import com.dermavisionai.service.ReportService;
import com.dermavisionai.utils.AlertUtil;
import com.dermavisionai.utils.SessionManager;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class HistoryController {
    @FXML private TextField searchField;
    @FXML private TableView<Report> reportsTable;
    @FXML private TableColumn<Report, Number> idColumn;
    @FXML private TableColumn<Report, String> dateColumn;
    @FXML private TableColumn<Report, Number> scoreColumn;
    @FXML private TableColumn<Report, String> severityColumn;
    @FXML private TableColumn<Report, String> skinTypeColumn;
    @FXML private LineChart<String, Number> scoreChart;

    private final ReportService reportService = new ReportService();
    private final PdfReportService pdfReportService = new PdfReportService();
    private final ObservableList<Report> reports = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()));
        dateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCreatedAt().toLocalDate().toString()));
        scoreColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getAnalysisResult().getSkinScore()));
        severityColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAnalysisResult().getSeverity().name()));
        skinTypeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAnalysisResult().getSkinType()));
        loadReports();
    }

    @FXML
    private void searchReports() {
        String query = searchField.getText() == null ? "" : searchField.getText().toLowerCase();
        List<Report> filtered = reports.stream()
                .filter(report -> report.getAnalysisResult().getSkinType().toLowerCase().contains(query)
                        || report.getAnalysisResult().getSeverity().name().toLowerCase().contains(query)
                        || String.valueOf(report.getAnalysisResult().getSkinScore()).contains(query))
                .toList();
        reportsTable.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    private void deleteSelected() {
        Report selected = reportsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.error("No report selected", "Select a report to delete.");
            return;
        }
        reportService.deleteReport(selected.getId());
        loadReports();
    }

    @FXML
    private void exportCsv() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Export analysis history");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        chooser.setInitialFileName("dermavision-history.csv");
        File file = chooser.showSaveDialog(reportsTable.getScene().getWindow());
        if (file == null) {
            return;
        }
        try {
            reportService.exportCsv(new ArrayList<>(reportsTable.getItems()), file);
            AlertUtil.info("Export complete", "History exported successfully.");
        } catch (Exception ex) {
            AlertUtil.error("Export failed", ex.getMessage());
        }
    }

    @FXML
    private void exportPdf() {
        Report selected = reportsTable.getSelectionModel().getSelectedItem();
        User user = SessionManager.getCurrentUser();
        if (selected == null || user == null) {
            AlertUtil.error("No report selected", "Select a report to export.");
            return;
        }
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Export PDF report");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        chooser.setInitialFileName("dermavision-report-" + selected.getId() + ".pdf");
        File file = chooser.showSaveDialog(reportsTable.getScene().getWindow());
        if (file == null) {
            return;
        }
        try {
            pdfReportService.export(user, selected, file);
            AlertUtil.info("PDF created", "Report exported successfully.");
        } catch (Exception ex) {
            AlertUtil.error("PDF export failed", ex.getMessage());
        }
    }

    private void loadReports() {
        User user = SessionManager.getCurrentUser();
        if (user == null) {
            return;
        }
        reports.setAll(reportService.getReports(user.getId()));
        reportsTable.setItems(reports);
        renderChart();
    }

    private void renderChart() {
        scoreChart.getData().clear();
        XYChart.Series<String, Number> skinScore = new XYChart.Series<>();
        skinScore.setName("Skin Score");
        XYChart.Series<String, Number> acne = new XYChart.Series<>();
        acne.setName("Acne Progress");
        XYChart.Series<String, Number> pigmentation = new XYChart.Series<>();
        pigmentation.setName("Pigmentation Progress");
        for (Report report : reports) {
            String date = report.getCreatedAt().toLocalDate().toString();
            skinScore.getData().add(new XYChart.Data<>(date, report.getAnalysisResult().getSkinScore()));
            report.getAnalysisResult().getIssues().stream()
                    .filter(issue -> issue.getName().equals("Acne"))
                    .findFirst()
                    .ifPresent(issue -> acne.getData().add(new XYChart.Data<>(date, issue.getScore())));
            report.getAnalysisResult().getIssues().stream()
                    .filter(issue -> issue.getName().equals("Pigmentation"))
                    .findFirst()
                    .ifPresent(issue -> pigmentation.getData().add(new XYChart.Data<>(date, issue.getScore())));
        }
        scoreChart.getData().addAll(skinScore, acne, pigmentation);
    }
}
