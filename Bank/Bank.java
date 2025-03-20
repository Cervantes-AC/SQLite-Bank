package Bank;

import java.sql.*;

public class Bank {
    private final int ID;
    private final String name;
    private final String passcode;
    private double DepositLimit, WithdrawLimit, CreditLimit;
    private double processingFee;

    private static final String DB_URL = "jdbc:sqlite:Database/Database.db";

    public Bank(int ID, String name, String passcode) {
        this.ID = ID;
        this.name = name;
        this.passcode = passcode;
    }

    public Bank(int ID, String name, String passcode, double DepositLimit, double WithdrawLimit, double CreditLimit, double processingFee) {
        this.ID = ID;
        this.name = name;
        this.passcode = passcode;
        this.DepositLimit = DepositLimit;
        this.WithdrawLimit = WithdrawLimit;
        this.CreditLimit = CreditLimit;
        this.processingFee = processingFee;
    }

    // Connect to database
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    // Save bank to database
    public void saveToDB() {
        String query = "INSERT INTO Bank (BankID, Name, Passcode, DepositLimit, WithdrawLimit, CreditLimit, ProcessingFee) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, this.ID);
            pstmt.setString(2, this.name);
            pstmt.setString(3, this.passcode);
            pstmt.setDouble(4, this.DepositLimit);
            pstmt.setDouble(5, this.WithdrawLimit);
            pstmt.setDouble(6, this.CreditLimit);
            pstmt.setDouble(7, this.processingFee);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Load a bank from the database
    public static Bank loadFromDB(int bankID) {
        String query = "SELECT * FROM Bank WHERE BankID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bankID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Bank(
                        rs.getInt("ID"),
                        rs.getString("Name"),
                        rs.getString("Passcode"),
                        rs.getDouble("DepositLimit"),
                        rs.getDouble("WithdrawLimit"),
                        rs.getDouble("CreditLimit"),
                        rs.getDouble("ProcessingFee")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Check if bank exists
    public static boolean bankExists(int bankID) {
        String query = "SELECT BankID FROM Bank WHERE BankID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bankID);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", passcode='" + passcode + '\'' +
                ", DepositLimit=" + DepositLimit +
                ", WithdrawLimit=" + WithdrawLimit +
                ", CreditLimit=" + CreditLimit +
                ", processingFee=" + processingFee +
                '}';


        public <T > void showAccounts (Class < T > accountType) {
            // TODO: Complete this method
        }

        public Account getBankAccount () {
            // TODO: Implement credit recompense processing
            return null;
        }

        /**
         * Captures user input to create a new account.
         * @return ArrayList of Field objects representing the account details.
         */
        public ArrayList<Field<String, ?>> createNewAccount () {
            // TODO: Complete this method
            return null;
        }

        /**
         * Creates a new credit account.
         * @return New CreditAccount object.
         */
        public CreditAccount createNewCreditAccount () {
            // TODO: Implement credit recompense processing
            return null;
        }

        /**
         * Creates a new savings account.
         * @return New SavingsAccount object.
         */
        public SavingsAccount createNewSavingsAccount () {
            // TODO: Implement credit recompense processing
            return null;
        }

        /**
         * Adds a new account to the bank if the account number doesn't already exist.
         * @param account Account object to be added.
         */
        public void addNewAccount (Account account){
            // TODO: Implement account addition
        }

        /**
         * Checks if an account exists in the specified bank by account number.
         * @param bank Bank to check.
         * @param accountNum Account number to find.
         * @return True if the account exists, false otherwise.
         */
        public boolean accountExists (Bank bank, String accountNum){
            // TODO: Implement credit recompense processing
            return false;
        }
    }
}

