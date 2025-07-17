package com.bcms.viewmodel;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * ViewModel for the Finance screen that handles the business logic
 * and data for the Finance UI.
 */
public class FinanceViewModel {
    
    private final ObservableList<TransactionItem> transactions = FXCollections.observableArrayList();
    
    /**
     * Initialize the ViewModel with sample data.
     * In a real application, this would fetch data from a repository or service.
     */
    public FinanceViewModel() {
        loadSampleData();
    }
    
    /**
     * Load sample data for the transactions.
     * In a real application, this would call a repository or service.
     */
    private void loadSampleData() {
        // Add sample transactions
        transactions.add(new TransactionItem(
            "2023-06-01",
            "Vehicle Purchase - Toyota Camry",
            "Inventory",
            -28500.00,
            125000.00
        ));
        
        transactions.add(new TransactionItem(
            "2023-06-02",
            "Vehicle Sale - Honda Accord",
            "Sales",
            32000.00,
            157000.00
        ));
        
        transactions.add(new TransactionItem(
            "2023-06-03",
            "Monthly Rent Payment",
            "Facilities",
            -8500.00,
            148500.00
        ));
        
        transactions.add(new TransactionItem(
            "2023-06-05",
            "Vehicle Sale - Ford F-150",
            "Sales",
            45000.00,
            193500.00
        ));
        
        transactions.add(new TransactionItem(
            "2023-06-07",
            "Marketing Campaign",
            "Marketing",
            -12000.00,
            181500.00
        ));
        
        transactions.add(new TransactionItem(
            "2023-06-10",
            "Staff Salaries",
            "Payroll",
            -35000.00,
            146500.00
        ));
        
        transactions.add(new TransactionItem(
            "2023-06-12",
            "Vehicle Purchase - BMW X5",
            "Inventory",
            -52000.00,
            94500.00
        ));
        
        transactions.add(new TransactionItem(
            "2023-06-15",
            "Vehicle Sale - Lexus RX",
            "Sales",
            48000.00,
            142500.00
        ));
        
        transactions.add(new TransactionItem(
            "2023-06-18",
            "Insurance Payment",
            "Insurance",
            -4500.00,
            138000.00
        ));
        
        transactions.add(new TransactionItem(
            "2023-06-20",
            "Vehicle Sale - Tesla Model 3",
            "Sales",
            55000.00,
            193000.00
        ));
    }
    
    /**
     * Get the list of transactions
     */
    public ObservableList<TransactionItem> getTransactions() {
        return transactions;
    }
    
    /**
     * Transaction item class to represent financial transactions
     */
    public static class TransactionItem {
        private final StringProperty date = new SimpleStringProperty();
        private final StringProperty description = new SimpleStringProperty();
        private final StringProperty category = new SimpleStringProperty();
        private final DoubleProperty amount = new SimpleDoubleProperty();
        private final DoubleProperty balance = new SimpleDoubleProperty();
        
        public TransactionItem(String date, String description, String category, double amount, double balance) {
            this.date.set(date);
            this.description.set(description);
            this.category.set(category);
            this.amount.set(amount);
            this.balance.set(balance);
        }
        
        // Getters
        public String getDate() {
            return date.get();
        }
        
        public StringProperty dateProperty() {
            return date;
        }
        
        public String getDescription() {
            return description.get();
        }
        
        public StringProperty descriptionProperty() {
            return description;
        }
        
        public String getCategory() {
            return category.get();
        }
        
        public StringProperty categoryProperty() {
            return category;
        }
        
        public double getAmount() {
            return amount.get();
        }
        
        public DoubleProperty amountProperty() {
            return amount;
        }
        
        public double getBalance() {
            return balance.get();
        }
        
        public DoubleProperty balanceProperty() {
            return balance;
        }
        
        // Setters
        public void setDate(String date) {
            this.date.set(date);
        }
        
        public void setDescription(String description) {
            this.description.set(description);
        }
        
        public void setCategory(String category) {
            this.category.set(category);
        }
        
        public void setAmount(double amount) {
            this.amount.set(amount);
        }
        
        public void setBalance(double balance) {
            this.balance.set(balance);
        }
    }
}