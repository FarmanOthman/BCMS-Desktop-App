package com.bcms.viewmodel;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;

import java.util.stream.Collectors;

/**
 * ViewModel for the Buyers screen that handles the business logic
 * and data for the Buyers UI.
 */
public class BuyerViewModel {
    
    private final ObservableList<BuyerItem> allBuyers = FXCollections.observableArrayList();
    
    /**
     * Initialize the ViewModel with sample data.
     * In a real application, this would fetch data from a repository or service.
     */
    public BuyerViewModel() {
        loadSampleData();
    }
    
    /**
     * Load sample data for the buyers.
     * In a real application, this would call a repository or service.
     */
    private void loadSampleData() {
        // Add sample buyers
        allBuyers.add(new BuyerItem(
            1, 
            "John Smith", 
            "john@example.com", 
            "+1 555-1234", 
            "123 Main St, NY",
            "Toyota Camry"
        ));
        
        allBuyers.add(new BuyerItem(
            2, 
            "Jane Doe", 
            "jane@domain.com", 
            "+1 555-5678", 
            "456 Oak Ave, CA",
            "Honda Civic"
        ));
        
        allBuyers.add(new BuyerItem(
            3, 
            "Mike Johnson", 
            "mike.johnson@outlook.com", 
            "+1 555-9012", 
            "789 Pine Rd, TX",
            "Ford F-150"
        ));
        
        allBuyers.add(new BuyerItem(
            4, 
            "Sarah Wilson", 
            "sarah.w@gmail.com", 
            "+1 555-3456", 
            "321 Elm St, FL",
            "BMW X3"
        ));
    }
    
    /**
     * Gets a filtered list of buyers based on search criteria and filters
     */
    public ObservableList<BuyerItem> getFilteredBuyers(String searchText, String buyerTypeFilter, String statusFilter) {
        // If no filters are applied, return all buyers
        if ((searchText == null || searchText.isEmpty()) && 
            (buyerTypeFilter == null || buyerTypeFilter.equals("All Buyers")) && 
            (statusFilter == null || statusFilter.equals("All Status"))) {
            return allBuyers;
        }
        
        // Apply filters
        return allBuyers.stream()
            .filter(buyer -> {
                // Apply search text filter
                boolean matchesSearch = searchText == null || searchText.isEmpty() || 
                    buyer.getName().toLowerCase().contains(searchText) ||
                    buyer.getEmail().toLowerCase().contains(searchText);
                
                // Apply buyer type filter (this is a placeholder - in a real app you'd have a more sophisticated filter)
                boolean matchesBuyerType = buyerTypeFilter == null || buyerTypeFilter.equals("All Buyers");
                
                // Apply status filter (this is a placeholder - in a real app you'd have a status field)
                boolean matchesStatus = statusFilter == null || statusFilter.equals("All Status");
                
                if (statusFilter != null && statusFilter.equals("Active Only")) {
                    // In a real app, you'd check a status field
                    matchesStatus = true; // Assuming all buyers are active for this example
                }
                
                return matchesSearch && matchesBuyerType && matchesStatus;
            })
            .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }
    
    /**
     * Get the buyer by ID
     */
    public BuyerItem getBuyerById(int id) {
        for (BuyerItem buyer : allBuyers) {
            if (buyer.getId() == id) {
                return buyer;
            }
        }
        return null;
    }
    
    /**
     * Add a new buyer
     */
    public void addBuyer(BuyerItem buyer) {
        allBuyers.add(buyer);
    }
    
    /**
     * Update an existing buyer
     */
    public void updateBuyer(BuyerItem updatedBuyer) {
        for (int i = 0; i < allBuyers.size(); i++) {
            if (allBuyers.get(i).getId() == updatedBuyer.getId()) {
                allBuyers.set(i, updatedBuyer);
                break;
            }
        }
    }
    
    /**
     * Remove a buyer by ID
     */
    public boolean removeBuyer(int id) {
        return allBuyers.removeIf(buyer -> buyer.getId() == id);
    }
    
    /**
     * Get the complete list of all buyers
     */
    public ObservableList<BuyerItem> getAllBuyers() {
        return allBuyers;
    }
    
    /**
     * Buyer item class to represent buyers in the system
     */
    public static class BuyerItem {
        private final IntegerProperty id = new SimpleIntegerProperty();
        private final StringProperty name = new SimpleStringProperty();
        private final StringProperty email = new SimpleStringProperty();
        private final StringProperty phone = new SimpleStringProperty();
        private final StringProperty address = new SimpleStringProperty();
        private final StringProperty carName = new SimpleStringProperty();
        private final ObjectProperty<HBox> actions = new SimpleObjectProperty<>();
        
        public BuyerItem(int id, String name, String email, String phone, String address, String carName) {
            this.id.set(id);
            this.name.set(name);
            this.email.set(email);
            this.phone.set(phone);
            this.address.set(address);
            this.carName.set(carName);
            
            // Create actions buttons for this buyer
            HBox actionBox = createActionButtons(id);
            this.actions.set(actionBox);
        }
        
        private HBox createActionButtons(int buyerId) {
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
                // Call edit method from controller
                // We'll use reflection to get the controller and call the method
                try {
                    // Get the current scene
                    javafx.scene.Scene scene = editBtn.getScene();
                    // Get the controller from the scene's user data
                    Object controller = scene.getUserData();
                    // If controller is null, try to get it from the root's user data
                    if (controller == null) {
                        controller = scene.getRoot().getUserData();
                    }
                    
                    if (controller != null && controller instanceof com.bcms.controller.BuyerController) {
                        // Call the handleEditBuyer method using reflection
                        java.lang.reflect.Method method = controller.getClass().getDeclaredMethod("handleEditBuyer", int.class);
                        method.setAccessible(true);
                        method.invoke(controller, buyerId);
                    } else {
                        System.out.println("Controller not found or not a BuyerController");
                    }
                } catch (Exception ex) {
                    System.err.println("Error calling handleEditBuyer: " + ex.getMessage());
                    ex.printStackTrace();
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
                // Call delete method
                try {
                    // Get the current scene
                    javafx.scene.Scene scene = deleteBtn.getScene();
                    // Get the controller from the scene's user data
                    Object controller = scene.getUserData();
                    // If controller is null, try to get it from the root's user data
                    if (controller == null) {
                        controller = scene.getRoot().getUserData();
                    }
                    
                    if (controller != null && controller instanceof com.bcms.controller.BuyerController) {
                        // Call the handleDeleteBuyer method using reflection
                        java.lang.reflect.Method method = controller.getClass().getDeclaredMethod("handleDeleteBuyer", int.class);
                        method.setAccessible(true);
                        method.invoke(controller, buyerId);
                    } else {
                        System.out.println("Controller not found or not a BuyerController");
                    }
                } catch (Exception ex) {
                    System.err.println("Error calling handleDeleteBuyer: " + ex.getMessage());
                    ex.printStackTrace();
                }
            });
            
            hbox.getChildren().addAll(editBtn, deleteBtn);
            return hbox;
        }
        
        public int getId() {
            return id.get();
        }
        
        public IntegerProperty idProperty() {
            return id;
        }
        
        public String getName() {
            return name.get();
        }
        
        public StringProperty nameProperty() {
            return name;
        }
        
        public String getEmail() {
            return email.get();
        }
        
        public StringProperty emailProperty() {
            return email;
        }
        
        public String getPhone() {
            return phone.get();
        }
        
        public StringProperty phoneProperty() {
            return phone;
        }
        
        public String getAddress() {
            return address.get();
        }
        
        public StringProperty addressProperty() {
            return address;
        }
        
        public String getCarName() {
            return carName.get();
        }
        
        public StringProperty carNameProperty() {
            return carName;
        }
        
        public HBox getActions() {
            return actions.get();
        }
        
        public ObjectProperty<HBox> actionsProperty() {
            return actions;
        }
        
        // Setter methods
        public void setName(String name) {
            this.name.set(name);
        }
        
        public void setEmail(String email) {
            this.email.set(email);
        }
        
        public void setPhone(String phone) {
            this.phone.set(phone);
        }
        
        public void setAddress(String address) {
            this.address.set(address);
        }
        
        public void setCarName(String carName) {
            this.carName.set(carName);
        }
    }
}