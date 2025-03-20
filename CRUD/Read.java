package CRUD;

import Accounts.Account;
import Bank.Bank; // Import Bank class
import java.sql.*;
import java.util.Scanner;

public class Read {
    public static void readbank() {
        String url = "jdbc:sqlite:Database/Database.db"; // SQLite DB path

        try (Scanner scanner = new Scanner(System.in);
             Connection conn = DriverManager.getConnection(url)) {

            if (conn != null) {
                System.out.println("Connected to SQLite!");

                // Display menu options
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
                scanner.nextLine(); // Consume newline character

                String selectSQL;

                if (choice == 8) { // View all data
                    selectSQL = "SELECT * FROM Bank";

                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery(selectSQL)) {

                        System.out.println("\nDisplaying all bank data:");
                        System.out.println("------------------------------");

                        while (rs.next()) {
                            // Create a Bank object using retrieved data
                            Bank bank = new Bank(rs.getString("name"), rs.getString("passcode"));
                            bank.setID(rs.getInt("ID"));
                            bank.setDepositLimit(rs.getDouble("DepositLimit"));
                            bank.setWithdrawLimit(rs.getDouble("WithdrawLimit"));
                            bank.setCreditLimit(rs.getDouble("CreditLimit"));
                            bank.setProcessingFee(rs.getDouble("processingFee"));

                            // Print using getters
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
                                    bank.getID(),
                                    bank.getName(),
                                    bank.getPasscode(),
                                    bank.getDepositLimit(),
                                    bank.getWithdrawLimit(),
                                    bank.getCreditLimit(),
                                    bank.getProcessingFee());
                        }
                    }
                } else {
                    // Map user choice to the corresponding column
                    String columnName;
                    switch (choice) {
                        case 1:
                            columnName = "ID";
                            break;
                        case 2:
                            columnName = "name";
                            break;
                        case 3:
                            columnName = "passcode";
                            break;
                        case 4:
                            columnName = "DepositLimit";
                            break;
                        case 5:
                            columnName = "WithdrawLimit";
                            break;
                        case 6:
                            columnName = "CreditLimit";
                            break;
                        case 7:
                            columnName = "processingFee";
                            break;
                        default:
                            columnName = null;
                    }

                    if (columnName == null) {
                        System.out.println("Invalid selection! Please enter a number between 1 and 8.");
                        return;
                    }

                    // Query to fetch only the selected column
                    selectSQL = "SELECT " + columnName + " FROM Bank";

                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery(selectSQL)) {

                        System.out.println("\nDisplaying all values for: " + columnName);
                        System.out.println("------------------------------");

                        while (rs.next()) {
                            System.out.println(rs.getString(columnName));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLite connection error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input! Please enter a valid number.");
        }
    }

    public static void readAccount() {
        String url = "jdbc:sqlite:Database/Database.db"; // SQLite DB path

        try (Scanner scanner = new Scanner(System.in);
             Connection conn = DriverManager.getConnection(url)) {

            if (conn != null) {
                System.out.println("Connected to SQLite!");

                // Display menu options
                System.out.println("""
                Select an option:
                1 - View ID
                2 - View Type
                3 - View Account ID
                4 - View First Name
                5 - View Last Name
                6 - View Email
                7 - View PIN
                8 - View All Data
                """);

                System.out.print("Enter a number (1-8): ");
                String input = scanner.nextLine().trim();  // Read input as a string
                int choice;

                try {
                    choice = Integer.parseInt(input);  // Convert to integer
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input! Please enter a number between 1 and 8.");
                    return;
                }

                String selectSQL;

                if (choice == 8) { // View all data
                    selectSQL = "SELECT * FROM Account";

                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery(selectSQL)) {

                        System.out.println("\nDisplaying all account data:");
                        System.out.println("------------------------------");

                        while (rs.next()) {
                            // Print account details
                            System.out.printf("""
                            Account ID: %s
                            Name: %s %s
                            Email: %s
                            Type: %s
                            ------------------------------
                            """,
                                    rs.getString("AccountID"),
                                    rs.getString("FirstName"),
                                    rs.getString("LastName"),
                                    rs.getString("Email"),
                                    rs.getString("Type")
                            );
                        }
                    }
                } else {
                    // Map user choice to the corresponding column
                    String columnName;
                    switch (choice) {
                        case 1 -> columnName = "ID";
                        case 2 -> columnName = "Type";
                        case 3 -> columnName = "AccountID";
                        case 4 -> columnName = "FirstName";
                        case 5 -> columnName = "LastName";
                        case 6 -> columnName = "Email";
                        case 7 -> columnName = "PIN";
                        default -> {
                            System.out.println("Invalid selection! Please enter a number between 1 and 8.");
                            return;
                        }
                    }

                    // Query to fetch only the selected column
                    selectSQL = "SELECT " + columnName + " FROM Account";

                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery(selectSQL)) {

                        System.out.println("\nDisplaying all values for: " + columnName);
                        System.out.println("------------------------------");

                        while (rs.next()) {
                            System.out.println(rs.getString(columnName));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLite connection error: " + e.getMessage());
        }
    }}


