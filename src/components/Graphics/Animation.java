package components.Graphics;

import debugging.Debug;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class Animation {

    public float[] frameTimes;
    public BufferedImage[] frames;

    public Animation(String[] paths, float[] times) {
        frameTimes = times;
        ArrayList<BufferedImage> frameList = getImages(paths);
        frames = frameList.toArray(new BufferedImage[0]);
    }

    private ArrayList<BufferedImage> getImages(String[] imagePaths) {
        ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
        for (int i = 0; i < imagePaths.length; i++) {
            File imageFile = new File(imagePaths[i]);
            if (imageFile != null) {
                try {
                    images.add(ImageIO.read(imageFile));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return images;
    }
}
