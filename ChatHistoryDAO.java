package com.dermavisionai.database;

import com.dermavisionai.model.ChatMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatHistoryDAO {
    private static final List<ChatMessage> MEMORY_MESSAGES = new CopyOnWriteArrayList<>();
    private static final AtomicInteger MEMORY_IDS = new AtomicInteger(1);

    public ChatMessage save(ChatMessage message) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement("""
                     INSERT INTO chat_history(user_id, sender, message) VALUES (?, ?, ?)
                     """, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, message.getUserId());
            ps.setString(2, message.getSender());
            ps.setString(3, message.getMessage());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return new ChatMessage(keys.getInt(1), message.getUserId(), message.getSender(),
                            message.getMessage(), LocalDateTime.now());
                }
            }
        } catch (SQLException ignored) {
            ChatMessage saved = new ChatMessage(MEMORY_IDS.getAndIncrement(), message.getUserId(),
                    message.getSender(), message.getMessage(), LocalDateTime.now());
            MEMORY_MESSAGES.add(saved);
            return saved;
        }
        return message;
    }

    public List<ChatMessage> findByUser(int userId) {
        List<ChatMessage> messages = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement("""
                     SELECT * FROM chat_history WHERE user_id = ? ORDER BY created_at ASC
                     """)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Timestamp created = rs.getTimestamp("created_at");
                    messages.add(new ChatMessage(rs.getInt("id"), rs.getInt("user_id"),
                            rs.getString("sender"), rs.getString("message"),
                            created == null ? LocalDateTime.now() : created.toLocalDateTime()));
                }
            }
            return messages;
        } catch (SQLException ignored) {
            return MEMORY_MESSAGES.stream()
                    .filter(message -> message.getUserId() == userId)
                    .toList();
        }
    }
}
