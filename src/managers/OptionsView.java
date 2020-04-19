package managers;

import customClasses.InputKeys;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.io.File;

/**
 * The OptionsView is similar to the MenuView but is responsible for displaying all components that will reside in the options submenu.
 */
public class OptionsView extends View {

    private JPanel optionsContainer;
    private CardLayout cardLayout;

    private JButton optionsBtn;
    private JButton gameplayBtn;
    private JButton controlsBtn;
    private JButton videoBtn;
    private JButton audioBtn;

    private JCheckBox slowMoCheckBox; // gameplay

    private JCheckBox fullscrenCheckBox; // video
    private JCheckBox fpsCheckBox;
    private JComboBox healthBarComboBox;

    Label[] controls; //controls

    private JSlider musicSlider; // audio
    private JSlider gameplaySlider;
    private JLabel curMusicVol;
    private JLabel gameplayVol;

    public OptionsView() {
        File path = new File("src/resources/icons/options.png");
        loadImage(path);
        optionsBtn = new JButton("OPTIONS", new ImageIcon(super.image));
        optionsBtn.setHorizontalTextPosition(SwingConstants.LEFT);
        optionsBtn.setIconTextGap(20);

        gameplayBtn = new JButton("GAMEPLAY");
        controlsBtn = new JButton("CONTROLS");
        videoBtn = new JButton("VIDEO");
        audioBtn = new JButton("AUDIO");

        musicSlider = new Slider();
        gameplaySlider = new Slider();

        createAndShowGUI();
    }

    /**
     * Responsible for creating and displaying all components and visually structuring them.
     */
    private void createAndShowGUI() {
        // using a mainPanel (background) which adds both the title and other components
        JImage background = super.loadResources();
        background.setLayout(new BoxLayout(background, BoxLayout.Y_AXIS));
        JPanel titlePanel = super.createTitlePanel(super.getBackBtn(), optionsBtn);
        background.add(titlePanel);
        background.add(Box.createRigidArea(new Dimension(0, 100))); // add the buttons to the options view

        JPanel options = new JPanel();
        options.setLayout(new BoxLayout(options, BoxLayout.X_AXIS));
        options.setOpaque(false);

        super.setFont(super.getFont().deriveFont(30f));
        File path = new File("src/resources/images/button.png");
        super.loadImage(path);
        addButton(gameplayBtn, options);
        addButton(controlsBtn, options);
        addButton(videoBtn, options);
        addButton(audioBtn, options);

        background.add(options);
        createOptions(background);
    }

    /**
     * Populates the background panel with the necessary options.
     * @param background the panel for the options to be enclosed in.
     */
    private void createOptions(JImage background) {
        optionsContainer = new JPanel();
        optionsContainer.setOpaque(false);
        optionsContainer.setLayout(new CardLayout());
        cardLayout = (CardLayout) optionsContainer.getLayout();

        gameplayOptions();
        controlOptions();
        videoOptions();
        audioOptions();
        background.add(optionsContainer);
    }

    /**
     * Responsible for adding all GUI components related to the gameplay options panel.
     */
    private void gameplayOptions() {
        JPanel gameplayContainer = new JPanel(); // container that will hold our two panels
        gameplayContainer.setLayout(new BoxLayout(gameplayContainer, BoxLayout.Y_AXIS));
        gameplayContainer.setOpaque(false);

        JPanel gameplayPanel = new JPanel(new MigLayout("gap 0", "[fill, 100%]"));
        gameplayPanel.setOpaque(false); // this panel contains any settings that are related to the gameplay

        Label inGame = new Label("IN-GAME SETTINGS", getFont());
        inGame.setOpaque(false);
        inGame.setForeground(Color.decode("#AF8FE4"));
        Label slowMo = new Label("SLOW MOTION", getFont());
        slowMoCheckBox = new CheckBox();

        gameplayPanel.add(inGame, "gaptop 100, gapleft 260, wrap");
        gameplayPanel.add(slowMo, "gapleft 260, h :64:");
        gameplayPanel.add(slowMoCheckBox, "gapright 260, h :64:, wrap");

        JPanel boosterPanel = new JPanel(new MigLayout());
        boosterPanel.setOpaque(false); // this panel includes information about our boosters

        Label powerUps = new Label("POWER-UPS", getFont());
        powerUps.setOpaque(false);
        powerUps.setForeground(Color.decode("#AF8FE4"));

        Label psychicBoost = new Label("", getFont());
        psychicBoost.setIcon(new ImageIcon(String.valueOf(new File("src/resources/sprites/boosters/basebooster.png"))));
        psychicBoost.setOpaque(false);
        Label psychicTitle = new Label("PSYCHIC:", getFont().deriveFont(28f));
        psychicTitle.setForeground(Color.decode("#6668CC"));
        psychicTitle.setOpaque(false);
        Label psychicIns = new Label("A genetic mutation which gives you the ultimate power to telepathically attack an enemy. ", getFont().deriveFont(20f));
        psychicIns.setOpaque(false);

        boosterPanel.add(powerUps, "gapleft 260, span 3, wrap");
        boosterPanel.add(psychicBoost, "gapleft 260, h :45:");
        boosterPanel.add(psychicTitle, "gapleft 10, h :45:");
        boosterPanel.add(psychicIns, "gapleft 10, wrap");

        Label superLeaping = new Label("", getFont());
        superLeaping.setIcon(new ImageIcon(String.valueOf(new File("src/resources/sprites/boosters/jumpbooster.png"))));
        superLeaping.setOpaque(false);
        Label superLeapingTitle = new Label("SUPER LEAPING:", getFont().deriveFont(28f));
        superLeapingTitle.setForeground(Color.decode("#6668CC"));
        superLeapingTitle.setOpaque(false);
        Label superLeapingIns = new Label("A super Apestronaut ability which gives you an augmented power to leap through dimensions. ", getFont().deriveFont(20f));
        superLeapingIns.setOpaque(false);

        boosterPanel.add(superLeaping, "gaptop 10, gapleft 260, h :45:");
        boosterPanel.add(superLeapingTitle, "gapleft 10, h :45:");
        boosterPanel.add(superLeapingIns, "gapleft 10, wrap");

        Label radiantBolt = new Label("", getFont());
        radiantBolt.setIcon(new ImageIcon(String.valueOf(new File("src/resources/sprites/boosters/speedbooster.png"))));
        radiantBolt.setOpaque(false);
        Label radiantBoltTitle = new Label("RADIANT BOLT:", getFont().deriveFont(28f));
        radiantBoltTitle.setForeground(Color.decode("#6668CC"));
        radiantBoltTitle.setOpaque(false);
        Label radiantBoltIns = new Label("Once you have equipped the Radiant Bolt, your enemies will be dazed by the sudden", getFont().deriveFont(20f));
        Label radiantBoltIns2 = new Label("power that you possess. This fast and deadly power lies in the battlefield!", getFont().deriveFont(20f));
        radiantBoltIns.setOpaque(false);
        radiantBoltIns2.setOpaque(false);

        boosterPanel.add(radiantBolt, "gaptop 10, gapleft 260, h :45:");
        boosterPanel.add(radiantBoltTitle, "gapleft 10, h :45:");
        boosterPanel.add(radiantBoltIns, "gapleft 10, wrap");
        boosterPanel.add(radiantBoltIns2, "gapleft 10, cell 2 6, wrap");

        Label chronoFusion = new Label("", getFont());
        chronoFusion.setIcon(new ImageIcon(String.valueOf(new File("src/resources/sprites/boosters/stunbooster.png"))));
        chronoFusion.setOpaque(false);
        Label chronoFusionTitle = new Label("CHRONO FUSION:", getFont().deriveFont(28f));
        chronoFusionTitle.setForeground(Color.decode("#6668CC"));
        chronoFusionTitle.setOpaque(false);
        Label chronoFusionIns = new Label("Use the ancient powers of Chrono Fusion to stop time against your enemy.", getFont().deriveFont(20f));
        chronoFusionIns.setOpaque(false);

        boosterPanel.add(chronoFusion, "gapleft 260, h :45:");
        boosterPanel.add(chronoFusionTitle, "gapleft 10, h :45:");
        boosterPanel.add(chronoFusionIns, "gapleft 10, wrap");

        Label berserker = new Label("", getFont());
        berserker.setIcon(new ImageIcon(String.valueOf(new File("src/resources/sprites/boosters/damagebooster.png"))));
        berserker.setOpaque(false);
        Label berserkerTitle = new Label("BERSERKER:", getFont().deriveFont(28f));
        berserkerTitle.setForeground(Color.decode("#6668CC"));
        berserkerTitle.setOpaque(false);
        Label berserkerPiercerIns = new Label("A penetrating blast that will crush your opponent. ", getFont().deriveFont(20f));
        berserkerPiercerIns.setOpaque(false);

        boosterPanel.add(berserker, "gaptop 10, gapleft 260, h :45:");
        boosterPanel.add(berserkerTitle, "gaptop 10, gapleft 10, h :45:");
        boosterPanel.add(berserkerPiercerIns, "gapleft 10, wrap");

        gameplayContainer.add(gameplayPanel);
        gameplayContainer.add(boosterPanel);
        optionsContainer.add(gameplayContainer, "Gameplay Options");
    }

    /**
     * Responsible for adding all GUI components related to the controls options panel.
     */
    private void controlOptions() {
        JPanel controlContainer = new JPanel(); // container that will hold our two panels
        controlContainer.setLayout(new BoxLayout(controlContainer, BoxLayout.Y_AXIS));
        controlContainer.setOpaque(false);

        JPanel controlPanel = new JPanel(new MigLayout("gap 0", "[fill, 100%]"));
        controlPanel.setOpaque(false);

        Label bindings = new Label("BINDINGS", getFont());
        bindings.setOpaque(false);
        bindings.setForeground(Color.decode("#AF8FE4"));
        Label key = new Label("KEY", getFont());
        key.setOpaque(false);
        key.setForeground(Color.decode("#AF8FE4"));

        controlPanel.add(bindings, "gaptop 100, gapleft 260");
        controlPanel.add(key, "gapright 260, wrap");

        Label move_forward = new Label("MOVE FORWARD", getFont());
        Label move_left = new Label("MOVE LEFT", getFont());
        Label move_right = new Label("MOVE RIGHT", getFont());
        Label kick = new Label("KICK", getFont());
        Label booster = new Label("USE BOOSTER", getFont());
        Label pause = new Label("PAUSE GAME", getFont());
        Label move_foward_key = new Label(KeyEvent.getKeyText(InputKeys.UP).toUpperCase(), getFont());
        Label move_left_key = new Label(KeyEvent.getKeyText(InputKeys.LEFT).toUpperCase(), getFont());
        Label move_right_key = new Label(KeyEvent.getKeyText(InputKeys.RIGHT).toUpperCase(), getFont());
        Label kick_key = new Label(KeyEvent.getKeyText(InputKeys.KICK_OR_SQUASH).toUpperCase(), getFont());
        Label booster_key = new Label(KeyEvent.getKeyText(InputKeys.BOOSTER).toUpperCase(), getFont());
        Label pause_key = new Label(KeyEvent.getKeyText(InputKeys.PAUSE).toUpperCase(), getFont());
        controls = new Label[] {move_foward_key, move_left_key, move_right_key, kick_key, booster_key, pause_key};

        controlPanel.add(move_forward, "gapleft 260");
        controlPanel.add(move_foward_key, "gapright 260, wrap");
        controlPanel.add(move_left, "gapleft 260");
        controlPanel.add(move_left_key, "gapright 260, wrap");
        controlPanel.add(move_right, "gapleft 260");
        controlPanel.add(move_right_key, "gapright 260, wrap");
        controlPanel.add(kick, "gapleft 260");
        controlPanel.add(kick_key, "gapright 260, wrap");
        controlPanel.add(booster, "gapleft 260");
        controlPanel.add(booster_key, "gapright 260, wrap");
        controlPanel.add(pause, "gapleft 260");
        controlPanel.add(pause_key, "gapright 260, wrap");

        JPanel bindingsPanel = new JPanel(new MigLayout());
        bindingsPanel.setOpaque(false); // this panel includes information about how to change key bindings

        Label bindingsExplained = new Label("BINDINGS EXPLAINED", getFont());
        bindingsExplained.setOpaque(false);
        bindingsExplained.setForeground(Color.decode("#AF8FE4"));

        Label bindingsInfo = new Label("To assign a key for a binding, click the key of a binding that you would like to change and enter any desired key.", getFont().deriveFont(20f));
        Label bindingsInfo2 = new Label("To update the binding, exit the highlighted key.", getFont().deriveFont(20f));
        bindingsInfo.setOpaque(false);
        bindingsInfo2.setOpaque(false);

        bindingsPanel.add(bindingsExplained, "gapleft 260, wrap");
        bindingsPanel.add(bindingsInfo, "gapleft 260, wrap");
        bindingsPanel.add(bindingsInfo2, "gapleft 260");

        controlContainer.add(controlPanel);
        controlContainer.add(bindingsPanel);
        optionsContainer.add(controlContainer, "Controls Options");
    }

    /**
     * Responsible for adding all GUI components related to the video options panel.
     */
    private void videoOptions() {
        JPanel videoPanel = new JPanel(new MigLayout("gap 0", "[fill, 100%]"));
        videoPanel.setOpaque(false);

        Label windowSettings = new Label("WINDOW SETTINGS", getFont());
        windowSettings.setOpaque(false);
        windowSettings.setForeground(Color.decode("#AF8FE4"));
        Label fullscreen = new Label("FULLSCREEN", getFont());
        fullscrenCheckBox = new CheckBox();
        fullscrenCheckBox.setSelected(true);

        videoPanel.add(windowSettings, "gaptop 100, gapleft 260, wrap");
        videoPanel.add(fullscreen, "gapleft 260, h :64:");
        videoPanel.add(fullscrenCheckBox, "gapright 260, h :64:, wrap");

        Label inGame = new Label("IN-GAME OVERLAY", getFont());
        inGame.setOpaque(false);
        inGame.setForeground(Color.decode("#AF8FE4"));

        Label showFps = new Label("SHOW FPS", getFont());
        fpsCheckBox = new CheckBox();
        Label healthBar = new Label("HEALTH COLOUR", getFont());
        String[] colours = {"GREEN", "MAGENTA", "BLUE", "YELLOW", "PINK", "ORANGE"};
        healthBarComboBox = new JComboBox(colours);
        healthBarComboBox.setSelectedIndex(0);
        healthBarComboBox.setBackground(Color.decode("#6668CC"));
        healthBarComboBox.setFont(getFont());
        healthBarComboBox.setForeground(Color.WHITE);

        videoPanel.add(inGame, "gaptop 100, gapleft 260, wrap");
        videoPanel.add(showFps, "gapleft 260, h :64:");
        videoPanel.add(fpsCheckBox, "gapright 260, h :64:, wrap");
        videoPanel.add(healthBar, "gapleft 260, h :64:");
        videoPanel.add(healthBarComboBox, "gapright 260, h :64:, wrap");

        optionsContainer.add(videoPanel, "Video Options");
    }

    /**
     * Responsible for adding all GUI components related to the audio options panel.
     */
    private void audioOptions() {
        JPanel audioPanel = new JPanel(new MigLayout("center, gap 0", "[fill, 100%]"));
        audioPanel.setOpaque(false);

        Label volumeSettings = new Label("VOLUME SETTINGS", getFont());
        volumeSettings.setOpaque(false);
        volumeSettings.setForeground(Color.decode("#AF8FE4"));
        audioPanel.add(volumeSettings, "gaptop 100, gapleft 260, wrap");

        Label musicText = new Label("MUSIC VOLUME", getFont());
        Label gameplayText = new Label("GAMEPLAY VOLUME", getFont());
        curMusicVol = new Label("50%", getFont());
        gameplayVol = new Label("50%", getFont());

        audioPanel.add(musicText, "gapleft 260, right, h :64:");
        audioPanel.add(musicSlider, "center, h :64:");
        audioPanel.add(curMusicVol, "gapright 260, wrap, h :64:");
        audioPanel.add(gameplayText, "gapleft 260, right, h :64:");
        audioPanel.add(gameplaySlider, "center, h :64:");
        audioPanel.add(gameplayVol, "gapright 260, wrap, h :64:");

        optionsContainer.add(audioPanel, "Audio Options");
    }

    /**
     * Adds an option item (button) to a panel and customises it.
     * @param btn the menu item to be added.
     */
    private void addButton(JButton btn, JPanel options) {
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

        options.add(btn);
    }

    public JSlider getMusicSlider() {
        return musicSlider;
    }

    public JLabel getCurMusicVol() {
        return curMusicVol;
    }

    public JSlider getGameplaySlider() {
        return gameplaySlider;
    }

    public JLabel getGameplayVol() {
        return gameplayVol;
    }

    public JButton getGameplayBtn() {
        return gameplayBtn;
    }

    public JButton getControlsBtn() {
        return controlsBtn;
    }

    public JButton getVideoBtn() {
        return videoBtn;
    }

    public JButton getAudioBtn() {
        return audioBtn;
    }

    public JPanel getOptionsContainer() {
        return optionsContainer;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public JCheckBox getFpsCheckBox() {
        return fpsCheckBox;
    }

    public JCheckBox getFullscrenCheckBox() {
        return fullscrenCheckBox;
    }


    public JComboBox getHealthBarComboBox() {
        return healthBarComboBox;
    }

    public JCheckBox getSlowMoCheckBox() {
        return slowMoCheckBox;
    }

    public Label[] getControls() {
        return controls;
    }

    private class Slider extends JSlider {
        public Slider() {
            setBackground(Color.decode("#6668CC"));
        }
    }

    public class Label extends JLabel {
        public Label(String text, Font font) {
            super(text);
            setFont(font);
            setForeground(Color.WHITE);
            setOpaque(true);
            setBackground(Color.decode("#6668CC"));
        }
    }

    private class CheckBox extends JCheckBox {
        public CheckBox() {
            setBackground(Color.decode("#6668CC"));
        }
    }

}
