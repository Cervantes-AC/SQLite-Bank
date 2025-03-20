//package Accounts;
//import Bank.Bank;
//import Transactions.*;
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
//
//    public CreditAccount(Bank Bank, String AccountNumber, String OwnerFirstName,
//                         String OwnerLastName, String OwnerEmail, String pin, double loan) {
//        super(Bank, AccountNumber, OwnerFirstName, OwnerLastName, OwnerEmail, pin);
//        this.loan = loan;
//    }
//
//    /**
//     * Gets the current loan amount.
//     * @return Current loan amount.
//     */
//    public double getLoan() {
//        return loan;
//    }
//
//    /**
//     * Sets a new loan amount.
//     * @param loan The new loan amount to set.
//     */
//    public void setLoan(double loan) {
//        this.loan = loan;
//    }
//
//    /**
//     * Returns the loan statement for this account.
//     * @return String representation of the loan statement.
//     */
//    public String getLoanStatement() {
//        return "";
//    }
//
//    /**
//     * Checks if the account can handle a credit transaction without exceeding the loan limit.
//     * @param amountAdjustment The amount to adjust.
//     * @return True if the account can handle the adjustment, false otherwise.
//     */
//    private boolean canCredit(double amountAdjustment) {
//        return amountAdjustment <= loan;
//    }
//
//    /**
//     * Adjusts the loan amount by a specified value. The loan cannot fall below zero.
//     * @param amountAdjustment Amount to adjust the loan by.
//     */
//    private void adjustLoanAmount(double amountAdjustment) {
//        loan += amountAdjustment;
//    }
//
//    /**
//     * Returns a string representation of this credit account.
//     * @return String representation of the credit account object.
//     */
//    @Override
//    public String toString() {
//        return "CreditAccount{" +
//                "loan=" + loan +
//                '}';
//    }
//
//    /**
//     * Pays an amount to a specified target account. Target account cannot be another CreditAccount.
//     * @param account The target account to receive the payment.
//     * @param amount The amount to pay.
//     * @return True if the payment is successful, false otherwise.
//     * @throws IllegalAccountType Thrown when trying to pay into another CreditAccount.
//     */
//    @Override
//    public boolean pay(Account account, double amount) throws IllegalAccountType {
//        return false;
//    }
//
//    /**
//     * Recompenses a specified amount of money to the bank, reducing the loan amount.
//     * The recompense amount cannot exceed the current loan.
//     * @param amount Amount to recompense.
//     * @return True if the recompense was successful, false otherwise.
//     */
//    @Override
//    public boolean recompense(double amount) {
//        return false;
//    }
//}
