package Transactions;

/**
 * Class representing a Transaction.
 * Contains transaction details such as account number, type, amount, description, and date.
 */
public class Transaction {

    /**
     * Enum representing different types of transactions.
     */
    public enum TransactionType {
        WITHDRAWAL,
        DEPOSIT,
        FUND_TRANSFER,
        PAYMENT,
        RECOMPENSE
    }

    private String accountNumber;
    private TransactionType transactionType;
    private double amount;
    private String description;
    private String date;

    /**
     * Constructor to create a new Transaction object.
     *
     * @param accountNumber The account number linked to this transaction.
     * @param amount The amount involved in the transaction.
     * @param transactionType The type of transaction.
     * @param description A short description of the transaction.
     */
    public Transaction(String accountNumber, String transactionType, double amount, String description) {
        this.accountNumber = accountNumber;
        this.transactionType = TransactionType.valueOf(transactionType);
        this.amount = amount;
        this.description = description;
        this.date = java.time.LocalDateTime.now().toString();
    }

    // Getters
    public String getAccountNumber() { return accountNumber; }
    public double getAmount() { return amount; }
    public TransactionType getTransactionType() { return transactionType; }
    public String getDescription() { return description; }
    public String getDate() { return date; }

    // Setters
    public void setAmount(double amount) { this.amount = amount; }
    public void setDescription(String description) { this.description = description; }

    /**
     * Returns a string representation of the transaction.
     *
     * @return A formatted string with transaction details.
     */
    @Override
    public String toString() {
        return "Transaction{" +
                "AccountNumber='" + accountNumber + '\'' +
                ", Type='" + transactionType + '\'' +
                ", Amount=" + amount +
                ", Description='" + description + '\'' +
                ", Date='" + date + '\'' +
                '}';
    }
}
