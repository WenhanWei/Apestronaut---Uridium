package GameObjects;

import components.Graphics.GraphicsComponent;
import components.PlayerController;
import customClasses.Vector2;

/**
 * Booster which gives the player an instant high-knockback attack with a baseball bat
 */
public class KnockbackBooster extends BaseBooster {

	public KnockbackBooster() {
		super("src/resources/sprites/booster/bat.png","KnockbackBooster", 50);
	}

	/**
	 * Knocks back the opponent
	 * @param gameObject player using the booster
	 * @return boolean checks the use of the booster has been successful
	 */
	@Override
	public boolean useBooster(GameObject gameObject) {
		gameObject.GetComponent(GraphicsComponent.class).playAnimation(gameObject.GetComponent(PlayerController.class).batSwingAnimation);
		gameObject.GetComponent(PlayerController.class).kickWithParams(new Vector2(2.2f, -1.5f), 2);
		return false;
	}

	@Override
	public String toString() {
		return "Bat";
	}
}