package com.bcms.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class representing a car in the inventory.
 */
public class Car {
    
    private final StringProperty id = new SimpleStringProperty();
    private final StringProperty make = new SimpleStringProperty();
    private final StringProperty model = new SimpleStringProperty();
    private final IntegerProperty year = new SimpleIntegerProperty();
    private final DoubleProperty price = new SimpleDoubleProperty();
    private final StringProperty status = new SimpleStringProperty();
    private final StringProperty vin = new SimpleStringProperty();
    private final StringProperty notes = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> addedDate = new SimpleObjectProperty<>();
    private final ListProperty<RepairPart> repairParts = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ListProperty<String> images = new SimpleListProperty<>(FXCollections.observableArrayList());
    
    /**
     * Default constructor.
     */
    public Car() {
        // Default constructor
    }
    
    /**
     * Constructor with basic properties.
     */
    public Car(String id, String make, String model, int year, double price, String status, String vin) {
        this.id.set(id);
        this.make.set(make);
        this.model.set(model);
        this.year.set(year);
        this.price.set(price);
        this.status.set(status);
        this.vin.set(vin);
        this.addedDate.set(LocalDate.now());
    }
    
    /**
     * Full constructor with all properties.
     */
    public Car(String id, String make, String model, int year, double price, String status, 
              String vin, String notes, LocalDate addedDate, 
              List<RepairPart> repairParts, List<String> images) {
        this.id.set(id);
        this.make.set(make);
        this.model.set(model);
        this.year.set(year);
        this.price.set(price);
        this.status.set(status);
        this.vin.set(vin);
        this.notes.set(notes);
        this.addedDate.set(addedDate);
        
        if (repairParts != null) {
            this.repairParts.setAll(repairParts);
        }
        
        if (images != null) {
            this.images.setAll(images);
        }
    }
    
    // Getters and property methods
    public String getId() { return id.get(); }
    public StringProperty idProperty() { return id; }
    public void setId(String id) { this.id.set(id); }
    
    public String getMake() { return make.get(); }
    public StringProperty makeProperty() { return make; }
    public void setMake(String make) { this.make.set(make); }
    
    public String getModel() { return model.get(); }
    public StringProperty modelProperty() { return model; }
    public void setModel(String model) { this.model.set(model); }
    
    public int getYear() { return year.get(); }
    public IntegerProperty yearProperty() { return year; }
    public void setYear(int year) { this.year.set(year); }
    
    public double getPrice() { return price.get(); }
    public DoubleProperty priceProperty() { return price; }
    public void setPrice(double price) { this.price.set(price); }
    
    // Format price as currency
    public String getFormattedPrice() {
        return String.format("$%,.2f", price.get());
    }
    
    public String getStatus() { return status.get(); }
    public StringProperty statusProperty() { return status; }
    public void setStatus(String status) { this.status.set(status); }
    
    public String getVin() { return vin.get(); }
    public StringProperty vinProperty() { return vin; }
    public void setVin(String vin) { this.vin.set(vin); }
    
    public String getNotes() { return notes.get(); }
    public StringProperty notesProperty() { return notes; }
    public void setNotes(String notes) { this.notes.set(notes); }
    
    public LocalDate getAddedDate() { return addedDate.get(); }
    public ObjectProperty<LocalDate> addedDateProperty() { return addedDate; }
    public void setAddedDate(LocalDate addedDate) { this.addedDate.set(addedDate); }
    
    // Format added date
    public String getFormattedAddedDate() {
        if (addedDate.get() == null) {
            return "";
        }
        return addedDate.get().toString();
    }
    
    public ObservableList<RepairPart> getRepairParts() { return repairParts.get(); }
    public ListProperty<RepairPart> repairPartsProperty() { return repairParts; }
    public void setRepairParts(ObservableList<RepairPart> repairParts) { this.repairParts.set(repairParts); }
    
    public ObservableList<String> getImages() { return images.get(); }
    public ListProperty<String> imagesProperty() { return images; }
    public void setImages(ObservableList<String> images) { this.images.set(images); }
    
    /**
     * Add a repair part to the car.
     * 
     * @param repairPart The repair part to add
     */
    public void addRepairPart(RepairPart repairPart) {
        this.repairParts.add(repairPart);
    }
    
    /**
     * Remove a repair part from the car.
     * 
     * @param repairPart The repair part to remove
     * @return true if the repair part was removed, false otherwise
     */
    public boolean removeRepairPart(RepairPart repairPart) {
        return this.repairParts.remove(repairPart);
    }
    
    /**
     * Add an image to the car.
     * 
     * @param imagePath The path to the image
     */
    public void addImage(String imagePath) {
        this.images.add(imagePath);
    }
    
    /**
     * Remove an image from the car.
     * 
     * @param imagePath The path to the image
     * @return true if the image was removed, false otherwise
     */
    public boolean removeImage(String imagePath) {
        return this.images.remove(imagePath);
    }
    
    /**
     * Get the full vehicle name (make + model + year).
     * 
     * @return The full vehicle name
     */
    public String getFullName() {
        return String.format("%s %s %d", make.get(), model.get(), year.get());
    }
    
    /**
     * Calculate the total repair costs.
     * 
     * @return The total repair costs
     */
    public double getTotalRepairCosts() {
        double total = 0.0;
        for (RepairPart part : repairParts) {
            total += part.getCost();
        }
        return total;
    }
    
    /**
     * Format the total repair costs as currency.
     * 
     * @return The formatted total repair costs
     */
    public String getFormattedTotalRepairCosts() {
        return String.format("$%,.2f", getTotalRepairCosts());
    }
    
    @Override
    public String toString() {
        return "Car{" +
                "id=" + id.get() +
                ", make=" + make.get() +
                ", model=" + model.get() +
                ", year=" + year.get() +
                ", price=" + price.get() +
                ", status=" + status.get() +
                ", vin=" + vin.get() +
                '}';
    }
    
    /**
     * Inner class representing a repair part.
     */
    public static class RepairPart {
        private final StringProperty name = new SimpleStringProperty();
        private final DoubleProperty cost = new SimpleDoubleProperty();
        
        /**
         * Default constructor.
         */
        public RepairPart() {
            // Default constructor
        }
        
        /**
         * Constructor with name and cost.
         */
        public RepairPart(String name, double cost) {
            this.name.set(name);
            this.cost.set(cost);
        }
        
        // Getters and property methods
        public String getName() { return name.get(); }
        public StringProperty nameProperty() { return name; }
        public void setName(String name) { this.name.set(name); }
        
        public double getCost() { return cost.get(); }
        public DoubleProperty costProperty() { return cost; }
        public void setCost(double cost) { this.cost.set(cost); }
        
        // Format cost as currency
        public String getFormattedCost() {
            return String.format("$%,.2f", cost.get());
        }
        
        @Override
        public String toString() {
            return name.get() + " - " + getFormattedCost();
        }
    }
}