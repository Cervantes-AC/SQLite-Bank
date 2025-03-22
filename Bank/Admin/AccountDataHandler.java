package Bank.Admin;

import Main.Main;

import java.sql.*;
import java.util.Scanner;

public class AccountDataHandler {
    private static final String DB_URL = "jdbc:sqlite:Database/Database.db";
    private static final Scanner scanner = new Scanner(System.in);

    public static void readAccount() {
        System.out.print("Enter Bank ID: ");
        int bankID;
        try {
            bankID = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Bank ID. Please enter a numeric value.");
            return;
        }

        Main.showMenu(32, 2);
        Main.setOption();

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            switch (Main.getOption()) {
                case 1 -> displayAccounts(conn, "SELECT * FROM SavingsAccount WHERE BankID = ?", "Balance", bankID);
                case 2 -> displayAccounts(conn, "SELECT * FROM CreditAccount WHERE BankID = ?", "Loan", bankID);
                case 3 -> {
                    displayAccounts(conn, "SELECT AccountID, Balance FROM SavingsAccount WHERE BankID = ?", "Balance", bankID);
                    displayAccounts(conn, "SELECT AccountID, Loan FROM CreditAccount WHERE BankID = ?", "Loan", bankID);
                }
                case 4 -> System.out.println("Returning to Admin Menu...");
                default -> System.out.println("Invalid selection! Please enter a number between 1 and 3.");
            }
        } catch (SQLException e) {
            System.out.println("SQLite connection error: " + e.getMessage());
        }
    }

    private static void displayAccounts(Connection conn, String query, String amountColumn, int bankID) {
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bankID);
            try (ResultSet rs = pstmt.executeQuery()) {
                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;
                    System.out.printf("""
                    Account ID: %s
                    %s: %s
                    
                    """, rs.getString("AccountID"), amountColumn, rs.getString(amountColumn) != null ? rs.getString(amountColumn) : "N/A");
                }
                if (!hasData) System.out.println("No accounts found for this Bank ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving account data: " + e.getMessage());
        }
    }

    public static void updateAccount() {
        System.out.print("Enter Account ID to update: ");
        String accountId = scanner.nextLine().trim().toUpperCase();

        // Validate and parse the account ID format
        if (!accountId.matches("(CA|SA)\\d{2}-\\d+")) {
            System.out.println("Invalid Account ID format. Use 'CA01-2' or 'SA01-2' format.");
            return;
        }

        String accountType = accountId.startsWith("CA") ? "CreditAccount" : "SavingsAccount";
        int bankId;

        try {
            bankId = Integer.parseInt(accountId.split("-")[1]);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.out.println("Invalid Account ID numbers. Ensure format is correct (e.g., CA01-2).");
            return;
        }

        // Choose what to update
        System.out.println("Select the field to update:");
        System.out.println("1. First Name");
        System.out.println("2. Last Name");
        System.out.println("3. Email");
        System.out.println("4. Password");

        System.out.print("Enter your choice: ");
        int choice;

        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice. Please enter a number between 1 and 4.");
            return;
        }

        String columnToUpdate;
        switch (choice) {
            case 1 -> columnToUpdate = "FirstName";
            case 2 -> columnToUpdate = "LastName";
            case 3 -> columnToUpdate = "Email";
            case 4 -> columnToUpdate = "Password";
            default -> {
                System.out.println("Invalid choice. Please select a number between 1 and 4.");
                return;
            }
        }

        System.out.printf("Enter new value for %s: ", columnToUpdate);
        String newValue = scanner.nextLine().trim();

        if (newValue.isEmpty()) {
            System.out.println("Value cannot be empty.");
            return;
        }

        String sql = "UPDATE " + accountType + " SET " + columnToUpdate + " = ? WHERE AccountID = ? AND BankID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newValue);
            pstmt.setString(2, accountId);
            pstmt.setInt(3, bankId);

            int rowsAffected = pstmt.executeUpdate();

            System.out.println(rowsAffected > 0
                    ? "Account updated successfully!"
                    : "No account found with ID: " + accountId);

        } catch (SQLException e) {
            System.out.println("Error updating account: " + e.getMessage());
        }
    }
}
