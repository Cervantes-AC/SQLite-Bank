//package Test;
//
//import Bank.BankLauncher;
//import Launchers.AccountLauncher;
//import java.util.InputMismatchException;
//import java.util.Scanner;
//
//public class TestCreate {
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        BankLauncher CreateNewBank = new BankLauncher();
//        AccountLauncher CreateNewAccount = new AccountLauncher();
//
//        while (true) {
//            System.out.println("\nSelect an option:");
//            System.out.println("1. Create a new bank");
//            System.out.println("2. Create a new account");
//            System.out.println("3. Exit");
//            System.out.print("Enter your choice: ");
//
//            int choice = -1;
//            try {
//                choice = scanner.nextInt();
//                scanner.nextLine(); // Consume the leftover newline
//            } catch (InputMismatchException e) {
//                System.out.println("Invalid input! Please enter a number between 1 and 3.");
//                scanner.nextLine(); // Clear invalid input
//                continue;
//            }
//
//            switch (choice) {
//                case 1:
//                    CreateNewBank.addBank();
//                    break;
//                case 2:
//                    CreateNewAccount.addAccount();
//                    break;
//                case 3:
//                    System.out.println("Exiting... Goodbye!");
//                    scanner.close();
//                    System.exit(0);
//                    break;
//                default:
//                    System.out.println("Invalid choice. Please try again.");
//            }
//        }
//    }
//}
