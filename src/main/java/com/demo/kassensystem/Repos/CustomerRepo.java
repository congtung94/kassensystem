package com.demo.kassensystem.Repos;

import com.demo.kassensystem.Database;
import com.demo.kassensystem.model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomerRepo {

    private static CustomerRepo customerRepo = new CustomerRepo();
    private CustomerRepo(){
    }

    public static CustomerRepo getCustomerRepo(){
        return customerRepo;
    }

    private final String TABLE_CUSTOMER = "customer";
    private final String CUSTOMER_NR_COL = "cus_nr";
    private final String LAST_NAME_COL = "last_name";
    private final String FIRST_NAME_COL = "first_name";
    private final String STRASSE = "strasse";
    private final String HAUSNR = "haus_nr";
    private final String ORT = "ort";
    private final String PLZ = "plz";
    private final String TEL1 = "tel1";
    private final String TEL2 = "tel2";
    private final String TEL3 = "tel3";
    private final String LAST_ORDER = "last_order";
    private final String NOTIZ = "notiz";


    private final String QUERY_DATA_FROM_CUSTOMER_TABLE ="SELECT * FROM " + TABLE_CUSTOMER;

    private final String QUERY_CUSTOMER_WITH_NR = "SELECT * FROM " +
            TABLE_CUSTOMER + " WHERE "+ CUSTOMER_NR_COL + "= ?";

    private final String QUERY_CUSTOMER_WITH_NR_OR_LASTNAME_OR_FIRSTNAME_OR_STRASSE_OR_ORT_OR_PLZ
            = "SELECT * FROM " + TABLE_CUSTOMER + " WHERE " +
            CUSTOMER_NR_COL + "= ? or " +
            LAST_NAME_COL + "= ? or "+
            FIRST_NAME_COL + "= ? or "+
            STRASSE + "= ? or "+
            ORT + "= ? or "+
            PLZ + "= ?";

    private final String UPDATE_CUSTOMER = "UPDATE "+ TABLE_CUSTOMER + " SET " + CUSTOMER_NR_COL + " = ?, "
            + FIRST_NAME_COL + " = ?, " + LAST_NAME_COL + " = ?, "  + STRASSE + " = ?, " + HAUSNR + " = ?, " +
             PLZ + " = ?, " +ORT + " = ?, "  +  TEL1 + " = ?, " +
             TEL2 + " = ?, " +  TEL3 + " = ?, " + LAST_ORDER + " = ?, " + NOTIZ + " = ?" + " WHERE " + CUSTOMER_NR_COL + "= ?";


    private final String DELETE_CUSTOMER = "DELETE FROM " + TABLE_CUSTOMER + " WHERE " + CUSTOMER_NR_COL + "= ?";

    private final String ADD_CUSTOMER = "INSERT INTO "+ TABLE_CUSTOMER + "(" + CUSTOMER_NR_COL + ", "
            + FIRST_NAME_COL + ", " + LAST_NAME_COL + ", " + STRASSE + ", " + HAUSNR + ", " +
            PLZ + ", " +ORT + ", " + TEL1 + ", " + TEL2 + ", " + TEL3 + ", " + LAST_ORDER + ", " + NOTIZ + ")" + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

    // alle Kunden in der datenbank zurückgeben
    public ObservableList<Customer> getCustomers () throws SQLException {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        Statement statement = Database.getInstance().getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(QUERY_DATA_FROM_CUSTOMER_TABLE);

        while ((resultSet.next())){
            int cusNr = resultSet.getInt(1);
            String firstName = resultSet.getString(2);
            String lastName = resultSet.getString(3);
            String strasse = resultSet.getString(4);
            String hausNr = resultSet.getString(5);
            String plz = resultSet.getString(6);
            String ort = resultSet.getString(7);
            String tel1 = resultSet.getString(8);
            String tel2 = resultSet.getString(9);
            String tel3 = resultSet.getString(10);
            String lastOrder = resultSet.getString(11);
            String notiz = resultSet.getString(12);

            customers.add(new Customer(cusNr, firstName, lastName, strasse, hausNr, plz, ort, tel1, tel2, tel3, lastOrder, notiz));
        }

        statement.close();
        resultSet.close();

        return customers;
    }

    // neuen Kunde hinzufügen
    public boolean addCustomer (Customer customer) throws SQLException {
        PreparedStatement preparedStatement = Database.getInstance().getConnection().prepareStatement(ADD_CUSTOMER);
        preparedStatement.setInt(1, customer.getCusNr());
        preparedStatement.setString(2, customer.getFirstName());
        preparedStatement.setString(3, customer.getLastName());
        preparedStatement.setString(4, customer.getStrasse());
        preparedStatement.setString(5, customer.getHausNr());
        preparedStatement.setString(6, customer.getPlz());
        preparedStatement.setString(7, customer.getOrt());
        preparedStatement.setString(8, customer.getTel1());
        preparedStatement.setString(9, customer.getTel2());
        preparedStatement.setString(10, customer.getTel3());
        preparedStatement.setString(11, customer.getLastOrder());
        preparedStatement.setString(12, customer.getNotiz());

        boolean result = preparedStatement.execute();
        preparedStatement.close();
        return result;
    }

    // update Kunde daten
    public boolean updateCustomer(Customer customer) throws SQLException {
        PreparedStatement preparedStatement = Database.getInstance().getConnection().prepareStatement(UPDATE_CUSTOMER);
        preparedStatement.setInt(1, customer.getCusNr());
        preparedStatement.setString(2, customer.getFirstName());
        preparedStatement.setString(3, customer.getLastName());
        preparedStatement.setString(4, customer.getStrasse());
        preparedStatement.setString(5, customer.getHausNr());
        preparedStatement.setString(6, customer.getPlz());
        preparedStatement.setString(7, customer.getOrt());
        preparedStatement.setString(8, customer.getTel1());
        preparedStatement.setString(9, customer.getTel2());
        preparedStatement.setString(10, customer.getTel3());
        preparedStatement.setString(11, customer.getLastOrder());
        preparedStatement.setString(12, customer.getNotiz());
        preparedStatement.setInt(13, customer.getCusNr());
        int tmp = preparedStatement.executeUpdate();
        preparedStatement.close();

        return tmp == 1;
    }

    // delete Customer
    public boolean deleteCustomer(Customer customer) throws SQLException {
        PreparedStatement preparedStatement = Database.getInstance().getConnection().prepareStatement(DELETE_CUSTOMER);
        preparedStatement.setInt(1, customer.getCusNr());
        boolean result = preparedStatement.execute();
        preparedStatement.close();
        return result;
    }

    // Kunde abfragen
    public boolean existsCustomer (int cusNr) throws SQLException {
        PreparedStatement preparedStatement = Database.getInstance().getConnection().prepareStatement(QUERY_CUSTOMER_WITH_NR);
        preparedStatement.setInt(1, cusNr);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    // zum testen
    public static void main(String[] args) {
        System.out.println(customerRepo.QUERY_DATA_FROM_CUSTOMER_TABLE);
        System.out.println(customerRepo.QUERY_CUSTOMER_WITH_NR);
        System.out.println(customerRepo.QUERY_CUSTOMER_WITH_NR_OR_LASTNAME_OR_FIRSTNAME_OR_STRASSE_OR_ORT_OR_PLZ);
        System.out.println(customerRepo.UPDATE_CUSTOMER);
        System.out.println(customerRepo.DELETE_CUSTOMER);
        System.out.println(customerRepo.ADD_CUSTOMER);

    }

}
