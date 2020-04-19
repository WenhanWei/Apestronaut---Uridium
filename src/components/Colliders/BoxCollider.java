package components.Colliders;

import org.w3c.dom.Element;
import customClasses.Vector2;
import physics.Boundary;

/**
 * A box shaped collider with 4 bounds
 */
public class BoxCollider extends Collider {

	public static String COMPONENT_NAME = "Collider";
	public Vector2 offset = new Vector2(0, 0);
	public Vector2 size;
	public boolean debug = true;

	public BoxCollider(String layer, Vector2 offset, Vector2 size) {
		super(layer);
		this.offset = offset;
		this.size = size;
	}

	public BoxCollider(String layer, Vector2 size) {
		super(layer);
		this.size = size;
	}

	@Override
	public void Start() {
		super.Start();
		SetBoundaries();
	}

	@Override
	public void UpdateBoundaries() {
		boundaries.clear();
		SetBoundaries();
	}

	@Override
	public void Update() {
		UpdateBoundaries();
		/*
		 * for (Boundary boundary : boundaries) { for (Ray ray : boundary.edgeRays) {
		 * Debug.DrawRay(ray); } }
		 */
	}

	private void SetBoundaries() {
		Vector2 pos = gameObject.GetTransform().pos.Add(offset);
		float xScale = (gameObject.GetTransform().scale.x != 0 ? gameObject.GetTransform().scale.x  : 1);
		float yScale = (gameObject.GetTransform().scale.y != 0 ? gameObject.GetTransform().scale.y  : 1);
		boundaries.add(new Boundary(pos, pos.Add(new Vector2(size.x * xScale, 0)), new Vector2(0, -1)));
		boundaries.add(
				new Boundary(pos.Add(new Vector2(size.x * xScale, 0)), pos.Add(new Vector2(size.x * xScale, size.y * yScale)), new Vector2(1, 0)));
		boundaries.add(
				new Boundary(pos.Add(new Vector2(size.x * xScale, size.y * yScale)), pos.Add(new Vector2(0, size.y * yScale)), new Vector2(0, 1)));
		boundaries.add(new Boundary(pos.Add(new Vector2(0, size.y * yScale)), pos.Add(new Vector2(0, 0)), new Vector2(-1, 0)));
	}

	@Override
	public void ResolveXML(Element element) {
		if (ResolveData(element, "sizeX") != null)
			size.x = Integer.parseInt(ResolveData(element, "sizeX"));
		if (ResolveData(element, "sizeY") != null)
			size.y = Integer.parseInt(ResolveData(element, "sizeY"));
	}

}
