package managers;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * PlayView is to be shown when the user would like to select a gamemode to play.
 */
public class PlayView extends View {
    private JPanel background;
    private JButton playBtn;

    private JLabel singleplayer;
    private JLabel multiplayer;
    private JLabel practice;
    private JLabel levelEditor;

    public PlayView() {
        File path = new File("src/resources/icons/play.png");
        loadImage(path);
        playBtn = new JButton("PLAY", new ImageIcon(super.image));
        playBtn.setIconTextGap(20);

        singleplayer = loadGamemodeInfo(new File("src/resources/images/singleplayer.png"));
        multiplayer = loadGamemodeInfo(new File("src/resources/images/multiplayer.png"));
        practice = loadGamemodeInfo(new File("src/resources/images/practice.png"));
        levelEditor = loadGamemodeInfo(new File("src/resources/images/levelEditor.png"));

        createAndShowGUI();
    }

    /**
     * Responsible for creating all components and visually structuring them.
     */
    private void createAndShowGUI() {
        // using a mainPanel (background) which adds both the title and the gamemodes
        background = super.loadResources();
        background.setLayout(new BoxLayout(background, BoxLayout.Y_AXIS));

        JPanel titlePanel = super.createTitlePanel(super.getBackBtn(), playBtn);
        background.add(titlePanel); 
        background.add(Box.createRigidArea(new Dimension(0, 100)));

        addGamemodes();
    }

    /**
     * Adds the gamemode images to the imagePanel which is then added to the background panel.
     */
    private void addGamemodes() {
        JPanel imagePanel = new JPanel(new MigLayout("center", "[]15[]", "[]15[]"));
        imagePanel.setOpaque(false);
        imagePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        imagePanel.add(singleplayer);
        imagePanel.add(multiplayer, "wrap");
        imagePanel.add(practice);
        imagePanel.add(levelEditor);

        background.add(imagePanel);
    }

    /**
     * Loads the gamemode description image.
     */
    private JLabel loadGamemodeInfo(File path) {
        super.loadImage(path);
        JLabel info = new JLabel();
        info.setIcon(new ImageIcon(super.image));
        return info;
    }

    public JLabel getSingleplayer() {
        return singleplayer;
    }

    public JLabel getMultiplayer() {
        return multiplayer;
    }

    public JLabel getPractice() {
        return practice;
    }

    public JLabel getLevelEditor() {
        return levelEditor;
    }
}
