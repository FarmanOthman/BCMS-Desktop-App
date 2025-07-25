package com.bcms.controller;

import com.bcms.model.ActivityLog;
import com.bcms.viewmodel.ActivityViewModel;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

public class ActivityController extends BaseController {
    
    // Page title and subtitle
    @FXML private Label activityTitle;
    @FXML private Label activitySubtitle;
    
    // Filters and search
    @FXML private TextField searchField;
    @FXML private ComboBox<String> severityFilter;
    @FXML private ComboBox<String> userFilter;
    @FXML private ComboBox<String> timeFilter;
    @FXML private Button refreshButton;
    @FXML private Button autoRefreshButton;
    
    // Summary cards
    @FXML private Label todayActivitiesValue;
    @FXML private Label activityChangeValue;
    @FXML private Label criticalEventsValue;
    @FXML private Label criticalEventsDetail;
    @FXML private Label activeUsersValue;
    @FXML private Label activeUsersDetail;
    @FXML private Label systemStatusValue;
    @FXML private Label systemStatusDetail;
    
    // Activity table
    @FXML private TableView<ActivityViewModel.ActivityLogItem> activityTableView;
    @FXML private TableColumn<ActivityViewModel.ActivityLogItem, Integer> idColumn;
    @FXML private TableColumn<ActivityViewModel.ActivityLogItem, ActivityLog.Severity> severityColumn;
    @FXML private TableColumn<ActivityViewModel.ActivityLogItem, String> actionColumn;
    @FXML private TableColumn<ActivityViewModel.ActivityLogItem, String> detailsColumn;
    @FXML private TableColumn<ActivityViewModel.ActivityLogItem, String> userColumn;
    @FXML private TableColumn<ActivityViewModel.ActivityLogItem, String> timestampColumn;
    @FXML private TableColumn<ActivityViewModel.ActivityLogItem, String> ipAddressColumn;
    
    // Pagination
    @FXML private Pagination pagination;
    
    private ActivityViewModel viewModel;
    private Timeline autoRefreshTimeline;
    private boolean autoRefreshEnabled = false;
    
    @Override
    protected String getCurrentPageName() {
        return "activity";
    }
    
    @Override
    protected void initializeController() {
        viewModel = new ActivityViewModel();
        
        setupFilters();
        setupTableView();
        setupPagination();
        setupAutoRefresh();
        updateSummaryCards();
    }
    
    private void setupFilters() {
        // Set up the severity filter
        severityFilter.setItems(FXCollections.observableArrayList(
            "All Severities", "INFO", "WARNING", "ERROR", "SUCCESS"
        ));
        severityFilter.setValue("All Severities");
        
        // Set up the user filter - in a real app, this would be populated with all users
        ObservableList<String> users = FXCollections.observableArrayList("All Users");
        // Add users from activity logs
        viewModel.getAllActivities().stream()
            .map(ActivityViewModel.ActivityLogItem::getUser)
            .distinct()
            .sorted()
            .forEach(users::add);
        userFilter.setItems(users);
        userFilter.setValue("All Users");
        
        // Set up the time filter
        timeFilter.setItems(FXCollections.observableArrayList(
            "Last 7 Days", "Last 24 Hours", "Last 30 Days", "All Time"
        ));
        timeFilter.setValue("Last 7 Days");
        
        // Set up search field
        searchField.setPromptText("Search activities...");
        
        // Add listeners to filters to refresh table when changed
        severityFilter.setOnAction(e -> refreshTable());
        userFilter.setOnAction(e -> refreshTable());
        timeFilter.setOnAction(e -> refreshTable());
        searchField.textProperty().addListener((obs, oldVal, newVal) -> refreshTable());
        
        // Set up refresh button
        refreshButton.setOnAction(e -> {
            System.out.println("Refresh clicked");
            refreshData();
        });
        
        // Set up auto-refresh button
        autoRefreshButton.setOnAction(e -> {
            toggleAutoRefresh();
        });
    }
    
    private void setupTableView() {
        // Configure table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        severityColumn.setCellValueFactory(new PropertyValueFactory<>("severityIcon"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));
        detailsColumn.setCellValueFactory(new PropertyValueFactory<>("details"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("formattedTimestamp"));
        ipAddressColumn.setCellValueFactory(new PropertyValueFactory<>("ipAddress"));
        
        // Load data into the table
        refreshTable();
    }
    
    private void setupPagination() {
        int pageCount = (int) Math.ceil((double) viewModel.getAllActivities().size() / 10);
        pagination.setPageCount(pageCount);
        pagination.setCurrentPageIndex(0);
        pagination.setPageFactory(this::createPage);
    }
    
    private TableView<ActivityViewModel.ActivityLogItem> createPage(int pageIndex) {
        int fromIndex = pageIndex * 10;
        int toIndex = Math.min(fromIndex + 10, viewModel.getAllActivities().size());
        
        ObservableList<ActivityViewModel.ActivityLogItem> pageData = FXCollections.observableArrayList(
            viewModel.getAllActivities().subList(fromIndex, toIndex)
        );
        
        activityTableView.setItems(pageData);
        return activityTableView;
    }
    
    private void setupAutoRefresh() {
        // Create a timeline for auto refresh (every 30 seconds)
        autoRefreshTimeline = new Timeline(
            new KeyFrame(Duration.seconds(30), e -> refreshData())
        );
        autoRefreshTimeline.setCycleCount(Animation.INDEFINITE);
    }
    
    private void toggleAutoRefresh() {
        autoRefreshEnabled = !autoRefreshEnabled;
        
        if (autoRefreshEnabled) {
            // Start auto refresh
            autoRefreshTimeline.play();
            autoRefreshButton.getStyleClass().add("active");
        } else {
            // Stop auto refresh
            autoRefreshTimeline.stop();
            autoRefreshButton.getStyleClass().remove("active");
        }
    }
    
    private void updateSummaryCards() {
        // Update summary card values from the view model
        todayActivitiesValue.setText(String.valueOf(viewModel.getTodayActivities()));
        activityChangeValue.setText(viewModel.getActivityChange() + " from yesterday");
        
        criticalEventsValue.setText(String.valueOf(viewModel.getCriticalEvents()));
        criticalEventsDetail.setText("Requires attention");
        
        activeUsersValue.setText(String.valueOf(viewModel.getActiveUsers()));
        activeUsersDetail.setText("Currently online");
        
        systemStatusValue.setText(viewModel.getSystemStatus());
        systemStatusDetail.setText("All systems operational");
        
        // Apply appropriate style class based on system status
        if ("GOOD".equals(viewModel.getSystemStatus())) {
            systemStatusValue.getStyleClass().add("status-good");
        } else if ("WARNING".equals(viewModel.getSystemStatus())) {
            systemStatusValue.getStyleClass().add("status-warning");
        } else if ("ERROR".equals(viewModel.getSystemStatus())) {
            systemStatusValue.getStyleClass().add("status-error");
        }
    }
    
    private void refreshData() {
        // In a real app, this would fetch new data from the server
        System.out.println("Refreshing data...");
        
        // For now, we'll just refresh the table with existing data
        refreshTable();
        
        // Update the pagination if the number of items has changed
        setupPagination();
        
        // Update the summary cards
        updateSummaryCards();
    }
    
    private void refreshTable() {
        // Apply filters and search
        String searchText = searchField.getText().toLowerCase();
        String severityFilter = this.severityFilter.getValue();
        String userFilter = this.userFilter.getValue();
        
        // Filter activities based on current filter settings
        ObservableList<ActivityViewModel.ActivityLogItem> filteredData = FXCollections.observableArrayList();
        
        for (ActivityViewModel.ActivityLogItem item : viewModel.getAllActivities()) {
            boolean matches = true;
            
            // Apply search filter
            if (!searchText.isEmpty()) {
                matches = item.getAction().toLowerCase().contains(searchText) ||
                         item.getDetails().toLowerCase().contains(searchText) ||
                         item.getUser().toLowerCase().contains(searchText);
            }
            
            // Apply severity filter
            if (!"All Severities".equals(severityFilter)) {
                matches = matches && item.getSeverity().toString().equals(severityFilter);
            }
            
            // Apply user filter
            if (!"All Users".equals(userFilter)) {
                matches = matches && item.getUser().equals(userFilter);
            }
            
            // Apply time filter (simplified - in real app would filter by actual time)
            // For now, we'll just show all data regardless of time filter
            
            if (matches) {
                filteredData.add(item);
            }
        }
        
        // Update table with filtered data
        activityTableView.setItems(filteredData);
        
        // Update pagination
        int pageCount = (int) Math.ceil((double) filteredData.size() / 10);
        pagination.setPageCount(Math.max(1, pageCount));
        pagination.setCurrentPageIndex(0);
    }

    // Clean up resources when controller is no longer needed
    public void cleanup() {
        if (autoRefreshTimeline != null) {
            autoRefreshTimeline.stop();
        }
    }
   
}