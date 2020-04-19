package managers;

import GameDatabase.GameAccountOperations;
import logic.Time;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * The MenuModel will perform all relevant operations without needing to communicate to any of the views.
 */
public class MenuModel {

    private AudioManager audioManager;
    private AudioManager[] soundEffects;

    public static AudioManager kick;
    public static AudioManager coin;
    public static AudioManager gameOver;
    public static AudioManager booster;
    public static AudioManager health;
    public static AudioManager jump;
    public static AudioManager jumpBoost;
    public static AudioManager speed;
    public static AudioManager stun;
    public static AudioManager damage;

    public MenuModel() {
        createMusic();
        createSoundEffects();
    }

    /**
     * This method creates an audio manager which will allow us to control the music that is being played.
     */
    private void createMusic() {
        audioManager = new AudioManager(new File("src/resources/audio/Roboxel - Space Music.wav"));

        audioManager.pauseAudio();

    }

    /**
     * This method creates all sound effects and sets them to be paused.
     */
    private void createSoundEffects() {
        kick = new AudioManager(new File("src/resources/audio/PUNCH.wav"));
        kick.pauseAudio();
        coin = new AudioManager(new File("src/resources/audio/coin.wav"));
        coin.pauseAudio();
        gameOver = new AudioManager(new File("src/resources/audio/gameOver.wav"));
        gameOver.pauseAudio();
        booster = new AudioManager(new File("src/resources/audio/pickUpBooster.wav"));
        booster.pauseAudio();
        health = new AudioManager(new File("src/resources/audio/health.wav"));
        health.pauseAudio();
        jump = new AudioManager(new File("src/resources/audio/jump.wav"));
        jump.pauseAudio();
        jumpBoost = new AudioManager(new File("src/resources/audio/jumpBoost.wav"));
        jumpBoost.pauseAudio();
        speed = new AudioManager(new File("src/resources/audio/speed.wav"));
        speed.pauseAudio();
        stun = new AudioManager(new File("src/resources/audio/stun.wav"));
        stun.pauseAudio();
        damage = new AudioManager(new File("src/resources/audio/damage.wav"));
        damage.pauseAudio();

        soundEffects = new AudioManager[] {kick, coin, gameOver, booster, health, jump, jumpBoost, speed, stun, damage};
    }

    /**
     * Attempts to sign-in the user.
     * @param username of the user.
     * @param password of the user.
     * @return a boolean to indicate whether the user has logged in.
     */
    protected boolean signIn(String username, String password) {
        return GameAccountOperations.trySignIn(username, password);
    }

    /**
     * Attempts to sign-up the user.
     * @param username of the user.
     * @param password of the user.
     * @param authenticate the date of birth of the user.
     * @return a boolean to indicate whether the user has signed up.
     */
    protected boolean signUp(String username, String password, String authenticate) {
        return GameAccountOperations.trySignUp(username, password, authenticate);
    }

    /**
     * Attempts to reset the password for a user.
     * @param username of the user.
     * @param authenticate the date of birth of the user.
     * @param password of the user.
     * @return a boolean to indicate whether the user has reset their password.
     */
    protected boolean resetPassword(String username, String authenticate, String password) {
        return GameAccountOperations.tryResetPassword(username, authenticate, password);
    }

    /**
     * Creates a new instance of the engine by specifying the gamemode to be loaded in.
     * @param jFrame of the menu which we would like to temporarily hide.
     * @param gamemode that the user has selected.
     */
    protected void startGame(JFrame jFrame, String gamemode) {
        jFrame.setVisible(false);
        Window.running = true;
        Engine.Instance = new Engine(gamemode);
        Window.LoadWindow();
    }

    /**
     * Activate slow motion in game if the checkbox is selected.
     * @param checkBox the checkbox in question.
     */
    protected void slowMo(JCheckBox checkBox) {
        if (checkBox.isSelected()) {
            Time.timeScale = 0.3f;
        } else {
            Time.timeScale = 1f;
        }
    }

    /**
     * Toggle fullscreen if the checkbox is selected.
     * @param jFrame of the menu.
     * @param checkBox the fullscreen checkbox.
     * @param graphicsDevice allows us to toggle between fullscreen and windowed.
     */
    protected void fullscreen(JFrame jFrame, JCheckBox checkBox, GraphicsDevice graphicsDevice ) {
        if (checkBox.isSelected()) {
            jFrame.dispose();
            Window.fullscreenFlag = true;
            jFrame.setUndecorated(true);
            graphicsDevice.setFullScreenWindow(jFrame);
            jFrame.setVisible(true);
        } else {
            jFrame.dispose();
            Window.fullscreenFlag = false;
            jFrame.setUndecorated(false);
            graphicsDevice.setFullScreenWindow(null);
            jFrame.setVisible(true);
        }
    }

    /**
     * Allows the user to change the colour of the health bar.
     * @param comboBox which presents the list of colours to the user.
     */
    protected void healthBarColour(JComboBox comboBox) {
        switch (comboBox.getSelectedItem().toString()) {
            case "GREEN":
                Window.healthBarColour = Color.GREEN;
                break;
            case "MAGENTA":
                Window.healthBarColour = Color.MAGENTA;
                break;
            case "BLUE":
                Window.healthBarColour = Color.BLUE;
                break;
            case "YELLOW":
                Window.healthBarColour = Color.YELLOW;
                break;
            case "PINK":
                Window.healthBarColour = Color.PINK;
                break;
            case "ORANGE":
                Window.healthBarColour = Color.ORANGE;
                break;
            default:
                break;
        }
    }

    /**
     * Updates the volume value of the music.
     * @param curMusicVol the text that represents the current music value.
     * @param musicSlider the slider that the user can change.
     */
    protected void updateMusic(JLabel curMusicVol, JSlider musicSlider) {
        curMusicVol.setText(musicSlider.getValue() + "%");
        audioManager.setAudio(musicSlider.getValue() / 100f);
    }

    /**
     * Updates the sound effects that are played in game.
     * @param gameplaySlider the slider that user can change.
     * @param audioManager the audioManager which controls the audio for a specific sound effect.
     */
    protected void updateSoundEffects(JSlider gameplaySlider, AudioManager audioManager) {
        audioManager.setAudio(gameplaySlider.getValue() / 100f);
    }

    /**
     * If the user clicks to see the high-scores, then refresh the panels by retrieving
     * the updated scores.
     * @param statsView the StatsView that contains our singleplayer and multiplayer high-score panels.
     */
    protected void updateLeaderboards(StatsView statsView) {
        statsView.getStatsContainer().remove(statsView.getSingleplayerPanel());
        statsView.getStatsContainer().remove(statsView.getMultiplayerPanel());
        statsView.getBackground().remove(statsView.getStatsContainer());
        statsView.singleplayerScores();
        statsView.multiplayerScores();
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }

    public AudioManager[] getSoundEffects() {
        return soundEffects;
    }
}
