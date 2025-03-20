package Launchers;

import Accounts.Account;
import java.util.Scanner;

public class AccountLauncher {

    // Method to create and add a new account with user input
    public void addAccount() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Account ID: ");
        int bankID = scanner.nextInt();
        scanner.nextLine(); // Consume leftover newline

        System.out.print("Enter Account Type (SA/CA): ");
        String type = scanner.nextLine().toUpperCase();

        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        System.out.print("Set Account PIN: ");
        String pin = scanner.nextLine();


        // Create the account
        Account newAccount = new Account(bankID, type, firstName, lastName, email, pin);

        if (newAccount.insertAccount(bankID, type, firstName, lastName, email, pin)) {
            System.out.println("Account created successfully!");
            System.out.println(newAccount);
        } else {
            System.out.println("Failed to create account.");
        }
    }
}