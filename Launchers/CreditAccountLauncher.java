package Launchers;

import Accounts.CreditAccount;
import Transactions.IllegalAccountType;
import Main.*;
import java.util.Scanner;


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
            Main.showMenuHeader("Credit Account Transaction Menu");
            Main.showMenu(41,2);
            Main.setOption();
            switch (Main.getOption()) {
                case 1:
                    Main.showMenuHeader("Loan Amount");
                    System.out.println(loggedAccount.getLoanStatement());
                    break;
                case 2:
                    Main.showMenuHeader("Payment");
                    creditPaymentProcess();
                    break;
                case 3:
                    Main.showMenuHeader("Recompense");
                    creditRecompenseProcess();
                    break;
                case 4:
                    Main.showMenuHeader("Transaction History");
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

    private static void creditPaymentProcess() {
        System.out.print("Enter amount to pay: ");
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
                System.out.println("Payment successful! Updated loan balance: ₱" + loggedAccount.getLoan());
            } else {
                System.out.println("Payment failed. Please check your balance and try again.");
            }
        } catch (IllegalAccountType e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void creditRecompenseProcess() {
        System.out.print("Enter amount to recompense: ");
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
            System.out.println("Recompense successful! Updated loan balance: ₱" + loggedAccount.getLoan());
        } else {
            System.out.println("Recompense failed. Please check your balance and try again.");
        }
    }
}