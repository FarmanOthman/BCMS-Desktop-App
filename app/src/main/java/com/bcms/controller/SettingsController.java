package com.bcms.controller;

import com.bcms.viewmodel.SettingsViewModel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class SettingsController extends BaseController {
    
    // Page title and subtitle
    @FXML private Label settingsTitle;
    @FXML private Label settingsSubtitle;
    
    // General settings
    @FXML private TextField companyNameField;
    @FXML private TextField companyAddressField;
    @FXML private TextField companyPhoneField;
    @FXML private TextField companyEmailField;
    @FXML private TextField taxRateField;
    @FXML private CheckBox enableTaxCheckbox;
    @FXML private ComboBox<String> defaultCurrencyComboBox;
    @FXML private ComboBox<String> dateFormatComboBox;
    @FXML private ColorPicker brandColorPicker;
    @FXML private TextField backupLocationField;
    @FXML private Button chooseBackupLocationButton;
    
    // Notification settings
    @FXML private CheckBox emailNotificationsCheckbox;
    @FXML private CheckBox smsNotificationsCheckbox;
    @FXML private CheckBox lowInventoryAlertsCheckbox;
    @FXML private CheckBox paymentReceiptAlertsCheckbox;
    @FXML private CheckBox repairStatusAlertsCheckbox;
    
    // Integration settings
    @FXML private TextField apiKeyField;
    @FXML private PasswordField secretKeyField;
    @FXML private TextField webhookUrlField;
    @FXML private Button regenerateApiKeyButton;
    
    // Database settings
    @FXML private TextField dbHostField;
    @FXML private TextField dbPortField;
    @FXML private TextField dbNameField;
    @FXML private TextField dbUserField;
    @FXML private PasswordField dbPasswordField;
    @FXML private Button testConnectionButton;
    @FXML private Button exportDatabaseButton;
    @FXML private Button importDatabaseButton;
    
    // Action buttons
    @FXML private Button saveButton;
    @FXML private Button resetButton;
    
    private SettingsViewModel viewModel;
    
    @Override
    public String getCurrentPageName() {
        return "settings";
    }
    
    @Override
    public void initializeController() {
        viewModel = new SettingsViewModel();
        
        setupDefaultValues();
        setupEventHandlers();
    }
    private void setupDefaultValues() {
        // Set the title and subtitle
        settingsTitle.setText("Settings");
        settingsSubtitle.setText("Configure system settings and preferences");
        
        // Setup ComboBoxes
        defaultCurrencyComboBox.setItems(FXCollections.observableArrayList(
            "USD - $", "EUR - €", "GBP - £", "JPY - ¥", "IQD - د.ع"
        ));
        defaultCurrencyComboBox.setValue("USD - $");
        
        dateFormatComboBox.setItems(FXCollections.observableArrayList(
            "MM/DD/YYYY", "DD/MM/YYYY", "YYYY-MM-DD", "DD-MMM-YYYY"
        ));
        dateFormatComboBox.setValue("MM/DD/YYYY");
        
        // Set default values from ViewModel
        companyNameField.setText(viewModel.getCompanyName());
        companyAddressField.setText(viewModel.getCompanyAddress());
        companyPhoneField.setText(viewModel.getCompanyPhone());
        companyEmailField.setText(viewModel.getCompanyEmail());
        taxRateField.setText(String.valueOf(viewModel.getTaxRate()));
        enableTaxCheckbox.setSelected(viewModel.isEnableTax());
        backupLocationField.setText(viewModel.getBackupLocation());
        brandColorPicker.setValue(Color.web(viewModel.getBrandColor()));
        
        // Notification settings
        emailNotificationsCheckbox.setSelected(viewModel.isEmailNotifications());
        smsNotificationsCheckbox.setSelected(viewModel.isSmsNotifications());
        lowInventoryAlertsCheckbox.setSelected(viewModel.isLowInventoryAlerts());
        paymentReceiptAlertsCheckbox.setSelected(viewModel.isPaymentReceiptAlerts());
        repairStatusAlertsCheckbox.setSelected(viewModel.isRepairStatusAlerts());
        
        // Integration settings
        apiKeyField.setText(viewModel.getApiKey());
        secretKeyField.setText(viewModel.getSecretKey());
        webhookUrlField.setText(viewModel.getWebhookUrl());
        
        // Database settings
        dbHostField.setText(viewModel.getDbHost());
        dbPortField.setText(viewModel.getDbPort());
        dbNameField.setText(viewModel.getDbName());
        dbUserField.setText(viewModel.getDbUser());
        dbPasswordField.setText(viewModel.getDbPassword());
    }
    
    private void setupEventHandlers() {
        // Choose backup location button
        chooseBackupLocationButton.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select Backup Location");
            
            // Set initial directory if the current path exists
            String currentPath = backupLocationField.getText();
            if (currentPath != null && !currentPath.isEmpty()) {
                File initialDir = new File(currentPath);
                if (initialDir.exists()) {
                    directoryChooser.setInitialDirectory(initialDir);
                }
            }
            
            File selectedDirectory = directoryChooser.showDialog(chooseBackupLocationButton.getScene().getWindow());
            if (selectedDirectory != null) {
                backupLocationField.setText(selectedDirectory.getAbsolutePath());
            }
        });
        
        // Regenerate API Key button
        regenerateApiKeyButton.setOnAction(e -> {
            // Show confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Regenerate API Key");
            alert.setHeaderText("Are you sure?");
            alert.setContentText("Regenerating your API key will invalidate your current key and may break existing integrations.");
            
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Generate new API key
                    String newApiKey = viewModel.generateNewApiKey();
                    apiKeyField.setText(newApiKey);
                    
                    // Show success notification
                    showInfoAlert("Success", "API Key Regenerated", 
                        "Your new API key has been generated. Make sure to update any applications that use this key.");
                }
            });
        });
        
        // Test Database Connection button
        testConnectionButton.setOnAction(e -> {
            // Simulate testing database connection
            boolean success = viewModel.testDatabaseConnection(
                dbHostField.getText(),
                dbPortField.getText(),
                dbNameField.getText(),
                dbUserField.getText(),
                dbPasswordField.getText()
            );
            
            if (success) {
                showInfoAlert("Connection Successful", "Database Connection Test", 
                    "Successfully connected to the database.");
            } else {
                showErrorAlert("Connection Failed", "Database Connection Test", 
                    "Could not connect to the database. Please check your settings.");
            }
        });
        
        // Export Database button
        exportDatabaseButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Database");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("SQL Files", "*.sql")
            );
            
            File file = fileChooser.showSaveDialog(exportDatabaseButton.getScene().getWindow());
            if (file != null) {
                // Simulate database export
                boolean success = viewModel.exportDatabase(file.getAbsolutePath());
                
                if (success) {
                    showInfoAlert("Export Successful", "Database Export", 
                        "Database has been exported to: " + file.getAbsolutePath());
                } else {
                    showErrorAlert("Export Failed", "Database Export", 
                        "Could not export the database. Please check permissions and try again.");
                }
            }
        });
        
        // Import Database button
        importDatabaseButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Import Database");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("SQL Files", "*.sql")
            );
            
            File file = fileChooser.showOpenDialog(importDatabaseButton.getScene().getWindow());
            if (file != null) {
                // Show confirmation dialog
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Import Database");
                alert.setHeaderText("Warning: This will overwrite your current database");
                alert.setContentText("Are you sure you want to import this database? This action cannot be undone.");
                
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        // Simulate database import
                        boolean success = viewModel.importDatabase(file.getAbsolutePath());
                        
                        if (success) {
                            showInfoAlert("Import Successful", "Database Import", 
                                "Database has been imported successfully. You may need to restart the application.");
                        } else {
                            showErrorAlert("Import Failed", "Database Import", 
                                "Could not import the database. Please check the file and try again.");
                        }
                    }
                });
            }
        });
        
        // Save button
        saveButton.setOnAction(e -> saveSettings());
        
        // Reset button
        resetButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Reset Settings");
            alert.setHeaderText("Reset to Default Settings");
            alert.setContentText("Are you sure you want to reset all settings to default values? This action cannot be undone.");
            
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    viewModel.resetToDefaults();
                    setupDefaultValues(); // Reset UI fields to default values
                    
                    showInfoAlert("Settings Reset", "Reset Successful", 
                        "All settings have been reset to their default values.");
                }
            });
        });
    }
    
    private void saveSettings() {
        try {
            // Validate inputs
            double taxRate = Double.parseDouble(taxRateField.getText());
            if (taxRate < 0 || taxRate > 100) {
                throw new NumberFormatException("Tax rate must be between 0 and 100");
            }
            
            // Update ViewModel with form values
            viewModel.setCompanyName(companyNameField.getText());
            viewModel.setCompanyAddress(companyAddressField.getText());
            viewModel.setCompanyPhone(companyPhoneField.getText());
            viewModel.setCompanyEmail(companyEmailField.getText());
            viewModel.setTaxRate(taxRate);
            viewModel.setEnableTax(enableTaxCheckbox.isSelected());
            viewModel.setDefaultCurrency(defaultCurrencyComboBox.getValue());
            viewModel.setDateFormat(dateFormatComboBox.getValue());
            viewModel.setBrandColor(convertColorToHex(brandColorPicker.getValue()));
            viewModel.setBackupLocation(backupLocationField.getText());
            
            // Notification settings
            viewModel.setEmailNotifications(emailNotificationsCheckbox.isSelected());
            viewModel.setSmsNotifications(smsNotificationsCheckbox.isSelected());
            viewModel.setLowInventoryAlerts(lowInventoryAlertsCheckbox.isSelected());
            viewModel.setPaymentReceiptAlerts(paymentReceiptAlertsCheckbox.isSelected());
            viewModel.setRepairStatusAlerts(repairStatusAlertsCheckbox.isSelected());
            
            // Integration settings
            viewModel.setApiKey(apiKeyField.getText());
            viewModel.setSecretKey(secretKeyField.getText());
            viewModel.setWebhookUrl(webhookUrlField.getText());
            
            // Database settings
            viewModel.setDbHost(dbHostField.getText());
            viewModel.setDbPort(dbPortField.getText());
            viewModel.setDbName(dbNameField.getText());
            viewModel.setDbUser(dbUserField.getText());
            viewModel.setDbPassword(dbPasswordField.getText());
            
            // Save settings
            boolean success = viewModel.saveSettings();
            
            if (success) {
                showInfoAlert("Settings Saved", "Save Successful", 
                    "Your settings have been saved successfully.");
            } else {
                showErrorAlert("Save Failed", "Error Saving Settings", 
                    "Could not save settings. Please try again.");
            }
            
        } catch (NumberFormatException ex) {
            showErrorAlert("Validation Error", "Invalid Input", 
                "Please enter a valid tax rate (0-100).");
        }
    }
    
    private String convertColorToHex(Color color) {
        return String.format("#%02X%02X%02X",
            (int) (color.getRed() * 255),
            (int) (color.getGreen() * 255),
            (int) (color.getBlue() * 255));
    }
    
    // Navigation methods are now handled by NavigationController
    
    // Helper methods for showing alerts
    private void showErrorAlert(String title, String header, String content) {
        NavigationController.showErrorAlert(title, header, content);
    }
    
    private void showInfoAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}