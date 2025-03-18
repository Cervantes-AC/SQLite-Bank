package Launchers;

import Accounts.*;
import Bank.*;

/**
 * AccountLauncher Class
 * Manages interactions with the account module.
 *
 * Attributes:
 * - Account loggedAccount: The account object representing the currently logged-in account.
 * - Bank assocBank: The associated bank selected for the account module.
 *
 * Methods:
 * - AccountInit(): Initializes account interactions.
 * - isLoggedIn(): Checks if an account is currently logged in.
 * - accountLogin(): Handles account login after selecting a bank.
 * - selectBank(): Prompts the user to select a bank by ID before login.
 * - setLogSession(Account): Creates a session for the logged-in account.
 * - destroyLogSession(): Ends the current account session.
 * - checkCredentials(String, String): Verifies account number and PIN.
 * - getLoggedAccount(): Returns the currently logged-in account.
 */

public class AccountLauncher {
    private static Account loggedAccount;
    private static Bank assocBank;

    /** Initializes account interactions */
    public static void AccountInit() {
        // TODO: Implement account initialization
    }

    /** Checks if an account is currently logged in. */
    private static boolean isLoggedIn() {
        return loggedAccount != null;
    }

    /** Handles the account login process. Requires bank selection first. */
    public void accountLogin() {
        // TODO: Implement account login
    }

    /** Prompts the user to select a bank by ID. */
    private  static Bank selectBank() {
        return assocBank; // TODO: Implement bank selection
    }

    /** Sets the session for the logged-in account. */
    private void setLogSession(Account account) {
        this.loggedAccount = account;
    }

    /** Ends the current account session. */
    private static void destroyLogSession() {
        loggedAccount = null;
    }

    /**
     * Verifies the account number and PIN.
     * @param accountNum The account number.
     * @param pin The 4-digit PIN.
     * @return Account object if credentials are valid, null otherwise.
     */
    public static Account checkCredentials(String accountNum, String pin) {
        return loggedAccount; // TODO: Implement credential checking
    }

    /** Returns the currently logged-in account. */
    protected static Account getLoggedAccount() {
        return loggedAccount;
    }
}
