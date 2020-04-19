package GameDatabase;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @Description  MySQL connection Utilities
 * @author WenhanWei
 * @date 2020/2/12
 */
public class JDBCUtils {

    /**
     *
     * Connect to Database
     *
     */

    public static Connection getConnection(){
        Connection connection = null;
        try {

            //1.read properties file
            InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("GameDatabase/jdbc.properties");
            Properties properties = new Properties();
            properties.load(in);

            String user = properties.getProperty("user");
            String password = properties.getProperty("password");
            String url = properties.getProperty("url");
            String driver = properties.getProperty("driver");

            //2.load driver
            Class.forName(driver);

            //3.get connection
            connection = DriverManager.getConnection(url,user,password);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return connection;
    }



    /**
     *
     * Close System Resources
     *
     */
    public static void closeResource(Connection connection, PreparedStatement preparedStatement){

        try{
            if (connection != null){
                connection.close();
            }
        }catch (SQLException e){
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }

        try{
            if (preparedStatement != null){
                preparedStatement.close();
            }
        }catch (SQLException e){
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }

}
