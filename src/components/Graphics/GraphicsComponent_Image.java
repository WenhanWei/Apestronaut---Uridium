package components.Graphics;

import customClasses.Vector2;
import debugging.Debug;
import logic.Time;
import org.w3c.dom.Element;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A single-image graphics element
 */
public class GraphicsComponent_Image extends GraphicsComponent {

    public int lookDir = 1;

    public static String COMPONENT_NAME = "GraphicsComponent_Image";

    public GraphicsComponent_Image(Vector2 size, String imagePath, int orderInLayer) {
        this.size = size;
        image = imageFromPath(imagePath);
        defaultImage = image;

        this.orderInLayer = orderInLayer;
    }

    public static Image scale(BufferedImage sbi, double fWidth, double fHeight) {
    	int dWidth = (int) (sbi.getWidth() * fWidth);
    	int dHeight = (int) (sbi.getHeight() * fHeight);
    	BufferedImage dbi = null;
        if(sbi != null) {
            dbi = new BufferedImage(dWidth, dHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = dbi.createGraphics();
            AffineTransform at = AffineTransform.getScaleInstance(fWidth, fHeight);
            g.drawRenderedImage(sbi, at);
        }
        return dbi;
    }

    @Override
    public void paint(Graphics2D graphics) {
        if (gameObject != null) {
            if (gameObject.GetTransform() != null) {
                if (lookDir == 1) {
                    graphics.drawImage(image, (int)(gameObject.GetTransform().pos.x + offset.x),
                            (int)(gameObject.GetTransform().pos.y + offset.y), null);
                } else {
                    graphics.drawImage(image, (int)(gameObject.GetTransform().pos.x + offset.x) + image.getWidth(),
                            (int)(gameObject.GetTransform().pos.y + offset.y), -image.getWidth(), image.getHeight(),
                            null);
                }
            } else {
                Debug.Error("[Transform] null");
            }
        } else {
            Debug.Error("[GameObject] null");
        }
    }

    @Override
    public void ResolveXML(Element element) {
        if (ResolveData(element, "sizeX") != null)
            size.x = Integer.parseInt(ResolveData(element, "sizeX"));
        if (ResolveData(element, "sizeY") != null)
            size.y = Integer.parseInt(ResolveData(element, "sizeY"));
    }
}
