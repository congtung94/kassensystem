package com.demo.kassensystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static Database instance = new Database();
    private Database(){

    }

    public static Database getInstance(){
        return instance;
    }

    private static final String DB_NAME = "database.db";
    private static final String CONNECTION_STRING = "jdbc:sqlite:" + DB_NAME;

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public boolean open(){
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING);
            System.out.println("verbindung zur datenbank aufgebaut");
            return true;
        }catch (SQLException e){
            return false;
        }
    }

    public boolean close(){
        try {
            connection.close();
            System.out.println("verbindung zur datenbank abgebaut");
            return true;
        }catch (SQLException e){
            return false;
        }
    }

}
