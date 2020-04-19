package components.Graphics;

import customClasses.Vector2;
import debugging.Debug;
import org.w3c.dom.Element;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

/**
 * A tileable graphics element, which tiles based on size
 */
public class GraphicsComponent_Tileable extends GraphicsComponent {

    public static String COMPONENT_NAME = "GraphicsComponent_Tileable";
    ArrayList<BufferedImage> tileImages = new ArrayList<>();
    int fullImageCount;
    BufferedImage partialImage;

    public GraphicsComponent_Tileable(Vector2 size, String imagePath, int orderInLayer) {
        this.size = size;
        File imageFile = new File(imagePath);
        if (imageFile != null) {
            try {
                image = ImageIO.read(imageFile);
                defaultImage = image;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.orderInLayer = orderInLayer;


    }

    @Override
    public void Update() {
        fullImageCount = (int)size.x / image.getWidth();
        if ((int)size.x % image.getWidth() > 0) {
            partialImage = image.getSubimage(0,0,(int)size.x % image.getWidth(), image.getHeight());
        }
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
                for (int i = 0; i < fullImageCount; i++) {
                    graphics.drawImage(image, (int)gameObject.GetTransform().pos.x + i*image.getWidth(),
                            (int)gameObject.GetTransform().pos.y,null);
                }
                graphics.drawImage(partialImage, (int)gameObject.GetTransform().pos.x + fullImageCount*image.getWidth(),
                        (int)gameObject.GetTransform().pos.y, null);

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
