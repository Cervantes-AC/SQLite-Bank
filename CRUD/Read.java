package CRUD;

import Accounts.Account;
import Bank.Bank;
import java.sql.*;
import java.util.Scanner;

public class Read {
    public static void readBank(Scanner scanner) {
        String url = "jdbc:sqlite:Database/Database.db"; // SQLite DB path

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("Connected to SQLite!");

                System.out.println("""
                    Select an option:
                    1 - View ID
                    2 - View Name
                    3 - View Passcode
                    4 - View Deposit Limit
                    5 - View Withdraw Limit
                    6 - View Credit Limit
                    7 - View Processing Fee
                    8 - View All Data
                    """);

                System.out.print("Enter a number (1-8): ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                String columnName;

                // Map user choice to column names
                switch (choice) {
                    case 1:
                        columnName = "BankID";
                        break;
                    case 2 :
                        columnName = "name";
                        break;
                    case 3 :
                        columnName = "passcode";
                        break;
                    case 4 :
                        columnName = "DepositLimit";
                        break;
                    case 5 :
                        columnName = "WithdrawLimit";
                        break;
                    case 6 :
                        columnName = "CreditLimit";
                        break;
                    case 7 :
                        columnName = "processingFee";
                        break;
                    case 8 : {
                        columnName = "*"; // Select all columns
                        break;
                    }
                    default: {
                        System.out.println("Invalid selection! Please enter a number between 1 and 8.");
                        return;
                    }
                }

                String selectSQL = "SELECT " + columnName + " FROM Bank";

                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(selectSQL)) {

                    System.out.println("\nDisplaying selected data:");
                    System.out.println("------------------------------");

                    while (rs.next()) {
                        if (choice == 8) {
                            // Print all columns when option 8 is selected
                            Bank bank = new Bank(rs.getString("name"), rs.getString("passcode"));
                            bank.setBankID(rs.getInt("BankID"));
                            bank.setDepositLimit(rs.getDouble("DepositLimit"));
                            bank.setWithdrawLimit(rs.getDouble("WithdrawLimit"));
                            bank.setCreditLimit(rs.getDouble("CreditLimit"));
                            bank.setProcessingFee(rs.getDouble("processingFee"));

                            System.out.printf("""
                                Bank ID: %d
                                Name: %s
                                Passcode: %s
                                Deposit Limit: %.2f
                                Withdraw Limit: %.2f
                                Credit Limit: %.2f
                                Processing Fee: %.2f
                                ------------------------------
                                """,
                                    bank.getBankID(),
                                    bank.getName(),
                                    bank.getPasscode(),
                                    bank.getDepositLimit(),
                                    bank.getWithdrawLimit(),
                                    bank.getCreditLimit(),
                                    bank.getProcessingFee());
                        } else {
                            // Print only the selected column
                            System.out.println(columnName + ": " + rs.getString(columnName));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLite connection error: " + e.getMessage());
        }
    }

    public static void readAccount(Scanner scanner) {  // Accept Scanner as a parameter
        String url = "jdbc:sqlite:Database/Database.db"; // SQLite DB path

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("Connected to SQLite!");

                System.out.println("""
                    Select an option:
                    1 - View Account ID
                    2 - View First Name
                    3 - View Last Name
                    4 - View Email
                    5 - View PIN
                    6 - View All Data
                    """);

                System.out.print("Enter a number (1-6): ");
                int choice;

                try {
                    choice = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input! Please enter a number between 1 and 6.");
                    return;
                }

                String columnName;
                switch (choice) {
                    case 1:
                        columnName = "AccountID";
                        break;
                    case 2:
                        columnName = "FirstName";
                        break;
                    case 3:
                        columnName = "LastName";
                        break;
                    case 4:
                        columnName = "Email";
                        break;
                    case 5:
                        columnName = "PIN";
                        break;
                    case 6:
                        columnName = "*"; // Select all relevant columns
                        break;
                    default:
                        System.out.println("Invalid selection! Please enter a number between 1 and 6.");
                        return;
                }

                String selectSQL;
                if (choice == 6) {
                    selectSQL = "SELECT AccountID, FirstName, LastName, Email, PIN FROM Account";  // Fetch only required columns
                } else {
                    selectSQL = "SELECT " + columnName + " FROM Account";
                }

                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(selectSQL)) {

                    System.out.println("\nDisplaying selected data:");
                    System.out.println("------------------------------");

                    while (rs.next()) {
                        if (choice == 6) {
                            // Print all selected columns when option 6 is chosen
                            System.out.printf("""
                                Account ID: %s
                                Name: %s %s
                                Email: %s
                                PIN: %s
                                ------------------------------
                                """,
                                    rs.getString("AccountID"),
                                    rs.getString("FirstName"),
                                    rs.getString("LastName"),
                                    rs.getString("Email"),
                                    rs.getString("PIN")
                            );
                        } else {
                            // Print only the selected column
                            System.out.println(columnName + ": " + rs.getString(columnName));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLite connection error: " + e.getMessage());
        }
    }


}
