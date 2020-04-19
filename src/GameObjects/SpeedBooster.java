package GameObjects;

import components.PlayerController;
import components.Colliders.Collider;
import customClasses.Vector2;

/**
 * Speed booster which increases the speed of the current player and decreases the speed of their opponent
 * This booster is called RADIANT BOLT
 */

public class SpeedBooster extends BaseBooster {

	public SpeedBooster() {
		super("src/resources/sprites/boosters/speedbooster.png","SpeedBooster", 30);
	}

	/**
	 * Opponent's speed declines and current player's speed increases when this booster is used
	 * 
	 * @return boolean checks booster has been used successfully
	 */
	@Override
	public boolean useBooster(GameObject gameObject) {
		String[] playerLayerMask = { "Player" };
		GameObject opponent = null;
		attackVec = new Vector2(attackDist * gameObject.GetComponent(PlayerController.class).xDir, 0);
		if (gameObject.GetComponent(Collider.class) != null) {
			opponent = (GameObject) gameObject.GetComponent(Collider.class).GetInDirection(playerLayerMask, attackVec);
			if (opponent != null) {
				gameObject.GetComponent(PlayerController.class).hasSpeedBooster = true;
				opponent.GetComponent(PlayerController.class).maxSpeed /= (4
						* gameObject.GetComponent(PlayerController.class).damage);
				opponent.GetComponent(PlayerController.class).acceleration /= (10 * gameObject.GetComponent(PlayerController.class).damage);
				gameObject.GetComponent(PlayerController.class).acceleration *= (10 * gameObject.GetComponent(PlayerController.class).damage);
				gameObject.GetComponent(PlayerController.class).maxSpeed *= (3
						* gameObject.GetComponent(PlayerController.class).damage);
				gameObject.GetComponent(PlayerController.class).health -= (1
				* gameObject.GetComponent(PlayerController.class).damage);
				gameObject.GetComponent(PlayerController.class).totalScore += 400;
				return true;
			}
		}
		return false;
	}


	@Override
	public String toString() {
		return "Radiant Bolt";
	}

}
