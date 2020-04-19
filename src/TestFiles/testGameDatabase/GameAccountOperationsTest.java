package testGameDatabase;

import GameDatabase.GameAccountOperations;
import GameDatabase.PlayerAccount;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * GameAccountOperations Tester.
 *
 * @author WenhanWei
 * @version 1.0
 */
public class GameAccountOperationsTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getScore(String Username)
     */
    @Test
    public void testGetScore() throws Exception {
        //TODO: Test goes here...
        Assert.assertEquals(30,GameAccountOperations.getScore("wei"));
    }

    /**
     * Method: trySignIn(String Username, String Password)
     */
    @Test
    public void testTrySignIn() throws Exception {
        //TODO: Test goes here...
        Assert.assertEquals(true,GameAccountOperations.trySignIn("wei","wei"));
    }
    /**
     * Method: trySignIn(String Username, String Password)
     */
    @Test
    public void testTrySignInWithWrongUsername() throws Exception {
        //TODO: Test goes here...
        Assert.assertEquals(false,GameAccountOperations.trySignIn("wei123","wei"));
    }

    /**
     * Method: trySignIn(String Username, String Password)
     */
    @Test
    public void testTrySignInWithWrongPassword() throws Exception {
        //TODO: Test goes here...
        Assert.assertEquals(false,GameAccountOperations.trySignIn("wei","wei123"));
    }

    /**
     * Method: trySignUp(String Username, String Password, String Authenticate)
     */
    @Test
    public void testTrySignUp() throws Exception {
        Assert.assertEquals(true, GameAccountOperations.trySignUp("wei", "wei", "1999-07-02"));
    }

    /**
     * Method: trySignUp(String Username, String Password, String Authenticate)
     */
    @Test
    public void testTrySignUpWithSameUsername() throws Exception {
        Assert.assertEquals(false, GameAccountOperations.trySignUp("wei", "wei", "1999-07-02"));
    }

    /**
     * Method: tryResetPassword(String Username, String Authenticate, String newPassword)
     */
    @Test
    public void testTryResetPassword() throws Exception {
        //TODO: Test goes here...
        Assert.assertEquals(true, GameAccountOperations.tryResetPassword("wei", "1999-07-02", "wei"));
    }

    /**
     * Method: tryResetPassword(String Username, String Authenticate, String newPassword)
     */
    @Test
    public void testTryResetPasswordWithWrongDate() throws Exception {
//TODO: Test goes here...
        Assert.assertEquals(false, GameAccountOperations.tryResetPassword("wei", "1999-07-03", "wei"));
    }

    /**
     * Method: tryAddSinglePlayerScores(int singlePlayerScore, String Username)
     */
    @Test
    public void testTryAddSinglePlayerScores() throws Exception {
        //TODO: Test goes here...
        Assert.assertEquals(true, GameAccountOperations.tryAddSinglePlayerScores(10, "wei"));
    }

    /**
     * Method: tryAddSinglePlayerScores(int singlePlayerScore, String Username)
     */
    @Test
    public void testTryAddSinglePlayerScoresWithWrongUsername() throws Exception {
        //TODO: Test goes here...
        Assert.assertEquals(false, GameAccountOperations.tryAddSinglePlayerScores(10, "wei123"));
    }

    /**
     * Method: tryAddMultiPlayerScore(int multiPlayerScore, String Username)
     */
    @Test
    public void testTryAddMultiPlayerScore() throws Exception {
        //TODO: Test goes here...
        Assert.assertEquals(true, GameAccountOperations.tryAddMultiPlayerScore(10, "wei"));
    }

    /**
     * Method: tryAddMultiPlayerScore(int multiPlayerScore, String Username)
     */
    @Test
    public void testTryAddMultiPlayerScoreWithWrongUsername() throws Exception {
        //TODO: Test goes here...
        Assert.assertEquals(false, GameAccountOperations.tryAddMultiPlayerScore(10, "wei123"));
    }


    /**
     * Method: tryUpdateHighestLevel(int HighestLevel, String Username)
     */
    @Test
    public void testTryUpdateHighestLevel() throws Exception {
        //TODO: Test goes here...
        Assert.assertEquals(true, GameAccountOperations.tryUpdateHighestLevel(5, "wei"));
    }

    /**
     * Method: tryUpdateHighestLevel(int HighestLevel, String Username)
     */
    @Test
    public void testTryUpdateHighestLevelWithWrongUsername() throws Exception {
        //TODO: Test goes here...
        Assert.assertEquals(false, GameAccountOperations.tryUpdateHighestLevel(5, "wei123"));
    }


    /**
     * Method: tryGetHighestLevel(String Username)
     */
    @Test
    public void testTryGetHighestLevel() throws Exception {
        //TODO: Test goes here...
        List<PlayerAccount> playerAccounts = GameAccountOperations.tryGetHighestLevel("wei");
        Assert.assertEquals(5,playerAccounts.get(0).getHighestLevel());
    }
    /**
     * Method: tryGetHighestLevel(String Username)
     */
    @Test
    public void testTryGetHighestLevelWithWrongUsername() throws Exception {
        //TODO: Test goes here...
        List<PlayerAccount> empty = new ArrayList<>();
        List<PlayerAccount> playerAccounts = GameAccountOperations.tryGetHighestLevel("wei123");
        Assert.assertEquals(empty,playerAccounts);
    }

    /**
     * Method: tryShowSinglePlayerScore()
     */
    @Test
    public void testTryShowSinglePlayerScore() throws Exception {
        //TODO: Test goes here...
        List<PlayerAccount> playerAccounts = GameAccountOperations.tryShowSinglePlayerScore();
        System.out.println(playerAccounts);

    }

    /**
     * Method: tryShowMultiPlayerScore()
     */
    @Test
    public void testTryShowMultiPlayerScore() throws Exception {
        //TODO: Test goes here...
        List<PlayerAccount> playerAccounts = GameAccountOperations.tryShowMultiPlayerScore();
        System.out.println(playerAccounts);
    }

    /**
     * Method: tryShowAllKindsOfPlayerScore()
     */
    @Test
    public void testTryShowAllKindsOfPlayerScore() throws Exception {
        //TODO: Test goes here...
        List<PlayerAccount> playerAccounts = GameAccountOperations.tryShowAllKindsOfPlayerScore();
        System.out.println(playerAccounts);
    }

//    /**
//     * Method: tryUploadAvatar(String Username, String pathToAvatar)
//     */
//    @Test
//    public void testTryUploadAvatar() throws Exception {
//        //TODO: Test goes here...
//        Assert.assertEquals(true,GameAccountOperations.tryUploadAvatar("wei","src/GameDatabase.testGameDatabase/testFiles/avatar.jpg"));
//    }
//
//    /**
//     * Method: tryUploadAvatar(String Username, String pathToAvatar)
//     */
//    @Test
//    public void testTryUploadAvatarWithWrongPath() throws Exception {
//        //TODO: Test goes here...
//        Assert.assertEquals(false,GameAccountOperations.tryUploadAvatar("wei","avatar.jpg"));
//    }
//
//    /**
//     * Method: tryDownloadAvatar(String Username)
//     */
//    @Test
//    public void testTryDownloadAvatar() throws Exception {
//        //TODO: Test goes here...
//        Assert.assertEquals(true,GameAccountOperations.tryDownloadAvatar("wei"));
//    }


    /**
     * Method: queryForPlayerAccounts(String sql, Object...args)
     */
    @Test
    public void testQueryForPlayerAccounts() throws Exception {
        //TODO: Test goes here...
        String sql ="select * from players";
        List<PlayerAccount> playerAccounts = GameAccountOperations.queryForPlayerAccounts(sql);
        System.out.println(playerAccounts);
    }


    /**
     * Method: whetherAccountExist(String Username)
     */
    @Test
    public void testWhetherAccountExist() throws Exception {
        //TODO: Test goes here...
        Assert.assertEquals(true,GameAccountOperations.whetherAccountExist("wei"));
    }

    /**
     * Method: whetherAccountExist(String Username)
     */
    @Test
    public void testWhetherAccountExistWithWrongUsername() throws Exception {
        //TODO: Test goes here...
        Assert.assertEquals(false,GameAccountOperations.whetherAccountExist("wei123"));
    }


    /**
     * Method: EncoderByMd5(String password)
     */
    @Test
    public void testEncoderByMd5() throws Exception {
        //TODO: Test goes here...

        try {
            Method method = GameAccountOperations.class.getDeclaredMethod("EncoderByMd5", String.class);
            method.setAccessible(true);
            Object afterEncrypt = method.invoke(method, "wei");
            Assert.assertEquals("d69d8949e163fd84c2a5da50138df308", afterEncrypt.toString());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    /**
     * Method: checkPassword(String enteredPassword, String password)
     */
    @Test
    public void testCheckPassword() throws Exception {
        //TODO: Test goes here...

        try {
            Method method = GameAccountOperations.class.getDeclaredMethod("checkPassword", String.class, String.class);
            method.setAccessible(true);
            Object test = method.invoke(method, "wei","d69d8949e163fd84c2a5da50138df308");
            Assert.assertEquals(true,test);
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
    public void testCheckFileType() throws Exception {
        //TODO: Test goes here...

        try {
            Method method = GameAccountOperations.class.getDeclaredMethod("checkFileType", File.class);
            method.setAccessible(true);
            Object testFileType = method.invoke(method, new File("src/GameDatabase.testGameDatabase/testFiles/avatar.jpg"));
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
            Method method = GameAccountOperations.class.getDeclaredMethod("checkFileType", File.class);
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
