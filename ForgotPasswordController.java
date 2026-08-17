package com.dermavisionai.controller;

import com.dermavisionai.service.AuthService;
import com.dermavisionai.utils.AlertUtil;
import com.dermavisionai.utils.ViewLoader;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ForgotPasswordController {
    @FXML private TextField emailField;
    @FXML private PasswordField newPasswordField;
    private final AuthService authService = new AuthService();

    @FXML
    private void resetPassword() {
        try {
            authService.resetPassword(emailField.getText(), newPasswordField.getText());
            AlertUtil.info("Password updated", "Your password has been reset.");
            showLogin();
        } catch (RuntimeException ex) {
            AlertUtil.error("Reset failed", ex.getMessage());
        }
    }

    @FXML
    private void showLogin() {
        ((Node) emailField).getScene().setRoot(ViewLoader.load("/fxml/login.fxml"));
    }
}
