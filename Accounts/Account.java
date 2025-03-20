package Accounts;

import Bank.*;
import Transactions.*;
import java.sql.*;
import java.util.ArrayList;

/**
 * Account Class
 * An abstract class representing a bank account.
 * Provides basic functionality and tracks account information and transactions.
 *
 * Attributes:
 * - Bank BankID: The associated bank object.
 * - String AccountID: The unique account ID.
 * - String AccountType: The type of account (e.g., SA, CA).
 * - String ownerFirstName, ownerLastName, ownerEmail: Account owner's personal information.
 * - String pin: The security PIN for this account.
 * - ArrayList<Transaction> transactions: A log of all account transactions.
 *
 * Methods:
 * - getFullName(): Returns the full name of the account owner.
 * - addNewTransaction(): Logs a new transaction and saves it to the database.
 * - getTransactionsInfo(): Retrieves all transactions from the database.
 * - Various getters and setters for account data.
 */
public abstract class Account {
    private Bank BankID;
    private String AccountID;
    private String AccountType;
    private String ownerFirstName, ownerLastName, ownerEmail;
    private String pin;
    private ArrayList<Transaction> transactions;
    private static final String DB_URL = "jdbc:sqlite:Database/Database.db";

    /**
     * Constructor to initialize an Account object.
     */
    public Account(Bank bank, String accountID, String accountType, String ownerFirstName, String ownerLastName, String ownerEmail, String pin) {
        this.BankID = bank;
        this.AccountID = accountID;
        this.AccountType = accountType;
        this.ownerFirstName = ownerFirstName;
        this.ownerLastName = ownerLastName;
        this.ownerEmail = ownerEmail;
        this.pin = pin;
        this.transactions = new ArrayList<>();
    }

    // Getters and Setters
    public Bank getBankID() { return BankID; }
    public void setBankID(Bank bank) { this.BankID = bank; }

    public String getAccountID() { return AccountID; }
    public void setAccountID(String accountID) { this.AccountID = accountID; }

    public String getAccountType() { return AccountType; }
    public void setAccountType(String accountType) { this.AccountType = accountType; }

    public String getOwnerFirstName() { return ownerFirstName; }
    public void setOwnerFirstName(String ownerFirstName) { this.ownerFirstName = ownerFirstName; }

    public String getOwnerLastName() { return ownerLastName; }
    public void setOwnerLastName(String ownerLastName) { this.ownerLastName = ownerLastName; }

    public String getOwnerEmail() { return ownerEmail; }
    public void setOwnerEmail(String ownerEmail) { this.ownerEmail = ownerEmail; }

    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }

    public ArrayList<Transaction> getTransactions() { return transactions; }
    public void setTransactions(ArrayList<Transaction> transactions) { this.transactions = transactions; }

    /**
     * Returns the full name of the account owner.
     *
     * @return The full name as "FirstName LastName".
     */
    public String getFullName() {
        return ownerFirstName + " " + ownerLastName;
    }

    /**
     * Logs a new transaction for this account and saves it to the database.
     *
     * @param amount The transaction amount.
     * @param type The type of transaction (e.g., withdrawal, deposit).
     * @param description A brief description of the transaction.
     */
    public void addNewTransaction(double amount, String type, String description) {
        Transaction transaction = new Transaction(AccountID, amount, type, description);
        transactions.add(transaction);

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:Database/Database.db")) {
            String sql = "INSERT INTO Transactions (AccountID, Type, Amount, Description, Date) VALUES (?, ?, ?, ?, datetime('now', 'localtime'))";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, AccountID);
            pstmt.setString(2, type);
            pstmt.setDouble(3, amount);
            pstmt.setString(4, description);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all logged transactions from the database.
     *
     * @return A string containing all transaction details.
     */
    public String getTransactionsInfo() {
        StringBuilder info = new StringBuilder();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:Database/Database.db")) {
            String sql = "SELECT * FROM Transactions WHERE AccountID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, AccountID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                info.append("TransactionID: ").append(rs.getInt("TransactionID"))
                        .append(", Type: ").append(rs.getString("Type"))
                        .append(", Amount: ").append(rs.getDouble("Amount"))
                        .append(", Description: ").append(rs.getString("Description"))
                        .append(", Date: ").append(rs.getString("Date"))
                        .append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return info.toString();
    }

    /**
     * Returns a string representation of the account object.
     *
     * @return A string containing account details.
     */
    @Override
    public String toString() {
        return "Account{" +
                "BankID=" + BankID +
                ", AccountID='" + AccountID + '\'' +
                ", AccountType='" + AccountType + '\'' +
                ", OwnerFirstName='" + ownerFirstName + '\'' +
                ", OwnerLastName='" + ownerLastName + '\'' +
                ", OwnerEmail='" + ownerEmail + '\'' +
                ", PIN='" + pin + '\'' +
                ", Transactions=\n" + getTransactionsInfo() +
                '}';
    }
}
