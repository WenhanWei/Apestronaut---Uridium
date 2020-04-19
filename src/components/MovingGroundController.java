package components;

import GameObjects.GameObject;
import components.Colliders.BoxCollider;
import components.Colliders.Collider;
import customClasses.Vector2;
import logic.Time;
import managers.Window;

/**
 * The controller for the MovingGroundObject, which moves horizontally in the game
 */
public class MovingGroundController extends PlayerController {

	public static String COMPONENT_NAME = "MovingGroundController";
	boolean movingRight = true;
	boolean left = true;

	@Override
	public void Update() {
		setMaxSpeed(groundSpeed);

		if (movingRight) {
			if (onScreen()) {
				moveRight();
			} else {
				moveLeft();
				movingRight = false;
				left = true;
			}
		} else {
			if (left) {
				moveLeft();
			}
			if (gameObject.GetTransform().pos.x < screenLeftLimitX) {
				moveRight();
				left = false;
			}
			if (gameObject.GetTransform().pos.x > screenRightLimitX) {
				left = true;
			}
		}

		if (vel.x != 0 || vel.y != 0)
			gameObject.GetComponent(CharacterController.class).Move(vel.Mult(speedMultiplier * Time.deltaTime));

		if (center) {
			Window.xLoc = (int) (-gameObject.GetTransform().pos.x - gameObject.GetComponent(BoxCollider.class).size.x);
			Window.yLoc = (int) (-gameObject.GetTransform().pos.y - gameObject.GetComponent(BoxCollider.class).size.y);
		}

		if (obstacleCollision()) {
			movingRight = !movingRight; // changes the direction of the moving ground if it collides with another game object

		}

		if (restartGame()) {
			movingRight = true;
		   // Puts the MovingGroundObject back to its original position
			gameObject.GetTransform().pos = new Vector2(gameObject.GetTransform().origPos.x, gameObject.GetTransform().origPos.y);
		}

	}


	/**
	 * Determines whether the ground has collided with a ground object or a player object
	 * @return boolean    returns true if the ground has collided with an obstacle
	 */

    public boolean obstacleCollision() {
		String[] groundPlayerLayerMask = { "Ground" , "Player" };
		Vector2 obstacleVec = new Vector2(obstacleDist * xDir, 0);
		GameObject obstacle = null;
            if (gameObject.GetComponent(Collider.class) != null) {
                obstacle = (GameObject) gameObject.GetComponent(Collider.class).GetInDirection(groundPlayerLayerMask, obstacleVec);
                if (obstacle != null) {
                    return true;
                }
            }
    
        return false;
	}

}