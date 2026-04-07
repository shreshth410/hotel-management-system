package com.hotel.controllers;

import com.hotel.models.Booking;
import com.hotel.models.Bill;
import com.hotel.models.AbstractRoom;
import com.hotel.models.RoomType;
import com.hotel.repository.DataStore;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardController {

    @FXML private Label totalRoomsLbl;
    @FXML private Label availableRoomsLbl;
    @FXML private Label occupiedRoomsLbl;
    @FXML private Label totalCustomersLbl;
    @FXML private Label activeBookingsLbl;
    @FXML private Label totalRevenueLbl;

    @FXML private PieChart roomTypeChart;
    @FXML private BarChart<String, Number> occupancyChart;

    @FXML private TableView<Booking> recentBookingsTable;
    @FXML private TableColumn<Booking, String> colBookingId;
    @FXML private TableColumn<Booking, String> colRoomNum;
    @FXML private TableColumn<Booking, String> colGuest;
    @FXML private TableColumn<Booking, String> colCheckIn;

    @FXML
    public void initialize() {
        colBookingId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBookingId()));
        colRoomNum.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRoomNumber()));
        colGuest.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCustomerId()));
        colCheckIn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCheckInDate().toString()));

        refreshStats();
    }

    private void refreshStats() {
        List<AbstractRoom> rooms = DataStore.getContainer().getRooms();
        long totalRooms = rooms.size();
        long availableRooms = rooms.stream().filter(AbstractRoom::isAvailable).count();
        long occupiedRooms = totalRooms - availableRooms;
        long totalCustomers = DataStore.getContainer().getCustomers().size();
        List<Booking> bookings = DataStore.getContainer().getBookings();
        long activeBookings = bookings.size();

        double totalRevenue = DataStore.getContainer().getBills().stream()
                .mapToDouble(Bill::getTotalAmount)
                .sum();

        totalRoomsLbl.setText(String.valueOf(totalRooms));
        availableRoomsLbl.setText(String.valueOf(availableRooms));
        occupiedRoomsLbl.setText(String.valueOf(occupiedRooms));
        totalCustomersLbl.setText(String.valueOf(totalCustomers));
        activeBookingsLbl.setText(String.valueOf(activeBookings));
        totalRevenueLbl.setText(String.format("₹ %.2f", totalRevenue));

        // Pie Chart
        Map<RoomType, Long> typeCounts = rooms.stream()
                .collect(Collectors.groupingBy(AbstractRoom::getRoomType, Collectors.counting()));

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        typeCounts.forEach((type, count) -> {
            pieData.add(new PieChart.Data(type.name(), count));
        });
        roomTypeChart.setData(pieData);

        // Bar Chart
        occupancyChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Occupied");
        typeCounts.forEach((type, total) -> {
            long available = rooms.stream()
                    .filter(r -> r.getRoomType() == type && r.isAvailable())
                    .count();
            series.getData().add(new XYChart.Data<>(type.name(), total - available));
        });
        occupancyChart.getData().add(series);

        // Recent Bookings
        int size = bookings.size();
        int limit = Math.min(size, 5);
        List<Booking> recent = bookings.subList(size - limit, size);
        recentBookingsTable.setItems(FXCollections.observableArrayList(recent));
    }
}
