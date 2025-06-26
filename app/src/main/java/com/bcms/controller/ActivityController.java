package com.bcms.controller;

import com.bcms.model.ActivityLog;
import com.bcms.viewmodel.ActivityViewModel;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ActivityController implements Initializable {
    
    // Navigation sidebar buttons
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
    public void initialize(URL location, ResourceBundle resources) {
        viewModel = new ActivityViewModel();
        
        setupNavigationHandlers();
        setupFilters();
        setupTableView();
        setupPagination();
        setupAutoRefresh();
        updateSummaryCards();
    }
    
    private void setupNavigationHandlers() {
        // Set Activity button as active
        setActiveButton(activityBtn);
        
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
        
        // Activity button (already active)
        activityBtn.setOnAction(e -> {
            // Already on activity page
            System.out.println("Already on Activity page");
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
        activityBtn.getStyleClass().remove("active");
        settingsBtn.getStyleClass().remove("active");
        userMgmtBtn.getStyleClass().remove("active");
        
        // Add active class to selected button
        activeBtn.getStyleClass().add("active");
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
     * Refresh the activity table based on current filters and search
     */
    private void refreshTable() {
        ObservableList<ActivityViewModel.ActivityLogItem> filteredList = viewModel.getFilteredActivities(
            searchField.getText(),
            severityFilter.getValue(),
            userFilter.getValue(),
            timeFilter.getValue()
        );
        activityTableView.setItems(filteredList);
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
    
    // Clean up resources when controller is no longer needed
    public void cleanup() {
        if (autoRefreshTimeline != null) {
            autoRefreshTimeline.stop();
        }
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