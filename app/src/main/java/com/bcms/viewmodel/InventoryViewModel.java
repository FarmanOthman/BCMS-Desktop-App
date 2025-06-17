package com.bcms.viewmodel;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.geometry.Pos;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * ViewModel for the Inventory screen that handles the business logic
 * and data for the Inventory UI.
 */
public class InventoryViewModel {
    
    private final ObservableList<CarItem> allCars = FXCollections.observableArrayList();
    
    /**
     * Initialize the ViewModel with sample data.
     * In a real application, this would fetch data from a repository or service.
     */
    public InventoryViewModel() {
        loadSampleData();
    }
    
    /**
     * Load sample data for the inventory.
     * In a real application, this would call a repository or service.
     */
    private void loadSampleData() {
        // Add sample cars to the inventory
        allCars.add(new CarItem(
            "#142", 
            "BMW X5 M Sport", 
            2022, 
            58900.00, 
            "Available", 
            "WBXPA934...", 
            "2 days ago"
        ));
        
        allCars.add(new CarItem(
            "#141", 
            "Tesla Model 3 Performance", 
            2023, 
            42500.00, 
            "Sold", 
            "5YJ3E1EA...", 
            "1 week ago"
        ));
        
        allCars.add(new CarItem(
            "#140", 
            "Ford F-150 Raptor", 
            2021, 
            38200.00, 
            "In Repair", 
            "1FTFW1E5...", 
            "3 days ago"
        ));
        
        allCars.add(new CarItem(
            "#139", 
            "Toyota Camry Hybrid LE", 
            2022, 
            27800.00, 
            "Available", 
            "4T1B11HK...", 
            "5 days ago"
        ));
        
        allCars.add(new CarItem(
            "#138", 
            "Honda Civic Type R", 
            2023, 
            24300.00, 
            "Sold", 
            "19XFC2F5...", 
            "1 week ago"
        ));
    }
    
    /**
     * Gets a filtered list of cars based on search criteria and filters
     */
    public ObservableList<CarItem> getFilteredCars(String searchText, String statusFilter, String yearRange) {
        // If no filters are applied, return all cars
        if ((searchText == null || searchText.isEmpty()) && 
            (statusFilter == null || statusFilter.equals("All Statuses")) && 
            (yearRange == null || yearRange.equals("All Years"))) {
            return allCars;
        }
        
        // Apply filters
        return allCars.stream()
            .filter(car -> {
                // Apply search text filter
                boolean matchesSearch = searchText == null || searchText.isEmpty() || 
                    car.getVehicle().toLowerCase().contains(searchText) ||
                    car.getVin().toLowerCase().contains(searchText) ||
                    String.valueOf(car.getYear()).contains(searchText);
                
                // Apply status filter
                boolean matchesStatus = statusFilter == null || statusFilter.equals("All Statuses") || 
                    car.getStatus().equals(statusFilter);
                
                // Apply year range filter
                boolean matchesYearRange = yearRange == null || yearRange.equals("All Years");
                
                if (!matchesYearRange && yearRange != null) {
                    // Parse the year range
                    String[] years = yearRange.split(" - ");
                    if (years.length == 2) {
                        int startYear = Integer.parseInt(years[0]);
                        int endYear = Integer.parseInt(years[1]);
                        matchesYearRange = car.getYear() >= startYear && car.getYear() <= endYear;
                    }
                }
                
                return matchesSearch && matchesStatus && matchesYearRange;
            })
            .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }
    
    /**
     * Get the complete list of all cars.
     */
    public ObservableList<CarItem> getAllCars() {
        return allCars;
    }
    
    /**
     * Car item class to represent cars in the inventory.
     */
    public static class CarItem {
        private final StringProperty id = new SimpleStringProperty();
        private final StringProperty vehicle = new SimpleStringProperty();
        private final IntegerProperty year = new SimpleIntegerProperty();
        private final DoubleProperty price = new SimpleDoubleProperty();
        private final StringProperty status = new SimpleStringProperty();
        private final StringProperty vin = new SimpleStringProperty();
        private final StringProperty added = new SimpleStringProperty();
        private final ObjectProperty<HBox> actions = new SimpleObjectProperty<>();
        
        public CarItem(String id, String vehicle, int year, double price, String status, String vin, String added) {
            this.id.set(id);
            this.vehicle.set(vehicle);
            this.year.set(year);
            this.price.set(price);
            this.status.set(status);
            this.vin.set(vin);
            this.added.set(added);
            
            // Create actions buttons for this car
            HBox actionBox = createActionButtons(id);
            this.actions.set(actionBox);
        }
        
        private HBox createActionButtons(String carId) {
            HBox hbox = new HBox(5); // 5 is the spacing
            hbox.setAlignment(Pos.CENTER);
            
            // Edit button
            Button editBtn = new Button();
            editBtn.getStyleClass().add("action-icon");
            editBtn.setTooltip(new Tooltip("Edit"));
            editBtn.setOnAction(e -> System.out.println("Edit car: " + carId));
            
            // Delete button
            Button deleteBtn = new Button();
            deleteBtn.getStyleClass().add("action-icon");
            deleteBtn.setTooltip(new Tooltip("Delete"));
            deleteBtn.setOnAction(e -> System.out.println("Delete car: " + carId));
            
            // View button
            Button viewBtn = new Button();
            viewBtn.getStyleClass().add("action-icon");
            viewBtn.setTooltip(new Tooltip("View Details"));
            viewBtn.setOnAction(e -> System.out.println("View car: " + carId));
            
            hbox.getChildren().addAll(editBtn, deleteBtn, viewBtn);
            return hbox;
        }
        
        // Getters and Property methods
        public String getId() { return id.get(); }
        public StringProperty idProperty() { return id; }
        
        public String getVehicle() { return vehicle.get(); }
        public StringProperty vehicleProperty() { return vehicle; }
        
        public int getYear() { return year.get(); }
        public IntegerProperty yearProperty() { return year; }
        
        public double getPrice() { return price.get(); }
        public DoubleProperty priceProperty() { return price; }
        
        // Format price as currency
        public String getFormattedPrice() {
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
            return currencyFormatter.format(price.get());
        }
        
        public String getStatus() { return status.get(); }
        public StringProperty statusProperty() { return status; }
        
        public String getVin() { return vin.get(); }
        public StringProperty vinProperty() { return vin; }
        
        public String getAdded() { return added.get(); }
        public StringProperty addedProperty() { return added; }
        
        public HBox getActions() { return actions.get(); }
        public ObjectProperty<HBox> actionsProperty() { return actions; }
    }
}