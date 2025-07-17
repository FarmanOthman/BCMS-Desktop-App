package com.bcms.controller;

import com.bcms.viewmodel.FinanceViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.util.Optional;

public class FinanceController extends BaseController {
    
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
    protected String getCurrentPageName() {
        return "finance";
    }
    
    @Override
    protected void initializeController() {
        viewModel = new FinanceViewModel();
        
        setupFilters();
        setupTableView();
        setupPagination();
        setupButtons();
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
                NavigationController.showErrorAlert("Invalid Input", "Please enter a valid number", "The amount must be a valid numeric value.");
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
                NavigationController.showErrorAlert("Invalid Input", "Please enter a valid number", "The amount must be a valid numeric value.");
            }
        });
    }
    
    private void deleteSelectedRecords() {
        // Get selected record
        FinanceViewModel.FinanceRecord selectedRecord = financeTableView.getSelectionModel().getSelectedItem();
        
        if (selectedRecord == null) {
            NavigationController.showErrorAlert("No Selection", "No Record Selected", "Please select a record to delete.");
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
                NavigationController.showErrorAlert("Delete Error", "Could not delete record", "The record could not be deleted.");
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
}