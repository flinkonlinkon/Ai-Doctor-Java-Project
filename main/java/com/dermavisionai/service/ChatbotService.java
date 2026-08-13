package com.dermavisionai.service;

import com.dermavisionai.database.ChatHistoryDAO;
import com.dermavisionai.model.ChatMessage;
import com.dermavisionai.model.SkinAnalysisResult;
import java.time.LocalDateTime;
import java.util.List;

public class ChatbotService {
    private final OpenAIService openAIService = new OpenAIService();
    private final ChatHistoryDAO chatHistoryDAO = new ChatHistoryDAO();

    public String ask(int userId, String question, SkinAnalysisResult latestAnalysis) {
        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException("Please type a skincare question.");
        }
        chatHistoryDAO.save(new ChatMessage(0, userId, "USER", question.trim(), LocalDateTime.now()));
        String answer = openAIService.askSkincareQuestion(question.trim(), latestAnalysis);
        chatHistoryDAO.save(new ChatMessage(0, userId, "AI", answer, LocalDateTime.now()));
        return answer;
    }

    public List<ChatMessage> history(int userId) {
        return chatHistoryDAO.findByUser(userId);
    }
}
