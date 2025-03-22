package Launchers;

import Accounts.EducationalAccount;
import Transactions.IllegalAccountType;
import Main.*;

import java.util.Scanner;

/**
 * EducationalAccountLauncher Class
 * Extends AccountLauncher to handle educational account-specific operations.
 * <p>
 * Methods:
 * - educationalAccountInit(): Initializes educational account interactions, displaying the main menu.
 * - depositProcess(): Handles the deposit transaction process.
 * - withdrawProcess(): Manages the withdrawal transaction process.
 * - transferProcess(): Handles fund transfers between accounts.
 * - getLoggedAccount(): Returns the currently logged-in EducationalAccount.
 */

public class EducationalAccountLauncher extends AccountLauncher {
    private static EducationalAccount loggedAccount;
    private static final Scanner scanner = new Scanner(System.in);

    public static void educationalAccountInit() throws IllegalAccountType {
        if (!isLoggedIn()) {
            System.out.println("No account is logged in. Redirecting to login...");
            AccountInit();
        }

        if (!(getLoggedAccount() instanceof EducationalAccount)) {
            System.out.println("This is not an educational account. Returning to menu.");
            return;
        }

        loggedAccount = (EducationalAccount) getLoggedAccount();
        System.out.println("\nWelcome, " + loggedAccount.getOwnerFullName());
        educationalMenu();
    }

    private static void educationalMenu() throws IllegalAccountType {
        while (true) {
            Main.showMenuHeader("Educational Account Transaction Menu");
            Main.showMenu(51, 2);
            Main.setOption();
            switch (Main.getOption()) {
                case 1:
                    Main.showMenuHeader("Educational Balance");
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
