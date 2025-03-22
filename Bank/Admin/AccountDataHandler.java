package Bank.Admin;

import Main.Main;

import java.sql.*;
import java.util.Scanner;

/**
 * Handles account-related operations for the bank administration.
 * Supports reading and updating account data in both Savings and Credit accounts.
 */
public class AccountDataHandler {
    private static final String DB_URL = "jdbc:sqlite:Database/Database.db";
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Reads account data based on the selected bank ID.
     * Provides options to view SavingsAccount, CreditAccount, or both.
     */
    public static void readAccount() {
        System.out.print("Enter Bank ID: ");
        int bankID;

        // Ensure Bank ID is numeric
        try {
            bankID = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Bank ID. Please enter a numeric value.");
            return;
        }

        Main.showMenu(32, 2);
        Main.setOption();

        // Connect to the database and fetch account data based on user choice
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            switch (Main.getOption()) {
                case 1:
                    // Show Savings Accounts
                    displayAccounts(conn, "SELECT * FROM SavingsAccount WHERE BankID = ?", "Balance", bankID);
                    break;

                case 2:
                    // Show Credit Accounts
                    displayAccounts(conn, "SELECT * FROM CreditAccount WHERE BankID = ?", "Loan", bankID);
                    break;

                case 3:
                    // Show both Savings and Credit Accounts
                    displayAccounts(conn, "SELECT AccountID, Balance FROM SavingsAccount WHERE BankID = ?", "Balance", bankID);
                    displayAccounts(conn, "SELECT AccountID, Loan FROM CreditAccount WHERE BankID = ?", "Loan", bankID);
                    break;

                case 4:
                    // Return to the admin menu
                    System.out.println("Returning to Admin Menu...");
                    break;

                default:
                    System.out.println("Invalid selection! Please enter a number between 1 and 3.");
                    break;
            }
        } catch (SQLException e) {
            System.out.println("SQLite connection error: " + e.getMessage());
        }
    }

    /**
     * Displays account data from the given query.
     *
     * @param conn         The database connection.
     * @param query        The SQL query to execute.
     * @param amountColumn The column representing the balance or loan.
     * @param bankID       The bank ID to filter accounts.
     */
    private static void displayAccounts(Connection conn, String query, String amountColumn, int bankID) {
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bankID);

            // Execute the query and fetch account data
            try (ResultSet rs = pstmt.executeQuery()) {
                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;
                    System.out.printf("""
                        Account ID: %s,
                        %s: %s
                        \n
                        """,
                            rs.getString("AccountID"),
                            amountColumn,
                            rs.getString(amountColumn) != null ? rs.getString(amountColumn) : "N/A"
                    );
                }
                if (!hasData) System.out.println("No accounts found for this Bank ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving account data: " + e.getMessage());
        }
    }


    /**
     * Updates account details such as First Name, Last Name, Email, or Password.
     * Supports both SavingsAccount and CreditAccount based on the Account ID prefix.
     */
    public static void updateAccount() {
        System.out.print("Enter Account ID to update: ");
        String accountId = scanner.nextLine().trim().toUpperCase();

        // Validate Account ID format (e.g., "CA01-2" or "SA01-2")
        if (!accountId.matches("(CA|SA)\\d{2}-\\d+")) {
            System.out.println("Invalid Account ID format. Use 'CA01-2' or 'SA01-2' format.");
            return;
        }

        // Determine account type based on prefix
        String accountType = accountId.startsWith("CA") ? "CreditAccount" : "SavingsAccount";
        int bankId;

        // Extract BankID from the Account ID (e.g., "2" from "CA01-2")
        try {
            bankId = Integer.parseInt(accountId.split("-")[1]);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.out.println("Invalid Account ID numbers. Ensure format is correct (e.g., CA01-2).");
            return;
        }

        // Check if the account exists in the database
        String checkAccountQuery = "SELECT COUNT(*) FROM " + accountType + " WHERE AccountID = ? AND BankID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(checkAccountQuery)) {

            pstmt.setString(1, accountId);
            pstmt.setInt(2, bankId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next() && rs.getInt(1) == 0) {
                System.out.println("Account with ID: " + accountId + " not found in the database.");
                return;
            }
        } catch (SQLException e) {
            System.out.println("Error checking account existence: " + e.getMessage());
            return;
        }

        // Display update menu options
        System.out.println("Select the field to update:");
        System.out.println("1. First Name");
        System.out.println("2. Last Name");
        System.out.println("3. Email");
        System.out.println("4. Password");

        System.out.print("Enter your choice: ");
        int choice;

        // Ensure user enters a valid numeric choice
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice. Please enter a number between 1 and 4.");
            return;
        }

        String columnToUpdate;

        // Determine which field to update based on choice
        switch (choice) {
            case 1:
                columnToUpdate = "FirstName";
                break;
            case 2:
                columnToUpdate = "LastName";
                break;
            case 3:
                columnToUpdate = "Email";
                break;
            case 4:
                columnToUpdate = "Password";
                break;
            default:
                System.out.println("Invalid choice. Please select a number between 1 and 4.");
                return;
        }

        // Get the new value from the user
        System.out.printf("Enter new value for %s: ", columnToUpdate);
        String newValue = scanner.nextLine().trim();

        if (newValue.isEmpty()) {
            System.out.println("Value cannot be empty.");
            return;
        }

        // Prepare SQL update query
        String sql = "UPDATE " + accountType + " SET " + columnToUpdate + " = ? WHERE AccountID = ? AND BankID = ?";

        // Execute the update query
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newValue);
            pstmt.setString(2, accountId);
            pstmt.setInt(3, bankId);

            int rowsAffected = pstmt.executeUpdate();

            // Confirm whether the update was successful
            if (rowsAffected > 0) {
                System.out.println("Account updated successfully!");
            } else {
                System.out.println("No account found with ID: " + accountId);
            }

        } catch (SQLException e) {
            System.out.println("Error updating account: " + e.getMessage());
        }
    }

    /**
     * Deletes an account from either the SavingsAccount or CreditAccount table.
     * The account type is determined based on the Account ID prefix ('CA' for Credit, 'SA' for Savings).
     * Ensures the Account ID follows the correct format: "CA01-2" or "SA01-2".
     */
    public static void deleteAccount() {
        System.out.print("Enter Account ID to delete: ");
        String accountId = scanner.nextLine().trim();

        // Validate Account ID format (e.g., "CA01-2" or "SA01-2")
        if (!accountId.matches("(CA|SA)\\d{2}-\\d+")) {
            System.out.println("Invalid Account ID format. Use 'CA01-2' or 'SA01-2' format.");
            return;
        }

        // Determine account type based on prefix
        String accountType = accountId.startsWith("CA") ? "CreditAccount" : "SavingsAccount";
        String deleteSql = "DELETE FROM " + accountType + " WHERE AccountID = ?";

        // Execute the deletion query
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {

            pstmt.setString(1, accountId);

            int rowsAffected = pstmt.executeUpdate();

            // Display success or failure message
            System.out.println(rowsAffected > 0
                    ? "Account with ID " + accountId + " has been deleted successfully."
                    : "No account found with ID: " + accountId + " in " + accountType + ".");

        } catch (SQLException e) {
            System.out.println("Error deleting account: " + e.getMessage());
        }
    }
}
