package com.bcms.controller;

import com.bcms.viewmodel.AnalyticsViewModel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class AnalyticsController extends BaseController {
    
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
    protected String getCurrentPageName() {
        return "analytics";
    }
    
    @Override
    protected void initializeController() {
        viewModel = new AnalyticsViewModel();
        
        setupFilters();
        setupMetricsCards();
        setupSalesPerformanceChart();
        setupButtonHandlers();
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Report Generation");
        alert.setHeaderText("Generating Report");
        alert.setContentText("Generating " + reportType + " for " + timeRange + "...\n\n" +
            "This would typically export or display a detailed report based on the selected parameters.");
        alert.showAndWait();
    }
    
    private void exportToPDF() {
        String timeRange = timeRangeComboBox.getValue();
        String reportType = reportTypeComboBox.getValue();
        
        // Display confirmation that PDF is being exported
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("PDF Export");
        alert.setHeaderText("Exporting to PDF");
        alert.setContentText("Exporting " + reportType + " for " + timeRange + " to PDF...\n\n" +
            "In a production environment, this would create and save a PDF file.");
        alert.showAndWait();
    }
    
    private void exportToCSV() {
        String timeRange = timeRangeComboBox.getValue();
        String reportType = reportTypeComboBox.getValue();
        
        // Display confirmation that CSV is being exported
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("CSV Export");
        alert.setHeaderText("Exporting to CSV");
        alert.setContentText("Exporting " + reportType + " for " + timeRange + " to CSV...\n\n" +
            "In a production environment, this would create and save a CSV file.");
        alert.showAndWait();
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
  
}