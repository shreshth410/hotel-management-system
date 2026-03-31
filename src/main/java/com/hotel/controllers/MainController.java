package com.hotel.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainController {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private StackPane contentArea;

    @FXML
    public void initialize() {
        loadView("/fxml/dashboard.fxml");
    }

    @FXML
    private void openDashboard() {
        loadView("/fxml/dashboard.fxml");
    }

    @FXML
    private void openRooms() {
        loadView("/fxml/rooms.fxml");
    }

    @FXML
    private void openCustomers() {
        loadView("/fxml/customers.fxml");
    }

    @FXML
    private void openBookings() {
        loadView("/fxml/bookings.fxml");
    }

    @FXML
    private void openBilling() {
        loadView("/fxml/billing.fxml");
    }

    private void loadView(String fxmlFile) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlFile));
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
