package Bank;

import Accounts.*;
import java.sql.*;

/**
 * Bank Class
 * Represents a bank that manages accounts and enforces transaction limits.
 * Now integrates with SQLite for persistent data storage.
 */
public class Bank {
    private int BankID;
    private String name, passcode;
    private double DepositLimit = 50000.0, WithdrawLimit = 50000.0, CreditLimit = 10000.0;
    private double processingFee;

    private static final String DB_URL = "jdbc:sqlite:Database/Database.db";

    public Bank(String name, String passcode) {
        this.name = name;
        this.passcode = passcode;
    }

    public Bank(int BankID, String name, String passcode, double DepositLimit, double WithdrawLimit, double CreditLimit, double processingFee) {
        this(name, passcode);
        this.BankID = BankID;
        this.DepositLimit = DepositLimit;
        this.WithdrawLimit = WithdrawLimit;
        this.CreditLimit = CreditLimit;
        this.processingFee = processingFee;
    }

    /** Getters and Setters **/
    public int getBankID() {
        return BankID;
    }

    public void setBankID(int bankID) {
        this.BankID = bankID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

    public double getDepositLimit() {
        return DepositLimit;
    }

    public void setDepositLimit(double depositLimit) {
        DepositLimit = depositLimit;
    }

    public double getWithdrawLimit() {
        return WithdrawLimit;
    }

    public void setWithdrawLimit(double withdrawLimit) {
        WithdrawLimit = withdrawLimit;
    }

    public double getCreditLimit() {
        return CreditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        CreditLimit = creditLimit;
    }

    public double getProcessingFee() {
        return processingFee;
    }

    public void setProcessingFee(double processingFee) {
        this.processingFee = processingFee;
    }

    /**
     * Saves the bank to the database and retrieves the generated BankID.
     */
    public void InsertBank() {
        String sql = "INSERT INTO Bank (name, passcode, DepositLimit, WithdrawLimit, CreditLimit, processingFee) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, this.name);
            pstmt.setString(2, this.passcode);
            pstmt.setDouble(3, this.DepositLimit);
            pstmt.setDouble(4, this.WithdrawLimit);
            pstmt.setDouble(5, this.CreditLimit);
            pstmt.setDouble(6, this.processingFee);
            pstmt.executeUpdate();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                this.BankID = generatedKeys.getInt(1);
            }

            System.out.println("Bank saved to database with ID: " + this.BankID);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /**
     * Loads a bank from the database by its ID.
     * @param bankID The ID of the bank.
     * @return Bank object if found, null otherwise.
     */
    public static Bank LoadBank(int bankID) {
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
            } else {
                System.out.println("No bank found with ID: " + bankID);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Displays accounts based on the specified type.
     * @param accountType Type of account to display.
     */
    public void showAccounts(String accountType) {
        String query = "SELECT * FROM Account WHERE BankID = ? AND AccountType = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, this.BankID);
            pstmt.setString(2, accountType);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("Account Number: " + rs.getString("AccountID") + ", Balance: " + rs.getDouble("Balance"));
            }
        } catch (SQLException e) {
            System.out.println("Error displaying accounts: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Bank{" +
                "ID=" + BankID +
                ", name='" + name + '\'' +
                ", passcode='" + passcode + '\'' +
                ", DepositLimit=" + DepositLimit +
                ", WithdrawLimit=" + WithdrawLimit +
                ", CreditLimit=" + CreditLimit +
                ", processingFee=" + processingFee +
                '}';
    }
}
