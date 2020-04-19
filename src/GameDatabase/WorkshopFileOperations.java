package GameDatabase;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.lang.reflect.Field;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author WenhanWei
 * @date 2020/3/24 - 2020
 */
public class WorkshopFileOperations {

    /**
     *
     * Try to download a xml level file -- search by author name
     * @param Author Player's Username
     * @param pathToXML File path
     * @return true or false
     *
     * */
    public static boolean tryUploadXMLFILE(String Author,String FileName, String pathToXML){

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        FileInputStream input = null;
        Boolean b = false;
        try {
            connection = JDBCUtils.getConnection();
            String sqlSelect = "select filename from workshop where filename = ?";// ? means A placeholder
            preparedStatement =connection.prepareStatement(sqlSelect);
            preparedStatement.setString(1,FileName);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){

                b = false;
                System.out.println("Filename is used, change it and try again!");

            }else{
                File xmlFile = new File(pathToXML);
                if(xmlFile.exists()) {
                    if(checkFileType(xmlFile)){
                        if(xmlFile.length() <= 16777215){

                            String xmlSQL = "insert into workshop(author, filename, levelFile) values (?,?,?)";
                            preparedStatement = connection.prepareStatement(xmlSQL);

                            try{
                                input = new FileInputStream(xmlFile);
                                preparedStatement.setString(1,Author);
                                preparedStatement.setString(2,FileName);
                                preparedStatement.setBlob(3,input);

                                preparedStatement.execute();
                                System.out.println("Upload Succeed!");
                                b = true;
                            }catch (Exception e){
                                System.out.println("Error! Upload Fail! Try again!");
                            }

                        }else{
                            System.out.println("Your file is too big! Max Size is 16MB! Your file size is :"+xmlFile.length()+" Bytes");
                        }
                    }else{
                        System.out.println("Please upload xml files");
                    }
                }else {
                    System.out.println("File does not exists");
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(input!=null){
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            JDBCUtils.closeResource(connection,preparedStatement);
        }

        return b;
    }
    /**
     *
     * Try to search a xml level file -- search by author name
     * @param Author Player's Username
     * @return true or false
     *
     * */
    public static List<XMLFileDetails> trySearchXMLFILEbyAuthorName(String Author){

        String AuthorSQL ="select author,filename from workshop where author like ?"; // ? means A placeholder
        List<XMLFileDetails> xmlFileDetailsList = queryForXMLFileDetails(AuthorSQL, "%" + Author + "%");

        return xmlFileDetailsList;
    }

    /**
     *
     * Try to search a xml level file -- search by File name
     * @param FileName File name
     * @return true or false
     *
     * */
    public static List<XMLFileDetails> trySearchXMLFILEbyFileName(String FileName){

        String FilenameSQL ="select author,filename from workshop where filename like ?"; // ? means A placeholder
        List<XMLFileDetails> xmlFileDetailsList = queryForXMLFileDetails(FilenameSQL, "%" + FileName + "%");

        return xmlFileDetailsList;
    }

    /**
     *
     * Try to search a xml level file
     * @param name  author or file name
     * @return true or false
     *
     * */
    public static List<XMLFileDetails> trySearchXMLFILEbyAuthorANDFileName(String name){

        String SQL ="select author,filename from workshop where filename like ? or author like ? limit 10"; // ? means A placeholder
        List<XMLFileDetails> xmlFileDetailsList = queryForXMLFileDetails(SQL, "%"+ name + "%","%"+ name + "%");

        return xmlFileDetailsList;
    }

    /**
     *
     * Try to download all xml level files that author create
     * @param Author Player's Username
     * @return true or false
     *
     * */
    public static boolean tryDownloadXMLFILESbyAuthorName(String Author){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Boolean b = false;
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;

        try {
            connection = JDBCUtils.getConnection();

            String AuthorSQL ="select levelfile from workshop where author = ?"; // ? means A placeholder

            preparedStatement = connection.prepareStatement(AuthorSQL);

            preparedStatement.setString(1, Author);

            ResultSet resultSet = preparedStatement.executeQuery();

            //Get the metadata for the result set
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            //Get columns number
            int columnCount =resultSetMetaData.getColumnCount();

            while (resultSet.next()){
                for(int i = 0;i < columnCount; i++){

                    try {
                        Blob avatar= resultSet.getBlob(i+1);
                        inputStream = avatar.getBinaryStream();
                        fileOutputStream = new FileOutputStream("src/Download/"+Author+"xmlFile"+getRandomFileName()+".xml");
                        byte[] buffer = new byte[1024];
                        int length;
                        while((length = inputStream.read(buffer)) != -1){
                                fileOutputStream.write(buffer,0,length);
                        }

                        System.out.println("Download Succeed");
                        b = true;
                    }catch (Exception e){
                        System.out.println("Error! Download Fail! Check Author Name");
                    }

                    }
                }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            try {
                if(inputStream!=null){
                    inputStream.close();
                }
                if(fileOutputStream!=null){
                    fileOutputStream.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            JDBCUtils.closeResource(connection,preparedStatement);
        }

        return b;
    }

    /**
     *
     * Try to download a xml level file
     * @param fileName File name
     * @return true or false
     *
     * */
    public static boolean tryDownloadXMLFILEbyFileName(String fileName){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Boolean b = false;
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;

        try {
            connection = JDBCUtils.getConnection();

            String fileNameSQL ="select levelfile from workshop where filename = ?"; // ? means A placeholder

            preparedStatement =connection.prepareStatement(fileNameSQL);

            preparedStatement.setString(1, fileName);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                try {
                Blob avatar= resultSet.getBlob(1);
                inputStream = avatar.getBinaryStream();
                fileOutputStream = new FileOutputStream("src/Download/"+fileName+".xml");
                byte[] buffer = new byte[1024];
                int length;
                while((length = inputStream.read(buffer)) != -1){
                    fileOutputStream.write(buffer,0,length);
                }

                System.out.println("Download Succeed");
                b = true;
                }catch (Exception e){
                    System.out.println("Error! Download Fail! Check Filename");
                }
            }else{
                System.out.println("No file exist.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            try {
                if(inputStream!=null){
                    inputStream.close();
                }
                if(fileOutputStream!=null){
                    fileOutputStream.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            JDBCUtils.closeResource(connection,preparedStatement);
        }
        return b;
    }

    /**
     *
     * Common Account Query Method, can be used to search different files
     * @param sql The SQL statement you want to execute
     * @param args Fill all placeholders inside your sql statement
     * @return a PlayAccount List containing the required information
     * */
    public static List<XMLFileDetails> queryForXMLFileDetails(String sql, Object...args){
        Connection connection = null;
        PreparedStatement preparedStatement= null;
        ResultSet resultSet;
        List<XMLFileDetails> xmlFileDetailsList = new ArrayList<>();

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
                XMLFileDetails xmlFileDetails= new XMLFileDetails();

                for(int i = 0;i < columnCount; i++){

                    Object columnValue = resultSet.getObject(i+1);

                    String columnName = resultSetMetaData.getColumnName(i+1);

                    Field field = XMLFileDetails.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(xmlFileDetails,columnValue);

                }

                xmlFileDetailsList.add(xmlFileDetails);
            }
            return xmlFileDetailsList;

        } catch (Exception e) {

            e.printStackTrace();

        }finally{

            JDBCUtils.closeResource(connection, preparedStatement);
        }

        return null;
    }

    /**
     *
     * Check if the file type is a xml file
     * @param file Player's xml File
     * @return true or false
     *
     * */

    private static boolean checkFileType(File file){
        boolean b = false;

        try {
            DocumentBuilderFactory factory =DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.parse(file);
            b = true;
        } catch(Exception e) {
            b = false;
        }
        return b;
    }

    /**
     *
     * Generate random file names
     * @return Random File Name
     *
     * */

    private static String getRandomFileName() {

        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

        Date date = new Date();
        String now = simpleDateFormat.format(date);

        Random random = new Random();
        int randomNum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;

        return randomNum + now;
    }

}
