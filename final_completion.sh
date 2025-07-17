#!/bin/bash

# Final completion script for all remaining controllers

cd /Users/rawaz/Desktop/BCMS-Desktop-App-1/app/src/main/java/com/bcms/controller

echo "🚀 FINAL COMPLETION: Updating all remaining controllers..."

# List of controllers that still need updates
controllers=(
    "BuyerController.java"
    "RepairsServiceController.java" 
    "SettingsController.java"
    "UserManagementController.java"
)

# Process each controller
for controller in "${controllers[@]}"; do
    echo ""
    echo "📝 Processing $controller..."
    
    # 1. Remove navigation button declarations
    sed -i '' '/^[[:space:]]*@FXML private Button.*Btn;$/d' "$controller"
    
    # 2. Change extends clause
    sed -i '' 's/implements Initializable/extends BaseController/g' "$controller"
    
    # 3. Remove imports
    sed -i '' '/import.*Initializable/d' "$controller"
    sed -i '' '/import.*URL/d' "$controller"
    sed -i '' '/import.*ResourceBundle/d' "$controller"
    
    echo "✅ $controller updated (needs manual method replacement)"
done

echo ""
echo "🎉 ALL CONTROLLERS PROCESSED!"
echo ""
echo "📋 SUMMARY:"
echo "✅ Completed controllers using BaseController:"
echo "   - ActivityController"
echo "   - AnalyticsController"
echo "   - DashboardController"
echo "   - FinanceController"
echo "   - InventoryController"
echo "   - SalesController"
echo ""
echo "🔄 Controllers need manual method cleanup:"
echo "   - BuyerController"
echo "   - RepairsServiceController"
echo "   - SettingsController"
echo "   - UserManagementController"
echo "   - FinanceController (navigation handlers)"
echo ""
echo "Each needs:"
echo "1. Replace initialize() with getCurrentPageName() and initializeController()"
echo "2. Remove setupNavigationHandlers() method"
echo "3. Remove setActiveButton() method"
