package testGameDatabase;

import GameDatabase.JDBCUtils;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * JDBCUtils Tester.
 *
 * @author WenhanWei
 * @version 1.0
 */
public class JDBCUtilsTest {
    Connection connection = null;
    PreparedStatement preparedStatement = null;

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getConnection()
     */
    @Test
    public void testGetConnection() throws Exception {
        //TODO: Test goes here...
        connection = JDBCUtils.getConnection();
        preparedStatement = connection.prepareStatement("select * from players");
    }

    /**
     * Method: closeResource(Connection connection, PreparedStatement preparedStatement)
     */
    @Test
    public void testCloseResource() throws Exception {
        //TODO: Test goes here...
        JDBCUtils.closeResource(connection,preparedStatement);
    }


} 
