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
    public String getCurrentPageName() {
        return "buyer";
    }
    
    @Override
    public void initializeController() {
        // Initialize the view model
        viewModel = new BuyerViewModel();
        
        // Set up the UI components
        setupFilters();
        setupTableView();
        setupPagination();
        setupButtons();
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
    
    // Method to show an error alert dialog (now using NavigationController)
    private void showErrorAlert(String title, String header, String content) {
        NavigationController.showErrorAlert(title, header, content);
    }
}