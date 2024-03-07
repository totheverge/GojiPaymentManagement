package com.example.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class PaymentManager {
    private static final String SENDER_BALANCE_CHECK = "SELECT balance from user WHERE id = ?";
    private static final String SENDER_BALANCE_DEDUCT = "UPDATE user SET balance = balance - ? WHERE id = ?";
    private static final String RECEIVER_BALANCE_DEDUCT = "UPDATE user SET balance = balance + ? WHERE id = ?";
    private static final String TRANSACTION_HISTORY_SQL = "CREATE TABLE IF NOT EXISTS txhistory (txid integer PRIMARY KEY NOT NULL, senderid integer, receiverid integer, amount integer)";
    private static final String TRANSACTION_LOG = "INSERT into txhistory(senderid,receiverid,amount) values(?,?,?)";
    public static void makePayment(Connection connection, int amount, int senderUserId, int receiverUserId) {
        try{
            PreparedStatement senderBalanceCheck = connection.prepareStatement(SENDER_BALANCE_CHECK);
            senderBalanceCheck.setInt(1, senderUserId);
            ResultSet result = senderBalanceCheck.executeQuery();
            boolean enoughBalance = false;
            while(result.next()){
                if(result.getInt("balance")>=amount){
                    enoughBalance = true;
                    break;
                }
            }
            if(enoughBalance){
                Statement statement = connection.createStatement();
                statement.execute(TRANSACTION_HISTORY_SQL);
                System.out.println("Database Created");
                PreparedStatement senderPreparedStatement = connection.prepareStatement(SENDER_BALANCE_DEDUCT);
                senderPreparedStatement.setInt(1, amount);
                senderPreparedStatement.setInt(2, senderUserId);
                senderPreparedStatement.execute();
                PreparedStatement receiverPreparedStatement = connection.prepareStatement(RECEIVER_BALANCE_DEDUCT);
                receiverPreparedStatement.setInt(1, amount);
                receiverPreparedStatement.setInt(2, receiverUserId);
                receiverPreparedStatement.execute();
                PreparedStatement logPreparedStatement = connection.prepareStatement(TRANSACTION_LOG);
                logPreparedStatement.setInt(1, senderUserId);
                logPreparedStatement.setInt(2,receiverUserId);
                logPreparedStatement.setInt(3, amount);
                logPreparedStatement.execute();
                System.out.println("Sender: " + senderUserId + "\nReceiver: "+ receiverUserId +"\nAmount: "+amount);
            }
            else{
                System.out.println("Not Enough Balance");
            }
        }
        catch(Exception e){
            System.out.println("Error");
            e.printStackTrace();
        }
    }
}
