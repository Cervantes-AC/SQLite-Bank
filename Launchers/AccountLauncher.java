package Launchers;

import Accounts.*;
import Transactions.IllegalAccountType;
import java.sql.*;
import java.util.Scanner;

public class AccountLauncher {
    protected static Account loggedAccount;
    private static final Scanner scanner = new Scanner(System.in);
    private static final String DB_URL = "jdbc:sqlite:Database/Database.db";

    public static void AccountInit() throws IllegalAccountType {
        System.out.println("Welcome to the Banking System!");

        while (true) {
            System.out.print("Enter Account ID: ");
            String accountID = scanner.nextLine().trim();

            System.out.print("Enter Account Passcode: ");
            String passcode = scanner.nextLine().trim();

            Account account = authenticateAccount(accountID, passcode);

            if (account != null) {
                setLogSession(account);
                System.out.println("\nLogin successful!");

                if (account instanceof CreditAccount) {
                    CreditAccountLauncher.creditAccountInit();
                } else if (account instanceof SavingsAccount) {
                    SavingsAccountLauncher.savingsAccountInit();
                } else {
                    System.out.println("Unknown account type. Logging out...");
                    destroyLogSession();
                }
                break;
            } else {
                System.out.println("Login failed. Invalid credentials. Try again.");
            }
        }
    }

    private static Account authenticateAccount(String accountID, String passcode) {
        String sqlSavings = "SELECT * FROM SavingsAccount WHERE AccountID = ? AND PIN = ?";
        String sqlCredit = "SELECT * FROM CreditAccount WHERE AccountID = ? AND PIN = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmtSavings = conn.prepareStatement(sqlSavings);
             PreparedStatement pstmtCredit = conn.prepareStatement(sqlCredit)) {

            // Check if it's a Savings Account
            pstmtSavings.setString(1, accountID);
            pstmtSavings.setString(2, passcode);
            try (ResultSet rs = pstmtSavings.executeQuery()) {
                if (rs.next()) {
                    return new SavingsAccount(accountID);  // Uses SavingsAccount constructor
                }
            }

            // Check if it's a Credit Account
            pstmtCredit.setString(1, accountID);
            pstmtCredit.setString(2, passcode);
            try (ResultSet rs = pstmtCredit.executeQuery()) {
                if (rs.next()) {
                    return new CreditAccount(accountID);
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return null;
    }

    private static void setLogSession(Account account) {
        loggedAccount = account;
    }

    public static Account getLoggedAccount() {
        return loggedAccount;
    }

    public static void destroyLogSession() {
        loggedAccount = null;
        System.out.println("Successfully logged out.");
    }

    public static boolean isLoggedIn() {
        return loggedAccount != null;
    }
}
