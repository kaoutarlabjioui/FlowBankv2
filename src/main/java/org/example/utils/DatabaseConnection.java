package org.example.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static final String URL = "jdbc:postgresql://localhost:5432/flowbank";
    public static final String USER = "postgres";
    public static final String PASSWORD = "0000";
     private static int counter = 0;
    private static DatabaseConnection instance;
    private static Connection connection;

    private DatabaseConnection(){
        try{
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            counter++;
            System.out.println("Database connection established  ");
            System.out.println(counter);
        } catch (SQLException e){
            System.out.println("failed connection to the database ");
            e.printStackTrace();
        }
    }

    public static DatabaseConnection getInstance(){
        if(instance == null){
            instance = new DatabaseConnection();
        }
        return instance;


    }

    public Connection getConnection(){
        return connection ;
    }


}
