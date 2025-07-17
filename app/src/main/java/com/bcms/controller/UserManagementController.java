package com.bcms.controller;

import com.bcms.viewmodel.UserViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.util.Optional;

public class UserManagementController extends BaseController {
    
    // Search and filter controls
    @FXML private TextField searchField;
    @FXML private ComboBox<String> roleFilter;
    @FXML private ComboBox<String> statusFilter;
    @FXML private Button resetButton;
    
    // Action buttons
    @FXML private Button addUserButton;
    @FXML private Button deleteButton;
    
    // User table view
    @FXML private TableView<UserViewModel.UserItem> userTableView;
    @FXML private TableColumn<UserViewModel.UserItem, Integer> idColumn;
    @FXML private TableColumn<UserViewModel.UserItem, String> nameColumn;
    @FXML private TableColumn<UserViewModel.UserItem, String> emailColumn;
    @FXML private TableColumn<UserViewModel.UserItem, String> roleColumn;
    @FXML private TableColumn<UserViewModel.UserItem, String> statusColumn;
    @FXML private TableColumn<UserViewModel.UserItem, String> lastLoginColumn;
    @FXML private TableColumn<UserViewModel.UserItem, HBox> actionsColumn;
    
    // Pagination
    @FXML private Pagination pagination;
    
    private UserViewModel viewModel;
    
    @Override
    public String getCurrentPageName() {
        return "userManagement";
    }
    
    @Override
    public void initializeController() {
        viewModel = new UserViewModel();
        
        setupFilters();
        setupTableView();
        setupPagination();
        setupButtons();
    }
    private void setupFilters() {
        // Set up the role filter
        roleFilter.setItems(FXCollections.observableArrayList(
            "All Roles", "Admin", "Manager", "Sales Staff", "Finance Staff", "Technician"
        ));
        roleFilter.setValue("All Roles");
        
        // Set up the status filter
        statusFilter.setItems(FXCollections.observableArrayList(
            "All Status", "Active", "Inactive"
        ));
        statusFilter.setValue("All Status");
        
        // Set up search field
        searchField.setPromptText("Search by name or email...");
        
        // Reset button action
        resetButton.setOnAction(e -> {
            searchField.clear();
            roleFilter.setValue("All Roles");
            statusFilter.setValue("All Status");
            refreshTable();
        });
        
        // Add listeners to filters to refresh table when changed
        roleFilter.setOnAction(e -> refreshTable());
        statusFilter.setOnAction(e -> refreshTable());
        searchField.textProperty().addListener((obs, oldVal, newVal) -> refreshTable());
    }
    
    private void setupTableView() {
        // Configure table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        lastLoginColumn.setCellValueFactory(new PropertyValueFactory<>("lastLogin"));
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
                    if (item.equals("Active")) {
                        setStyle("-fx-text-fill: #10b981;"); // Green for active
                    } else if (item.equals("Inactive")) {
                        setStyle("-fx-text-fill: #ef4444;"); // Red for inactive
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
    
    private TableView<UserViewModel.UserItem> createPage(int pageIndex) {
        // This method would update the table with the correct page of data
        // For now, we'll just use the same data for each page
        return userTableView;
    }
    
    private void setupButtons() {
        // Add User button
        addUserButton.setOnAction(e -> {
            System.out.println("Add User clicked");
            openAddUserDialog();
        });
        
        // Delete button
        deleteButton.setOnAction(e -> {
            System.out.println("Delete clicked");
            deleteSelectedUsers();
        });
    }
    
    private void openAddUserDialog() {
        try {
            // Create a dialog
            Dialog<UserViewModel.UserItem> dialog = new Dialog<>();
            dialog.setTitle("Add New User");
            dialog.setHeaderText("Enter user details");
            
            // Set the button types
            ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
            
            // Create the form grid
            javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));
            
            // Create form fields
            TextField nameField = new TextField();
            nameField.setPromptText("Full Name");
            
            TextField emailField = new TextField();
            emailField.setPromptText("Email Address");
            
            PasswordField passwordField = new PasswordField();
            passwordField.setPromptText("Password");
            
            ComboBox<String> roleComboBox = new ComboBox<>();
            roleComboBox.setItems(FXCollections.observableArrayList(
                "Admin", "Manager", "Sales Staff", "Finance Staff", "Technician"
            ));
            roleComboBox.setValue("Sales Staff");
            
            // Add fields to grid
            grid.add(new Label("Name:"), 0, 0);
            grid.add(nameField, 1, 0);
            grid.add(new Label("Email:"), 0, 1);
            grid.add(emailField, 1, 1);
            grid.add(new Label("Password:"), 0, 2);
            grid.add(passwordField, 1, 2);
            grid.add(new Label("Role:"), 0, 3);
            grid.add(roleComboBox, 1, 3);
            
            dialog.getDialogPane().setContent(grid);
            
            // Request focus on the name field by default
            nameField.requestFocus();
            
            // Convert the result to a user when the save button is clicked
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) {
                    // Validate fields
                    if (nameField.getText().isEmpty() || emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                        NavigationController.showErrorAlert("Validation Error", "Missing Required Fields", "All fields are required.");
                        return null;
                    }
                    
                    // Create new user (in a real app, this would also create the user in the database)
                    int newId = viewModel.getMaxId() + 1;
                    UserViewModel.UserItem newUser = new UserViewModel.UserItem(
                        newId,
                        nameField.getText(),
                        emailField.getText(),
                        roleComboBox.getValue(),
                        "Active",
                        "Just now"
                    );
                    
                    // Add the user to the view model
                    viewModel.addUser(newUser);
                    
                    return newUser;
                }
                return null;
            });
            
            // Show the dialog and process the result
            Optional<UserViewModel.UserItem> result = dialog.showAndWait();
            result.ifPresent(user -> {
                System.out.println("New user added: " + user.getName());
                refreshTable();
            });
            
        } catch (Exception ex) {
            ex.printStackTrace();
            NavigationController.showErrorAlert("Error", "Failed to open dialog", ex.getMessage());
        }
    }
    
    private void deleteSelectedUsers() {
        // Get selected user
        UserViewModel.UserItem selectedUser = userTableView.getSelectionModel().getSelectedItem();
        
        if (selectedUser == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Selection");
            alert.setHeaderText("No User Selected");
            alert.setContentText("Please select a user to delete.");
            alert.showAndWait();
            return;
        }
        
        // Show confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete User");
        alert.setContentText("Are you sure you want to delete the user: " + selectedUser.getName() + "?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Delete the user
            boolean success = viewModel.removeUser(selectedUser.getId());
            
            if (success) {
                refreshTable();
            } else {
                NavigationController.showErrorAlert("Delete Error", "Could not delete user", "The user could not be deleted.");
            }
        }
    }
    
    private void refreshTable() {
        // Get the filtered data from view model based on current filters
        String searchText = searchField.getText().toLowerCase();
        String roleValue = roleFilter.getValue();
        String statusValue = statusFilter.getValue();
        
        ObservableList<UserViewModel.UserItem> filteredData = viewModel.getFilteredUsers(searchText, roleValue, statusValue);
        
        // Update the table with the filtered data
        userTableView.setItems(filteredData);
    }
    
    // Action handlers for edit and delete buttons in each row
    @FXML
    public void handleEditUser(int userId) {
        System.out.println("Edit user with ID: " + userId);
        // Open edit dialog
        UserViewModel.UserItem user = viewModel.getUserById(userId);
        if (user != null) {
            openEditUserDialog(user);
        }
    }
    
    private void openEditUserDialog(UserViewModel.UserItem user) {
        try {
            // Create a dialog
            Dialog<UserViewModel.UserItem> dialog = new Dialog<>();
            dialog.setTitle("Edit User");
            dialog.setHeaderText("Edit user details");
            
            // Set the button types
            ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
            
            // Create the form grid
            javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));
            
            // Create form fields with existing values
            TextField nameField = new TextField(user.getName());
            TextField emailField = new TextField(user.getEmail());
            
            ComboBox<String> roleComboBox = new ComboBox<>();
            roleComboBox.setItems(FXCollections.observableArrayList(
                "Admin", "Manager", "Sales Staff", "Finance Staff", "Technician"
            ));
            roleComboBox.setValue(user.getRole());
            
            ComboBox<String> statusComboBox = new ComboBox<>();
            statusComboBox.setItems(FXCollections.observableArrayList(
                "Active", "Inactive"
            ));
            statusComboBox.setValue(user.getStatus());
            
            // Add fields to grid
            grid.add(new Label("Name:"), 0, 0);
            grid.add(nameField, 1, 0);
            grid.add(new Label("Email:"), 0, 1);
            grid.add(emailField, 1, 1);
            grid.add(new Label("Role:"), 0, 2);
            grid.add(roleComboBox, 1, 2);
            grid.add(new Label("Status:"), 0, 3);
            grid.add(statusComboBox, 1, 3);
            
            dialog.getDialogPane().setContent(grid);
            
            // Request focus on the name field by default
            nameField.requestFocus();
            
            // Convert the result to a user when the save button is clicked
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) {
                    // Validate fields
                    if (nameField.getText().isEmpty() || emailField.getText().isEmpty()) {
                        NavigationController.showErrorAlert("Validation Error", "Missing Required Fields", "Name and email are required.");
                        return null;
                    }
                    
                    // Update the user (in a real app, this would also update the user in the database)
                    user.setName(nameField.getText());
                    user.setEmail(emailField.getText());
                    user.setRole(roleComboBox.getValue());
                    user.setStatus(statusComboBox.getValue());
                    
                    return user;
                }
                return null;
            });
            
            // Show the dialog and process the result
            Optional<UserViewModel.UserItem> result = dialog.showAndWait();
            result.ifPresent(updatedUser -> {
                System.out.println("User updated: " + updatedUser.getName());
                refreshTable();
            });
            
        } catch (Exception ex) {
            ex.printStackTrace();
            NavigationController.showErrorAlert("Error", "Failed to open dialog", ex.getMessage());
        }
    }
    
    @FXML
    public void handleDeleteUser(int userId) {
        System.out.println("Delete user with ID: " + userId);
        
        // Show confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete User");
        
        UserViewModel.UserItem user = viewModel.getUserById(userId);
        if (user != null) {
            alert.setContentText("Are you sure you want to delete the user: " + user.getName() + "?");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Delete the user
                boolean success = viewModel.removeUser(userId);
                
                if (success) {
                    refreshTable();
                } else {
                    NavigationController.showErrorAlert("Delete Error", "Could not delete user", "The user could not be deleted.");
                }
            }
        }
    }
}