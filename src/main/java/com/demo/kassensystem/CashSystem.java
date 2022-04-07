package com.demo.kassensystem;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CashSystem extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CashSystem.class.getResource("/home.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Kassensystem");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();

        if (!Database.getInstance().open()){
            System.out.println("verbindung zur datenbank gescheitert");
            Platform.exit();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Database.getInstance().close();
    }

    public static void main(String[] args) {
        launch();
    }
}