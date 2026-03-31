package com.hotel.controllers;

import com.hotel.models.Room;
import com.hotel.services.RoomService;
import com.hotel.utils.AlertBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class RoomController {

    @FXML
    private TableView<Room> roomTable;

    @FXML
    private TableColumn<Room, String> colRoomNumber;

    @FXML
    private TableColumn<Room, String> colRoomType;

    @FXML
    private TableColumn<Room, Double> colPrice;

    @FXML
    private TableColumn<Room, Boolean> colAvailable;

    @FXML
    private TextField txtRoomNumber;

    @FXML
    private ComboBox<String> cbRoomType;

    @FXML
    private TextField txtPrice;

    private RoomService roomService = new RoomService();
    private ObservableList<Room> roomList;

    @FXML
    public void initialize() {
        colRoomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        colRoomType.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("pricePerNight"));
        colAvailable.setCellValueFactory(new PropertyValueFactory<>("available"));

        cbRoomType.getItems().addAll("Standard", "Deluxe", "Suite");
        cbRoomType.getSelectionModel().selectFirst();

        loadRooms();
    }

    private void loadRooms() {
        roomList = FXCollections.observableArrayList(roomService.getAllRooms());
        roomTable.setItems(roomList);
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
