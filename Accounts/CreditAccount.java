package Accounts;

import Bank.Bank;
import Transactions.*;
import java.sql.*;
import java.util.Scanner;

/**
 * CreditAccount Class
 * Represents a credit account that allows transactions based on a loan amount.
 * Implements Payment and Recompense interfaces.
 */
public class CreditAccount extends Account implements Payment, Recompense {
    private double loan;
    private static final String DB_URL = "jdbc:sqlite:Database/Database.db";
    private static final Scanner input = new Scanner(System.in);

    public CreditAccount(int bankID, String firstName, String lastName, String email, String pin, double initialLoan) {
        super(bankID, "Credit", firstName, lastName, email, pin);
        this.loan = initialLoan;
    }

    /**
     * Creates a new CreditAccount with an initial loan amount and inserts it into the database.
     */
    public static CreditAccount createCreditAccount(int bankID, String firstName, String lastName, String email, String pin) {
        System.out.print("Enter initial loan amount: ");
        double initialLoan;

        // Handle bad input gracefully
        try {
            initialLoan = Double.parseDouble(input.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid loan amount. Defaulting to 0.");
            initialLoan = 0;
        }

        // Create a new CreditAccount object
        CreditAccount newAccount = new CreditAccount(bankID, firstName, lastName, email, pin, initialLoan);

        // Try inserting into the database
        return newAccount.insertAccount("CreditAccount", "Loan", initialLoan) ? newAccount : null;
    }

    public double getLoan() {
        return loan;
    }

    public void setLoan(double loan) {
        this.loan = Math.max(0, loan); // Prevent negative loan values
    }

    public String getLoanStatement() {
        return "Loan balance: " + loan;
    }

    /**
     * Checks if the account has enough loan balance for a credit transaction.
     */
    private boolean canCredit(double amountAdjustment) {
        return amountAdjustment <= loan;
    }

    /**
     * Adjusts the loan balance (e.g., after a transaction).
     */
    private void adjustLoanAmount(double amountAdjustment) {
        loan = Math.max(0, loan + amountAdjustment);
    }

    @Override
    public boolean pay(Account account, double amount) throws IllegalAccountType {
        // Placeholder logic — expand this later
        return false;
    }

    @Override
    public boolean recompense(double amount) {
        // Placeholder logic — expand this later
        return false;
    }
}
