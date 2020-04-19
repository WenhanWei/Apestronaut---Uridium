package testGameDatabase;

import GameDatabase.GameAccountOperations;
import GameDatabase.WorkshopFileOperations;
import GameDatabase.XMLFileDetails;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * WorkshopFileOperations Tester.
 *
 * @author WenhanWei
 * @version 1.0
 */
public class WorkshopFileOperationsTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: tryUploadXMLFILE(String Author, String FileName, String pathToXML)
     */
    @Test
    public void testTryUploadXMLFILE() throws Exception {
        //TODO: Test goes here...
        Assert.assertEquals(true,WorkshopFileOperations.tryUploadXMLFILE("wei","level1","src/GameDatabase.testGameDatabase/testFiles/test.xml"));

    }

    /**
     * Method: tryUploadXMLFILE(String Author, String FileName, String pathToXML)
     */
    @Test
    public void testTryUploadXMLFILEWithFilenameExist() throws Exception {
        //TODO: Test goes here...
        Assert.assertEquals(false,WorkshopFileOperations.tryUploadXMLFILE("wei","level1","src/GameDatabase.testGameDatabase/testFiles/test.xml"));

    }

    /**
     * Method: trySearchXMLFILEbyAuthorName(String Author)
     */
    @Test
    public void testTrySearchXMLFILEbyAuthorName() throws Exception {
        //TODO: Test goes here...
        List<XMLFileDetails> xmlFileDetails = WorkshopFileOperations.trySearchXMLFILEbyAuthorName("wei");
        System.out.println(xmlFileDetails);

    }

    /**
     * Method: trySearchXMLFILEbyAuthorName(String Author)
     */
    @Test
    public void testTrySearchXMLFILEbyAuthorNameFuzzySearch() throws Exception {
        //TODO: Test goes here...
        List<XMLFileDetails> xmlFileDetails = WorkshopFileOperations.trySearchXMLFILEbyAuthorName("w");
        System.out.println(xmlFileDetails);

    }

    /**
     * Method: trySearchXMLFILEbyFileName(String FileName)
     */
    @Test
    public void testTrySearchXMLFILEbyFileName() throws Exception {
        //TODO: Test goes here...
        List<XMLFileDetails> xmlFileDetails = WorkshopFileOperations.trySearchXMLFILEbyFileName("level1");
        System.out.println(xmlFileDetails);

    }

    /**
     * Method: trySearchXMLFILEbyFileName(String FileName)
     */
    @Test
    public void testTrySearchXMLFILEbyFileNameFuzzySearch() throws Exception {
        //TODO: Test goes here...
        List<XMLFileDetails> xmlFileDetails = WorkshopFileOperations.trySearchXMLFILEbyFileName("le");
        System.out.println(xmlFileDetails);

    }

    /**
     * Method: trySearchXMLFILEbyAuthorANDFileName(String name)
     */
    @Test
    public void testTrySearchXMLFILEbyAuthorANDFileNameFuzzySearch() throws Exception {
        //TODO: Test goes here...
        List<XMLFileDetails> xmlFileDetails = WorkshopFileOperations.trySearchXMLFILEbyAuthorANDFileName("");
        System.out.println(xmlFileDetails);

    }

    /**
     * Method: tryDownloadXMLFILESbyAuthorName(String Author)
     */
    @Test
    public void testTryDownloadXMLFILESbyAuthorName() throws Exception {
        //TODO: Test goes here...
        Assert.assertEquals(true,WorkshopFileOperations.tryDownloadXMLFILESbyAuthorName("wei"));
    }

    /**
     * Method: tryDownloadXMLFILESbyAuthorName(String Author)
     */
    @Test
    public void testTryDownloadXMLFILESbyWrongAuthorName() throws Exception {
        //TODO: Test goes here...
        Assert.assertEquals(false,WorkshopFileOperations.tryDownloadXMLFILESbyAuthorName("wei123"));
    }

    /**
     * Method: tryDownloadXMLFILEbyFileName(String fileName)
     */
    @Test
    public void testTryDownloadXMLFILEbyFileName() throws Exception {
        //TODO: Test goes here...
        Assert.assertEquals(true,WorkshopFileOperations.tryDownloadXMLFILEbyFileName("level1"));
    }

    /**
     * Method: tryDownloadXMLFILEbyFileName(String fileName)
     */
    @Test
    public void testTryDownloadXMLFILEbyWrongFileName() throws Exception {
        //TODO: Test goes here...
        Assert.assertEquals(false,WorkshopFileOperations.tryDownloadXMLFILEbyFileName("level1111"));
    }


    /**
     * Method: queryForXMLFileDetails(String sql, Object...args)
     */
    @Test
    public void testQueryForXMLFileDetails() throws Exception {
        //TODO: Test goes here...
        String sql ="select author,filename from workshop where author = ?";
        List<XMLFileDetails> xmlFileDetails = WorkshopFileOperations.queryForXMLFileDetails(sql,"wei");
        System.out.println(xmlFileDetails);
    }


    /**
     * Method: checkFileType(File file)
     */
    @Test
    public void testCheckFileType() throws Exception {
        //TODO: Test goes here...
        try {
            Method method = WorkshopFileOperations.class.getDeclaredMethod("checkFileType", File.class);
            method.setAccessible(true);
            Object testFileType = method.invoke(method, new File("src/GameDatabase/testGameDatabase/testFiles/test.xml"));
            Assert.assertEquals(true,testFileType);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method: checkFileType(File file)
     */
    @Test
    public void testCheckFileTypeWrong() throws Exception {
        //TODO: Test goes here...
        try {
            Method method = WorkshopFileOperations.class.getDeclaredMethod("checkFileType", File.class);
            method.setAccessible(true);
            Object testFileType = method.invoke(method, new File("src/GameDatabase.testGameDatabase/testFiles/test.txt"));
            Assert.assertEquals(false,testFileType);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method: getRandomFileName()
     */
    @Test
    public void testGetRandomFileName() throws Exception {
        //TODO: Test goes here...

        try {
            Method method1 = GameAccountOperations.class.getDeclaredMethod("getRandomFileName");
            method1.setAccessible(true);
            Object first = method1.invoke(method1);
            Method method2 = GameAccountOperations.class.getDeclaredMethod("getRandomFileName");
            method2.setAccessible(true);
            Object second = method2.invoke(method2);
            Assert.assertEquals(true,first != second);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

} 
