package managers;

import logic.Time;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * View for the practice mode of the game
 */
public class PracticeView {

    private static JDialog practice = new JDialog();

    /**
     * Determines whether the player is playing in the practice mode scene rather than just viewing the instructions
     */
    public static boolean exploring = false;

    public static JDialog PracticeView() {
        if (!practice.isDisplayable()) {
            practice.setUndecorated(true);
        }
        practice.setSize(600, 500);
        practice.setResizable(false);
        practice.setLocationRelativeTo(Window.frame); // centers the pop up menus

        JPanel intructions = addPracticeDescriptions();
        practice.add(intructions);

        return practice;
    }

    public static JPanel addPracticeDescriptions() {
        JPanel intro = createPanel("<html><div style='text-align: center;'>You have just entered Odysseus Galaxy on your mission to find life elsewhere in the universe.<br>You were successful - but there's a problem.<br>The apes you found in this galaxy don't recognise you and they're out to get you. Your new mission is to defeat these Apestronauts and return home safely.</div></html>");
        JPanel introPartTwo = createPanel("<html><div style='text-align: center;'>There's not much time, so I'll give you some hints.<br>You can defeat the other apes by kicking them using the SPACE key.</div></html>");
        JPanel startGame = createPanel("<html><div style='text-align: center;'>Time is running out, so you should start now. <br>When you're ready to put your skills to the test against your opponent in practice mode, press S. Good luck!</div></html>");
        JPanel coin = createPanel("<html><div style='text-align: center;'>Collecting coins will boost your total score by up to 500 points! Keep collecting these coins to jump up the leaderboard.</div></html>");
        JPanel baseBooster = createPanel("<html><div style='text-align: center;'>You just found the PSYCHIC. You can now telepathically attack an enemy.<br>Your score will shoot up and your enemy's health will spiral down. You'll increase your strength so you get an extra chance to fight each level.<br>Press the ENTER key to activate.</div></html>");
        JPanel damageBooster = createPanel("<html><div style='text-align: center;'>You just found the BERSERKER. Your enemies won't see this coming - a penetrating blast that will crush their health and make your future attacks more deadly.<br>Press the ENTER key to activate.</div></html>");
        JPanel jumpBooster = createPanel("<html><div style='text-align: center;'>You now have SUPER LEAPING. Jump to the highest heights and leap through dimensions while you leave your enemy struggling behind.<br>Press the ENTER key to activate.</div></html>");
        JPanel knockbackBooster = createPanel("<html><div style='text-align: center;'>If kicking the apes wasn't enough, try knocking them back when you pick up this power up.<br>Press the ENTER key to activate.</div></html>");
        JPanel speedBooster = createPanel("<html><div style='text-align: center;'>You've just found the RADIANT BOLT. Your enemies will be amazed by the speed you now possess. They will never even see it coming.<br>Press the ENTER key to activate.</div></html>");
        JPanel stunBooster = createPanel("<html><div style='text-align: center;'>You've just found CHRONO FUSION. Yield the power of stopping time to freeze your enemy.<br>Press the ENTER key to activate.</div></html>");
        JPanel invincibleWeapon = createPanel("<html><div style='text-align: center;'>Look at you go - you've found the weapon of invincibility. Nothing can defeat you now, you're unstoppable.<br>Just collect the weapon and watch your powers grow.</div></html>");
        JPanel obstacleWeapon = createPanel("<html><div style='text-align: center;'>You've found the obstacles weapon. Prepare for your opponents to be stunned when meteors start appearing.<br>Just collect the weapon to activate the meteors.</div></html>");
        JPanel movingGround = createPanel("<html><div style='text-align: center;'>If you land on this other worldly surface, you can escape your enemies and travel through dimensions.</div></html>");
        JPanel meteor = createPanel("<html><div style='text-align: center;'>There are many dangers you'll encounter in this galaxy, like these meteors. Be careful - if you collide with one, you'll be temporarily frozen.</div></html>");
        JPanel movingObstacle = createPanel("<html><div style='text-align: center;'>Watch out for these deadly obstacles. If you collide with them, you will really take the hit and have to start fighting the apes right from the beginning again.</div></html>");

        PracticeView.Button explore = new PracticeView.Button("EXPLORE PRACTICE GAME");
        PracticeView.Button singlePlayer = new PracticeView.Button("START SINGLEPLAYER GAME");
        startGame.add(explore, "center, wrap, gaptop 20");
        startGame.add(singlePlayer, "center, wrap");
        explore.addMouseListener(new MouseAdapter() { //enters the practice playing mode of the game (practice.xml)
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                practice.setVisible(false);
                Time.timeScale = 1f;
                exploring = true;
            }
        });

        singlePlayer.addMouseListener(new MouseAdapter() { // enter single player game mode against AI
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Engine.Instance.unloadAllScenes();
                Window.running = false;
                Engine.Instance.running = false;
                Window.frame.dispose();
                practice.setVisible(false);
                practice.dispose();
                Engine.Instance = new Engine("Singleplayer");
                Window.running = true;
                Window.LoadWindow();
                Time.timeScale = 1f;
            }
        });

        nextPanel(intro, introPartTwo);
        // Add the gifs to each JPanel
        createGifPanel(introPartTwo, "src/resources/videos/kickcropped.gif", intro, coin);
        createGifPanel(coin, "src/resources/videos/coin.gif", introPartTwo, baseBooster);
        createGifPanel(baseBooster, "src/resources/videos/basebooster.gif", coin, damageBooster);
        createGifPanel(damageBooster, "src/resources/videos/damagebooster.gif", baseBooster, jumpBooster);
        createGifPanel(jumpBooster, "src/resources/videos/jumpbooster.gif", damageBooster, knockbackBooster);
        createGifPanel(knockbackBooster, "src/resources/videos/knockbackbooster.gif", jumpBooster, speedBooster);
        createGifPanel(speedBooster, "src/resources/videos/speedbooster.gif", knockbackBooster, stunBooster);
        createGifPanel(stunBooster, "src/resources/videos/stunbooster.gif", speedBooster, invincibleWeapon);
        createGifPanel(invincibleWeapon, "src/resources/videos/invincibleweapon.gif", stunBooster, obstacleWeapon);
        createGifPanel(obstacleWeapon, "src/resources/videos/obstacleweapon.gif", invincibleWeapon, movingGround);
        createGifPanel(movingGround, "src/resources/videos/movingground.gif", obstacleWeapon, meteor);
        createGifPanel(meteor, "src/resources/videos/meteor.gif", movingGround, movingObstacle);
        createGifPanel(movingObstacle, "src/resources/videos/movingobstacle.gif", meteor, startGame);

        return intro;
    }

    /**
     * Adds a gif to a given JPanel, as well as setting its previous and next panels
     * @param current JPanel to add the gif to
     * @param filePath the String filepath for the gif
     * @param previous the previous JPanel to navigate to when the back button is pressed
     * @param next the next JPanel to navigate to when the continue button is pressed
     */
    public static void createGifPanel(JPanel current, String filePath, JPanel previous, JPanel next) {
        addGif(current, filePath, 350, 200);
        nextPanel(current, next);
        previousPanel(current, previous);
    }


    /**
     * Creates a JPanel with the standard properties and a button to go back to the main menu
     * @param message String message for the JLabel
     * @return JPanel tha has been created
     */
    public static JPanel createPanel(String message) {
        JPanel panel = new JPanel(new MigLayout("align 50% 50%"));;
        panel.setBackground(Color.decode("#101A57")); //  // #7597E3
        panel.setBorder(new CompoundBorder( // CompoundBorder allows us to add two borders to our panel
                BorderFactory.createMatteBorder(10, 10, 10, 10, Color.decode("#6668CC")),
                BorderFactory.createMatteBorder(10, 10, 10, 10, Color.decode("#101A57"))));

        PracticeView.Label info = new PracticeView.Label(message);
        panel.add(info, "center, wrap");
        PracticeView.Button exitToMenu = new PracticeView.Button("EXIT TO MENU");

        panel.add(exitToMenu, "wrap, center, south, width ::200, gaptop 20");

        exitToMenu.addMouseListener(new MouseAdapter() { // button listener which go back to the main menu
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Engine.Instance.unloadAllScenes();
                Window.running = false;
                Engine.Instance.running = false;
                Window.frame.dispose();
                panel.setVisible(false);
                practice.setVisible(false);
                practice.dispose();
                MenuController.jFrame.setVisible(true);
            }
        });

        return panel;
    }


    /**
     * Add a gif to a JPanel
     * @param panel JPanel to add the gif to
     * @param filePath String filepath of the gif
     * @param width width of the gif
     * @param height height of the gif
     */
    public static void addGif(JPanel panel, String filePath, int width, int height) {
        ImageIcon imageIcon = new ImageIcon(filePath);
        Image image = imageIcon.getImage();
        Image newImage = image.getScaledInstance(width, height, java.awt.Image.SCALE_DEFAULT);
        imageIcon = new ImageIcon(newImage);
        JLabel jImage = new JLabel();
        jImage.setIcon(imageIcon);
        panel.add(jImage, "wrap, center");
    }

    /**
     * Create an arrow button
     * @param direction which direction the arrow points in
     * @return arrow button
     */
    public static BasicArrowButton createArrow(String direction) {
        BasicArrowButton arrow = null;
        if (direction.equals("forward")) {
            arrow = new BasicArrowButton(BasicArrowButton.EAST, Color.decode("#6668CC"), Color.decode("#AEA4ED"), Color.decode("#40309C"), Color.decode("#000000")) {
                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(50, 50);
                }
            };
        } else if (direction.equals("back")) {
            arrow = new BasicArrowButton(BasicArrowButton.WEST, Color.decode("#6668CC"), Color.decode("#AEA4ED"), Color.decode("#40309C"), Color.decode("#000000")) {
                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(50, 50);
                }
            };
        }
        return arrow;
    }

    /**
     * Adds a continue button to a JPanel to navigate to the next JPanel in the sequence
     * @param current
     * @param nextPanel
     */
    public static void nextPanel(JPanel current, JPanel nextPanel) {
        PracticeView.Button forwardButton = new PracticeView.Button("CONTINUE");
        forwardButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Time.timeScale = 0f;
                current.setVisible(false);
                practice.add(nextPanel);
                nextPanel.setVisible(true);
            }
        });
        current.add(forwardButton, "center, wrap, gaptop 20, split 2");
    }

    /**
     * Adds a back button to navigate to the previous JPanel in the sequence
     * @param current
     * @param previousPanel
     */
    public static void previousPanel(JPanel current, JPanel previousPanel) {
        PracticeView.Button backButton = new PracticeView.Button("BACK");
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Time.timeScale = 0f;
                previousPanel.setVisible(true);
                current.setVisible(false);
                //  Time.timeScale = 1f;
            }
        });

        current.add(backButton, "center, wrap");

    }

    //Formatting the buttons and text for the pop up windows

    private static class Button extends JButton {
        public Button(String text) {
            super(text);
            setPreferredSize(new Dimension(40, 40));
            setAlignmentX(Component.CENTER_ALIGNMENT);
            setFont(super.getFont().deriveFont(20f));
            setForeground(Color.WHITE);
            setBackground(Color.decode("#6668CC"));
        }
    }

    private static class Label extends JLabel {
        public Label(String text) {
            super(text);
            setForeground(Color.WHITE);
            setFont(super.getFont().deriveFont(15f));
        }
    }

}
