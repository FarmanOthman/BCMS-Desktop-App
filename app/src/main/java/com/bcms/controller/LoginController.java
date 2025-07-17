package com.bcms.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.net.URL;

public class LoginController {
    
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox rememberMeCheckbox;
    @FXML private Hyperlink forgotPasswordLink;
    @FXML private Button signInButton;
    
    @FXML
    public void initialize() {
        // Set up event handlers
        signInButton.setOnAction(this::handleSignIn);
        
        // Optional: Add enter key support for password field
        passwordField.setOnAction(this::handleSignIn);
    }
    
    @FXML
    private void handleSignIn(ActionEvent event) {
        try {
            // Close the login window
            Stage loginStage = (Stage) signInButton.getScene().getWindow();
            loginStage.close();
            
            // Open the dashboard
            openDashboard();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void openDashboard() throws IOException {
        // Create a new stage for dashboard since we're not navigating from a button
        Stage dashboardStage = new Stage();
        dashboardStage.setMinWidth(1200);
        dashboardStage.setMinHeight(800);
        
        // Load the dashboard FXML
        URL fxmlUrl = getClass().getResource("/fxml/Dashboard.fxml");
        if (fxmlUrl == null) {
            System.err.println("ERROR: Cannot find Dashboard.fxml. Check your project structure.");
            throw new IOException("Dashboard.fxml not found");
        }
        
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        
        // Create a new scene
        Scene scene = new Scene(root, 1400, 900);
        
        // Add the CSS file
        URL cssUrl = getClass().getResource("/styles/dashboard.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.err.println("WARNING: dashboard.css not found");
        }
        
        // Set the scene to the stage
        dashboardStage.setScene(scene);
        dashboardStage.setTitle("Bestun Cars Management System - Dashboard");
        
        // Show the dashboard
        dashboardStage.show();
    }
}