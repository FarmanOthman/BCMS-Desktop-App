package com.bcms.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * A centralized controller for handling navigation between different pages in the application.
 * This class reduces code duplication by providing common navigation methods that can be used by all controllers.
 */
public class NavigationController {
    
    /**
     * Navigates to the specified page.
     * 
     * @param sourceButton The button that triggered the navigation (used to get the current stage)
     * @param fxmlPath The path to the FXML file of the target page
     * @param pageTitle The title of the target page for error messages
     * @param cssPath The path to the CSS file for styling the page
     * @throws IOException If there's an error loading the FXML file
     */
    public static void navigateTo(Button sourceButton, String fxmlPath, String pageTitle, String... cssPaths) throws IOException {
        // Get the current stage
        Stage currentStage = (Stage) sourceButton.getScene().getWindow();
        
        // Load the FXML
        URL fxmlUrl = NavigationController.class.getResource(fxmlPath);
        if (fxmlUrl == null) {
            System.err.println("ERROR: Cannot find " + fxmlPath + ". Check your project structure.");
            throw new IOException(pageTitle + " FXML not found");
        }
        
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        
        // Create a new scene
        Scene scene = new Scene(root, 1400, 900);
        
        // Add CSS stylesheets if provided
        if (cssPaths != null) {
            for (String cssPath : cssPaths) {
                URL cssUrl = NavigationController.class.getResource(cssPath);
                if (cssUrl != null) {
                    scene.getStylesheets().add(cssUrl.toExternalForm());
                } else {
                    System.err.println("WARNING: Cannot find CSS: " + cssPath);
                }
            }
        }
        
        // Set the new scene on the current stage
        currentStage.setScene(scene);
        currentStage.setTitle("Bestun Cars Management System - " + pageTitle);
        currentStage.show();
    }
    
    /**
     * Navigates to the Dashboard page.
     * 
     * @param sourceButton The button that triggered the navigation
     * @throws IOException If there's an error loading the FXML file
     */
    public static void openDashboard(Button sourceButton) throws IOException {
        navigateTo(sourceButton, "/fxml/Dashboard.fxml", "Dashboard", "/styles/dashboard.css");
    }
    
    /**
     * Navigates to the Inventory page.
     * 
     * @param sourceButton The button that triggered the navigation
     * @throws IOException If there's an error loading the FXML file
     */
    public static void openInventory(Button sourceButton) throws IOException {
        navigateTo(sourceButton, "/fxml/Inventory.fxml", "Inventory", "/styles/dashboard.css", "/styles/inventory.css");
    }
    
    /**
     * Navigates to the Repairs & Service page.
     * 
     * @param sourceButton The button that triggered the navigation
     * @throws IOException If there's an error loading the FXML file
     */
    public static void openRepairsService(Button sourceButton) throws IOException {
        navigateTo(sourceButton, "/fxml/RepairsService.fxml", "Repairs & Service", "/styles/dashboard.css", "/styles/repairs.css");
    }
    
    /**
     * Navigates to the Sales page.
     * 
     * @param sourceButton The button that triggered the navigation
     * @throws IOException If there's an error loading the FXML file
     */
    public static void openSales(Button sourceButton) throws IOException {
        navigateTo(sourceButton, "/fxml/Sales.fxml", "Sales", "/styles/dashboard.css", "/styles/Sales.css");
    }
    
    /**
     * Navigates to the Buyers page.
     * 
     * @param sourceButton The button that triggered the navigation
     * @throws IOException If there's an error loading the FXML file
     */
    public static void openBuyers(Button sourceButton) throws IOException {
        navigateTo(sourceButton, "/fxml/Buyer.fxml", "Buyers", "/styles/dashboard.css", "/styles/buyer.css");
    }
    
    /**
     * Navigates to the Analytics page.
     * 
     * @param sourceButton The button that triggered the navigation
     * @throws IOException If there's an error loading the FXML file
     */
    public static void openAnalytics(Button sourceButton) throws IOException {
        navigateTo(sourceButton, "/fxml/Analytics.fxml", "Analytics", "/styles/dashboard.css", "/styles/analytics.css");
    }
    
    /**
     * Navigates to the Finance page.
     * 
     * @param sourceButton The button that triggered the navigation
     * @throws IOException If there's an error loading the FXML file
     */
    public static void openFinance(Button sourceButton) throws IOException {
        navigateTo(sourceButton, "/fxml/finance.fxml", "Finance", "/styles/dashboard.css", "/styles/finance.css");
    }
    
    /**
     * Navigates to the Activity Log page.
     * 
     * @param sourceButton The button that triggered the navigation
     * @throws IOException If there's an error loading the FXML file
     */
    public static void openActivity(Button sourceButton) throws IOException {
        navigateTo(sourceButton, "/fxml/Activity.fxml", "Activity", "/styles/dashboard.css", "/styles/activity.css");
    }
    
    /**
     * Navigates to the Settings page.
     * 
     * @param sourceButton The button that triggered the navigation
     * @throws IOException If there's an error loading the FXML file
     */
    public static void openSettings(Button sourceButton) throws IOException {
        navigateTo(sourceButton, "/fxml/Settings.fxml", "Settings", "/styles/dashboard.css", "/styles/settings.css");
    }
    
    /**
     * Navigates to the User Management page.
     * 
     * @param sourceButton The button that triggered the navigation
     * @throws IOException If there's an error loading the FXML file
     */
    public static void openUserManagement(Button sourceButton) throws IOException {
        navigateTo(sourceButton, "/fxml/UserManagement.fxml", "User Management", "/styles/dashboard.css", "/styles/usermanagement.css");
    }
    
    /**
     * Shows an error alert dialog with the provided information.
     * 
     * @param title The title of the alert dialog
     * @param header The header text for the alert dialog
     * @param content The content text for the alert dialog
     */
    public static void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
