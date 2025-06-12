package com.bcms.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;

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
        // Load the dashboard FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
        Parent root = loader.load();
        
        // Create new stage for dashboard
        Stage dashboardStage = new Stage();
        Scene scene = new Scene(root, 1400, 900);
        
        // Add the CSS file
        scene.getStylesheets().add(getClass().getResource("/styles/dashboard.css").toExternalForm());
        
        // Set up the dashboard stage
        dashboardStage.setTitle("Bestun Cars Management System");
        dashboardStage.setScene(scene);
        dashboardStage.setMinWidth(1200);
        dashboardStage.setMinHeight(800);
        
        // Show the dashboard
        dashboardStage.show();
    }
}