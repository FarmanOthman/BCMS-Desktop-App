package com.bcms.controller;

import com.bcms.viewmodel.SaleViewModel;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class SalesController implements Initializable {
    
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
    
    @FXML private TextField searchField;
    @FXML private ComboBox<String> timeFilter;
    @FXML private ComboBox<String> statusFilter;
    @FXML private Button resetButton;
    @FXML private Button addSaleButton;
    @FXML private Button deleteButton;
    @FXML private Button exportCSVButton;
    
    @FXML private TableView<SaleViewModel.SaleItem> salesTableView;
    @FXML private TableColumn<SaleViewModel.SaleItem, String> idColumn;
    @FXML private TableColumn<SaleViewModel.SaleItem, String> vehicleColumn;
    @FXML private TableColumn<SaleViewModel.SaleItem, String> buyerNameColumn;
    @FXML private TableColumn<SaleViewModel.SaleItem, String> salePriceColumn;
    @FXML private TableColumn<SaleViewModel.SaleItem, String> profitLossColumn;
    @FXML private TableColumn<SaleViewModel.SaleItem, String> statusColumn;
    @FXML private TableColumn<SaleViewModel.SaleItem, HBox> actionsColumn;
    
    @FXML private Pagination pagination;
    
    private SaleViewModel viewModel;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewModel = new SaleViewModel();
        
        setupNavigationHandlers();
        setupFilters();
        setupTableView();
        setupPagination();
        setupButtons();
    }
    
    private void setupNavigationHandlers() {
        // Set Sales button as active
        setActiveButton(salesBtn);
        
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
            setActiveButton(repairsBtn);
            // Navigate to repairs view
            System.out.println("Navigate to Repairs");
        });
        
        // Sales button (already active)
        salesBtn.setOnAction(e -> System.out.println("Sales clicked"));
        
        // Customers button
        customersBtn.setOnAction(e -> {
            try {
                setActiveButton(customersBtn);
                openBuyers();
            } catch (IOException ex) {
                ex.printStackTrace();
                showErrorAlert("Navigation Error", "Could not open Buyers", ex.getMessage());
            }
        });
        
        // Analytics button
        analyticsBtn.setOnAction(e -> {
            try {
                setActiveButton(analyticsBtn);
                openAnalytics();
            } catch (Exception ex) {
                ex.printStackTrace();
                showErrorAlert("Navigation Error", "Could not open Analytics", ex.getMessage());
            }
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
        financeBtn.getStyleClass().remove("active");
        activityBtn.getStyleClass().remove("active");
        settingsBtn.getStyleClass().remove("active");
        userMgmtBtn.getStyleClass().remove("active");
        
        // Add active class to selected button
        activeBtn.getStyleClass().add("active");
    }
    
    private void setupFilters() {
        // Set up the status filter
        statusFilter.setItems(FXCollections.observableArrayList(
            "All Statuses", "Completed", "Pending", "Cancelled"
        ));
        statusFilter.setValue("All Statuses");
        
        // Set up the time filter
        timeFilter.setItems(FXCollections.observableArrayList(
            "All Time", "Today", "This Week", "This Month", "This Year"
        ));
        timeFilter.setValue("All Time");
        
        // Set up search field
        searchField.setPromptText("Search by car make, model, or buyer name...");
        
        // Reset button action
        resetButton.setOnAction(e -> {
            searchField.clear();
            statusFilter.setValue("All Statuses");
            timeFilter.setValue("All Time");
            refreshTable();
        });
        
        // Add listeners to filters to refresh table when changed
        statusFilter.setOnAction(e -> refreshTable());
        timeFilter.setOnAction(e -> refreshTable());
        searchField.textProperty().addListener((obs, oldVal, newVal) -> refreshTable());
    }
    
    private void setupTableView() {
        // Configure table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        vehicleColumn.setCellValueFactory(new PropertyValueFactory<>("vehicle"));
        buyerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customer"));
        salePriceColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        profitLossColumn.setCellValueFactory(new PropertyValueFactory<>("profitLoss"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // Set up profit/loss column with color formatting
        profitLossColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    setText(item);
                    
                    // Apply styles based on profit/loss value
                    if (item.startsWith("+")) {
                        setStyle("-fx-text-fill: #10b981;"); // Positive in green
                    } else if (item.startsWith("-")) {
                        setStyle("-fx-text-fill: #ef4444;"); // Negative in red
                    } else {
                        setStyle("-fx-text-fill: white;"); // Default in white
                    }
                }
            }
        });
        
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
                    if (item.equals("Completed")) {
                        setStyle("-fx-text-fill: #10b981;"); // Green for completed
                    } else if (item.equals("Pending")) {
                        setStyle("-fx-text-fill: #f59e0b;"); // Orange for pending
                    } else if (item.equals("Cancelled")) {
                        setStyle("-fx-text-fill: #ef4444;"); // Red for cancelled
                    } else {
                        setStyle("-fx-text-fill: white;"); // Default in white
                    }
                }
            }
        });
        
        // Set up actions column with buttons
        actionsColumn.setCellValueFactory(param -> {
            HBox actionBox = createActionButtons(param.getValue().getId());
            return new javafx.beans.property.SimpleObjectProperty<>(actionBox);
        });
        
        // Load data into the table
        refreshTable();
    }
    
    private HBox createActionButtons(String saleId) {
        HBox hbox = new HBox(5); // 5 is the spacing
        hbox.setAlignment(javafx.geometry.Pos.CENTER);
        
        // Edit button
        Button editBtn = new Button();
        editBtn.getStyleClass().add("action-icon");
        
        // Create an inner shape for the edit icon
        Button editIcon = new Button();
        editIcon.getStyleClass().add("edit-icon");
        editIcon.setMinSize(15, 15);
        editIcon.setMaxSize(15, 15);
        
        editBtn.setGraphic(editIcon);
        editBtn.setTooltip(new Tooltip("Edit"));
        editBtn.setOnAction(e -> handleEditSale(saleId));
        
        // Delete button
        Button deleteBtn = new Button();
        deleteBtn.getStyleClass().add("action-icon");
        
        // Create an inner shape for the delete icon
        Button deleteIcon = new Button();
        deleteIcon.getStyleClass().add("delete-icon");
        deleteIcon.setMinSize(15, 15);
        deleteIcon.setMaxSize(15, 15);
        
        deleteBtn.setGraphic(deleteIcon);
        deleteBtn.setTooltip(new Tooltip("Delete"));
        deleteBtn.setOnAction(e -> handleDeleteSale(saleId));
        
        // View details button
        Button viewBtn = new Button();
        viewBtn.getStyleClass().add("action-icon");
        
        // Create an inner shape for the view icon
        Button viewIcon = new Button();
        viewIcon.getStyleClass().add("view-icon");
        viewIcon.setMinSize(15, 15);
        viewIcon.setMaxSize(15, 15);
        
        viewBtn.setGraphic(viewIcon);
        viewBtn.setTooltip(new Tooltip("View Details"));
        viewBtn.setOnAction(e -> handleViewSaleDetails(saleId));
        
        hbox.getChildren().addAll(editBtn, deleteBtn, viewBtn);
        return hbox;
    }
    
    private void setupPagination() {
        pagination.setPageCount(10); // Set based on total pages in your data
        pagination.setCurrentPageIndex(0);
        pagination.setPageFactory(this::createPage);
    }
    
    private TableView<SaleViewModel.SaleItem> createPage(int pageIndex) {
        // This method would update the table with the correct page of data
        // For now, we'll just use the same data for each page
        return salesTableView;
    }
    
    private void setupButtons() {
        // Add Sale button
        addSaleButton.setOnAction(e -> {
            System.out.println("Add Sale clicked");
            openAddSaleDialog();
        });
        
        // Delete button
        deleteButton.setOnAction(e -> {
            System.out.println("Delete clicked");
            deleteSelectedSales();
        });
        
        // Export CSV button
        exportCSVButton.setOnAction(e -> {
            System.out.println("Export CSV clicked");
            // Export data to CSV
        });
    }
    
    private void openAddSaleDialog() {
        // This would be implemented with a new stage or dialog
        System.out.println("Opening Add Sale dialog");
        
        // Sample implementation (in a real app, you would have a proper dialog)
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add New Sale");
        dialog.setHeaderText("Enter sale details");
        dialog.setContentText("Vehicle ID:");
        
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(vehicleId -> {
            // In a real implementation, you would create a new sale record
            // and add it to the sales table
            System.out.println("Adding sale for vehicle ID: " + vehicleId);
            
            // Refresh the table to show the new sale
            refreshTable();
        });
    }
    
    private void handleEditSale(String saleId) {
        System.out.println("Edit sale with ID: " + saleId);
        // Open edit dialog
    }
    
    private void handleDeleteSale(String saleId) {
        System.out.println("Delete sale with ID: " + saleId);
        
        // Show confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Sale");
        alert.setContentText("Are you sure you want to delete sale " + saleId + "?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Delete the sale
            System.out.println("Sale " + saleId + " deleted");
            
            // Refresh the table
            refreshTable();
        }
    }
    
    private void handleViewSaleDetails(String saleId) {
        System.out.println("View sale details with ID: " + saleId);
        // Open details view
    }
    
    private void deleteSelectedSales() {
        // Get selected sale
        SaleViewModel.SaleItem selectedSale = salesTableView.getSelectionModel().getSelectedItem();
        
        if (selectedSale == null) {
            showInfoAlert("No Selection", "No Sale Selected", "Please select a sale to delete.");
            return;
        }
        
        // Show confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Sale");
        alert.setContentText("Are you sure you want to delete the selected sale?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // In a real implementation, you would delete the sale from the database
            System.out.println("Sale " + selectedSale.getId() + " deleted");
            
            // Refresh the table
            refreshTable();
        }
    }
    
    private void refreshTable() {
        // Get the filtered data from view model based on current filters
        String searchText = searchField.getText().toLowerCase();
        String statusValue = statusFilter.getValue();
        String timeValue = timeFilter.getValue();
        
        ObservableList<SaleViewModel.SaleItem> filteredData = viewModel.searchSales(searchText, statusValue, timeValue);
        
        // Update the table with the filtered data
        salesTableView.setItems(filteredData);
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
        URL fxmlUrl = getClass().getResource("/fxml/sales.fxml");
        if (fxmlUrl == null) {
            System.err.println("ERROR: Cannot find sales.fxml. Check your project structure.");
            throw new IOException("sales.fxml not found");
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
    
    /**
     * Navigate to the Finance page.
     */
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
     * Navigate to the Buyers page
     */
    private void openBuyers() throws IOException {
        // Get the current stage
        Stage currentStage = (Stage) dashboardBtn.getScene().getWindow();
        
        // Load the Buyer FXML
        URL fxmlUrl = getClass().getResource("/fxml/Buyer.fxml");
        if (fxmlUrl == null) {
            System.err.println("ERROR: Cannot find Buyer.fxml. Check your project structure.");
            throw new IOException("Buyer.fxml not found");
        }
        
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        
        // Create a new scene
        Scene scene = new Scene(root, 1400, 900);
        
        // Add the CSS file
        scene.getStylesheets().add(getClass().getResource("/styles/buyer.css").toExternalForm());
        
        // Set the controller as user data in the scene for action buttons access
        scene.setUserData(loader.getController());
        
        // Set the scene to the stage
        currentStage.setScene(scene);
        currentStage.show();
    }
    
    /**
     * Navigate to the Analytics page
     */
    private void openAnalytics() throws IOException {
        // Get the current stage
        Stage currentStage = (Stage) analyticsBtn.getScene().getWindow();
        
        // Load the analytics FXML
        URL fxmlUrl = getClass().getResource("/fxml/Analytics.fxml");
        if (fxmlUrl == null) {
            System.err.println("ERROR: Cannot find Analytics.fxml. Check your project structure.");
            throw new IOException("Analytics.fxml not found");
        }
        
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        
        // Create a new scene
        Scene scene = new Scene(root, 1400, 900);
        
        // Add the CSS file
        URL analyticsCssUrl = getClass().getResource("/styles/analytics.css");
        
        if (analyticsCssUrl != null) {
            scene.getStylesheets().add(analyticsCssUrl.toExternalForm());
        } else {
            System.err.println("WARNING: analytics.css not found");
        }
        
        // Set the scene to the current stage
        currentStage.setScene(scene);
        currentStage.setTitle("Bestun Cars Management System - Analytics");
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