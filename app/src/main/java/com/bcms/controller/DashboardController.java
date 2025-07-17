package com.bcms.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

public class DashboardController extends BaseController {
    
    @FXML private LineChart<String, Number> revenueChart;
    @FXML private BarChart<String, Number> repairsChart;
    @FXML private VBox activityList;
    
    @Override
    protected String getCurrentPageName() {
        return "dashboard";
    }
    
    @Override
    protected void initializeController() {
        setupCharts();
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
    
    private void loadRecentActivities() {
        // This would normally load from database
        // For now, activities are defined in FXML
    }
    
    // Quick Actions handlers
    @FXML
    private void handleAddNewCar(ActionEvent event) {
        System.out.println("Add new car clicked");
        try {
            // Get the button that triggered this event
            Button sourceButton = (Button) event.getSource();
            NavigationController.openInventory(sourceButton);
        } catch (Exception e) {
            System.err.println("Error navigating to inventory from quick action:");
            e.printStackTrace();
        }
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