package com.bcms.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Centralized sidebar controller that handles navigation for all pages
 */
public class SidebarController implements Initializable {
    
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
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupNavigationHandlers();
    }
    
    /**
     * Sets which page is currently active
     */
    public void setActivePage(String pageName) {
        setActiveButton(getButtonByPageName(pageName));
    }
    
    /**
     * Gets the button corresponding to a page name
     */
    private Button getButtonByPageName(String pageName) {
        switch (pageName.toLowerCase()) {
            case "dashboard": return dashboardBtn;
            case "inventory": return inventoryBtn;
            case "repairs": 
            case "repairsservice": return repairsBtn;
            case "sales": return salesBtn;
            case "customers": 
            case "buyers": return customersBtn;
            case "analytics": return analyticsBtn;
            case "finance": return financeBtn;
            case "activity": return activityBtn;
            case "settings": return settingsBtn;
            case "usermanagement": 
            case "usermgmt": return userMgmtBtn;
            default: return dashboardBtn;
        }
    }
    
    /**
     * Sets up navigation event handlers for all sidebar buttons
     */
    private void setupNavigationHandlers() {
        // Dashboard button
        dashboardBtn.setOnAction(e -> {
            try {
                setActiveButton(dashboardBtn);
                NavigationController.openDashboard(dashboardBtn);
            } catch (Exception ex) {
                ex.printStackTrace();
                NavigationController.showErrorAlert("Navigation Error", "Could not open Dashboard", ex.getMessage());
            }
        });
        
        // Inventory button
        inventoryBtn.setOnAction(e -> {
            try {
                setActiveButton(inventoryBtn);
                NavigationController.openInventory(inventoryBtn);
            } catch (Exception ex) {
                ex.printStackTrace();
                NavigationController.showErrorAlert("Navigation Error", "Could not open Inventory", ex.getMessage());
            }
        });
        
        // Repairs button
        repairsBtn.setOnAction(e -> {
            try {
                setActiveButton(repairsBtn);
                NavigationController.openRepairsService(repairsBtn);
            } catch (Exception ex) {
                ex.printStackTrace();
                NavigationController.showErrorAlert("Navigation Error", "Could not open Repairs & Service", ex.getMessage());
            }
        });
        
        // Sales button
        salesBtn.setOnAction(e -> {
            try {
                setActiveButton(salesBtn);
                NavigationController.openSales(salesBtn);
            } catch (Exception ex) {
                ex.printStackTrace();
                NavigationController.showErrorAlert("Navigation Error", "Could not open Sales", ex.getMessage());
            }
        });
        
        // Customers button
        customersBtn.setOnAction(e -> {
            try {
                setActiveButton(customersBtn);
                NavigationController.openBuyers(customersBtn);
            } catch (Exception ex) {
                ex.printStackTrace();
                NavigationController.showErrorAlert("Navigation Error", "Could not open Customers", ex.getMessage());
            }
        });
        
        // Analytics button
        analyticsBtn.setOnAction(e -> {
            try {
                setActiveButton(analyticsBtn);
                NavigationController.openAnalytics(analyticsBtn);
            } catch (Exception ex) {
                ex.printStackTrace();
                NavigationController.showErrorAlert("Navigation Error", "Could not open Analytics", ex.getMessage());
            }
        });
        
        // Finance button
        financeBtn.setOnAction(e -> {
            try {
                setActiveButton(financeBtn);
                NavigationController.openFinance(financeBtn);
            } catch (Exception ex) {
                ex.printStackTrace();
                NavigationController.showErrorAlert("Navigation Error", "Could not open Finance", ex.getMessage());
            }
        });
        
        // Activity button
        activityBtn.setOnAction(e -> {
            try {
                setActiveButton(activityBtn);
                NavigationController.openActivity(activityBtn);
            } catch (Exception ex) {
                ex.printStackTrace();
                NavigationController.showErrorAlert("Navigation Error", "Could not open Activity", ex.getMessage());
            }
        });
        
        // Settings button
        settingsBtn.setOnAction(e -> {
            try {
                setActiveButton(settingsBtn);
                NavigationController.openSettings(settingsBtn);
            } catch (Exception ex) {
                ex.printStackTrace();
                NavigationController.showErrorAlert("Navigation Error", "Could not open Settings", ex.getMessage());
            }
        });
        
        // User Management button
        userMgmtBtn.setOnAction(e -> {
            try {
                setActiveButton(userMgmtBtn);
                NavigationController.openUserManagement(userMgmtBtn);
            } catch (Exception ex) {
                ex.printStackTrace();
                NavigationController.showErrorAlert("Navigation Error", "Could not open User Management", ex.getMessage());
            }
        });
    }
    
    /**
     * Sets the active button styling
     */
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
        if (activeBtn != null) {
            activeBtn.getStyleClass().add("active");
        }
    }
    
    /**
     * Prevents navigation to the same page
     */
    public void handleSamePageNavigation(String pageName) {
        System.out.println("Already on " + pageName + " page");
    }
    
    /**
     * Gets reference to specific buttons for external use
     */
    public Button getDashboardBtn() { return dashboardBtn; }
    public Button getInventoryBtn() { return inventoryBtn; }
    public Button getRepairsBtn() { return repairsBtn; }
    public Button getSalesBtn() { return salesBtn; }
    public Button getCustomersBtn() { return customersBtn; }
    public Button getAnalyticsBtn() { return analyticsBtn; }
    public Button getFinanceBtn() { return financeBtn; }
    public Button getActivityBtn() { return activityBtn; }
    public Button getSettingsBtn() { return settingsBtn; }
    public Button getUserMgmtBtn() { return userMgmtBtn; }
}
