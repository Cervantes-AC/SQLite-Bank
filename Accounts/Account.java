package Accounts;

import java.sql.*;
import java.util.Scanner;

/**
 * Base Account Class - Handles Savings and Credit accounts
 */
public class Account {
    protected int bankID;
    protected String type; // Savings or Credit
    protected String accountID;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String pin;
    private static Scanner input = new Scanner(System.in);

    protected static final String DB_URL = "jdbc:sqlite:Database/Database.db";

    // Constructor for loading an existing account
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

    // Split creation into separate Savings and Credit methods with user input for initial value
    public static Account createSavingsAccount(int bankID, String firstName, String lastName, String email, String pin) {
        System.out.print("Enter initial deposit amount: ");
        double initialBalance = Double.parseDouble(input.nextLine());

        Account newAccount = new Account(bankID, "Savings", firstName, lastName, email, pin);
        return newAccount.insertAccount("SavingsAccount", "Balance", initialBalance) ? newAccount : null;
    }

    public static Account createCreditAccount(int bankID, String firstName, String lastName, String email, String pin) {
        System.out.print("Enter initial loan amount: ");
        double initialLoan = Double.parseDouble(input.nextLine());

        Account newAccount = new Account(bankID, "Credit", firstName, lastName, email, pin);
        return newAccount.insertAccount("CreditAccount", "Loan", initialLoan) ? newAccount : null;
    }

    // Insert Account into SQLite database (handles Savings and Credit properly)
    private boolean insertAccount(String table, String balanceColumn, double defaultValue) {
        String sql = String.format(
                "INSERT INTO %s (BankID, AccountID, FirstName, LastName, Email, PIN, %s) VALUES (?, ?, ?, ?, ?, ?, ?)",
                table, balanceColumn);

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set account details
            pstmt.setInt(1, bankID);
            pstmt.setString(2, accountID);
            pstmt.setString(3, firstName);
            pstmt.setString(4, lastName);
            pstmt.setString(5, email);
            pstmt.setString(6, pin);

            // Set default Balance/Loan value
            pstmt.setDouble(7, defaultValue);

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

    // Generate Account ID (SA01-BankID or CA02-BankID)
    private String generateAccountID() {
        String prefix = type.equalsIgnoreCase("Savings") ? "SA" : "CA";
        int count = 1;

        String table = type.equalsIgnoreCase("Savings") ? "SavingsAccount" : "CreditAccount";
        String sql = "SELECT COUNT(*) AS count FROM " + table + " WHERE BankID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bankID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt("count") + 1; // Increment for the next account
            }

        } catch (SQLException e) {
            System.out.println("Failed to generate account ID: " + e.getMessage());
        }

        // Format: SA01-BankID or CA01-BankID
        return String.format("%s%02d-%d", prefix, count, bankID);
    }

}
