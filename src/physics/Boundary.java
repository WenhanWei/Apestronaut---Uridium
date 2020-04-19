package physics;

import java.util.ArrayList;
import java.util.List;

import customClasses.Vector2;
import debugging.Debug;

/**
 * A boundary for physics
 */
public class Boundary {

	/**
	 * The start and end locations of the boundary
	 */
	public Vector2 pos1, pos2;
	/**
	 * The outward facing rays used to detect collisions
	 */
	public List<Ray> edgeRays = new ArrayList<Ray>();
	/**
	 * The direction the boundary faces in
	 */
	public Vector2 dir = Vector2.zero();

	public Boundary(Vector2 pos1, Vector2 pos2) {
		this.pos1 = pos1;
		this.pos2 = pos2;
	}

	public Boundary(Vector2 pos1, Vector2 pos2, Vector2 dir) {
		this(pos1, pos2);
		this.dir = dir;

		float distBetween = pos2.Minus(pos1).length();
		Vector2 connectingDir = pos2.Minus(pos1).Normalize();
		int subDivideNum = (int) pos2.Minus(pos1).length();
		float distSubDivide = distBetween / subDivideNum;

		Vector2[] rayPos = new Vector2[subDivideNum];

		for (int i = 0; i < subDivideNum; i++) {
			rayPos[i] = pos1.Add(connectingDir.Mult(distSubDivide * i));
		}

		for (int i = 0; i < subDivideNum; i++) {
			edgeRays.add(new Ray(rayPos[i], connectingDir.Perp()));
		}
	}

	/**
	 * Check all rays in a direction for a collision
	 * 
	 * @param layerMask The layers in which collisions can occur
	 * @param move      The direction and distance in which to check for a collision
	 * @return The distance to the collided object
	 */
	public RaycastHit CheckBoundary(String[] layerMask, Vector2 move) {
		List<RaycastHit> raycastHits = new ArrayList<RaycastHit>();
		for (Ray ray : edgeRays) {
			//Debug.DrawRay(ray);
			RaycastHit collisionPoint = ray.Raycast(layerMask, move.length());
			if (collisionPoint != null) {
				raycastHits.add(collisionPoint);
			}
		}
		if (raycastHits.size() > 0) {
			float shortestDist = Float.MAX_VALUE;
			RaycastHit closestHit = null;
			for (RaycastHit hit : raycastHits) {
				if (hit.distance < shortestDist) {
					shortestDist = hit.distance;
					closestHit = hit;
				}
			}
			return closestHit;
		}
		return null;
	}

}
