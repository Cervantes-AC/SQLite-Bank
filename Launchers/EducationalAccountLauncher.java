package Launchers;

import Accounts.EducationalAccount;
import Transactions.IllegalAccountType;

import java.util.Scanner;
/**
 * EducationalAccountLauncher Class
 * Extends AccountLauncher to handle educational account-specific operations.
 *
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
            System.out.println("This is not a educational account. Returning to menu.");
            return;
        }

        loggedAccount = (EducationalAccount) getLoggedAccount();
        System.out.println("\nWelcome, " + loggedAccount.getOwnerFullName());
        educationalMenu();
    }

    private static void educationalMenu() throws IllegalAccountType {
        while (true) {
            System.out.println("\n1. View Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Transfer Funds");
            System.out.println("5. Show Transactions");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.println("\n" + loggedAccount.getAccountBalanceStatement());
                    break;
                case 2:
                    depositProcess();
                    break;
                case 3:
                    withdrawProcess();
                    break;
                case 4:
                    transferProcess();
                    break;
                case 5:
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
        System.out.print("\nEnter deposit amount: ");
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
        System.out.print("\nEnter withdrawal amount: ");
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
        System.out.print("\nEnter recipient account ID: ");
        String recipientID = scanner.nextLine().trim();

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