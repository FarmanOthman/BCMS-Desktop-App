package com.bcms.controller;

import com.bcms.model.Car;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.scene.paint.Color;

import java.io.File;
import java.net.URL;
import java.text.NumberFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Controller for the Edit Car Dialog.
 */
public class EditCarDialogController implements Initializable {

    @FXML private TextField makeField;
    @FXML private TextField modelField;
    @FXML private ComboBox<Integer> yearComboBox;
    @FXML private TextField priceField;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private TextField vinField;
    @FXML private TextArea notesTextArea;
    @FXML private VBox repairPartsContainer;
    @FXML private TextField repairNameField;
    @FXML private TextField repairCostField;
    @FXML private Button addRepairButton;
    @FXML private Button cancelButton;
    @FXML private Button updateButton;
    @FXML private Button closeButton;
    @FXML private StackPane addImageBox;
    @FXML private Button uploadMultipleButton;
    
    private Car car; // The car being edited
    private boolean commitChanges = false;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupYearComboBox();
        setupStatusComboBox();
        setupTextFormatters();
    }
    
    /**
     * Set the car to be edited.
     * 
     * @param car The car to edit
     */
    public void setCar(Car car) {
        this.car = car;
        populateFields();
    }
    
    /**
     * Returns whether changes should be committed.
     * 
     * @return true if changes should be committed, false otherwise
     */
    public boolean isCommitChanges() {
        return commitChanges;
    }
    
    /**
     * Get the updated car with changes.
     * 
     * @return The updated car
     */
    public Car getUpdatedCar() {
        // In a real implementation, you would update the car object with
        // values from the form fields and return it
        return car;
    }
    
    /**
     * Setup the year combo box with a range of years.
     */
    private void setupYearComboBox() {
        // Create a list of years from 1990 to current year
        int currentYear = Year.now().getValue();
        List<Integer> years = IntStream.rangeClosed(1990, currentYear)
                .boxed()
                .collect(Collectors.toList());
        Collections.reverse(years); // Most recent years first
        
        yearComboBox.setItems(FXCollections.observableArrayList(years));
        yearComboBox.setValue(2023); // Default to the value in the mockup
    }
    
    /**
     * Setup the status combo box with available statuses.
     */
    private void setupStatusComboBox() {
        ObservableList<String> statuses = FXCollections.observableArrayList(
            "Available", "Sold", "Under Repair"
        );
        
        statusComboBox.setItems(statuses);
        statusComboBox.setValue("Under Repair"); // Default to the value in the mockup
        
        // Set cell factory to style different statuses
        statusComboBox.setCellFactory(listView -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    
                    // Apply appropriate style class based on status
                    getStyleClass().removeAll("status-available", "status-sold", "status-repair");
                    
                    if ("Available".equals(item)) {
                        getStyleClass().add("status-available");
                    } else if ("Sold".equals(item)) {
                        getStyleClass().add("status-sold");
                    } else if ("Under Repair".equals(item)) {
                        getStyleClass().add("status-repair");
                    }
                }
            }
        });
        
        // Also style the button cell (selected value)
        statusComboBox.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    
                    // Apply appropriate style class based on status
                    getStyleClass().removeAll("status-available", "status-sold", "status-repair");
                    
                    if ("Available".equals(item)) {
                        getStyleClass().add("status-available");
                    } else if ("Sold".equals(item)) {
                        getStyleClass().add("status-sold");
                    } else if ("Under Repair".equals(item)) {
                        getStyleClass().add("status-repair");
                    }
                }
            }
        });
    }
    
    /**
     * Setup text formatters for currency and other fields.
     */
    private void setupTextFormatters() {
        // Format price as currency
        priceField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) { // Focus lost
                try {
                    // Parse the current value
                    String text = priceField.getText().replaceAll("[^\\d.]", "");
                    double amount = Double.parseDouble(text);
                    
                    // Format as currency
                    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
                    priceField.setText(currencyFormatter.format(amount));
                } catch (NumberFormatException e) {
                    // If parsing fails, do nothing or show an error
                }
            } else { // Focus gained
                // Optionally remove currency formatting for easier editing
                String text = priceField.getText().replaceAll("[^\\d.]", "");
                priceField.setText(text);
            }
        });
        
        // Format repair cost as currency
        repairCostField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) { // Focus lost
                try {
                    // Parse the current value
                    String text = repairCostField.getText().replaceAll("[^\\d.]", "");
                    if (!text.isEmpty()) {
                        double amount = Double.parseDouble(text);
                        
                        // Format as currency
                        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
                        repairCostField.setText(currencyFormatter.format(amount));
                    }
                } catch (NumberFormatException e) {
                    // If parsing fails, do nothing or show an error
                }
            } else { // Focus gained
                // Optionally remove currency formatting for easier editing
                String text = repairCostField.getText().replaceAll("[^\\d.]", "");
                repairCostField.setText(text);
            }
        });
    }
    
    /**
     * Populate fields with car data.
     */
    private void populateFields() {
        // This would be implemented to populate form fields with data from the car object
        // For now, we'll use the sample data from the mockup
        makeField.setText("Toyota");
        modelField.setText("Corolla");
        yearComboBox.setValue(2023);
        priceField.setText("$24,999.99");
        statusComboBox.setValue("Under Repair");
        vinField.setText("VIN1234567890ABCDE");
        notesTextArea.setText("Engine replaced in 2022, new tires installed");
        
        // Sample repair parts would be loaded from the car object
        // For now, we're using the static ones in the FXML
    }
    
    /**
     * Handle adding a new repair part.
     */
    @FXML
    private void handleAddRepair() {
        String repairName = repairNameField.getText().trim();
        String repairCost = repairCostField.getText().trim();
        
        if (repairName.isEmpty() || repairCost.isEmpty()) {
            // Show error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Missing Information");
            alert.setContentText("Please enter both repair name and cost.");
            alert.showAndWait();
            return;
        }
        
        // Create a new repair part item
        HBox repairItem = new HBox();
        repairItem.getStyleClass().add("repair-part-item");
        
        Label nameLabel = new Label(repairName + " - ");
        Label costLabel = new Label(repairCost);
        costLabel.getStyleClass().add("repair-cost");
        
        repairItem.getChildren().addAll(nameLabel, costLabel);
        
        // Add to the repair parts container
        repairPartsContainer.getChildren().add(repairItem);
        
        // Clear the input fields
        repairNameField.clear();
        repairCostField.clear();
    }
    
    /**
     * Handle the close button action.
     */
    @FXML
    private void handleClose() {
        closeDialog(false);
    }
    
    /**
     * Handle the cancel button action.
     */
    @FXML
    private void handleCancel() {
        closeDialog(false);
    }
    
    /**
     * Handle the update button action.
     */
    @FXML
    private void handleUpdate() {
        // Validate fields
        if (!validateFields()) {
            return;
        }
        
        // Set flag to commit changes and close
        closeDialog(true);
    }
    
    /**
     * Handle adding a new image.
     */
    @FXML
    private void handleAddImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Car Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        
        File selectedFile = fileChooser.showOpenDialog(addImageBox.getScene().getWindow());
        if (selectedFile != null) {
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            // In a real implementation, you would add the image to the car
        }
    }
    
    /**
     * Handle uploading multiple images.
     */
    @FXML
    private void handleUploadMultiple() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Car Images");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(uploadMultipleButton.getScene().getWindow());
        if (selectedFiles != null) {
            System.out.println("Selected " + selectedFiles.size() + " files");
            // In a real implementation, you would add the images to the car
        }
    }
    
    /**
     * Validate form fields.
     * 
     * @return true if fields are valid, false otherwise
     */
    private boolean validateFields() {
        List<String> errors = new ArrayList<>();
        
        if (makeField.getText().trim().isEmpty()) {
            errors.add("Make is required");
        }
        
        if (modelField.getText().trim().isEmpty()) {
            errors.add("Model is required");
        }
        
        if (yearComboBox.getValue() == null) {
            errors.add("Year is required");
        }
        
        if (priceField.getText().trim().isEmpty()) {
            errors.add("Price is required");
        }
        
        if (statusComboBox.getValue() == null) {
            errors.add("Status is required");
        }
        
        if (vinField.getText().trim().isEmpty()) {
            errors.add("VIN is required");
        }
        
        // If there are errors, show them
        if (!errors.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Please correct the following errors");
            alert.setContentText(String.join("\n", errors));
            alert.showAndWait();
            return false;
        }
        
        return true;
    }
    
    /**
     * Close the dialog.
     * 
     * @param commit Whether to commit changes
     */
    private void closeDialog(boolean commit) {
        this.commitChanges = commit;
        
        // Get the stage from any control
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}