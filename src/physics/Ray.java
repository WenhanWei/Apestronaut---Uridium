package physics;

import java.util.List;

import components.Colliders.Collider;
import customClasses.Vector2;

/**
 * The class for defining a physics ray used for detecting collisions
 */
public class Ray {

	public Vector2 origin, direction;

	public Ray(Vector2 origin, Vector2 direction) {
		this.origin = origin;
		this.direction = direction;
	}

	public Ray(float x1, float y1, float dirX, float dirY) {
		this.origin = new Vector2(x1, y1);
		this.direction = new Vector2(dirX, dirY);
	}

	/**
	 * Detect collisions.
	 * https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection
	 * 
	 * @param target The target wall that the raycast is checking for an
	 *               intersection with
	 * @return the collision point
	 */

	public RaycastHit Raycast(String[] layers, float dist) {
		RaycastHit bestIntersection = null;
		float bestDist = Float.MAX_VALUE;
		for (String layer : layers) {
			RaycastHit intersect = RaycastColliders(ColliderManager.colliders.get(layer));
			if (intersect != null) {
				if (intersect.distance < bestDist && intersect.distance < dist) {
					bestIntersection = intersect;
					bestDist = intersect.distance;
				}
			}
		}
		return bestIntersection;
	}

	/**
	 * Sub method for detecting collision against a list of colliders
	 */
	public RaycastHit RaycastColliders(List<Collider> targets) {
		RaycastHit bestIntersection = null;
		if (targets != null) {
			float bestDist = Float.MAX_VALUE;
			for (int i = 0;i<targets.size();i++) {
				Collider target = targets.get(i);
				RaycastHit intersect = RaycastBoundaries(target.boundaries);
				if (intersect != null) {
					intersect.collision = target.gameObject;
					if (intersect.distance < bestDist) {
						bestIntersection = intersect;
						bestDist = intersect.distance;
					}
				}
			}
		}
		return bestIntersection;
	}

	/**
	 * Sub method for detecting collision against a list of boundaries
	 */
	public RaycastHit RaycastBoundaries(List<Boundary> targets) {
		RaycastHit bestIntersection = null;
		if (targets != null) {
			float bestDist = Float.MAX_VALUE;
			for (Boundary target : targets) {
				RaycastHit intersect = RaycastIndividual(target);
				if (intersect != null) {
					if (intersect.distance < bestDist) {
						bestIntersection = intersect;
						bestDist = intersect.distance;
					}
				}
			}
		}
		return bestIntersection;
	}

	/**
	 * Sub method for detecting collision against an individual boundary
	 */
	public RaycastHit RaycastIndividual(Boundary target) {
		float x_1 = target.pos1.x;
		float y_1 = target.pos1.y;
		float x_2 = target.pos2.x;
		float y_2 = target.pos2.y;

		float x_3 = origin.x;
		float y_3 = origin.y;
		float x_4 = origin.x + direction.x;
		float y_4 = origin.y + direction.y;

		float denominator = (x_1 - x_2) * (y_3 - y_4) - (y_1 - y_2) * (x_3 - x_4);

		if (denominator == 0) {
			return null;
		}

		float t = ((x_1 - x_3) * (y_3 - y_4) - (y_1 - y_3) * (x_3 - x_4)) / denominator;
		float u = -((x_1 - x_2) * (y_1 - y_3) - (y_1 - y_2) * (x_1 - x_3)) / denominator;

		if (t >= 0 && t <= 1 && u > 0) {
			Vector2 intersectPos = new Vector2(x_1 + t * (x_2 - x_1), y_1 + t * (y_2 - y_1));
			RaycastHit hit = new RaycastHit();
			hit.point = intersectPos;
			hit.distance = intersectPos.Minus(origin).length();
			return hit;
		} else {
			return null;
		}

	}

}
