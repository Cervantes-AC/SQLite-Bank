package Bank;

import Bank.Admin.AccountDataHandler;
import Bank.Admin.BankDataHandler;
import Main.*;
import java.util.Scanner;

public class AdminLauncher {
    private static final Scanner scanner = new Scanner(System.in);

    public static void AdminLauncherInit() {
        while (true) {
            Main.showMenuHeader("Admin Menu");
            System.out.println("""
                    1 - Create Bank
                    2 - Read Bank Database
                    3 - Update Account
                    4 - Delete Account
                    5 - Exit
                    """);

            System.out.print("Enter your choice: ");
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.next(); // Clear invalid input
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    Main.showMenuHeader("Create New Bank");
                    BankLauncher.createNewBank();
                    break;

                case 2:
                    Main.showMenuHeader("Read Bank Database");
                    System.out.println("""
            1 - View Bank Data
            2 - View Account Data
            3 - Go Back
            """);

                    System.out.print("Enter your choice (1-3): ");
                    int subChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

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


                case 3:
                    Main.showMenuHeader("Update Account");
                    AccountDataHandler.updateAccount(); // Assuming you'll define this later
                    break;

                case 4:
                    Main.showMenuHeader("Delete Account");
                    BankDataHandler.deleteAccount();
                    break;

                case 5:
                    System.out.println("Exiting Admin Panel...");
                    return;

                default:
                    System.out.println("Invalid choice! Please enter a number between 1 and 5.");
                    break;
            }
        }
    }
}
