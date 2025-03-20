package Bank;

import Accounts.Account;
import java.sql.*;
import java.util.Scanner;

public class Bank {
    private int bankID;
    private String name, passcode;
    private double depositLimit = 50000.0, withdrawLimit = 50000.0, creditLimit = 100000.0;
    private double processingFee = 10.0;

    private static final String DB_URL = "jdbc:sqlite:Database/Database.db";

    // Constructor with default limits
    public Bank(String name, String passcode) {
        this.name = name;
        this.passcode = passcode;
    }

    // Display bank information
    @Override
    public String toString() {
        return String.format("""
                ------------------------
                Bank ID: %d
                Name: %s
                Passcode: %s
                Deposit Limit: %.2f
                Withdraw Limit: %.2f
                Credit Limit: %.2f
                Processing Fee: %.2f
                ------------------------
                """,
                bankID, name, passcode, depositLimit, withdrawLimit, creditLimit, processingFee);
    }

    // Insert Bank into SQLite database
    public boolean insertBank() {
        String sql = """
            INSERT INTO Bank (name, passcode, DepositLimit, WithdrawLimit, CreditLimit, processingFee) 
            VALUES (?, ?, ?, ?, ?, ?)""";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, this.name);
            pstmt.setString(2, this.passcode);
            pstmt.setDouble(3, this.depositLimit);
            pstmt.setDouble(4, this.withdrawLimit);
            pstmt.setDouble(5, this.creditLimit);
            pstmt.setDouble(6, this.processingFee);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Bank added successfully!");
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    this.bankID = rs.getInt(1);
                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println("SQLite error: " + e.getMessage());
        }

        return false;
    }

    // Getters and Setters
    public int getBankID() { return bankID; }
    public String getName() { return name; }
    public String getPasscode() { return passcode; }
    public double getDepositLimit() { return depositLimit; }
    public double getWithdrawLimit() { return withdrawLimit; }
    public double getCreditLimit() { return creditLimit; }
    public double getProcessingFee() { return processingFee; }

    public void setBankID(int bankID) { this.bankID = bankID; }
    public void setName(String name) { this.name = name; }
    public void setPasscode(String passcode) { this.passcode = passcode; }
    public void setDepositLimit(double depositLimit) { this.depositLimit = depositLimit; }
    public void setWithdrawLimit(double withdrawLimit) { this.withdrawLimit = withdrawLimit; }
    public void setCreditLimit(double creditLimit) { this.creditLimit = creditLimit; }
    public void setProcessingFee(double processingFee) { this.processingFee = processingFee; }

    // Retrieve an account by account number and bank ID
    public static Account getBankAccount() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Bank ID: ");
        int bankID = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Account ID: ");
        String accountID = scanner.nextLine();

        String query = "SELECT * FROM Account WHERE AccountID = ? AND BankID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, accountID);
            pstmt.setInt(2, bankID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("Account Found!");
                return new Account(
                        rs.getInt("BankID"),
                        rs.getString("AccountType"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Email"),
                        rs.getString("PIN")
                );
            } else {
                System.out.println("No account found with the given details.");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving account: " + e.getMessage());
        }
        return null;
    }

    // Check if an account exists
    public static boolean accountExists(Bank bank, String accountNum) {
        String query = "SELECT AccountID FROM Account WHERE AccountID = ? AND BankID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, accountNum);
            pstmt.setInt(2, bank.getBankID());
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error checking account: " + e.getMessage());
        }
        return false;
    }
}