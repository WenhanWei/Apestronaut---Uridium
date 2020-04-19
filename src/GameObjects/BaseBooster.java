package GameObjects;

import components.Graphics.GraphicsComponent_Image;
import components.Graphics.GraphicsComponent_Animated;
import components.PlayerController;
import components.Colliders.BoxCollider;
import components.Colliders.Collider;
import components.Graphics.GraphicsComponent_Square;
import customClasses.Vector2;
import debugging.Debug;
import managers.Engine;


/**
 * A base BaseBooster object to form all the boosters, coins and weapons
 * This booster is called PSYCHIC
 */

public class BaseBooster extends GameObject {

	/**
	 * Maximum distance between current player and opponent player in order for the
	 * player to use a booster against their opponent
	 */
	float attackDist = 700;

	Vector2 attackVec;

	static final int boosterSize = 20;

	/**
	 * Handles animation for coin sprites
	 */
	static float[] time = new float[11];
	public float[] initialiseTime() {
		for (int i = 0; i < time.length - 1; i++) {
			time[i] = 0.15f;
		}

		return time;
	}

	public BaseBooster() {
		super();
		AddComponent(
				new GraphicsComponent_Image(new Vector2(30, 30), "src/resources/sprites/boosters/basebooster.png", 0),
				GraphicsComponent_Image.COMPONENT_NAME);
		GetComponent(GraphicsComponent_Image.class).shouldHover = true;
		BoxCollider collider = new BoxCollider("Booster", new Vector2(boosterSize, boosterSize));
		AddComponent(collider, Collider.COMPONENT_NAME);
	}

	/**
	 * Constructor to create a booster with a given colour and name (for layer
	 * mask)
	 * 
	 * @param image filepath to image of boosetr
	 * @param name  name of the box collider
	 */
	public BaseBooster(String image, String name) {
		super();
		AddComponent(new GraphicsComponent_Image(new Vector2(boosterSize, boosterSize), image, 0),
				GraphicsComponent_Square.COMPONENT_NAME);
		BoxCollider collider = new BoxCollider(name, new Vector2(boosterSize, boosterSize));
		AddComponent(collider, Collider.COMPONENT_NAME);
	}
	

	/**
	 * Constructor creating animated booster object wih a specified array of image paths, name and size
	 * @param images string array of file paths to images for animation
	 * @param name name of the collider
	 * @param size size of the booster
	 */

	public BaseBooster(String[] images, String name, int size) {
		super(new Vector2(size, size));
		AddComponent(new GraphicsComponent_Animated(new Vector2(size, size), images, -1, initialiseTime()),
				GraphicsComponent_Square.COMPONENT_NAME);
		BoxCollider collider = new BoxCollider(name, new Vector2(boosterSize, boosterSize));
		AddComponent(collider, Collider.COMPONENT_NAME);
	}

	/**
	 * Constructor to create a booster object with a given image, name and size
	 * @param imagePath file path for image
	 * @param name name of the collider
	 * @param size size of the booster
	 */
	public BaseBooster(String imagePath, String name, int size) {
		super();
		AddComponent(
				new GraphicsComponent_Image(new Vector2(size, size), imagePath, 0),
				GraphicsComponent_Image.COMPONENT_NAME);
		GetComponent(GraphicsComponent_Image.class).shouldHover = true;
		BoxCollider collider = new BoxCollider(name, new Vector2(boosterSize, boosterSize));
		AddComponent(collider, Collider.COMPONENT_NAME);
	}

	/**
	 * Uses booster, which decreases opponent's health by 1 * damage and increases the number of "lives" (game runs) of the player by 1
	 * 
	 * @return true if booster has been used successfully
	 */
	public boolean useBooster(GameObject gameObject) {
		String[] playerLayerMask = { "Player" };
		GameObject opponent = null;
		attackVec = new Vector2(attackDist * gameObject.GetComponent(PlayerController.class).xDir, 0);
		if (gameObject.GetComponent(Collider.class) != null) {
			opponent = (GameObject) gameObject.GetComponent(Collider.class).GetInDirection(playerLayerMask, attackVec);
			if (opponent != null) {
				Debug.Log("successfully going to use base booster");
				opponent.GetComponent(PlayerController.class).health -= (1
						* gameObject.GetComponent(PlayerController.class).damage);
				gameObject.GetComponent(PlayerController.class).health -= 1;
				gameObject.GetComponent(PlayerController.class).totalScore += 200;
				return true;

			}
		}
		return false;
	}

	/**
	 * Removes booster from game space
	 * 
	 */
	public void RemoveBooster() {
		if (this != null) {
			Engine.Instance.RemoveObject(this);
		}
	}


	@Override
	public String toString() {
		return "Psychic";
	}

}
