package Bank;

import Accounts.*;
import java.sql.*;
import java.util.Scanner;

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

    /** Checks if a bank is currently logged in. */
    public static boolean isLogged() {
        return loggedBank != null;
    }

    /** Initializes bank interaction. */
    public static void bankInit() {
        System.out.println("Welcome to the Bank System!");
        System.out.println("1. Create New Bank\n2. Login to Existing Bank\n3. Exit");

        int choice = input.nextInt();
        input.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                createNewBank();
                break;
            case 2:
                System.out.print("Enter bank name: ");
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
            case 3:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option. Try again.");
        }
    }

    /** Menu for logged-in bank operations */
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

    /** Displays accounts of the logged-in bank, categorized by type. */
    private static void showAccounts() {
        System.out.print("Enter account type (Savings/Credit): ");
        String accountType = input.nextLine();
        loggedBank.showAccounts(accountType);
    }

    /** Handles creating a new account for the logged-in bank. */
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

    /** Handles bank login. */
    public static void bankLogin(String bankName, String passcode) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String query = "SELECT * FROM Bank WHERE Name = ? AND Passcode = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, bankName);
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

    /** Ends the current bank session. */
    private static void logout() {
        loggedBank = null;
        System.out.println("Logged out successfully.");
    }

    /** Creates a new bank record. */
    public static void createNewBank() {
        System.out.print("Enter bank name: ");
        String name = input.nextLine();
        System.out.print("Enter bank passcode: ");
        String passcode = input.nextLine();

        Bank newBank = new Bank(name, passcode);
        newBank.InsertBank();
    }

    /** Retrieves a bank from the database based on criteria. */
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

    /** Returns the number of registered banks. */
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
}
