package com.demo.kassensystem.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Order {
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty cusId;
    private SimpleStringProperty order_time;

    public Order(int id, int cusId, String order_time) {
        this.id = new SimpleIntegerProperty(id);
        this.cusId = new SimpleIntegerProperty(cusId);
        this.order_time = new SimpleStringProperty(order_time);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getCusId() {
        return cusId.get();
    }

    public void setCusId(int cusId) {
        this.cusId.set(cusId);
    }

    public String getOrder_time() {
        return order_time.get();
    }

    public void setOrder_time(String order_time) {
        this.order_time.set(order_time);
    }
}
