package com.bcms.controller;

import com.bcms.model.Car;
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
    @FXML private Button financeBtn; // ADDED: Finance button
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
        
        // Set the handler for edit car actions
        viewModel.setEditCarHandler(this::handleEditCar);
        
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
                System.err.println("Error navigating to Sales: " + ex.getMessage());
                
                // Show alert dialog
                Alert alert = new Alert(Alert.AlertType.ERROR, 
                    "Could not navigate to Sales page. Error: " + ex.getMessage());
                alert.setHeaderText("Navigation Error");
                alert.showAndWait();
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
        
        // ADDED: Finance button
        financeBtn.setOnAction(e -> {
            try {
                System.out.println("Finance button clicked - Navigating to finance page");
                setActiveButton(financeBtn);
                openFinance();
            } catch (Exception ex) {
                System.err.println("Error navigating to finance:");
                ex.printStackTrace();
            }
        });
        
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
        financeBtn.getStyleClass().remove("active"); // ADDED: Finance button
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
        // Example implementation would go here
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
    
    /**
     * Refresh the table view after changes.
     */
    private void refreshTableView() {
        // In a real application, this would fetch fresh data from your data source
        // For now, we'll just simulate a refresh by using the current mock data
        refreshTable();
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
    
    // ADDED: Method to open Finance page
    private void openFinance() throws IOException {
        // Get the current stage
        Stage currentStage = (Stage) financeBtn.getScene().getWindow();
        
        // Load the finance FXML
        URL fxmlUrl = getClass().getResource("/fxml/finance.fxml");
        if (fxmlUrl == null) {
            System.err.println("ERROR: Cannot find Finance.fxml. Check your project structure.");
            throw new IOException("Finance.fxml not found");
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
        Stage currentStage = (Stage) dashboardBtn.getScene().getWindow();
        
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
    
    /**
     * Show an error alert dialog
     */
    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    // Action handlers for edit, delete, and view buttons in each row
    @FXML
    private void handleEditCar(String carId) {
        System.out.println("Edit car with ID: " + carId);
        try {
            // Find the car to edit
            InventoryViewModel.CarItem selectedCar = null;
            for (InventoryViewModel.CarItem car : carTableView.getItems()) {
                if (car.getId().equals(carId)) {
                    selectedCar = car;
                    break;
                }
            }
            
            if (selectedCar == null) {
                System.err.println("Car with ID " + carId + " not found");
                showErrorAlert("Error", "Car Not Found", "The selected car (ID: " + carId + ") could not be found.");
                return;
            }
            
            // Load the Edit Car Dialog FXML
            URL fxmlUrl = getClass().getResource("/fxml/EditCarDialog.fxml");
            if (fxmlUrl == null) {
                System.err.println("Could not find EditCarDialog.fxml");
                showErrorAlert("Error", "Resource Not Found", "The EditCarDialog.fxml file could not be found.");
                return;
            }
            
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent dialogRoot;
            
            try {
                dialogRoot = loader.load();
            } catch (Exception e) {
                System.err.println("Error loading EditCarDialog.fxml: " + e.getMessage());
                e.printStackTrace();
                showErrorAlert("Error", "Dialog Load Error", 
                               "There was an error loading the Edit Car dialog: " + e.getMessage());
                return;
            }
            
            // Get the controller and set the car data
            EditCarDialogController controller = loader.getController();
            
            // Create a dummy Car object with data from the CarItem for demonstration
            // In a real app, you'd fetch the complete car data from your data service
            Car carToEdit = new Car();
            carToEdit.setId(selectedCar.getId());
            carToEdit.setMake(selectedCar.getVehicle().split(" ")[0]);  // Assuming "Make Model"
            
            // Handle the model name safely in case there's no space in the vehicle string
            String[] vehicleParts = selectedCar.getVehicle().split(" ", 2);
            String modelName = vehicleParts.length > 1 ? vehicleParts[1] : "";
            carToEdit.setModel(modelName);
            
            carToEdit.setYear(selectedCar.getYear());
            carToEdit.setPrice(selectedCar.getPrice());
            carToEdit.setStatus(selectedCar.getStatus());
            carToEdit.setVin(selectedCar.getVin());
            
            // Set the car to edit in the controller
            controller.setCar(carToEdit);
            
            // Create and configure the dialog stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Car");
            dialogStage.initOwner(carTableView.getScene().getWindow());
            dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL); // Block input to other windows
            dialogStage.setResizable(false); // Prevent resizing
            
            // Set up the scene with proper styling
            Scene scene = new Scene(dialogRoot);
            scene.getStylesheets().add(getClass().getResource("/styles/editcardialog.css").toExternalForm());
            dialogStage.setScene(scene);
            
            // Show the dialog and wait for it to close
            dialogStage.showAndWait();
            
            // Check if changes should be committed
            if (controller.isCommitChanges()) {
                Car updatedCar = controller.getUpdatedCar();
                System.out.println("Car updated: " + updatedCar.getMake() + " " + updatedCar.getModel());
                // In a real application, you would save the changes to your data source
                
                // Update the table view
                refreshTableView();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Error", "Could not open Edit Car dialog", e.getMessage());
        }
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
}