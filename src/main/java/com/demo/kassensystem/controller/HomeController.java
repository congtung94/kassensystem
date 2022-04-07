package com.demo.kassensystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private BorderPane homePane;


    @FXML
    private Button homeBtn;

    @FXML
    private MenuBar homeMenuBar;

    @FXML
    private VBox leftVBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        leftVBox.prefWidthProperty().bind(homePane.widthProperty().multiply(0.068));
        homeMenuBar.prefWidthProperty().bind(homePane.widthProperty().multiply(0.1));
        loadHomeCenter();
    }


    public void loadHomeCenter() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fragments/homeCenter.fxml"));
        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (homePane.getCenter() != null){
            homePane.getChildren().remove(homePane.getCenter());
        }
        homePane.setCenter(pane);
    }


    public void collectionBtnClicked(ActionEvent actionEvent) {
    }

    public void restaurantClicked(ActionEvent actionEvent) {
    }
}