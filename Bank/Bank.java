package Bank;

import Accounts.*;
import Main.Field;
import java.sql.*;
import java.util.ArrayList;

/**
 * Bank Class
 * Represents a bank that manages accounts and enforces transaction limits.
 *
 * Now integrates with SQLite for persistent data storage.
 */

public class Bank {
    private int ID;
    private String name, passcode;
    private double DepositLimit, WithdrawLimit, CreditLimit;
    private double processingFee;

    private static final String DB_URL = "jdbc:sqlite:Database/Database.db";

    public Bank(int ID, String name, String passcode) {
        this.ID = ID;
        this.name = name;
        this.passcode = passcode;
    }

    public Bank(int ID, String name, String passcode, double DepositLimit, double WithdrawLimit, double CreditLimit, double processingFee) {
        this(ID, name, passcode);
        this.DepositLimit = DepositLimit;
        this.WithdrawLimit = WithdrawLimit;
        this.CreditLimit = CreditLimit;
        this.processingFee = processingFee;
    }

    /** Getters and Setters **/
    public int getID() { return ID; }
    public void setID(int ID) { this.ID = ID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPasscode() { return passcode; }
    public void setPasscode(String passcode) { this.passcode = passcode; }

    public double getDepositLimit() { return DepositLimit; }
    public void setDepositLimit(double depositLimit) { DepositLimit = depositLimit; }

    public double getWithdrawLimit() { return WithdrawLimit; }
    public void setWithdrawLimit(double withdrawLimit) { WithdrawLimit = withdrawLimit; }

    public double getCreditLimit() { return CreditLimit; }
    public void setCreditLimit(double creditLimit) { CreditLimit = creditLimit; }

    public double getProcessingFee() { return processingFee; }
    public void setProcessingFee(double processingFee) { this.processingFee = processingFee; }

    /**
     * Saves the bank to the database.
     */
    public void saveToDatabase() {
        String sql = "INSERT INTO Bank (name, passcode, DepositLimit, WithdrawLimit, CreditLimit, processingFee) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, this.name);
            pstmt.setString(2, this.passcode);
            pstmt.setDouble(3, this.DepositLimit);
            pstmt.setDouble(4, this.WithdrawLimit);
            pstmt.setDouble(5, this.CreditLimit);
            pstmt.setDouble(6, this.processingFee);
            pstmt.executeUpdate();

            System.out.println("Bank saved to database.");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /**
     * Loads a bank from the database by its ID.
     * @param bankID The ID of the bank.
     * @return Bank object if found, null otherwise.
     */
    public static Bank loadFromDatabase(int bankID) {
        String sql = "SELECT * FROM Bank WHERE BankID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bankID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Bank(
                        rs.getInt("BankID"),
                        rs.getString("name"),
                        rs.getString("passcode"),
                        rs.getDouble("DepositLimit"),
                        rs.getDouble("WithdrawLimit"),
                        rs.getDouble("CreditLimit"),
                        rs.getDouble("processingFee")
                );
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return null;
    }

    // Existing constructors and getters/setters...

    /**
     * Displays accounts based on the specified type.
     * @param accountType Type of account to display.
     */
    public <T> void showAccounts(Class<T> accountType) {
        String typeName = accountType.getSimpleName();
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String query = "SELECT * FROM Account WHERE BankID = ? AND AccountType = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, this.ID);
                pstmt.setString(2, typeName);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    System.out.println("Account Number: " + rs.getString("AccountNumber") + ", Balance: " + rs.getDouble("Balance"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error displaying accounts: " + e.getMessage());
        }
    }

    /**
     * Retrieves an account by account number from the specified bank.
     * @param accountNum Account number to find.
     * @return Account object if found, null otherwise.
     */
    public Account getBankAccount(String accountNum) {
        // TODO: COMPLETE THIS METHOD
        return null;
    }

    /**
     * Creates a new credit account.
     * @return New CreditAccount object.
     */
    public CreditAccount createNewCreditAccount(String accountNum, double initialBalance) {
        return null;
    }

    /**
     * Creates a new savings account.
     * @return New SavingsAccount object.
     */
    public SavingsAccount createNewSavingsAccount(String accountNum, double initialBalance) {
        // TODO: COMPLETE THIS METHOD
        return null;
    }

    /**
     * Adds a new account to the bank if the account number doesn't already exist.
     */
    public void addNewAccount(Account account) {
        // TODO: Complete this method
    }

    /**
     * Checks if an account exists in the specified bank by account number.
     * @return True if the account exists, false otherwise.
     */
    public boolean accountExists(String accountNum) {
        String sql = "SELECT 1 FROM Account WHERE BankID = ? AND AccountID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, this.ID);
            pstmt.setString(2, accountNum);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error checking account existence: " + e.getMessage());
        }
        return false;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", passcode='" + passcode + '\'' +
                ", DepositLimit=" + DepositLimit +
                ", WithdrawLimit=" + WithdrawLimit +
                ", CreditLimit=" + CreditLimit +
                ", processingFee=" + processingFee +
                '}';
    }
}
