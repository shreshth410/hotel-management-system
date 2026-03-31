package com.hotel.controllers;

import com.hotel.models.Booking;
import com.hotel.models.Customer;
import com.hotel.models.Room;
import com.hotel.services.BillingService;
import com.hotel.services.BookingService;
import com.hotel.services.CustomerService;
import com.hotel.services.RoomService;
import com.hotel.utils.AlertBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    private ComboBox<Room> cbRooms;

    @FXML
    private ComboBox<Customer> cbCustomers;

    @FXML
    private DatePicker dpCheckIn;

    @FXML
    private DatePicker dpCheckOut;

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

        loadBookings();
        loadDropdowns();
    }

    private void loadBookings() {
        bookingList = FXCollections.observableArrayList(bookingService.getAllBookings());
        bookingTable.setItems(bookingList);
    }

    private void loadDropdowns() {
        ObservableList<Room> availableRooms = FXCollections.observableArrayList(
                roomService.getAllRooms().stream().filter(Room::isAvailable).collect(Collectors.toList())
        );
        cbRooms.setItems(availableRooms);

        ObservableList<Customer> customers = FXCollections.observableArrayList(customerService.getAllCustomers());
        cbCustomers.setItems(customers);
    }

    @FXML
    private void addBooking() {
        Room r = cbRooms.getValue();
        Customer c = cbCustomers.getValue();
        LocalDate in = dpCheckIn.getValue();
        LocalDate out = dpCheckOut.getValue();

        if (r == null || c == null || in == null || out == null) {
            AlertBox.showError("Validation Error", "All fields must be selected!");
            return;
        }

        boolean success = bookingService.bookRoom(r.getRoomNumber(), c.getCustomerId(), in, out);
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
