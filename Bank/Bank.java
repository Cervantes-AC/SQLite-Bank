package Bank;
import Accounts.*;
import Main.Field;

import java.util.ArrayList;

/**
 * Bank Class
 * Represents a bank that manages accounts and enforces transaction limits.
 *
 * Attributes:
 * - int ID: Unique identifier for the bank.
 * - String name: The bank's name.
 * - String passcode: Security passcode for bank operations.
 * - double DepositLimit: Maximum deposit limit per transaction. Defaults to 50,000.0.
 * - double WithdrawLimit: Maximum withdrawal limit per transaction. Defaults to 50,000.0.
 * - double CreditLimit: Maximum credit limit per account. Defaults to 100,000.0.
 * - double processingFee: Fee charged for transactions involving other banks. Defaults to 10.0.
 * - ArrayList<Account> accounts: List of accounts registered under the bank.
 *
 * Methods:
 * - showAccounts(Class<T>): Displays accounts based on the specified type.
 * - getBankAccount(Bank, String): Retrieves an account by account number from a specified bank.
 * - createNewAccount(): Captures user information to create a new account.
 * - createNewCreditAccount(): Creates a new credit account.
 * - createNewSavingsAccount(): Creates a new savings account.
 * - addNewAccount(Account): Adds a new account to the bank.
 * - accountExists(Bank, String): Checks if an account exists in the bank by account number.
 */

public class Bank {
    private int ID;
    private String name, passcode;
    private double DepositLimit, WithdrawLimit, CreditLimit;
    private double processingFee;
    private ArrayList<Account> accounts;

    public Bank(int ID, String name, String passcode) {
        this.ID = ID;
        this.name = name;
        this.passcode = passcode;
    }

    public Bank(int ID, String name, String passcode, double DepositLimit, double WithdrawLimit, double CreditLimit, double processingFee) {
        this.ID = ID;
        this.name = name;
        this.passcode = passcode;
        this.DepositLimit = 50000.0;
        this.WithdrawLimit = 50000.0;
        this.CreditLimit = 100000.0;
        this.processingFee = 10.0;
        this.accounts = new ArrayList<>();
    }

    /** Getters and Setters **/
    public int getID() { return ID; }
    public void setID(int ID) { this.ID = ID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPasscode() { return passcode; }
    public void setPasscode(String passcode) { this.passcode = passcode; }

    public double getDepositLimit() { return DepositLimit; }
    public void setDepositLimit(double depositLimit) { DepositLimit = depositLimit; }

    public double getWithdrawLimit() { return WithdrawLimit; }
    public void setWithdrawLimit(double withdrawLimit) { WithdrawLimit = withdrawLimit; }

    public double getCreditLimit() { return CreditLimit; }
    public void setCreditLimit(double creditLimit) { CreditLimit = creditLimit; }

    public double getProcessingFee() { return processingFee; }
    public void setProcessingFee(double processingFee) { this.processingFee = processingFee; }

    public ArrayList<Account> getAccounts() { return accounts; }
    public void setAccounts(ArrayList<Account> accounts) { this.accounts = accounts; }

    /**
     * Displays accounts based on the specified type.
     * @param accountType Type of account to display.
     */
    public <T> void showAccounts(Class<T> accountType) {
        // TODO: Complete this method
    }

    /**
     * Retrieves an account by account number from the specified bank.
     * @param bank Bank to search from.
     * @param accountNum Account number to find.
     * @return Account object if found, null otherwise.
     */
    public Account getBankAccount(Bank bank, String accountNum) {
        // TODO: Implement credit recompense processing
        return null;
    }

    /**
     * Captures user input to create a new account.
     * @return ArrayList of Field objects representing the account details.
     */
    public ArrayList<Field<String, ?>> createNewAccount() {
        // TODO: Complete this method
        return null;
    }

    /**
     * Creates a new credit account.
     * @return New CreditAccount object.
     */
    public CreditAccount createNewCreditAccount() {
        // TODO: Implement credit recompense processing
        return null;
    }

    /**
     * Creates a new savings account.
     * @return New SavingsAccount object.
     */
    public SavingsAccount createNewSavingsAccount() {
        // TODO: Implement credit recompense processing
        return null;
    }

    /**
     * Adds a new account to the bank if the account number doesn't already exist.
     * @param account Account object to be added.
     */
    public void addNewAccount(Account account) {
        // TODO: Implement account addition
    }

    /**
     * Checks if an account exists in the specified bank by account number.
     * @param bank Bank to check.
     * @param accountNum Account number to find.
     * @return True if the account exists, false otherwise.
     */
    public boolean accountExists(Bank bank, String accountNum) {
        // TODO: Implement credit recompense processing
        return false;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", passcode='" + passcode + '\'' +
                ", DepositLimit=" + DepositLimit +
                ", WithdrawLimit=" + WithdrawLimit +
                ", CreditLimit=" + CreditLimit +
                ", processingFee=" + processingFee +
                ", accounts=" + accounts +
                '}';
    }
}
