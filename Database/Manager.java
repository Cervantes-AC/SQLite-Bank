package Database;
import java.sql.*;

public class Manager {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:Database/Database.db";  // SQLite file path

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("Connected to SQLite!");

                // Define SQL statements for creating tables
                String[] sqlStatements = {
                        """
                    CREATE TABLE IF NOT EXISTS Bank (
                        BankID INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT UNIQUE NOT NULL,
                        passcode TEXT NOT NULL,
                        DepositLimit REAL DEFAULT 50000.0,
                        WithdrawLimit REAL DEFAULT 50000.0,
                        CreditLimit REAL DEFAULT 100000.0,
                        processingFee REAL DEFAULT 10.0
                    );
                    """,
                        """
                    CREATE TABLE IF NOT EXISTS SavingsAccount (
                        BankID INTEGER NOT NULL,
                        AccountID TEXT UNIQUE NOT NULL,
                        FirstName TEXT NOT NULL,
                        LastName TEXT NOT NULL,
                        Email TEXT NOT NULL,
                        PIN TEXT NOT NULL,
                        Balance REAL DEFAULT 0.0,
                        FOREIGN KEY (BankID) REFERENCES Bank(BankID) ON DELETE CASCADE
                    );
                    """,
                        """
                    CREATE TABLE IF NOT EXISTS CreditAccount (
                        BankID INTEGER NOT NULL,
                        AccountID TEXT UNIQUE NOT NULL,
                        FirstName TEXT NOT NULL,
                        LastName TEXT NOT NULL,
                        Email TEXT NOT NULL,
                        PIN TEXT NOT NULL,
                        Loan REAL DEFAULT 0.0,
                        FOREIGN KEY (BankID) REFERENCES Bank(BankID) ON DELETE CASCADE
                    );
                    """,
                        """
                    CREATE TABLE IF NOT EXISTS BusinessAccount (
                        BankID INTEGER NOT NULL,
                        AccountID TEXT UNIQUE NOT NULL,
                        FirstName TEXT NOT NULL,
                        LastName TEXT NOT NULL,
                        Email TEXT NOT NULL,
                        PIN TEXT NOT NULL,
                        Loan REAL DEFAULT 0.0,
                        FOREIGN KEY (BankID) REFERENCES Bank(BankID) ON DELETE CASCADE
                    );
                    """,
                        """
                    CREATE TABLE IF NOT EXISTS EducationalAccount (
                        BankID INTEGER NOT NULL,
                        AccountID TEXT UNIQUE NOT NULL,
                        FirstName TEXT NOT NULL,
                        LastName TEXT NOT NULL,
                        Email TEXT NOT NULL,
                        PIN TEXT NOT NULL,
                        Balance REAL DEFAULT 0.0,
                        FOREIGN KEY (BankID) REFERENCES Bank(BankID) ON DELETE CASCADE
                    );
                    """,
                        """
                    CREATE TABLE IF NOT EXISTS Transactions (
                        AccountID TEXT NOT NULL,
                        Type TEXT CHECK(Type IN ('Withdrawal', 'Deposit', 'Fund Transfer', 'Payment', 'Recompense')) NOT NULL,
                        Amount REAL NOT NULL,
                        Description TEXT,
                        Date TEXT DEFAULT (datetime('now', 'localtime')),
                        FOREIGN KEY (AccountID) REFERENCES SavingsAccount(AccountID) ON DELETE CASCADE,
                        FOREIGN KEY (AccountID) REFERENCES CreditAccount(AccountID) ON DELETE CASCADE,
                        FOREIGN KEY (AccountID) REFERENCES BusinessAccount(AccountID) ON DELETE CASCADE,
                        FOREIGN KEY (AccountID) REFERENCES EducationalAccount(AccountID) ON DELETE CASCADE
                    );
                    """,
                        // Adding indexes for faster lookups
                        "CREATE INDEX IF NOT EXISTS idx_savings_account ON SavingsAccount(AccountID);",
                        "CREATE INDEX IF NOT EXISTS idx_credit_account ON CreditAccount(AccountID);",
                        "CREATE INDEX IF NOT EXISTS idx_business_account ON BusinessAccount(AccountID);",
                        "CREATE INDEX IF NOT EXISTS idx_educational_account ON EducationalAccount(AccountID);",
                        "CREATE INDEX IF NOT EXISTS idx_transactions_account ON Transactions(AccountID);"
                };

                // Execute each statement separately
                try (Statement stmt = conn.createStatement()) {
                    for (String sql : sqlStatements) {
                        stmt.executeUpdate(sql);
                    }
                }

                System.out.println("All tables created successfully!");
            }
        } catch (SQLException e) {
            System.out.println("SQLite error: " + e.getMessage());
        }
    }
}
