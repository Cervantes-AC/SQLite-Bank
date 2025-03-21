//package Accounts;
//
//import Bank.Bank;
//import Transactions.*;
//import java.sql.*;
//
///**
// * SavingsAccount Class
// * Represents a savings account supporting withdrawals, deposits, and transfers.
// */
//public class SavingsAccount extends Account implements Withdrawal, Deposit, FundTransfer {
//    private double balance;
//    private static final String DB_URL = "jdbc:sqlite:Database/Database.db";
//
//    public SavingsAccount(Bank bank, String accountNumber, String ownerFirstName,
//                          String ownerLastName, String ownerEmail, String pin, double balance) {
//        super(bank.getBankID(), accountNumber, ownerFirstName, ownerLastName, ownerEmail, pin);
//        this.balance = balance;
//    }
//
//    public double getBalance() {
//        return balance;
//    }
//
//    public void setBalance(double balance) {
//        this.balance = Math.max(0, balance);
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
//    private void adjustAccountBalance(double amount) {
//        setBalance(balance + amount);
//        updateBalanceInDB();
//    }
//
//    private void updateBalanceInDB() {
//        try (Connection conn = DriverManager.getConnection(DB_URL);
//             PreparedStatement pstmt = conn.prepareStatement(
//                     "UPDATE SavingsAccount SET Balance = ? WHERE AccountID = ?")) {
//            pstmt.setDouble(1, balance);
//            pstmt.setString(2, getAccountID());
//            pstmt.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public String toString() {
//        return "SavingsAccount{balance=" + balance + "}";
//    }
//
//    @Override
//    public boolean cashDeposit(double amount) {
//        if (amount <= 0) return false;
//        adjustAccountBalance(amount);
//        addNewTransaction("Deposit", amount, "Cash deposit");
//        return true;
//    }
//
//    @Override
//    public boolean transfer(Bank bank, Account account, double amount) throws IllegalAccountType {
//        if (account instanceof CreditAccount) throw new IllegalAccountType("Cannot transfer to a CreditAccount");
//        if (!hasEnoughBalance(amount)) return false;
//
//        adjustAccountBalance(-amount);
//        addNewTransaction("Transfer", amount, "Transfer to account: " + account.getAccountID());
//        return true;
//    }
//
//    @Override
//    public boolean transfer(Account account, double amount) throws IllegalAccountType {
//        return true;
//    }
//
//    @Override
//    public boolean withdrawal(double amount) {
//        if (!hasEnoughBalance(amount)) return false;
//
//        adjustAccountBalance(-amount);
//        addNewTransaction("Withdrawal", amount, "Cash withdrawal");
//        return true;
//    }
//}
