package com.bcms.viewmodel;

import com.bcms.model.RepairService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class RepairsServiceViewModel {

    // Observable list for the repairs table
    private final ObservableList<RepairItem> repairsList = FXCollections.observableArrayList();
    private final ObservableList<RepairItem> filteredList = FXCollections.observableArrayList();
    
    // Callback for edit action
    private Consumer<RepairItem> editRepairHandler;
    
    public RepairsServiceViewModel() {
        // Initialize with empty data
    }
    
    // Method to set the edit repair handler
    public void setEditRepairHandler(Consumer<RepairItem> handler) {
        this.editRepairHandler = handler;
    }
    
    // Get repairs list for binding to TableView
    public ObservableList<RepairItem> getRepairsList() {
        return filteredList.isEmpty() ? repairsList : filteredList;
    }
    
    // Load sample repair data (in a real app, this would come from a database)
    public void loadSampleData() {
        // Clear existing data
        repairsList.clear();
        
        // Add sample data
        repairsList.addAll(
            new RepairItem("R-1001", "BMW X5 M Sport", "Oil Change", "Completed", "John Smith", 
                    LocalDate.of(2023, 6, 10), 95.50),
            new RepairItem("R-1002", "Tesla Model Y", "Battery Diagnostic", "In Progress", "Mike Johnson", 
                    LocalDate.of(2023, 6, 12), 150.00),
            new RepairItem("R-1003", "Mercedes-Benz GLE", "Brake Pad Replacement", "Completed", "Robert Chen", 
                    LocalDate.of(2023, 6, 15), 320.75),
            new RepairItem("R-1004", "Toyota Camry", "Transmission Service", "Pending", "John Smith", 
                    LocalDate.of(2023, 6, 20), 450.00),
            new RepairItem("R-1005", "Audi Q7", "Wheel Alignment", "Completed", "Sarah Williams", 
                    LocalDate.of(2023, 6, 22), 110.25),
            new RepairItem("R-1006", "Honda Accord", "AC Repair", "In Progress", "Mike Johnson", 
                    LocalDate.of(2023, 6, 25), 275.50),
            new RepairItem("R-1007", "Ford F-150", "Engine Diagnostic", "Completed", "Robert Chen", 
                    LocalDate.of(2023, 6, 28), 175.00),
            new RepairItem("R-1008", "Chevrolet Malibu", "Radiator Replacement", "Completed", "Sarah Williams", 
                    LocalDate.of(2023, 7, 2), 385.25),
            new RepairItem("R-1009", "Nissan Altima", "Suspension Service", "In Progress", "John Smith", 
                    LocalDate.of(2023, 7, 5), 290.00),
            new RepairItem("R-1010", "Hyundai Sonata", "Fuel System Cleaning", "Pending", "Mike Johnson", 
                    LocalDate.of(2023, 7, 8), 130.50),
            new RepairItem("R-1011", "Subaru Outback", "Timing Belt Replacement", "Completed", "Robert Chen", 
                    LocalDate.of(2023, 7, 10), 495.75),
            new RepairItem("R-1012", "Mazda CX-5", "Exhaust System Repair", "Pending", "Sarah Williams", 
                    LocalDate.of(2023, 7, 15), 215.00),
            new RepairItem("R-1013", "Volkswagen Golf", "Electrical Diagnostic", "In Progress", "John Smith", 
                    LocalDate.of(2023, 7, 18), 145.25),
            new RepairItem("R-1014", "Kia Sportage", "Coolant Flush", "Completed", "Mike Johnson", 
                    LocalDate.of(2023, 7, 20), 85.50),
            new RepairItem("R-1015", "Jeep Grand Cherokee", "Axle Repair", "In Progress", "Robert Chen", 
                    LocalDate.of(2023, 7, 22), 625.00)
        );
        
        // Clear and initialize filtered list
        filteredList.clear();
    }
    
    // Filter by search text
    public void filterBySearch(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            filteredList.clear();
            return;
        }
        
        String search = searchText.toLowerCase();
        List<RepairItem> filtered = repairsList.stream()
                .filter(repair -> repair.getCarInfo().toLowerCase().contains(search) ||
                                 repair.getRepairType().toLowerCase().contains(search) ||
                                 repair.getRepairId().toLowerCase().contains(search) ||
                                 repair.getTechnician().toLowerCase().contains(search))
                .collect(Collectors.toList());
        
        filteredList.setAll(filtered);
    }
    
    // Filter by status
    public void filterByStatus(String status) {
        if (status == null || status.equals("All Statuses")) {
            filteredList.clear();
            return;
        }
        
        List<RepairItem> filtered = repairsList.stream()
                .filter(repair -> repair.getStatus().equals(status))
                .collect(Collectors.toList());
        
        filteredList.setAll(filtered);
    }
    
    // Filter by date range
    public void filterByDateRange(String dateRange) {
        if (dateRange == null || dateRange.equals("All Time")) {
            filteredList.clear();
            return;
        }
        
        LocalDate today = LocalDate.now();
        LocalDate startDate;
        
        switch (dateRange) {
            case "Last 7 Days":
                startDate = today.minusDays(7);
                break;
            case "Last 30 Days":
                startDate = today.minusDays(30);
                break;
            case "Last 90 Days":
                startDate = today.minusDays(90);
                break;
            case "This Year":
                startDate = LocalDate.of(today.getYear(), 1, 1);
                break;
            default:
                filteredList.clear();
                return;
        }
        
        List<RepairItem> filtered = repairsList.stream()
                .filter(repair -> repair.getStartDate().isAfter(startDate) ||
                                  repair.getStartDate().isEqual(startDate))
                .collect(Collectors.toList());
        
        filteredList.setAll(filtered);
    }
    
    // Reset all filters
    public void resetFilters() {
        filteredList.clear();
    }
    
    // Inner class to represent a repair item in the table view
    public static class RepairItem {
        private final String repairId;
        private final String carInfo;
        private final String repairType;
        private final String status;
        private final String technician;
        private final LocalDate startDate;
        private final Double cost;
        private boolean selected;
        private final HBox actions;
        
        public RepairItem(String repairId, String carInfo, String repairType, String status, 
                        String technician, LocalDate startDate, Double cost) {
            this.repairId = repairId;
            this.carInfo = carInfo;
            this.repairType = repairType;
            this.status = status;
            this.technician = technician;
            this.startDate = startDate;
            this.cost = cost;
            this.selected = false;
            this.actions = new HBox();
        }
        
        // Getters and setters
        public String getRepairId() {
            return repairId;
        }
        
        public String getCarInfo() {
            return carInfo;
        }
        
        public String getRepairType() {
            return repairType;
        }
        
        public String getStatus() {
            return status;
        }
        
        public String getTechnician() {
            return technician;
        }
        
        public LocalDate getStartDate() {
            return startDate;
        }
        
        public Double getCost() {
            return cost;
        }
        
        public boolean isSelected() {
            return selected;
        }
        
        public void setSelected(boolean selected) {
            this.selected = selected;
        }
        
        public HBox getActions() {
            return actions;
        }
    }
}
