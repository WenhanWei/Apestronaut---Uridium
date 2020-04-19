package managers;

import GameDatabase.GameAccountOperations;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.io.File;

/**
 * The LobbyView is to be used in conjunction with our networking codebase to be used for the multiplayer.
 */
public class LobbyView extends View {
    private JPanel lobbyContainer;
    private CardLayout cardLayout;

    private JButton gamepadBtn;
    private JButton twoPlayers;
    private JButton threePlayers;
    private JButton fourPlayers;

    private String map;

    public JButton readyBtn_2Player;
    public JLabel player1_2Player;
    public JLabel player2_2Player;
    public Label player2Score;

    public JButton readyBtn_3Player;
    public JLabel player1_3Player;
    public JLabel player2_3Player;
    public JLabel player3_3Player;
    public Label player2Score_3;
    public  Label player3Score_3;

    public JButton readyBtn_4Player;
    public JLabel player1_4Player;
    public JLabel player2_4Player;
    public JLabel player3_4Player;
    public JLabel player4_4Player;
    public Label player2Score_4;
    public Label player3Score_4;
    public Label player4Score_4;

    public LobbyView() {
        File path = new File("src/resources/icons/gamepad.png");
        loadImage(path);
        gamepadBtn = new JButton("LOBBY", new ImageIcon(super.image));
        gamepadBtn.setIconTextGap(20);

        createAndShowGUI();
    }

    /**
     * Responsible for creating all components and visually structuring them.
     */
    private void createAndShowGUI() {
        // using a mainPanel (background) which adds both the title and other components
        JImage background = super.loadResources();
        background.setLayout(new BoxLayout(background, BoxLayout.Y_AXIS));
        JPanel titlePanel = super.createTitlePanel(super.getBackBtn(), gamepadBtn);
        background.add(titlePanel);
        background.add(Box.createRigidArea(new Dimension(0, 100)));

        lobbyContainer = new JPanel();
        lobbyContainer.setOpaque(false);
        lobbyContainer.setLayout(new CardLayout());
        cardLayout = (CardLayout) lobbyContainer.getLayout();

        JPanel userInfoContainer = new JPanel(); // container for the user information that allows to have two colours side-by-side
        userInfoContainer.setOpaque(false);
        userInfoContainer.setLayout(new BoxLayout(userInfoContainer, BoxLayout.X_AXIS));

        JPanel usernamePanel = new JPanel(new MigLayout("center, debug")); // this panel will contain the username and the multiplayer score
        usernamePanel.setMaximumSize(new Dimension(500, 50));
        usernamePanel.setBackground(Color.decode("#7597E3"));
        usernamePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel scorePanel = new JPanel(new MigLayout("center, debug")); // this panel will contain the username and the multiplayer score
        scorePanel.setMaximumSize(new Dimension(500, 50));
        scorePanel.setBackground(Color.decode("#6668CC"));
        scorePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        Label username = new Label("Player = " + SignInView.getUsernameField().getText(), getFont());
        Label score = new Label("Score = " + GameAccountOperations.getScore(SignInView.getUsernameField().getText()) + "", getFont());
        usernamePanel.add(username, "wrap");
        scorePanel.add(score);

        userInfoContainer.add(usernamePanel);
        userInfoContainer.add(scorePanel);
        background.add(userInfoContainer);
        background.add(Box.createRigidArea(new Dimension(0, 150)));

        createLobby();
        twoPlayerLobby();
        threePlayerLobby();
        fourPlayerLobby();

        background.add(lobbyContainer);
    }

    /**
     * This is the main lobby that will allow the user to choose between 2 players, 3 players, and 4 players.
     */
    private void createLobby() {
        JPanel panelContainer = new JPanel();
        panelContainer.setLayout(new BoxLayout(panelContainer, BoxLayout.Y_AXIS));
        panelContainer.setOpaque(false);

        JPanel opponentSelectionContainer = new JPanel(); // container for the opponent selection text that allows two colours to be side-by-side
        opponentSelectionContainer.setOpaque(false);
        opponentSelectionContainer.setLayout(new BoxLayout(opponentSelectionContainer, BoxLayout.X_AXIS));

        JPanel opponentPanel = new JPanel(new MigLayout("center"));
        opponentPanel.setMaximumSize(new Dimension(300, 50));
        opponentPanel.setBackground(Color.decode("#6668CC"));
        opponentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel selectionPanel = new JPanel(new MigLayout("center"));
        selectionPanel.setMaximumSize(new Dimension(300, 50));
        selectionPanel.setBackground(Color.decode("#7597E3"));
        selectionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        Label opponentText = new Label("Opponent", getFont());
        Label selectionText = new Label("Selection", getFont());
        opponentPanel.add(opponentText);
        selectionPanel.add(selectionText);

        opponentSelectionContainer.add(opponentPanel);
        opponentSelectionContainer.add(selectionPanel);

        JPanel btnPanel = new JPanel(); // panel which stores all of our buttons
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
        btnPanel.setOpaque(false);

        twoPlayers = new JButton("2 PLAYERS");
        threePlayers = new JButton("3 PLAYERS");
        fourPlayers = new JButton("4 PLAYERS");
        customiseBtn(twoPlayers);
        customiseBtn(threePlayers);
        customiseBtn(fourPlayers);
        btnPanel.add(twoPlayers);
        btnPanel.add(threePlayers);
        btnPanel.add(fourPlayers);

        panelContainer.add(opponentSelectionContainer);
        panelContainer.add(Box.createRigidArea(new Dimension(0, 100)));
        panelContainer.add(btnPanel);
        lobbyContainer.add(panelContainer, "Main Lobby");
    }

    /**
     * This is the lobby for when the user selects 2 players.
     */
    private void twoPlayerLobby() {
        JPanel panelContainer = new JPanel();
        panelContainer.setLayout(new BoxLayout(panelContainer, BoxLayout.Y_AXIS));
        panelContainer.setOpaque(false);

        JPanel lobbyPanel = new JPanel(new MigLayout("center")); // the main panel which contains our rectangular box
        lobbyPanel.setMaximumSize(new Dimension(1000, 300));
        lobbyPanel.setBorder(new MatteBorder(10,10,10,10,Color.GREEN));
        lobbyPanel.setBackground(Color.decode("#7597E3"));
        lobbyPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        Label lobbyText = new Label("SEARCHING FOR PLAYERS", getFont());
        JLabel separator = new JLabel();
        separator.setPreferredSize(new Dimension(450, 0));
        separator.setBorder(new MatteBorder(2,2,2,2,Color.GREEN));
        map = "Citadel Flesh";
        Label mapText = new Label("Map - " + map    , getFont().deriveFont(20f)); // we should receive the map from the server

        player1_2Player = new Label("Player 1", getFont()); // a label to represent the players
        player2_2Player = new Label("Player 2", getFont());
        player2Score = new Label("score", getFont());

        lobbyPanel.add(lobbyText, "span 2, cell 0 0, center");
        lobbyPanel.add(separator, "span 2, cell 0 1, center");
        lobbyPanel.add(mapText, "span 2, cell 0 2, center");
        lobbyPanel.add(player1_2Player, "cell 0 3,gaptop 20, gapright 20");
        lobbyPanel.add(player2_2Player, "cell 1 3, gaptop 20");
        lobbyPanel.add(player2Score, "cell 1 4");

        readyBtn_2Player = new JButton("READY");
        File path = new File("src/resources/images/button.png");
        loadImage(path);
        customiseBtn(readyBtn_2Player);

        panelContainer.add(lobbyPanel);
        panelContainer.add(Box.createRigidArea(new Dimension(0, 100)));
        panelContainer.add(readyBtn_2Player);
        lobbyContainer.add(panelContainer, "Two Player Lobby");
    }

    /**
     * This is the lobby for when the user selects 3 players.
     */
    private void threePlayerLobby() {
        JPanel panelContainer = new JPanel();
        panelContainer.setLayout(new BoxLayout(panelContainer, BoxLayout.Y_AXIS));
        panelContainer.setOpaque(false);

        JPanel lobbyPanel = new JPanel(new MigLayout("center")); // the main panel which contains our rectangular box
        lobbyPanel.setMaximumSize(new Dimension(1000, 300));
        lobbyPanel.setBorder(new MatteBorder(10,10,10,10,Color.GREEN));
        lobbyPanel.setBackground(Color.decode("#7597E3"));
        lobbyPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        Label lobbyText = new Label("SEARCHING FOR PLAYERS", getFont());
        JLabel separator = new JLabel();
        separator.setPreferredSize(new Dimension(450, 0));
        separator.setBorder(new MatteBorder(2,2,2,2,Color.GREEN));
        map = "Citadel Flesh";
        Label mapText = new Label("Map - " + map    , getFont().deriveFont(20f)); // we should receive the map from the server

        player1_3Player = new Label("Player 1", getFont()); // a label to represent the players
        player2_3Player = new Label("Player 2", getFont());
        player3_3Player = new Label("Player 3", getFont());
        player2Score_3 = new Label("score", getFont());
        player3Score_3 = new Label("score", getFont());


        lobbyPanel.add(lobbyText, "span 3, cell 0 0, center");
        lobbyPanel.add(separator, "span 3, cell 0 1, center");
        lobbyPanel.add(mapText, "span 3, cell 0 2, center");
        lobbyPanel.add(player1_3Player, "cell 0 3, gaptop 20, gapright 20");
        lobbyPanel.add(player2_3Player, "cell 1 3, gaptop 20, gapright 20");
        lobbyPanel.add(player3_3Player, "cell 2 3, gaptop 20");
        lobbyPanel.add(player2Score_3, "cell 1 4");
        lobbyPanel.add(player3Score_3, "cell 2 4");


        readyBtn_3Player = new JButton("READY");
        File path = new File("src/resources/images/button.png");
        loadImage(path);
        customiseBtn(readyBtn_3Player);

        panelContainer.add(lobbyPanel);
        panelContainer.add(Box.createRigidArea(new Dimension(0, 100)));
        panelContainer.add(readyBtn_3Player);
        lobbyContainer.add(panelContainer, "Three Player Lobby");
    }

    /**
     * This is the lobby for when the user selects 4 players.
     */
    private void fourPlayerLobby() {
        JPanel panelContainer = new JPanel();
        panelContainer.setLayout(new BoxLayout(panelContainer, BoxLayout.Y_AXIS));
        panelContainer.setOpaque(false);

        JPanel lobbyPanel = new JPanel(new MigLayout("center")); // the main panel which contains our rectangular box
        lobbyPanel.setMaximumSize(new Dimension(1000, 300));
        lobbyPanel.setBorder(new MatteBorder(10,10,10,10,Color.GREEN));
        lobbyPanel.setBackground(Color.decode("#7597E3"));
        lobbyPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        Label lobbyText = new Label("SEARCHING FOR PLAYERS", getFont());
        JLabel separator = new JLabel();
        separator.setPreferredSize(new Dimension(450, 0));
        separator.setBorder(new MatteBorder(2,2,2,2,Color.GREEN));
        map = "Citadel Flesh";
        Label mapText = new Label("Map - " + map    , getFont().deriveFont(20f)); // we should receive the map from the server

        player1_4Player = new Label("Player 1", getFont()); // a label to represent the players
        player2_4Player = new Label("Player 2", getFont());
        player3_4Player = new Label("Player 3", getFont());
        player4_4Player = new Label("Player 4", getFont());

        player2Score_4 = new Label("score", getFont());
        player3Score_4 = new Label("score", getFont());
        player4Score_4 = new Label("score", getFont());


        lobbyPanel.add(lobbyText, "span 4, cell 0 0, center");
        lobbyPanel.add(separator, "span 4, cell 0 1, center");
        lobbyPanel.add(mapText, "span 4, cell 0 2, center");
        lobbyPanel.add(player1_4Player, "cell 0 3, gaptop 20, gapright 20");
        lobbyPanel.add(player2_4Player, "cell 1 3, gaptop 20, gapright 20");
        lobbyPanel.add(player3_4Player, "cell 2 3, gaptop 20, gapright 20");
        lobbyPanel.add(player4_4Player, "cell 3 3, gaptop 20");

        lobbyPanel.add(player2Score_4, "cell 1 4");
        lobbyPanel.add(player3Score_4, "cell 2 4");
        lobbyPanel.add(player4Score_4, "cell 3 4");


        readyBtn_4Player = new JButton("READY");
        File path = new File("src/resources/images/button.png");
        loadImage(path);
        customiseBtn(readyBtn_4Player);

        panelContainer.add(lobbyPanel);
        panelContainer.add(Box.createRigidArea(new Dimension(0, 100)));
        panelContainer.add(readyBtn_4Player);
        lobbyContainer.add(panelContainer, "Four Player Lobby");
    }

    /**
     * Customise the button without adding it to our panel.
     * @param btn the button to be customised.
     */
    protected void customiseBtn(JButton btn) {
        File path = new File("src/resources/images/button.png");
        loadImage(path);

        btn.setIcon(new ImageIcon(image));
        btn.setVerticalTextPosition(SwingConstants.CENTER);
        btn.setHorizontalTextPosition(SwingConstants.CENTER);

        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFont(getFont());
        btn.setForeground(Color.WHITE);

        // makes the button transparent
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
    }

    public JButton getTwoPlayers() {
        return twoPlayers;
    }

    public JButton getThreePlayers() {
        return threePlayers;
    }

    public JButton getFourPlayers() {
        return fourPlayers;
    }

    public JPanel getLobbyContainer() {
        return lobbyContainer;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public JButton getReadyBtn_2Player() {
        return readyBtn_2Player;
    }

    public JLabel getPlayer1_2Player() {
        return player1_2Player;
    }

    public JLabel getPlayer2_2Player() {
        return player2_2Player;
    }

    public JButton getReadyBtn_3Player() {
        return readyBtn_3Player;
    }

    public JLabel getPlayer1_3Player() {
        return player1_3Player;
    }

    public JLabel getPlayer2_3Player() {
        return player2_3Player;
    }

    public JLabel getPlayer3_3Player() {
        return player3_3Player;
    }

    public JButton getReadyBtn_4Player() {
        return readyBtn_4Player;
    }

    public JLabel getPlayer1_4Player() {
        return player1_4Player;
    }

    public JLabel getPlayer2_4Player() {
        return player2_4Player;
    }

    public JLabel getPlayer3_4Player() {
        return player3_4Player;
    }

    public JLabel getPlayer4_4Player() {
        return player4_4Player;
    }

}
