package Bank.Admin;

import java.sql.*;
import java.util.Scanner;
import Main.*;

/**
 * Handles administrative operations for banks, including reading bank data and deleting accounts.
 */
public class BankDataHandler {
    private static final String DB_URL = "jdbc:sqlite:Database/Database.db";
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Reads and displays bank or account data based on user selection.
     * Options:
     * 1. View Bank Data — Displays BankID and Name from the Bank table.
     * 2. View Account Data — Prompts for Account ID, then displays account details (Savings or Credit).
     * 3. Go Back — Returns to the previous menu.
     */
    public static void readBank() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            // Display menu options
            System.out.println("""
                1. View Bank Data
                2. View Account Data
                3. Go Back
                """);

            System.out.print("Enter a number: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline left from nextInt()

            String query;

            // Determine the query based on user choice
            if (choice == 1) {
                // Modified to select BankID and Name from the Bank table
                query = "SELECT BankID, Name FROM Bank";
            } else if (choice == 2) {
                // Query combines Savings and Credit account data into one view
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

            // Display a custom header for clarity
            Main.showMenuHeader("Bank Data Menu");

            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                // If viewing account data, prompt for Account ID
                if (choice == 2) {
                    System.out.print("Enter Account ID: ");
                    String accountId = scanner.nextLine().trim().toUpperCase();
                    pstmt.setString(1, accountId);
                    pstmt.setString(2, accountId);
                }

                // Execute the query and process results
                try (ResultSet rs = pstmt.executeQuery()) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columns = metaData.getColumnCount();

                    boolean hasData = false;

                    // Loop through each row of data and print column values
                    while (rs.next()) {
                        hasData = true;
                        for (int i = 1; i <= columns; i++) {  // Correct loop to include last column
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


}
