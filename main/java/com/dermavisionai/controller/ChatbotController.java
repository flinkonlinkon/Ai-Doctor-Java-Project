package com.dermavisionai.controller;

import com.dermavisionai.model.ChatMessage;
import com.dermavisionai.model.User;
import com.dermavisionai.service.ChatbotService;
import com.dermavisionai.utils.AlertUtil;
import com.dermavisionai.utils.SessionManager;
import java.time.format.DateTimeFormatter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatbotController {
    @FXML private TextArea chatArea;
    @FXML private TextField questionField;
    private final ChatbotService chatbotService = new ChatbotService();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    private void initialize() {
        User user = SessionManager.getCurrentUser();
        if (user != null) {
            for (ChatMessage message : chatbotService.history(user.getId())) {
                append(message.getSender(), message.getMessage(), message.getCreatedAt().format(formatter));
            }
        }
    }

    @FXML
    private void sendQuestion() {
        try {
            User user = SessionManager.getCurrentUser();
            int userId = user == null ? 0 : user.getId();
            String question = questionField.getText();
            append("USER", question, "now");
            questionField.clear();
            String answer = chatbotService.ask(userId, question, SessionManager.getLatestAnalysis());
            append("AI", answer, "now");
        } catch (RuntimeException ex) {
            AlertUtil.error("Chatbot error", ex.getMessage());
        }
    }

    @FXML
    private void askSuggested(ActionEvent event) {
        if (event.getSource() instanceof Button button) {
            questionField.setText(button.getText());
            sendQuestion();
        }
    }

    private void append(String sender, String message, String time) {
        chatArea.appendText("[%s] %s: %s%n%n".formatted(time, sender, message));
    }
}
