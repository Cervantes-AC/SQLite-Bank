package Accounts;

import Bank.Bank;
import Transactions.*;
import java.sql.*;

/**
 * SavingsAccount Class
 * Represents a savings account supporting withdrawals, deposits, and transfers.
 */
public class SavingsAccount extends Account implements Withdrawal, Deposit, FundTransfer {
    private double balance;
    private double withdrawLimit, depositLimit;
    private static final String DB_URL = "jdbc:sqlite:Database/Database.db";

    // Constructor for new account creation
    public SavingsAccount(Bank bank, String firstName, String lastName, String email, String pin, double balance) {
        super(bank.getBankID(), "Savings", firstName, lastName, email, pin);
        this.balance = Math.max(0, balance);
        this.withdrawLimit = bank.getWithdrawLimit();
        this.depositLimit = bank.getDepositLimit();
    }

    // Constructor for retrieving an existing account from the database
    public SavingsAccount(String accountID) {
        super(getBankIDFromDatabase(accountID), "Savings", "", "", "", "");
        this.setAccountID(accountID);
        loadSavingsAccountDetails(accountID);
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = Math.max(0, balance);
        updateBalanceInDB();
    }

    public String getAccountBalanceStatement() {
        return "Current balance: $" + balance;
    }

    private boolean hasEnoughBalance(double amount) {
        return balance >= amount;
    }

    private void adjustAccountBalance(double amount) {
        balance = Math.max(0, balance + amount);
        updateBalanceInDB();
    }

    private void updateBalanceInDB() {
        String sql = "UPDATE SavingsAccount SET Balance = ? WHERE AccountID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false);  // Disable auto-commit for transaction safety

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setDouble(1, balance);
                pstmt.setString(2, getAccountID());

                int rowsUpdated = pstmt.executeUpdate();
                if (rowsUpdated > 0) {
                    conn.commit();  // Explicitly commit changes
                } else {
                    System.out.println("Database update failed. Rolling back changes...");
                    conn.rollback();  // Rollback if update failed
                }
            }

        } catch (SQLException e) {
            System.out.println("Error updating balance in DB: " + e.getMessage());
        }
    }

    private void loadSavingsAccountDetails(String accountID) {
        String sql = "SELECT * FROM SavingsAccount WHERE AccountID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, accountID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                this.setFirstName(rs.getString("FirstName"));
                this.setLastName(rs.getString("LastName"));
                this.setEmail(rs.getString("Email"));
                this.balance = rs.getDouble("Balance");
                this.setBankID(rs.getInt("BankID"));
                Bank bank = new Bank(this.getBankID(), "", "", 0, 0, 0, 0);
                this.withdrawLimit = bank.getWithdrawLimit();
                this.depositLimit = bank.getDepositLimit();
            } else {
                System.out.println("Account not found: " + accountID);
            }
        } catch (SQLException e) {
            System.out.println("Error loading account details: " + e.getMessage());
        }
    }

    private static int getBankIDFromDatabase(String accountID) {
        String sql = "SELECT BankID FROM SavingsAccount WHERE AccountID = ?";
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

    @Override
    public String toString() {
        return "SavingsAccount{" +
                "balance=" + balance +
                ", accountID='" + getAccountID() + '\'' +
                ", fullName='" + getOwnerFullName() + '\'' +
                '}';
    }

    /** Enforced Deposit with Bank Limit **/
    @Override
    public boolean cashDeposit(double amount) {
        if (amount <= 0) {
            System.out.println("Deposit failed: Amount must be positive.");
            return false;
        }
        if (amount > depositLimit) {
            System.out.println("Deposit failed: Amount exceeds bank deposit limit of $");
            return false;
        }

        System.out.println("Depositing $" + amount + " to Account ID: " + getAccountID());
        adjustAccountBalance(amount);
        addNewTransaction("Deposit", amount, "Cash deposit");

        System.out.println("Deposit successful! Updated balance: $" + balance);
        return true;
    }

    /** Enforced Withdrawal with Bank Limit **/
    @Override
    public boolean withdrawal(double amount) {
        if (!hasEnoughBalance(amount)) {
            System.out.println("Withdrawal failed: Insufficient balance.");
            return false;
        }
        if (amount > withdrawLimit) {
            System.out.println("Withdrawal failed: Amount exceeds bank withdrawal limit of $");
            return false;
        }

        System.out.println("Withdrawing $" + amount + " from Account ID: " + getAccountID());
        adjustAccountBalance(-amount);
        addNewTransaction("Withdrawal", amount, "Cash withdrawal");

        System.out.println("Withdrawal successful! Updated balance: $" + balance);
        return true;
    }

    /** Fund Transfer **/
    @Override
    public boolean transfer(Bank bank, Account account, double amount) throws IllegalAccountType {
        return transfer(account, amount);
    }

    @Override
    public boolean transfer(Account account, double amount) throws IllegalAccountType {
        if (!(account instanceof SavingsAccount)) {
            throw new IllegalAccountType("Cannot transfer funds to a CreditAccount.");
        }
        if (!hasEnoughBalance(amount)) {
            System.out.println("Transfer failed: Insufficient balance.");
            return false;
        }

        SavingsAccount recipient = new SavingsAccount(account.getAccountID());
        if (recipient.getFirstName().isEmpty()) {  // FirstName being empty means account wasn't found
            System.out.println("Transfer failed: Recipient account not found.");
            return false;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false); // Start transaction

            // Deduct from sender
            this.balance -= amount;
            try (PreparedStatement deductStmt = conn.prepareStatement(
                    "UPDATE SavingsAccount SET Balance = ? WHERE AccountID = ?")) {
                deductStmt.setDouble(1, this.balance);
                deductStmt.setString(2, this.getAccountID());
                deductStmt.executeUpdate();
            }

            // Add to recipient
            recipient.balance += amount;
            try (PreparedStatement addStmt = conn.prepareStatement(
                    "UPDATE SavingsAccount SET Balance = ? WHERE AccountID = ?")) {
                addStmt.setDouble(1, recipient.balance);
                addStmt.setString(2, recipient.getAccountID());
                addStmt.executeUpdate();
            }

            conn.commit(); // Commit transaction
            System.out.println("Transfer successful! Updated balance: $" + this.balance);
            addNewTransaction("Fund Transfer", amount, "Transfer to " + recipient.getAccountID());

            return true;
        } catch (SQLException e) {
            System.out.println("Transfer failed due to database error: " + e.getMessage());
            return false;
        }
    }
}
