package components.Graphics;

import org.w3c.dom.Element;


import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import customClasses.Vector2;
import debugging.Debug;
import logic.Time;

/**
 * An animated graphics element
 */
public class GraphicsComponent_Animated extends GraphicsComponent {

    public static String COMPONENT_NAME = "GraphicsComponent_Animated";
    //Vector2 size;
    List<BufferedImage> images = new ArrayList<BufferedImage>();
    int curImage;
    float timeSinceChange;
    float[] time;

    public GraphicsComponent_Animated(Vector2 size, String[] imagePaths, int orderInLayer) {
        this.size = size;
        for (String s : imagePaths) {
            File imageFile = new File(s);
            if (imageFile != null) {
                try {
                    BufferedImage img = ImageIO.read(imageFile);
                    images.add(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        this.orderInLayer = orderInLayer;
    }

    public GraphicsComponent_Animated(Vector2 size, String[] imagePaths, int orderInLayer, float[] time) {
        this(size, imagePaths, orderInLayer);
        if (time.length != images.size()) {
            Debug.Error("Number of frame times do not match number of frame images");
            throw (null);
        }
        this.time = time;
    }

    @Override
    public void Update() {
        if (time.length > 0) {
            timeSinceChange += Time.deltaTime / Time.deltaMult;
            // Debug.Log(delta + " " + timeSinceChange);
            if (timeSinceChange > time[curImage]) {
                timeSinceChange = 0;
                curImage += 1;
                if (curImage >= images.size()) {
                    curImage = 0;
                }
            }
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
            	graphics.drawImage(scale(images.get(curImage), (gameObject.GetTransform().scale.x != 0 ? gameObject.GetTransform().scale.x  : 1),
            			(gameObject.GetTransform().scale.y != 0 ? gameObject.GetTransform().scale.y  : 1)), (int) gameObject.GetTransform().pos.x + 1,
                        (int) gameObject.GetTransform().pos.y + 1, null);
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
