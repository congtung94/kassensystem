package com.demo.kassensystem.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class OrderedItem {
    private SimpleIntegerProperty count;
    private SimpleStringProperty itemNr;
    private SimpleStringProperty itemName;
    private SimpleDoubleProperty unitPrice;

    public OrderedItem(int count, String itemNr, String itemName, double unitPrice) {
        this.count = new SimpleIntegerProperty(count);
        this.itemNr = new SimpleStringProperty(itemNr);
        this.itemName = new SimpleStringProperty(itemName);
        this.unitPrice = new SimpleDoubleProperty(unitPrice);
    }

    public int getCount() {
        return count.get();
    }

    public void setCount(int count) {
        this.count.set(count);
    }

    public String getItemNr() {
        return itemNr.get();
    }

    public void setItemNr(String itemNr) {
        this.itemNr.set(itemNr);
    }

    public String getItemName() {
        return itemName.get();
    }

    public void setItemName(String itemName) {
        this.itemName.set(itemName);
    }

    public double getUnitPrice() {
        return unitPrice.get();
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice.set(unitPrice);
    }
}
