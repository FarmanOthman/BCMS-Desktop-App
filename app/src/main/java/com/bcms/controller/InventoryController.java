package com.bcms.controller;

import com.bcms.viewmodel.InventoryViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InventoryController implements Initializable {
    
    @FXML private Button dashboardBtn;
    @FXML private Button inventoryBtn;
    @FXML private Button repairsBtn;
    @FXML private Button salesBtn;
    @FXML private Button customersBtn;
    @FXML private Button analyticsBtn;
    @FXML private Button activityBtn;
    @FXML private Button settingsBtn;
    @FXML private Button userMgmtBtn;
    
    @FXML private TextField searchField;
    @FXML private ComboBox<String> statusFilter;
    @FXML private ComboBox<String> yearRangeFilter;
    @FXML private Button resetButton;
    @FXML private Button addNewCarButton;
    @FXML private Button deleteButton;
    @FXML private Button exportCSVButton;
    
    @FXML private TableView<InventoryViewModel.CarItem> carTableView;
    @FXML private TableColumn<InventoryViewModel.CarItem, Boolean> selectColumn;
    @FXML private TableColumn<InventoryViewModel.CarItem, String> idColumn;
    @FXML private TableColumn<InventoryViewModel.CarItem, String> vehicleColumn;
    @FXML private TableColumn<InventoryViewModel.CarItem, Integer> yearColumn;
    @FXML private TableColumn<InventoryViewModel.CarItem, Double> priceColumn;
    @FXML private TableColumn<InventoryViewModel.CarItem, String> statusColumn;
    @FXML private TableColumn<InventoryViewModel.CarItem, String> vinColumn;
    @FXML private TableColumn<InventoryViewModel.CarItem, String> addedColumn;
    @FXML private TableColumn<InventoryViewModel.CarItem, HBox> actionsColumn;
    
    @FXML private Pagination pagination;
    
    private InventoryViewModel viewModel;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewModel = new InventoryViewModel();
        
        setupNavigationHandlers();
        setupFilters();
        setupTableView();
        setupPagination();
        setupButtons();
    }
    
    private void setupNavigationHandlers() {
        // Set Inventory button as active
        setActiveButton(inventoryBtn);
        
        // Dashboard button
        dashboardBtn.setOnAction(e -> {
            setActiveButton(dashboardBtn);
            // Navigate to dashboard view
            try {
                openDashboard();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        
        // Inventory button (already active)
        inventoryBtn.setOnAction(e -> System.out.println("Inventory clicked"));
        
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
    
    private void setupFilters() {
        // Set up the status filter
        statusFilter.setItems(FXCollections.observableArrayList(
            "All Statuses", "Available", "Sold", "In Repair"
        ));
        statusFilter.setValue("All Statuses");
        
        // Set up the year range filter
        yearRangeFilter.setItems(FXCollections.observableArrayList(
            "2020 - 2024", "2015 - 2019", "2010 - 2014", "All Years"
        ));
        yearRangeFilter.setValue("2020 - 2024");
        
        // Set up search field
        searchField.setPromptText("Search by make, model, VIN, or year...");
        
        // Reset button action
        resetButton.setOnAction(e -> {
            searchField.clear();
            statusFilter.setValue("All Statuses");
            yearRangeFilter.setValue("2020 - 2024");
            refreshTable();
        });
        
        // Add listeners to filters to refresh table when changed
        statusFilter.setOnAction(e -> refreshTable());
        yearRangeFilter.setOnAction(e -> refreshTable());
        searchField.textProperty().addListener((obs, oldVal, newVal) -> refreshTable());
    }
    
    private void setupTableView() {
        // Configure table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        vehicleColumn.setCellValueFactory(new PropertyValueFactory<>("vehicle"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("formattedPrice"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        vinColumn.setCellValueFactory(new PropertyValueFactory<>("vin"));
        addedColumn.setCellValueFactory(new PropertyValueFactory<>("added"));
        actionsColumn.setCellValueFactory(new PropertyValueFactory<>("actions"));
        
        // Style the status column based on value
        statusColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    setText(item);
                    
                    // Apply styles based on status
                    if (item.equals("Available")) {
                        setStyle("-fx-text-fill: #10b981;");
                    } else if (item.equals("Sold")) {
                        setStyle("-fx-text-fill: #ef4444;");
                    } else if (item.equals("In Repair")) {
                        setStyle("-fx-text-fill: #f59e0b;");
                    } else {
                        setStyle("-fx-text-fill: white;");
                    }
                }
            }
        });
        
        // Load data into the table
        refreshTable();
    }
    
    private void setupPagination() {
        pagination.setPageCount(10); // Set based on total pages in your data
        pagination.setCurrentPageIndex(0);
        pagination.setPageFactory(this::createPage);
    }
    
    private TableView<InventoryViewModel.CarItem> createPage(int pageIndex) {
        // This method would update the table with the correct page of data
        // For now, we'll just use the same data for each page
        return carTableView;
    }
    
    private void setupButtons() {
        // Add New Car button
        addNewCarButton.setOnAction(e -> {
            System.out.println("Add New Car clicked");
            openAddNewCarDialog();
        });
        
        // Delete button
        deleteButton.setOnAction(e -> {
            System.out.println("Delete clicked");
            // Delete selected cars
        });
        
        // Export CSV button
        exportCSVButton.setOnAction(e -> {
            System.out.println("Export CSV clicked");
            // Export data to CSV
        });
    }
    
    /**
     * Opens the Add New Car dialog.
     * This method can be called from the Dashboard controller.
     */
    public void openAddNewCarDialog() {
        // Code to open Add New Car dialog
        // This would be implemented with a new stage or dialog
        System.out.println("Opening Add New Car dialog");
        // Example implementation:
        /*
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddCarDialog.fxml"));
            Parent dialogRoot = loader.load();
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add New Car");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(addNewCarButton.getScene().getWindow());
            
            Scene scene = new Scene(dialogRoot);
            scene.getStylesheets().add(getClass().getResource("/styles/dashboard.css").toExternalForm());
            
            dialogStage.setScene(scene);
            dialogStage.showAndWait();
            
            // Refresh the table after dialog closes
            refreshTable();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        */
    }
    
    private void refreshTable() {
        // Get the filtered data from view model based on current filters
        String searchText = searchField.getText().toLowerCase();
        String statusValue = statusFilter.getValue();
        String yearRange = yearRangeFilter.getValue();
        
        ObservableList<InventoryViewModel.CarItem> filteredData = viewModel.getFilteredCars(searchText, statusValue, yearRange);
        
        // Update the table with the filtered data
        carTableView.setItems(filteredData);
    }
    
    private void openDashboard() throws IOException {
        // Get the current stage
        Stage currentStage = (Stage) dashboardBtn.getScene().getWindow();
        
        // Load the dashboard FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
        Parent root = loader.load();
        
        // Create a new scene
        Scene scene = new Scene(root, 1400, 900);
        
        // Add the CSS file
        scene.getStylesheets().add(getClass().getResource("/styles/dashboard.css").toExternalForm());
        
        // Set the scene to the current stage
        currentStage.setScene(scene);
        currentStage.setTitle("Bestun Cars Management System - Dashboard");
    }
    
    // Action handlers for edit, delete, and view buttons in each row
    @FXML
    private void handleEditCar(String carId) {
        System.out.println("Edit car with ID: " + carId);
        // Open edit dialog
    }
    
    @FXML
    private void handleDeleteCar(String carId) {
        System.out.println("Delete car with ID: " + carId);
        // Show confirmation dialog and delete
    }
    
    @FXML
    private void handleViewCarDetails(String carId) {
        System.out.println("View car details with ID: " + carId);
        // Open details view
    }
}