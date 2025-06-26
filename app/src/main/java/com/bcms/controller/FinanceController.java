package com.bcms.controller;

import com.bcms.viewmodel.FinanceViewModel;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class FinanceController implements Initializable {
    
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
    @FXML private ComboBox<String> typeFilter;
    @FXML private ComboBox<String> timeFilter;
    @FXML private Button resetButton;
    @FXML private Button addExpenseButton;
    @FXML private Button addIncomeButton;
    @FXML private Button deleteButton;
    
    @FXML private TableView<FinanceViewModel.FinanceRecord> financeTableView;
    @FXML private TableColumn<FinanceViewModel.FinanceRecord, String> idColumn;
    @FXML private TableColumn<FinanceViewModel.FinanceRecord, String> typeColumn;
    @FXML private TableColumn<FinanceViewModel.FinanceRecord, String> categoryColumn;
    @FXML private TableColumn<FinanceViewModel.FinanceRecord, String> amountColumn;
    @FXML private TableColumn<FinanceViewModel.FinanceRecord, String> descriptionColumn;
    @FXML private TableColumn<FinanceViewModel.FinanceRecord, String> dateColumn;
    @FXML private TableColumn<FinanceViewModel.FinanceRecord, HBox> actionsColumn;
    
    @FXML private Pagination pagination;
    
    private FinanceViewModel viewModel;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewModel = new FinanceViewModel();
        
        setupNavigationHandlers();
        setupFilters();
        setupTableView();
        setupPagination();
        setupButtons();
    }
    
    private void setupNavigationHandlers() {
        // Set Finance button as active
        setActiveButton(financeBtn);
        
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
                System.out.println("Sales button clicked - Navigating to sales page");
            } catch (IOException ex) {
                ex.printStackTrace();
                showErrorAlert("Navigation Error", "Could not open Sales", ex.getMessage());
            }
        });
        
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
        
        // Finance button (already active)
        financeBtn.setOnAction(e -> System.out.println("Finance clicked"));
        
        // Activity button
        activityBtn.setOnAction(e -> {
            try {
                setActiveButton(activityBtn);
                openActivity();
            } catch (IOException ex) {
                ex.printStackTrace();
                showErrorAlert("Navigation Error", "Could not open Activity", ex.getMessage());
            }
        });
        
        // Settings button
        settingsBtn.setOnAction(e -> {
            setActiveButton(settingsBtn);
            // Navigate to settings view
            System.out.println("Navigate to Settings");
        });
        
        // User Management button
        userMgmtBtn.setOnAction(e -> {
            try {
                setActiveButton(userMgmtBtn);
                openUserManagement();
            } catch (Exception ex) {
                ex.printStackTrace();
                showErrorAlert("Navigation Error", "Could not open User Management", ex.getMessage());
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
        settingsBtn.getStyleClass().remove("active");
        userMgmtBtn.getStyleClass().remove("active");
        
        // Add active class to selected button
        activeBtn.getStyleClass().add("active");
    }
    
    private void setupFilters() {
        // Set up the type filter
        typeFilter.setItems(FXCollections.observableArrayList(
            "All Type", "Expense", "Income"
        ));
        typeFilter.setValue("All Type");
        
        // Set up the time filter
        timeFilter.setItems(FXCollections.observableArrayList(
            "All Time", "Today", "This Week", "This Month", "This Year"
        ));
        timeFilter.setValue("All Time");
        
        // Set up search field
        searchField.setPromptText("Search by Category or Description");
        
        // Reset button action
        resetButton.setOnAction(e -> {
            searchField.clear();
            typeFilter.setValue("All Type");
            timeFilter.setValue("All Time");
            refreshTable();
        });
        
        // Add listeners to filters to refresh table when changed
        typeFilter.setOnAction(e -> refreshTable());
        timeFilter.setOnAction(e -> refreshTable());
        searchField.textProperty().addListener((obs, oldVal, newVal) -> refreshTable());
    }
    
    private void setupTableView() {
        // Configure table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("typeString"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("formattedAmount"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("formattedDate"));
        actionsColumn.setCellValueFactory(new PropertyValueFactory<>("actions"));
        
        // Style the type column based on value
        typeColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    setText(item);
                    
                    // Apply styles based on type
                    if (item.equals("Expense")) {
                        getStyleClass().add("expense-type");
                    } else if (item.equals("Income")) {
                        getStyleClass().add("income-type");
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
    
    private TableView<FinanceViewModel.FinanceRecord> createPage(int pageIndex) {
        // This method would update the table with the correct page of data
        // For now, we'll just use the same data for each page
        return financeTableView;
    }
    
    private void setupButtons() {
        // Add Expense button
        addExpenseButton.setOnAction(e -> {
            System.out.println("Add Expense clicked");
            openAddExpenseDialog();
        });
        
        // Add Income button
        addIncomeButton.setOnAction(e -> {
            System.out.println("Add Income clicked");
            openAddIncomeDialog();
        });
        
        // Delete button
        deleteButton.setOnAction(e -> {
            System.out.println("Delete clicked");
            deleteSelectedRecords();
        });
    }
    
    private void openAddExpenseDialog() {
        // This would be implemented with a new stage or dialog
        System.out.println("Opening Add Expense dialog");
        
        // Sample implementation (in a real app, you would have a proper dialog)
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Expense");
        dialog.setHeaderText("Enter expense details");
        dialog.setContentText("Amount:");
        
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(amountStr -> {
            try {
                double amount = Double.parseDouble(amountStr);
                
                // Generate new ID (in a real app, this would be handled by a service)
                String newId = "#F" + String.format("%03d", viewModel.getAllRecords().size() + 1);
                
                // Create new expense record
                FinanceViewModel.FinanceRecord newRecord = new FinanceViewModel.FinanceRecord(
                    newId,
                    FinanceViewModel.RecordType.EXPENSE,
                    "New Expense",
                    amount,
                    "New expense added",
                    LocalDate.now());
                
                // Add the record to view model
                viewModel.addRecord(newRecord);
                
                // Refresh the table
                refreshTable();
            } catch (NumberFormatException ex) {
                showErrorAlert("Invalid Input", "Please enter a valid number", "The amount must be a valid numeric value.");
            }
        });
    }
    
    private void openAddIncomeDialog() {
        // This would be implemented with a new stage or dialog
        System.out.println("Opening Add Income dialog");
        
        // Sample implementation (in a real app, you would have a proper dialog)
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Income");
        dialog.setHeaderText("Enter income details");
        dialog.setContentText("Amount:");
        
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(amountStr -> {
            try {
                double amount = Double.parseDouble(amountStr);
                
                // Generate new ID (in a real app, this would be handled by a service)
                String newId = "#F" + String.format("%03d", viewModel.getAllRecords().size() + 1);
                
                // Create new income record
                FinanceViewModel.FinanceRecord newRecord = new FinanceViewModel.FinanceRecord(
                    newId,
                    FinanceViewModel.RecordType.INCOME,
                    "New Income",
                    amount,
                    "New income added",
                    LocalDate.now());
                
                // Add the record to view model
                viewModel.addRecord(newRecord);
                
                // Refresh the table
                refreshTable();
            } catch (NumberFormatException ex) {
                showErrorAlert("Invalid Input", "Please enter a valid number", "The amount must be a valid numeric value.");
            }
        });
    }
    
    private void deleteSelectedRecords() {
        // Get selected record
        FinanceViewModel.FinanceRecord selectedRecord = financeTableView.getSelectionModel().getSelectedItem();
        
        if (selectedRecord == null) {
            showInfoAlert("No Selection", "No Record Selected", "Please select a record to delete.");
            return;
        }
        
        // Show confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Record");
        alert.setContentText("Are you sure you want to delete the selected record?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Delete the record
            boolean success = viewModel.removeRecord(selectedRecord.getId());
            
            if (success) {
                refreshTable();
            } else {
                showErrorAlert("Delete Error", "Could not delete record", "The record could not be deleted.");
            }
        }
    }
    
    private void refreshTable() {
        // Get the filtered data from view model based on current filters
        String searchText = searchField.getText();
        String typeValue = typeFilter.getValue();
        String timeValue = timeFilter.getValue();
        
        ObservableList<FinanceViewModel.FinanceRecord> filteredData = viewModel.getFilteredRecords(searchText, typeValue, timeValue);
        
        // Update the table with the filtered data
        financeTableView.setItems(filteredData);
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
    
    // ADDED: Method to open Sales page
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
    
    // Method to show an error alert dialog
    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Method to show an info alert dialog
    private void showInfoAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void openUserManagement() throws IOException {
        // Get the current stage
        Stage currentStage = (Stage) userMgmtBtn.getScene().getWindow();
        
        // Load the user management FXML
        URL fxmlUrl = getClass().getResource("/fxml/UserManagement.fxml");
        if (fxmlUrl == null) {
            System.err.println("ERROR: Cannot find UserManagement.fxml. Check your project structure.");
            throw new IOException("UserManagement.fxml not found");
        }
        
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();
        
        // Create a new scene
        Scene scene = new Scene(root, 1400, 900);
        
        // Get controller
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
        
        // Set the scene to the current stage
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
}