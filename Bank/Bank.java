package Bank;

import Accounts.*;

import java.sql.*;

/**
 * Bank Class
 * Represents a bank that manages accounts and enforces transaction limits.
 * Now integrates with SQLite for persistent data storage.
 */
public class Bank {
    public int getBankID;
    private int AccountID;
    private String name, passcode;
    private double DepositLimit, WithdrawLimit, CreditLimit;
    private double processingFee;

    private static final String DB_URL = "jdbc:sqlite:Database/Database.db";

    public Bank(int AccountID, String name, String passcode) {
        this.AccountID = AccountID;
        this.name = name;
        this.passcode = passcode;
    }

    public Bank(int AccountID, String name, String passcode, double DepositLimit, double WithdrawLimit, double CreditLimit, double processingFee) {
        this(AccountID, name, passcode);
        this.DepositLimit = DepositLimit;
        this.WithdrawLimit = WithdrawLimit;
        this.CreditLimit = CreditLimit;
        this.processingFee = processingFee;
    }

    /** Getters and Setters **/
    public int getGetBankID() {
        return getBankID;
    }

    public void setGetBankID(int getBankID) {
        this.getBankID = getBankID;
    }

    public int getAccountID() {
        return AccountID;
    }

    public void setAccountID(int accountID) {
        this.AccountID = accountID;
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
     * Saves the bank to the database.
     */
    public void InsertBank() {
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

            pstmt.setInt(1, this.AccountID);
            pstmt.setString(2, accountType);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("Account Number: " + rs.getString("AccountID") + ", Balance: " + rs.getDouble("Balance"));
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
        String sql = "SELECT * FROM Account WHERE BankID = ? AND AccountID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, this.AccountID);
            pstmt.setString(2, accountNum);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Account(
                        rs.getInt("BankID"),
                        rs.getString("AccountType"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Email"),
                        rs.getString("PIN")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving account: " + e.getMessage());
        }
        return null;
    }

    /**
     * Checks if an account number already exists in the bank.
     */
    private boolean accountExists(String accountNum) {
        String sql = "SELECT AccountID FROM Account WHERE BankID = ? AND AccountID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, this.AccountID);
            pstmt.setString(2, accountNum);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error checking account existence: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String toString() {
        return "Bank{" +
                "ID=" + AccountID +
                ", name='" + name + '\'' +
                ", passcode='" + passcode + '\'' +
                ", DepositLimit=" + DepositLimit +
                ", WithdrawLimit=" + WithdrawLimit +
                ", CreditLimit=" + CreditLimit +
                ", processingFee=" + processingFee +
                '}';
    }
}