package com.dermavisionai.service;

import com.dermavisionai.model.SkinAnalysisResult;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * REST client for OpenAI. Reads the API key from OPENAI_API_KEY.
 */
public class OpenAIService {
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String MODEL = "gpt-4o-mini";
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(12))
            .build();

    public String askSkincareQuestion(String question, SkinAnalysisResult latestAnalysis) {
        String apiKey = System.getenv("OPENAI_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            return offlineAnswer(question, latestAnalysis);
        }
        String analysisContext = latestAnalysis == null ? "No image analysis is available yet." : latestAnalysis.toSummaryText();
        String payload = """
                {
                  "model": "%s",
                  "messages": [
                    {"role":"system","content":"You are DermaVision AI, an educational skincare assistant. Do not provide medical diagnosis. Recommend dermatologist consultation for severe or persistent symptoms."},
                    {"role":"user","content":"Latest analysis context:\\n%s\\nQuestion: %s"}
                  ],
                  "temperature": 0.4
                }
                """.formatted(MODEL, escapeJson(analysisContext), escapeJson(question));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .timeout(Duration.ofSeconds(30))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                return "The AI service returned an error. Please check your API key and internet connection.";
            }
            return extractContent(response.body());
        } catch (IOException ex) {
            return "I could not reach the AI service. Please check your internet connection.";
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return "The AI request was interrupted.";
        }
    }

    private String offlineAnswer(String question, SkinAnalysisResult latestAnalysis) {
        String lower = question == null ? "" : question.toLowerCase();
        if (lower.contains("acne") || lower.contains("pimple")) {
            return "For acne-prone skin, use a gentle cleanser, non-comedogenic moisturizer, SPF, and consider salicylic acid or benzoyl peroxide slowly. See a dermatologist for painful or persistent acne.";
        }
        if (lower.contains("sunscreen")) {
            return "Choose broad-spectrum SPF 30 or higher. Lightweight gel formulas often suit oily skin, while cream formulas often suit dry skin.";
        }
        if (lower.contains("moisturizer")) {
            return "Look for ceramides, glycerin, hyaluronic acid, and niacinamide. Avoid heavy fragrance if your skin feels irritated.";
        }
        if (latestAnalysis != null) {
            return "Your latest mock analysis shows " + latestAnalysis.getSkinType() + " skin with a score of "
                    + latestAnalysis.getSkinScore() + "/100. Focus on sunscreen, gentle cleansing, and targeted treatments based on the highlighted issues.";
        }
        return "I can help with skincare education. Upload and analyze an image first if you want advice connected to your report.";
    }

    private String extractContent(String json) {
        String marker = "\"content\":";
        int start = json.indexOf(marker);
        if (start < 0) {
            return "The AI response could not be parsed.";
        }
        int quoteStart = json.indexOf('"', start + marker.length());
        int quoteEnd = quoteStart + 1;
        boolean escaped = false;
        while (quoteEnd < json.length()) {
            char ch = json.charAt(quoteEnd);
            if (ch == '"' && !escaped) {
                break;
            }
            escaped = ch == '\\' && !escaped;
            if (ch != '\\') {
                escaped = false;
            }
            quoteEnd++;
        }
        return unescapeJson(json.substring(quoteStart + 1, quoteEnd));
    }

    private String escapeJson(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }

    private String unescapeJson(String value) {
        return value.replace("\\n", "\n").replace("\\\"", "\"").replace("\\\\", "\\");
    }
}
