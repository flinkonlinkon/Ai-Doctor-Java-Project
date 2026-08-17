package com.dermavisionai.controller;

import com.dermavisionai.model.User;
import com.dermavisionai.service.AuthService;
import com.dermavisionai.utils.AlertUtil;
import com.dermavisionai.utils.SessionManager;
import com.dermavisionai.utils.ViewLoader;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    private final AuthService authService = new AuthService();

    @FXML
    private void login() {
        try {
            User user = authService.login(emailField.getText(), passwordField.getText());
            SessionManager.setCurrentUser(user);
            switchRoot(emailField, "/fxml/dashboard.fxml");
        } catch (RuntimeException ex) {
            AlertUtil.error("Login failed", ex.getMessage());
        }
    }

    @FXML
    private void showRegister() {
        switchRoot(emailField, "/fxml/register.fxml");
    }

    @FXML
    private void showForgotPassword() {
        switchRoot(emailField, "/fxml/forgot_password.fxml");
    }

    private void switchRoot(Node node, String fxml) {
        node.getScene().setRoot(ViewLoader.load(fxml));
    }
}
