//package Accounts;
//
//import Bank.Bank;
//import Transactions.*;
//import java.sql.*;
//
///**
// * SavingsAccount Class
// * Represents a savings account that supports basic banking operations.
// * Implements Withdrawal, Deposit, and FundTransfer interfaces.
// *
// * Attributes:
// * - double balance: The current balance in this savings account.
// *
// * Methods:
// * - getBalance(): Returns the current account balance.
// * - setBalance(double balance): Sets a new balance for the account.
// * - getAccountBalanceStatement(): Returns a string representing the account balance statement.
// * - hasEnoughBalance(double amount): Checks if the account has enough balance for a transaction.
// * - insufficientBalance(double amount): Deducts the amount from balance. If balance goes negative, it's reset to 0.
// * - adjustAccountBalance(double amount): Adjusts the balance by adding or subtracting the specified amount.
// * - cashDeposit(double amount): Deposits money into the account, respecting bank limits.
// * - transfer(Bank bank, Account account, double amount): Transfers money to an account in another bank.
// * - transfer(Account account, double amount): Transfers money to another savings account within the same bank.
// * - withdrawal(double amount): Withdraws money from the account, only if balance is sufficient.
// */
//
//public class SavingsAccount extends Account implements Withdrawal, Deposit, FundTransfer {
//    private double balance;
//    private static final String DB_URL = "jdbc:sqlite:Database/Database.db";
//
//    public SavingsAccount(Bank bank, String accountNumber, String ownerFirstName,
//                          String ownerLastName, String ownerEmail, String pin, double balance) {
//        super(bank, accountNumber, ownerFirstName, ownerLastName, ownerEmail, pin);
//        this.balance = balance;
//    }
//
//    public double getBalance() {
//        return balance;
//    }
//
//    public void setBalance(double balance) {
//        this.balance = balance;
//    }
//
//    public String getAccountBalanceStatement() {
//        return "Current balance: " + balance;
//    }
//
//    private boolean hasEnoughBalance(double amount) {
//        return balance >= amount;
//    }
//
//    private void insufficientBalance(double amount) {
//        balance = Math.max(0, balance - amount);
//    }
//
//    private void adjustAccountBalance(double amount) {
//        balance += amount;
//    }
//
//    @Override
//    public String toString() {
//        return "SavingsAccount{" +
//                "balance=" + balance +
//                '}';
//    }
//
//    @Override
//    public boolean cashDeposit(double amount) {
//        if (amount <= 0) return false;
//        adjustAccountBalance(amount);
//        addNewTransaction("Deposit", amount, "Cash deposit");
//
//        try (Connection conn = DriverManager.getConnection(DB_URL)) {
//            String updateBalance = "UPDATE SavingsAccount SET Balance = ? WHERE AccountID = ?";
//            try (PreparedStatement pstmt = conn.prepareStatement(updateBalance)) {
//                pstmt.setDouble(1, balance);
//                pstmt.setString(2, getAccountNumber());
//                pstmt.executeUpdate();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//
//        return true;
//    }
//
//    @Override
//    public boolean transfer(Bank bank, Account account, double amount) throws IllegalAccountType {
//        if (account instanceof CreditAccount) {
//            throw new IllegalAccountType("Cannot transfer to a CreditAccount");
//        }
//        if (hasEnoughBalance(amount)) {
//            insufficientBalance(amount);
//            addNewTransaction("Transfer", amount, "Transfer to account: " + account.getAccountNumber());
//
//            try (Connection conn = DriverManager.getConnection(DB_URL)) {
//                String updateBalance = "UPDATE SavingsAccount SET Balance = ? WHERE AccountID = ?";
//                try (PreparedStatement pstmt = conn.prepareStatement(updateBalance)) {
//                    pstmt.setDouble(1, balance);
//                    pstmt.setString(2, getAccountNumber());
//                    pstmt.executeUpdate();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//                return false;
//            }
//
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean transfer(Account account, double amount) throws IllegalAccountType {
//        return transfer(this.getBank(), account, amount);
//    }
//
//    @Override
//    public boolean withdrawal(double amount) {
//        if (hasEnoughBalance(amount)) {
//            insufficientBalance(amount);
//            addNewTransaction("Withdrawal", amount, "Cash withdrawal");
//
//            try (Connection conn = DriverManager.getConnection(DB_URL)) {
//                String updateBalance = "UPDATE SavingsAccount SET Balance = ? WHERE AccountID = ?";
//                try (PreparedStatement pstmt = conn.prepareStatement(updateBalance)) {
//                    pstmt.setDouble(1, balance);
//                    pstmt.setString(2, getAccountNumber());
//                    pstmt.executeUpdate();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//                return false;
//            }
//
//            return true;
//        }
//        return false;
//    }
//}
