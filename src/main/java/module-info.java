module com.hotel.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens com.hotel.app to javafx.fxml;
    opens com.hotel.controllers to javafx.fxml;
    opens com.hotel.models to javafx.base; // for TableView reflections

    exports com.hotel.app;
    exports com.hotel.controllers;
    exports com.hotel.models;
}
