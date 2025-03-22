package Bank;

import Bank.Admin.AccountDataHandler;
import Bank.Admin.BankDataHandler;
import Main.*;
import java.util.Scanner;

/**
 * Launches the Admin Panel, providing access to bank management operations like
 * creating banks, reading data, updating accounts, and deleting accounts.
 */
public class AdminLauncher {
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Initializes the Admin Panel menu loop.
     * Provides options for creating banks, managing accounts, and exiting.
     * Handles invalid inputs gracefully.
     */
    public static void AdminLauncherInit() {
        while (true) {
            // Display Admin Menu Header and options
            Main.showMenuHeader("Admin Menu");
            System.out.println("""
                    1 - Create Bank
                    2 - Read Bank Database
                    3 - Update Account
                    4 - Delete Account
                    5 - Exit
                    """);

            System.out.print("Enter your choice: ");

            // Ensure valid numeric input for menu selection
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.next(); // Clear invalid input
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume leftover newline character

            // Main menu switch to handle user actions
            switch (choice) {

                // Option 1: Create a new bank
                case 1:
                    Main.showMenuHeader("Create New Bank");
                    BankLauncher.createNewBank();
                    break;

                // Option 2: Read bank or account data
                case 2:
                    Main.showMenuHeader("Read Bank Database");

                    // Submenu for data reading options
                    System.out.println("""
                        1 - View Bank Data
                        2 - View Account Data
                        3 - Go Back
                        """);

                    System.out.print("Enter your choice (1-3): ");
                    int subChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    // Submenu handling
                    switch (subChoice) {
                        case 1:
                            BankDataHandler.readBank();
                            break;
                        case 2:
                            AccountDataHandler.readAccount();
                            break;
                        case 3:
                            System.out.println("Returning to Admin Menu...");
                            break;
                        default:
                            System.out.println("Invalid choice! Please enter 1, 2, or 3.");
                            break;
                    }
                    break;

                // Option 3: Update an existing account
                case 3:
                    Main.showMenuHeader("Update Account");
                    AccountDataHandler.updateAccount();
                    break;

                // Option 4: Delete an account
                case 4:
                    Main.showMenuHeader("Delete Account");
                    AccountDataHandler.deleteAccount();
                    break;

                // Option 5: Exit the Admin Panel
                case 5:
                    System.out.println("Exiting Admin Panel...");
                    return;

                // Handle invalid main menu selections
                default:
                    System.out.println("Invalid choice! Please enter a number between 1 and 5.");
                    break;
            }
        }
    }
}
