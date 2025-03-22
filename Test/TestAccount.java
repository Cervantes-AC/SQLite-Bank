//package Test;
//
//import java.sql.*;
//
//public class TestAccount {
//    public static void main(String[] args) {
//        String url = "jdbc:sqlite:Database/Database.db";  // Path to your SQLite DB file
//
//        try (Connection conn = DriverManager.getConnection(url)) {
//            if (conn != null) {
//                System.out.println("Connected to SQLite!");
//
//                // SQL query to fetch all data from the 'Account' table
//                String selectSQL = "SELECT * FROM Account";
//
//                try (Statement stmt = conn.createStatement();
//                     ResultSet rs = stmt.executeQuery(selectSQL)) {
//
//                    // Loop through and display each row
//                    while (rs.next()) {
//                        System.out.printf("""
//                                Account ID: %s
//                                Bank ID: %d
//                                Type: %s
//                                First Name: %s
//                                Last Name: %s
//                                Email: %s
//                                PIN: %s
//                                ------------------------
//                                """,
//                                rs.getString("AccountID"),
//                                rs.getInt("BankID"),
//                                rs.getString("AccountType"),
//                                rs.getString("FirstName"),
//                                rs.getString("LastName"),
//                                rs.getString("Email"),
//                                rs.getString("PIN"));
//                    }
//
//                } catch (SQLException e) {
//                    System.out.println("Query error: " + e.getMessage());
//                }
//            }
//
//        } catch (SQLException e) {
//            System.out.println("SQLite connection error: " + e.getMessage());
//        }
//    }
//}
