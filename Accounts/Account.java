package Accounts;

import Bank.*;
import Transactions.*;
import java.util.ArrayList;

/**
 * Account Class
 * An abstract class representing a bank account.
 * Provides basic functionality and tracks account information and transactions.
 *
 * Attributes:
 * - Bank bank: The associated bank object.
 * - String accountNumber: The unique account number.
 * - String ownerFirstName, ownerLastName, ownerEmail: Account owner's personal information.
 * - String pin: The security PIN for this account.
 * - ArrayList<Transaction> transactions: A log of all account transactions.
 *
 * Methods:
 * - getFullName(): Returns the full name of the account owner.
 * - addNewTransaction(): Logs a new transaction.
 * - getTransactionsInfo(): Returns a formatted string of all transaction details.
 * - Various getters and setters for account data.
 */
public abstract class Account {
    private Bank bank;
    private String accountNumber;
    private String ownerFirstName, ownerLastName, ownerEmail;
    private String pin;
    private ArrayList<Transaction> transactions;

    /**
     * Constructor to initialize an Account object.
     */
    public Account(Bank bank, String accountNumber, String ownerFirstName, String ownerLastName, String ownerEmail, String pin) {
        this.bank = bank;
        this.accountNumber = accountNumber;
        this.ownerFirstName = ownerFirstName;
        this.ownerLastName = ownerLastName;
        this.ownerEmail = ownerEmail;
        this.pin = pin;
        this.transactions = new ArrayList<>();
    }

    // Getters and Setters
    public Bank getBank() { return bank; }
    public void setBank(Bank bank) { this.bank = bank; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getOwnerFirstName() { return ownerFirstName; }
    public void setOwnerFirstName(String ownerFirstName) { this.ownerFirstName = ownerFirstName; }

    public String getOwnerLastName() { return ownerLastName; }
    public void setOwnerLastName(String ownerLastName) { this.ownerLastName = ownerLastName; }

    public String getOwnerEmail() { return ownerEmail; }
    public void setOwnerEmail(String ownerEmail) { this.ownerEmail = ownerEmail; }

    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }

    public ArrayList<Transaction> getTransactions() { return transactions; }
    public void setTransactions(ArrayList<Transaction> transactions) { this.transactions = transactions; }

    /**
     * Returns the full name of the account owner.
     *
     * @return The full name as "FirstName LastName".
     */
    public String getFullName() {
        return ownerFirstName + " " + ownerLastName;
    }

    /**
     * Logs a new transaction for this account.
     *
     * @param accountNumber The account number involved.
     * @param type The type of transaction (e.g., withdrawal, deposit).
     * @param description A brief description of the transaction.
     */
    public void addNewTransaction(String accountNumber, Transaction type, String description) {
        // TODO: Complete this method
    }

    /**
     * Retrieves all logged transactions as a formatted string.
     *
     * @return A string containing all transaction details.
     */
    public String getTransactionsInfo() {
        StringBuilder info = new StringBuilder();
        for (Transaction t : transactions) {
            info.append(t.toString()).append("\n");
        }
        return info.toString();
    }

    /**
     * Returns a string representation of the account object.
     *
     * @return A string containing account details.
     */
    @Override
    public String toString() {
        return "Account{" +
                "Bank=" + bank +
                ", AccountNumber='" + accountNumber + '\'' +
                ", OwnerFirstName='" + ownerFirstName + '\'' +
                ", OwnerLastName='" + ownerLastName + '\'' +
                ", OwnerEmail='" + ownerEmail + '\'' +
                ", pin='" + pin + '\'' +
                ", Transactions=" + transactions +
                '}';
    }
}
