package com.project.jaba24.controllers;
import com.project.jaba24.DAO.ImpUser;
import com.project.jaba24.MainApplication;
import java.util.prefs.Preferences;

import com.project.jaba24.business.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class authController {
    @FXML
    private TextField emailField;
    @FXML
    private Label emailFailedLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private Label usernameFailedLabel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label passwordFailedLabel;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label confirmPasswordFailedLabel;
    @FXML
    private TextField locationField;
    @FXML
    private Label locationFailedLabel;
    @FXML
    private Button authButton;
    @FXML
    private Button toggleAuthButton;
    private boolean isLogin = true;
    private final ImpUser userService = new ImpUser();
    private static final Preferences preferences = Preferences.userNodeForPackage(MainApplication.class);
    @FXML
    private void handleAuth(){
        if(isLogin){
            handleLogin();
        } else {
            handleRegister();
        }
    }
    @FXML
    private void handleToggleAuth(){
        isLogin = !isLogin;
        if(isLogin){
            usernameField.setVisible(false);
            usernameField.setManaged(false);
            confirmPasswordField.setVisible(false);
            confirmPasswordField.setManaged(false);
            locationField.setVisible(false);
            locationField.setManaged(false);
            authButton.setText("Login");
            toggleAuthButton.setText("Don't have an account? Register");
        } else {
            usernameField.setVisible(true);
            usernameField.setManaged(true);
            confirmPasswordField.setVisible(true);
            confirmPasswordField.setManaged(true);
            locationField.setVisible(true);
            locationField.setManaged(true);
            authButton.setText("Register");
            toggleAuthButton.setText("Already have an account? Login");
        }
    }

    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();
        clearErrorLabels();

        if (!email.isEmpty() && !password.isEmpty()) {
            setButtonLoading(authButton, true);
            try {
                if (userService.LoginUser(email, password)) {
                    preferences.put("email", email);
                    MainApplication.showHomePage();
                } else {
                    showConfirmationDialog("Login Failed", "Invalid email or password", Alert.AlertType.ERROR);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                setButtonLoading(authButton, false);
            }
        }
        if (email.isEmpty()) {
            showLabel(usernameFailedLabel, "Username is required");
        } else {
            hideLabel(usernameFailedLabel);
        }
        if (password.isEmpty()) {
            showLabel(passwordFailedLabel, "Password is required");
        } else {
            hideLabel(passwordFailedLabel);
        }
    }

    private void handleRegister() {
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String location = locationField.getText();
        clearErrorLabels();

        if (email.isEmpty()) {
            showLabel(emailFailedLabel, "Email is required");
        } else {
            hideLabel(emailFailedLabel);
        }
        if (username.isEmpty()) {
            showLabel(usernameFailedLabel, "Username is required");
        } else {
            hideLabel(usernameFailedLabel);
        }
        if (password.isEmpty()) {
            showLabel(passwordFailedLabel, "Password is required");
        } else {
            hideLabel(passwordFailedLabel);
        }
        if (confirmPassword.isEmpty()) {
            showLabel(confirmPasswordFailedLabel, "Confirm Password is required");
        } else {
            hideLabel(confirmPasswordFailedLabel);
        }
        if (!password.equals(confirmPassword)) {
            showLabel(confirmPasswordFailedLabel, "Password and Confirm Password must be the same");
        } else {
            hideLabel(confirmPasswordFailedLabel);
        }
        if (location.isEmpty()) {
            showLabel(locationFailedLabel, "Location is required");
        } else {
            hideLabel(locationFailedLabel);
        }

        if (!email.isEmpty() && !username.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()
                && password.equals(confirmPassword) && !location.isEmpty()) {
            try {
                setButtonLoading(authButton, true);
                User existingUser = userService.getUserByEmail(email);
                if (existingUser != null) {
                    showLabel(emailFailedLabel, "Email is already taken");
                } else {
                    if (userService.insertUser(new User(username, email, password, location))) {
                        showConfirmationDialog(
                                "Registration Successful",
                                "Your account has been created successfully!"
                                , Alert.AlertType.INFORMATION
                                );
                        preferences.put("email", email);
                        MainApplication.showHomePage();
                    } else {
                        showLabel(emailFailedLabel, "Something went wrong! Please try again.");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                setButtonLoading(authButton, false);
            }
        }
    }


    private void hideLabel(Label label){
        label.setVisible(false);
        label.setManaged(false);
    }

    private void showLabel(Label label, String message){
        label.setVisible(true);
        label.setManaged(true);
        label.setText(message);
    }
    private void clearErrorLabels() {
        hideLabel(usernameFailedLabel);
        hideLabel(emailFailedLabel);
        hideLabel(passwordFailedLabel);
        hideLabel(confirmPasswordFailedLabel);
    }

    private void setButtonLoading (Button button, boolean isLoading) {
        if (isLoading) {
            button.setText("Loading...");
            button.setDisable(true);
        } else {
            button.setText("Login");
            button.setDisable(false);
        }
    }
    private void showConfirmationDialog(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}

