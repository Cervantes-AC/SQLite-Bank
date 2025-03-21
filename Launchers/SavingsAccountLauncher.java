package Launchers;

import Accounts.SavingsAccount;
import Transactions.IllegalAccountType;

import java.util.Scanner;
import Main.*;


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

        loggedAccount.transfer(new SavingsAccount(recipientID), amount);
    }
}