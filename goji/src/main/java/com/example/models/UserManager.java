package com.example.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserManager {
    private static final String INSERT_USER_SQL = "INSERT INTO user (name, balance, email) VALUES (?, ?, ?)";
    private static final String UPDATE_BALANCE_SQL = "UPDATE user SET balance = balance + ? WHERE id = ?";
    private static final String CHECK_EMAIL = "SELECT email from user where email = ?";

    public static void addUser(Connection connection, User user) {
        try{
            PreparedStatement checkEmailPreparedStatement = connection.prepareStatement(CHECK_EMAIL);
            checkEmailPreparedStatement.setString(1, user.getEmail());
            ResultSet result = checkEmailPreparedStatement.executeQuery();
            boolean emailExists = false;
            while(result.next()){
                if(result.getString("email").equals(user.getEmail())){
                    emailExists = true;
                    break;
                }
            }
            if (!emailExists) {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL);
                preparedStatement.setString(1, user.getName());
                preparedStatement.setInt(2, user.getBalance());
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.execute();
                System.out.println("User added: " + user.getName());
            } else {
                System.out.println("User with this email already exists.");
            }
        }
        catch(Exception e){
            System.out.println("Error");
            e.printStackTrace();
        }
    }
    

    public static void updateUserBalance(Connection connection, int amount, int userId) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BALANCE_SQL);
            preparedStatement.setInt(1, amount);
            preparedStatement.setInt(2, userId);
            preparedStatement.execute();
            System.out.println("Updated balance for user with ID " + userId);
        }
        catch(Exception e){
            System.out.println("Error");
            e.printStackTrace();
        }
    }
}
