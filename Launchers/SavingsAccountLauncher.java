package Launchers;

import Accounts.EducationalAccount;
import Accounts.SavingsAccount;
import Transactions.IllegalAccountType;
import Main.*;

import java.util.Scanner;
/**
 * SavingsAccountLauncher Class
 * Extends AccountLauncher to handle savings account-specific operations.
 * <p>
 * Methods:
 * - savingsAccountInit(): Initializes savings account interactions, displaying the main menu.
 * - depositProcess(): Handles the deposit transaction process.
 * - withdrawProcess(): Manages the withdrawal transaction process.
 * - transferProcess(): Handles fund transfers between accounts.
 * - getLoggedAccount(): Returns the currently logged-in SavingsAccount.
 */

public class SavingsAccountLauncher extends AccountLauncher {
    private static SavingsAccount loggedAccount;
    private static final Scanner scanner = new Scanner(System.in);

    public static void savingsAccountInit() throws IllegalAccountType {
        if (!isLoggedIn()) {
            System.out.println("No account is logged in. Redirecting to login...");
            AccountInit();
        }

        if (!(getLoggedAccount() instanceof SavingsAccount)) {
            System.out.println("This is not a savings account. Returning to menu.");
            return;
        }

        loggedAccount = (SavingsAccount) getLoggedAccount();
        System.out.println("\nWelcome, " + loggedAccount.getOwnerFullName());
        savingsMenu();
    }

    private static void savingsMenu() throws IllegalAccountType {
        while (true) {
            Main.showMenuHeader("Savings Account");
            Main.showMenu(51,2);
            Main.setOption();

            switch (Main.getOption()) {
                case 1:
                    Main.showMenuHeader("Balance Amount");
                    System.out.println(loggedAccount.getAccountBalanceStatement());
                    break;
                case 2:
                    Main.showMenuHeader("Deposit");
                    depositProcess();
                    break;
                case 3:
                    Main.showMenuHeader("Withdraw");
                    withdrawProcess();
                    break;
                case 4:
                    Main.showMenuHeader("Transfer");
                    transferProcess();
                    break;
                case 5:
                    Main.showMenuHeader("Transaction History");
                    System.out.println(loggedAccount.getTransactionsInfo());
                    break;
                case 6:
                    System.out.println("Logging out...");
                    destroyLogSession();
                    return;
                default:
                    System.out.println("Invalid choice, try again.");
            }
        }
    }

    private static void depositProcess() {
        System.out.print("Enter deposit amount: ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount.");
            return;
        }
        loggedAccount.cashDeposit(amount);
    }

    private static void withdrawProcess() {
        System.out.print("Enter withdrawal amount: ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount.");
            return;
        }

        loggedAccount.withdrawal(amount);
    }

    private static void transferProcess() throws IllegalAccountType {
        System.out.print("Enter recipient account ID: ");
        String recipientID = scanner.nextLine().trim().toUpperCase();

        System.out.print("Enter transfer amount: ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount.");
            return;
        }

        loggedAccount.transfer(new EducationalAccount(recipientID), amount);
    }
}