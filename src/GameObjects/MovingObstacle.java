package GameObjects;

import components.CharacterController;
import components.Graphics.GraphicsComponent_Image;
import components.MovingObstacleController;
import components.Colliders.BoxCollider;
import components.Colliders.Collider;
import customClasses.Vector2;

/**
 * The MovingObstacle object which moves linearly back and forth across the screen
 * If a player collides with the moving obstacle, the game restarts
 */

public class MovingObstacle extends GameObject {

	final int WIDTH = 50, HEIGHT = 50;

	public MovingObstacle() {
		super(new Vector2(0, 0));
		AddComponent(new GraphicsComponent_Image(new Vector2(WIDTH, HEIGHT),
						"src/resources/sprites/Space Suit Fighter Assets Pack/eyeball.png",
						-1),
				GraphicsComponent_Image.COMPONENT_NAME);
		BoxCollider collider = new BoxCollider("MovingObstacle", new Vector2(WIDTH, HEIGHT));
		AddComponent(collider, Collider.COMPONENT_NAME);
		AddComponent(CharacterController.class, CharacterController.COMPONENT_NAME);
		AddComponent(MovingObstacleController.class, MovingObstacleController.COMPONENT_NAME);
	}

	public MovingObstacle(Vector2 pos) {
		super(pos);
		AddComponent(new GraphicsComponent_Image(new Vector2(WIDTH, HEIGHT),
						"src/resources/sprites/Space Suit Fighter Assets Pack/eyeball.png",
						-1),
				GraphicsComponent_Image.COMPONENT_NAME);
		BoxCollider collider = new BoxCollider("MovingObstacle", new Vector2(WIDTH, HEIGHT));
		AddComponent(collider, Collider.COMPONENT_NAME);
		AddComponent(CharacterController.class, CharacterController.COMPONENT_NAME);
		AddComponent(MovingObstacleController.class, MovingObstacleController.COMPONENT_NAME);
	}

	@Override
	public String toString() {
		return "Watch out for these deadly obstacles. If you collide with them, you will really take the hit and have to start fighting the apes right from the beginning again.";
	}

}


