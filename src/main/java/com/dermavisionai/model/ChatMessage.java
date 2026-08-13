package com.dermavisionai.model;

import java.time.LocalDateTime;

/**
 * Chat message exchanged between a user and the AI assistant.
 */
public class ChatMessage {
    private int id;
    private int userId;
    private String sender;
    private String message;
    private LocalDateTime createdAt;

    public ChatMessage(int id, int userId, String sender, String message, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.sender = sender;
        this.message = message;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
