package managers;

import GameDatabase.PlayerAccount;
import GameDatabase.GameAccountOperations;
import net.miginfocom.swing.MigLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.io.File;

/**
 * The StatsView is similar to the MenuView but is responsible for displaying all components that will reside in the stats submenu.
 */
public class StatsView extends View {

    private JImage background;
    private JPanel statsContainer;
    private CardLayout cardLayout;
    private JPanel singleplayerPanel;
    private JPanel multiplayerPanel;

    private JButton statsBtn;
    private JButton singleplayer;
    private JButton multiplayer;

    public StatsView() {
        File path = new File("src/resources/icons/stats.png");
        loadImage(path);
        statsBtn = new JButton("STATS", new ImageIcon(image));
        statsBtn.setHorizontalTextPosition(SwingConstants.LEFT);
        statsBtn.setIconTextGap(20);

        singleplayer = new JButton("SINGLEPLAYER");
        multiplayer = new JButton("MULTIPLAYER");

        createAndShowGUI();
    }

    /**
     * Responsible for creating and displaying all components and visually structuring them.
     */
    private void createAndShowGUI() {
        // using a mainPanel (background) which adds both the title and other components
        background = loadResources();
        background.setLayout(new BoxLayout(background, BoxLayout.Y_AXIS));
        JPanel titlePanel = super.createTitlePanel(super.getBackBtn(), statsBtn);
        background.add(titlePanel);
        background.add(Box.createRigidArea(new Dimension(0, 100)));
        displayStats();
    }

    /**
     * Populates the background panel with the information related to the leaderboards.
     */
    private void displayStats() {
        JPanel statsItems = new JPanel();
        statsItems.setLayout(new BoxLayout(statsItems, BoxLayout.X_AXIS));
        statsItems.setOpaque(false);

        statsContainer = new JPanel();
        statsContainer.setLayout(new CardLayout());
        statsContainer.setBackground(Color.decode("#7597E3"));
        statsContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        statsContainer.setMaximumSize(new Dimension(800, 700));
        statsContainer.setBorder(new CompoundBorder( // CompoundBorder allows us to add two borders to our panel
                BorderFactory.createMatteBorder(20, 20, 20, 20, Color.decode("#6668CC")),
                BorderFactory.createMatteBorder(20, 20, 20, 20, Color.decode("#AF8FE4"))));
        cardLayout = (CardLayout) statsContainer.getLayout();

        setFont(getFont().deriveFont(30f));
        File path = new File("src/resources/images/button.png");
        loadImage(path);
        addButton(singleplayer, statsItems, true);
        addButton(multiplayer, statsItems, true);
        background.add(statsItems);

        background.add(Box.createRigidArea(new Dimension(0, 50)));
        singleplayerScores();
        multiplayerScores();
    }

    /**
     * Displays the singleplayer scores by statically accessing the database and extracting the corresponding scores.
     */
    protected void singleplayerScores() {
        singleplayerPanel = new JPanel(new MigLayout("center", "[]100[]100[]"));
        singleplayerPanel.setOpaque(false); // our singleplayer panel that will contain all related components
        singleplayerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        Label hallOfFame = new Label("HALL OF FAME", getFont());
        Label best10 = new Label("TODAY'S BEST 10", getFont());
        hallOfFame.setForeground(Color.decode("#181A2D"));
        best10.setForeground(Color.decode("#6668CC"));

        JLabel posText = new Label("" , getFont());
        JLabel usernameText = new Label("NAME", getFont());
        JLabel scoreText = new Label("SCORE", getFont());

        singleplayerPanel.add(hallOfFame, "span 3, center, wrap");
        singleplayerPanel.add(best10, "span 3, center, wrap");
        singleplayerPanel.add(posText);
        singleplayerPanel.add(usernameText);
        singleplayerPanel.add(scoreText, "wrap");

        ArrayList<JLabel> position = new ArrayList<>(); // Create an ArrayList object
        ArrayList<JLabel> usernames = new ArrayList<>();
        ArrayList<JLabel> scores = new ArrayList<>();
        List<PlayerAccount> singlePlayerScores = GameAccountOperations.tryShowSinglePlayerScore();

        int pos = 1;
        for (PlayerAccount playerAccount : singlePlayerScores) {
            if (pos == 1) {
               position.add(new Label(pos + "ST", getFont()));
            } else if (pos == 2) {
                position.add(new Label(pos + "ND", getFont()));
            } else if (pos == 3) {
                position.add(new Label(pos + "RD", getFont()));
            } else {
                position.add(new Label(pos + "TH", getFont()));
            }
            usernames.add(new Label(playerAccount.getUsername(), getFont()));
            scores.add(new Label(playerAccount.getSinglePlayerScore() + "", getFont()));
            pos++;
        }
        for (int i = 0; i < usernames.size(); i++) {
            singleplayerPanel.add(position.get(i));
            singleplayerPanel.add(usernames.get(i));
            singleplayerPanel.add(scores.get(i), "wrap");
        }

        statsContainer.add(singleplayerPanel, "Singleplayer Scores");
        background.add(statsContainer);
    }

    /**
     * Displays the multiplayer scores by statically accessing the database and extracting the corresponding scores.
     */
    protected void multiplayerScores() {
        multiplayerPanel = new JPanel(new MigLayout("center", "[]100[]100[]"));
        multiplayerPanel.setOpaque(false); // our multiplayer panel that will contain all related components
        multiplayerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        Label hallOfFame = new Label("HALL OF FAME", getFont());
        Label best10 = new Label("TODAY'S BEST 10", getFont());
        hallOfFame.setForeground(Color.decode("#181A2D"));
        best10.setForeground(Color.decode("#6668CC"));

        JLabel posText = new Label("" , getFont());
        JLabel usernameText = new Label("NAME", getFont());
        JLabel scoreText = new Label("SCORE", getFont());

        multiplayerPanel.add(hallOfFame, "span 3, center, wrap");
        multiplayerPanel.add(best10, "span 3, center, wrap");
        multiplayerPanel.add(posText);
        multiplayerPanel.add(usernameText);
        multiplayerPanel.add(scoreText, "wrap");

        ArrayList<JLabel> position = new ArrayList<>(); // Create an ArrayList object
        ArrayList<JLabel> usernames = new ArrayList<>();
        ArrayList<JLabel> scores = new ArrayList<>();
        List<PlayerAccount> multiPlayerScores = GameAccountOperations.tryShowMultiPlayerScore();

        int pos = 1;
        for (PlayerAccount playerAccount : multiPlayerScores) {
            if (pos == 1) {
                position.add(new Label(pos + "ST", getFont()));
            } else if (pos == 2) {
                position.add(new Label(pos + "ND", getFont()));
            } else if (pos == 3) {
                position.add(new Label(pos + "RD", getFont()));
            } else {
                position.add(new Label(pos + "TH", getFont()));
            }
            usernames.add(new Label(playerAccount.getUsername(), getFont()));
            scores.add(new Label(playerAccount.getMultiPlayerScore() + "", getFont()));
            pos++;
        }
        for (int i = 0; i < usernames.size(); i++) {
            multiplayerPanel.add(position.get(i));
            multiplayerPanel.add(usernames.get(i));
            multiplayerPanel.add(scores.get(i), "wrap");
        }

        statsContainer.add(multiplayerPanel, "Multiplayer Scores");
        background.add(statsContainer);
    }

    public JButton getSingleplayer() {
        return singleplayer;
    }

    public JButton getMultiplayer() {
        return multiplayer;
    }

    public JPanel getStatsContainer() {
        return statsContainer;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public JPanel getSingleplayerPanel() {
        return singleplayerPanel;
    }

    public JPanel getMultiplayerPanel() {
        return multiplayerPanel;
    }

    public JPanel getBackground() {
        return background;
    }

}
