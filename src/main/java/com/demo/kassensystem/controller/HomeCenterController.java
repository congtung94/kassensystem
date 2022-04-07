package com.demo.kassensystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;


public class HomeCenterController {

    @FXML
    private Pane centerPane;

    @FXML
    public void deliveryBtnClicked (){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fragments/customers.fxml"));
        Scene scene = centerPane.getScene();
        BorderPane homePane = (BorderPane) scene.getRoot();
        homePane.getChildren().remove(homePane.getCenter());
        try {
            Pane pane = loader.load();
            pane.prefWidthProperty().bind(homePane.widthProperty().multiply(0.932));
            pane.prefHeightProperty().bind(homePane.heightProperty().multiply(0.95));
            homePane.setCenter(pane);

        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void collectionBtnClicked(ActionEvent actionEvent) {
    }

    public void restaurantClicked(ActionEvent actionEvent) {
    }
}
