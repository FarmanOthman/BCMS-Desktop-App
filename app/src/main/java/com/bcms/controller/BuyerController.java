package com.bcms.controller;

import com.bcms.viewmodel.BuyerViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.util.Optional;

public class BuyerController extends BaseController {
    
    @FXML private TextField searchField;
    // Remove these fields that don't exist in the new FXML
    // @FXML private ComboBox<String> buyerTypeFilter;
    // @FXML private ComboBox<String> statusFilter;
    // @FXML private Button resetButton;
    @FXML private Button addCustomerBtn; // Changed from addBuyerButton to match FXML
    // @FXML private Button deleteButton;
    
    @FXML private TableView<BuyerViewModel.BuyerItem> customersTable; // Changed from buyerTableView to match FXML
    @FXML private TableColumn<BuyerViewModel.BuyerItem, Integer> idColumn;
    @FXML private TableColumn<BuyerViewModel.BuyerItem, String> nameColumn;
    @FXML private TableColumn<BuyerViewModel.BuyerItem, String> emailColumn;
    @FXML private TableColumn<BuyerViewModel.BuyerItem, String> phoneColumn;
    @FXML private TableColumn<BuyerViewModel.BuyerItem, String> addressColumn;
    @FXML private TableColumn<BuyerViewModel.BuyerItem, Integer> purchasesColumn; // Changed from carNameColumn
    @FXML private TableColumn<BuyerViewModel.BuyerItem, HBox> actionsColumn;
    
    // @FXML private Pagination pagination; // Removed as it's not in the new FXML
    
    @FXML private Label totalCustomersLabel;
    @FXML private Label newCustomersLabel;
    @FXML private Label repeatCustomersLabel;
    
    private BuyerViewModel viewModel;
    
    @Override
    public String getCurrentPageName() {
        return "buyer";
    }
    
    @Override
    public void initializeController() {
        // Initialize the view model
        viewModel = new BuyerViewModel();
        
        // Set up the UI components
        // setupFilters(); // Remove as filters have been removed
        setupTableView();
        // setupPagination(); // Remove as pagination has been removed
        setupButtons();
        setupStatLabels();
    }
    
    // Remove setupFilters() method as it's no longer needed
    
    private void setupTableView() {
        // Configure table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        purchasesColumn.setCellValueFactory(new PropertyValueFactory<>("purchases")); // Changed from carName
        actionsColumn.setCellValueFactory(new PropertyValueFactory<>("actions"));
        
        // Load data into the table
        refreshTable();
    }
    
    // Remove setupPagination() method as it's no longer needed
    
    // Remove createPage() method as it's no longer needed
    
    private void setupButtons() {
        // Add Customer button
        addCustomerBtn.setOnAction(e -> {
            System.out.println("Add Customer clicked");
            openAddBuyerDialog();
        });
    }
    
    private void setupStatLabels() {
        // Set up the statistics labels with data from the view model
        totalCustomersLabel.setText(String.valueOf(viewModel.getAllBuyers().size()));
        newCustomersLabel.setText("18"); // Placeholder value
        repeatCustomersLabel.setText("42"); // Placeholder value
    }
    
    private void openAddBuyerDialog() {
        // This would be implemented with a new stage or dialog
        System.out.println("Opening Add Buyer dialog");
        
        // Sample implementation (in a real app, you would have a proper dialog)
        Dialog<BuyerViewModel.BuyerItem> dialog = new Dialog<>();
        dialog.setTitle("Add New Customer");
        dialog.setHeaderText("Enter customer details");
        
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
        TextField purchasesField = new TextField();
        purchasesField.setPromptText("Number of Purchases");
        
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
        grid.add(new Label("Purchases:"), 0, 4);
        grid.add(purchasesField, 1, 4);
        
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
                    Integer.parseInt(purchasesField.getText().isEmpty() ? "0" : purchasesField.getText())
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
            
            // Update stats
            setupStatLabels();
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
        // Get all buyers since filters have been removed
        customersTable.setItems(viewModel.getAllBuyers());
    }
    
    private void deleteSelectedBuyers() {
        // Get selected items
        ObservableList<BuyerViewModel.BuyerItem> selectedItems = customersTable.getSelectionModel().getSelectedItems();
        
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
    
    // Method to show an error alert dialog (now using NavigationController)
    private void showErrorAlert(String title, String header, String content) {
        NavigationController.showErrorAlert(title, header, content);
    }
}