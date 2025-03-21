package Accounts;

import Transactions.Transaction;
import java.sql.*;

public class Account {
    private int bankID;
    private String type; // Savings or Credit
    private String accountID;
    private String firstName;
    private String lastName;
    private String email;
    private String pin;

    private static final String DB_URL = "jdbc:sqlite:Database/Database.db";

    // Constructor
    public Account(int bankID, String type, String firstName, String lastName, String email, String pin) {
        this.bankID = bankID;
        this.type = validateAccountType(type);
        this.accountID = generateAccountID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.pin = pin;
    }

    // Ensure the account type is either Savings or Credit
    private String validateAccountType(String type) {
        if (type.equalsIgnoreCase("Savings") || type.equalsIgnoreCase("Credit")) {
            return type;
        }
        throw new IllegalArgumentException("Invalid account type. Must be 'Savings' or 'Credit'");
    }

    // Insert Account into SQLite database
    public boolean insertAccount() {
        String table = type.equalsIgnoreCase("Savings") ? "SavingsAccount" : "CreditAccount";
        String sql = "INSERT INTO " + table + " (BankID, AccountID, FirstName, LastName, Email, PIN) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bankID);
            pstmt.setString(2, accountID);
            pstmt.setString(3, firstName);
            pstmt.setString(4, lastName);
            pstmt.setString(5, email);
            pstmt.setString(6, pin);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Account created successfully!");
                System.out.println("Account ID: " + accountID);
                return true;
            }
        } catch (SQLException e) {
            System.out.println("SQLite error: " + e.getMessage());
        }

        return false;
    }

    // Generate Account ID (Type-BankID-XXXX)
    private String generateAccountID() {
        int randomNum = (int) (Math.random() * 9000 + 1000); // Generates a 4-digit number
        return String.format("%s-%d-%d", type, bankID, randomNum);
    }

    // Getters
    public int getBankID() { return bankID; }
    public String getType() { return type; }
    public String getAccountID() { return accountID; }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    // Logs a new transaction and saves it to the database
    public void addNewTransaction(String transactionType, double amount, String description) {
        String sql = "INSERT INTO Transactions (AccountID, Type, Amount, Description) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, this.accountID);
            pstmt.setString(2, transactionType);
            pstmt.setDouble(3, amount);
            pstmt.setString(4, description);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to add transaction: " + e.getMessage());
        }
    }

    public String getTransactionsInfo() {
        StringBuilder info = new StringBuilder();
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String query = "SELECT * FROM Transactions WHERE AccountID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, this.accountID);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    info.append("Type: ").append(rs.getString("Type"))
                            .append(", Amount: ").append(rs.getDouble("Amount"))
                            .append(", Description: ").append(rs.getString("Description"))
                            .append(", Date: ").append(rs.getString("Date"))
                            .append("\n");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return info.toString();
    }

    @Override
    public String toString() {
        return "Account{" +
                "BankID=" + bankID +
                ", AccountID='" + accountID + '\'' +
                ", FirstName='" + firstName + '\'' +
                ", LastName='" + lastName + '\'' +
                ", Email='" + email + '\'' +
                ", Pin='" + pin + '\'' +
                '}';
    }
}