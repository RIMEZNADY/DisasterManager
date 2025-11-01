package com.project.jaba24.controllers;

import com.project.jaba24.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;
import com.project.jaba24.DAO.ImpUser;
import com.project.jaba24.business.User;
import java.util.prefs.Preferences;


public class UserProfileController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField oldPasswordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField locationField;

    private User user;
    private ImpUser impUser;
    private static final Preferences preferences = Preferences.userNodeForPackage(MainApplication.class);

    @FXML
    public void initialize() {
        // Initialize the user profile page with the user's data from the database
        impUser = new ImpUser();
        String email = preferences.get("email", "");
        user = impUser.getUserByEmail(email);
        loadUserData();
    }

    private void loadUserData() {
        usernameField.setText(user.getUsername());
        emailField.setText(user.getEmail());
        locationField.setText(user.getLocation());
    }

    @FXML
    public void updateProfile() {
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!impUser.checkPassword(oldPassword, user.getPassword())) {
            showAlert("Error", "Old password is incorrect.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert("Error", "New password and confirmation do not match.");
            return;
        }

        user.setUsername(usernameField.getText());
        user.setEmail(emailField.getText());

        if (!newPassword.isEmpty()) {
            user.setPassword(newPassword);
        }

        user.setLocation(locationField.getText());

        if (impUser.updateUser(user)) {
            showAlert("Success", "User profile updated successfully.");
        } else {
            showAlert("Error", "Failed to update user profile.");
        }
    }

    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void goToHomePage(ActionEvent event) throws IOException {
        MainApplication.showHomePage();
    }
}
