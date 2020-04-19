package GameObjects;

import components.EnemyController;
import components.Graphics.GraphicsComponent_Image;
import components.PlayerController;
import customClasses.Vector2;

/**
 * Placeholder EnemyObject such that two players can play on the screen at the same time
 */
public class EnemyObject extends PlayerObject {

	public EnemyObject() {
		super();
		PlayerObject.players.add(this);
		RemoveComponent(GetComponent(PlayerController.class));
		AddComponent(
				new GraphicsComponent_Image(new Vector2(WIDTH, HEIGHT),
						"src/resources/sprites/player/player_monkey.png",
						-1),
				GraphicsComponent_Image.COMPONENT_NAME);
		AddComponent(EnemyController.class, EnemyController.COMPONENT_NAME);
	}

	public String toString() {
		return "enemy player object";
	}
	
}
