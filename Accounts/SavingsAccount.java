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
    private double withdrawLimit, depositLimit, processingfee;
    private static final String DB_URL = "jdbc:sqlite:Database/Database.db";

    // Constructor for new account creation
    public SavingsAccount(Bank bank, String firstName, String lastName, String email, String pin, double balance) {
        super(bank.getBankID(), "Savings", firstName, lastName, email, pin);
        this.balance = Math.max(0, balance);
        this.withdrawLimit = bank.getWithdrawLimit();
        this.depositLimit = bank.getDepositLimit();
        this.processingfee = bank.getProcessingFee();
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

    public void loadSavingsAccountDetails(String accountID) {
        String sql = "SELECT s.*, b.DepositLimit, b.WithdrawLimit, b.processingFee " +
                "FROM SavingsAccount s " +
                "JOIN Bank b ON s.BankID = b.BankID " +
                "WHERE s.AccountID = ?";
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
                this.processingfee = rs.getDouble("processingFee");
            } else {
                System.out.println("Account " + accountID + " not found.");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
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
        if (amount <= 0){
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

    @Override
    public boolean transfer(Account account, double amount) throws IllegalAccountType {
        // Allow transfer between EducationalAccount and SavingsAccount
        if (!(account instanceof EducationalAccount || account instanceof SavingsAccount)) {
            throw new IllegalAccountType("Cannot transfer funds to a non-Savings or non-Educational account.");
        }

        if (amount <= 0) {
            System.out.println("Transfer failed: Amount must be positive.");
            return false;
        }

        // Ensure sender has enough balance to cover the transfer
        if (amount > this.getBalance()) {
            System.out.println("Transfer failed: Insufficient balance.");
            return false;
        }

        // Apply processing fee if the banks are different
        double totalAmountToDeduct = amount;
        if (this.getBankID() != account.getBankID()) {
            totalAmountToDeduct += processingfee;  // Add processing fee for inter-bank transfer
        }

        if (totalAmountToDeduct > this.getBalance()) {
            System.out.println("Transfer failed: Insufficient funds to cover the transfer and processing fee.");
            return false;
        }

        // Proceed with the transfer if validation passes
        if (account instanceof SavingsAccount) {
            SavingsAccount recipient = (SavingsAccount) account;  // Cast to SavingsAccount
            recipient.setBalance(recipient.getBalance() + amount);
        } else if (account instanceof EducationalAccount) {
            EducationalAccount recipient = (EducationalAccount) account;  // Cast to EducationalAccount
            recipient.setBalance(recipient.getBalance() + amount);
        }

        // Deduct from sender's account balance
        setBalance(this.getBalance() - totalAmountToDeduct);

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false);  // Disable auto-commit to make this a single transaction

            // Update sender's balance in the database
            String updateSenderSQL = "UPDATE " + (this instanceof SavingsAccount ? "SavingsAccount" : "EducationalAccount") + " SET Balance = ? WHERE AccountID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateSenderSQL)) {
                pstmt.setDouble(1, this.getBalance());
                pstmt.setString(2, getAccountID());
                pstmt.executeUpdate();
            }

            // Update recipient's balance in the database
            String updateRecipientSQL = "UPDATE " + (account instanceof SavingsAccount ? "SavingsAccount" : "EducationalAccount") + " SET Balance = ? WHERE AccountID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateRecipientSQL)) {
                if (account instanceof SavingsAccount) {
                    pstmt.setDouble(1, ((SavingsAccount) account).getBalance());
                } else if (account instanceof EducationalAccount) {
                    pstmt.setDouble(1, ((EducationalAccount) account).getBalance());
                }
                pstmt.setString(2, account.getAccountID());
                pstmt.executeUpdate();
            }

            conn.commit(); // Commit the transaction
            System.out.println("Transfer successful! Updated balance: $" + this.getBalance());

            // Record the transaction for both sender and recipient
            addNewTransaction("Fund Transfer", amount, "Transfer to " + account.getAccountID() +
                    (this.getBankID() != account.getBankID() ? " (Fee: $" + processingfee + ")" : ""));
            account.addNewTransaction("Fund Transfer", amount, "Received from " + this.getAccountID());

            return true;
        } catch (SQLException e) {
            System.out.println("Transfer failed due to database error: " + e.getMessage());
            return false;
        }
    }


}

