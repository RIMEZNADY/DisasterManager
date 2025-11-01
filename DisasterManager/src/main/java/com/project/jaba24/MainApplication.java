package com.project.jaba24;

import com.project.jaba24.business.Disaster;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class MainApplication extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
         showAuthPage();
    }

    public static void showAuthPage() throws IOException {
        Parent root = FXMLLoader.load(
                Objects.requireNonNull(MainApplication.class.getResource("auth.fxml"))
        );
        primaryStage.setTitle("Login Page - JABA24");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void showHomePage() throws IOException {
        Parent root = FXMLLoader.load(
                Objects.requireNonNull(MainApplication.class.getResource("dashboard.fxml"))
        );
        primaryStage.setTitle("Global Map - JABA24");
        Scene scene = new Scene(root, 1500, 800);
        scene.getStylesheets().add(Objects.requireNonNull(MainApplication.class.getResource("dashboard-style.css")).toExternalForm());
        primaryStage.setFullScreen(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void showUserProfilePage() throws IOException {
        Parent root = FXMLLoader.load(
                Objects.requireNonNull(MainApplication.class.getResource("UserProfile.fxml"))
        );

        Stage newStage = new Stage();
        newStage.setTitle("User Profile - JABA24");

        Scene scene = new Scene(root);
        newStage.setScene(scene);

        newStage.show();
    }

    public static void showAlertWindow() throws IOException {
        Parent root = FXMLLoader.load(
                Objects.requireNonNull(MainApplication.class.getResource("alert.fxml"))
        );

        Stage newStage = new Stage();
        newStage.setTitle("Alert - JABA24");
        newStage.setAlwaysOnTop(true);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(MainApplication.class.getResource("alert.css")).toExternalForm());
        newStage.setScene(scene);

        newStage.show();
    }
    public static void showGuidePage() throws IOException {
        Parent root = FXMLLoader.load(
                Objects.requireNonNull(MainApplication.class.getResource("Guides.fxml"))
        );
        Stage newStage = new Stage();
        newStage.setMaximized(true);
        Scene scene = new Scene(root, 500, 800);
        scene.getStylesheets().add(Objects.requireNonNull(MainApplication.class.getResource("Guides.css")).toExternalForm());
        newStage.setScene(scene);
        newStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
