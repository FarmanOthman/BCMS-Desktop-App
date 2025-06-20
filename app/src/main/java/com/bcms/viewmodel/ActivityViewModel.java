package com.bcms.viewmodel;

import com.bcms.model.ActivityLog;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.shape.Circle;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

/**
 * ViewModel for the Activity Log screen that handles the business logic
 * and data for the Activity Log UI.
 */
public class ActivityViewModel {
    
    // Summary metrics properties
    private final IntegerProperty todayActivities = new SimpleIntegerProperty(247);
    private final StringProperty activityChange = new SimpleStringProperty("+18%");
    private final IntegerProperty criticalEvents = new SimpleIntegerProperty(3);
    private final IntegerProperty activeUsers = new SimpleIntegerProperty(12);
    private final StringProperty systemStatus = new SimpleStringProperty("GOOD");
    
    private final ObservableList<ActivityLogItem> allActivities = FXCollections.observableArrayList();
    
    /**
     * Initialize the ViewModel with sample data.
     * In a real application, this would fetch data from a repository or service.
     */
    public ActivityViewModel() {
        loadSampleData();
    }
    
    /**
     * Load sample data for activity logs.
     * In a real application, this would call a repository or service.
     */
    private void loadSampleData() {
        LocalDate today = LocalDate.now();
        
        // Add sample activity logs
        allActivities.add(new ActivityLogItem(
            247,
            ActivityLog.Severity.INFO,
            "Car Deleted",
            "Vehicle ID #142 removed",
            "John Doe",
            LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 13, 55),
            "192.168.1.45"
        ));
        
        allActivities.add(new ActivityLogItem(
            246,
            ActivityLog.Severity.WARNING,
            "Failed Login Attempt",
            "Multiple failed attempts for user",
            "Unknown",
            LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 13, 45),
            "203.45.12.67"
        ));
        
        allActivities.add(new ActivityLogItem(
            245,
            ActivityLog.Severity.SUCCESS,
            "Sale Completed",
            "Sale transaction processed",
            "Jane Smith",
            LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 12, 15),
            "192.168.1.23"
        ));
        
        allActivities.add(new ActivityLogItem(
            244,
            ActivityLog.Severity.INFO,
            "User Login",
            "Successful authentication",
            "Mike Johnson",
            LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 11, 30),
            "192.168.1.67"
        ));
        
        allActivities.add(new ActivityLogItem(
            243,
            ActivityLog.Severity.INFO,
            "Report Generated",
            "Monthly sales report created",
            "System",
            LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 10, 0),
            "127.0.0.1"
        ));
        
        allActivities.add(new ActivityLogItem(
            242,
            ActivityLog.Severity.INFO,
            "Car Updated",
            "Vehicle information modified",
            "Sarah Wilson",
            LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 9, 45),
            "192.168.1.89"
        ));
        
        allActivities.add(new ActivityLogItem(
            241,
            ActivityLog.Severity.SUCCESS,
            "Backup Completed",
            "Daily database backup finished",
            "System",
            LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 3, 0),
            "127.0.0.1"
        ));
    }
    
    /**
     * Gets a filtered list of activity logs based on search criteria and filters
     */
    public ObservableList<ActivityLogItem> getFilteredActivities(String searchText, String severityFilter, 
                                                                String userFilter, String timeFilter) {
        // If no filters are applied, return all activities
        if ((searchText == null || searchText.isEmpty()) && 
            (severityFilter == null || severityFilter.equals("All Severities")) && 
            (userFilter == null || userFilter.equals("All Users")) &&
            (timeFilter == null || timeFilter.equals("All Time"))) {
            return allActivities;
        }
        
        // Apply filters
        return allActivities.stream()
            .filter(activity -> {
                // Apply search text filter
                boolean matchesSearch = searchText == null || searchText.isEmpty() || 
                    activity.getAction().toLowerCase().contains(searchText.toLowerCase()) ||
                    activity.getDetails().toLowerCase().contains(searchText.toLowerCase()) ||
                    activity.getUser().toLowerCase().contains(searchText.toLowerCase());
                
                // Apply severity filter
                boolean matchesSeverity = severityFilter == null || severityFilter.equals("All Severities");
                
                if (!matchesSeverity) {
                    try {
                        ActivityLog.Severity filterSeverity = ActivityLog.Severity.valueOf(severityFilter.toUpperCase());
                        matchesSeverity = activity.getSeverity() == filterSeverity;
                    } catch (IllegalArgumentException e) {
                        // Invalid severity filter, treat as no filter
                        matchesSeverity = true;
                    }
                }
                
                // Apply user filter
                boolean matchesUser = userFilter == null || userFilter.equals("All Users") ||
                    activity.getUser().equals(userFilter);
                
                // Apply time filter
                boolean matchesTime = timeFilter == null || timeFilter.equals("All Time");
                
                if (!matchesTime) {
                    LocalDateTime now = LocalDateTime.now();
                    LocalDateTime cutoff = now;
                    
                    switch (timeFilter) {
                        case "Last 24 Hours":
                            cutoff = now.minusDays(1);
                            break;
                        case "Last 7 Days":
                            cutoff = now.minusDays(7);
                            break;
                        case "Last 30 Days":
                            cutoff = now.minusDays(30);
                            break;
                    }
                    
                    matchesTime = activity.getTimestamp().isAfter(cutoff) || activity.getTimestamp().isEqual(cutoff);
                }
                
                return matchesSearch && matchesSeverity && matchesUser && matchesTime;
            })
            .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }
    
    /**
     * Get the complete list of all activity logs
     */
    public ObservableList<ActivityLogItem> getAllActivities() {
        return allActivities;
    }
    
    // Getters for the properties
    public int getTodayActivities() { return todayActivities.get(); }
    public IntegerProperty todayActivitiesProperty() { return todayActivities; }
    
    public String getActivityChange() { return activityChange.get(); }
    public StringProperty activityChangeProperty() { return activityChange; }
    
    public int getCriticalEvents() { return criticalEvents.get(); }
    public IntegerProperty criticalEventsProperty() { return criticalEvents; }
    
    public int getActiveUsers() { return activeUsers.get(); }
    public IntegerProperty activeUsersProperty() { return activeUsers; }
    
    public String getSystemStatus() { return systemStatus.get(); }
    public StringProperty systemStatusProperty() { return systemStatus; }
    
    /**
     * Activity log item class for display in tables
     */
    public static class ActivityLogItem {
        private final IntegerProperty id = new SimpleIntegerProperty();
        private final ObjectProperty<ActivityLog.Severity> severity = new SimpleObjectProperty<>();
        private final StringProperty action = new SimpleStringProperty();
        private final StringProperty details = new SimpleStringProperty();
        private final StringProperty user = new SimpleStringProperty();
        private final ObjectProperty<LocalDateTime> timestamp = new SimpleObjectProperty<>();
        private final StringProperty ipAddress = new SimpleStringProperty();
        private final ObjectProperty<Circle> severityIcon = new SimpleObjectProperty<>();
        
        public ActivityLogItem(int id, ActivityLog.Severity severity, String action, String details,
                              String user, LocalDateTime timestamp, String ipAddress) {
            this.id.set(id);
            this.severity.set(severity);
            this.action.set(action);
            this.details.set(details);
            this.user.set(user);
            this.timestamp.set(timestamp);
            this.ipAddress.set(ipAddress);
            
            // Create the severity icon
            Circle icon = new Circle(8);
            
            // Set color based on severity
            switch (severity) {
                case INFO:
                    icon.getStyleClass().add("severity-info");
                    break;
                case WARNING:
                    icon.getStyleClass().add("severity-warning");
                    break;
                case ERROR:
                    icon.getStyleClass().add("severity-error");
                    break;
                case SUCCESS:
                    icon.getStyleClass().add("severity-success");
                    break;
            }
            
            this.severityIcon.set(icon);
        }
        
        // Getters and property methods
        public int getId() { return id.get(); }
        public IntegerProperty idProperty() { return id; }
        
        public ActivityLog.Severity getSeverity() { return severity.get(); }
        public ObjectProperty<ActivityLog.Severity> severityProperty() { return severity; }
        
        public String getAction() { return action.get(); }
        public StringProperty actionProperty() { return action; }
        
        public String getDetails() { return details.get(); }
        public StringProperty detailsProperty() { return details; }
        
        public String getUser() { return user.get(); }
        public StringProperty userProperty() { return user; }
        
        public LocalDateTime getTimestamp() { return timestamp.get(); }
        public ObjectProperty<LocalDateTime> timestampProperty() { return timestamp; }
        
        // Format timestamp as a string
        public String getFormattedTimestamp() {
            return timestamp.get().format(DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a"));
        }
        
        public StringProperty formattedTimestampProperty() {
            StringProperty formatted = new SimpleStringProperty();
            formatted.bind(timestamp.asString("MMMM d, yyyy 'at' h:mm a"));
            return formatted;
        }
        
        public String getIpAddress() { return ipAddress.get(); }
        public StringProperty ipAddressProperty() { return ipAddress; }
        
        public Circle getSeverityIcon() { return severityIcon.get(); }
        public ObjectProperty<Circle> severityIconProperty() { return severityIcon; }
    }
}