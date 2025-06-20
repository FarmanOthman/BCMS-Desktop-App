package com.bcms.model;

import java.time.LocalDateTime;

/**
 * Model class for representing a system activity log entry.
 */
public class ActivityLog {
    
    private int id;
    private Severity severity;
    private String action;
    private String details;
    private String user;
    private LocalDateTime timestamp;
    private String ipAddress;
    
    /**
     * Enum for activity severity levels.
     */
    public enum Severity {
        INFO,
        WARNING,
        ERROR,
        SUCCESS
    }
    
    /**
     * Default constructor.
     */
    public ActivityLog() {
        // Default constructor
    }
    
    /**
     * Constructor with all fields.
     */
    public ActivityLog(int id, Severity severity, String action, String details, 
                      String user, LocalDateTime timestamp, String ipAddress) {
        this.id = id;
        this.severity = severity;
        this.action = action;
        this.details = details;
        this.user = user;
        this.timestamp = timestamp;
        this.ipAddress = ipAddress;
    }
    
    // Getters and setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Severity getSeverity() {
        return severity;
    }
    
    public void setSeverity(Severity severity) {
        this.severity = severity;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public String getDetails() {
        return details;
    }
    
    public void setDetails(String details) {
        this.details = details;
    }
    
    public String getUser() {
        return user;
    }
    
    public void setUser(String user) {
        this.user = user;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    @Override
    public String toString() {
        return "ActivityLog{" +
                "id=" + id +
                ", severity=" + severity +
                ", action='" + action + '\'' +
                ", details='" + details + '\'' +
                ", user='" + user + '\'' +
                ", timestamp=" + timestamp +
                ", ipAddress='" + ipAddress + '\'' +
                '}';
    }
}