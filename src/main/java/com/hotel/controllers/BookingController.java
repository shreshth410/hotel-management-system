package com.hotel.controllers;

import com.hotel.models.Booking;
import com.hotel.models.Customer;
import com.hotel.models.AbstractRoom;
import com.hotel.services.BillingService;
import com.hotel.services.BookingService;
import com.hotel.services.CustomerService;
import com.hotel.services.RoomService;
import com.hotel.utils.AlertBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.stream.Collectors;

public class BookingController {

    @FXML
    private TableView<Booking> bookingTable;

    @FXML
    private TableColumn<Booking, String> colBookingId;

    @FXML
    private TableColumn<Booking, String> colRoomNumber;

    @FXML
    private TableColumn<Booking, String> colCustomerId;

    @FXML
    private TableColumn<Booking, LocalDate> colCheckIn;

    @FXML
    private TableColumn<Booking, LocalDate> colCheckOut;

    @FXML
    private ComboBox<AbstractRoom> cbRooms;

    @FXML
    private ComboBox<Customer> cbCustomers;

    @FXML
    private DatePicker dpCheckIn;

    @FXML
    private DatePicker dpCheckOut;

    @FXML
    private HBox amenitiesBox;

    @FXML
    private CheckBox chkWifi;

    @FXML
    private CheckBox chkCleaning;

    @FXML
    private CheckBox chkBreakfast;

    private BookingService bookingService = new BookingService();
    private RoomService roomService = new RoomService();
    private CustomerService customerService = new CustomerService();
    private BillingService billingService = new BillingService();
    private ObservableList<Booking> bookingList;

    @FXML
    public void initialize() {
        colBookingId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        colRoomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colCheckIn.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
        colCheckOut.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));

        amenitiesBox.managedProperty().bind(amenitiesBox.visibleProperty());
        chkWifi.managedProperty().bind(chkWifi.visibleProperty());
        chkCleaning.managedProperty().bind(chkCleaning.visibleProperty());
        chkBreakfast.managedProperty().bind(chkBreakfast.visibleProperty());

        cbRooms.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                com.hotel.models.RoomType type = newVal.getRoomType();
                if (type == com.hotel.models.RoomType.DELUXE) {
                    amenitiesBox.setVisible(true);
                    chkWifi.setVisible(true);
                    chkCleaning.setVisible(true);
                    chkBreakfast.setVisible(false);
                    chkBreakfast.setSelected(false);
                } else if (type == com.hotel.models.RoomType.SUITE) {
                    amenitiesBox.setVisible(true);
                    chkWifi.setVisible(true);
                    chkCleaning.setVisible(true);
                    chkBreakfast.setVisible(true);
                } else {
                    amenitiesBox.setVisible(false);
                    chkWifi.setSelected(false);
                    chkCleaning.setSelected(false);
                    chkBreakfast.setSelected(false);
                }
            }
        });

        loadBookings();
        loadDropdowns();
    }

    private void loadBookings() {
        bookingList = FXCollections.observableArrayList(bookingService.getAllBookings());
        bookingTable.setItems(bookingList);
    }

    private void loadDropdowns() {
        ObservableList<AbstractRoom> availableRooms = FXCollections.observableArrayList(
                roomService.getAllRooms().stream().filter(AbstractRoom::isAvailable).collect(Collectors.toList())
        );
        cbRooms.setItems(availableRooms);

        ObservableList<Customer> customers = FXCollections.observableArrayList(customerService.getAllCustomers());
        cbCustomers.setItems(customers);
    }

    @FXML
    private void addBooking() {
        AbstractRoom r = cbRooms.getValue();
        Customer c = cbCustomers.getValue();
        LocalDate in = dpCheckIn.getValue();
        LocalDate out = dpCheckOut.getValue();

        if (r == null || c == null || in == null || out == null) {
            AlertBox.showError("Validation Error", "All fields must be selected!");
            return;
        }

        double extraCost = 0.0;
        String amenities = "";
        if (amenitiesBox.isVisible()) {
            if (chkWifi.isVisible() && chkWifi.isSelected()) {
                extraCost += 200.0;
                amenities += "Wifi, ";
            }
            if (chkCleaning.isVisible() && chkCleaning.isSelected()) {
                extraCost += 300.0;
                amenities += "Cleaning, ";
            }
            if (chkBreakfast.isVisible() && chkBreakfast.isSelected()) {
                extraCost += 500.0;
                amenities += "Breakfast, ";
            }
        }
        if (amenities.endsWith(", ")) {
            amenities = amenities.substring(0, amenities.length() - 2);
        }
        
        // Final variables for lambda
        final String amenitiesFinal = amenities;
        final double extraCostFinal = extraCost;

        new Thread(() -> {
            try {
                Thread.sleep(1500); // 1.5s simulation
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            boolean success = bookingService.bookRoom(r.getRoomNumber(), c.getCustomerId(), in, out, amenitiesFinal, extraCostFinal);

            javafx.application.Platform.runLater(() -> {
                if (success) {
                    AlertBox.showInfo("Success", "Room successfully booked!");
                    dpCheckIn.setValue(null);
                    dpCheckOut.setValue(null);
                    cbRooms.getSelectionModel().clearSelection();
                    cbCustomers.getSelectionModel().clearSelection();
                    
                    // Refund the UI lists
                    loadBookings();
                    loadDropdowns();
                } else {
                    AlertBox.showError("Error", "Invalid dates or room not available.");
                }
            });
        }).start();
    }

    @FXML
    private void checkout() {
        Booking selected = bookingTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertBox.showError("Selection Error", "Please select a booking from the table to checkout.");
            return;
        }

        billingService.generateBill(selected);
        AlertBox.showInfo("Success", "Checkout complete! Bill generated.");
        
        loadBookings();
        loadDropdowns();
    }
}
