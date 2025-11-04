package Banking;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    // This variable will hold the actual connection link
    static Connection con;

    // This method sets up and returns the database connection
    public static Connection getConnection() {
        try {
            // 1. Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. Connection details
            String url = "jdbc:mysql://localhost:3306/bank"; // Database URL
            String user = ""; 
            String pass = "";

            // 3. Create the connection
            con = DriverManager.getConnection(url, user, pass);

            // 4. Tell us if it worked
            System.out.println("Connection to database successful!");
        } 
        catch (Exception e) {
            System.out.println("Connection failed: " + e.getMessage());
        }

        // 5. Return the connection (or null if it failed)
        return con;
    }
}
