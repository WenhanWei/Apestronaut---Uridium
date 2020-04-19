package components.Colliders;

import java.util.ArrayList;
import java.util.List;

import components.Component;
import customClasses.Vector2;
import GameObjects.GameObject;
import physics.Boundary;
import physics.ColliderManager;
import physics.RaycastHit;

public class Collider extends Component {

	public static String COMPONENT_NAME = "Collider";
	public List<Boundary> boundaries = new ArrayList<Boundary>();
	public String layer = "Default";

	public Collider(String layer) {
		this.layer = layer;
	}

	@Override
	public void Start() {
		super.Start();
		ColliderManager.AddBoundary(layer, this);
	}

	@Override
	public void Remove() {
		ColliderManager.RemoveBounday(layer, this);
	}

	public void UpdateBoundaries() {
	}

	/**
	 * Get the game object hit by a collision check
	 * 
	 * @param layerMask The method only detects colliders in the layers listed in
	 *                  the layer mask
	 * @param dirDist   The method will only check up to the distance defined, in
	 *                  the direction defined, by the Vector2
	 * @return The distance from the edge of the collider to whichever collider is
	 *         in the direction stated
	 */
	public GameObject GetInDirection(String[] layerMask, Vector2 dirDist) {
		GameObject horiz = GetInDirection1D(layerMask, new Vector2(dirDist.x, 0));
		GameObject vert = GetInDirection1D(layerMask, new Vector2(0,dirDist.y));
		if (horiz != null)
			return horiz;
		if (vert != null)
			return vert;
		return null;
	}
	
	private GameObject GetInDirection1D(String[] layerMask, Vector2 dirDist) {
		for (Boundary boundary : boundaries) {
			if (Math.signum(boundary.dir.x) == Math.signum(dirDist.x)
					&& Math.signum(boundary.dir.y) == Math.signum(dirDist.y)) {
				if (boundary.CheckBoundary(layerMask, dirDist) != null) {
					return (boundary.CheckBoundary(layerMask, dirDist).collision);
				}
			}
		}
		return null;		
	}

	/**
	 * Check in a direction as to whether there is a collider there
	 * 
	 * @param layerMask The method only detects colliders in the layers listed in
	 *                  the layer mask
	 * @param dirDist   The method will only check up to the distance defined, in
	 *                  the direction defined, by the Vector2
	 * @return The distance from the edge of the collider to whichever collider is
	 *         in the direction stated
	 */
	public float CheckDirection(String[] layerMask, Vector2 dirDist) {
		for (Boundary boundary : boundaries) {
			if (Math.signum(boundary.dir.x) == Math.signum(dirDist.x)
					&& Math.signum(boundary.dir.y) == Math.signum(dirDist.y)) {
				RaycastHit hit = boundary.CheckBoundary(layerMask, dirDist);
				if (hit != null)
					return hit.distance;
			}
		}
		return Float.MAX_VALUE;
	}
}
