package Accounts;

import java.sql.*;

/**
 * Base Account Class - Handles common attributes and behaviors for Savings and Credit accounts.
 */
public abstract class Account {
    protected int bankID;
    protected String type; // Savings or Credit
    protected String accountID;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String pin;

    protected static final String DB_URL = "jdbc:sqlite:Database/Database.db";

    // Constructor for creating a new account
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

    /**
     * Inserts the account into the appropriate table (SavingsAccount or CreditAccount).
     *
     * @param table         The target table ("SavingsAccount" or "CreditAccount").
     * @param balanceColumn  The column to store balance or loan ("Balance" or "Loan").
     * @param defaultValue   The initial value (e.g., starting balance or loan amount).
     * @return true if insertion is successful, false otherwise.
     */
    public boolean insertAccount(String table, String balanceColumn, double defaultValue) {
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

    /**
     * Generates a unique account ID for the account (e.g., SA01-BankID or CA02-BankID).
     *
     * @return The generated account ID string.
     */
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

    // Getters for account info

    public int getBankID() {
        return bankID;
    }

    public String getType() {
        return type;
    }

    public String getAccountID() {
        return accountID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getPin() {
        return pin;
    }

}
