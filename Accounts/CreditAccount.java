//package Accounts;
//
//import Bank.Bank;
//import Transactions.*;
//import java.sql.*;
//
///**
// * CreditAccount Class
// * Represents a credit account that allows transactions based on a loan amount.
// * Implements Payment and Recompense interfaces.
// *
// * Attributes:
// * - double loan: The loan amount associated with this credit account.
// *
// * Methods:
// * - getLoan(): Returns the current loan amount.
// * - setLoan(double loan): Sets the loan amount.
// * - getLoanStatement(): Returns a string representing the loan statement.
// * - canCredit(double amountAdjustment): Checks if the credit account can handle the given credit amount without exceeding the loan limit.
// * - adjustLoanAmount(double amountAdjustment): Adjusts the loan amount. The result cannot be less than 0.
// * - pay(Account account, double amount): Pays an amount to a target account, but cannot pay to another CreditAccount.
// * - recompense(double amount): Recompenses a specified amount to the bank, reducing the loan. Cannot exceed the current loan.
// */
//
//public class CreditAccount extends Account implements Payment, Recompense {
//    private double loan;
//    private static final String DB_URL = "jdbc:sqlite:Database/Database.db";
//
//    public CreditAccount(int bankID, String type, String firstName,
//                         String lastName, String email, String pin, double loan) {
//        super(bankID, type, firstName, lastName, email, pin);
//        this.loan = loan;
//    }
//
//    public double getLoan() {
//        return loan;
//    }
//
//    public void setLoan(double loan) {
//        this.loan = loan;
//    }
//
//    public String getLoanStatement() {
//        return "Loan balance: " + loan;
//    }
//
//    private boolean canCredit(double amountAdjustment) {
//        return amountAdjustment <= loan;
//    }
//
//    private void adjustLoanAmount(double amountAdjustment) {
//        loan = Math.max(0, loan + amountAdjustment);
//    }
//
//    @Override
//    public String toString() {
//        return "CreditAccount{" +
//                "loan=" + loan +
//                '}';
//    }
//
//    @Override
//    public boolean pay(Account account, double amount) throws IllegalAccountType {
//        if (account instanceof CreditAccount) {
//            throw new IllegalAccountType("Cannot pay into another CreditAccount");
//        }
//        if (canCredit(amount)) {
//            adjustLoanAmount(-amount);
//            addNewTransaction("Payment", amount, "Payment to " + account.getFullName());
//
//            try (Connection conn = DriverManager.getConnection(DB_URL)) {
//                String updateLoan = "UPDATE CreditAccount SET Loan = ? WHERE AccountID = ?";
//                try (PreparedStatement pstmt = conn.prepareStatement(updateLoan)) {
//                    pstmt.setDouble(1, loan);
//                    pstmt.setString(2, getAccountID());
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
//    public boolean recompense(double amount) {
//        if (amount > loan) return false;
//        adjustLoanAmount(-amount);
//        addNewTransaction("Recompense", amount, "Loan recompense");
//
//        try (Connection conn = DriverManager.getConnection(DB_URL)) {
//            String updateLoan = "UPDATE CreditAccount SET Loan = ? WHERE AccountID = ?";
//            try (PreparedStatement pstmt = conn.prepareStatement(updateLoan)) {
//                pstmt.setDouble(1, loan);
//                pstmt.setString(2, getAccountID());
//                pstmt.executeUpdate();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//
//        return true;
//    }
//}