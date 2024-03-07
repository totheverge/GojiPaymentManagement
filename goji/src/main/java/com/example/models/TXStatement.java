package com.example.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TXStatement {
    public static final String senderQuery = "SELECT * FROM txhistory WHERE senderid = ?";
    public static void displayTransactionHistory(Connection connection, int userId) {
        try {
            PreparedStatement senderStatement = connection.prepareStatement(senderQuery);
            senderStatement.setInt(1, userId);
            ResultSet senderResult = senderStatement.executeQuery();

            System.out.println("Sent Transactions:");
            while (senderResult.next()) {
                int receiverId = senderResult.getInt("receiverid");
                int amount = senderResult.getInt("amount");
                System.out.println("Sent " + amount + " to user with ID " + receiverId);
            }
            String receiverQuery = "SELECT * FROM txhistory WHERE receiverid = ?";
            PreparedStatement receiverStatement = connection.prepareStatement(receiverQuery);
            receiverStatement.setInt(1, userId);
            ResultSet receiverResult = receiverStatement.executeQuery();

            System.out.println("\nReceived Transactions:");
            while (receiverResult.next()) {
                int senderId = receiverResult.getInt("senderid");
                int amount = receiverResult.getInt("amount");
                System.out.println("Received " + amount + " from user with ID " + senderId);
            }
        } catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
        }
    }
}
