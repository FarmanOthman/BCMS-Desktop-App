package com.bcms.viewmodel;
import com.bcms.controller.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;

import javafx.geometry.Pos;
import java.util.stream.Collectors;

/**
 * ViewModel for the User Management screen that handles the business logic
 * and data for the User Management UI.
 */
public class UserViewModel {
    
    private final ObservableList<UserItem> allUsers = FXCollections.observableArrayList();
    
    /**
     * Initialize the ViewModel with sample data.
     * In a real application, this would fetch data from a repository or service.
     */
    public UserViewModel() {
        loadSampleData();
    }
    
    /**
     * Load sample data for users.
     * In a real application, this would call a repository or service.
     */
    private void loadSampleData() {
        // Add sample users
        allUsers.add(new UserItem(
            1,
            "John Doe",
            "john@example.com",
            "Manager",
            "Active",
            "Today"
        ));
        
        allUsers.add(new UserItem(
            2,
            "Jane Smith",
            "jane@domain.com",
            "Finance Staff",
            "Active",
            "Yesterday"
        ));
        
        allUsers.add(new UserItem(
            3,
            "Mike Johnson",
            "mike@company.local",
            "Technician",
            "Inactive",
            "2 weeks ago"
        ));
        
        allUsers.add(new UserItem(
            4,
            "Sarah Wilson",
            "sarah.w@company.com",
            "Sales Staff",
            "Active",
            "2 hours ago"
        ));
        
        allUsers.add(new UserItem(
            5,
            "Robert Chen",
            "robert.c@company.com",
            "Admin",
            "Active",
            "30 min ago"
        ));
    }
    
    /**
     * Gets a filtered list of users based on search criteria and filters
     */
    public ObservableList<UserItem> getFilteredUsers(String searchText, String roleFilter, String statusFilter) {
        // If no filters are applied, return all users
        if ((searchText == null || searchText.isEmpty()) && 
            (roleFilter == null || roleFilter.equals("All Roles")) && 
            (statusFilter == null || statusFilter.equals("All Status"))) {
            return allUsers;
        }
        
        // Apply filters
        return allUsers.stream()
            .filter(user -> {
                // Apply search text filter
                boolean matchesSearch = searchText == null || searchText.isEmpty() || 
                    user.getName().toLowerCase().contains(searchText) ||
                    user.getEmail().toLowerCase().contains(searchText);
                
                // Apply role filter
                boolean matchesRole = roleFilter == null || roleFilter.equals("All Roles") ||
                    user.getRole().equals(roleFilter);
                
                // Apply status filter
                boolean matchesStatus = statusFilter == null || statusFilter.equals("All Status") ||
                    user.getStatus().equals(statusFilter);
                
                return matchesSearch && matchesRole && matchesStatus;
            })
            .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }
    
    /**
     * Add a new user
     */
    public void addUser(UserItem user) {
        allUsers.add(user);
    }
    
    /**
     * Remove a user by ID
     */
    public boolean removeUser(int id) {
        return allUsers.removeIf(user -> user.getId() == id);
    }
    
    /**
     * Get the complete list of all users
     */
    public ObservableList<UserItem> getAllUsers() {
        return allUsers;
    }
    
    /**
     * Get a user by ID
     */
    public UserItem getUserById(int id) {
        for (UserItem user : allUsers) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }
    
    /**
     * Get the maximum user ID
     */
    public int getMaxId() {
        int maxId = 0;
        for (UserItem user : allUsers) {
            if (user.getId() > maxId) {
                maxId = user.getId();
            }
        }
        return maxId;
    }
    
    /**
     * User item class to represent users
     */
    public static class UserItem {
        private final IntegerProperty id = new SimpleIntegerProperty();
        private final StringProperty name = new SimpleStringProperty();
        private final StringProperty email = new SimpleStringProperty();
        private final StringProperty role = new SimpleStringProperty();
        private final StringProperty status = new SimpleStringProperty();
        private final StringProperty lastLogin = new SimpleStringProperty();
        private final ObjectProperty<HBox> actions = new SimpleObjectProperty<>();
        
        public UserItem(int id, String name, String email, String role, String status, String lastLogin) {
            this.id.set(id);
            this.name.set(name);
            this.email.set(email);
            this.role.set(role);
            this.status.set(status);
            this.lastLogin.set(lastLogin);
            
            // Create actions buttons for this user
            HBox actionBox = createActionButtons(id);
            this.actions.set(actionBox);
        }
        
        private HBox createActionButtons(int userId) {
            HBox hbox = new HBox(5); // 5 is the spacing
            hbox.setAlignment(Pos.CENTER);
            
            // Edit button
            Button editBtn = new Button();
            editBtn.getStyleClass().add("action-icon");
            
            // Create an inner shape for the edit icon
            Button editIcon = new Button();
            editIcon.getStyleClass().add("edit-icon");
            editIcon.setMinSize(15, 15);
            editIcon.setMaxSize(15, 15);
            
            editBtn.setGraphic(editIcon);
            editBtn.setTooltip(new Tooltip("Edit"));
            editBtn.setOnAction(e -> {
                // Call the controller method to handle editing
                // We need to use lookup to find the controller since we're in a static inner class
                javafx.scene.Scene scene = editBtn.getScene();
                if (scene != null) {
                    UserManagementController controller = (UserManagementController) scene.getUserData();
                    if (controller != null) {
                        // Use reflection to access the method even if it's private
                        try {
                            java.lang.reflect.Method method = UserManagementController.class.getDeclaredMethod("handleEditUser", int.class);
                            method.setAccessible(true);
                            method.invoke(controller, userId);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });
            
            // Delete button
            Button deleteBtn = new Button();
            deleteBtn.getStyleClass().add("action-icon");
            
            // Create an inner shape for the delete icon
            Button deleteIcon = new Button();
            deleteIcon.getStyleClass().add("delete-icon");
            deleteIcon.setMinSize(15, 15);
            deleteIcon.setMaxSize(15, 15);
            
            deleteBtn.setGraphic(deleteIcon);
            deleteBtn.setTooltip(new Tooltip("Delete"));
            deleteBtn.setOnAction(e -> {
                // Call the controller method to handle deletion
                javafx.scene.Scene scene = deleteBtn.getScene();
                if (scene != null) {
                    UserManagementController controller = (UserManagementController) scene.getUserData();
                    if (controller != null) {
                        // Use reflection to access the method even if it's private
                        try {
                            java.lang.reflect.Method method = UserManagementController.class.getDeclaredMethod("handleDeleteUser", int.class);
                            method.setAccessible(true);
                            method.invoke(controller, userId);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });
            
            hbox.getChildren().addAll(editBtn, deleteBtn);
            return hbox;
        }
        
        // Getters and setters
        public int getId() { return id.get(); }
        public IntegerProperty idProperty() { return id; }
        
        public String getName() { return name.get(); }
        public StringProperty nameProperty() { return name; }
        public void setName(String name) { this.name.set(name); }
        
        public String getEmail() { return email.get(); }
        public StringProperty emailProperty() { return email; }
        public void setEmail(String email) { this.email.set(email); }
        
        public String getRole() { return role.get(); }
        public StringProperty roleProperty() { return role; }
        public void setRole(String role) { this.role.set(role); }
        
        public String getStatus() { return status.get(); }
        public StringProperty statusProperty() { return status; }
        public void setStatus(String status) { this.status.set(status); }
        
        public String getLastLogin() { return lastLogin.get(); }
        public StringProperty lastLoginProperty() { return lastLogin; }
        public void setLastLogin(String lastLogin) { this.lastLogin.set(lastLogin); }
        
        public HBox getActions() { return actions.get(); }
        public ObjectProperty<HBox> actionsProperty() { return actions; }
    }
}