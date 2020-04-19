package GameObjects;

import components.PlayerController;
import components.Colliders.Collider;
import customClasses.Vector2;
import debugging.Debug;

/**
 * The jump booster which decreases the height of the player's opponent's jump
 * This booster is called SUPER LEAPING.
 */
public class JumpBooster extends BaseBooster {

	public JumpBooster() {
		super("src/resources/sprites/boosters/jumpbooster.png","JumpBooster", 30);
}

	/**
	 * Decreases the height of the opponent's jump by a factor of 10
	 * 
	 * @param gameObject player using the booster
	 * @return boolean checks the use of the booster has been successful
	 */
	@Override
	public boolean useBooster(GameObject gameObject) {
		String[] playerLayerMask = { "Player" };
		GameObject opponent = null;
		attackVec = new Vector2(attackDist * gameObject.GetComponent(PlayerController.class).xDir, 0);
		if (gameObject.GetComponent(Collider.class) != null) {
			opponent = (GameObject) gameObject.GetComponent(Collider.class).GetInDirection(playerLayerMask, attackVec);
			if (opponent != null) {
				Debug.Log("successfully going to use jump booster");
				opponent.GetComponent(PlayerController.class).jumpSpeed -= (10
						* gameObject.GetComponent(PlayerController.class).damage);
				gameObject.GetComponent(PlayerController.class).health -= (1
				* gameObject.GetComponent(PlayerController.class).damage);
				gameObject.GetComponent(PlayerController.class).totalScore += 300;
				return true;
			}
		}
		return false;
	}


	@Override
	public String toString() {
		return "Super Leaping";
	}
}
