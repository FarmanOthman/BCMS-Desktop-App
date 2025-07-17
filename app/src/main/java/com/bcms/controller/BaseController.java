package com.bcms.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Base controller class that handles sidebar integration
 */
public abstract class BaseController implements Initializable {
    
    @FXML protected VBox sidebarContainer;
    
    protected SidebarController sidebarController;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeSidebar();
        initializeController();
    }
    
    /**
     * Initializes the sidebar component
     */
    private void initializeSidebar() {
        try {
            SidebarHelper.SidebarComponents components = SidebarHelper.loadSidebarWithController();
            sidebarController = components.getController();
            
            // Set the active page based on the current controller
            String currentPage = getCurrentPageName();
            sidebarController.setActivePage(currentPage);
            
            // Add sidebar to the container
            if (sidebarContainer != null) {
                sidebarContainer.getChildren().clear();
                sidebarContainer.getChildren().add(components.getSidebarNode());
            }
        } catch (IOException e) {
            System.err.println("Error loading sidebar: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Gets the current page name - to be implemented by subclasses
     */
    protected abstract String getCurrentPageName();
    
    /**
     * Initialize the specific controller - to be implemented by subclasses
     */
    protected abstract void initializeController();
    
    /**
     * Gets the sidebar controller instance
     */
    public SidebarController getSidebarController() {
        return sidebarController;
    }
}
