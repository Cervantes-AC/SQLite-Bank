package Bank;

import Accounts.Account;
import java.sql.*;
import java.util.Scanner;

public class Bank {
    private int ID;
    private String name, passcode;
    private double DepositLimit = 50000.0, WithdrawLimit = 50000.0, CreditLimit = 100000.0;
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
                ID, name, passcode, DepositLimit, WithdrawLimit, CreditLimit, processingFee);
    }

    // Insert Bank into SQLite database
    public boolean insertBank(Bank bank) {
        String sql = """
            INSERT INTO Bank (name, passcode, DepositLimit, WithdrawLimit, CreditLimit, processingFee) 
            VALUES (?, ?, ?, ?, ?, ?)""";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, bank.getName());
            pstmt.setString(2, bank.getPasscode());
            pstmt.setDouble(3, bank.getDepositLimit());
            pstmt.setDouble(4, bank.getWithdrawLimit());
            pstmt.setDouble(5, bank.getCreditLimit());
            pstmt.setDouble(6, bank.getProcessingFee());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Bank added successfully!");
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int newID = rs.getInt(1);
                    bank.setID(newID);
                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println("SQLite error: " + e.getMessage());
        }

        return false;
    }

    // Getters
    public int getID() { return ID; }
    public String getName() { return name; }
    public String getPasscode() { return passcode; }
    public double getDepositLimit() { return DepositLimit; }
    public double getWithdrawLimit() { return WithdrawLimit; }
    public double getCreditLimit() { return CreditLimit; }
    public double getProcessingFee() { return processingFee; }

    // Setters
    public void setID(int ID) { this.ID = ID; }
    public void setName(String name) { this.name = name; }
    public void setPasscode(String passcode) { this.passcode = passcode; }
    public void setDepositLimit(double depositLimit) { DepositLimit = depositLimit; }
    public void setWithdrawLimit(double withdrawLimit) { WithdrawLimit = withdrawLimit; }
    public void setCreditLimit(double creditLimit) { CreditLimit = creditLimit; }
    public void setProcessingFee(double processingFee) { this.processingFee = processingFee; }



//    public <T extends Account> void showAccounts(Class<T> accountType) {
//        // TODO: Complete this method
//}


    // Retrieve an account by account number after asking for bank ID
    public static Account getBankAccount() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Bank ID: ");
        int ID = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Account ID: ");
        String AccountID = scanner.nextLine();

        String query = "SELECT * FROM Account WHERE AccountID = ? AND BankID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, AccountID);
            pstmt.setInt(2, ID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("Account Found!");
                // Assuming your Account constructor accepts these parameters
                return new Account(
                        rs.getInt("ID"),
                        rs.getString("Type"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Email"),
                        rs.getString("Pin")
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
        // TODO: Complete this method
        String query = "SELECT AccountID FROM Account WHERE AccountID = ? AND bankID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, accountNum);
            pstmt.setInt(2, bank.getID());
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error checking account: " + e.getMessage());
        }
        return false;
    }
}
