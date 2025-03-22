package Accounts;

import Bank.Bank;
import Transactions.*;
import java.sql.*;

/**
 * EducationalAccount Class
 * Represents an educational account supporting withdrawals, deposits, and transfers.
 */
public class EducationalAccount extends Account implements Withdrawal, Deposit, FundTransfer {
    private double balance;
    private double withdrawLimit, depositLimit, processingFee;
    private static final String DB_URL = "jdbc:sqlite:Database/Database.db";

    // Constructor for new account creation
    public EducationalAccount(Bank bank, String firstName, String lastName, String email, String pin, double balance) {
        super(bank.getBankID(), "Educational", firstName, lastName, email, pin);
        this.balance = Math.max(0, balance);
        this.withdrawLimit = bank.getWithdrawLimit();
        this.depositLimit = bank.getDepositLimit();
        this.processingFee = bank.getProcessingFee();
    }

    // Constructor for retrieving an existing account from the database
    public EducationalAccount(String accountID) {
        super(getBankIDFromDatabase(accountID), "Educational", "", "", "", "");
        this.setAccountID(accountID);
        loadEducationalAccountDetails(accountID);
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
        String sql = "UPDATE EducationalAccount SET Balance = ? WHERE AccountID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, balance);
            pstmt.setString(2, getAccountID());

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Balance updated successfully.");
            } else {
                System.out.println("Failed to update balance.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating balance in DB: " + e.getMessage());
        }
    }

    public void loadEducationalAccountDetails(String accountID) {
        String sql = "SELECT e.*, b.DepositLimit, b.WithdrawLimit, b.processingFee " +
                "FROM EducationalAccount e " +
                "JOIN Bank b ON e.BankID = b.BankID " +
                "WHERE e.AccountID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accountID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                this.setAccountID(rs.getString("AccountID"));
                this.setBankID(rs.getInt("BankID"));
                this.setFirstName(rs.getString("FirstName"));
                this.setLastName(rs.getString("LastName"));
                this.setEmail(rs.getString("Email"));
                this.balance = rs.getDouble("Balance");

                this.depositLimit = rs.getDouble("DepositLimit");
                this.withdrawLimit = rs.getDouble("WithdrawLimit");
                this.processingFee = rs.getDouble("processingFee");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private static int getBankIDFromDatabase(String accountID) {
        String sql = "SELECT BankID FROM EducationalAccount WHERE AccountID = ? " +
                "UNION SELECT BankID FROM SavingsAccount WHERE AccountID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accountID);
            pstmt.setString(2, accountID);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getInt("BankID") : -1;
        } catch (SQLException e) {
            System.out.println("Error fetching BankID: " + e.getMessage());
            return -1;
        }
    }

    @Override
    public String toString() {
        return "EducationalAccount{" +
                "balance=" + balance +
                ", accountID='" + getAccountID() + '\'' +
                ", fullName='" + getOwnerFullName() + '\'' +
                '}';
    }

    /** Enforced Deposit with Bank Limit **/
    public boolean cashDeposit(double amount) {
        if (amount <= 0) {
            System.out.println("Deposit failed: Amount must be positive.");
            return false;
        }

        if (amount > depositLimit) {
            System.out.println("Deposit failed: Amount exceeds bank deposit limit of $" + depositLimit);
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
        if (amount <= 0) {
            System.out.println("Withdrawal failed: Amount must be greater than zero.");
            return false;
        }

        if (!hasEnoughBalance(amount)) {
            System.out.println("Withdrawal failed: Insufficient balance.");
            return false;
        }

        if (amount > withdrawLimit) {
            System.out.println("Withdrawal failed: Amount exceeds bank withdrawal limit of $" + withdrawLimit);
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

    /** Fund Transfer Implementation **/
    @Override
    public boolean transfer(Account recipient, double amount) {
        if (amount <= 0) {
            System.out.println("Transfer failed: Amount must be positive.");
            return false;
        }

        if (!hasEnoughBalance(amount)) {
            System.out.println("Transfer failed: Insufficient balance.");
            return false;
        }

        // Ensure recipient exists in either SavingsAccount or EducationalAccount
        String recipientTable = null;
        double recipientBalance = 0;

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            // Check SavingsAccount
            String checkSavingsSQL = "SELECT Balance FROM SavingsAccount WHERE AccountID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(checkSavingsSQL)) {
                pstmt.setString(1, recipient.getAccountID());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    recipientTable = "SavingsAccount";
                    recipientBalance = rs.getDouble("Balance");
                }
            }

            // If not found in SavingsAccount, check EducationalAccount
            if (recipientTable == null) {
                String checkEduSQL = "SELECT Balance FROM EducationalAccount WHERE AccountID = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(checkEduSQL)) {
                    pstmt.setString(1, recipient.getAccountID());
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        recipientTable = "EducationalAccount";
                        recipientBalance = rs.getDouble("Balance");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return false;
        }

        // If recipient does not exist, stop transfer
        if (recipientTable == null) {
            System.out.println("Transfer failed: Recipient account ID " + recipient.getAccountID() + " not found.");
            return false;
        }

        // Check for interbank transfer and apply processing fee
        double totalAmountToDeduct = amount;
        if (this.getBankID() != recipient.getBankID()) {
            totalAmountToDeduct += processingFee;
        }

        if (!hasEnoughBalance(totalAmountToDeduct)) {
            System.out.println("Transfer failed: Insufficient funds to cover amount and processing fee.");
            return false;
        }

        // Execute the transfer
        String updateRecipientSQL = "UPDATE " + recipientTable + " SET Balance = Balance + ? WHERE AccountID = ?";
        String updateSenderSQL = "UPDATE EducationalAccount SET Balance = ? WHERE AccountID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false);

            // Deduct from sender
            try (PreparedStatement updateSenderStmt = conn.prepareStatement(updateSenderSQL)) {
                updateSenderStmt.setDouble(1, this.balance - totalAmountToDeduct);
                updateSenderStmt.setString(2, this.getAccountID());
                int senderUpdated = updateSenderStmt.executeUpdate();

                if (senderUpdated == 0) {
                    System.out.println("Transfer failed: Could not update sender balance.");
                    conn.rollback();
                    return false;
                }
            }

            // Add to recipient
            try (PreparedStatement updateRecipientStmt = conn.prepareStatement(updateRecipientSQL)) {
                updateRecipientStmt.setDouble(1, amount);
                updateRecipientStmt.setString(2, recipient.getAccountID());
                int recipientUpdated = updateRecipientStmt.executeUpdate();

                if (recipientUpdated == 0) {
                    System.out.println("Transfer failed: Could not update recipient balance.");
                    conn.rollback();
                    return false;
                }
            }

            conn.commit();
            this.balance -= totalAmountToDeduct;
            System.out.println("Transfer successful! $" + amount + " transferred to Account ID: " + recipient.getAccountID());
            return true;
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return false;
        }
    }

}
