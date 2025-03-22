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
    // Getters
    public int getBankID() { return bankID; }
    public String getType() { return type; }
    public String getAccountID() { return accountID; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPin() { return pin; }

    //Setters
    public void setBankID(int bankID) { this.bankID = bankID; }
    public void setType(String Type) { this.type = Type; }
    public void setAccountID(String accountID) { this.accountID = accountID; }
    public void setFirstName(String FirstName) { this.firstName = FirstName; }
    public void setLastName(String LastName) { this.lastName = LastName; }
    public void setEmail(String Email) { this.email = Email; }
    public void setPin(String Pin) { this.pin = Pin; }


    // Method to get full name of the account owner
    public String getOwnerFullName() {
        return firstName + " " + lastName;
    }


    // Ensure the account type is either Savings or Credit
    private String validateAccountType(String type) {
        if (type.equalsIgnoreCase("Savings") || type.equalsIgnoreCase("Credit") || type.equalsIgnoreCase("Educational") || type.equalsIgnoreCase("Business")) {
            return type;
        }
        throw new IllegalArgumentException("Invalid account type. Must be 'Savings' , 'Credit' , 'Business' or 'Educational'");
    }

    // Insert Account into SQLite database (handles Savings and Credit properly)
    public boolean insertAccount(double initialAmount) {
        String table;
        String sql;

        // Determine table and SQL statement based on account type
        if (type.equalsIgnoreCase("Savings")) {
            table = "SavingsAccount";
            sql = "INSERT INTO " + table + " (BankID, AccountID, FirstName, LastName, Email, PIN, Balance) VALUES (?, ?, ?, ?, ?, ?, ?)";
        } else if (type.equalsIgnoreCase("Credit")) {
            table = "CreditAccount";
            sql = "INSERT INTO " + table + " (BankID, AccountID, FirstName, LastName, Email, PIN, Loan) VALUES (?, ?, ?, ?, ?, ?, ?)";
        } else if (type.equalsIgnoreCase("Business")) {
            table = "BusinessAccount";
            sql = "INSERT INTO " + table + " (BankID, AccountID, FirstName, LastName, Email, PIN, Loan) VALUES (?, ?, ?, ?, ?, ?, ?)";
        } else if (type.equalsIgnoreCase("Educational")) {
            table = "EducationalAccount";
            sql = "INSERT INTO " + table + " (BankID, AccountID, FirstName, LastName, Email, PIN, Balance) VALUES (?, ?, ?, ?, ?, ?, ?)";
        } else {
            System.out.println("Invalid account type.");
            return false;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, bankID);
                pstmt.setString(2, accountID);
                pstmt.setString(3, firstName);
                pstmt.setString(4, lastName);
                pstmt.setString(5, email);
                pstmt.setString(6, pin);
                pstmt.setDouble(7, initialAmount);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    conn.commit();
                    System.out.println("Account created successfully!");
                    System.out.println("Account ID: " + accountID);
                    return true;
                } else {
                    conn.rollback(); // Rollback if insert fails
                    System.out.println("Failed to insert account.");
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLite error: " + e.getMessage());
        }
        return false;
    }


    // Generate Account ID (SA01-BankID or CA02-BankID)
    private String generateAccountID() {
        String prefix;
        String table;

        // Determine prefix and table based on account type
        switch (type.toLowerCase()) {
            case "savings":
                prefix = "SA";
                table = "SavingsAccount";
                break;
            case "credit":
                prefix = "CA";
                table = "CreditAccount";
                break;
            case "business":
                prefix = "BA";
                table = "BusinessAccount";
                break;
            case "educational":
                prefix = "EA";
                table = "EducationalAccount";
                break;
            default:
                throw new IllegalArgumentException("Invalid account type: " + type);
        }

        // Validate that table is set correctly
        if (table == null || table.isEmpty()) {
            throw new IllegalStateException("Table name is not properly assigned for account type: " + type);
        }

        int count = 1;
        String sql = "SELECT COUNT(*) AS count FROM " + table + " WHERE BankID = ?"; // Corrected Query

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bankID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt("count") + 1; // Ensure count starts from 1
            }
        } catch (SQLException e) {
            System.out.println("Failed to generate account ID: " + e.getMessage());
        }

        // Generate AccountID in format SA01-4 (example for bankID=4)
        return String.format("%s%02d-%d", prefix, count, bankID);
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

    // Fetch transaction history for the account
    public String getTransactionsInfo() {
        StringBuilder info = new StringBuilder();

        // Print the account ID as part of the output
        info.append("Transaction History for Account ID: ").append(this.accountID).append("\n");

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String query = "SELECT * FROM Transactions WHERE AccountID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, this.accountID);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    info.append("- Type: ").append(rs.getString("Type"))
                            .append(", Amount: ").append(rs.getDouble("Amount"))
                            .append(", Description: ").append(rs.getString("Description"))
                            .append(", Date: ").append(rs.getString("Date"))
                            .append("\n");
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to fetch transactions: " + e.getMessage());
        }

        return info.toString();
    }
    // Insert Account into SQLite database (handles Savings and Credit properly)
    private boolean isAccountIDUnique(String accountID) {
        String sql = "SELECT COUNT(*) AS count FROM " + getAccountTable() + " WHERE AccountID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accountID);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt("count") == 0; // If count == 0, ID is unique
        } catch (SQLException e) {
            System.out.println("Error checking account ID uniqueness: " + e.getMessage());
        }
        return false;
    }
    // Helper method to get correct table name
    private String getAccountTable() {
        switch (type.toLowerCase()) {
            case "savings": return "SavingsAccount";
            case "credit": return "CreditAccount";
            case "business": return "BusinessAccount";
            case "educational": return "EducationalAccount";
            default: throw new IllegalArgumentException("Invalid account type: " + type);
        }
    }



    @Override
    public String toString() {
        return "Account{" +
                "BankID=" + bankID +
                ", AccountID='" + accountID + '\'' +
                ", FirstName='" + firstName + '\'' +
                ", LastName='" + lastName + '\'' +
                ", Email='" + email + '\'' +
                '}';
    }
}
