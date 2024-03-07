package com.example;

import java.sql.*;

import com.example.models.PaymentManager;
import com.example.models.TXStatement;
import com.example.models.User;
import com.example.models.UserManager;

public class Main {
    public static final String USER_TABLE = "user";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_BALANCE = "balance";
    public static final String COLUMN_EMAIL = "email";


    public static void main(String[] args) {
        String url = "jdbc:sqlite:mydb.db";
        try{
            Connection connection = DriverManager.getConnection(url);
            System.out.println("Connected");
            String createTableSQL = "CREATE TABLE IF NOT EXISTS "+USER_TABLE+"("+ COLUMN_ID +" integer PRIMARY KEY NOT NULL," +
                    COLUMN_NAME+" text," +
                    COLUMN_BALANCE+" integer," +
                    COLUMN_EMAIL +" text)";
            Statement statement = connection.createStatement();
            statement.execute(createTableSQL);
            System.out.println("Database Created");
            User u1 = new User("test@test.com","Mandip Lal Joshi",1000);
            UserManager.addUser(connection, u1);
            UserManager.updateUserBalance(connection, 100, 1);
            PaymentManager.makePayment(connection, 100, 1, 2);
            TXStatement.displayTransactionHistory(connection, 2);
            connection.close();
        }
        catch(Exception e){
            System.out.println("Error");
            e.printStackTrace();
        }
    }
}