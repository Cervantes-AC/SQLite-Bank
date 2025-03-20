package Bank;
import CRUD.Create;

import java.util.Scanner;

public class BankLauncher {

    // Method to create and add a new bank with user input
    public static void addBank() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Bank Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Passcode: ");
        String passcode = scanner.nextLine();

        // Create a new bank with default limits and fees
        Bank newBank = new Bank(name, passcode);

        // Insert the bank into the database
        if (newBank.insertBank(newBank)) {
            System.out.println("Bank added successfully: " + newBank.getName());
            System.out.println("Bank ID: " + newBank.getID());

            // Display the bank’s details
            newBank.toString();
        } else {
            System.out.println("Failed to add bank.");
        }

        scanner.close();
    }
}
