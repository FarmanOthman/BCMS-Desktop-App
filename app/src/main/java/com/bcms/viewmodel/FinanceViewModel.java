package com.bcms.viewmodel;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;

import java.time.LocalDate;
import java.util.stream.Collectors;

/**
 * ViewModel for the Finance screen that handles the business logic
 * and data for the Finance UI.
 */
public class FinanceViewModel {
    
    private final ObservableList<FinanceRecord> allRecords = FXCollections.observableArrayList();
    
    /**
     * Initialize the ViewModel with sample data.
     * In a real application, this would fetch data from a repository or service.
     */
    public FinanceViewModel() {
        loadSampleData();
    }
    
    /**
     * Load sample data for the finance records.
     * In a real application, this would call a repository or service.
     */
    private void loadSampleData() {
        // Add sample finance records
        allRecords.add(new FinanceRecord(
            "#F001", 
            RecordType.EXPENSE, 
            "Electricity", 
            150.00, 
            "Monthly showroom power bill for June",
            LocalDate.of(2025, 6, 9)
        ));
        
        allRecords.add(new FinanceRecord(
            "#F002", 
            RecordType.EXPENSE, 
            "Office Supplies", 
            87.50, 
            "Printer ink and paper restock",
            LocalDate.of(2025, 6, 8)
        ));
        
        allRecords.add(new FinanceRecord(
            "#F003", 
            RecordType.INCOME, 
            "Detailing Service", 
            500.00, 
            "Full interior/exterior detail on client vehicle",
            LocalDate.of(2025, 6, 7)
        ));
        
        // Add more sample data as needed
        allRecords.add(new FinanceRecord(
            "#F004", 
            RecordType.EXPENSE, 
            "Internet Service", 
            79.99, 
            "Monthly internet service bill",
            LocalDate.of(2025, 6, 5)
        ));
        
        allRecords.add(new FinanceRecord(
            "#F005", 
            RecordType.INCOME, 
            "Repair Service", 
            350.00, 
            "Transmission repair for customer vehicle",
            LocalDate.of(2025, 6, 3)
        ));
    }
    
    /**
     * Gets a filtered list of finance records based on search criteria and filters
     */
    public ObservableList<FinanceRecord> getFilteredRecords(String searchText, String typeFilter, String timeFilter) {
        // If no filters are applied, return all records
        if ((searchText == null || searchText.isEmpty()) && 
            (typeFilter == null || typeFilter.equals("All Type")) && 
            (timeFilter == null || timeFilter.equals("All Time"))) {
            return allRecords;
        }
        
        // Apply filters
        return allRecords.stream()
            .filter(record -> {
                // Apply search text filter
                boolean matchesSearch = searchText == null || searchText.isEmpty() || 
                    record.getCategory().toLowerCase().contains(searchText.toLowerCase()) ||
                    record.getDescription().toLowerCase().contains(searchText.toLowerCase());
                
                // Apply type filter
                boolean matchesType = typeFilter == null || typeFilter.equals("All Type");
                
                if (!matchesType && typeFilter != null) {
                    if (typeFilter.equals("Expense")) {
                        matchesType = record.getType() == RecordType.EXPENSE;
                    } else if (typeFilter.equals("Income")) {
                        matchesType = record.getType() == RecordType.INCOME;
                    }
                }
                
                // Apply time filter (implementation would depend on how you define time ranges)
                boolean matchesTimeFilter = timeFilter == null || timeFilter.equals("All Time");
                
                // You could implement more sophisticated time filtering here based on the record date
                
                return matchesSearch && matchesType && matchesTimeFilter;
            })
            .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }
    
    /**
     * Add a new finance record
     */
    public void addRecord(FinanceRecord record) {
        allRecords.add(record);
    }
    
    /**
     * Remove a finance record by ID
     */
    public boolean removeRecord(String id) {
        return allRecords.removeIf(record -> record.getId().equals(id));
    }
    
    /**
     * Get the complete list of all finance records
     */
    public ObservableList<FinanceRecord> getAllRecords() {
        return allRecords;
    }
    
    /**
     * Calculate total expenses
     */
    public double getTotalExpenses() {
        return allRecords.stream()
            .filter(record -> record.getType() == RecordType.EXPENSE)
            .mapToDouble(FinanceRecord::getAmount)
            .sum();
    }
    
    /**
     * Calculate total income
     */
    public double getTotalIncome() {
        return allRecords.stream()
            .filter(record -> record.getType() == RecordType.INCOME)
            .mapToDouble(FinanceRecord::getAmount)
            .sum();
    }
    
    /**
     * Enum for record types
     */
    public enum RecordType {
        EXPENSE,
        INCOME
    }
    
    /**
     * Finance record class to represent financial transactions
     */
    public static class FinanceRecord {
        private final StringProperty id = new SimpleStringProperty();
        private final ObjectProperty<RecordType> type = new SimpleObjectProperty<>();
        private final StringProperty category = new SimpleStringProperty();
        private final DoubleProperty amount = new SimpleDoubleProperty();
        private final StringProperty description = new SimpleStringProperty();
        private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
        private final ObjectProperty<HBox> actions = new SimpleObjectProperty<>();
        
        public FinanceRecord(String id, RecordType type, String category, double amount, 
                            String description, LocalDate date) {
            this.id.set(id);
            this.type.set(type);
            this.category.set(category);
            this.amount.set(amount);
            this.description.set(description);
            this.date.set(date);
            
            // Create actions buttons for this record
            HBox actionBox = createActionButtons(id);
            this.actions.set(actionBox);
        }
        
        private HBox createActionButtons(String recordId) {
            HBox hbox = new HBox(5); // 5 is the spacing
            hbox.setAlignment(Pos.CENTER);
            
            // Edit button
            Button editBtn = new Button();
            editBtn.getStyleClass().addAll("action-icon");
            
            // Create an inner shape for the edit icon
            Button editIcon = new Button();
            editIcon.getStyleClass().add("edit-icon");
            editIcon.setMinSize(15, 15);
            editIcon.setMaxSize(15, 15);
            
            editBtn.setGraphic(editIcon);
            editBtn.setTooltip(new Tooltip("Edit"));
            editBtn.setOnAction(e -> System.out.println("Edit record: " + recordId));
            
            // Delete button
            Button deleteBtn = new Button();
            deleteBtn.getStyleClass().addAll("action-icon");
            
            // Create an inner shape for the delete icon
            Button deleteIcon = new Button();
            deleteIcon.getStyleClass().add("delete-icon");
            deleteIcon.setMinSize(15, 15);
            deleteIcon.setMaxSize(15, 15);
            
            deleteBtn.setGraphic(deleteIcon);
            deleteBtn.setTooltip(new Tooltip("Delete"));
            deleteBtn.setOnAction(e -> System.out.println("Delete record: " + recordId));
            
            hbox.getChildren().addAll(editBtn, deleteBtn);
            return hbox;
        }
        
        // Getters and property methods
        public String getId() { return id.get(); }
        public StringProperty idProperty() { return id; }
        
        public RecordType getType() { return type.get(); }
        public ObjectProperty<RecordType> typeProperty() { return type; }
        
        public String getTypeString() {
            return type.get() == RecordType.EXPENSE ? "Expense" : "Income";
        }
        
        public StringProperty typeStringProperty() {
            StringProperty typeStringProperty = new SimpleStringProperty();
            typeStringProperty.bind(type.map(t -> t == RecordType.EXPENSE ? "Expense" : "Income"));
            return typeStringProperty;
        }
        
        public String getCategory() { return category.get(); }
        public StringProperty categoryProperty() { return category; }
        
        public double getAmount() { return amount.get(); }
        public DoubleProperty amountProperty() { return amount; }
        
        // Format amount as currency with dollar sign
        public String getFormattedAmount() {
            return String.format("$%.2f", amount.get());
        }
        
        public StringProperty formattedAmountProperty() {
            StringProperty formattedProperty = new SimpleStringProperty();
            formattedProperty.bind(amount.asString("$%.2f"));
            return formattedProperty;
        }
        
        public String getDescription() { return description.get(); }
        public StringProperty descriptionProperty() { return description; }
        
        public LocalDate getDate() { return date.get(); }
        public ObjectProperty<LocalDate> dateProperty() { return date; }
        
        // Format date as YYYY-MM-DD
        public String getFormattedDate() {
            return date.get().toString();
        }
        
        public HBox getActions() { return actions.get(); }
        public ObjectProperty<HBox> actionsProperty() { return actions; }
    }
}