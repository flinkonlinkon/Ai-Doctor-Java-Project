package com.dermavisionai.controller;

import com.dermavisionai.service.AuthService;
import com.dermavisionai.utils.AlertUtil;
import com.dermavisionai.utils.ViewLoader;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    private final AuthService authService = new AuthService();

    @FXML
    private void register() {
        try {
            if (!passwordField.getText().equals(confirmPasswordField.getText())) {
                throw new IllegalArgumentException("Passwords do not match.");
            }
            authService.register(nameField.getText(), emailField.getText(), passwordField.getText());
            AlertUtil.info("Account created", "You can now log in with your new account.");
            showLogin();
        } catch (RuntimeException ex) {
            AlertUtil.error("Registration failed", ex.getMessage());
        }
    }

    @FXML
    private void showLogin() {
        switchRoot(emailField, "/fxml/login.fxml");
    }

    private void switchRoot(Node node, String fxml) {
        node.getScene().setRoot(ViewLoader.load(fxml));
    }
}
