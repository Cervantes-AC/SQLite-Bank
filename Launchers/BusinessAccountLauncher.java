package Launchers;

import Accounts.BusinessAccount;
import Transactions.IllegalAccountType;

import java.util.Scanner;
/**
 * BusinessAccountLauncher Class
 * Extends AccountLauncher to handle business account-specific operations.
 *
 * Methods:
 * - BusinessAccountInit(): Initializes business account interactions, displaying the main menu.
 * - businessPaymentProcess(): Processes a business payment transaction.
 * - businessRecompenseProcess(): Handles business recompense transactions.
 * - getLoggedAccount(): Returns the currently logged-in BusinessAccount.
 */



/**
 * BusinessAccountLauncher Class
 * Handles business account-specific operations.
 */
public class BusinessAccountLauncher extends AccountLauncher {
    private static BusinessAccount loggedAccount;
    private static final Scanner scanner = new Scanner(System.in);

    public static void businessAccountInit() throws IllegalAccountType {
        if (!isLoggedIn()) {
            System.out.println("No account is logged in. Redirecting to login...");
            AccountInit();
        }

        if (!(getLoggedAccount() instanceof BusinessAccount)) {
            System.out.println("This is not a business account. Returning to menu.");
            return;
        }

        loggedAccount = (BusinessAccount) getLoggedAccount();
        System.out.println("\nWelcome, " + loggedAccount.getOwnerFullName());
        businessMenu();
    }

    private static void businessMenu() {
        while (true) {
            System.out.println("\n1. View Loan Statement");
            System.out.println("2. Make Payment");
            System.out.println("3. Recompense Loan");
            System.out.println("4. Show Transactions");
            System.out.println("5. Logout");

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
                    System.out.println("\n" + loggedAccount.getLoanStatement());
                    break;
                case 2:
                    businessPaymentProcess();
                    break;
                case 3:
                    businessRecompenseProcess();
                    break;
                case 4:
                    System.out.println(loggedAccount.getTransactionsInfo());
                    break;
                case 5:
                    System.out.println("Logging out...");
                    destroyLogSession();
                    return;
                default:
                    System.out.println("Invalid choice, try again.");
            }
        }
    }

    private static void businessPaymentProcess() {
        System.out.print("\nEnter amount to pay: ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a valid number.");
            return;
        }

        try {
            boolean success = loggedAccount.pay(loggedAccount, amount);
            if (success) {
                System.out.println("Payment successful! Updated loan balance: $" + loggedAccount.getLoan());
            } else {
                System.out.println("Payment failed. Please check your balance and try again.");
            }
        } catch (IllegalAccountType e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void businessRecompenseProcess() {
        System.out.print("\nEnter amount to recompense: ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a valid number.");
            return;
        }

        if (amount <= 0) {
            System.out.println("Invalid recompense amount.");
            return;
        }

        boolean success = loggedAccount.recompense(amount);

        if (success) {
            System.out.println("Recompense successful! Updated loan balance: $" + loggedAccount.getLoan());
        } else {
            System.out.println("Recompense failed. Please check your balance and try again.");
        }
    }
}