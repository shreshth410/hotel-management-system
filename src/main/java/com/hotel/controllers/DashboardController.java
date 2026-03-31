package com.hotel.controllers;

import com.hotel.repository.DataStore;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML
    private Label totalRoomsLbl;

    @FXML
    private Label availableRoomsLbl;

    @FXML
    private Label totalCustomersLbl;

    @FXML
    private Label activeBookingsLbl;

    @FXML
    public void initialize() {
        refreshStats();
    }

    private void refreshStats() {
        long totalRooms = DataStore.getContainer().getRooms().size();
        long availableRooms = DataStore.getContainer().getRooms().stream().filter(r -> r.isAvailable()).count();
        long totalCustomers = DataStore.getContainer().getCustomers().size();
        long activeBookings = DataStore.getContainer().getBookings().size();

        totalRoomsLbl.setText(String.valueOf(totalRooms));
        availableRoomsLbl.setText(String.valueOf(availableRooms));
        totalCustomersLbl.setText(String.valueOf(totalCustomers));
        activeBookingsLbl.setText(String.valueOf(activeBookings));
    }
}
