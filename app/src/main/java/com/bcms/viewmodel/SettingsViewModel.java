package com.bcms.viewmodel;

import javafx.beans.property.*;

import java.util.UUID;

/**
 * ViewModel for the Settings screen that handles the business logic
 * and data for the Settings UI.
 */
public class SettingsViewModel {
    
    // General settings properties
    private final StringProperty companyName = new SimpleStringProperty("Bestun Cars");
    private final StringProperty companyAddress = new SimpleStringProperty("123 Main Street, Erbil, Iraq");
    private final StringProperty companyPhone = new SimpleStringProperty("+964 750 123 4567");
    private final StringProperty companyEmail = new SimpleStringProperty("info@bestuncars.com");
    private final DoubleProperty taxRate = new SimpleDoubleProperty(15.0);
    private final BooleanProperty enableTax = new SimpleBooleanProperty(true);
    private final StringProperty defaultCurrency = new SimpleStringProperty("USD - $");
    private final StringProperty dateFormat = new SimpleStringProperty("MM/DD/YYYY");
    private final StringProperty brandColor = new SimpleStringProperty("#4f7cff");
    private final StringProperty backupLocation = new SimpleStringProperty("/home/user/backups");
    
    // Notification settings properties
    private final BooleanProperty emailNotifications = new SimpleBooleanProperty(true);
    private final BooleanProperty smsNotifications = new SimpleBooleanProperty(false);
    private final BooleanProperty lowInventoryAlerts = new SimpleBooleanProperty(true);
    private final BooleanProperty paymentReceiptAlerts = new SimpleBooleanProperty(true);
    private final BooleanProperty repairStatusAlerts = new SimpleBooleanProperty(true);
    
    // Integration settings properties
    private final StringProperty apiKey = new SimpleStringProperty("api_key_" + UUID.randomUUID().toString().substring(0, 8));
    private final StringProperty secretKey = new SimpleStringProperty("sk_" + UUID.randomUUID().toString().substring(0, 16));
    private final StringProperty webhookUrl = new SimpleStringProperty("https://webhook.bestuncars.com/incoming");
    
    // Database settings properties
    private final StringProperty dbHost = new SimpleStringProperty("localhost");
    private final StringProperty dbPort = new SimpleStringProperty("3306");
    private final StringProperty dbName = new SimpleStringProperty("bestun_cars_db");
    private final StringProperty dbUser = new SimpleStringProperty("bcms_user");
    private final StringProperty dbPassword = new SimpleStringProperty("password123");
    
    /**
     * Initialize the ViewModel with default data.
     * In a real application, this would fetch data from a configuration file or database.
     */
    public SettingsViewModel() {
        // Initialization done via field initializers above
    }
    
    /**
     * Save settings to persistent storage.
     * In a real application, this would save to a configuration file or database.
     * 
     * @return true if save was successful
     */
    public boolean saveSettings() {
        // Simulate saving settings to persistent storage
        System.out.println("Saving settings");
        System.out.println("Company Name: " + getCompanyName());
        System.out.println("Tax Rate: " + getTaxRate());
        // In a real app, you would save to file/database here
        
        return true; // Assume save is successful
    }
    
    /**
     * Generate a new API key.
     * 
     * @return The new API key
     */
    public String generateNewApiKey() {
        String newKey = "api_key_" + UUID.randomUUID().toString().substring(0, 8);
        apiKey.set(newKey);
        return newKey;
    }
    
    /**
     * Test database connection with the given parameters.
     * In a real application, this would attempt to connect to the database.
     * 
     * @param host Database host
     * @param port Database port
     * @param name Database name
     * @param user Database user
     * @param password Database password
     * @return true if connection successful
     */
    public boolean testDatabaseConnection(String host, String port, String name, String user, String password) {
        // In a real app, you would try to connect to the database
        System.out.println("Testing connection to database: " + name + " on " + host + ":" + port);
        
        // Simulate connection test (always successful for demo)
        return true;
    }
    
    /**
     * Export database to the specified file location.
     * In a real application, this would create a database dump.
     * 
     * @param filePath The path to save the database export
     * @return true if export successful
     */
    public boolean exportDatabase(String filePath) {
        // Simulate database export
        System.out.println("Exporting database to: " + filePath);
        
        // In a real app, you would execute a database dump command
        return true; // Assume export is successful
    }
    
    /**
     * Import database from the specified file location.
     * In a real application, this would restore a database from a dump file.
     * 
     * @param filePath The path to the database dump file
     * @return true if import successful
     */
    public boolean importDatabase(String filePath) {
        // Simulate database import
        System.out.println("Importing database from: " + filePath);
        
        // In a real app, you would execute a database restore command
        return true; // Assume import is successful
    }
    
    /**
     * Reset all settings to default values.
     */
    public void resetToDefaults() {
        companyName.set("Bestun Cars");
        companyAddress.set("123 Main Street, Erbil, Iraq");
        companyPhone.set("+964 750 123 4567");
        companyEmail.set("info@bestuncars.com");
        taxRate.set(15.0);
        enableTax.set(true);
        defaultCurrency.set("USD - $");
        dateFormat.set("MM/DD/YYYY");
        brandColor.set("#4f7cff");
        backupLocation.set("/home/user/backups");
        
        emailNotifications.set(true);
        smsNotifications.set(false);
        lowInventoryAlerts.set(true);
        paymentReceiptAlerts.set(true);
        repairStatusAlerts.set(true);
        
        apiKey.set("api_key_" + UUID.randomUUID().toString().substring(0, 8));
        secretKey.set("sk_" + UUID.randomUUID().toString().substring(0, 16));
        webhookUrl.set("https://webhook.bestuncars.com/incoming");
        
        dbHost.set("localhost");
        dbPort.set("3306");
        dbName.set("bestun_cars_db");
        dbUser.set("bcms_user");
        dbPassword.set("password123");
    }
    
    // Getters and setters for all properties
    
    public String getCompanyName() { return companyName.get(); }
    public StringProperty companyNameProperty() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName.set(companyName); }
    
    public String getCompanyAddress() { return companyAddress.get(); }
    public StringProperty companyAddressProperty() { return companyAddress; }
    public void setCompanyAddress(String companyAddress) { this.companyAddress.set(companyAddress); }
    
    public String getCompanyPhone() { return companyPhone.get(); }
    public StringProperty companyPhoneProperty() { return companyPhone; }
    public void setCompanyPhone(String companyPhone) { this.companyPhone.set(companyPhone); }
    
    public String getCompanyEmail() { return companyEmail.get(); }
    public StringProperty companyEmailProperty() { return companyEmail; }
    public void setCompanyEmail(String companyEmail) { this.companyEmail.set(companyEmail); }
    
    public double getTaxRate() { return taxRate.get(); }
    public DoubleProperty taxRateProperty() { return taxRate; }
    public void setTaxRate(double taxRate) { this.taxRate.set(taxRate); }
    
    public boolean isEnableTax() { return enableTax.get(); }
    public BooleanProperty enableTaxProperty() { return enableTax; }
    public void setEnableTax(boolean enableTax) { this.enableTax.set(enableTax); }
    
    public String getDefaultCurrency() { return defaultCurrency.get(); }
    public StringProperty defaultCurrencyProperty() { return defaultCurrency; }
    public void setDefaultCurrency(String defaultCurrency) { this.defaultCurrency.set(defaultCurrency); }
    
    public String getDateFormat() { return dateFormat.get(); }
    public StringProperty dateFormatProperty() { return dateFormat; }
    public void setDateFormat(String dateFormat) { this.dateFormat.set(dateFormat); }
    
    public String getBrandColor() { return brandColor.get(); }
    public StringProperty brandColorProperty() { return brandColor; }
    public void setBrandColor(String brandColor) { this.brandColor.set(brandColor); }
    
    public String getBackupLocation() { return backupLocation.get(); }
    public StringProperty backupLocationProperty() { return backupLocation; }
    public void setBackupLocation(String backupLocation) { this.backupLocation.set(backupLocation); }
    
    public boolean isEmailNotifications() { return emailNotifications.get(); }
    public BooleanProperty emailNotificationsProperty() { return emailNotifications; }
    public void setEmailNotifications(boolean emailNotifications) { this.emailNotifications.set(emailNotifications); }
    
    public boolean isSmsNotifications() { return smsNotifications.get(); }
    public BooleanProperty smsNotificationsProperty() { return smsNotifications; }
    public void setSmsNotifications(boolean smsNotifications) { this.smsNotifications.set(smsNotifications); }
    
    public boolean isLowInventoryAlerts() { return lowInventoryAlerts.get(); }
    public BooleanProperty lowInventoryAlertsProperty() { return lowInventoryAlerts; }
    public void setLowInventoryAlerts(boolean lowInventoryAlerts) { this.lowInventoryAlerts.set(lowInventoryAlerts); }
    
    public boolean isPaymentReceiptAlerts() { return paymentReceiptAlerts.get(); }
    public BooleanProperty paymentReceiptAlertsProperty() { return paymentReceiptAlerts; }
    public void setPaymentReceiptAlerts(boolean paymentReceiptAlerts) { this.paymentReceiptAlerts.set(paymentReceiptAlerts); }
    
    public boolean isRepairStatusAlerts() { return repairStatusAlerts.get(); }
    public BooleanProperty repairStatusAlertsProperty() { return repairStatusAlerts; }
    public void setRepairStatusAlerts(boolean repairStatusAlerts) { this.repairStatusAlerts.set(repairStatusAlerts); }
    
    public String getApiKey() { return apiKey.get(); }
    public StringProperty apiKeyProperty() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey.set(apiKey); }
    
    public String getSecretKey() { return secretKey.get(); }
    public StringProperty secretKeyProperty() { return secretKey; }
    public void setSecretKey(String secretKey) { this.secretKey.set(secretKey); }
    
    public String getWebhookUrl() { return webhookUrl.get(); }
    public StringProperty webhookUrlProperty() { return webhookUrl; }
    public void setWebhookUrl(String webhookUrl) { this.webhookUrl.set(webhookUrl); }
    
    public String getDbHost() { return dbHost.get(); }
    public StringProperty dbHostProperty() { return dbHost; }
    public void setDbHost(String dbHost) { this.dbHost.set(dbHost); }
    
    public String getDbPort() { return dbPort.get(); }
    public StringProperty dbPortProperty() { return dbPort; }
    public void setDbPort(String dbPort) { this.dbPort.set(dbPort); }
    
    public String getDbName() { return dbName.get(); }
    public StringProperty dbNameProperty() { return dbName; }
    public void setDbName(String dbName) { this.dbName.set(dbName); }
    
    public String getDbUser() { return dbUser.get(); }
    public StringProperty dbUserProperty() { return dbUser; }
    public void setDbUser(String dbUser) { this.dbUser.set(dbUser); }
    
    public String getDbPassword() { return dbPassword.get(); }
    public StringProperty dbPasswordProperty() { return dbPassword; }
    public void setDbPassword(String dbPassword) { this.dbPassword.set(dbPassword); }
}