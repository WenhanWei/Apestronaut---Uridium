package managers;

import javax.swing.*;
import java.awt.*;

/**
 * This class extends JPanel which allows us to add an image by overriding paintComponent.
 */
public class JImage extends JPanel {
    private Image image;

    public JImage(Image image) {
        this.image = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this); // by using getWidth() and getHeight(), it will now dynamically scale
    }
}
