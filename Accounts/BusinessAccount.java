package Accounts;

import Transactions.*;
import java.sql.*;

/**
 * BusinessAccount Class
 * Represents a credit account that allows transactions based on a loan amount.
 * Implements Payment and Recompense interfaces.
 */
public class BusinessAccount extends Account implements Payment, Recompense {
    private double loan;
    private double creditLimit;
    private static final String DB_URL = "jdbc:sqlite:Database/Database.db";

    // Constructor for a new Credit Account
    public BusinessAccount(int bankID, String firstName, String lastName, String email, String pin, double loan) {
        super(bankID, "Business", firstName, lastName, email, pin);
        this.loan = Math.max(0, loan); // Ensure loan is non-negative
        this.creditLimit = getCreditLimitFromDatabase(bankID); // Fetch credit limit
    }

    // Constructor for retrieving an existing Credit Account from the database
    public BusinessAccount(String accountID) {
        super(getBankIDFromDatabase(accountID), "Business", "", "", "", "");
        this.setAccountID(accountID);
        loadBusinessAccountDetails(accountID);

        //Fetch credit limit from database
        this.creditLimit = getCreditLimitFromDatabase(getBankID());
    }

    public double getLoan() {
        return loan;
    }

    public void setLoan(double loan) {
        this.loan = Math.max(0, loan); // Prevent negative loan values
        updateLoanInDatabase();
    }

    public String getLoanStatement() {
        return "Loan balance: $" + loan;
    }

    private boolean canCredit(double amount) {
        return (loan + amount) <= creditLimit;
    }

    private void adjustLoanAmount(double amount) {
        loan = Math.max(0, loan + amount);
        updateLoanInDatabase();
    }

    @Override
    public boolean pay(Account account, double amount) throws IllegalAccountType {
        if (amount <= 0) {
            System.out.println("Invalid payment amount.");
            return false;
        }

        if (loan == 0) {
            System.out.println("No loan balance to pay.");
            return false;
        }

        if (amount > loan) {
            System.out.println("Payment exceeds loan balance. Adjusting to full repayment.");
            amount = loan;
        }

        adjustLoanAmount(-amount); // Reduce loan
        addNewTransaction("Payment", amount, "Loan repayment");
        return true;
    }

    @Override
    public boolean recompense(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid recompense amount.");
            return false;
        }

        if (!canCredit(amount)) {
            System.out.println("Recompense failed: Exceeds credit limit of $" + creditLimit);
            return false;
        }

        System.out.println("Taking an additional loan of: $" + amount);
        adjustLoanAmount(amount);
        addNewTransaction("Recompense", amount, "Loan recompense");
        return true;
    }

    private boolean updateLoanInDatabase() {
        String sql = "UPDATE BusinessAccount SET Loan = ? WHERE AccountID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, loan);
            pstmt.setString(2, getAccountID());
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Error updating loan balance: " + e.getMessage());
            return false;
        }
    }

    private void loadBusinessAccountDetails(String accountID) {
        String sql = "SELECT * FROM BusinessAccount WHERE AccountID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, accountID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                this.setFirstName(rs.getString("FirstName"));
                this.setLastName(rs.getString("LastName"));
                this.setEmail(rs.getString("Email"));
                this.setPin(rs.getString("PIN"));
                this.loan = rs.getDouble("Loan");
            } else {
                System.out.println("Account not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error loading BusinessAccount details: " + e.getMessage());
        }
    }

    private static int getBankIDFromDatabase(String accountID) {
        String sql = "SELECT BankID FROM BusinessAccount WHERE AccountID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, accountID);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getInt("BankID") : -1;
        } catch (SQLException e) {
            System.out.println("Error fetching BankID: " + e.getMessage());
            return -1;
        }
    }

    // Fetch credit limit from the database
    private static double getCreditLimitFromDatabase(int bankID) {
        String sql = "SELECT CreditLimit FROM Bank WHERE BankID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bankID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("CreditLimit");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching credit limit: " + e.getMessage());
        }
        return 0; // Default if bank is not found
    }

    @Override
    public String toString() {
        return "BusinessAccount{" +
                "loan=" + loan +
                ", accountID='" + getAccountID() + '\'' +
                ", fullName='" + getOwnerFullName() + '\'' +
                '}';
    }
}
