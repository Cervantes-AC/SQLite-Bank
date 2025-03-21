package Bank;

import Accounts.*;
import java.sql.*;
import java.util.Scanner;
import Main.*;
/**
 * BankLauncher Class
 * Manages interactions with banks and accounts.
 *
 * Now integrates with SQLite for persistent data storage.
 */
public class BankLauncher {
    private static Bank loggedBank = null;
    private static final String DB_URL = "jdbc:sqlite:Database/Database.db";
    private static final Scanner input = new Scanner(System.in);

    /**
     * Checks if a bank is currently logged in.
     */
    public static boolean isLogged() {
        return loggedBank != null;
    }

    /**
     * Initializes bank interaction.
     */
    public static void bankInit() {
        Main.showMenu(3, 2);
        Main.setOption();

        switch (Main.getOption()) {
            case 1:
                ShowRegisteredBank();
                System.out.print("Enter Bank ID: ");
                String name = input.nextLine();
                System.out.print("Enter passcode: ");
                String passcode = input.nextLine();
                bankLogin(name, passcode);

                if (loggedBank != null) {
                    System.out.println("Successfully logged in!");
                    bankMenu();
                } else {
                    System.out.println("Invalid credentials.");
                }
                break;
            case 2:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option. Try again.");
        }
    }

    /**
     * Menu for logged-in bank operations
     */
    private static void bankMenu() {
        while (isLogged()) {
            System.out.println("\n1. Show Accounts\n2. Create New Account\n3. Logout");
            int choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    showAccounts();
                    break;
                case 2:
                    newAccount();
                    break;
                case 3:
                    logout();
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    /**
     * Displays accounts based on the selected type.
     */
    public static void showAccounts() {
        String query; // Ensure query is initialized
        Main.showMenu(32, 2);
        Main.setOption();

        switch (Main.getOption()) {
            case 1:
                Main.showMenuHeader("Credit Accounts");
                query = "SELECT * FROM CreditAccount WHERE BankID = ?";
                break;
            case 2:
                Main.showMenuHeader("Savings Accounts");
                query = "SELECT * FROM SavingsAccount WHERE BankID = ?";
                break;
            case 3:
                Main.showMenuHeader("All Accounts");
                query = "SELECT * FROM CreditAccount WHERE BankID = ? UNION SELECT * FROM SavingsAccount WHERE BankID = ?";
                break;
            default:
                System.out.println("Invalid account type.");
                return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, loggedBank.getBankID());
            if (Main.getOption() == 3) pstmt.setInt(2, loggedBank.getBankID());

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("User: " + rs.getString("FirstName") + " " + rs.getString("LastName"));
                System.out.println("Account Number: " + rs.getString("AccountID"));
            }
        } catch (SQLException e) {
            System.out.println("Error displaying accounts: " + e.getMessage());
        }
    }


    /**
     * Handles creating a new account for the logged-in bank.
     */
    private static void newAccount() {
        System.out.print("Enter first name: ");
        String firstName = input.nextLine();
        System.out.print("Enter last name: ");
        String lastName = input.nextLine();
        System.out.print("Enter email: ");
        String email = input.nextLine();
        System.out.print("Enter PIN: ");
        String pin = input.nextLine();

        System.out.print("Enter account type (Savings/Credit): ");
        String type = input.nextLine();

        System.out.print("Enter account type (Savings/Credit): ");
        String amount = input.nextLine();
        try {
            Account newAccount = new Account(loggedBank.getBankID(), type, firstName, lastName, email, pin);
            if (newAccount.insertAccount()) {
                System.out.println("Account created successfully!");
            } else {
                System.out.println("Failed to create account.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Handles bank login.
     */
    public static void bankLogin(String bankID, String passcode) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String query = "SELECT * FROM Bank WHERE BankID = ? AND Passcode = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, bankID);
                pstmt.setString(2, passcode);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    loggedBank = new Bank(
                            rs.getInt("BankID"),
                            rs.getString("Name"),
                            rs.getString("Passcode"),
                            rs.getDouble("DepositLimit"),
                            rs.getDouble("WithdrawLimit"),
                            rs.getDouble("CreditLimit"),
                            rs.getDouble("processingFee")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ends the current bank session.
     */
    private static void logout() {
        loggedBank = null;
        System.out.println("Logged out successfully.");
    }

    /**
     * Creates a new bank record.
     */
    public static void createNewBank() {
        System.out.print("Enter bank name: ");
        String name = input.nextLine();
        System.out.print("Enter bank passcode: ");
        String passcode = input.nextLine();

        Bank newBank = new Bank(name, passcode);
        newBank.InsertBank();
    }

    /**
     * Retrieves a bank from the database based on criteria.
     */
    public static Bank getBank(String name) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String query = "SELECT * FROM Bank WHERE Name = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, name);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return new Bank(
                            rs.getInt("BankID"),
                            rs.getString("Name"),
                            rs.getString("Passcode"),
                            rs.getDouble("DepositLimit"),
                            rs.getDouble("WithdrawLimit"),
                            rs.getDouble("CreditLimit"),
                            rs.getDouble("processingFee")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns the number of registered banks.
     */
    public static int bankSize() {
        int count = 0;
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String query = "SELECT COUNT(*) AS count FROM Bank";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                count = rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public static void ShowRegisteredBank() {
        String query;
        query = "SELECT BankID, Name FROM Bank";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            Main.showMenuHeader("Registered Bank");
            while (rs.next()) {
                int bankID = rs.getInt("BankID");
                String bankName = rs.getString("Name");

                System.out.printf("[%d] %s%n", bankID, bankName);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching registered banks: " + e.getMessage());
        }
    }
}
