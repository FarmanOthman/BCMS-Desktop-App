#!/bin/bash

# Complete script to update all remaining controllers to use BaseController

echo "🚀 Completing centralized sidebar implementation..."

# List of controllers to update
controllers=(
    "AnalyticsController:analytics"
    "FinanceController:finance"
    "UserManagementController:usermanagement"
    "RepairsServiceController:repairs"
    "SettingsController:settings"
    "BuyerController:customers"
)

for controller_info in "${controllers[@]}"; do
    IFS=':' read -r controller_name page_name <<< "$controller_info"
    file="/Users/rawaz/Desktop/BCMS-Desktop-App-1/app/src/main/java/com/bcms/controller/${controller_name}.java"
    
    echo "📝 Processing $controller_name..."
    
    # Create backup
    cp "$file" "${file}.backup"
    
    # Step 1: Remove navigation button declarations
    sed -i '' '/^[[:space:]]*@FXML private Button dashboardBtn;/d' "$file"
    sed -i '' '/^[[:space:]]*@FXML private Button inventoryBtn;/d' "$file"
    sed -i '' '/^[[:space:]]*@FXML private Button repairsBtn;/d' "$file"
    sed -i '' '/^[[:space:]]*@FXML private Button salesBtn;/d' "$file"
    sed -i '' '/^[[:space:]]*@FXML private Button customersBtn;/d' "$file"
    sed -i '' '/^[[:space:]]*@FXML private Button analyticsBtn;/d' "$file"
    sed -i '' '/^[[:space:]]*@FXML private Button financeBtn;/d' "$file"
    sed -i '' '/^[[:space:]]*@FXML private Button activityBtn;/d' "$file"
    sed -i '' '/^[[:space:]]*@FXML private Button settingsBtn;/d' "$file"
    sed -i '' '/^[[:space:]]*@FXML private Button userMgmtBtn;/d' "$file"
    
    # Step 2: Change class declaration
    sed -i '' "s/implements Initializable/extends BaseController/g" "$file"
    
    # Step 3: Remove unused imports
    sed -i '' '/import.*Initializable/d' "$file"
    sed -i '' '/import.*URL/d' "$file"
    sed -i '' '/import.*ResourceBundle/d' "$file"
    
    # Step 4: Add required methods (this is a template - needs manual refinement)
    echo "✅ $controller_name structure updated. Manual refinement needed for:"
    echo "   - Initialize method conversion"
    echo "   - Navigation handlers removal"
    echo "   - SetActiveButton method removal"
    echo ""
done

echo "🎉 Batch processing complete!"
echo "📋 Next steps:"
echo "1. Each controller needs manual refinement to:"
echo "   - Replace initialize() method with getCurrentPageName() and initializeController()"
echo "   - Remove setupNavigationHandlers() method"
echo "   - Remove setActiveButton() method"
echo ""
echo "2. Test each controller for compilation errors"
echo "3. Update corresponding FXML files to include sidebar container"
