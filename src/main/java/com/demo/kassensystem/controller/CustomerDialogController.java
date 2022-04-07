package com.demo.kassensystem.controller;

import com.demo.kassensystem.model.Customer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomerDialogController {
    @FXML
    private TextField cusNrTxf;

    @FXML
    private TextField firstNameTxf;

    @FXML
    private TextField lastNameTxt;

    @FXML
    private TextField strTxf;

    @FXML
    private TextField hausNrTxf;

    @FXML
    private TextField plzTxf;

    @FXML
    private TextField ortTxf;

    @FXML
    private TextField tel1Txf;

    @FXML
    private TextField tel2Txf;

    @FXML
    private TextField tel3Txf;

    @FXML
    private TextField lastOrderTxf;

    @FXML
    private TextArea notizTextArea;

    @FXML
    private Button todayBtn;

    private void initialize (){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                cusNrTxf.requestFocus();
            }
        });
    }



    public Customer getNewCustomer (){
        Integer cusNr;
        try {
            cusNr = Integer.parseInt(cusNrTxf.getText());
        }catch (NumberFormatException e){
            return null;
        }
        String tel1 = telWithSpace(tel1Txf.getText());
        String tel2 = telWithSpace(tel2Txf.getText());
        String tel3 = telWithSpace(tel3Txf.getText());
        return new Customer(cusNr, firstNameTxf.getText(), lastNameTxt.getText(),
                strTxf.getText(), hausNrTxf.getText(), plzTxf.getText(), ortTxf.getText(), tel1,
                tel2, tel3, lastOrderTxf.getText(), notizTextArea.getText());
    }

    public void setNewCusNr (String newCusNr){
        cusNrTxf.setText(newCusNr);
    }

    public void editCustomer (Customer customer){
        cusNrTxf.setText(customer.getCusNr()+"");
        firstNameTxf.setText(customer.getFirstName());
        lastNameTxt.setText(customer.getLastName());
        strTxf.setText(customer.getStrasse());
        hausNrTxf.setText(customer.getHausNr());
        plzTxf.setText(customer.getPlz());
        ortTxf.setText(customer.getOrt());
        tel1Txf.setText(customer.getTel1());
        tel2Txf.setText(customer.getTel2());
        tel3Txf.setText(customer.getTel3());
        lastOrderTxf.setText(customer.getLastOrder());
        notizTextArea.setText(customer.getNotiz());
        cusNrTxf.setEditable(false);
    }

    public void editedCustomer(Customer customer){
        customer.setFirstName(firstNameTxf.getText());
        customer.setLastName(lastNameTxt.getText());
        customer.setStrasse(strTxf.getText());
        customer.setHausNr(hausNrTxf.getText());
        customer.setPlz(plzTxf.getText());
        customer.setOrt(ortTxf.getText());
        customer.setTel1(tel1Txf.getText());
        customer.setTel2(tel2Txf.getText());
        customer.setTel3(tel3Txf.getText());
        customer.setLastOrder(lastOrderTxf.getText());
        customer.setNotiz(notizTextArea.getText());
    }

    public void setBstLHeute(){
        LocalDate heute = LocalDate.now();
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d/MM/uuuu");
        lastOrderTxf.setText(heute.format(formatters));
    }

    private String telWithSpace(String tel){
        tel = tel.replace(" ","");
        int telLength = tel.length();
        StringBuilder stringBuilder = new StringBuilder(tel);
        if (tel.startsWith("089")){
            int n = 6;
            int i = 0;
            stringBuilder.insert(3," ");

            while (n < telLength){
                stringBuilder.insert(n+i, " ");
                n+=2;
                i++;
            }
            return stringBuilder.toString();
        }

        if(tel.startsWith("08121")){
            int n = 8;
            int i = 0;
            stringBuilder.insert(5," ");

            while (n < telLength){
                stringBuilder.insert(n+i, " ");
                n+=2;
                i++;
            }
            return stringBuilder.toString();
        }

        if (telLength < 9){
            int n = 2;
            int i = 0;
            while (n < telLength){
                stringBuilder.insert(n+i, " ");
                n+=2;
                i++;
            }
            return stringBuilder.toString();
        }

        int m = 7;
        int i = 0;
        stringBuilder.insert(4, " ");
        while (m < telLength){
            stringBuilder.insert(m+i, " ");
            m+=2;
            i++;
        }
        return stringBuilder.toString();
    }
}
