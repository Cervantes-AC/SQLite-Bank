package Bank;

import Accounts.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

/**
 * BankLauncher Class
 * Manages interactions with banks and accounts.
 *
 * Now integrates with SQLite for persistent data storage.
 */
public class BankLauncher {
    private static Bank loggedBank = null;
    private Scanner input = new Scanner(System.in);
    private static final String DB_URL = "jdbc:sqlite:Database/Database.db";

    /** Checks if a bank is currently logged in. */
    public static boolean isLogged() {
        return loggedBank != null;
    }

    /** Initializes bank interaction. */
    public static void bankInit() {
        // TODO: COMPLETE THIS METHOD
    }

    /** Displays accounts of the logged-in bank, categorized by type. */
    private static void showAccounts() {
        // TODO: Implement account display with DB support
    }

    /** Handles creating a new account for the logged-in bank. */
    private static void newAccount() {
        // TODO: Implement new account creation with DB support
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
                    loggedBank = new Bank(rs.getInt("ID"), rs.getString("Name"), rs.getString("Passcode"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Sets the current logged-in bank. */
    private static void setLogSession(Bank b) {
        loggedBank = b;
    }

    /** Ends the current bank session. */
    private static void logout() {
        loggedBank = null;
    }

    /** Creates a new bank record. */
    public static void createNewBank() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter bank name: ");
        String name = scanner.nextLine();
        System.out.print("Enter bank passcode: ");
        String passcode = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String insertBank = "INSERT INTO Bank(Name, Passcode) VALUES(?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertBank)) {
                pstmt.setString(1, name);
                pstmt.setString(2, passcode);
                pstmt.executeUpdate();
                System.out.println("Bank created successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Displays a menu of all registered banks. */
    public static void showBanksMenu() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String query = "SELECT * FROM Bank";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("ID") + ", Name: " + rs.getString("Name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Retrieves a bank from the database based on criteria. */
    public static Bank getBank(String name) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String query = "SELECT * FROM Bank WHERE Name = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, name);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return new Bank(rs.getInt("ID"), rs.getString("Name"), rs.getString("Passcode"));
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
