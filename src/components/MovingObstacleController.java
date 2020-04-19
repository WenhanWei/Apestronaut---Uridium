package components;

import components.Colliders.BoxCollider;
import components.Colliders.Collider;
import components.Graphics.GraphicsComponent_Image;
import customClasses.Vector2;
import GameObjects.GameObject;
import GameObjects.GroundObject;
import logic.Time;
import managers.Engine;
import managers.Window;

/**
 * The controller for the MovingObstacle object, which moves horizontally back and forth on a ground object
 */

public class MovingObstacleController extends PlayerController {

	/**
	 * Initial speed of the moving obstacle
	 */
	final float initialSpeed = 0.5f;

	public static String COMPONENT_NAME = "MovingObstacleController";

	boolean moveRight = true;

	@Override
	public void Update() {

		setMaxSpeed(initialSpeed);

		if (vel.y < 0) {
			if (gameObject.GetComponent(BoxCollider.class).CheckDirection(groundLayerMask, Vector2.up()) < groundDist)
				vel.y = 0;
			isGrounded = false;
		} else {
			isGrounded = gameObject.GetComponent(BoxCollider.class).CheckDirection(groundLayerMask,
					Vector2.down()) < groundDist;
		}

		if (!isGrounded) {
			acceleration = AIR_ACCELERATION;
			vel.y += gravity * Time.deltaTime;
		} else {
			acceleration = GROUND_ACCELERATION;
			vel.y = 0;
		}

		// Ensures the obstacles doesn't fall off the platform it moves across
		if (isGrounded) {
			GroundObject ground = (GroundObject) gameObject.GetComponent(BoxCollider.class)
				.GetInDirection(groundLayerMask, new Vector2(0, groundDist));
			if (ground != null) {
				float groundLength = ground.GetComponent(BoxCollider.class).size.x;
				float groundStartPosition = ground.GetTransform().pos.x;
				if (gameObject.GetTransform().pos.x >= (groundLength + groundStartPosition - 50)) {
					moveRight = false;
				} 
				if (gameObject.GetTransform().pos.x <= groundStartPosition) {
					moveRight = true;
				}
			}
		}

		if (moveRight) {
			moveRight();
		} else {
			moveLeft();
		}

		gameObject.GetComponent(GraphicsComponent_Image.class).lookDir = xDir;

		if (!onScreen()) {
			moveRight = false;
		}

		if (vel.x != 0 || vel.y != 0)
			gameObject.GetComponent(CharacterController.class).Move(vel.Mult(speedMultiplier * Time.deltaTime));

		if (center) {
			Window.xLoc = (int) (-gameObject.GetTransform().pos.x - gameObject.GetComponent(BoxCollider.class).size.x);
			Window.yLoc = (int) (-gameObject.GetTransform().pos.y - gameObject.GetComponent(BoxCollider.class).size.y);
		}

		obstacleCollisionWithPlayer(gameObject);

	}

	/**
	 * Determines whether a player has collided with a moving obstacle. In the event of a collision, the player's health decrease and the level resets
	 * @param current the moving obstacle
	 * @return true if the player has collided with the moving obstacle
	 */
	public boolean obstacleCollisionWithPlayer(GameObject current) {
		String[] playerLayerMask = { "Player" };
		Vector2 obstacleVec = new Vector2(5 * current.GetComponent(PlayerController.class).vel.x, 0);
		GameObject player = null;
		if (current.GetComponent(Collider.class) != null) {
			player = (GameObject) current.GetComponent(Collider.class).GetInDirection(playerLayerMask, obstacleVec);
			if (player != null) {
				player.GetComponent(PlayerController.class).health -= (3 * player.GetComponent(PlayerController.class).damage);
				Engine.Instance.restart();
				return true;
			}
		}

		return false;
	}

}
