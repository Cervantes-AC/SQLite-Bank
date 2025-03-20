package Accounts;
import Bank.Bank;
import Transactions.*;

/**
 * SavingsAccount Class
 * Represents a savings account that supports basic banking operations.
 * Implements Withdrawal, Deposit, and FundTransfer interfaces.
 *
 * Attributes:
 * - double balance: The current balance in this savings account.
 *
 * Methods:
 * - getBalance(): Returns the current account balance.
 * - setBalance(double balance): Sets a new balance for the account.
 * - getAccountBalanceStatement(): Returns a string representing the account balance statement.
 * - hasEnoughBalance(double amount): Checks if the account has enough balance for a transaction.
 * - insufficientBalance(double amount): Deducts the amount from balance. If balance goes negative, it's reset to 0.
 * - adjustAccountBalance(double amount): Adjusts the balance by adding or subtracting the specified amount.
 * - cashDeposit(double amount): Deposits money into the account, respecting bank limits.
 * - transfer(Bank bank, Account account, double amount): Transfers money to an account in another bank.
 * - transfer(Account account, double amount): Transfers money to another savings account within the same bank.
 * - withdrawal(double amount): Withdraws money from the account, only if balance is sufficient.
 */

public class SavingsAccount extends Account implements Withdrawal, Deposit, FundTransfer {
    private double balance;

    public SavingsAccount(Bank Bank, String AccountNumber, String OwnerFirstName,
                          String OwnerLastName, String OwnerEmail, String pin, double balance) {
        super(Bank, AccountNumber, OwnerFirstName, OwnerLastName, OwnerEmail, pin);
        this.balance = balance;
    }

    /**
     * Gets the current account balance.
     * @return Current account balance.
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Sets a new balance for this account.
     * @param balance The new balance to set.
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * Returns the account balance statement.
     * @return String representation of the account balance.
     */
    public String getAccountBalanceStatement() {
        return "";
    }

    /**
     * Checks whether the account has enough balance for a transaction.
     * @param amount The amount to check.
     * @return True if the account has enough balance, false otherwise.
     */
    private boolean hasEnoughBalance(double amount) {
        return balance >= amount;
    }

    /**
     * Deducts the specified amount from the account balance.
     * If the resulting balance is negative, it resets to 0.
     * @param amount The amount to deduct.
     */
    private void insufficientBalance(double amount) {
        balance = Math.max(0, balance - amount);
    }

    /**
     * Adjusts the account balance by the given amount (can be positive or negative).
     * @param amount The amount to adjust.
     */
    private void adjustAccountBalance(double amount) {
        balance += amount;
    }

    /**
     * Returns a string representation of this savings account.
     * @return String representation of the savings account object.
     */
    @Override
    public String toString() {
        return "SavingsAccount{" +
                "balance=" + balance +
                '}';
    }

    /**
     * Deposits cash into the account, respecting the bank's deposit limit.
     * @param amount The amount to deposit.
     * @return True if the deposit is successful, false otherwise.
     */
    @Override
    public boolean cashDeposit(double amount) {
        return false;
    }

    /**
     * Transfers money to an account in another bank.
     * @param bank The recipient's bank.
     * @param account The target account to transfer money to.
     * @param amount The amount to transfer.
     * @return True if the transfer is successful, false otherwise.
     * @throws IllegalAccountType If the target account is a CreditAccount.
     */
    @Override
    public boolean transfer(Bank bank, Account account, double amount) throws IllegalAccountType {
        return false;
    }

    /**
     * Transfers money to another savings account within the same bank.
     * @param account The target account to transfer money to.
     * @param amount The amount to transfer.
     * @return True if the transfer is successful, false otherwise.
     * @throws IllegalAccountType If the target account is a CreditAccount.
     */
    @Override
    public boolean transfer(Account account, double amount) throws IllegalAccountType {
        return false;
    }

    /**
     * Withdraws money from the account, only if the balance is sufficient.
     * @param amount The amount to withdraw.
     * @return True if the withdrawal is successful, false otherwise.
     */
    @Override
    public boolean withdrawal(double amount) {
        return false;
    }
}
