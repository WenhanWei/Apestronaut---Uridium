package components;

import debugging.Debug;
import org.w3c.dom.Element;

import customClasses.Vector2;

/**
 * Contains overarching position data of a GameObject
 */
public class Transform extends Component {

	public static String COMPONENT_NAME = "Transform";
	public Vector2 origPos;
	public Vector2 pos;
	public Vector2 scale = new Vector2(1f, 1f);

	public Transform(Vector2 pos) {
		this.pos = pos;
	}

	public Transform(float x, float y) {
		this.pos = new Vector2(x, y);
	}

	public Transform() {
		this.pos = new Vector2(0, 0);
	}

	@Override
	public void Start() {
		super.Start();
		this.origPos = this.pos;
	}

	@Override
	public void ResolveXML(Element element) {
		if (ResolveData(element, "x") != null)
			pos.x = Integer.parseInt(ResolveData(element, "x"));
		if (ResolveData(element, "y") != null)
			pos.y = Integer.parseInt(ResolveData(element, "y"));
			
		this.origPos = this.pos;
	}

}
