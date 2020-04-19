package components.Graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import components.Component;
import customClasses.Vector2;
import debugging.Debug;
import logic.Time;
import managers.GraphicsManager;
import managers.Window;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * The parent component of all graphics components
 */
public class GraphicsComponent extends Component implements GraphicsManager {

	BufferedImage image;
	BufferedImage defaultImage;

	BufferedImage[] animFrames;
	float[] animFrameTimes;
	float frameTimer;
	public boolean playingAnimation = false;
	int frameIndex;
	Vector2 offset = Vector2.zero();

	float hoverRadius = 10;
	public boolean shouldHover = false;
	boolean hoveringUp = true;

	public static String COMPONENT_NAME = "GraphicsComponent";
	/**
	 * The order within the layer in which the object is printed. Higher numbers are
	 * painted over lower numbers
	 */
	public int orderInLayer = 50;
	/**
	 * The layer in which the graphics component lies. The order that layers are
	 * rendered is defined by their value in the Layer enum
	 */
	public Layer layer = Layer.Default;

	public Vector2 size;

	@Override
	public void OnEnable() {
		Window.Instance.AddGraphicsComponent(this);
	}

	@Override
	public void OnDisable() {
		Window.Instance.graphicsManagersToRemove.add(this);
	}

	@Override
	public void paint(Graphics2D graphics) {

	}

	@Override
	public void Update() {
		if (playingAnimation) {
			image = animFrames[frameIndex];
			frameTimer -= Time.deltaTime;
			if (frameTimer <= 0) {
				if (animFrames.length - 1 > frameIndex) {
					frameIndex++;
					frameTimer = animFrameTimes[frameIndex];
				} else {
					playingAnimation = false;
					image = defaultImage;
				}
			}
		}

		if (shouldHover) {
			hover(hoverRadius);
		}
	}

	public void playAnimation(Animation anim) {
		if (anim.frames.length !=  anim.frameTimes.length) {
			Debug.Warning(""+anim.frames.length + " frames but " + anim.frameTimes.length + " frame times");
			return;
		}
		animFrames = anim.frames;
		animFrameTimes = anim.frameTimes;
		frameIndex = 0;
		frameTimer = anim.frameTimes[0];
		playingAnimation = true;
	}

	public void changeImage(String path) {
		File imageFile = new File(path);
		try {
			image = ImageIO.read(imageFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		playingAnimation = false;
	}

	public void changeImage(BufferedImage img) {
		image = img;
		playingAnimation = false;
	}

	protected void hover(float radius) {
		if (offset.y > -radius && hoveringUp) {
			offset.y -= 0.25f*Time.deltaTime;
		} else if (offset.y < radius && !hoveringUp) {
			offset.y += 0.25f*Time.deltaTime;
		}
		if (offset.y <= -radius) {
			offset.y = -radius;
			hoveringUp = false;
		} else if (offset.y >= radius) {
			offset.y = radius;
			hoveringUp = true;
		}
	}

	public static BufferedImage imageFromPath(String filePath) {
		File imageFile = new File(filePath);
		BufferedImage img = null;
		try {
			img = ImageIO.read(imageFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return img;
	}

	public void setToDefaultImage() {
		image = defaultImage;
	}
}
