package CRUD;

import Bank.Bank;
import java.sql.*;
import java.util.Scanner;
import Accounts.Account;

public class Update {
    public static void updateBank() {
        String url = "jdbc:sqlite:Database/Database.db"; // SQLite DB path

        try (Scanner scanner = new Scanner(System.in);
             Connection conn = DriverManager.getConnection(url)) {

            if (conn != null) {
                System.out.println("Connected to SQLite!");

                // Display menu options
                System.out.println("Select an option to update:\n1 - Name\n2 - Passcode\n3 - Deposit Limit\n4 - Withdraw Limit\n5 - Credit Limit\n6 - Processing Fee");

                System.out.print("Enter a number (1-6): ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                String bankIdentifier;
                if (choice == 1) {
                    System.out.print("Enter the Bank name to update: ");
                    bankIdentifier = scanner.nextLine();
                } else if (choice == 2) {
                    System.out.print("Enter the Bank passcode to update: ");
                    bankIdentifier = scanner.nextLine();
                } else {
                    System.out.print("Enter the Bank ID to update: ");
                    bankIdentifier = scanner.nextLine();
                }

                // Determine the SQL query based on input type
                String fetchSQL = (choice == 1) ? "SELECT * FROM Bank WHERE name = ?" :
                        (choice == 2) ? "SELECT * FROM Bank WHERE passcode = ?" :
                                "SELECT * FROM Bank WHERE ID = ?";

                Bank bank = null;

                try (PreparedStatement fetchStmt = conn.prepareStatement(fetchSQL)) {
                    fetchStmt.setString(1, bankIdentifier);
                    ResultSet rs = fetchStmt.executeQuery();
                    if (rs.next()) {
                        bank = new Bank(rs.getString("name"), rs.getString("passcode"));
                        bank.setBankID(rs.getInt("BankID"));
                        bank.setDepositLimit(rs.getDouble("DepositLimit"));
                        bank.setWithdrawLimit(rs.getDouble("WithdrawLimit"));
                        bank.setCreditLimit(rs.getDouble("CreditLimit"));
                        bank.setProcessingFee(rs.getDouble("processingFee"));
                    } else {
                        System.out.println("No bank found with the given identifier.");
                        return;
                    }
                }

                // Ask for new value
                System.out.print("Enter new value: ");
                String newValue = scanner.nextLine();

                // Apply the change using Bank setters
                switch (choice) {
                    case 1: bank.setName(newValue); break;
                    case 2: bank.setPasscode(newValue); break;
                    case 3: bank.setDepositLimit(Double.parseDouble(newValue)); break;
                    case 4: bank.setWithdrawLimit(Double.parseDouble(newValue)); break;
                    case 5: bank.setCreditLimit(Double.parseDouble(newValue)); break;
                    case 6: bank.setProcessingFee(Double.parseDouble(newValue)); break;
                    default:
                        System.out.println("Invalid selection! Please enter a number between 1 and 6.");
                        return;
                }

                // Prepare SQL statement to update
                String updateSQL = "UPDATE Bank SET name = ?, passcode = ?, DepositLimit = ?, WithdrawLimit = ?, CreditLimit = ?, processingFee = ? WHERE BankID = ?";

                try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
                    pstmt.setString(1, bank.getName());
                    pstmt.setString(2, bank.getPasscode());
                    pstmt.setDouble(3, bank.getDepositLimit());
                    pstmt.setDouble(4, bank.getWithdrawLimit());
                    pstmt.setDouble(5, bank.getCreditLimit());
                    pstmt.setDouble(6, bank.getProcessingFee());
                    pstmt.setInt(7, bank.getBankID());

                    int rowsUpdated = pstmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Record updated successfully!");
                    } else {
                        System.out.println("No record found with the given Bank identifier.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLite connection error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid numeric value.");
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
}
