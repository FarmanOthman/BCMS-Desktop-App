package com.bcms;

import javafx.application.Application;
import javafx.fxml.FXMLLoader; // Added import
import javafx.scene.Parent; // Added import
import javafx.scene.Scene; // Added import
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Placeholder for FXML loading, e.g., login screen
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/LoginView.fxml")); // Load FXML
        primaryStage.setTitle("BCMS Desktop - Login"); // Set title
        primaryStage.setScene(new Scene(root)); // Create and set scene
        primaryStage.show(); // Show the stage
        // System.out.println("JavaFX Application Started"); // Commented out or removed
    }

    public static void main(String[] args) {
        launch(args);
    }
}
