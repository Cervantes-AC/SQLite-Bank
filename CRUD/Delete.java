package CRUD;

import java.sql.*;
        import java.util.Scanner;

public class Delete {
    private static final String DB_URL = "jdbc:sqlite:./Database/Database.db"; // Ensure correct path

    // Method to delete a bank by ID
    public static boolean deleteBank(int bankID) {
        String sql = "DELETE FROM Bank WHERE ID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bankID);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println(" Bank with ID " + bankID + " has been deleted successfully.");
                return true;
            } else {
                System.out.println("No bank found with ID: " + bankID);
            }
        } catch (SQLException e) {
            System.out.println(" Error deleting bank: " + e.getMessage());
        }

        return false;
    }

    // Main method for testing
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Bank ID to delete: ");
        while (!scanner.hasNextInt()) {
            System.out.println(" Invalid input. Please enter a valid Bank ID.");
            scanner.next();
        }
        int bankID = scanner.nextInt();
        scanner.nextLine(); // Clear buffer

        deleteBank(bankID);
        scanner.close();
    }
}
