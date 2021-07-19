package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * DBQuery class used for prepare statements on the MySQL database.
 *
 * @author Rodney Agosto
 */
public class DBQuery {

    private static PreparedStatement statement; // Statement reference

    /**
     * Create Statement Object.
     * @return Create Statement Object.
     */
    public static void setPreparedStatement(Connection conn, String sqlStatement) throws SQLException
    {
        statement = conn.prepareStatement(sqlStatement);
    }

    /**
     * Return Statement Object
     * @return Return Statement Object
     */
    public static PreparedStatement getPreparedStatement()
    {
        return statement;
    }

}
