package com.bcms.controller;

import com.bcms.viewmodel.AnalyticsViewModel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AnalyticsController implements Initializable {
    
    @FXML private Button dashboardBtn;
    @FXML private Button inventoryBtn;
    @FXML private Button repairsBtn;
    @FXML private Button salesBtn;
    @FXML private Button customersBtn;
    @FXML private Button analyticsBtn;
    @FXML private Button financeBtn;
    @FXML private Button activityBtn;
    @FXML private Button settingsBtn;
    @FXML private Button userMgmtBtn;
    
    @FXML private ComboBox<String> timeRangeComboBox;
    @FXML private ComboBox<String> reportTypeComboBox;
    @FXML private Button generateReportBtn;
    @FXML private Button pdfExportBtn;
    @FXML private Button csvExportBtn;
    @FXML private Button resetBtn;
    
    @FXML private Label totalSalesValue;
    @FXML private Label totalSalesDetail;
    @FXML private Label netProfitValue;
    @FXML private HBox netProfitMargin;
    @FXML private Label avgSalePrice;
    @FXML private Label avgSalePriceDetail;
    @FXML private Label repairCostsValue;
    @FXML private Label repairCostsDetail;
    
    @FXML private BarChart<String, Number> salesPerformanceChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;
    
    @SuppressWarnings("unused")
    private AnalyticsViewModel viewModel;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewModel = new AnalyticsViewModel();
        
        setupNavigationHandlers();
        setupFilters();
        setupMetricsCards();
        setupSalesPerformanceChart();
        setupButtonHandlers();
    }
    
    private void setupNavigationHandlers() {
        // Set Analytics button as active
        setActiveButton(analyticsBtn);
        
        // Dashboard button
        dashboardBtn.setOnAction(e -> {
            try {
                setActiveButton(dashboardBtn);
                openDashboard();
            } catch (IOException ex) {
                ex.printStackTrace();
                showErrorAlert("Navigation Error", "Could not open Dashboard", ex.getMessage());
            }
        });
        
        // Inventory button
        inventoryBtn.setOnAction(e -> {
            try {
                setActiveButton(inventoryBtn);
                openInventory();
            } catch (IOException ex) {
                ex.printStackTrace();
                showErrorAlert("Navigation Error", "Could not open Inventory", ex.getMessage());
            }
        });
        
        // Repairs button
        repairsBtn.setOnAction(e -> {
            try {
                setActiveButton(repairsBtn);
                openRepairsService();
            } catch (IOException ex) {
                ex.printStackTrace();
                showErrorAlert("Navigation Error", "Could not open Repairs & Service", ex.getMessage());
            }
        });
        
        // Sales button
        salesBtn.setOnAction(e -> {
            try {
                setActiveButton(salesBtn);
                openSales();
            } catch (IOException ex) {
                ex.printStackTrace();
                showErrorAlert("Navigation Error", "Could not open Sales", ex.getMessage());
            }
        });
        
        // Customers button
        customersBtn.setOnAction(e -> {
            try {
                setActiveButton(customersBtn);
                // When a customers view is created, uncomment this line:
                // openCustomers();
                System.out.println("Navigate to Customers - View not yet implemented");
            } catch (Exception ex) {
                ex.printStackTrace();
                showErrorAlert("Navigation Error", "Could not open Customers view", ex.getMessage());
            }
        });
        
        // Analytics button (already active)
        analyticsBtn.setOnAction(e -> {
            setActiveButton(analyticsBtn); 
            System.out.println("Already on Analytics page");
        });
        
        // Finance button
        financeBtn.setOnAction(e -> {
            try {
                setActiveButton(financeBtn);
                openFinance();
            } catch (IOException ex) {
                ex.printStackTrace();
                showErrorAlert("Navigation Error", "Could not open Finance", ex.getMessage());
            }
        });
        
        // Activity button
        activityBtn.setOnAction(e -> {
            try {
                setActiveButton(activityBtn);
                openActivity();
            } catch (IOException ex) {
                ex.printStackTrace();
                showErrorAlert("Navigation Error", "Could not open Activity view", ex.getMessage());
            }
        });
        
        // Settings button
        settingsBtn.setOnAction(e -> {
            try {
                setActiveButton(settingsBtn);
                // When a settings view is created, uncomment this line:
                // openSettings();
                System.out.println("Navigate to Settings - View not yet implemented");
            } catch (Exception ex) {
                ex.printStackTrace();
                showErrorAlert("Navigation Error", "Could not open Settings view", ex.getMessage());
            }
        });
        
        // User Management button
        userMgmtBtn.setOnAction(e -> {
            try {
                setActiveButton(userMgmtBtn);
                openUserManagement();
            } catch (Exception ex) {
                ex.printStackTrace();
                showErrorAlert("Navigation Error", "Could not open User Management view", ex.getMessage());
            }
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
        financeBtn.getStyleClass().remove("active");
        activityBtn.getStyleClass().remove("active");
        settingsBtn.getStyleClass().remove("active");
        userMgmtBtn.getStyleClass().remove("active");
        
        // Add active class to selected button
        activeBtn.getStyleClass().add("active");
    }
    
    private void setupFilters() {
        // Set up time range filter
        timeRangeComboBox.setItems(FXCollections.observableArrayList(
            "This Month", "Last Month", "Last 3 Months", "Last 6 Months", "This Year", "Last Year", "Custom..."
        ));
        timeRangeComboBox.setValue("This Month");
        
        // Set up report type filter
        reportTypeComboBox.setItems(FXCollections.observableArrayList(
            "Sales Summary", "Inventory Report", "Finance Report", "Customer Report", "Repair Report"
        ));
        reportTypeComboBox.setValue("Sales Summary");
        
        // Add listeners to filters to refresh data when changed
        timeRangeComboBox.setOnAction(e -> refreshData());
        reportTypeComboBox.setOnAction(e -> refreshData());
    }
    
    private void setupMetricsCards() {
        // Set values for metric cards
        totalSalesValue.setText("$50,000");
        totalSalesDetail.setText("6 cars sold this month");
        
        netProfitValue.setText("+$7,000");
        // netProfitMargin is now an HBox, so we don't need to set its text
        
        avgSalePrice.setText("$8,333");
        avgSalePriceDetail.setText("per vehicle");
        
        repairCostsValue.setText("-$2,400");
        repairCostsDetail.setText("18 repairs completed");
        
        // Style profit/loss values appropriately
        netProfitValue.getStyleClass().add("profit-value");
        repairCostsValue.getStyleClass().add("loss-value");
    }
    
    private void setupSalesPerformanceChart() {
        // Configure chart axes
        xAxis.setLabel("Month");
        yAxis.setLabel("Revenue ($)");
        
        // Create the series
        XYChart.Series<String, Number> actualSeries = new XYChart.Series<>();
        actualSeries.setName("Actual");
        
        // Add data to the series
        actualSeries.getData().add(new XYChart.Data<>("Jan", 5000));
        actualSeries.getData().add(new XYChart.Data<>("Feb", 10000));
        actualSeries.getData().add(new XYChart.Data<>("Mar", 15000));
        actualSeries.getData().add(new XYChart.Data<>("Apr", 20000));
        actualSeries.getData().add(new XYChart.Data<>("May", 25000));
        actualSeries.getData().add(new XYChart.Data<>("Jun", 30000));
        
        // Add current month and projections
        XYChart.Series<String, Number> projectionSeries = new XYChart.Series<>();
        projectionSeries.setName("Projected");
        
        projectionSeries.getData().add(new XYChart.Data<>("Jul", 15000));
        projectionSeries.getData().add(new XYChart.Data<>("Aug", 10000));
        
        // Add series to the chart - individually to avoid generic array creation warning
        salesPerformanceChart.getData().add(actualSeries);
        salesPerformanceChart.getData().add(projectionSeries);
        
        // Style the chart
        salesPerformanceChart.setLegendVisible(true);
        salesPerformanceChart.setAnimated(false);
        
        // Style the bars
        for (XYChart.Series<String, Number> series : salesPerformanceChart.getData()) {
            if (series.getName().equals("Actual")) {
                for (XYChart.Data<String, Number> data : series.getData()) {
                    data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                        if (newNode != null) {
                            if (data.getXValue().equals("Jun")) {
                                newNode.setStyle("-fx-bar-fill: #4ade80;"); // Current month in green
                            } else {
                                newNode.setStyle("-fx-bar-fill: #60a5fa;"); // Past months in blue
                            }
                        }
                    });
                }
            } else {
                // Projected months
                for (XYChart.Data<String, Number> data : series.getData()) {
                    data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                        if (newNode != null) {
                            newNode.setStyle("-fx-bar-fill: #9ca3af; -fx-opacity: 0.7;"); // Projected in gray
                        }
                    });
                }
            }
        }
    }
    
    private void setupButtonHandlers() {
        // Generate Report button
        generateReportBtn.setOnAction(e -> {
            System.out.println("Generate Report clicked");
            generateReport();
        });
        
        // PDF Export button
        pdfExportBtn.setOnAction(e -> {
            System.out.println("PDF Export clicked");
            exportToPDF();
        });
        
        // CSV Export button
        csvExportBtn.setOnAction(e -> {
            System.out.println("CSV Export clicked");
            exportToCSV();
        });
        
        // Reset button
        resetBtn.setOnAction(e -> {
            System.out.println("Reset clicked");
            resetFilters();
        });
    }
    
    private void generateReport() {
        String timeRange = timeRangeComboBox.getValue();
        String reportType = reportTypeComboBox.getValue();
        
        // Display confirmation that report is being generated
        showInfoAlert("Report Generation", "Generating Report", 
            "Generating " + reportType + " for " + timeRange + "...\n\n" +
            "This would typically export or display a detailed report based on the selected parameters."
        );
    }
    
    private void exportToPDF() {
        String timeRange = timeRangeComboBox.getValue();
        String reportType = reportTypeComboBox.getValue();
        
        // Display confirmation that PDF is being exported
        showInfoAlert("PDF Export", "Exporting to PDF", 
            "Exporting " + reportType + " for " + timeRange + " to PDF...\n\n" +
            "In a production environment, this would create and save a PDF file."
        );
    }
    
    private void exportToCSV() {
        String timeRange = timeRangeComboBox.getValue();
        String reportType = reportTypeComboBox.getValue();
        
        // Display confirmation that CSV is being exported
        showInfoAlert("CSV Export", "Exporting to CSV", 
            "Exporting " + reportType + " for " + timeRange + " to CSV...\n\n" +
            "In a production environment, this would create and save a CSV file."
        );
    }
    
    private void resetFilters() {
        // Reset filters to default values
        timeRangeComboBox.setValue("This Month");
        reportTypeComboBox.setValue("Sales Summary");
        
        // Refresh data
        refreshData();
    }
    
    private void refreshData() {
        // Get selected values
        String timeRange = timeRangeComboBox.getValue();
        String reportType = reportTypeComboBox.getValue();
        
        System.out.println("Refreshing data for " + reportType + " in " + timeRange);
        
        // In a real implementation, this would update the chart and metrics
        // based on the selected filters
        
        // For now, we'll just update the sales metrics with different values based on time range
        if (timeRange.equals("This Month")) {
            totalSalesValue.setText("$50,000");
            totalSalesDetail.setText("6 cars sold this month");
            netProfitValue.setText("+$7,000");
            // netProfitMargin is an HBox, not a Label
        } else if (timeRange.equals("Last Month")) {
            totalSalesValue.setText("$42,500");
            totalSalesDetail.setText("5 cars sold last month");
            netProfitValue.setText("+$5,800");
            // netProfitMargin is an HBox, not a Label
        } else if (timeRange.contains("3 Months")) {
            totalSalesValue.setText("$135,000");
            totalSalesDetail.setText("16 cars sold in last 3 months");
            netProfitValue.setText("+$18,900");
            // netProfitMargin is an HBox, not a Label
        }
    }
    
    private void openDashboard() throws IOException {
        // Get the current stage
        Stage currentStage = (Stage) dashboardBtn.getScene().getWindow();
        
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
        
        // Set the scene to the current stage
        currentStage.setScene(scene);
        currentStage.setTitle("Bestun Cars Management System - Dashboard");
    }
    
    private void openInventory() throws IOException {
        // Get the current stage
        Stage currentStage = (Stage) inventoryBtn.getScene().getWindow();
        
        // Load the inventory FXML
        URL fxmlUrl = getClass().getResource("/fxml/Inventory.fxml");
        if (fxmlUrl == null) {
            System.err.println("ERROR: Cannot find Inventory.fxml. Check your project structure.");
            throw new IOException("Inventory.fxml not found");
        }
        
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        
        // Create a new scene
        Scene scene = new Scene(root, 1400, 900);
        
        // Add the CSS files
        URL dashboardCssUrl = getClass().getResource("/styles/dashboard.css");
        URL inventoryCssUrl = getClass().getResource("/styles/inventory.css");
        
        if (dashboardCssUrl != null) {
            scene.getStylesheets().add(dashboardCssUrl.toExternalForm());
        } else {
            System.err.println("WARNING: dashboard.css not found");
        }
        
        if (inventoryCssUrl != null) {
            scene.getStylesheets().add(inventoryCssUrl.toExternalForm());
        } else {
            System.err.println("WARNING: inventory.css not found");
        }
        
        // Set the scene to the current stage
        currentStage.setScene(scene);
        currentStage.setTitle("Bestun Cars Management System - Inventory");
    }
    
    private void openSales() throws IOException {
        // Get the current stage
        Stage currentStage = (Stage) salesBtn.getScene().getWindow();
        
        // Load the sales FXML
        URL fxmlUrl = getClass().getResource("/fxml/Sales.fxml");
        if (fxmlUrl == null) {
            System.err.println("ERROR: Cannot find Sales.fxml. Check your project structure.");
            throw new IOException("Sales.fxml not found");
        }
        
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        
        // Create a new scene
        Scene scene = new Scene(root, 1400, 900);
        
        // Add the CSS files
        URL dashboardCssUrl = getClass().getResource("/styles/dashboard.css");
        URL salesCssUrl = getClass().getResource("/styles/sales.css");
        
        if (dashboardCssUrl != null) {
            scene.getStylesheets().add(dashboardCssUrl.toExternalForm());
        } else {
            System.err.println("WARNING: dashboard.css not found");
        }
        
        if (salesCssUrl != null) {
            scene.getStylesheets().add(salesCssUrl.toExternalForm());
        } else {
            System.err.println("WARNING: sales.css not found");
        }
        
        // Set the scene to the current stage
        currentStage.setScene(scene);
        currentStage.setTitle("Bestun Cars Management System - Sales");
    }
    
    private void openFinance() throws IOException {
        // Get the current stage
        Stage currentStage = (Stage) financeBtn.getScene().getWindow();
        
        // Load the finance FXML
        URL fxmlUrl = getClass().getResource("/fxml/finance.fxml");
        if (fxmlUrl == null) {
            System.err.println("ERROR: Cannot find finance.fxml. Check your project structure.");
            throw new IOException("finance.fxml not found");
        }
        
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        
        // Create a new scene
        Scene scene = new Scene(root, 1400, 900);
        
        // Add the CSS files
        URL dashboardCssUrl = getClass().getResource("/styles/dashboard.css");
        URL financeCssUrl = getClass().getResource("/styles/finance.css");
        
        if (dashboardCssUrl != null) {
            scene.getStylesheets().add(dashboardCssUrl.toExternalForm());
        } else {
            System.err.println("WARNING: dashboard.css not found");
        }
        
        if (financeCssUrl != null) {
            scene.getStylesheets().add(financeCssUrl.toExternalForm());
        } else {
            System.err.println("WARNING: finance.css not found");
        }
        
        // Set the scene to the current stage
        currentStage.setScene(scene);
        currentStage.setTitle("Bestun Cars Management System - Finance");
    }
    
    /**
     * Navigate to the Activity page
     */
    private void openActivity() throws IOException {
        // Get the current stage
        Stage currentStage = (Stage) dashboardBtn.getScene().getWindow();
        
        // Load the Activity FXML
        URL fxmlUrl = getClass().getResource("/fxml/Activity.fxml");
        if (fxmlUrl == null) {
            System.err.println("ERROR: Cannot find Activity.fxml. Check your project structure.");
            throw new IOException("Activity.fxml not found");
        }
        
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        
        // Create a new scene
        Scene scene = new Scene(root, 1400, 900);
        
        // Add the CSS files
        URL dashboardCssUrl = getClass().getResource("/styles/dashboard.css");
        URL activityCssUrl = getClass().getResource("/styles/activity.css");
        
        if (dashboardCssUrl != null) {
            scene.getStylesheets().add(dashboardCssUrl.toExternalForm());
        } else {
            System.err.println("WARNING: dashboard.css not found");
        }
        
        if (activityCssUrl != null) {
            scene.getStylesheets().add(activityCssUrl.toExternalForm());
        } else {
            System.err.println("WARNING: activity.css not found");
        }
        
        // Set the scene to the current stage
        currentStage.setScene(scene);
        currentStage.setTitle("Bestun Cars Management System - Activity Log");
    }
    
    // Method to open User Management page
    private void openUserManagement() throws IOException {
        // Get the current stage
        Stage currentStage = (Stage) userMgmtBtn.getScene().getWindow();
        
        // Load the user management FXML
        URL fxmlUrl = getClass().getResource("/fxml/UserManagement.fxml");
        if (fxmlUrl == null) {
            throw new IOException("UserManagement.fxml not found");
        }
        
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        
        // Create a new scene
        Scene scene = new Scene(root, 1400, 900);
        
        // Store the controller in the scene's userData for action button access
        UserManagementController controller = loader.getController();
        scene.setUserData(controller);
        
        // Add the CSS files
        URL dashboardCssUrl = getClass().getResource("/styles/dashboard.css");
        if (dashboardCssUrl != null) {
            scene.getStylesheets().add(dashboardCssUrl.toExternalForm());
        } else {
            System.err.println("WARNING: dashboard.css not found");
        }
        
        URL userManagementCssUrl = getClass().getResource("/styles/usermanagement.css");
        if (userManagementCssUrl != null) {
            scene.getStylesheets().add(userManagementCssUrl.toExternalForm());
        } else {
            System.err.println("WARNING: usermanagement.css not found");
        }
        
        // Set the scene to the stage and show
        currentStage.setScene(scene);
        currentStage.setTitle("Bestun Cars Management System - User Management");
    }
    
    /**
     * Navigate to the Repairs & Service page
     */
    private void openRepairsService() throws IOException {
        // Get the current stage
        Stage currentStage = (Stage) repairsBtn.getScene().getWindow();
        
        // Load the repairs & service FXML
        URL fxmlUrl = getClass().getResource("/fxml/RepairsService.fxml");
        if (fxmlUrl == null) {
            System.err.println("ERROR: Cannot find RepairsService.fxml. Check your project structure.");
            throw new IOException("RepairsService.fxml not found");
        }
        
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        
        // Create a new scene
        Scene scene = new Scene(root, 1400, 900);
        
        // Set the new scene on the current stage
        currentStage.setScene(scene);
        currentStage.show();
    }
    
    // Helper methods for showing alerts
    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private void showInfoAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}