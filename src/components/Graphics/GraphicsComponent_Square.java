package components.Graphics;

import org.w3c.dom.Element;
import java.awt.Color;
import java.awt.Graphics2D;

import customClasses.Vector2;
import debugging.Debug;

/**
 * A box shaped graphical element
 */
public class GraphicsComponent_Square extends GraphicsComponent {

	public static String COMPONENT_NAME = "GraphicsComponent_Square";
	public Color color = Color.black;

	public GraphicsComponent_Square(Vector2 size, Color color, int orderInLayer) {
		this.size = size;
		this.color = color;
		this.orderInLayer = orderInLayer;
	}

	@Override
	public void paint(Graphics2D graphics) {
		if (gameObject != null) {
			if (gameObject.GetTransform() != null) {
				graphics.setColor(color);
				graphics.fillRect(Math.round(gameObject.GetTransform().pos.x),
						Math.round(gameObject.GetTransform().pos.y), (int) (size.x * (gameObject.GetTransform().scale.x != 0 ? gameObject.GetTransform().scale.x  : 1)), (int) (size.y * (gameObject.GetTransform().scale.y != 0 ? gameObject.GetTransform().scale.y  : 1)));
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
