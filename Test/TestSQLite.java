//package Test;
//
//import java.sql.*;
//
//public class TestSQLite {
//    public static void main(String[] args) {
//        String url = "jdbc:sqlite:Database/Database.db";  // Path to your SQLite DB file
//
//        try (Connection conn = DriverManager.getConnection(url)) {
//            if (conn != null) {
//                System.out.println("Connected to SQLite!");
//
//                // SQL query to fetch all data from the 'Bank' table
//                String selectSQL = "SELECT * FROM Bank";
//
//                try (Statement stmt = conn.createStatement();
//                     ResultSet rs = stmt.executeQuery(selectSQL)) {
//
//                    // Loop through and display each row
//                    while (rs.next()) {
//                        System.out.printf("""
//                                ID: %d
//                                Name: %s
//                                Passcode: %s
//                                Deposit Limit: %.2f
//                                Withdraw Limit: %.2f
//                                Credit Limit: %.2f
//                                Processing Fee: %.2f
//                                ------------------------
//                                """,
//                                rs.getInt("BankID"),
//                                rs.getString("name"),
//                                rs.getString("passcode"),
//                                rs.getDouble("DepositLimit"),
//                                rs.getDouble("WithdrawLimit"),
//                                rs.getDouble("CreditLimit"),
//                                rs.getDouble("processingFee"));
//                    }
//
//                } catch (SQLException e) {
//                    System.out.println("Query error: " + e.getMessage());
//                }
//
//            }
//
//        } catch (SQLException e) {
//            System.out.println("SQLite connection error: " + e.getMessage());
//        }
//    }
//}
