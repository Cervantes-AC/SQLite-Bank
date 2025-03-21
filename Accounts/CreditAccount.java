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
// */
//public class CreditAccount extends Account implements Payment, Recompense {
//    private double loan;
//    private static final String DB_URL = "jdbc:sqlite:Database/Database.db";
//
//    public CreditAccount(int bankID, String firstName, String lastName, String email, String pin, double loan) {
//        super(bankID, "Credit", firstName, lastName, email, pin);
//        this.loan = Math.max(0, loan); // Ensure loan is non-negative
//    }
//
//    public double getLoan() {
//        return loan;
//    }
//
//    public void setLoan(double loan) {
//        this.loan = Math.max(0, loan); // Prevent negative loan values
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
//    public boolean pay(Account account, double amount) throws IllegalAccountType {
//        if (account instanceof CreditAccount) {
//            throw new IllegalAccountType("Cannot pay into another CreditAccount.");
//        }
//        if (!canCredit(amount)) {
//            System.out.println("Insufficient loan balance.");
//            return false;
//        }
//
//        adjustLoanAmount(-amount);
//        addNewTransaction("Payment", amount, "Payment to " + account.getFullName());
//
//        return updateLoanInDatabase();
//    }
//
//    @Override
//    public boolean recompense(double amount) {
//        if (amount > loan) {
//            System.out.println("Recompense amount exceeds loan balance.");
//            return false;
//        }
//
//        adjustLoanAmount(-amount);
//        addNewTransaction("Recompense", amount, "Loan recompense");
//
//        return updateLoanInDatabase();
//    }
//
//    private boolean updateLoanInDatabase() {
//        String updateLoanSQL = "UPDATE CreditAccount SET Loan = ? WHERE AccountID = ?";
//
//        try (Connection conn = DriverManager.getConnection(DB_URL);
//             PreparedStatement pstmt = conn.prepareStatement(updateLoanSQL)) {
//
//            pstmt.setDouble(1, loan);
//            pstmt.setString(2, getAccountID());
//            pstmt.executeUpdate();
//            return true;
//        } catch (SQLException e) {
//            System.out.println("Error updating loan balance: " + e.getMessage());
//            return false;
//        }
//    }
//
//    @Override
//    public String toString() {
//        return "CreditAccount{" +
//                "loan=" + loan +
//                ", accountID='" + getAccountID() + '\'' +
//                ", fullName='" + getFullName() + '\'' +
//                '}';
//    }
//}
