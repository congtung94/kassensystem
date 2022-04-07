package com.demo.kassensystem.Repos;

import com.demo.kassensystem.Database;
import com.demo.kassensystem.model.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ItemRepo {
    private static ItemRepo itemRepo = new ItemRepo();
    private ItemRepo(){

    }

    public static ItemRepo getItemRepo(){
        return itemRepo;
    }

    private final String TABLE_ITEM = "item";
    private final String ITEM_NR = "item_nr";
    private final String ITEM_NAME = "item_name";
    private final String UNIT_PRICE = "unit_price";
    private final String CATEGORY = "category";
    private final String DESCRIPTION = "description";
    private final String DISCOUNT = "discount";

    private final String QUERY_DATA_FROM_CUSTOMER_TABLE = "SELECT * FROM " + TABLE_ITEM;

    private final String QUERY_ITEM_WITH_NR = "SELECT * FROM " + TABLE_ITEM + " WHERE " + ITEM_NR + "= ? OR " + ITEM_NR + "= ?";
    private final String QUERY_ITEM_WITH_NR_OR_NAME_OR_CATEGORY =
            "SELECT * FROM " + TABLE_ITEM + " WHERE " +
            ITEM_NR + " = ? OR " + ITEM_NAME +" ? OR " + CATEGORY+ " =  ?";

    private final String UPDATE_ITEM = "UPDATE " + TABLE_ITEM + " SET " + ITEM_NR + " =?, "+
            ITEM_NAME + " =?, " + UNIT_PRICE + " =?, " + CATEGORY + " =?, " +
            DESCRIPTION + " =? ," + DISCOUNT + " =? " + "WHERE " + ITEM_NR + "=?";

    private final String DELETE_ITEM = "DELETE FROM "+ TABLE_ITEM + " WHERE " + ITEM_NR + " = ?";

    private final String ADD_ITEM = "INSERT INTO " + TABLE_ITEM + "(" + ITEM_NR + "," + ITEM_NAME
                                    + "," + UNIT_PRICE + "," + CATEGORY + "," + DESCRIPTION + "," + DISCOUNT + ")" +
                                    " VALUES (?,?,?,?,?,?)";



    public ObservableList<Item> getItems () throws SQLException {
        ObservableList<Item> items = FXCollections.observableArrayList();
        Statement statement = Database.getInstance().getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(QUERY_DATA_FROM_CUSTOMER_TABLE);

        while (resultSet.next()){
            String itemNr = resultSet.getString(1);
            String name = resultSet.getString(2);
            double price = resultSet.getDouble(3);
            String category = resultSet.getString(4);
            String desc = resultSet.getString(5);
            double discount = resultSet.getDouble(6);

            items.add(new Item(itemNr, name, price, category, desc, discount));
        }
        resultSet.close();
        statement.close();

        return items;
    }

    public void addItem (Item item) throws SQLException {
        PreparedStatement preparedStatement = Database.getInstance().getConnection().prepareStatement(ADD_ITEM);
        preparedStatement.setString(1, item.getNr());
        preparedStatement.setString(2, item.getName());
        preparedStatement.setDouble(3, item.getPrice());
        preparedStatement.setString(4, item.getCategory());
        preparedStatement.setString(5, item.getDescription());
        preparedStatement.setDouble(6, item.getDiscount());

        preparedStatement.execute();
        preparedStatement.close();
    }

    public void updateItem (Item item) throws SQLException {
        PreparedStatement preparedStatement = Database.getInstance().getConnection().prepareStatement(UPDATE_ITEM);
        preparedStatement.setString(1, item.getNr());
        preparedStatement.setString(2, item.getName());
        preparedStatement.setDouble(3, item.getPrice());
        preparedStatement.setString(4, item.getCategory());
        preparedStatement.setString(5, item.getDescription());
        preparedStatement.setDouble(6, item.getDiscount());
        preparedStatement.setString(7, item.getNr());

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void deleteItem (Item item) throws SQLException {
        PreparedStatement preparedStatement = Database.getInstance().getConnection().prepareStatement(DELETE_ITEM);
        preparedStatement.setString(1, item.getNr());
        preparedStatement.execute();
        preparedStatement.close();
    }

    public Item getItemWithNr (String itemNr) throws SQLException {
        PreparedStatement preparedStatement = Database.getInstance().getConnection().prepareStatement(QUERY_ITEM_WITH_NR);
        preparedStatement.setString(1, itemNr);
        preparedStatement.setString(2,itemNr.toUpperCase());
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()){
            return new Item(resultSet.getString(1), resultSet.getString(2), resultSet.getDouble(3),
                    resultSet.getString(4), resultSet.getString(5), resultSet.getDouble(6));
        }

        return null;
    }

    public static void main(String[] args) {
        System.out.println(itemRepo.QUERY_DATA_FROM_CUSTOMER_TABLE);
        System.out.println(itemRepo.QUERY_ITEM_WITH_NR);
        System.out.println(itemRepo.QUERY_ITEM_WITH_NR_OR_NAME_OR_CATEGORY);
        System.out.println(itemRepo.UPDATE_ITEM);
        System.out.println(itemRepo.DELETE_ITEM);
        System.out.println(itemRepo.ADD_ITEM);
    }
}
