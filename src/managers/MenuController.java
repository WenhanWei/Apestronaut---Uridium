package managers;

import javax.sound.sampled.Clip;
import GameDatabase.GameAccountOperations;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


/**
 * The MenuController binds both the MenuModel and the MenuView(s) so that any interactions can be coordinated.
 */
public class MenuController {
    protected static final int WIDTH = 1920;
    protected static final int HEIGHT = 1080;

    public static JFrame jFrame;

    public static JPanel jPanel;

    private SignInView signInView;
    private SignUpView signUpView;
    private ResetPasswordView resetPasswordView;
    private MenuView menuView;
    private StatsView statsView;
    private WorkshopView workshopView;
    private OptionsView optionsView;
    private PlayView playView;

    public static LobbyView lobbyView;

    private MenuModel menuModel;
    public static CardLayout cardLayout;

    public static LevelView levelView;
    public static GameOverView gameOverView;
    public static LostView lostView;
    public static boolean viewing;
    public static boolean close_update;
    public static Update connection = new Update(4);
    public static boolean engine_started=false;
    public static boolean ports_generated = false;
    public static ArrayList<Integer> ports;


    /**
     * Constructor which adds listener objects to each view in our UI and initialises a JFrame.
     */
    public MenuController() {
        jFrame = new JFrame("Apestronaut");
        jPanel = new JPanel();

        menuModel = new MenuModel();
        signInView = new SignInView();
        signUpView = new SignUpView();
        resetPasswordView = new ResetPasswordView();
        menuView = new MenuView();
        statsView = new StatsView();
        workshopView = new WorkshopView();
        optionsView = new OptionsView();
        playView = new PlayView();
        levelView = new LevelView();
        gameOverView = new GameOverView();
        lostView = new LostView();
        cardLayout = createAndShowGUI();

        signInViewListeners(cardLayout);
        signUpViewListeners(cardLayout);
        resetPasswordViewListeners(cardLayout);
        menuViewListeners(cardLayout);
        playViewListeners(cardLayout);
        statsViewListeners(cardLayout);
        workshopViewListeners(cardLayout);
        optionsViewListeners(cardLayout);
    }

    /**
     * A listener for the sign-in view which adds a listener to the sign-in, reset password buttons, and the sign-up label.
     * @param cardLayout the card layout where each panel has been added.
     */
    private void signInViewListeners(CardLayout cardLayout) {
        signInView.getSignIn().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                boolean result = menuModel.signIn(SignInView.getUsernameField().getText(), String.valueOf(SignInView.getPasswordField().getPassword()));
                if (!result) {
                    JOptionPane.showMessageDialog(jFrame, "Incorrect password, please try again.");
                } else {
                    lobbyView = new LobbyView();
                    jPanel.add(lobbyView.getPanel(), "Lobby View");
                    lobbyViewListeners(cardLayout);
                    menuModel.getAudioManager().startAudio();
                    menuModel.getAudioManager().getClip().loop(Clip.LOOP_CONTINUOUSLY);
                    cardLayout.show(jPanel, "Menu View");
                }
            }
        });
        signInView.getResetPassword().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cardLayout.show(jPanel, "Reset Password View");
            }
        });
        signInView.getSignUp().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cardLayout.show(jPanel, "Sign Up View");
            }
        });
    }

    /**
     * A listener for the sign-up view which adds a listener to the sign-up button and the sign-in label.
     * @param cardLayout the card layout where each panel has been added.
     */
    private void signUpViewListeners(CardLayout cardLayout) {
        signUpView.getSignUp().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                boolean result = menuModel.signUp(SignUpView.getUsernameField().getText(), String.valueOf(SignUpView.getPasswordField().getPassword()), SignUpView.getDateOfBirthField().getText());
                if (!result) {
                    JOptionPane.showMessageDialog(jFrame, "This account already exists or you have entered the wrong date format.");
                } else {
                    JOptionPane.showMessageDialog(jFrame, "Account successfully created, please sign in.");
                    SignUpView.getUsernameField().setText("Username");
                    SignUpView.getPasswordField().setText("Password");
                    SignUpView.getDateOfBirthField().setText("yyyy-MM-dd");
                    cardLayout.show(jPanel, "Sign In View");
                }
            }
        });
        signUpView.getSignIn().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cardLayout.show(jPanel, "Sign In View");
            }
        });
    }

    /**
     * A listener for the reset password view which adds a listener to the reset password button.
     * @param cardLayout the card layout where each panel has been added.
     */
    private void resetPasswordViewListeners(CardLayout cardLayout) {
        resetPasswordView.getSubmit().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                boolean result = menuModel.resetPassword(ResetPasswordView.getUsernameField().getText(), ResetPasswordView.getDateOfBirthField().getText(), String.valueOf(ResetPasswordView.getPasswordField().getPassword()));
                if (!result) {
                    JOptionPane.showMessageDialog(jFrame, "The username or date of birth is incorrect or you have entered the wrong date format.");
                } else {
                    JOptionPane.showMessageDialog(jFrame, "Password successfully reset, please sign in.");
                    ResetPasswordView.getUsernameField().setText("Username");
                    ResetPasswordView.getPasswordField().setText("Password");
                    ResetPasswordView.getDateOfBirthField().setText("yyyy-MM-dd");
                    cardLayout.show(jPanel, "Sign In View");
                }
            }
        });
        resetPasswordView.getSignIn().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cardLayout.show(jPanel, "Sign In View");
            }
        });
    }

    /**
     * Adds the listener objects for the main menu view.
     * @param cardLayout the card layout where each panel has been added.
     */
    private void menuViewListeners(CardLayout cardLayout) {
        // the following listeners allow the user to navigate to the corresponding view when clicking the respective buttons
        menuView.addListener(menuView.getPlayBtn(), e -> {
            cardLayout.show(jPanel, "Play View");
        }, new ButtonListener(menuView.getPlayBtn()));

        menuView.addListener(menuView.getStatsBtn(), e -> {
            menuModel.updateLeaderboards(statsView);
            cardLayout.show(jPanel, "Stats View");
        }, new ButtonListener(menuView.getStatsBtn()));

        menuView.addListener(menuView.getWorkshopBtn(), e -> {
            cardLayout.show(jPanel, "Workshop View");
        }, new ButtonListener(menuView.getWorkshopBtn()));

        menuView.addListener(menuView.getOptionsBtn(), e -> {
            cardLayout.show(jPanel, "Options View");
        }, new ButtonListener(menuView.getOptionsBtn()));

        menuView.addListener(menuView.getExitBtn(), e -> {
            menuModel.getAudioManager().stopAudio();
            System.exit(0);
        }, new ButtonListener(menuView.getExitBtn()));
    }

    /**
     * Adds the listener objects for the play view.
     * @param cardLayout the card layout where each panel has been added.
     */
    private void playViewListeners(CardLayout cardLayout) {
        // the following listener allows the user to go back to the main menu view when clicking the back button
        playView.addListener(playView.getBackBtn(), e -> {
            cardLayout.show(jPanel, "Menu View");
        }, new ButtonListener(playView.getBackBtn()));

        // the following listeners allow the user to select a gamemode and proceed into that gamemode
        playView.getSingleplayer().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { // launches the gamemode
                super.mouseClicked(e);
                menuModel.startGame(jFrame, "Singleplayer");
            }
            @Override
            public void mouseEntered(MouseEvent e) { // changes the cursor and the background around the gamemode
                super.mouseEntered(e);
                playView.getSingleplayer().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                playView.getSingleplayer().setBorder(new LineBorder(Color.YELLOW, 3));
            }
            @Override
            public void mouseExited(MouseEvent e) { // reverts back to default settings
                super.mouseExited(e);
                playView.getSingleplayer().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                playView.getSingleplayer().setBorder(null);
            }
        });
        // the remaining listeners are similar to the first, however, when clicked they launch into different gamemodes
        playView.getMultiplayer().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cardLayout.show(jPanel, "Lobby View");
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                playView.getMultiplayer().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                playView.getMultiplayer().setBorder(new LineBorder(Color.YELLOW, 3));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                playView.getMultiplayer().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                playView.getMultiplayer().setBorder(null);
            }
        });
        playView.getPractice().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                menuModel.startGame(jFrame, "Practice");
                Window.generatePracticeInfo();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                playView.getPractice().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                playView.getPractice().setBorder(new LineBorder(Color.YELLOW, 3));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                playView.getPractice().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                playView.getPractice().setBorder(null);
            }
        });
        playView.getLevelEditor().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                menuModel.startGame(jFrame, "Level Editor");
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                playView.getLevelEditor().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                playView.getLevelEditor().setBorder(new LineBorder(Color.YELLOW, 3));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                playView.getLevelEditor().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                playView.getLevelEditor().setBorder(null);
            }
        });
    }

    /**
     * Adds the listener objects for the stats view.
     * @param cardLayout the card layout where each panel has been added.
     */
    private void statsViewListeners(CardLayout cardLayout) {
        statsView.addListener(statsView.getBackBtn(), e -> {
            cardLayout.show(jPanel, "Menu View");
        }, new ButtonListener(statsView.getBackBtn()));

        // the two listeners allow the user to alternate between the singleplayer and multiplayer scores
        statsView.getSingleplayer().addMouseListener(new ButtonListener(statsView.getSingleplayer()));
        statsView.getSingleplayer().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                statsView.getCardLayout().show(statsView.getStatsContainer(), "Singleplayer Scores");
            }
        });
        statsView.getMultiplayer().addMouseListener(new ButtonListener(statsView.getMultiplayer()));
        statsView.getMultiplayer().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                statsView.getCardLayout().show(statsView.getStatsContainer(), "Multiplayer Scores");
            }
        });
    }

    /**
     * Adds the listener objects for the workshop view.
     * @param cardLayout the card layout where each panel has been added.
     */
    private void workshopViewListeners(CardLayout cardLayout) {
        workshopView.addListener(workshopView.getBackBtn(), e -> {
            cardLayout.show(jPanel, "Menu View");
        }, new ButtonListener(workshopView.getBackBtn()));

        // the two listeners allow the user to alternate between the upload and download
        workshopView.getUpload().addMouseListener(new ButtonListener(workshopView.getUpload()));
        workshopView.getUpload().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                workshopView.getCardLayout().show(workshopView.getWorkshopContainer(), "Upload");
            }
        });
        workshopView.getDownload().addMouseListener(new ButtonListener(workshopView.getDownload()));
        workshopView.getDownload().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                workshopView.getCardLayout().show(workshopView.getWorkshopContainer(), "Download");
            }
        });
    }

    /**
     * Adds the listener objects for the lobby system view.
     * @param cardLayout the card layout where each panel has been added.
     */
    private void lobbyViewListeners(CardLayout cardLayout) {
        lobbyView.addListener(lobbyView.getBackBtn(), e -> {
            close_update = true;
            lobbyView.getCardLayout().show(lobbyView.getLobbyContainer(), "Main Lobby");
            cardLayout.show(jPanel, "Play View");
            try {
                connection.Back();
            }catch (Exception ce){

            }
            lobbyView.player1_2Player.setForeground(Color.WHITE);
            lobbyView.player1_2Player.setText("Player 1");
            lobbyView.player1_3Player.setForeground(Color.WHITE);
            lobbyView.player1_3Player.setText("Player 1");
            lobbyView.player1_4Player.setForeground(Color.WHITE);
            lobbyView.player1_4Player.setText("Player 1");

        }, new ButtonListener(lobbyView.getBackBtn()));

        lobbyView.getTwoPlayers().addMouseListener(new ButtonListener(lobbyView.getTwoPlayers())); // changes the colour when the user clicks any of these buttons
        lobbyView.getThreePlayers().addMouseListener(new ButtonListener(lobbyView.getThreePlayers()));
        lobbyView.getFourPlayers().addMouseListener(new ButtonListener(lobbyView.getFourPlayers()));

        // the following are listeners to change between the different player lobbies
        lobbyView.getTwoPlayers().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                viewing = true;
                engine_started = false;
                super.mouseClicked(e);
                lobbyView.getCardLayout().show(lobbyView.getLobbyContainer(), "Two Player Lobby");
                Update connection = new Update(2);
                connection.Connect_to_Server();
                See_Players players = new See_Players(connection,lobbyView.player2_2Player,lobbyView);
                players.start();

            }
        });
        lobbyView.getThreePlayers().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                viewing=true;
                engine_started=false;
                super.mouseClicked(e);
                lobbyView.getCardLayout().show(lobbyView.getLobbyContainer(), "Three Player Lobby");
                Update connection = new Update(3);
                connection.Connect_to_Server();
                See_Players players = new See_Players(connection,lobbyView.player2_3Player,lobbyView.player3_3Player,lobbyView);
                players.start();

            }
        });
        lobbyView.getFourPlayers().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                viewing=true;
                engine_started=false;
                super.mouseClicked(e);
                lobbyView.getCardLayout().show(lobbyView.getLobbyContainer(), "Four Player Lobby");
                Update connection = new Update(4);
                connection.Connect_to_Server();
                See_Players players = new See_Players(connection,lobbyView.player2_4Player,lobbyView.player3_4Player,lobbyView.player4_4Player,lobbyView);
                players.start();

            }
        });

        // if the user presses ready then the label should change to green
        lobbyView.getReadyBtn_2Player().addActionListener(e -> {
            connection=new Update(4);
            lobbyView.getPlayer1_2Player().setForeground(Color.GREEN);
            viewing = false;
            close_update=false;
            connection = new Update(2);
            Update_Players connect = new Update_Players(connection,lobbyView.player1_2Player,lobbyView.player2_2Player,lobbyView);
            connect.start();
        });
        lobbyView.getReadyBtn_3Player().addActionListener(e -> {
            connection=new Update(4);
            lobbyView.getPlayer1_3Player().setForeground(Color.GREEN);
            viewing = false;
            close_update=false;
            connection = new Update(3);
            Update_Players connect = new Update_Players(connection,lobbyView.player1_3Player,lobbyView.player2_3Player,lobbyView.player3_3Player,lobbyView);
            connect.start();
        });
        lobbyView.getReadyBtn_4Player().addActionListener(e -> {
            connection = new Update(4);
            viewing = false;
            close_update=false;
            lobbyView.getPlayer1_4Player().setForeground(Color.GREEN);
            connection = new Update(4);
            Update_Players connect = new Update_Players(connection,lobbyView.player1_4Player,lobbyView.player2_4Player,lobbyView.player3_4Player,lobbyView.player4_4Player,lobbyView);
            connect.start();

        });

    }

    /**
     * Adds the listener objects for the options view.
     * @param cardLayout the card layout where each panel has been added.
     */
    private void optionsViewListeners(CardLayout cardLayout) {
        optionsView.addListener(optionsView.getBackBtn(), e -> {
            cardLayout.show(jPanel, "Menu View");
        }, new ButtonListener(optionsView.getBackBtn()));

        // the following listeners allow the user to switch between the different options
        optionsView.getGameplayBtn().addMouseListener(new ButtonListener(optionsView.getGameplayBtn()));
        optionsView.getGameplayBtn().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                optionsView.getCardLayout().show(optionsView.getOptionsContainer(), "Gameplay Options");
            }
        });
        optionsView.getControlsBtn().addMouseListener(new ButtonListener(optionsView.getControlsBtn()));
        optionsView.getControlsBtn().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                optionsView.getCardLayout().show(optionsView.getOptionsContainer(), "Controls Options");
            }
        });
        optionsView.getVideoBtn().addMouseListener(new ButtonListener(optionsView.getVideoBtn()));
        optionsView.getVideoBtn().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                optionsView.getCardLayout().show(optionsView.getOptionsContainer(), "Video Options");
            }
        });
        optionsView.getAudioBtn().addMouseListener(new ButtonListener(optionsView.getAudioBtn()));
        optionsView.getAudioBtn().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                optionsView.getCardLayout().show(optionsView.getOptionsContainer(), "Audio Options");
            }
        });

        // listeners for the gameplay panel
        optionsView.getSlowMoCheckBox().addActionListener(e -> { // adds a listener to the slowMoCheckBox, will activate in game if checked
            menuModel.slowMo(optionsView.getSlowMoCheckBox());
        });

        // listeners for the control panel
        int id = 0;
        for (JLabel label : optionsView.getControls()) {
            label.addMouseListener(new LabelListener(label, id, optionsView.getControls(), jFrame)); // adds a listener to each label
            id++;
        }

        // listeners for the video panel
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        optionsView.getFullscrenCheckBox().addActionListener(e -> { // listener which allows us to toggle between fullscreen and windowed
            menuModel.fullscreen(jFrame, optionsView.getFullscrenCheckBox(), graphicsDevice);
        });
        optionsView.getFpsCheckBox().addActionListener(e -> { // adds a listener to the fpsCheckBox, will show fps in the game if checked
            Window.showDebug = !Window.showDebug;
        });
        optionsView.getHealthBarComboBox().addItemListener(e -> { // listener which allows the user to change the colour of the health bar
            if (e.getStateChange() == ItemEvent.SELECTED) {
                menuModel.healthBarColour(optionsView.getHealthBarComboBox());
            }
        });

        // listeners for the audio panel
        menuModel.getAudioManager().setAudio(optionsView.getMusicSlider().getValue() / 100f); // sets the initial volume of the music

        optionsView.getMusicSlider().addChangeListener(e -> { // adds a listener to the music slider
            menuModel.updateMusic(optionsView.getCurMusicVol(), optionsView.getMusicSlider());
        });

        optionsView.getGameplaySlider().addChangeListener(e -> optionsView.getGameplayVol().setText(optionsView.getGameplaySlider().getValue() + "%"));
        for (AudioManager audioManager : menuModel.getSoundEffects()) { // updates each sound file volume
            optionsView.getGameplaySlider().addChangeListener(e -> menuModel.updateSoundEffects(optionsView.getGameplaySlider(), audioManager));
        }
    }

    /**
     * Add each view (panel) to an overarching JPanel which uses a card layout. This helps to avoid using multiple JFrames for each view.
     * @return the card layout.
     */
    private CardLayout createAndShowGUI() {
        jPanel.setLayout(new CardLayout());
        CardLayout cardLayout = (CardLayout) jPanel.getLayout();
        jPanel.add(signInView.getPanel(), "Sign In View");
        jPanel.add(signUpView.getPanel(), "Sign Up View");
        jPanel.add(resetPasswordView.getPanel(), "Reset Password View");
        jPanel.add(menuView.getPanel(), "Menu View");
        jPanel.add(playView.getPanel(), "Play View");
        jPanel.add(statsView.getPanel(), "Stats View");
        jPanel.add(workshopView.getPanel(),"Workshop View");
        jPanel.add(optionsView.getPanel(), "Options View");

        jFrame.setFocusable(true);
        jFrame.setFocusTraversalKeysEnabled(true);
        jFrame.addKeyListener(new Input());

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close the program when the window-close is pressed
        jFrame.setSize(WIDTH, HEIGHT);
        jFrame.setLocationRelativeTo(null); // centers the application in the center of the screen
        jFrame.setResizable(false);
        jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jFrame.setUndecorated(true);
        jFrame.add(jPanel);
        jFrame.setVisible(true);

        return cardLayout;
    }

    class See_Players extends Thread{
        private Update connection;
        private JLabel player2;
        private JLabel player3;
        private JLabel player4;
        LobbyView view;

        /**
         * This class requests an update from the server as well, the only reason it isn't one class it's because of the way it handles the data compared to Update_Players
         * and the behaviour of the game after that
         * @param connection the Update class to handle communication with the server
         * @param player2 the JLabel for the second player
         * @param player3 the JLabel for the third player
         * @param player4 the JLabel for th forth player
         * @param view
         */
        See_Players(Update connection, JLabel player2, JLabel player3,JLabel player4,LobbyView view){
            this.connection = connection;
            this.player2 = player2;
            this.player3 = player3;
            this.player4 = player4;
            this.view = view;
        }
        See_Players(Update connection , JLabel player2, JLabel player3,LobbyView view){
            this.connection = connection;
            this.player2 = player2;
            this.player3 = player3;
            this.view = view;
        }
        See_Players(Update connection , JLabel player2,LobbyView view){
            this.connection = connection;
            this.player2 = player2;
            this.view = view;
        }
        private void Update_View() {
            if (!connection.is_connected()) {
                connection.Connect_to_Server();
            }
            if (connection.Check_Update()) {
                System.exit(0);
            }
            ArrayList<String> names = connection.Update_State();
            if (names != null) {
//                System.out.println(names.toString());
                if(connection.players == 2){
                    if(names.size() == 1) {
                        if (names.get(0).startsWith("Player")) {
                        } else {
                            player2.setText(names.get(0));
                            player2.setForeground(Color.GREEN);
                            lobbyView.player2Score.setText(String.valueOf(GameAccountOperations.getScore(names.get(0))));
                        }
                    }
                    if(names.size() == 0){
                        player2.setForeground(Color.WHITE);
                        player2.setText("Player 2");
                        lobbyView.player2Score.setText("score");
                    }
                }
                else if(connection.players == 3){
                    if(names.size() == 1) {
                        if (names.get(0).startsWith("Player")) {

                        } else {
                            player2.setText(names.get(0));
                            player2.setForeground(Color.GREEN);
                            lobbyView.player2Score_3.setText(String.valueOf(GameAccountOperations.getScore(names.get(0))));
                            player3.setText("Player 3");
                            player3.setForeground(Color.WHITE);
                            lobbyView.player3Score_3.setText("Score");
                        }
                    }
                    if(names.size() == 2){
                        if(names.get(0).startsWith("Player")) {
                        }
                        else{
                            player2.setText(names.get(0));
                            player2.setForeground(Color.GREEN);
                            lobbyView.player2Score_3.setText(String.valueOf(GameAccountOperations.getScore(names.get(0))));
                        }
                        if(names.get(1).startsWith("Player")){

                        }else{
                            player3.setText(names.get(1));
                            player3.setForeground(Color.GREEN);
                            lobbyView.player3Score_3.setText(String.valueOf(GameAccountOperations.getScore(names.get(1))));
                        }
                    }
                    if(names.size() == 0){
                        player2.setForeground(Color.WHITE);
                        player2.setText("Player 2");
                        lobbyView.player2Score_3.setText("score");
                        player3.setForeground(Color.WHITE);
                        player3.setText("Player 3");
                        lobbyView.player3Score_3.setText("score");
                    }
                }
                else if(connection.players == 4) {
                    if (names.size() == 1) {

                            player2.setText(names.get(0));
                            player2.setForeground(Color.GREEN);
                            lobbyView.player2Score_4.setText(String.valueOf(GameAccountOperations.getScore(names.get(0))));
                            lobbyView.player3Score_4.setText("score");
                            player3.setText("Player 3");
                            player3.setForeground(Color.WHITE);

                    }
                    else if (names.size() == 2) {
                            player2.setText(names.get(1));
                            player2.setForeground(Color.GREEN);
                            lobbyView.player2Score_4.setText(String.valueOf(GameAccountOperations.getScore(names.get(1))));

                            player3.setText(names.get(0));
                            player3.setForeground(Color.GREEN);
                            lobbyView.player3Score_4.setText(String.valueOf(GameAccountOperations.getScore(names.get(0))));
                            player4.setText("Player 4");
                            player4.setForeground(Color.WHITE);
                            lobbyView.player4Score_4.setText("score");

                    } else if (names.size() == 3) {
                            player2.setText(names.get(1));
                            player2.setForeground(Color.GREEN);
                            lobbyView.player2Score_4.setText(String.valueOf(GameAccountOperations.getScore(names.get(1))));
                            player3.setText(names.get(2));
                            player3.setForeground(Color.GREEN);
                            lobbyView.player3Score_4.setText(String.valueOf(GameAccountOperations.getScore(names.get(2))));
                            player4.setText(names.get(0));
                            player4.setForeground(Color.GREEN);
                            lobbyView.player4Score_4.setText(String.valueOf(GameAccountOperations.getScore(names.get(0))));

                    }
                    else if(names.size() == 0){
                        player2.setForeground(Color.WHITE);
                        player2.setText("Player 2");
                        lobbyView.player2Score_4.setText("score");
                        player3.setForeground(Color.WHITE);
                        player3.setText("Player 3");
                        lobbyView.player3Score_4.setText("score");
                        player4.setText("Player 4");
                        player4.setForeground(Color.WHITE);
                        lobbyView.player4Score_4.setText("score");
                    }

                }

                names = new ArrayList<>();
            }
            else {

//                player2.setText("Player 2");
//                player2.setForeground(Color.WHITE);
//                lobbyView.player2Score.setText("score");
//                lobbyView.player2Score_3.setText("score");
//                lobbyView.player2Score_4.setText("score");
//                if(connection.players >=3) {
//                    player3.setText("Player 3");
//                    player3.setForeground(Color.WHITE);
//                    lobbyView.player3Score_3.setText("score");
//                    lobbyView.player3Score_4.setText("score");
//                }
//                if(connection.players == 4) {
//                    player4.setText("Player 4");
//                    player4.setForeground(Color.WHITE);
//                    lobbyView.player4Score_4.setText("score");
//                }
            }
        }

        @Override
        public void run() {
            while(MenuController.viewing == true &&engine_started == false) {
                Update_View();
                try {
                    connection.Close();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    class Update_Players extends Thread{
        public boolean join = false;
        private ArrayList<String> names;
        private JLabel player1;
        private JLabel player2;
        private JLabel player3;
        private JLabel player4;
        private Update connection;
        public LobbyView view;
        public void set_join(boolean value){
            join=value;
        }

        /**
         * @param connection the Update class that makes the communication with the server
         * @param player1 the JLabel for the first player
         * @param player2 the JLabel for the second player
         * @param player3 the JLabel for the third player
         * @param view the LobbyView class that handles the Lobby System's rendering
         * @param player4 the JLabel for the forth player
         */
        Update_Players(Update connection , JLabel player1, JLabel player2, JLabel player3, JLabel player4,LobbyView view){
            this.connection = connection;
            this.player1=player1;
            this.player2 = player2;
            this.player3 = player3;
            this.player4 = player4;
            this.view=view;
        }
        Update_Players(Update connection , JLabel player1, JLabel player2, JLabel player3,LobbyView view){
            this.connection = connection;
            this.player1=player1;
            this.player2 = player2;
            this.player3 = player3;
            this.view = view;
        }
        Update_Players(Update connection , JLabel player1, JLabel player2,LobbyView view){
            this.connection = connection;
            this.player1=player1;
            this.player2 = player2;
            this.view = view;
        }


        /**
         * @param connection the Update class that makes the communication with the server
         * @param player1 the JLabel for the first player
         * @param player2 the JLabel for the second player
         * @param player3 the JLabel for the third player
         * @param player4 the JLabel for the forth player
         */
        private void Request_Update(Update connection, JLabel player1, JLabel player2, JLabel player3, JLabel player4) {
            int names_size = 0;
            while (names_size < connection.players){
                names = connection.Update_State();
                if (names != null) {
                    names_size = names.size();
                    System.out.println("Names: " + names);
                    System.out.println("Names size:" + names.size());
                    System.out.println("Players: " +(connection.players+1));
                    if (connection.players == 2) {
                        if (names.size() == 1) {
                            if (GameAccountOperations.playerUsername.equals(names.get(0))) {
                                player1.setText(names.get(0));
                                player1.setForeground(Color.GREEN);
                            } else {
                                player2.setText(names.get(0));
                                player2.setForeground(Color.GREEN);
                            }
                        }
                        if (names.size() == 2) {
                            if (GameAccountOperations.playerUsername.equals(names.get(0))) {
                                player1.setText(names.get(0));
                                player1.setForeground(Color.GREEN);
                                player2.setText(names.get(1));
                                player2.setForeground(Color.GREEN);
                            } else {
                                player1.setText(names.get(1));
                                player1.setForeground(Color.GREEN);
                                player2.setText(names.get(0));
                                player2.setForeground(Color.GREEN);
                            }
                        }
                    } else if (connection.players == 3) {
                        if (names.size() == 1) {
                            if (GameAccountOperations.playerUsername.equals(names.get(0))) {
                                player1.setText(names.get(0));
                                player1.setForeground(Color.GREEN);
                                player2.setText("Player 2");
                                lobbyView.player2Score_4.setText("score");
                                player2.setForeground(Color.WHITE);
                            } else {
                                player1.setText("Player 1");
                                lobbyView.player2Score_4.setText("score");
                                player1.setForeground(Color.WHITE);
                                player2.setText(names.get(0));
                                player2.setForeground(Color.GREEN);
                                lobbyView.player2Score_3.setText(String.valueOf(GameAccountOperations.getScore(names.get(0))));
                            }
                        }
                        if (names.size() == 2) {
                            if (GameAccountOperations.playerUsername.equals(names.get(0))) {
                                player1.setText(names.get(0));
                                player1.setForeground(Color.GREEN);
                                player2.setText(names.get(1));
                                player2.setForeground(Color.GREEN);
                                lobbyView.player2Score_3.setText(String.valueOf(GameAccountOperations.getScore(names.get(1))));
                            } else {
                                player1.setText(names.get(1));
                                player1.setForeground(Color.GREEN);
                                player2.setText(names.get(0));
                                player2.setForeground(Color.GREEN);
                                lobbyView.player2Score_3.setText(String.valueOf(GameAccountOperations.getScore(names.get(0))));
                            }
                        }
                        if (names.size() == 3) {
                            if (GameAccountOperations.playerUsername.equals(names.get(0))) {
                                player1.setText(names.get(0));
                                player1.setForeground(Color.GREEN);
                                player2.setText(names.get(1));
                                player2.setForeground(Color.GREEN);
                                lobbyView.player2Score_3.setText(String.valueOf(GameAccountOperations.getScore(names.get(1))));
                                player3.setText(names.get(2));
                                player3.setForeground(Color.GREEN);
                                lobbyView.player3Score_3.setText(String.valueOf(GameAccountOperations.getScore(names.get(2))));
                            } else if (GameAccountOperations.playerUsername.equals(names.get(1))) {
                                player1.setText(names.get(1));
                                player1.setForeground(Color.GREEN);
                                player2.setText(names.get(2));
                                player2.setForeground(Color.GREEN);
                                lobbyView.player2Score_3.setText(String.valueOf(GameAccountOperations.getScore(names.get(2))));
                                player3.setText(names.get(0));
                                player3.setForeground(Color.GREEN);
                                lobbyView.player3Score_3.setText(String.valueOf(GameAccountOperations.getScore(names.get(0))));
                            } else if (GameAccountOperations.playerUsername.equals(names.get(2))) {
                                player1.setText(names.get(2));
                                player1.setForeground(Color.GREEN);
                                player2.setText(names.get(0));
                                player2.setForeground(Color.GREEN);
                                lobbyView.player2Score_3.setText(String.valueOf(GameAccountOperations.getScore(names.get(0))));
                                player3.setText(names.get(1));
                                player3.setForeground(Color.GREEN);
                                lobbyView.player3Score_3.setText(String.valueOf(GameAccountOperations.getScore(names.get(1))));
                            }
                        }
                    } else if (connection.players == 4) {
                        if (names.size() == 1) {
                            if (GameAccountOperations.playerUsername.equals(names.get(0))) {
                                player1.setText(names.get(0));
                                player1.setForeground(Color.GREEN);
                                player2.setText("Player 2");
                                lobbyView.player2Score_4.setText("score");
                                player2.setForeground(Color.WHITE);
                                player3.setText("Player 3");
                                lobbyView.player3Score_4.setText("score");
                                player3.setForeground(Color.WHITE);
                                player4.setText("Player 4");
                                player4.setForeground(Color.WHITE);
                                lobbyView.player4Score_4.setText("score");
                            } else {
                                player1.setText(names.get(0));
                                player1.setForeground(Color.GREEN);
                                player2.setText("Player 2");
                                player2.setForeground(Color.WHITE);
                                lobbyView.player2Score_4.setText("score");
                                player3.setText("Player 3");
                                lobbyView.player3Score_4.setText("score");
                                player3.setForeground(Color.WHITE);
                                player4.setText("Player 4");
                                player4.setForeground(Color.WHITE);
                                lobbyView.player4Score_4.setText("score");
                            }
                        } else if (names.size() == 2) {
                            if (GameAccountOperations.playerUsername.equals(names.get(0))) {
                                player1.setText(names.get(0));
                                player1.setForeground(Color.GREEN);
                                player2.setText(names.get(1));
                                player2.setForeground(Color.GREEN);
                                lobbyView.player2Score_4.setText(String.valueOf(GameAccountOperations.getScore(names.get(1))));
                                player3.setText("Player 3");
                                lobbyView.player3Score_4.setText("score");
                                player3.setForeground(Color.WHITE);
                                player4.setText("Player 4");
                                player4.setForeground(Color.WHITE);
                                lobbyView.player4Score_4.setText("score");
                            } else if (GameAccountOperations.playerUsername.equals(names.get(1))) {
                                player1.setText(names.get(1));
                                player1.setForeground(Color.GREEN);
                                player2.setText(names.get(0));
                                player2.setForeground(Color.GREEN);
                                lobbyView.player2Score_4.setText(String.valueOf(GameAccountOperations.getScore(names.get(0))));
                                player3.setText("Player 3");
                                lobbyView.player3Score_4.setText("score");
                                player3.setForeground(Color.WHITE);
                                player4.setText("Player 4");
                                player4.setForeground(Color.WHITE);
                                lobbyView.player4Score_4.setText("score");
                            }
                        } else if (names.size() == 3) {
                            if (GameAccountOperations.playerUsername.equals(names.get(0))) {
                                player1.setText(names.get(0));
                                player1.setForeground(Color.GREEN);
                                player2.setText(names.get(1));
                                player2.setForeground(Color.GREEN);
                                lobbyView.player2Score_4.setText(String.valueOf(GameAccountOperations.getScore(names.get(1))));
                                player3.setText(names.get(2));
                                player3.setForeground(Color.GREEN);
                                lobbyView.player3Score_4.setText(String.valueOf(GameAccountOperations.getScore(names.get(2))));
                                player4.setText("Player 4");
                                player4.setForeground(Color.WHITE);
                                lobbyView.player4Score_4.setText("score");
                            }
                            if (GameAccountOperations.playerUsername.equals(names.get(1))) {
                                player1.setText(names.get(1));
                                player1.setForeground(Color.GREEN);
                                player2.setText(names.get(2));
                                player2.setForeground(Color.GREEN);
                                lobbyView.player2Score_4.setText(String.valueOf(GameAccountOperations.getScore(names.get(2))));
                                player3.setText(names.get(0));
                                player3.setForeground(Color.GREEN);
                                lobbyView.player3Score_4.setText(String.valueOf(GameAccountOperations.getScore(names.get(0))));
                                player4.setText("Player 4");
                                player4.setForeground(Color.WHITE);
                                lobbyView.player4Score_4.setText("score");
                            }
                            if (GameAccountOperations.playerUsername.equals(names.get(2))) {
                                player1.setText(names.get(2));
                                player1.setForeground(Color.GREEN);
                                player2.setText(names.get(0));
                                player2.setForeground(Color.GREEN);
                                lobbyView.player2Score_4.setText(String.valueOf(GameAccountOperations.getScore(names.get(0))));
                                player3.setText(names.get(1));
                                player3.setForeground(Color.GREEN);
                                lobbyView.player3Score_4.setText(String.valueOf(GameAccountOperations.getScore(names.get(1))));
                                player4.setText("Player 4");
                                player4.setForeground(Color.WHITE);
                                lobbyView.player4Score_4.setText("score");
                            }
                        } else if (names.size() == 4) {
                            if (GameAccountOperations.playerUsername.equals(names.get(0))) {
                                player1.setText(names.get(0));
                                player1.setForeground(Color.GREEN);
                                player2.setText(names.get(1));
                                player2.setForeground(Color.GREEN);
                                lobbyView.player2Score_4.setText(String.valueOf(GameAccountOperations.getScore(names.get(1))));
                                player3.setText(names.get(2));
                                player3.setForeground(Color.GREEN);
                                lobbyView.player3Score_4.setText(String.valueOf(GameAccountOperations.getScore(names.get(2))));
                                player4.setText(names.get(3));
                                player4.setForeground(Color.GREEN);
                                lobbyView.player4Score_4.setText(String.valueOf(GameAccountOperations.getScore(names.get(3))));
                            } else if (GameAccountOperations.playerUsername.equals(names.get(1))) {
                                player1.setText(names.get(1));
                                player1.setForeground(Color.GREEN);
                                player2.setText(names.get(0));
                                player2.setForeground(Color.GREEN);
                                lobbyView.player2Score_4.setText(String.valueOf(GameAccountOperations.getScore(names.get(0))));
                                player3.setText(names.get(2));
                                player3.setForeground(Color.GREEN);
                                lobbyView.player3Score_4.setText(String.valueOf(GameAccountOperations.getScore(names.get(2))));
                                player4.setText(names.get(3));
                                player4.setForeground(Color.GREEN);
                                lobbyView.player4Score_4.setText(String.valueOf(GameAccountOperations.getScore(names.get(3))));
                            } else if (GameAccountOperations.playerUsername.equals(names.get(2))) {
                                player1.setText(names.get(2));
                                player1.setForeground(Color.GREEN);
                                player2.setText(names.get(1));
                                player2.setForeground(Color.GREEN);
                                lobbyView.player2Score_4.setText(String.valueOf(GameAccountOperations.getScore(names.get(1))));
                                player3.setText(names.get(0));
                                player3.setForeground(Color.GREEN);
                                lobbyView.player3Score_4.setText(String.valueOf(GameAccountOperations.getScore(names.get(0))));
                                player4.setText(names.get(3));
                                player4.setForeground(Color.GREEN);
                                lobbyView.player4Score_4.setText(String.valueOf(GameAccountOperations.getScore(names.get(3))));
                            } else if (GameAccountOperations.playerUsername.equals(names.get(3))) {
                                player1.setText(names.get(3));
                                player1.setForeground(Color.GREEN);
                                player2.setText(names.get(1));
                                player2.setForeground(Color.GREEN);
                                lobbyView.player2Score_4.setText(String.valueOf(GameAccountOperations.getScore(names.get(1))));
                                player3.setText(names.get(2));
                                player3.setForeground(Color.GREEN);
                                lobbyView.player3Score_4.setText(String.valueOf(GameAccountOperations.getScore(names.get(2))));
                                player4.setText(names.get(0));
                                player4.setForeground(Color.GREEN);
                                lobbyView.player4Score_4.setText(String.valueOf(GameAccountOperations.getScore(names.get(0))));
                            }

                        }
                    }
                    names.clear();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
        }
        private void Update(Update connection, JLabel player1, JLabel player2, JLabel player3, JLabel player4){
                if (close_update == false && viewing == false) {
                    connection.Connect_to_Server();
                    if (connection.Check_Update()) {
                        System.exit(0);
                    }
                    connection.GeneratePorts();
                    connection.JoinQueue();

                    Request_Update(connection, player1, player2, player3, player4);

//            }
                    if (connection.players == 2) {
                        connection.get2Players();
                        engine_started = true;
                        jFrame.setVisible(false);
                        Window.running = true;
                        Window.LoadWindow();
//                        System.out.println("Ports: " + connection.get_ports()[0]);
                        if(Engine.Instance!=null){
                            Engine.Instance.setIPs(connection.get_ips());
                            Engine.Instance.setPorts(connection.get_ports());
//                            Engine.Instance = null;
                            Engine.Instance.player2_exit=false;
                        }
                        Engine.Instance = new Engine("Multiplayer", connection.get_ips(), connection.get_ports(),2);
                        names = new ArrayList<>();

                    } else if (connection.players == 3) {
                        connection.get3Players();
                        engine_started = true;
                        jFrame.setVisible(false);
                        Window.running = true;
                        Window.LoadWindow();
                        if(Engine.Instance!=null){
                            Engine.Instance = null;
                        }
                        Engine.Instance = new Engine("Multiplayer 3", connection.get_ips(), connection.get_ports(),3);
                        names = new ArrayList<>();

                    } else if (connection.players == 4) {
                        connection.get4Players();
                        engine_started = true;
                        jFrame.setVisible(false);
                        Window.running = true;
                        Window.LoadWindow();
                        if(Engine.Instance!=null){
                            Engine.Instance = null;
                        }
                        Engine.Instance = new Engine("Multiplayer 4", connection.get_ips(), connection.get_ports(),4);
                        names = new ArrayList<>();

                    }
                    System.out.println("Players: " + connection.players);
                }
        }

        @Override
        public void run() {
            if(!engine_started) {
                Update(connection, player1, player2, player3, player4);
            }
        }
    }
    public static void main(String[] args) {
        // runs the MainMenuController() constructor in the event dispatcher thread (for
        // thread safety)
        SwingUtilities.invokeLater(MenuController::new); // method reference
    }
}
