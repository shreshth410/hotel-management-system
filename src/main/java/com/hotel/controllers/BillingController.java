package com.hotel.controllers;

import com.hotel.models.Bill;
import com.hotel.services.BillingService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class BillingController {

    @FXML
    private TableView<Bill> billTable;

    @FXML
    private TableColumn<Bill, String> colBillId;

    @FXML
    private TableColumn<Bill, String> colBookingId;

    @FXML
    private TableColumn<Bill, String> colCustomerName;

    @FXML
    private TableColumn<Bill, String> colRoomNumber;

    @FXML
    private TableColumn<Bill, Double> colAmount;

    @FXML
    private TableColumn<Bill, LocalDate> colDate;

    private BillingService billingService = new BillingService();
    private ObservableList<Bill> billList;

    @FXML
    public void initialize() {
        colBillId.setCellValueFactory(new PropertyValueFactory<>("billId"));
        colBookingId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colRoomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("billDate"));

        loadBills();
    }

    private void loadBills() {
        billList = FXCollections.observableArrayList(billingService.getAllBills());
        billTable.setItems(billList);
    }
}
