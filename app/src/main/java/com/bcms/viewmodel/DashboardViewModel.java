package com.bcms.viewmodel;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;

/**
 * ViewModel for the Dashboard screen that handles the business logic
 * and data for the Dashboard UI.
 */
public class DashboardViewModel {
    
    // Statistics properties
    private final IntegerProperty totalCars = new SimpleIntegerProperty(142);
    private final IntegerProperty availableCars = new SimpleIntegerProperty(98);
    private final IntegerProperty carsInRepair = new SimpleIntegerProperty(24);
    private final IntegerProperty soldCars = new SimpleIntegerProperty(14);
    private final StringProperty carGrowthRate = new SimpleStringProperty("+12%");
    
    private final StringProperty monthlyRevenue = new SimpleStringProperty("$1.2M");
    private final StringProperty revenueGrowthRate = new SimpleStringProperty("+23%");
    
    // Activities data
    private final ObservableList<ActivityItem> recentActivities = FXCollections.observableArrayList();
    
    /**
     * Initialize the ViewModel with sample data.
     * In a real application, this would fetch data from a repository or service.
     */
    public DashboardViewModel() {
        loadSampleData();
    }
    
    /**
     * Load sample data for the dashboard.
     * In a real application, this would call a repository or service.
     */
    private void loadSampleData() {
        // Initialize with some sample activities
        recentActivities.add(new ActivityItem(
            ActivityType.SUCCESS,
            "Added BMW X5 2023 to inventory",
            "Car ID: #142",
            LocalDateTime.now().minusMinutes(2)
        ));
        
        recentActivities.add(new ActivityItem(
            ActivityType.INFO,
            "Completed brake service for Toyota Camry",
            "Repair ID: #87",
            LocalDateTime.now().minusHours(1)
        ));
        
        recentActivities.add(new ActivityItem(
            ActivityType.SUCCESS,
            "Sale completed: $42,000 - Honda Accord",
            "Customer: John Smith",
            LocalDateTime.now().minusHours(3)
        ));
        
        recentActivities.add(new ActivityItem(
            ActivityType.WARNING,
            "Pending insurance claim for Mercedes",
            "Claim ID: #234",
            LocalDateTime.now().minusHours(5)
        ));
    }
    
    // Getters for the properties
    public int getTotalCars() { return totalCars.get(); }
    public IntegerProperty totalCarsProperty() { return totalCars; }
    
    public int getAvailableCars() { return availableCars.get(); }
    public IntegerProperty availableCarsProperty() { return availableCars; }
    
    public int getCarsInRepair() { return carsInRepair.get(); }
    public IntegerProperty carsInRepairProperty() { return carsInRepair; }
    
    public int getSoldCars() { return soldCars.get(); }
    public IntegerProperty soldCarsProperty() { return soldCars; }
    
    public String getCarGrowthRate() { return carGrowthRate.get(); }
    public StringProperty carGrowthRateProperty() { return carGrowthRate; }
    
    public String getMonthlyRevenue() { return monthlyRevenue.get(); }
    public StringProperty monthlyRevenueProperty() { return monthlyRevenue; }
    
    public String getRevenueGrowthRate() { return revenueGrowthRate.get(); }
    public StringProperty revenueGrowthRateProperty() { return revenueGrowthRate; }
    
    public ObservableList<ActivityItem> getRecentActivities() { return recentActivities; }
    
    /**
     * Activity item class to represent recent activities.
     */
    public static class ActivityItem {
        private final ActivityType type;
        private final String title;
        private final String subtitle;
        private final LocalDateTime timestamp;
        
        public ActivityItem(ActivityType type, String title, String subtitle, LocalDateTime timestamp) {
            this.type = type;
            this.title = title;
            this.subtitle = subtitle;
            this.timestamp = timestamp;
        }
        
        public ActivityType getType() { return type; }
        public String getTitle() { return title; }
        public String getSubtitle() { return subtitle; }
        public LocalDateTime getTimestamp() { return timestamp; }
        
        /**
         * Format the timestamp for display.
         */
        public String getFormattedTime() {
            LocalDateTime now = LocalDateTime.now();
            
            if (now.toLocalDate().equals(timestamp.toLocalDate())) {
                // Today
                long hoursAgo = java.time.Duration.between(timestamp, now).toHours();
                if (hoursAgo == 0) {
                    long minutesAgo = java.time.Duration.between(timestamp, now).toMinutes();
                    return minutesAgo + " minutes ago";
                } else {
                    return hoursAgo + " hour" + (hoursAgo > 1 ? "s" : "") + " ago";
                }
            } else {
                // Another day
                return timestamp.format(java.time.format.DateTimeFormatter.ofPattern("MMM d, yyyy"));
            }
        }
    }
    
    /**
     * Enum for activity types with corresponding style classes.
     */
    public enum ActivityType {
        SUCCESS("activity-success"),
        INFO("activity-info"),
        WARNING("activity-warning"),
        ERROR("activity-error");
        
        private final String styleClass;
        
        ActivityType(String styleClass) {
            this.styleClass = styleClass;
        }
        
        public String getStyleClass() {
            return styleClass;
        }
    }
}