package Accounts;

import Transactions.Transaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Account {
    private int bankID;
    private String type; // SA or CA
    private String accountID;
    private String firstName;
    private String lastName;
    private String email;
    private String pin;
    private double balance = 0.0;
    private double loan = 0.0;

    // Constructor
    public Account(int bankID, String type, String firstName, String lastName, String email, String pin) {
        this.bankID = bankID;
        this.type = type;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.pin = pin;
        this.accountID = generateAccountID();
    }

    // Insert Account into SQLite database
    public boolean insertAccount(int bankID, String type, String firstName, String lastName, String email, String pin) {
        String url = "jdbc:sqlite:Database/Database.db";
        String sql = """
                INSERT INTO Account (ID, Type, AccountID, FirstName, LastName, Email, PIN) 
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bankID);
            pstmt.setString(2, type);
            pstmt.setString(3, accountID);
            pstmt.setString(4, firstName);
            pstmt.setString(5, lastName);
            pstmt.setString(6, email);
            pstmt.setString(7, pin);

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
        int randomNum = (int) (20250 + Math.random());
        return String.format("%s-%d-%d", type, bankID, randomNum);
    }

    // Getters
    public int getBankID() { return bankID; }
    public String getType() { return type; }
    public String getAccountID() { return accountID; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPin() { return pin; }
    public double getBalance() { return balance; }
    public double getLoan() { return loan; }

    // Setters
    public void setBalance(double balance) { this.balance = balance; }
    public void setLoan(double loan) { this.loan = loan; }

    // Method to get full name of the account owner
    public String getOwnerFullName() {
        return firstName + " " + lastName;
    }

    public void addNewTransaction(String accountNumber, Transaction.Transactions type, String description) {
        // TODO: Complete this method
    }

    /**
     * Retrieves all logged transactions as a formatted string.
     *
     * @return A string containing all transaction details.
     */
    public String getTransactionsInfo() {
        // TODO: Complete this method
        return "";
    }

    // Display account info
    @Override
    public String toString() {
        return String.format(
                "Account ID: %s\nName: %s %s\nEmail: %s\nType: %s\nBalance: %.2f\nLoan: %.2f\n------------------------",
                accountID, firstName, lastName, email, type, balance, loan
        );
    }
}
