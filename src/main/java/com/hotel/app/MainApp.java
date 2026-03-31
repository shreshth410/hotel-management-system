package com.hotel.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        primaryStage.setTitle("Hotel Management System");
        
        // Creative Hotel Theme: Setting scene slightly larger for better presentation
        Scene scene = new Scene(root, 1080, 720);
        
        // Attach external CSS just in case root doesn't have it (though it is attached in fxml)
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        
        primaryStage.setScene(scene);
        primaryStage.setResizable(false); // Locking size for static desktop ui
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
