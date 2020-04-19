package managers;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioManager {
    private AudioInputStream audioInputStream;
    private Clip clip;

    /**
     * Initialises a clip that allows us to play audio.
     * @param file which is to be played.
     */
    public AudioManager(File file) {
        try {
            audioInputStream = AudioSystem.getAudioInputStream(file); // gets an audio input stream from the file specified
            clip = AudioSystem.getClip(); // gets a clip that allows us to play back the audio
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts the audio by checking if there is a clip currently playing. If there is, then
     * reset the audio playing and play the new clip.
     */
    public void startAudio() {
        if (clip.getMicrosecondLength() != clip.getMicrosecondPosition()) {
            pauseAudio();
        }
        clip.setMicrosecondPosition(0);
        clip.start();
    }

    /**
     * Pauses the audio and restarts the audio clip from the beginning.
     */
    protected void pauseAudio() {
        clip.stop();
        clip.setMicrosecondPosition(0);
    }

    /**
     * Stops the audio and closes the clip.
     */
    protected void stopAudio() {
        try {
            audioInputStream.close();
            clip.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the volume of a clip by normalising the scale to linear (0.0 to 1.0)
     * @param volume is the float value specified by the slider from the audio options.
     */
    protected void setAudio(float volume) {
        FloatControl floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        floatControl.setValue(20f * (float) Math.log10(volume));
    }

    public Clip getClip() {
        return clip;
    }
}
