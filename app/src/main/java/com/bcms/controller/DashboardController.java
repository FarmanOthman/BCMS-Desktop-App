package com.bcms.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    
    @FXML private Button dashboardBtn;
    @FXML private Button inventoryBtn;
    @FXML private Button repairsBtn;
    @FXML private Button salesBtn;
    @FXML private Button customersBtn;
    @FXML private Button analyticsBtn;
    @FXML private Button activityBtn;
    @FXML private Button settingsBtn;
    @FXML private Button userMgmtBtn;
    
    @FXML private LineChart<String, Number> revenueChart;
    @FXML private BarChart<String, Number> repairsChart;
    @FXML private VBox activityList;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupCharts();
        setupNavigationHandlers();
        loadRecentActivities();
    }
    
    private void setupCharts() {
        // Setup Revenue Chart
        XYChart.Series<String, Number> revenueSeries = new XYChart.Series<>();
        revenueSeries.getData().add(new XYChart.Data<>("Jan", 800000));
        revenueSeries.getData().add(new XYChart.Data<>("Feb", 950000));
        revenueSeries.getData().add(new XYChart.Data<>("Mar", 1100000));
        revenueSeries.getData().add(new XYChart.Data<>("Apr", 1200000));
        
        revenueChart.getData().add(revenueSeries);
        revenueChart.setCreateSymbols(false);
        revenueChart.setAnimated(false);
        
        // Style the revenue line
        revenueSeries.getNode().setStyle("-fx-stroke: #10b981; -fx-stroke-width: 2;");
        
        // Setup Repairs Chart
        XYChart.Series<String, Number> repairsSeries = new XYChart.Series<>();
        repairsSeries.getData().add(new XYChart.Data<>("Mon", 3));
        repairsSeries.getData().add(new XYChart.Data<>("Tue", 5));
        repairsSeries.getData().add(new XYChart.Data<>("Wed", 4));
        repairsSeries.getData().add(new XYChart.Data<>("Thu", 6));
        repairsSeries.getData().add(new XYChart.Data<>("Fri", 4));
        repairsSeries.getData().add(new XYChart.Data<>("Sat", 2));
        repairsSeries.getData().add(new XYChart.Data<>("Sun", 1));
        
        repairsChart.getData().add(repairsSeries);
        
        // Style the bars
        for (XYChart.Data<String, Number> data : repairsSeries.getData()) {
            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    newNode.setStyle("-fx-bar-fill: #4f7cff;");
                }
            });
        }
    }
    
    private void setupNavigationHandlers() {
        // Dashboard button (already active)
        dashboardBtn.setOnAction(e -> System.out.println("Dashboard clicked"));
        
        // Inventory button
        inventoryBtn.setOnAction(e -> {
            setActiveButton(inventoryBtn);
            // Navigate to inventory view
            System.out.println("Navigate to Inventory");
        });
        
        // Repairs button
        repairsBtn.setOnAction(e -> {
            setActiveButton(repairsBtn);
            // Navigate to repairs view
            System.out.println("Navigate to Repairs");
        });
        
        // Sales button
        salesBtn.setOnAction(e -> {
            setActiveButton(salesBtn);
            // Navigate to sales view
            System.out.println("Navigate to Sales");
        });
        
        // Customers button
        customersBtn.setOnAction(e -> {
            setActiveButton(customersBtn);
            // Navigate to customers view
            System.out.println("Navigate to Customers");
        });
        
        // Analytics button
        analyticsBtn.setOnAction(e -> {
            setActiveButton(analyticsBtn);
            // Navigate to analytics view
            System.out.println("Navigate to Analytics");
        });
        
        // Activity button
        activityBtn.setOnAction(e -> {
            setActiveButton(activityBtn);
            // Navigate to activity view
            System.out.println("Navigate to Activity");
        });
        
        // Settings button
        settingsBtn.setOnAction(e -> {
            setActiveButton(settingsBtn);
            // Navigate to settings view
            System.out.println("Navigate to Settings");
        });
        
        // User Management button
        userMgmtBtn.setOnAction(e -> {
            setActiveButton(userMgmtBtn);
            // Navigate to user management view
            System.out.println("Navigate to User Management");
        });
    }
    
    private void setActiveButton(Button activeBtn) {
        // Remove active class from all buttons
        dashboardBtn.getStyleClass().remove("active");
        inventoryBtn.getStyleClass().remove("active");
        repairsBtn.getStyleClass().remove("active");
        salesBtn.getStyleClass().remove("active");
        customersBtn.getStyleClass().remove("active");
        analyticsBtn.getStyleClass().remove("active");
        activityBtn.getStyleClass().remove("active");
        settingsBtn.getStyleClass().remove("active");
        userMgmtBtn.getStyleClass().remove("active");
        
        // Add active class to selected button
        activeBtn.getStyleClass().add("active");
    }
    
    private void loadRecentActivities() {
        // This would normally load from database
        // For now, activities are defined in FXML
    }
    
    // Quick Actions handlers
    @FXML
    private void handleAddNewCar() {
        System.out.println("Add new car clicked");
        // Open add car dialog
    }
    
    @FXML
    private void handleQuickSale() {
        System.out.println("Quick sale clicked");
        // Open quick sale dialog
    }
    
    @FXML
    private void handleScheduleRepair() {
        System.out.println("Schedule repair clicked");
        // Open schedule repair dialog
    }
    
    @FXML
    private void handleGenerateReport() {
        System.out.println("Generate report clicked");
        // Open report generation dialog
    }
    
    @FXML
    private void handleManageCustomers() {
        System.out.println("Manage customers clicked");
        // Navigate to customers management
    }
}