package com.bcms.model;

import java.time.LocalDate;

/**
 * Model class representing a repair or service job for a vehicle.
 */
public class RepairService {
    private String repairId;
    private String carId;
    private String carInfo;
    private String repairType;
    private String description;
    private String status;
    private String technician;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double cost;
    private String notes;
    
    // Default constructor
    public RepairService() {
    }
    
    // Parameterized constructor
    public RepairService(String repairId, String carId, String carInfo, String repairType, 
                         String description, String status, String technician, 
                         LocalDate startDate, LocalDate endDate, Double cost, String notes) {
        this.repairId = repairId;
        this.carId = carId;
        this.carInfo = carInfo;
        this.repairType = repairType;
        this.description = description;
        this.status = status;
        this.technician = technician;
        this.startDate = startDate;
        this.endDate = endDate;
        this.cost = cost;
        this.notes = notes;
    }
    
    // Getters and Setters
    public String getRepairId() {
        return repairId;
    }
    
    public void setRepairId(String repairId) {
        this.repairId = repairId;
    }
    
    public String getCarId() {
        return carId;
    }
    
    public void setCarId(String carId) {
        this.carId = carId;
    }
    
    public String getCarInfo() {
        return carInfo;
    }
    
    public void setCarInfo(String carInfo) {
        this.carInfo = carInfo;
    }
    
    public String getRepairType() {
        return repairType;
    }
    
    public void setRepairType(String repairType) {
        this.repairType = repairType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getTechnician() {
        return technician;
    }
    
    public void setTechnician(String technician) {
        this.technician = technician;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public Double getCost() {
        return cost;
    }
    
    public void setCost(Double cost) {
        this.cost = cost;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    @Override
    public String toString() {
        return "RepairService{" +
                "repairId='" + repairId + '\'' +
                ", carId='" + carId + '\'' +
                ", carInfo='" + carInfo + '\'' +
                ", repairType='" + repairType + '\'' +
                ", status='" + status + '\'' +
                ", technician='" + technician + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", cost=" + cost +
                '}';
    }
}
