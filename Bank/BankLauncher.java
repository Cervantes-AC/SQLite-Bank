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
        Main.showMenuHeader("Bank Menu");
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
                    bankMenu();
                } else {
                    System.out.println("Invalid credentials.");
                }
                break;
            case 2:
                System.out.print("Returning to Main Menu\n");
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
            Main.showMenuHeader("Bank Menu");
            Main.showMenu(31);
            Main.setOption();
            switch (Main.getOption()) {
                case 1:
                    Main.showMenuHeader("View Accounts Menu");
                    showAccounts();
                    break;
                case 2:
                    Main.showMenuHeader("Create New Account");
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
                query = "SELECT AccountID, FirstName, LastName, 'Credit' AS AccountType FROM CreditAccount WHERE BankID = ?";
                break;
            case 2:
                Main.showMenuHeader("Savings Accounts");
                query = "SELECT AccountID, FirstName, LastName, 'Savings' AS AccountType FROM SavingsAccount WHERE BankID = ?";
                break;
            case 3:
                Main.showMenuHeader("Business Accounts");
                query = "SELECT AccountID, FirstName, LastName, 'Business' AS AccountType FROM BusinessAccount WHERE BankID = ?";
                break;
            case 4:
                Main.showMenuHeader("Educational Accounts");
                query = "SELECT AccountID, FirstName, LastName, 'Educational' AS AccountType FROM EducationalAccount WHERE BankID = ?";
                break;
            case 5:
                Main.showMenuHeader("All Accounts");
                query = "SELECT AccountID, FirstName, LastName, 'Credit' AS AccountType FROM CreditAccount WHERE BankID = ? " +
                        "UNION " +
                        "SELECT AccountID, FirstName, LastName, 'Savings' AS AccountType FROM SavingsAccount WHERE BankID = ? " +
                        "UNION " +
                        "SELECT AccountID, FirstName, LastName, 'Business' AS AccountType FROM BusinessAccount WHERE BankID = ? " +
                        "UNION " +
                        "SELECT AccountID, FirstName, LastName, 'Educational' AS AccountType FROM EducationalAccount WHERE BankID = ?";
                break;
            default:
                System.out.println("Go back to previous menu.");
                return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set parameters depending on the selected option
            if (Main.getOption() == 5) {
                // For case 5, all four queries will use the same BankID
                pstmt.setInt(1, loggedBank.getBankID());
                pstmt.setInt(2, loggedBank.getBankID());
                pstmt.setInt(3, loggedBank.getBankID());
                pstmt.setInt(4, loggedBank.getBankID());
            } else {
                // For other cases (1-4), only one parameter (BankID) is needed
                pstmt.setInt(1, loggedBank.getBankID());
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                // Retrieve and display the account type, and other information
                String accountType = rs.getString("AccountType");  // Correctly fetch the alias
                System.out.println("Account Type: " + accountType);
                System.out.println("User: " + rs.getString("FirstName") + " " + rs.getString("LastName"));
                System.out.println("Account Number: " + rs.getString("AccountID"));
                System.out.println("-----------------------------------");
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

        System.out.print("Enter account type (Savings/Credit/Business/Educational): ");
        String type = input.nextLine().toLowerCase(); // Convert to lowercase for case insensitivity

        double amount = 0; // Default value for initial amount

        // Handle different account types and their specific requirements
        if (type.equals("savings") || type.equals("credit") || type.equals("business") || type.equals("educational")) {

            // Ask for initial amount or loan based on account type
            if (type.equals("savings") || type.equals("educational")) {
                System.out.print("Enter initial balance: ");
                amount = input.nextDouble();
            }
            if (type.equals("credit") || type.equals("business")) {
                System.out.print("Enter loan amount: ");
                amount = input.nextDouble();
            }

            input.nextLine(); // Consume the newline character after entering amount

            // Create a general Account object based on the type
            Account newAccount = new Account(loggedBank.getBankID(), type, firstName, lastName, email, pin);

            // Insert the new account into the database and confirm success
            if (newAccount.insertAccount(amount)) {
                System.out.println(type.equals("savings") ? "Savings account created successfully!" :
                        type.equals("credit") ? "Credit account created successfully!" :
                                type.equals("business") ? "Business account created successfully!" :
                                        "Educational account created successfully!");
            } else {
                System.out.println("Failed to create " + type + " account.");
            }
        } else {
            System.out.println("Invalid account type. Please enter Savings, Credit, Business, or Educational.");
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

                    loggedInBankID = rs.getInt("BankID");
                    System.out.println("Successfully logged into Bank ID: " + loggedInBankID);
                } else {
                    System.out.println("Login failed! Invalid Bank ID or Passcode.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    /**
     * Ends the current bank session.
     */
    private static void logout() {
        loggedBank = null;
        loggedInBankID = -1; // Reset logged-in Bank ID
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
    public static int loggedInBankID = -1; // Default to -1 (not logged in)
}
