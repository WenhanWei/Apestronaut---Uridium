package GameDatabase;


import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author WenhanWei
 * @date 2020/2/12
 */
public class GameAccountOperations {

    public static String playerUsername = null;

    /**
     *
     * Try to get sign in player's MultiPlayerScore
     * @param Username Player's Username
     * @return int score
     *
     * */

    public static int getScore(String Username){
        int score = -1;

        if(whetherAccountExist(Username)){
            String sqlSelect = "SELECT multiPlayerScore FROM players WHERE username = ?";
            List<PlayerAccount> player = queryForPlayerAccounts(sqlSelect, Username);
            score = player.get(0).getMultiPlayerScore();
        }else{
            System.out.println("Error");
        }

        return score;
    }

    /**
     *
     * Try to Sign In
     * @param Username Player's Username
     * @param Password Password
     * @return true or false
     *
     * */

    public static boolean trySignIn(String Username,String Password){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Boolean b = false;
        try {
            connection = JDBCUtils.getConnection();

            String sql ="select password from players where username = ?"; // ? means A placeholder
            preparedStatement =connection.prepareStatement(sql);

            preparedStatement.setString(1,Username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                String selectedPassword= resultSet.getString(1);

                if (checkPassword(Password,selectedPassword)){
                    b = true;
                    playerUsername = Username;
                    System.out.println("Sign In successfully");

                }else{
                    b = false;
                    System.out.println("Wrong Username or Password!Try Again!");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(connection,preparedStatement);
        }
        return b;
    }

    /**
     *
     * Try to Sign Up
     * @param Username Player's Username
     * @param Password Password
     * @param Authenticate Player's Birthday
     * @return true or false
     *
     * */

    public static boolean trySignUp(String Username,String Password,String Authenticate){

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Boolean b = false;


        try {
            connection = JDBCUtils.getConnection();

            if(whetherAccountExist(Username)){

                b = false;
                System.out.println("Username is used, change it and try again!");

            }else{
                Date authenticate =Date.valueOf(Authenticate);
                String sqlInsert ="insert into players(username,password,authenticate)values(?,?,?)"; // ? --> A placeholder
                preparedStatement =connection.prepareStatement(sqlInsert);

                preparedStatement.setString(1,Username);
                preparedStatement.setString(2,EncoderByMd5(Password));
                preparedStatement.setDate(3,authenticate);

                preparedStatement.execute();
                b = true;

                System.out.println("Sign Up successfully");

            }


        }catch (IllegalArgumentException e){
            System.out.println("Wrong Date Format! Should be yyyy-MM-dd");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection,preparedStatement);
        }

        return b;
    }

    /**
     *
     * Try to Reset Password
     * @param Username Player's Username
     * @param Authenticate Player's birthday
     * @param newPassword New Password
     * @return true or false
     *
     * */

    public static boolean tryResetPassword(String Username,String Authenticate,String newPassword){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Boolean b = false;
        Date authenticate = null;

        try {
            connection = JDBCUtils.getConnection();

            //authenticate first
            String sqlAuthenticate = "select authenticate from players where username = ? ";
            preparedStatement =connection.prepareStatement(sqlAuthenticate);
            preparedStatement.setString(1,Username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){

                Date selectedAuthenticate= resultSet.getDate(1);

                authenticate =Date.valueOf(Authenticate);

                if(selectedAuthenticate.toString().equals(authenticate.toString())){
                    //real reset
                    String sql ="update players set password = ? where username = ?"; // ? means A placeholder
                    preparedStatement =connection.prepareStatement(sql);

                    preparedStatement.setString(1,EncoderByMd5(newPassword));
                    preparedStatement.setString(2,Username);

                    preparedStatement.execute();

                    System.out.println("Reset password successfully!");

                    b = true;

                }else{
                    System.out.println("Wrong Date!");
                }

            }else{
                System.out.println("Account does not exist! Fail to Update!");
            }

        } catch (IllegalArgumentException e){
            System.out.println("Wrong Date Format! Should be yyyy-MM-dd");
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection,preparedStatement);
        }

        return b;
    }
    /**
     *
     * Try to Add SinglePlayer PlayerScore
     * @param Username Player's Username
     * @param singlePlayerScore Score that a player gains in a battle
     * @return true or false
     *
     * */

    public static boolean tryAddSinglePlayerScores(int singlePlayerScore,String Username){

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Boolean b = false;

        try {
            connection = JDBCUtils.getConnection();

            String sqlSelect = "select singlePlayerScore from players where username = ?";
            preparedStatement =connection.prepareStatement(sqlSelect);
            preparedStatement.setString(1,Username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                int originScore = resultSet.getInt(1);

                String sqlUpdate = " update players set singlePlayerScore = ? where username = ?";// ? means A placeholder
                preparedStatement =connection.prepareStatement(sqlUpdate);
                preparedStatement.setInt(1,(singlePlayerScore+originScore));
                preparedStatement.setString(2,Username);

                preparedStatement.execute();
                b = true;

            }else{
                System.out.println("Account does not exist! Fail to Update!");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(connection,preparedStatement);
        }

        return b;
    }

    /**
     *
     * Try to Add MultiPlayer PlayerScore
     * @param Username Player's Username
     * @param multiPlayerScore Score that a player gains in a battle
     * @return true or false
     *
     * */


    public static boolean tryAddMultiPlayerScore(int multiPlayerScore,String Username){

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Boolean b = false;

        try {
            connection = JDBCUtils.getConnection();

            String sqlSelect = "select multiPlayerScore from players where username = ?";
            preparedStatement =connection.prepareStatement(sqlSelect);
            preparedStatement.setString(1,Username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                int originScore = resultSet.getInt(1);

                String sqlUpdate = " update players set multiPlayerScore = ? where username = ?";// ? means A placeholder
                preparedStatement =connection.prepareStatement(sqlUpdate);
                preparedStatement.setInt(1,(multiPlayerScore+originScore));
                preparedStatement.setString(2,Username);

                preparedStatement.execute();
                b = true;
            }else{
                System.out.println("Account does not exist! Fail to Update!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(connection,preparedStatement);
        }

        return b;
    }

    /**
     *
     * Try to update highest level
     * @param Username Player's Username
     * @param HighestLevel Highest Level that a player reach
     * @return true or false
     *
     * */

    public static boolean tryUpdateHighestLevel(int HighestLevel,String Username){

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Boolean b = false;

        try {

            connection = JDBCUtils.getConnection();

            if(whetherAccountExist(Username)){
                String sqlUpdate = " update players set highestLevel = ? where username = ?";// ? means A placeholder
                preparedStatement =connection.prepareStatement(sqlUpdate);
                preparedStatement.setInt(1,HighestLevel);
                preparedStatement.setString(2,Username);

                preparedStatement.execute();

                b = true;
            }else {
                b = false;
                System.out.println("Fail to Update!");
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(connection,preparedStatement);
        }

        return b;
    }

    /**
     *
     * Try to Get HighestLevel that a Player reach
     * @param Username Player's Username
     * @return a PlayAccount List containing the required information
     *
     */

    public static List<PlayerAccount> tryGetHighestLevel(String Username){

        List<PlayerAccount> highestLevel;

        String sqlSelect = "select highestLevel from players where username = ?";// ? means A placeholder

        highestLevel = queryForPlayerAccounts(sqlSelect,Username);

        return highestLevel;
    }

    /**
     *
     * Try to Show SinglePlayer PlayerScore
     * @return a PlayAccount List containing the required information
     *
     * */

    public static List<PlayerAccount> tryShowSinglePlayerScore(){

        List<PlayerAccount> top10;

        String sqlSelect = "select username,singlePlayerScore from players order by singlePlayerScore DESC limit 10";// ? means A placeholder

        top10 = queryForPlayerAccounts(sqlSelect);

        return top10;
    }

    /**
     *
     * Try to Show MultiPlayer PlayerScore
     * @return a PlayAccount List containing the required information
     *
     * */

    public static List<PlayerAccount> tryShowMultiPlayerScore(){

        List<PlayerAccount> top10;

        String sqlSelect = "select username,multiPlayerScore from players order by multiPlayerScore DESC limit 10";

        top10 = queryForPlayerAccounts(sqlSelect);

        return top10;
    }

    /**
     *
     * Try to Show All Kinds Of PlayerScore
     * @return a PlayAccount List containing the required information
     *
     */

    public static List<PlayerAccount> tryShowAllKindsOfPlayerScore(){


        List<PlayerAccount> top10;

        String sqlSelect = "SELECT username,singlePlayerScore,multiPlayerScore FROM players ORDER BY multiPlayerScore DESC,singlePlayerScore DESC LIMIT 10";

        top10 = queryForPlayerAccounts(sqlSelect);

        return top10;
    }

//    /**
//     *
//     * Try to upload a avatar
//     * @param Username Player's Username
//     * @param pathToAvatar Image file path
//     * @return true or false
//     *
//     * */
//    public static boolean tryUploadAvatar(String Username, String pathToAvatar){
//        Connection connection = null;
//        PreparedStatement preparedStatement = null;
//        FileInputStream input = null;
//        Boolean b = false;
//        try {
//            connection = JDBCUtils.getConnection();
//            String AvatarSQL = "update players set avatar = ? where username = ? ";
//            preparedStatement = connection.prepareStatement(AvatarSQL);
//
//            File imageFile = new File(pathToAvatar);
//
//            if(imageFile.exists()) {
//
//                if(checkFileType(imageFile)){
//
//                    if(imageFile.length() <= 65535){
//
//                        input = new FileInputStream(imageFile);
//                        preparedStatement.setBlob(1,input);
//                        preparedStatement.setString(2,Username);
//
//                        preparedStatement.execute();
//                        System.out.println("Upload Succeed!");
//                        b = true;
//                    }else{
//                        System.out.println("Avatar is too big! Image Max Size is 65KB! Your image size is :"+imageFile.length()+" Bytes");
//                    }
//
//                }else{
//                    System.out.println("Please upload a image of the following file type: BMP/GIF/JPG/PNG");
//                }
//            }else {
//                System.out.println("File does not exists");
//            }
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            try {
//                if(input!=null){
//                    input.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            JDBCUtils.closeResource(connection,preparedStatement);
//        }
//
//        return b;
//    }
//
//    /**
//     *
//     * Try to download a avatar image
//     * @param Username Player's Username
//     * @return true or false
//     *
//     * */
//
//    public static boolean tryDownloadAvatar(String Username){
//        Connection connection = null;
//        PreparedStatement preparedStatement = null;
//        Boolean b = false;
//        InputStream inputStream = null;
//        FileOutputStream fileOutputStream = null;
//
//        try {
//            connection = JDBCUtils.getConnection();
//
//            String sql ="select avatar from players where username = ?"; // ? means A placeholder
//            preparedStatement =connection.prepareStatement(sql);
//
//            preparedStatement.setString(1,Username);
//
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            if(resultSet.next()){
//                Blob avatar= resultSet.getBlob(1);
//                try {
//                    inputStream = avatar.getBinaryStream();
//                    fileOutputStream = new FileOutputStream("src/Download/avatar"+getRandomFileName()+".jpg");
//                    byte[] buffer = new byte[1024];
//                    int length;
//                    while((length = inputStream.read(buffer)) != -1){
//                        fileOutputStream.write(buffer,0,length);
//                    }
//                    System.out.println("Download Succeed");
//                    b = true;
//                }catch (Exception e){
//                    System.out.println("Error! You might didn't upload your avatar.");
//                    e.printStackTrace();
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//
//            try {
//                if(inputStream!=null){
//                    inputStream.close();
//                }
//                if(fileOutputStream!=null){
//                    fileOutputStream.close();
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            JDBCUtils.closeResource(connection,preparedStatement);
//        }
//
//        return b;
//    }


    /**
     *
     * Common Account Query Method, can be used to search different accounts
     * @param sql The SQL statement you want to execute
     * @param args Fill all placeholders inside your sql statement
     * @return a PlayAccount List containing the required information
     * */
    public static List<PlayerAccount> queryForPlayerAccounts(String sql,Object...args){
        Connection connection = null;
        PreparedStatement preparedStatement= null;
        ResultSet resultSet;
        List<PlayerAccount> accountList = new ArrayList<>();

        try {
            //Get the connection to the database
            connection = JDBCUtils.getConnection();

            //Prepare statement
            preparedStatement = connection.prepareStatement(sql);

            //Fill in placeholders
            for(int i = 0;i < args.length;i++){
                preparedStatement.setObject(i + 1, args[i]);
            }
            //execute sql
            resultSet = preparedStatement.executeQuery();

            //Get the metadata for the result set
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            //Get columns number
            int columnCount =resultSetMetaData.getColumnCount();

            while(resultSet.next()){
                PlayerAccount playerAccount = new PlayerAccount();

                for(int i = 0;i < columnCount; i++){

                   Object columnValue = resultSet.getObject(i+1);

                   String columnName = resultSetMetaData.getColumnName(i+1);

                   Field field = PlayerAccount.class.getDeclaredField(columnName);
                   field.setAccessible(true);
                   field.set(playerAccount,columnValue);

                }

                accountList.add(playerAccount);
            }
            return accountList;

        } catch (Exception e) {

            e.printStackTrace();

        }finally{

            JDBCUtils.closeResource(connection, preparedStatement);
        }

        return null;
    }

    /**
     *
     * Try to find out whether an Account is exist
     * @param Username Player's Username
     * @return true or false
     *
     * */

    public static boolean whetherAccountExist(String Username){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Boolean b = false;


        try {
            connection = JDBCUtils.getConnection();

            String sqlSelect = "select username from players where username = ?";// ? means A placeholder
            preparedStatement =connection.prepareStatement(sqlSelect);
            preparedStatement.setString(1,Username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                b = true;
                //System.out.println("Username is used, account is exist!");
            }else{
                b = false;
                //System.out.println("Username is not used, account doesn't exist!");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            //close resource
            JDBCUtils.closeResource(connection, preparedStatement);
        }

        return b;

    }

    /**
     * Use MD5 for encryption
     * @param password  Password to be encrypted
     * @return  The encrypted password
     */
    private static String EncoderByMd5(String password) {
        MessageDigest md5;
        String afterEncrypt=null;

        try {
            md5 = MessageDigest.getInstance("MD5");

            byte[] messageDigest = md5.digest(password.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            afterEncrypt = no.toString(16);
            while (afterEncrypt.length() < 32) {
                afterEncrypt = "0" + afterEncrypt;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return afterEncrypt;
    }

    /**
     * Determine if the user password is correct
     * @param enteredPassword  The password entered by the user
     * @param password  The password stored in the database
     * @return true or false
     */
    private static boolean checkPassword(String enteredPassword,String password){

        if(EncoderByMd5(enteredPassword).equals(password)){
            return true;
        } else{
            return false;
        }
    }

//    /**
//     *
//     * Check if the file type is a picture
//     * @param file Player's Avatar File
//     * @return true or false
//     *
//     * */
//    private static boolean checkFileType(File file){
//        try {
//            Image image = ImageIO.read(file);
//            return image != null;
//        } catch(Exception e) {
//            return false;
//        }
//    }
//
//    /**
//     *
//     * Generate random file names
//     * @return Random File Name
//     *
//     * */
//
//    private static String getRandomFileName() {
//
//        SimpleDateFormat simpleDateFormat;
//        simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
//
//        java.util.Date date = new java.util.Date();
//        String now = simpleDateFormat.format(date);
//
//        Random random = new Random();
//        int randomNum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;
//
//        return randomNum + now;
//    }

}
