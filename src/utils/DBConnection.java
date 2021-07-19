package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection class used to initiate connections to the MySQL database.
 *
 * @author Rodney Agosto
 */
public class DBConnection {

    // JBDC URL parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//www.test.com/123"; // ipAddress - Modified for securty purposes

    // JBDC URL
    private static final String jdbcURL = protocol + vendorName + ipAddress;

    // Driver and Connection Interface reference
    private static final String MYSQLJDBCDriver = "com.mysql.cj.jdbc.Driver";
    private static Connection conn = null;

    // private static final String username = "123123"; // Username - Modified for securty purposes
    // private static final String password = "123123"; // Password - Modified for securty purposes

    /**
     * Start connection used to start the connection to the database.
     * @return Start connection used to start the connection to the database.
     */
    public static Connection startConnection()
    {
        try
        {
            Class.forName(MYSQLJDBCDriver);
            conn = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection successfull!");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    /**
     * Close connection used to close the connection to the database.
     * @return Close connection used to close the connection to the database.
     */
    public static Connection closeConnection()
    {
        try
        {
            conn.close();
            System.out.println("Connection closed!");
        }
        catch (SQLException e)
        {
            System.out.println("Error: " + e.getMessage());
        }
        return conn;
    }


}
