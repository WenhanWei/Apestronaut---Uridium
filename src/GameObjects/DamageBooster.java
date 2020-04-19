package GameObjects;

import components.PlayerController;
import debugging.Debug;

/**
 * The DamageBooster where boosters are used to increase the damage of a player
 * This booster is called BERSERKER
 */

public class DamageBooster extends BaseBooster {

	public DamageBooster() {
		super("src/resources/sprites/boosters/damagebooster.png", "DamageBooster", 30);
	}

	/**
	 * Using this booster increases the damage of the current player such that
	 * performing any future attacks against an opponent will have 1.5x the impact
	 * 
	 * @return boolean checks the booster has been used successfully
	 */
	@Override
	public boolean useBooster(GameObject gameObject) {
		Debug.Log("successfully going to use damage booster");
		gameObject.GetComponent(PlayerController.class).damage *= 1.5;
		gameObject.GetComponent(PlayerController.class).health -= 1;
		gameObject.GetComponent(PlayerController.class).totalScore += 250;
		return true;
	}

	@Override
	public String toString() {
		return "Berserker";
	}

}
