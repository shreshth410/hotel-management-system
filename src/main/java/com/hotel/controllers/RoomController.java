package com.hotel.controllers;

import com.hotel.models.AbstractRoom;
import com.hotel.models.RoomType;
import com.hotel.services.RoomService;
import com.hotel.repository.DataStore;
import com.hotel.utils.AlertBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

public class RoomController {

    @FXML
    private TableView<AbstractRoom> roomTable;

    @FXML
    private TableColumn<AbstractRoom, String> colRoomNumber;

    @FXML
    private TableColumn<AbstractRoom, RoomType> colRoomType;

    @FXML
    private TableColumn<AbstractRoom, Double> colPrice;

    @FXML
    private TableColumn<AbstractRoom, Boolean> colAvailable;

    @FXML
    private TextField txtRoomNumber;

    @FXML
    private ComboBox<String> cbRoomType;

    @FXML
    private TextField txtPrice;

    @FXML
    private CheckBox chkAvailableOnly;

    private RoomService roomService = new RoomService();
    private ObservableList<AbstractRoom> roomList;
    private FilteredList<AbstractRoom> filteredRoomList;

    @FXML
    public void initialize() {
        colRoomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        colRoomType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("pricePerNight"));
        colAvailable.setCellValueFactory(new PropertyValueFactory<>("available"));

        // Format price to display INR
        colPrice.setCellFactory(column -> new TableCell<AbstractRoom, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("₹ " + String.format("%.2f", item));
                }
            }
        });

        // Color-coded availability status
        colAvailable.setCellFactory(column -> new TableCell<AbstractRoom, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (item) {
                        setText("Available");
                        setTextFill(Color.GREEN);
                        setStyle("-fx-font-weight: bold;");
                    } else {
                        setText("Occupied");
                        setTextFill(Color.RED);
                        setStyle("-fx-font-weight: bold;");
                    }
                }
            }
        });

        cbRoomType.getItems().addAll("Standard", "Deluxe", "Suite");
        cbRoomType.getSelectionModel().selectFirst();

        loadRooms();
    }

    private void loadRooms() {
        roomList = FXCollections.observableArrayList(roomService.getAllRooms());
        filteredRoomList = new FilteredList<>(roomList, b -> true);
        roomTable.setItems(filteredRoomList);
        filterRooms(); // apply current filter
    }

    @FXML
    private void filterRooms() {
        if (chkAvailableOnly != null && filteredRoomList != null) {
            filteredRoomList.setPredicate(room -> {
                if (chkAvailableOnly.isSelected()) {
                    return room.isAvailable();
                }
                return true;
            });
        }
    }

    @FXML
    private void loadData() {
        DataStore.loadData();
        loadRooms();
    }

    @FXML
    private void saveData() {
        DataStore.saveData();
        AlertBox.showInfo("Success", "Data saved successfully to hotel_data.dat.");
    }

    @FXML
    private void addRoom() {
        String num = txtRoomNumber.getText();
        String type = cbRoomType.getValue();
        
        if (num.isEmpty() || txtPrice.getText().isEmpty()) {
            AlertBox.showError("Validation Error", "All fields must be filled!");
            return;
        }

        try {
            double price = Double.parseDouble(txtPrice.getText());
            boolean success = roomService.addRoom(num, type, price);
            if (success) {
                AlertBox.showInfo("Success", "Room added successfully!");
                txtRoomNumber.clear();
                txtPrice.clear();
                loadRooms();
            } else {
                AlertBox.showError("Error", "Room number already exists!");
            }
        } catch (NumberFormatException e) {
            AlertBox.showError("Format Error", "Price must be a valid number!");
        }
    }
}
