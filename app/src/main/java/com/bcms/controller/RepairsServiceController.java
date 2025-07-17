package com.bcms.controller;

import com.bcms.model.RepairService;
import com.bcms.viewmodel.RepairsServiceViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDate;

public class RepairsServiceController extends BaseController {
    
    // Action Buttons
    @FXML private Button addRepairButton;
    @FXML private Button exportButton;
    @FXML private Button resetButton;
    
    // Filters
    @FXML private TextField searchField;
    @FXML private ComboBox<String> statusFilter;
    @FXML private ComboBox<String> dateRangeFilter;
    
    // Table View
    @FXML private TableView<RepairsServiceViewModel.RepairItem> repairsTableView;
    @FXML private TableColumn<RepairsServiceViewModel.RepairItem, Boolean> selectColumn;
    @FXML private TableColumn<RepairsServiceViewModel.RepairItem, String> repairIdColumn;
    @FXML private TableColumn<RepairsServiceViewModel.RepairItem, String> carInfoColumn;
    @FXML private TableColumn<RepairsServiceViewModel.RepairItem, String> repairTypeColumn;
    @FXML private TableColumn<RepairsServiceViewModel.RepairItem, String> statusColumn;
    @FXML private TableColumn<RepairsServiceViewModel.RepairItem, String> technicianColumn;
    @FXML private TableColumn<RepairsServiceViewModel.RepairItem, LocalDate> startDateColumn;
    @FXML private TableColumn<RepairsServiceViewModel.RepairItem, Double> costColumn;
    @FXML private TableColumn<RepairsServiceViewModel.RepairItem, HBox> actionsColumn;
    
    @FXML private Pagination pagination;
    
    // ViewModel reference
    private RepairsServiceViewModel viewModel;
    
    @Override
    public String getCurrentPageName() {
        return "repairsService";
    }
    
    @Override
    public void initializeController() {
        viewModel = new RepairsServiceViewModel();
        
        // Set the handler for edit repair actions
        viewModel.setEditRepairHandler(this::handleEditRepair);
        
        setupFilters();
        setupTableView();
        setupPagination();
        setupButtons();
        
        // Load sample data
        loadRepairData();
    }
    private void setupFilters() {
        // Set up the status filter
        statusFilter.setItems(FXCollections.observableArrayList(
            "All Statuses", "In Progress", "Completed", "Pending"
        ));
        statusFilter.setValue("All Statuses");
        
        // Set up the date range filter
        dateRangeFilter.setItems(FXCollections.observableArrayList(
            "All Time", "Last 7 Days", "Last 30 Days", "Last 90 Days", "This Year"
        ));
        dateRangeFilter.setValue("All Time");
        
        // Setup search field listener
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Searching for: " + newValue);
            // Implement search functionality
            viewModel.filterBySearch(newValue);
        });
        
        // Setup status filter listener
        statusFilter.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Filter by status: " + newValue);
            // Implement status filtering
            viewModel.filterByStatus(newValue);
        });
        
        // Setup date range filter listener
        dateRangeFilter.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Filter by date range: " + newValue);
            // Implement date range filtering
            viewModel.filterByDateRange(newValue);
        });
        
        // Reset button action
        resetButton.setOnAction(e -> {
            searchField.clear();
            statusFilter.setValue("All Statuses");
            dateRangeFilter.setValue("All Time");
            viewModel.resetFilters();
        });
    }
    
    private void setupTableView() {
        // Initialize Table columns
        repairIdColumn.setCellValueFactory(new PropertyValueFactory<>("repairId"));
        carInfoColumn.setCellValueFactory(new PropertyValueFactory<>("carInfo"));
        repairTypeColumn.setCellValueFactory(new PropertyValueFactory<>("repairType"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        technicianColumn.setCellValueFactory(new PropertyValueFactory<>("technician"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        
        // Set up checkbox column
        selectColumn.setCellValueFactory(new PropertyValueFactory<>("selected"));
        selectColumn.setCellFactory(col -> new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();
            
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    RepairsServiceViewModel.RepairItem repair = getTableView().getItems().get(getIndex());
                    checkBox.setSelected(repair.isSelected());
                    checkBox.setOnAction(e -> repair.setSelected(checkBox.isSelected()));
                    setGraphic(checkBox);
                }
            }
        });
        
        // Setup actions column
        actionsColumn.setCellFactory(col -> new TableCell<>() {
            private final Button viewBtn = new Button("View");
            private final Button editBtn = new Button("Edit");
            
            {
                viewBtn.getStyleClass().add("table-button");
                editBtn.getStyleClass().add("table-button");
                
                viewBtn.setOnAction(e -> {
                    RepairsServiceViewModel.RepairItem repair = getTableRow().getItem();
                    if (repair != null) {
                        handleViewRepair(repair);
                    }
                });
                
                editBtn.setOnAction(e -> {
                    RepairsServiceViewModel.RepairItem repair = getTableRow().getItem();
                    if (repair != null) {
                        handleEditRepair(repair);
                    }
                });
            }
            
            @Override
            protected void updateItem(HBox item, boolean empty) {
                super.updateItem(item, empty);
                
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5, viewBtn, editBtn);
                    buttons.getStyleClass().add("action-buttons");
                    setGraphic(buttons);
                }
            }
        });
        
        // Bind table view to the view model
        repairsTableView.setItems(viewModel.getRepairsList());
    }
    
    private void setupPagination() {
        // Set up pagination
        int itemsPerPage = 10;
        pagination.setPageCount((viewModel.getRepairsList().size() / itemsPerPage) + 1);
        pagination.setCurrentPageIndex(0);
        
        pagination.setPageFactory(pageIndex -> {
            int fromIndex = pageIndex * itemsPerPage;
            int toIndex = Math.min(fromIndex + itemsPerPage, viewModel.getRepairsList().size());
            
            repairsTableView.setItems(FXCollections.observableArrayList(
                viewModel.getRepairsList().subList(fromIndex, toIndex)));
            
            return repairsTableView;
        });
    }
    
    private void setupButtons() {
        // Add Repair Button
        addRepairButton.setOnAction(e -> openNewRepairDialog());
        
        // Export Button
        exportButton.setOnAction(e -> {
            System.out.println("Export data to CSV/Excel");
            // Implement export functionality
        });
    }
    
    // Method to load repair data (in a real application, this would come from a database)
    private void loadRepairData() {
        viewModel.loadSampleData();
        setupPagination(); // Refresh pagination after loading data
    }
    
    private void handleViewRepair(RepairsServiceViewModel.RepairItem repair) {
        System.out.println("View repair details for: " + repair.getRepairId());
        // Implement view repair details functionality
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Repair Details");
        alert.setHeaderText("Repair ID: " + repair.getRepairId());
        alert.setContentText("Vehicle: " + repair.getCarInfo() + "\n" +
                           "Type: " + repair.getRepairType() + "\n" +
                           "Status: " + repair.getStatus() + "\n" +
                           "Technician: " + repair.getTechnician() + "\n" +
                           "Start Date: " + repair.getStartDate() + "\n" +
                           "Cost: $" + repair.getCost());
        alert.showAndWait();
    }
    
    private void handleEditRepair(RepairsServiceViewModel.RepairItem repair) {
        System.out.println("Edit repair: " + repair.getRepairId());
        // Implement edit repair functionality
        try {
            openEditRepairDialog(repair);
        } catch (IOException ex) {
            ex.printStackTrace();
            NavigationController.showErrorAlert("Error", "Could not open edit repair dialog", ex.getMessage());
        }
    }
    
    private void openNewRepairDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NewRepairDialog.fxml"));
            Parent root = loader.load();
            
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.DECORATED);
            dialogStage.setTitle("New Repair");
            dialogStage.setScene(new Scene(root));
            dialogStage.setResizable(false);
            
            // Show the dialog and wait for it to close
            dialogStage.showAndWait();
            
            // Refresh data after dialog closes
            loadRepairData();
            
        } catch (IOException e) {
            e.printStackTrace();
            NavigationController.showErrorAlert("Error", "Could not open new repair dialog", e.getMessage());
        }
    }
    
    private void openEditRepairDialog(RepairsServiceViewModel.RepairItem repair) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditRepairDialog.fxml"));
        Parent root = loader.load();
        
        // Get the controller and pass the repair data
        //EditRepairDialogController controller = loader.getController();
        //controller.initData(repair);
        
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initStyle(StageStyle.DECORATED);
        dialogStage.setTitle("Edit Repair - " + repair.getRepairId());
        dialogStage.setScene(new Scene(root));
        dialogStage.setResizable(false);
        
        // Show the dialog and wait for it to close
        dialogStage.showAndWait();
        
        // Refresh data after dialog closes
        loadRepairData();
    }
    
   
}
