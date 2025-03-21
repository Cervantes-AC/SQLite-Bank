package Launchers;

import Accounts.CreditAccount;
import Transactions.IllegalAccountType;

import java.util.Scanner;
/**
 * CreditAccountLauncher Class
 * Extends AccountLauncher to handle credit account-specific operations.
 *
 * Methods:
 * - creditAccountInit(): Initializes credit account interactions, displaying the main menu.
 * - creditPaymentProcess(): Processes a credit payment transaction.
 * - creditRecompenseProcess(): Handles credit recompense transactions.
 * - getLoggedAccount(): Returns the currently logged-in CreditAccount.
 */



/**
 * CreditAccountLauncher Class
 * Handles credit account-specific operations.
 */
public class CreditAccountLauncher extends AccountLauncher {
    private static CreditAccount loggedAccount;
    private static final Scanner scanner = new Scanner(System.in);

    public static void creditAccountInit() throws IllegalAccountType {
        if (!isLoggedIn()) {
            System.out.println("No account is logged in. Redirecting to login...");
            AccountInit();
        }

        if (!(getLoggedAccount() instanceof CreditAccount)) {
            System.out.println("This is not a credit account. Returning to menu.");
            return;
        }

        loggedAccount = (CreditAccount) getLoggedAccount();
        System.out.println("\nWelcome, " + loggedAccount.getOwnerFullName());
        creditMenu();
    }

    private static void creditMenu() {
        while (true) {
            System.out.println("\n1. View Loan Statement");
            System.out.println("2. Make Payment");
            System.out.println("3. Recompense Loan");
            System.out.println("4. Logout");
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
                    creditPaymentProcess();
                    break;
                case 3:
                    creditRecompenseProcess();
                    break;
                case 4:
                    System.out.println("Logging out...");
                    destroyLogSession();
                    return;
                default:
                    System.out.println("Invalid choice, try again.");
            }
        }
    }

    private static void creditPaymentProcess() {
        System.out.print("\nEnter amount to pay: ");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a valid number.");
            return;
        }

        // ✅ FIX: Call pay() instead of recompense()
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

    private static void creditRecompenseProcess() {
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