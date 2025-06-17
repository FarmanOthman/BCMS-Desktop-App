package com.bcms.viewmodel;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for Sale operations and data.
 */
public class SaleViewModel {
    
    // Sale properties
    private final StringProperty id = new SimpleStringProperty();
    private final ObjectProperty<CarViewModel> car = new SimpleObjectProperty<>();
    private final StringProperty customerId = new SimpleStringProperty();
    private final StringProperty customerName = new SimpleStringProperty();
    private final StringProperty customerEmail = new SimpleStringProperty();
    private final StringProperty customerPhone = new SimpleStringProperty();
    private final DoubleProperty salePrice = new SimpleDoubleProperty();
    private final DoubleProperty tax = new SimpleDoubleProperty();
    private final DoubleProperty fees = new SimpleDoubleProperty();
    private final ObjectProperty<LocalDateTime> saleDate = new SimpleObjectProperty<>();
    private final StringProperty paymentMethod = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();
    private final StringProperty notes = new SimpleStringProperty();
    private final ObjectProperty<List<SaleDocumentItem>> documents = new SimpleObjectProperty<>(new ArrayList<>());
    
    // Sample data
    private final ObservableList<SaleItem> allSales = FXCollections.observableArrayList();
    
    /**
     * Default constructor.
     */
    public SaleViewModel() {
        loadSampleData();
    }
    
    /**
     * Constructor with sale details.
     */
    public SaleViewModel(String id, CarViewModel car, String customerId, String customerName,
                        String customerEmail, String customerPhone, double salePrice,
                        double tax, double fees, LocalDateTime saleDate,
                        String paymentMethod, String status, String notes,
                        List<SaleDocumentItem> documents) {
        this.id.set(id);
        this.car.set(car);
        this.customerId.set(customerId);
        this.customerName.set(customerName);
        this.customerEmail.set(customerEmail);
        this.customerPhone.set(customerPhone);
        this.salePrice.set(salePrice);
        this.tax.set(tax);
        this.fees.set(fees);
        this.saleDate.set(saleDate);
        this.paymentMethod.set(paymentMethod);
        this.status.set(status);
        this.notes.set(notes);
        this.documents.set(documents);
    }
    
    /**
     * Load sample sales data.
     */
    private void loadSampleData() {
        // Add sample sales to the list
        allSales.add(new SaleItem(
            "#1001",
            "BMW X5 M Sport",
            "John Smith",
            "$62,500",
            "Completed",
            "June 12, 2025"
        ));
        
        allSales.add(new SaleItem(
            "#1002",
            "Tesla Model 3 Performance",
            "Emily Johnson",
            "$45,800",
            "Completed",
            "June 10, 2025"
        ));
        
        allSales.add(new SaleItem(
            "#1003",
            "Toyota Camry Hybrid SE",
            "Michael Brown",
            "$31,200",
            "Pending",
            "June 13, 2025"
        ));
        
        allSales.add(new SaleItem(
            "#1004",
            "Honda Civic Type R",
            "Sarah Wilson",
            "$26,800",
            "Completed",
            "June 5, 2025"
        ));
        
        allSales.add(new SaleItem(
            "#1005",
            "Ford Mustang GT",
            "David Lee",
            "$38,500",
            "Pending",
            "June 14, 2025"
        ));
    }
    
    /**
     * Get the total sale amount.
     */
    public double getTotalAmount() {
        return salePrice.get() + tax.get() + fees.get();
    }
    
    /**
     * Format the total amount as a currency string.
     */
    public String getFormattedTotalAmount() {
        return String.format("$%,.2f", getTotalAmount());
    }
    
    /**
     * Format the sale price as a currency string.
     */
    public String getFormattedSalePrice() {
        return String.format("$%,.2f", salePrice.get());
    }
    
    /**
     * Format the tax as a currency string.
     */
    public String getFormattedTax() {
        return String.format("$%,.2f", tax.get());
    }
    
    /**
     * Format the fees as a currency string.
     */
    public String getFormattedFees() {
        return String.format("$%,.2f", fees.get());
    }
    
    /**
     * Format the sale date as a string.
     */
    public String getFormattedSaleDate() {
        if (saleDate.get() == null) {
            return "";
        }
        return saleDate.get().format(DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a"));
    }
    
    /**
     * Get all sales for display.
     */
    public ObservableList<SaleItem> getAllSales() {
        return allSales;
    }
    
    /**
     * Search sales based on criteria.
     */
    public ObservableList<SaleItem> searchSales(String searchText, String statusFilter, String dateRange) {
        if ((searchText == null || searchText.isEmpty()) && 
            (statusFilter == null || statusFilter.equals("All Statuses")) && 
            (dateRange == null || dateRange.equals("All Time"))) {
            return allSales;
        }
        
        // Apply filters (implementation would filter based on criteria)
        return allSales;
    }
    
    // Getters and property methods
    public String getId() { return id.get(); }
    public StringProperty idProperty() { return id; }
    public void setId(String id) { this.id.set(id); }
    
    public CarViewModel getCar() { return car.get(); }
    public ObjectProperty<CarViewModel> carProperty() { return car; }
    public void setCar(CarViewModel car) { this.car.set(car); }
    
    public String getCustomerId() { return customerId.get(); }
    public StringProperty customerIdProperty() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId.set(customerId); }
    
    public String getCustomerName() { return customerName.get(); }
    public StringProperty customerNameProperty() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName.set(customerName); }
    
    public String getCustomerEmail() { return customerEmail.get(); }
    public StringProperty customerEmailProperty() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail.set(customerEmail); }
    
    public String getCustomerPhone() { return customerPhone.get(); }
    public StringProperty customerPhoneProperty() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone.set(customerPhone); }
    
    public double getSalePrice() { return salePrice.get(); }
    public DoubleProperty salePriceProperty() { return salePrice; }
    public void setSalePrice(double salePrice) { this.salePrice.set(salePrice); }
    
    public double getTax() { return tax.get(); }
    public DoubleProperty taxProperty() { return tax; }
    public void setTax(double tax) { this.tax.set(tax); }
    
    public double getFees() { return fees.get(); }
    public DoubleProperty feesProperty() { return fees; }
    public void setFees(double fees) { this.fees.set(fees); }
    
    public LocalDateTime getSaleDate() { return saleDate.get(); }
    public ObjectProperty<LocalDateTime> saleDateProperty() { return saleDate; }
    public void setSaleDate(LocalDateTime saleDate) { this.saleDate.set(saleDate); }
    
    public String getPaymentMethod() { return paymentMethod.get(); }
    public StringProperty paymentMethodProperty() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod.set(paymentMethod); }
    
    public String getStatus() { return status.get(); }
    public StringProperty statusProperty() { return status; }
    public void setStatus(String status) { this.status.set(status); }
    
    public String getNotes() { return notes.get(); }
    public StringProperty notesProperty() { return notes; }
    public void setNotes(String notes) { this.notes.set(notes); }
    
    public List<SaleDocumentItem> getDocuments() { return documents.get(); }
    public ObjectProperty<List<SaleDocumentItem>> documentsProperty() { return documents; }
    public void setDocuments(List<SaleDocumentItem> documents) { this.documents.set(documents); }
    
    /**
     * Sale item class for display in tables.
     */
    public static class SaleItem {
        private final StringProperty id = new SimpleStringProperty();
        private final StringProperty vehicle = new SimpleStringProperty();
        private final StringProperty customer = new SimpleStringProperty();
        private final StringProperty amount = new SimpleStringProperty();
        private final StringProperty status = new SimpleStringProperty();
        private final StringProperty date = new SimpleStringProperty();
        
        public SaleItem(String id, String vehicle, String customer, String amount, String status, String date) {
            this.id.set(id);
            this.vehicle.set(vehicle);
            this.customer.set(customer);
            this.amount.set(amount);
            this.status.set(status);
            this.date.set(date);
        }
        
        // Getters and property methods
        public String getId() { return id.get(); }
        public StringProperty idProperty() { return id; }
        
        public String getVehicle() { return vehicle.get(); }
        public StringProperty vehicleProperty() { return vehicle; }
        
        public String getCustomer() { return customer.get(); }
        public StringProperty customerProperty() { return customer; }
        
        public String getAmount() { return amount.get(); }
        public StringProperty amountProperty() { return amount; }
        
        public String getStatus() { return status.get(); }
        public StringProperty statusProperty() { return status; }
        
        public String getDate() { return date.get(); }
        public StringProperty dateProperty() { return date; }
    }
    
    /**
     * Document item class for sale documents.
     */
    public static class SaleDocumentItem {
        private final StringProperty name = new SimpleStringProperty();
        private final StringProperty type = new SimpleStringProperty();
        private final ObjectProperty<LocalDate> uploadDate = new SimpleObjectProperty<>();
        private final StringProperty path = new SimpleStringProperty();
        
        public SaleDocumentItem(String name, String type, LocalDate uploadDate, String path) {
            this.name.set(name);
            this.type.set(type);
            this.uploadDate.set(uploadDate);
            this.path.set(path);
        }
        
        // Getters and property methods
        public String getName() { return name.get(); }
        public StringProperty nameProperty() { return name; }
        
        public String getType() { return type.get(); }
        public StringProperty typeProperty() { return type; }
        
        public LocalDate getUploadDate() { return uploadDate.get(); }
        public ObjectProperty<LocalDate> uploadDateProperty() { return uploadDate; }
        
        public String getPath() { return path.get(); }
        public StringProperty pathProperty() { return path; }
        
        /**
         * Format the upload date as a string.
         */
        public String getFormattedUploadDate() {
            if (uploadDate.get() == null) {
                return "";
            }
            return uploadDate.get().format(DateTimeFormatter.ofPattern("MMM d, yyyy"));
        }
    }
}