package Bank.Admin;

import java.sql.*;
import java.util.Scanner;
import Main.*;

public class BankDataHandler {
    private static final String DB_URL = "jdbc:sqlite:Database/Database.db";
    private static final Scanner scanner = new Scanner(System.in);

    public static void readBank() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            System.out.println("""
                1. View Bank Data
                2. View Account Data
                3. Go Back
                """);

            System.out.print("Enter a number: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            String query = "";

            if (choice == 1) {
                query = "SELECT BankID, Name FROM Bank";
            } else if (choice == 2) {
                query = """
                    SELECT AccountID, FirstName, LastName, Balance 
                    FROM SavingsAccount 
                    WHERE AccountID = ? 
                    UNION 
                    SELECT AccountID, FirstName, LastName, Loan AS Balance 
                    FROM CreditAccount 
                    WHERE AccountID = ?
                    """;
            } else if (choice == 3) {
                System.out.println("Returning to Admin Menu...");
                return;
            } else {
                System.out.println("Invalid choice. Please try again.");
                return;
            }

            Main.showMenuHeader("Bank Data Menu");

            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                if (choice == 2) {
                    System.out.print("Enter Account ID: ");
                    String accountId = scanner.nextLine().trim().toUpperCase();
                    pstmt.setString(1, accountId);
                    pstmt.setString(2, accountId);
                }

                try (ResultSet rs = pstmt.executeQuery()) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columns = metaData.getColumnCount();

                    boolean hasData = false;

                    while (rs.next()) {
                        hasData = true;
                        for (int i = 1; i < columns; i++) {
                            System.out.printf("%s: %s%n", metaData.getColumnName(i), rs.getString(i));
                        }
                        System.out.println("------------------------------");
                    }

                    if (!hasData) System.out.println("No data found.");
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLite connection error: " + e.getMessage());
        }
    }

    public static void deleteAccount() {
        System.out.print("Enter Account ID to delete: ");
        String accountId = scanner.nextLine().trim();

        // Validate Account ID format (e.g., CA01-2 or SA01-2)
        if (!accountId.matches("(CA|SA)\\d{2}-\\d+")) {
            System.out.println("Invalid Account ID format. Use 'CA01-2' or 'SA01-2' format.");
            return;
        }

        // Determine account type based on prefix
        String accountType = accountId.startsWith("CA") ? "CreditAccount" : "SavingsAccount";
        String deleteSql = "DELETE FROM " + accountType + " WHERE AccountID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {

            pstmt.setString(1, accountId);

            int rowsAffected = pstmt.executeUpdate();

            // Show result
            System.out.println(rowsAffected > 0
                    ? "Account with ID " + accountId + " has been deleted successfully."
                    : "No account found with ID: " + accountId + " in " + accountType + ".");

        } catch (SQLException e) {
            System.out.println("Error deleting account: " + e.getMessage());
        }
    }
}
