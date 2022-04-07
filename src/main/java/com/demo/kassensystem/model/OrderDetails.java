package com.demo.kassensystem.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class OrderDetails {

    private SimpleIntegerProperty id;
    private SimpleIntegerProperty orderId;
    private SimpleStringProperty itemNr;
    private SimpleIntegerProperty quantity;

    public OrderDetails(int id, int orderId,
                        String itemNr, int quantity) {
        this.id = new SimpleIntegerProperty(id);
        this.orderId = new SimpleIntegerProperty(orderId);
        this.itemNr = new SimpleStringProperty(itemNr);
        this.quantity = new SimpleIntegerProperty(quantity);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getOrderId() {
        return orderId.get();
    }


    public void setOrderId(int orderId) {
        this.orderId.set(orderId);
    }

    public String getItemNr() {
        return itemNr.get();
    }

    public void setItemNr(String itemNr) {
        this.itemNr.set(itemNr);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }
}
