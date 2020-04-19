package GameObjects;

import components.PlayerController;
import components.Colliders.Collider;
import customClasses.Vector2;
import debugging.Debug;

/**
 * Stun booster which freezes the opponent (such that they cannot move) for as long as the freezeTimer is active (see PlayerController class)
 * This booster is called CHRONO FUSION
 */
public class StunBooster extends BaseBooster {

	public StunBooster() {
		super("src/resources/sprites/boosters/stunbooster.png","StunBooster", 30);
	}

	/**
	 *
	 * Freezes the opponent 
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
				Debug.Log("successfully going to use stun booster");
				opponent.GetComponent(PlayerController.class).frozen = true;
				gameObject.GetComponent(PlayerController.class).health -= (1
				* gameObject.GetComponent(PlayerController.class).damage);
				gameObject.GetComponent(PlayerController.class).totalScore += 500;
				return true;
			}
		}
		return false;
	}


	@Override
	public String toString() {
		return "Chrono Fusion";
	}
}