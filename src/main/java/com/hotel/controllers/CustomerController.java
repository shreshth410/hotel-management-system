package com.hotel.controllers;

import com.hotel.models.Customer;
import com.hotel.models.Pair;
import com.hotel.models.Booking;
import com.hotel.services.CustomerService;
import com.hotel.services.BookingService;
import com.hotel.utils.AlertBox;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class CustomerController {

    @FXML
    private TableView<Pair<Customer, String>> customerTable;

    @FXML
    private TableColumn<Pair<Customer, String>, String> colCustomerId;

    @FXML
    private TableColumn<Pair<Customer, String>, String> colName;

    @FXML
    private TableColumn<Pair<Customer, String>, String> colPhone;

    @FXML
    private TableColumn<Pair<Customer, String>, String> colAllocatedRoom;

    @FXML
    private TextField txtCustomerId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPhone;

    private CustomerService customerService = new CustomerService();
    private BookingService bookingService = new BookingService();
    private ObservableList<Pair<Customer, String>> customerList;

    @FXML
    public void initialize() {
        colCustomerId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKey().getCustomerId()));
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKey().getName()));
        colPhone.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKey().getPhone()));
        colAllocatedRoom.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getValue()));

        // Restrict phone field to 10 digits
        txtPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtPhone.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (txtPhone.getText().length() > 10) {
                String s = txtPhone.getText().substring(0, 10);
                txtPhone.setText(s);
            }
        });

        loadCustomers();
    }

    private void loadCustomers() {
        List<Pair<Customer, String>> pairs = new ArrayList<>();
        for (Customer c : customerService.getAllCustomers()) {
            String room = "None";
            for (Booking b : bookingService.getAllBookings()) {
                if (b.getCustomerId().equals(c.getCustomerId())) {
                    room = b.getRoomNumber();
                    break;
                }
            }
            pairs.add(new Pair<>(c, room));
        }
        customerList = FXCollections.observableArrayList(pairs);
        customerTable.setItems(customerList);
    }

    @FXML
    private void addCustomer() {
        String id = txtCustomerId.getText();
        String name = txtName.getText();
        String phone = txtPhone.getText();
        
        if (id.isEmpty() || name.isEmpty() || phone.isEmpty()) {
            AlertBox.showError("Validation Error", "All fields must be filled!");
            return;
        }

        // Validate phone number: exactly 10 digits
        if (!phone.matches("\\d{10}")) {
            AlertBox.showError("Validation Error", "Invalid Phone Number! Must be exactly 10 digits.");
            return;
        }

        boolean success = customerService.addCustomer(id, name, phone);
        if (success) {
            AlertBox.showInfo("Success", "Customer added successfully!");
            txtCustomerId.clear();
            txtName.clear();
            txtPhone.clear();
            loadCustomers();
        } else {
            AlertBox.showError("Error", "Customer ID already exists!");
        }
    }

    @FXML
    private void removeCustomer() {
        Pair<Customer, String> selected = customerTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertBox.showError("Selection Error", "Please select a customer to remove.");
            return;
        }

        if (!selected.getValue().equals("None")) {
            AlertBox.showError("Restriction Error", "Cannot remove a customer who is currently checked into room " + selected.getValue() + "!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove " + selected.getKey().getName() + "?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait();

        if (confirm.getResult() == ButtonType.YES) {
            boolean success = customerService.removeCustomer(selected.getKey().getCustomerId());
            if (success) {
                AlertBox.showInfo("Success", "Customer removed.");
                loadCustomers();
            } else {
                AlertBox.showError("Error", "Could not remove customer.");
            }
        }
    }
}
