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
    private double DepositLimit = 50000.0, WithdrawLimit = 50000.0, CreditLimit = 100000.0;
    private double processingFee = 10.0;

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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public double getDepositLimit() {
        return DepositLimit;
    }


    public double getWithdrawLimit() {
        return WithdrawLimit;
    }



    public double getProcessingFee() {
        return processingFee;
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

    @Override
    public String toString() {
        return "Bank{" +
                "ID=" + BankID +
                ", name='" + name + '\'' +
                '}';
    }
}
