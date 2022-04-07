package com.demo.kassensystem.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Item {

    private SimpleStringProperty nr;
    private SimpleStringProperty name;
    private SimpleDoubleProperty price;
    private SimpleStringProperty category;
    private SimpleStringProperty description;
    private SimpleDoubleProperty discount;

    public Item(String nr, String name, double price, String category, String description, double discount) {
        this.nr = new SimpleStringProperty(nr);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.category = new SimpleStringProperty(category);
        this.description = new SimpleStringProperty(description);
        this.discount = new SimpleDoubleProperty(discount);
    }

    public String getNr() {
        return nr.get();
    }

    public void setNr(String nr) {
        this.nr.set(nr);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public double getPrice() {
        return price.get();
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getCategory() {
        return category.get();
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public double getDiscount() {
        return discount.get();
    }

    public void setDiscount(double discount) {
        this.discount.set(discount);
    }
}
