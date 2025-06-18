package com.bcms.viewmodel;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * ViewModel for the Analytics screen that handles the business logic
 * and data for the Analytics UI.
 */
public class AnalyticsViewModel {
    
    // Sales metrics properties
    private final DoubleProperty totalSales = new SimpleDoubleProperty(50000.0);
    private final IntegerProperty carsSold = new SimpleIntegerProperty(6);
    private final DoubleProperty netProfit = new SimpleDoubleProperty(7000.0);
    private final DoubleProperty profitMargin = new SimpleDoubleProperty(14.0);
    private final DoubleProperty avgSalePrice = new SimpleDoubleProperty(8333.33);
    private final DoubleProperty repairCosts = new SimpleDoubleProperty(2400.0);
    private final IntegerProperty repairsCompleted = new SimpleIntegerProperty(18);
    
    // Monthly sales data
    private final ObservableList<MonthlySalesData> monthlySalesData = FXCollections.observableArrayList();
    
    /**
     * Initialize the ViewModel with sample data.
     * In a real application, this would fetch data from a repository or service.
     */
    public AnalyticsViewModel() {
        loadSampleData();
    }
    
    /**
     * Load sample data for the analytics.
     * In a real application, this would call a repository or service.
     */
    private void loadSampleData() {
        // Initialize with some sample monthly sales data
        int currentYear = LocalDate.now().getYear();
        
        // Add data for past months (Jan-Jun)
        monthlySalesData.add(new MonthlySalesData(Month.JANUARY, currentYear, 5000.0, 0.0));
        monthlySalesData.add(new MonthlySalesData(Month.FEBRUARY, currentYear, 10000.0, 0.0));
        monthlySalesData.add(new MonthlySalesData(Month.MARCH, currentYear, 15000.0, 0.0));
        monthlySalesData.add(new MonthlySalesData(Month.APRIL, currentYear, 20000.0, 0.0));
        monthlySalesData.add(new MonthlySalesData(Month.MAY, currentYear, 25000.0, 0.0));
        monthlySalesData.add(new MonthlySalesData(Month.JUNE, currentYear, 30000.0, 0.0));
        
        // Add projections for future months (Jul-Aug)
        monthlySalesData.add(new MonthlySalesData(Month.JULY, currentYear, 0.0, 15000.0));
        monthlySalesData.add(new MonthlySalesData(Month.AUGUST, currentYear, 0.0, 10000.0));
    }
    
    /**
     * Update data based on selected time range.
     * In a real application, this would fetch new data from a repository or service.
     */
    public void updateDataForTimeRange(String timeRange) {
        switch (timeRange) {
            case "This Month":
                totalSales.set(50000.0);
                carsSold.set(6);
                netProfit.set(7000.0);
                profitMargin.set(14.0);
                break;
            case "Last Month":
                totalSales.set(42500.0);
                carsSold.set(5);
                netProfit.set(5800.0);
                profitMargin.set(13.6);
                break;
            case "Last 3 Months":
                totalSales.set(135000.0);
                carsSold.set(16);
                netProfit.set(18900.0);
                profitMargin.set(14.0);
                break;
            case "Last 6 Months":
                totalSales.set(205000.0);
                carsSold.set(24);
                netProfit.set(28700.0);
                profitMargin.set(14.0);
                break;
            default:
                // Default case, use initial values
                break;
        }
        
        // Recalculate average sale price
        if (carsSold.get() > 0) {
            avgSalePrice.set(totalSales.get() / carsSold.get());
        }
    }
    
    // Getters for the properties
    public double getTotalSales() { return totalSales.get(); }
    public DoubleProperty totalSalesProperty() { return totalSales; }
    
    public int getCarsSold() { return carsSold.get(); }
    public IntegerProperty carsSoldProperty() { return carsSold; }
    
    public double getNetProfit() { return netProfit.get(); }
    public DoubleProperty netProfitProperty() { return netProfit; }
    
    public double getProfitMargin() { return profitMargin.get(); }
    public DoubleProperty profitMarginProperty() { return profitMargin; }
    
    public double getAvgSalePrice() { return avgSalePrice.get(); }
    public DoubleProperty avgSalePriceProperty() { return avgSalePrice; }
    
    public double getRepairCosts() { return repairCosts.get(); }
    public DoubleProperty repairCostsProperty() { return repairCosts; }
    
    public int getRepairsCompleted() { return repairsCompleted.get(); }
    public IntegerProperty repairsCompletedProperty() { return repairsCompleted; }
    
    public ObservableList<MonthlySalesData> getMonthlySalesData() { return monthlySalesData; }
    
    /**
     * Format total sales as a currency string.
     */
    public String getFormattedTotalSales() {
        return String.format("$%,.0f", totalSales.get());
    }
    
    /**
     * Format net profit as a currency string with sign.
     */
    public String getFormattedNetProfit() {
        return String.format("%s$%,.0f", netProfit.get() >= 0 ? "+" : "", netProfit.get());
    }
    
    /**
     * Format profit margin as a percentage string.
     */
    public String getFormattedProfitMargin() {
        return String.format("%.1f%% profit margin", profitMargin.get());
    }
    
    /**
     * Format average sale price as a currency string.
     */
    public String getFormattedAvgSalePrice() {
        return String.format("$%,.0f", avgSalePrice.get());
    }
    
    /**
     * Format repair costs as a currency string with sign.
     */
    public String getFormattedRepairCosts() {
        return String.format("%s$%,.0f", repairCosts.get() <= 0 ? "-" : "+", Math.abs(repairCosts.get()));
    }
    
    /**
     * Class to represent monthly sales data for charts.
     */
    public static class MonthlySalesData {
        private final Month month;
        private final int year;
        private final double actualSales;
        private final double projectedSales;
        
        public MonthlySalesData(Month month, int year, double actualSales, double projectedSales) {
            this.month = month;
            this.year = year;
            this.actualSales = actualSales;
            this.projectedSales = projectedSales;
        }
        
        public Month getMonth() { return month; }
        public int getYear() { return year; }
        public double getActualSales() { return actualSales; }
        public double getProjectedSales() { return projectedSales; }
        
        /**
         * Get month name as a short string (e.g., "Jan").
         */
        public String getMonthShortName() {
            return month.getDisplayName(TextStyle.SHORT, Locale.getDefault());
        }
        
        /**
         * Get month name as a full string (e.g., "January").
         */
        public String getMonthFullName() {
            return month.getDisplayName(TextStyle.FULL, Locale.getDefault());
        }
        
        /**
         * Get formatted month and year (e.g., "Jan 2023").
         */
        public String getMonthYearFormatted() {
            return month.getDisplayName(TextStyle.SHORT, Locale.getDefault()) + " " + year;
        }
        
        /**
         * Determine if this month has actual data or is a projection.
         */
        public boolean isProjected() {
            return actualSales == 0 && projectedSales > 0;
        }
    }
}