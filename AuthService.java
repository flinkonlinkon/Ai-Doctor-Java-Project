package com.dermavisionai.service;

import com.dermavisionai.database.UserDAO;
import com.dermavisionai.model.User;
import com.dermavisionai.utils.PasswordHasher;
import com.dermavisionai.utils.ValidationUtil;
import java.time.LocalDateTime;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();

    public User register(String fullName, String email, String password) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name is required.");
        }
        if (!ValidationUtil.isValidEmail(email)) {
            throw new IllegalArgumentException("Please enter a valid email address.");
        }
        if (!ValidationUtil.isStrongPassword(password)) {
            throw new IllegalArgumentException("Password must contain at least 8 characters.");
        }
        if (userDAO.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("An account with this email already exists.");
        }
        User user = new User(0, fullName.trim(), email.trim().toLowerCase(),
                PasswordHasher.hashPassword(password), "Unknown", LocalDateTime.now());
        return userDAO.save(user);
    }

    public User login(String email, String password) {
        if (!ValidationUtil.isValidEmail(email) || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Email and password are required.");
        }
        User user = userDAO.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        if (!PasswordHasher.verifyPassword(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password.");
        }
        return user;
    }

    public void resetPassword(String email, String newPassword) {
        if (!ValidationUtil.isValidEmail(email)) {
            throw new IllegalArgumentException("Please enter a valid email address.");
        }
        if (!ValidationUtil.isStrongPassword(newPassword)) {
            throw new IllegalArgumentException("Password must contain at least 8 characters.");
        }
        userDAO.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("No account was found for this email."));
        userDAO.updatePassword(email.trim().toLowerCase(), PasswordHasher.hashPassword(newPassword));
    }
}
