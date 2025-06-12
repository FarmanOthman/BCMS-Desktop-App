package com.bcms;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Parent root = loader.load();
          // Create a scene
        Scene scene = new Scene(root, 1200, 750);
        
        // Add the CSS file
        scene.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());
        
        // Set up and show the stage
        primaryStage.setTitle("Bestun Cars");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}