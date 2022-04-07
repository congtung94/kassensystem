package com.demo.kassensystem.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Customer {

    private SimpleIntegerProperty cusNr;
    private SimpleStringProperty firstName;
    private SimpleStringProperty lastName;
    private SimpleStringProperty strasse;
    private SimpleStringProperty hausNr;
    private SimpleStringProperty plz;
    private SimpleStringProperty ort;
    private SimpleStringProperty tel1;
    private SimpleStringProperty tel2;
    private SimpleStringProperty tel3;
    private SimpleStringProperty lastOrder;
    private SimpleStringProperty notiz;

    public Customer(int cusNr, String firstName, String lastName, String strasse, String hausNr,
                    String plz, String ort, String tel1, String tel2, String tel3, String lastOrder, String notiz) {
        this.cusNr = new SimpleIntegerProperty(cusNr);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.strasse = new SimpleStringProperty(strasse);
        this.hausNr = new SimpleStringProperty(hausNr);
        this.plz = new SimpleStringProperty(plz);
        this.ort = new SimpleStringProperty(ort);
        this.tel1 = new SimpleStringProperty(tel1);
        this.tel2 = new SimpleStringProperty(tel2);
        this.tel3 = new SimpleStringProperty(tel3);
        this.lastOrder = new SimpleStringProperty(lastOrder);
        this.notiz = new SimpleStringProperty(notiz);
    }

    public int getCusNr() {
        return cusNr.get();
    }


    public void setCusNr(int cusNr) {
        this.cusNr.set(cusNr);
    }

    public String getFirstName() {
        return firstName.get();
    }


    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }


    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getStrasse() {
        return strasse.get();
    }


    public void setStrasse(String strasse) {
        this.strasse.set(strasse);
    }

    public String getHausNr() {
        return hausNr.get();
    }


    public void setHausNr(String hausNr) {
        this.hausNr.set(hausNr);
    }

    public String getPlz() {
        return plz.get();
    }

    public void setPlz(String plz) {
        this.plz.set(plz);
    }

    public String getOrt() {
        return ort.get();
    }

    public void setOrt(String ort) {
        this.ort.set(ort);
    }

    public String getTel1() {
        return tel1.get();
    }

    public String getLastOrder() {
        return lastOrder.get();
    }

    public void setLastOrder(String lastOrder) {
        this.lastOrder.set(lastOrder);
    }

    public void setTel1(String tel1) {
        this.tel1.set(tel1);
    }

    public String getTel2() {
        return tel2.get();
    }

    public void setTel2(String tel2) {
        this.tel2.set(tel2);
    }

    public String getTel3() {
        return tel3.get();
    }

    public void setTel3(String tel3) {
        this.tel3.set(tel3);
    }

    public String getNotiz() {
        return notiz.get();
    }

    public void setNotiz(String notiz) {
        this.notiz.set(notiz);
    }

    @Override
    public String toString() {
        return
                "  Kundennummer : " + cusNr.get() +
                "  \n  Nachname : " + lastName.get() + "  \n  "+
                        strasse.get() + "." + hausNr.get() +"  \n  "+

                 ort.get() + "  \n  " + tel1.get();
    }

}
