package com.hotel.controllers;

import com.hotel.models.Customer;
import com.hotel.services.CustomerService;
import com.hotel.utils.AlertBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class CustomerController {

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Customer, String> colCustomerId;

    @FXML
    private TableColumn<Customer, String> colName;

    @FXML
    private TableColumn<Customer, String> colPhone;

    @FXML
    private TextField txtCustomerId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPhone;

    private CustomerService customerService = new CustomerService();
    private ObservableList<Customer> customerList;

    @FXML
    public void initialize() {
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        loadCustomers();
    }

    private void loadCustomers() {
        customerList = FXCollections.observableArrayList(customerService.getAllCustomers());
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
}
