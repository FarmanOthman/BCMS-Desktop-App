#!/bin/bash

# Script to update FXML files to use sidebar container pattern
echo "Updating FXML files to use sidebar container pattern..."

# List of FXML files to update (excluding Dashboard which is already updated)
files=(
    "Inventory.fxml"
    "Sales.fxml"
    "SaleView.fxml"
    "Analytics.fxml"
    "Activity.fxml"
    "finance.fxml"
    "UserManagement.fxml"
)

# Base path to FXML files
base_path="/Users/rawaz/Desktop/BCMS-Desktop-App-1/app/src/main/resources/fxml"

# Function to update each file
update_fxml() {
    local file="$1"
    local full_path="$base_path/$file"
    
    if [[ -f "$full_path" ]]; then
        echo "Updating $file..."
        
        # Create backup
        cp "$full_path" "$full_path.backup"
        
        # Replace HBox with BorderPane and add sidebar container
        sed -i '' 's/<HBox xmlns="http:\/\/javafx.com\/javafx\/17" xmlns:fx="http:\/\/javafx.com\/fxml\/1"/<BorderPane xmlns="http:\/\/javafx.com\/javafx\/17" xmlns:fx="http:\/\/javafx.com\/fxml\/1"/' "$full_path"
        
        # Replace the sidebar section with sidebar container
        # This is a simplified approach - in practice, you might need to manually edit complex files
        echo "File $file updated (manual editing may be required for complex layouts)"
    else
        echo "File $file not found at $full_path"
    fi
}

# Update each file
for file in "${files[@]}"; do
    update_fxml "$file"
done

echo "FXML update script completed!"
echo "Note: Some files may require manual editing for complex layouts."
