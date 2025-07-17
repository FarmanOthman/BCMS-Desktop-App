#!/bin/bash

# Script to update all remaining controllers to use BaseController

CONTROLLERS=(
    "ActivityController"
    "RepairsServiceController"
    "BuyerController"
    "AnalyticsController"
    "FinanceController"
    "UserManagementController"
    "SettingsController"
)

CONTROLLER_DIR="/Users/rawaz/Desktop/BCMS-Desktop-App-1/app/src/main/java/com/bcms/controller"

for controller in "${CONTROLLERS[@]}"; do
    echo "Updating $controller..."
    
    # Get the page name (lowercase controller name without "Controller")
    page_name=$(echo $controller | sed 's/Controller$//' | tr '[:upper:]' '[:lower:]')
    
    # Special cases for page names
    case $controller in
        "RepairsServiceController")
            page_name="repairs"
            ;;
        "BuyerController")
            page_name="customers"
            ;;
        "UserManagementController")
            page_name="usermanagement"
            ;;
    esac
    
    # Create the updated controller file
    cat > "$CONTROLLER_DIR/$controller.java.new" << EOF
package com.bcms.controller;

import javafx.fxml.FXML;
// Add other necessary imports here - they will be added based on original file content

public class $controller extends BaseController {
    
    // All original non-sidebar FXML components will be preserved
    // Navigation sidebar buttons are removed
    
    private Object viewModel; // This will be corrected based on actual type
    
    @Override
    protected String getCurrentPageName() {
        return "$page_name";
    }
    
    @Override
    protected void initializeController() {
        // Original initialization logic will be preserved
        // Navigation setup will be removed
    }
    
    // All other methods from original file will be preserved
}
EOF
    
    echo "Created template for $controller"
done

echo "Templates created. Manual refinement needed for each controller."
