package Bank;
import Accounts.*;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * BankLauncher Class
 * Manages interactions with banks and accounts.
 *
 * Attributes:
 * - ArrayList<Bank> BANKS: List of banks currently registered.
 * - Bank loggedBank: Bank object representing the current logged-in bank. Null if no bank is logged in.
 *
 * Methods:
 * - bankInit(): Initializes bank interactions.
 * - showAccounts(): Displays accounts of the logged-in bank by type.
 * - newAccount(): Creates a new account for the logged-in bank.
 * - bankLogin(): Handles bank login.
 * - setLogSession(Bank): Sets the logged-in bank.
 * - logout(): Ends the current bank session.
 * - createNewBank(): Creates a new bank record.
 * - showBanksMenu(): Displays all registered banks.
 * - addBank(Bank): Adds a new bank to the list.
 * - getBank(Comparator<Bank>, Bank): Retrieves a bank based on criteria.
 * - findAccount(String): Finds an account across all banks by account number.
 * - bankSize(): Returns the number of registered banks.
 */

public class BankLauncher {
    private static ArrayList<Bank> BANKS = new ArrayList<>();
    private static Bank loggedBank = null;

    /** Checks if a bank is currently logged in. */
    public static boolean isLogged() {
        return loggedBank != null;
    }

    /** Initializes bank interaction. */
    public static void bankInit() {
        // TODO: Implement initialization
    }

    /** Displays accounts of the logged-in bank, categorized by type. */
    private static void showAccounts() {
        // TODO: Implement account display
    }

    /** Handles creating a new account for the logged-in bank. */
    private static void newAccount() {
        // TODO: Implement new account creation
    }

    /** Handles bank login. */
    public static void bankLogin() {
        // TODO: Implement bank login
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
        // TODO: Implement bank creation
    }

    /** Displays a menu of all registered banks. */
    public static void showBanksMenu() {
        // TODO: Implement bank menu display
    }

    /** Adds a new bank to the list of banks. */
    private static void addBank(Bank b) {
        BANKS.add(b);
    }

    /**
     * Retrieves a bank from the list based on a comparator and target bank.
     * @param comparator Criteria for comparing banks.
     * @param bank Target bank to search for.
     * @return Bank object if found, null otherwise.
     */
    public static Bank getBank(Comparator<Bank> comparator, Bank bank) {
        return BANKS.stream().filter(o -> o.equals(bank)).findFirst().orElse(null);
    }

    /**
     * Finds an account across all registered banks by account number.
     * @param accountNum Account number to search for.
     * @return Account object if found, null otherwise.
     */
    public static Account findAccount(String accountNum) {
        return null; // TODO: Implement account search
    }

    /** Returns the number of registered banks. */
    public static int bankSize() {
        return BANKS.size();
    }
}
