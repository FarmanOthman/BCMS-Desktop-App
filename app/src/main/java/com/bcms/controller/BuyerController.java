package com.bcms.controller;

import com.bcms.model.Buyer;
import com.bcms.viewmodel.BuyerViewModel;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class BuyerController implements Initializable {
    
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
    @FXML private ComboBox<String> buyerTypeFilter;
    @FXML private ComboBox<String> statusFilter;
    @FXML private Button resetButton;
    @FXML private Button addBuyerButton;
    @FXML private Button deleteButton;
    
    @FXML private TableView<BuyerViewModel.BuyerItem> buyerTableView;
    @FXML private TableColumn<BuyerViewModel.BuyerItem, Integer> idColumn;
    @FXML private TableColumn<BuyerViewModel.BuyerItem, String> nameColumn;
    @FXML private TableColumn<BuyerViewModel.BuyerItem, String> emailColumn;
    @FXML private TableColumn<BuyerViewModel.BuyerItem, String> phoneColumn;
    @FXML private TableColumn<BuyerViewModel.BuyerItem, String> addressColumn;
    @FXML private TableColumn<BuyerViewModel.BuyerItem, String> carNameColumn;
    @FXML private TableColumn<BuyerViewModel.BuyerItem, HBox> actionsColumn;
    
    @FXML private Pagination pagination;
    
    private BuyerViewModel viewModel;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        viewModel = new BuyerViewModel();
        
        setupNavigationHandlers();
        setupFilters();
        setupTableView();
        setupPagination();
        setupButtons();
    }
    
    private void setupNavigationHandlers() {
        // Set Customers/Buyers button as active
        setActiveButton(customersBtn);
        
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
        
        // Customers button (already active)
        customersBtn.setOnAction(e -> System.out.println("Customers clicked"));
        
        // Analytics button
        analyticsBtn.setOnAction(e -> {
            setActiveButton(analyticsBtn);
            // Navigate to analytics view
            System.out.println("Navigate to Analytics");
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
        // Set up the buyer type filter
        buyerTypeFilter.setItems(FXCollections.observableArrayList(
            "All Buyers", "New Buyers", "Returning Buyers"
        ));
        buyerTypeFilter.setValue("All Buyers");
        
        // Set up the status filter
        statusFilter.setItems(FXCollections.observableArrayList(
            "Active Only", "Inactive", "All Status"
        ));
        statusFilter.setValue("Active Only");
        
        // Set up search field
        searchField.setPromptText("Search by name or email...");
        
        // Reset button action
        resetButton.setOnAction(e -> {
            searchField.clear();
            buyerTypeFilter.setValue("All Buyers");
            statusFilter.setValue("Active Only");
            refreshTable();
        });
        
        // Add listeners to filters to refresh table when changed
        buyerTypeFilter.setOnAction(e -> refreshTable());
        statusFilter.setOnAction(e -> refreshTable());
        searchField.textProperty().addListener((obs, oldVal, newVal) -> refreshTable());
    }
    
    private void setupTableView() {
        // Configure table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        carNameColumn.setCellValueFactory(new PropertyValueFactory<>("carName"));
        actionsColumn.setCellValueFactory(new PropertyValueFactory<>("actions"));
        
        // Load data into the table
        refreshTable();
    }
    
    private void setupPagination() {
        pagination.setPageCount(10); // Set based on total pages in your data
        pagination.setCurrentPageIndex(0);
        pagination.setPageFactory(this::createPage);
    }
    
    private TableView<BuyerViewModel.BuyerItem> createPage(int pageIndex) {
        // This method would update the table with the correct page of data
        // For now, we'll just use the same data for each page
        return buyerTableView;
    }
    
    private void setupButtons() {
        // Add Buyer button
        addBuyerButton.setOnAction(e -> {
            System.out.println("Add Buyer clicked");
            openAddBuyerDialog();
        });
        
        // Delete button
        deleteButton.setOnAction(e -> {
            System.out.println("Delete clicked");
            deleteSelectedBuyers();
        });
    }
    
    private void openAddBuyerDialog() {
        // This would be implemented with a new stage or dialog
        System.out.println("Opening Add Buyer dialog");
        
        // Sample implementation (in a real app, you would have a proper dialog)
        Dialog<BuyerViewModel.BuyerItem> dialog = new Dialog<>();
        dialog.setTitle("Add New Buyer");
        dialog.setHeaderText("Enter buyer details");
        
        // Set the button types
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        // Create the form fields
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone");
        TextField addressField = new TextField();
        addressField.setPromptText("Address");
        TextField carField = new TextField();
        carField.setPromptText("Car Name/Model");
        
        // Layout the dialog
        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));
        
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Phone:"), 0, 2);
        grid.add(phoneField, 1, 2);
        grid.add(new Label("Address:"), 0, 3);
        grid.add(addressField, 1, 3);
        grid.add(new Label("Car:"), 0, 4);
        grid.add(carField, 1, 4);
        
        dialog.getDialogPane().setContent(grid);
        
        // Request focus on the name field by default
        nameField.requestFocus();
        
        // Convert the result to a buyer item when the save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                // Generate new ID (in a real app, this would be handled by a service)
                int newId = viewModel.getAllBuyers().size() + 1;
                
                // Create new buyer record
                return new BuyerViewModel.BuyerItem(
                    newId,
                    nameField.getText(),
                    emailField.getText(),
                    phoneField.getText(),
                    addressField.getText(),
                    carField.getText()
                );
            }
            return null;
        });
        
        Optional<BuyerViewModel.BuyerItem> result = dialog.showAndWait();
        
        result.ifPresent(buyer -> {
            // Add the record to view model
            viewModel.addBuyer(buyer);
            
            // Refresh the table
            refreshTable();
        });
    }
    
    private void handleEditBuyer(int buyerId) {
        System.out.println("Edit buyer with ID: " + buyerId);
        
        // Find the buyer in the view model
        BuyerViewModel.BuyerItem buyer = viewModel.getBuyerById(buyerId);
        
        if (buyer == null) {
            showErrorAlert("Edit Error", "Could not find buyer", "The buyer with ID " + buyerId + " could not be found.");
            return;
        }
        
        // Create and configure the dialog
        Dialog<BuyerViewModel.BuyerItem> dialog = new Dialog<>();
        dialog.setTitle("Edit Buyer");
        dialog.setHeaderText("Edit buyer details");
        
        // Set the button types
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        // Create the form fields with current buyer data
        TextField nameField = new TextField(buyer.getName());
        TextField emailField = new TextField(buyer.getEmail());
        TextField phoneField = new TextField(buyer.getPhone());
        TextField addressField = new TextField(buyer.getAddress());
        TextField carField = new TextField(buyer.getCarName());
        
        // Layout the dialog
        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));
        
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Phone:"), 0, 2);
        grid.add(phoneField, 1, 2);
        grid.add(new Label("Address:"), 0, 3);
        grid.add(addressField, 1, 3);
        grid.add(new Label("Car:"), 0, 4);
        grid.add(carField, 1, 4);
        
        dialog.getDialogPane().setContent(grid);
        
        // Request focus on the name field by default
        nameField.requestFocus();
        
        // Convert the result to a buyer item when the save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                // Update the buyer with edited values
                buyer.setName(nameField.getText());
                buyer.setEmail(emailField.getText());
                buyer.setPhone(phoneField.getText());
                buyer.setAddress(addressField.getText());
                buyer.setCarName(carField.getText());
                return buyer;
            }
            return null;
        });
        
        Optional<BuyerViewModel.BuyerItem> result = dialog.showAndWait();
        
        result.ifPresent(updatedBuyer -> {
            // Update the record in view model
            viewModel.updateBuyer(updatedBuyer);
            
            // Refresh the table
            refreshTable();
        });
    }
    
    private void handleDeleteBuyer(int buyerId) {
        System.out.println("Delete buyer with ID: " + buyerId);
        
        // Confirm deletion
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Delete");
        confirmDialog.setHeaderText("Delete Buyer");
        confirmDialog.setContentText("Are you sure you want to delete this buyer?");
        
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Delete the buyer
            boolean success = viewModel.removeBuyer(buyerId);
            
            if (success) {
                // Refresh the table
                refreshTable();
            } else {
                showErrorAlert("Delete Error", "Could not delete buyer", 
                    "The buyer with ID " + buyerId + " could not be deleted.");
            }
        }
    }
    
    private void refreshTable() {
        // Get the filtered data from view model based on current filters
        String searchText = searchField.getText() != null ? searchField.getText().toLowerCase() : "";
        String buyerTypeFilterValue = buyerTypeFilter.getValue();
        String statusFilterValue = statusFilter.getValue();
        
        buyerTableView.setItems(viewModel.getFilteredBuyers(searchText, buyerTypeFilterValue, statusFilterValue));
    }
    
    private void deleteSelectedBuyers() {
        // Get selected items
        ObservableList<BuyerViewModel.BuyerItem> selectedItems = buyerTableView.getSelectionModel().getSelectedItems();
        
        if (selectedItems == null || selectedItems.isEmpty()) {
            showErrorAlert("Delete Error", "No buyers selected", "Please select one or more buyers to delete.");
            return;
        }
        
        // Ask for confirmation
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Delete");
        confirmDialog.setHeaderText("Delete Selected Buyers");
        confirmDialog.setContentText("Are you sure you want to delete the selected buyers? This action cannot be undone.");
        
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Delete all selected buyers
            boolean allDeleted = true;
            for (BuyerViewModel.BuyerItem buyer : selectedItems) {
                if (!viewModel.removeBuyer(buyer.getId())) {
                    allDeleted = false;
                }
            }
            
            // Show result
            if (!allDeleted) {
                showErrorAlert("Delete Error", "Deletion incomplete", "Some buyers could not be deleted.");
            }
            
            // Refresh the table
            refreshTable();
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
    
    // Method to show an error alert dialog
    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
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