package components;

import components.Colliders.Collider;
import customClasses.Vector2;
import debugging.Debug;

/**
 * A controller for all moveable characters
 */
public class CharacterController extends Component {

	public static String COMPONENT_NAME = "CharacterController";

	/**
	 * The layer on which solid objects exist
	 */
	public String[] groundLayerMask = new String[] { "Ground", "MovingGroundObject" };

	/**
	 * Move GameObject based on a Vector2 if there is no collision preventing this
	 * 
	 * @param move The distance and direction of the desired movement
	 */
	public void Move(Vector2 move) {
		Collider collider = gameObject.GetComponent(Collider.class);
		if (collider != null) {
			MoveHoriz(collider, move.x);
			collider.UpdateBoundaries();
			MoveVert(collider, move.y);
			collider.UpdateBoundaries();
		} else {
			Debug.Warning("No collider on " + gameObject.toString());
		}
	}

	/**
	 * Performs the horizontal component of Move(Vector2)
	 * 
	 * @param collider the current collider of the GameObject
	 * @param dist     the distance of the desired horizonal movement (can be
	 *                 negative)
	 */
	private void MoveHoriz(Collider collider, float dist) {
		Vector2 move = new Vector2(dist, 0);
		float collisionDistance = collider.CheckDirection(groundLayerMask, new Vector2(move.x, 0));
		float distToMove = 0;
		if (collisionDistance == Float.MAX_VALUE) {
			gameObject.GetTransform().pos = gameObject.GetTransform().pos.Add(move);
		} else {
			distToMove = collisionDistance * 0.9f;
			if (distToMove > 0.1f)
				gameObject.GetTransform().pos = gameObject.GetTransform().pos
						.Add(new Vector2(move.Normalize().Mult((distToMove)).x, 0));
			else
				gameObject.GetComponent(PlayerController.class).vel.x = 0;
		}

		move = new Vector2(-dist, 0);
		collisionDistance = collider.CheckDirection(groundLayerMask, new Vector2(0, move.y));
		if (collisionDistance != Float.MAX_VALUE) {
			if (distToMove > 0.1f)
				gameObject.GetTransform().pos = gameObject.GetTransform().pos
						.Add(new Vector2(0, move.Normalize().Mult((distToMove)).y));
		}
	}

	/**
	 * Performs the vertical component of Move(Vector2)
	 * 
	 * @param collider the current collider of the GameObject
	 * @param dist     the distance of the desired vertical movement (can be
	 *                 negative)
	 */
	private void MoveVert(Collider collider, float dist) {
		Vector2 move = new Vector2(0, dist);
		float collisionDistance = collider.CheckDirection(groundLayerMask, new Vector2(0, move.y));
		float distToMove = 0;
		if (collisionDistance == Float.MAX_VALUE) {
			gameObject.GetTransform().pos = gameObject.GetTransform().pos.Add(move);
		} else {
			distToMove = collisionDistance * 0.9f;
			if (distToMove > 0.1f)
				gameObject.GetTransform().pos = gameObject.GetTransform().pos
						.Add(new Vector2(0, move.Normalize().Mult((distToMove)).y));
			else
				gameObject.GetComponent(PlayerController.class).vel.y = 0;
		}

		move = new Vector2(0, -dist);
		collisionDistance = collider.CheckDirection(groundLayerMask, new Vector2(0, move.y));
		if (collisionDistance != Float.MAX_VALUE) {
			if (distToMove > 0.1f)
				gameObject.GetTransform().pos = gameObject.GetTransform().pos
						.Add(new Vector2(0, move.Normalize().Mult((distToMove)).y));
		}
	}

}
