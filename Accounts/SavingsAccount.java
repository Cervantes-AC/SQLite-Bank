package Accounts;

import Bank.Bank;
import Transactions.*;
import java.sql.*;
import java.util.Scanner;

/**
 * SavingsAccount Class
 * Represents a savings account supporting withdrawals, deposits, and transfers.
 */
public class SavingsAccount extends Account implements Withdrawal, Deposit, FundTransfer {
    private double balance;
    private static final String DB_URL = "jdbc:sqlite:Database/Database.db";
    private static final Scanner input = new Scanner(System.in);

    public SavingsAccount(int bankID, String firstName, String lastName, String email, String pin, double initialBalance) {
        super(bankID, "Savings", firstName, lastName, email, pin);
        this.balance = initialBalance;
    }

    /**
     * Creates a new SavingsAccount with an initial deposit and inserts it into the database.
     */
    public static SavingsAccount createSavingsAccount(int bankID, String firstName, String lastName, String email, String pin) {
        System.out.print("Enter initial deposit amount: ");
        double initialBalance;

        // Handle invalid deposit input gracefully
        try {
            initialBalance = Double.parseDouble(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid deposit amount. Defaulting to 0.");
            initialBalance = 0;
        }

        // Create a new SavingsAccount object
        SavingsAccount newAccount = new SavingsAccount(bankID, firstName, lastName, email, pin, initialBalance);

        // Try inserting the account into the database
        return newAccount.insertAccount("SavingsAccount", "Balance", initialBalance) ? newAccount : null;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = Math.max(0, balance); // Prevent negative balance
    }

    public String getAccountBalanceStatement() {
        return "Current balance: " + balance;
    }

    /**
     * Checks if the account has enough balance for a transaction.
     */
    private boolean hasEnoughBalance(double amount) {
        return balance >= amount;
    }

    /**
     * Adjusts the account balance after a transaction.
     */
    private void adjustAccountBalance(double amount) {
        setBalance(balance + amount);
    }

    // Placeholder transaction methods for later implementation
    @Override
    public boolean cashDeposit(double amount) {
        return false;
    }

    @Override
    public boolean transfer(Bank bank, Account account, double amount) throws IllegalAccountType {
        return false;
    }

    @Override
    public boolean transfer(Account account, double amount) throws IllegalAccountType {
        return false;
    }

    @Override
    public boolean withdrawal(double amount) {
        return false;
    }
}
