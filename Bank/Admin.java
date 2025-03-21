package Bank;

import java.sql.*;
import java.util.Scanner;


public class Admin {
    private static final String DB_URL = "jdbc:sqlite:Database/Database.db";
    private static final Scanner scanner = new Scanner(System.in);

    public static void adminMenu() {
        while (true) {
            System.out.println("""
                    \n===== Admin Panel =====
                    1 - Create Bank
                    2 - Read Data
                    3 - Update Data
                    4 - Delete Data
                    5 - Exit
                    """);

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> BankLauncher.createNewBank();
                case 2 -> selectEntityAndPerformAction("read");
                case 3 -> selectEntityAndPerformAction("update");
                case 4 -> selectEntityAndPerformAction("delete");
                case 5 -> {
                    System.out.println("Exiting Admin Panel...");
                    return;
                }
                default -> System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    // New method to determine whether to act on Bank or Account
    private static void selectEntityAndPerformAction(String action) {
        System.out.println("""
                \nSelect an entity:
                1 - Bank
                2 - Account
                """);

        System.out.print("Enter your choice: ");
        int entityChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (entityChoice) {
            case 1:
                if (action.equals("read")) readBank();
//            else if (action.equals("update")) updateBank();
                else if (action.equals("delete")) deleteBank();
                break;
            case 2:
                if (action.equals("read")) readAccount(); // No need for bankID parameter
//            else if (action.equals("update")) updateAccount();
//            else if (action.equals("delete")) deleteAccount();
                break;
            default:
                System.out.println("Invalid selection! Returning to Admin Menu.");
        }
    }

    public static void readBank() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                System.out.println("""
                        Select an option:
                        1 - View ID
                        2 - View Name
                        3 - View Passcode
                        4 - View All Data
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
                    case 2:
                        columnName = "name";
                        break;
                    case 3:
                        columnName = "passcode";
                        break;
                    case 4: {
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
                        if (choice == 4) {
                            // Print all columns when option 8 is selected
                            Bank bank = new Bank(rs.getString("name"), rs.getString("passcode"));
                            bank.setBankID(rs.getInt("BankID"));
                            System.out.printf("""
                                            Bank ID: %d
                                            Name: %s
                                            Passcode: %s
                                            ------------------------------
                                            """,
                                    bank.getBankID(),
                                    bank.getName(),
                                    bank.getPasscode());
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

    public static boolean deleteBank() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Bank ID to delete: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid Bank ID.");
            scanner.next(); // Clear invalid input
        }
        int bankID = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String sql = "DELETE FROM Bank WHERE BankID = ?"; // Make sure 'BankID' matches your DB schema

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bankID);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Bank with ID " + bankID + " has been deleted successfully.");
                return true;
            } else {
                System.out.println("No bank found with ID: " + bankID);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting bank: " + e.getMessage());
        }

        return false;
    }

    public static void readAccount() {
        if (BankLauncher.loggedInBankID == -1) { // Auto-login if not logged in
            System.out.print("Enter Bank ID to log in: ");
            String bankID = scanner.nextLine();

            System.out.print("Enter Bank Passcode: ");
            String passcode = scanner.nextLine();

            BankLauncher.bankLogin(bankID, passcode);
            if (BankLauncher.loggedInBankID == -1) {
                System.out.println("Login failed. Returning to the menu.");
                return;
            }
        }

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                // Query SavingsAccount
                String savingsSQL = "SELECT AccountID, Balance FROM SavingsAccount WHERE BankID = ?";
                System.out.println("\n--- Savings Accounts ---");
                displayAccounts(conn, savingsSQL, "Balance");

                // Query CreditAccount (Loan instead of Balance)
                String creditSQL = "SELECT AccountID, Loan FROM CreditAccount WHERE BankID = ?";
                System.out.println("\n--- Credit Accounts ---");
                displayAccounts(conn, creditSQL, "Loan");
            }
        } catch (SQLException e) {
            System.out.println("SQLite connection error: " + e.getMessage());
        } finally {
            // Auto-logout after reading accounts
            BankLauncher.loggedInBankID = -1;
            System.out.println("Logged out.");
        }
    }

    // Helper method to display account data
    private static void displayAccounts(Connection conn, String query, String amountColumn) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, BankLauncher.loggedInBankID);
            System.out.println("Using BankID: " + BankLauncher.loggedInBankID);  // Debugging line
            try (ResultSet rs = pstmt.executeQuery()) {
                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;
                    System.out.printf("""
                    Account ID: %s
                    %s: %s
                    ------------------------------
                    """,
                            rs.getString("AccountID"),
                            amountColumn,
                            rs.getString(amountColumn) != null ? rs.getString(amountColumn) : "N/A"
                    );
                }
                if (!hasData) {
                    System.out.println("No accounts found for this bank.");  // Debugging message
                }
            }
        }
    }

}
