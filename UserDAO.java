package com.dermavisionai.database;

import com.dermavisionai.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class UserDAO {
    private static final Map<String, User> MEMORY_USERS = new ConcurrentHashMap<>();
    private static final AtomicInteger MEMORY_IDS = new AtomicInteger(1);

    public User save(User user) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement("""
                     INSERT INTO users(full_name, email, password_hash, skin_type)
                     VALUES (?, ?, ?, ?)
                     """, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getSkinType());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    user.setId(keys.getInt(1));
                }
            }
            user.setCreatedAt(LocalDateTime.now());
            return user;
        } catch (SQLException ex) {
            user.setId(MEMORY_IDS.getAndIncrement());
            user.setCreatedAt(LocalDateTime.now());
            MEMORY_USERS.put(user.getEmail().toLowerCase(), user);
            return user;
        }
    }

    public Optional<User> findByEmail(String email) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE email = ?")) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapUser(rs));
                }
            }
        } catch (SQLException ignored) {
            return Optional.ofNullable(MEMORY_USERS.get(email.toLowerCase()));
        }
        return Optional.empty();
    }

    public void updatePassword(String email, String passwordHash) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement("UPDATE users SET password_hash = ? WHERE email = ?")) {
            ps.setString(1, passwordHash);
            ps.setString(2, email);
            ps.executeUpdate();
        } catch (SQLException ignored) {
            User user = MEMORY_USERS.get(email.toLowerCase());
            if (user != null) {
                user.setPasswordHash(passwordHash);
            }
        }
    }

    private User mapUser(ResultSet rs) throws SQLException {
        Timestamp created = rs.getTimestamp("created_at");
        return new User(
                rs.getInt("id"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("password_hash"),
                rs.getString("skin_type"),
                created == null ? LocalDateTime.now() : created.toLocalDateTime()
        );
    }
}
