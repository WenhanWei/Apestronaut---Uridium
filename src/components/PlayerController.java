package components;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import GameDatabase.GameAccountOperations;
import components.Colliders.BoxCollider;
import components.Colliders.Collider;
import components.Graphics.Animation;
import components.Graphics.GraphicsComponent;
import components.Graphics.GraphicsComponent_Image;
import customClasses.InputKeys;
import customClasses.Vector2;
import debugging.Debug;
import GameObjects.*;
import logic.LevelEditor;
import logic.Time;
import managers.*;
import managers.Window;


/**
 * The main controller component for a player (single player game mode).
 */
public class PlayerController extends Component {

	public static String COMPONENT_NAME = "PlayerController";

	/**
	 * Defining all sprites and animations for the player
	 */
	public BufferedImage defaultSprite = GraphicsComponent.imageFromPath("src/resources/sprites/player/player_monkey.png");
	public BufferedImage crouchSprite = GraphicsComponent.imageFromPath("src/resources/sprites/player/player_monkey_c.png");
	public BufferedImage holdingBatSprite = GraphicsComponent.imageFromPath("src/resources/sprites/player/player_monkey_bat.png");

	public Animation kickAnimation = new Animation(new String[] {"src/resources/sprites/player/player_monkey_kick_0.png",
			"src/resources/sprites/player/player_monkey_kick.png"}, new float[] { 5f, 15f });
	public Animation batSwingAnimation = new Animation(new String[] {"src/resources/sprites/player/player_monkey_bat_hit_0.png",
			"src/resources/sprites/player/player_monkey_bat_hit_1.png",
			"src/resources/sprites/player/player_monkey_bat_hit_2.png",
			"src/resources/sprites/player/player_monkey_bat_hit_3.png",
			"src/resources/sprites/player/player_monkey_bat_hit_4.png",}, new float[] { 2f, 2f, 2f, 3f, 25f });

	BufferedImage currentSprite = defaultSprite;

	/**
	 * Current velocity the player is travelling at
	 */
	public Vector2 vel = Vector2.zero();
	/**
	 * Multiplier for player speed
	 */
	float speedMultiplier = 10f;
	/**
	 * Max speed of player
	 */
	final float INITIAL_MAX_SPEED = 1f;  // 1.3
	public float maxSpeed = INITIAL_MAX_SPEED;

	final float AIR_ACCELERATION = 0.05f;  // 0.1
	final float GROUND_ACCELERATION = 0.12f;  // 0.4
	final float MOVING_GROUND_ACCELERATION = 0.08f;
	/**
	 * Acceleration of player
	 */
	public float acceleration = AIR_ACCELERATION;
	/**
	 * Strength of gravity
	 */
	final float gravity = 0.05f;  // 0.08
	/**
	 * Initial speed of jump (original value: 20)
	 */
	final float INITIAL_JUMP_SPEED = 18.5f;

	public float jumpSpeed = INITIAL_JUMP_SPEED;

	/**
	 * Maximum distance within which a player is considered to have collided with a
	 * moving obstacle
	 */
	int obstacleDist = 5;

	/**
	 * Defines if player is touching the ground i.e. there is a collider directly
	 * below the player
	 */
	public boolean isGrounded = false;

	/**
	 * Defines if the player is touching moving ground (ie a MovingGroundObject)
	 */
	public boolean isGroundedOnMove = false;

	/**
	 * How close a ground collider has to be for it to be classified as the player
	 * being grounded
	 */
	final float groundDist = 0.2f;

	/**
	 * If true, the main window will follow the player
	 */
	protected boolean center = false;

	/**
	 * Timer to determiner how long to keep the window in the same position for
	 */
	int centerTimer = 200;

	/**
	 * Screen moves with player's X direction only
	 */
	boolean centerX = false;

	/**
	 * The layerMask for ground (including moving ground)
	 */
	public String[] groundLayerMask = new String[] { "Ground", "MovingGroundObject" };

	/**
	 * The layerMask for moving ground
	 */
	 public String[] moveGroundLayerMask = new String[] { "MovingGroundObject" };

	 /**
	  * The layerMask for (ordinary) ground
	  */
	 public String[] origGroundLayerMask = new String[] { "Ground" };

	/**
	 * Speed of moving ground
	 */
	final float groundSpeed = 0.3f;

	/**
	 * Initial health value set at the start of the game for all players
	 */
	public final int INITIAL = 10;
	public int health = INITIAL;

	/**
	 * Damage rating from 1 to 3, 1 being the lowest and 3 being the highest. The
	 * rating determines how detrimental impacts to health will be from kicks, going
	 * off screen etc
	 * Damage will be the damagePower of the weapon if the player owns a weapon (see Update method)
	 */
	public int damage = 1;

	/**
	 * Maximum distance between current player and opponent player in order for a
	 * kick to be successful
	 */
	public int kickDist = 200;

	/**
	 * Velocity opponent receives from kick
	 */
	Vector2 kickVelVec = new Vector2(1.4f, -1f);

	/**
	 * Determines timings between kicks
	 */
	final float COOLDOWN = 40f;
	float kickCooldown = COOLDOWN;
	public boolean hasKickCooldown = false;

	/**
	 * Maximum distance between current player and opponent player in order for squashing them to be successful
	 */
	public int squashDist = 100;

	/**
	 * Maximum distance between current player and a booster in order for the player to
	 * be able to successfully pick it up
	 */
	int boosterDist = 100 / 3;

	/**
	 * The boosters and weapons a player collects in the game are stored in a stack.
	 * When a player attempts to activate a booster, the one at the top of the stack is chosen
	 */
	public Stack<BaseBooster> boosters = new Stack<BaseBooster>();
	public Stack<BaseBooster> weapons = new Stack<BaseBooster>();

	/**
	 * Total score for the player, which will be added to the leaderboard (and therefore stored in the database)
	 */
	public int totalScore = 0;

	/**
	 * Total currency for the player, which could be used to purchase extra features of the game
	 */
	public int currency = 900;

	/**
	 * Ensures PickupBooster() is only called once when booster is found
	 */
	int pickUpCount = 0;

	/**
	 * Direction the player is facing: left = -1, right = 1
	 */
	public int xDir = 1;

	/**
	 * The x position limits beyond which the player will disappear from the game window
	 */
	int screenRightLimitX = Window.WINDOW_WIDTH + 100;
	int screenLeftLimitX = -100;

	/**
	 * The y position limits beyond which the player will disappear
	 */
	int screenUpLimitY = -50;
	int screenDownLimitY = Window.WINDOW_HEIGHT;

	/**
	 * Number of jumps done on normal ground
	 */
	int jumpCount = 0;

	/**
	 * Number of jumps done on moving ground
	 */
	int jumpCountMovingGround = 0;

	/**
	 * Determines whether the player has been frozen (by their opponent using a booster against them)
	 */
	public boolean frozen = false;

	/**
	 * The number of ticks the player will be frozen for
	 */
	int freezeTimer = 800;

	/**
	 * The number of ticks the player will have a higher speeds after using the speed booster
	 */
	public int speedTimer = 1000;
	public boolean hasSpeedBooster = false;

	/**
	 * Determines whether the player has been squashed ie their size reduced (by their opponent)
	 */
	public boolean squashed = false;

	/**
	 * The number of ticks the player will be squashed for
	 */
	int squashTimer = 1000;

	/**
	 * Determines whether the player is invincible
	 * If a player is invincible, their health cannot decrease and they won't be affected by any boosters, squash or kick operations
	 */
	public boolean invincible = false;

	/**
	 * The number of ticks the player will be invincible for
	 */
	int invincibleTimer = 800;


	boolean debug = true;

	/**
	 * Determines first run of the Update() method
	 */
	boolean start = true;

	@Override
	public void Update() {

		Window.health = health; // will draw the relevant component to the screen
		Window.totalScore = totalScore;
		Window.currency = currency;

		if (!boosters.empty()) {
			Window.booster = boosters.peek().toString();
		} else if (boosters.empty()) { // if the player has no boosters equipped then we adjust this accordingly on the GUI
			Window.booster = "";
		}

		// Allows the player to freeze their AI opponent in the practice mode of the game
		if (Input.KeyDown(KeyEvent.VK_S)) {
			if (Engine.Instance.getGameMode().equals("Practice")) {
				for (GameObject gO : Engine.Instance.gameObjects) {
					if (gO.GetComponent(AIPlayerController.class) != null) {
						Debug.Log("unfreezing AI in practice mode");
						gO.GetComponent(AIPlayerController.class).frozen = false;
						gO.GetComponent(AIPlayerController.class).freezeTimer = 800;
					}
				}
			}
		}

		// Initialises a player with any weapons it may have selected before the game begins
		if (start) {
			if (Engine.Instance.getGameMode().equals("Practice")) {
				for (GameObject gO : Engine.Instance.gameObjects) {
					if (gO.GetComponent(AIPlayerController.class) != null) {
							Debug.Log("freezing AI in practice mode");
							gO.GetComponent(AIPlayerController.class).frozen = true;
							gO.GetComponent(AIPlayerController.class).freezeTimer = 5000;

					}
				}
			}

			center = true;
			if (gameObject.GetComponent(WeaponComponent.class) != null) {
				damage = gameObject.GetComponent(WeaponComponent.class).damagePower;
				Collection<BaseBooster> weaponBoosters = gameObject.GetComponent(WeaponComponent.class).boosters;
				if (!weaponBoosters.isEmpty()) {
					if (boosters.empty()) {
						boosters.addAll(0, weaponBoosters);
					} else {
						boosters.addAll(boosters.size() - 1, weaponBoosters);
					}
				}
			}

			start = false;
		}

		// Activates the scrolling screen
		if (center) {
			Window.Center(new Vector2((-gameObject.GetTransform().pos.x) + 500, (-gameObject.GetTransform().pos.y) + 500));
		}

		// Handles the timings and attributes of a player if they're frozen, invincible or squashed, and resetting after a set number of ticks
		if (freezeTimer < 0) {
			frozen = false;
			freezeTimer = 800;
		}

		if (invincibleTimer < 0 && invincible) {
			//gameObject.GetTransform().scale = new Vector2(1f, 1f);
			invincible = false;
			invincibleTimer = 800;
		}

		if (squashTimer < 0 && squashed) {
			squashed = false;
			squashTimer = 1000;
			gameObject.GetTransform().scale = new Vector2(1f, 1f);
			gameObject.GetComponent(PlayerController.class).maxSpeed = INITIAL_MAX_SPEED;
			gameObject.GetComponent(PlayerController.class).jumpSpeed = INITIAL_JUMP_SPEED;
		}

		if (speedTimer < 0 && hasSpeedBooster) {
			hasSpeedBooster = false;
			speedTimer = 1000;
			gameObject.GetComponent(PlayerController.class).maxSpeed = INITIAL_MAX_SPEED;
		}

		if (hasSpeedBooster) {
			speedTimer -= 1;
		}


		if (invincible) {
			invincibleTimer -= 1;
		}

		if (frozen) {
			freezeTimer -= 1;
		}

		if (squashed) {
			squashTimer -= 1;
		}


		if (hasKickCooldown) {
			kickCooldown -= Time.deltaTime;
			if (kickCooldown <= 0) {
				kickCooldown = COOLDOWN; // time between kicks
				hasKickCooldown = false;
			}
		}

		if (Input.KeyHold(InputKeys.RIGHT) && !frozen) {
			moveRight();
			center = true;

		} else if (Input.KeyHold(InputKeys.LEFT) && !frozen) {
			moveLeft();
			center = true;

		} else {
			decelerate();
		}

		// Handles velocity of player if they're on ground or moving ground
		if (vel.y < 0) {
			if (gameObject.GetComponent(BoxCollider.class).CheckDirection(groundLayerMask,
					Vector2.up()) < groundDist)
				vel.y = 0;
			isGrounded = false;
		} else {
			isGrounded = gameObject.GetComponent(BoxCollider.class).CheckDirection(groundLayerMask,
					Vector2.down()) < groundDist;
		}

		if (vel.y < 0) {
			if (gameObject.GetComponent(BoxCollider.class).CheckDirection(moveGroundLayerMask,
					Vector2.up()) < groundDist)
				vel.y = 0;
			isGroundedOnMove = false;
		} else {
			isGroundedOnMove = gameObject.GetComponent(BoxCollider.class).CheckDirection(moveGroundLayerMask,
					Vector2.down()) < groundDist;
		}

		checkTopBooster();

		if (Input.KeyDown(InputKeys.KICK_OR_SQUASH) && !frozen) {
			if (!hasKickCooldown) {
				hasKickCooldown = true;
				kick();
			}
			squash(gameObject);
		}

		gameObject.GetComponent(GraphicsComponent_Image.class).lookDir = xDir;

		// Allows player to double jump
		if (!isGrounded) {
			if (jumpCount == 0)
				jumpCount = 1;
			acceleration = AIR_ACCELERATION;
			vel.y += gravity * Time.deltaTime;
		} else {
			jumpCount = 0;
			acceleration = GROUND_ACCELERATION;
			vel.y = 0;
		}

		if (Input.KeyDown(InputKeys.UP) && !frozen) {
			jump();
			center = true;

		}

		PickupBooster();
		pickUpWeapon();
		pickUpCoin();

		if (isGrounded && !invincible) {
			breakGround();
		}

		if (isGroundedOnMove) {
			moveWithGround();
			center = true;

		} else {
			if (!squashed) {
				maxSpeed = INITIAL_MAX_SPEED;
				jumpSpeed = INITIAL_JUMP_SPEED;
			}
		}

		if (meteorCollision(gameObject) && !invincible) {
			frozen = true;
		}

		if (vel.x != 0 || vel.y != 0)
			gameObject.GetComponent(CharacterController.class).Move(vel.Mult(speedMultiplier * Time.deltaTime));

		if (Input.KeyDown(InputKeys.BOOSTER) && !frozen) {
			if (!boosters.isEmpty()) {
				switch (boosters.peek().toString()) { // plays the respective audio for when the player uses a booster
					case "Psychic":
						MenuModel.health.startAudio();
						break;
					case "Super Leaping":
						MenuModel.jumpBoost.startAudio();
						break;
					case "Radiant Bolt":
						MenuModel.speed.startAudio();
						break;
					case "Chrono Fusion":
						MenuModel.stun.startAudio();
						break;
					case "Berserker":
						MenuModel.damage.startAudio();
						break;
					default:
						break;
				}
				boosters.pop().useBooster(gameObject);
			}
		}

		// If a player collects a weapon, it's automatically activated
		if (!weapons.isEmpty() && !frozen) {
			if (isGrounded) {
				weapons.pop().useBooster(gameObject);
			}

		}

		// Player's health decreases if they fall off a platform
		if (!onScreen() && gameObject.GetTransform().pos.y > screenDownLimitY) {
			gameObject.GetComponent(PlayerController.class).health -= (2
					* gameObject.GetComponent(PlayerController.class).damage);
		}

		if (progressNextLevel(totalScore, Engine.Instance.currentLevelNum) && health > 0) {
			Engine.Instance.newLevel();
			Time.timeScale = 0f; // freezes the game
			MenuController.levelView.nextLevelMenu.setVisible(true);
		}

		if (restartGame()) {
			Window.health = INITIAL; // resets all in-game GUI if the game restarts
			Window.totalScore = totalScore;
			Window.currency = 900;
			Window.booster = "";

			MenuModel.gameOver.startAudio(); // plays the gameOver audio file if the player dies

			if (Engine.gamemode == "Level Editor") {
				LevelEditor.Instance.shouldReload = true;
			} else {
				Engine.Instance.restart();
			}
		}
	}

	/**
	 * Determines whether the player can progress to the next level of the game depending on their totalScore
	 * @param totalScore the player's total score
	 * @param levelNum the current level that the player is on
	 * @return true if the player has a high enough score to reach the next level
	 */
	public boolean progressNextLevel(int totalScore, int levelNum) {
		switch (levelNum) {
			case 1:
				if (totalScore > 2500) {
				GameAccountOperations.tryUpdateHighestLevel(levelNum,GameAccountOperations.playerUsername);
					return true;
				}else{
					return false;
				}
			case 2:
				if (totalScore > 2800) {
					GameAccountOperations.tryUpdateHighestLevel(levelNum,GameAccountOperations.playerUsername);
					return true;
				} else {
					return false;
				}
			case 3:
				if (totalScore > 3200) {
					GameAccountOperations.tryUpdateHighestLevel(levelNum,GameAccountOperations.playerUsername);
					return true;
				} else {
					return false;
				}
			case 4:
				if (totalScore > 3600) {
					GameAccountOperations.tryUpdateHighestLevel(levelNum,GameAccountOperations.playerUsername);
					return true;
				} else {
					return false;
				}
			case 5:
				if (totalScore > 4000) {
					GameAccountOperations.tryUpdateHighestLevel(levelNum,GameAccountOperations.playerUsername);
					return true;
				} else {
					return false;
				}
			default:
				Debug.Log("level: " + levelNum + " not handled in switch statement");
				return false;
		}

	}

	/**
	 * Makes the player move with the moving ground 
	 */
	public void moveWithGround() {
		GameObject movingGround = (MovingGroundObject) gameObject.GetComponent(BoxCollider.class)
				.GetInDirection(moveGroundLayerMask, new Vector2(0, groundDist));
		if (movingGround != null) {
			int groundDirection = movingGround.GetComponent(MovingGroundController.class).xDir;
			gameObject.GetComponent(PlayerController.class).maxSpeed = groundSpeed;
		if (groundDirection == 1) {
			moveRight();
			center = true;

		} else {
			moveLeft();
			center = true;

		}
		if (Input.KeyHold(InputKeys.RIGHT)) {
			moveRightOnMoveGround();
			center = true;

		}
		if (Input.KeyHold(InputKeys.LEFT) && !frozen) {
			moveLeftOnMoveGround();
			center = true;

		} 

		if (Input.KeyHold(InputKeys.UP) && !frozen) {
			doubleJump(isGroundedOnMove, MOVING_GROUND_ACCELERATION);
			jumpOnMoveGround();
		} 
	}

	}

	/**
	 * Changes the sprite of the player (when they have a particular booster)
	 * @param sprite sprite to change to
	 */
	public void changeSprite(BufferedImage sprite) {
	    if (!sprite.equals(currentSprite)) {
            gameObject.GetComponent(GraphicsComponent.class).changeImage(sprite);
            currentSprite = sprite;
        }
    }

	/**
	 * Breaks the ground object the player lands on depending on the strength of it
	 */
	public void breakGround() {
		GroundObject groundBreak = (GroundObject) gameObject.GetComponent(BoxCollider.class).GetInDirection(origGroundLayerMask, new Vector2(0, groundDist * 200));
		if (groundBreak != null) {
			if (!groundBreak.splitGround()) {
				int newStrength = groundBreak.GetComponent(StrengthComponent.class).getStrength() - 1;
				groundBreak.GetComponent(StrengthComponent.class).setStrength(newStrength);
			}
		}
	}

	/**
	 * Determines whether the player is still within the screen limits
	 * 
	 * @return boolean true if the player is still on the screen, returns false
	 *         otherwise
	 */
	public boolean onScreen() {
		if (gameObject.GetTransform().pos.x > screenRightLimitX || gameObject.GetTransform().pos.x < screenLeftLimitX) {
			return false;
		}
		return true;
	}

	/**
	 * Finds the player objects from a list of game objects
	 * This method was used before multiplayer mode was implemented
	 * 
	 * @param GameObjects list of game objects currently in the game
	 * @return ArrayList<GameObject> list of player objects currently in the game
	 *         (PlayerObject, EnemyObject or AIPlayerObject)
	 */
	public ArrayList<GameObject> findPlayers(List<GameObject> GameObjects) {
		ArrayList<GameObject> players = new ArrayList<GameObject>();
		for (int i = 0; i < GameObjects.size(); i++) {
			if (GameObjects.get(i).GetComponent(PlayerController.class) != null
					|| GameObjects.get(i).GetComponent(AIPlayerController.class) != null
					|| GameObjects.get(i).GetComponent(EnemyController.class) != null) {
				players.add(GameObjects.get(i));

			}
		}

		return players;
	}

	/**
	 * Determines whether the game should be restarted
	 * @return boolean returns true if game needs to be restarted
	 */
	public boolean restartGame() {
		boolean finish = false;
		ArrayList<GameObject> allPlayers = findPlayers(Engine.Instance.gameObjects);
		int deadPlayers = 0;
		for (int i = 0; i < allPlayers.size(); i++) {
			if (allPlayers.get(i).GetComponent(PlayerController.class).health <= 0) {
				deadPlayers++;
			}
		}
		// More than one player's health has decreased below 0
		if (allPlayers.size() >= 2 && deadPlayers >= allPlayers.size() - 1) {
			finish = true;
		}

		if (gameObject.GetComponent(PlayerController.class).health <= 0) {
			finish = true;
		}

		// Player falls off platform
		if (!onScreen() && gameObject.GetTransform().pos.y > screenDownLimitY) {
			finish = true;
		}

		if (gameObject.GetTransform().pos.y > screenDownLimitY) {
			finish = true;
		}

		if (!onScreen() && isGroundedOnMove) {
			finish = true;
		}

		if (obstacleCollision(gameObject) && !invincible) {
			Debug.Log("obstacle collision");
			finish = true;
		}

		return finish;
	}


	/**
	 * Checks if the player has collided with the moving obstacle
	 * 
	 * @param current player in the game
	 * @return true if the player has hit the moving obstacle
	 */
	public boolean obstacleCollision(GameObject current) {
		String[] obstacleLayerMask = { "MovingObstacle" };
		Vector2 obstacleVec = new Vector2(obstacleDist * current.GetComponent(PlayerController.class).vel.x, 0);
		GameObject obstacle = null;
		if (current.GetComponent(Collider.class) != null) {
			obstacle = (MovingObstacle) current.GetComponent(Collider.class).GetInDirection(obstacleLayerMask,
					obstacleVec);
			if (obstacle != null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the player has collided with a meteor.
	 * If the player collides with the meteor then the meteor disappears
	 *
	 * @param current player in the game
	 * @return true if the player has hit the meteor
	 */
	public boolean meteorCollision(GameObject current) {
		String[] meteorLayerMask = { "Meteor" };
		Vector2 obstacleVec = new Vector2(current.GetComponent(PlayerController.class).vel.x, boosterDist * 30); // could be zero if vel.y is set to zero in the grounded bit
		GameObject obstacle = null;
		if (current.GetComponent(Collider.class) != null) {
			obstacle = (Meteor) current.GetComponent(Collider.class).GetInDirection(meteorLayerMask,
					obstacleVec);
			if (obstacle != null) {
				current.GetComponent(PlayerController.class).health -= (5 * current.GetComponent(PlayerController.class).damage);
				Debug.Log("health after meteor: " + health);
				Engine.Instance.RemoveObject(obstacle);
				return true;
			}
		}
		return false;
	}

	/**
	 * Pick up a weapon from the game space and adds it to the weapons stack, increasing the health of the player and the player's score
	 */
	public void pickUpWeapon() {
		String[] invincibleWeaponLayerMask = { "InvincibleWeapon" };
		String[] obstacleWeaponLayerMask = { "ObstacleWeapon" };
		Vector2 weaponVec = new Vector2(Math.signum(vel.x) * 20, Math.signum(vel.y) * 20);
		BaseBooster weapon = null;
		 if (gameObject.GetComponent(Collider.class).GetInDirection(invincibleWeaponLayerMask, weaponVec) != null) {
			weapon = (InvincibleWeapon) gameObject.GetComponent(Collider.class).GetInDirection(invincibleWeaponLayerMask,
					weaponVec);
		 }

		if (gameObject.GetComponent(Collider.class).GetInDirection(obstacleWeaponLayerMask, weaponVec) != null) {
			weapon = (ObstacleWeapon) gameObject.GetComponent(Collider.class).GetInDirection(obstacleWeaponLayerMask,
					weaponVec);
		}

		if (weapon != null) {
			if (weapon.GetComponent(WeaponComponent.class) != null) {
				gameObject.GetComponent(PlayerController.class).damage = weapon.GetComponent(WeaponComponent.class).damagePower;
				weapons.push(weapon);
				if (health >= 5 && health < 10) {
					gameObject.GetComponent(PlayerController.class).health = 10;
				}
				else if (health < 5) {
					gameObject.GetComponent(PlayerController.class).health += 5;
				}
				gameObject.GetComponent(PlayerController.class).totalScore += 200;
				GameAccountOperations.tryAddSinglePlayerScores(200, GameAccountOperations.playerUsername);
				weapon.RemoveBooster();
			}
		}
	}

	/**
	 * Picks up a booster from the game space and adds it to the boosters stack,
	 * which increases the health of the player and their score
	 * 
	 */
	public void PickupBooster() {
		Vector2 boostVec = new Vector2(Math.signum(vel.x) * 20, Math.signum(vel.y) * 20); // (Math.signum(vel.x) * 20, Math.signum(vel.y) * 20);
		String[] boosterLayerMask = { "Booster" , "SpeedBooster", "DamageBooster", "JumpBooster", "StunBooster", "KnockbackBooster"};
		BaseBooster booster = null;
		float testCollision = gameObject.GetComponent(Collider.class).CheckDirection(boosterLayerMask, boostVec);
		if (gameObject.GetComponent(Collider.class).GetInDirection(boosterLayerMask, boostVec) != null) {
			booster = (BaseBooster) gameObject.GetComponent(Collider.class).GetInDirection(boosterLayerMask, boostVec);
		}

		if (booster != null && booster.getIsActive() == true) {
			boosters.push(booster);
			if (health >= 7 && health < 10) {
				gameObject.GetComponent(PlayerController.class).health = 10;
			}
			else if (health < 7) {
				gameObject.GetComponent(PlayerController.class).health += 3;
			}
			gameObject.GetComponent(PlayerController.class).totalScore += 100;
			GameAccountOperations.tryAddSinglePlayerScores(100, GameAccountOperations.playerUsername);
			booster.RemoveBooster();
			pickUpCount++;

			MenuModel.booster.startAudio(); // plays the booster audio if the player picks up a booster
		}


	}

	/**
	 * Checks the booster at the top of the player's booster stack.
	 * If the booster is the KnockbackBooster, the player's sprite changes to holding the bat.
	 */
	protected void checkTopBooster() {
        if (!boosters.isEmpty()) {
            if (boosters.peek() instanceof KnockbackBooster) {
                changeSprite(holdingBatSprite);
            } else {
            	if (!gameObject.GetComponent(GraphicsComponent.class).playingAnimation) {
					changeSprite(defaultSprite);
				}
            }
        }
    }


	/**
	 * Picks up a coin from the game space, which increases the player's total score
	 * @return value of the coin collected, returns -1 if no coin was collected
	 */
	public int pickUpCoin() {
		String[] coinLayerMask = {"Coin"};
		Vector2 boostVec = new Vector2(Math.signum(vel.x) * 20, Math.signum(vel.y) * 20);
		BaseBooster coin = (CoinObject) objectHitOnLayer(coinLayerMask, boostVec);

		if (coin != null && coin.getIsActive()) {
			currency += coin.GetComponent(MoneyComponent.class).value;
			coin.RemoveBooster();
			totalScore += coin.GetComponent(MoneyComponent.class).value;
			GameAccountOperations.tryAddSinglePlayerScores(coin.GetComponent(MoneyComponent.class).value, GameAccountOperations.playerUsername);
			currency += coin.GetComponent(MoneyComponent.class).value;
			coin.RemoveBooster();

			MenuModel.coin.startAudio(); // plays the coin audio if the player picks up the coin

			return coin.GetComponent(MoneyComponent.class).value;
		}

		return -1;
	}

	/**
	 * Confirms a player has picked up coins and is identified by their username
	 * @return player's username as given in the database
	 */
	public String playerWithCoin() {
		if (pickUpCoin() > 0) {
			return GameAccountOperations.playerUsername;
		}

		return "error";
	}

	/**
	 * Performs a kick operation on the opponent player
	 *
	 * @return boolean checks the kick operation has been successful
	 */
	public boolean kick() {
		// Play animation
		gameObject.GetComponent(GraphicsComponent_Image.class).playAnimation(kickAnimation);
		// Look for and kick player
		String[] playerLayerMask = { "Player" };
		Vector2 checkVec = new Vector2(xDir * kickDist, 0);
		GameObject opponent = objectHitOnLayer(playerLayerMask, checkVec);
		if (opponent != null && opponent != this.gameObject && !invincible) {
			Vector2 moveVec = new Vector2(kickVelVec.x * xDir, kickVelVec.y);
			opponent.GetComponent(PlayerController.class).vel = moveVec;
			opponent.GetComponent(PlayerController.class).health -= (2 * gameObject.GetComponent(PlayerController.class).damage);
			gameObject.GetComponent(PlayerController.class).totalScore += 500;

			GameAccountOperations.tryAddSinglePlayerScores(500, GameAccountOperations.playerUsername);

			MenuModel.kick.startAudio(); // plays the kick audio if the player kicks
			return true;
		} else {
			return false;
		}
	}

    public boolean kickWithParams(Vector2 kickVector, int kickDamage) {
        // Look for and kick player
        String[] playerLayerMask = { "Player" };
        Vector2 checkVec = new Vector2(xDir * kickDist, 0);
        GameObject opponent = objectHitOnLayer(playerLayerMask, checkVec);
        if (opponent != null && opponent != this.gameObject && !invincible) {
            Vector2 moveVec = new Vector2(kickVector.x * xDir, kickVector.y);
            opponent.GetComponent(PlayerController.class).vel = moveVec;
            opponent.GetComponent(PlayerController.class).health -= kickDamage;
            gameObject.GetComponent(PlayerController.class).totalScore += 500;

			GameAccountOperations.tryAddSinglePlayerScores(500, GameAccountOperations.playerUsername);

            return true;
        } else {
            return false;
        }
    }

	/**
	 * Squashes the opponent player when the specific key is pressed
	 * @param current  the current player
	 * @return  true if the squash operation is successful
	 */
	public boolean squash(GameObject current) {
		String[] playerLayerMask = { "Player" };
		Vector2 squashVec = new Vector2(0, squashDist * current.GetComponent(PlayerController.class).vel.y);
		GameObject opponent = objectHitOnLayer(playerLayerMask, squashVec);
		if (opponent != null && opponent != this.gameObject && !invincible) {
			opponent.GetComponent(PlayerController.class).health -= (3 * current.GetComponent(PlayerController.class).damage);
			opponent.GetTransform().scale = new Vector2(0.5f, 0.5f);
			opponent.GetComponent(PlayerController.class).squashed = true;
			float newMaxSpeed = opponent.GetComponent(PlayerController.class).maxSpeed / (3
					* gameObject.GetComponent(PlayerController.class).damage);
			opponent.GetComponent(PlayerController.class).maxSpeed = newMaxSpeed;
			opponent.GetComponent(PlayerController.class).jumpSpeed /= (2 * gameObject.GetComponent(PlayerController.class).damage);
			current.GetComponent(PlayerController.class).totalScore += 700;

			GameAccountOperations.tryAddSinglePlayerScores(700, GameAccountOperations.playerUsername);

			return true;
		}
		return false;
	}

	/**
	 * A helper method to determine what game object is within a given direction a layer mask.
	 * Used to detect collisions.
	 * @param layerMask the layers to check for the game object
	 * @param direction the vector to check within
	 * @return the game object in the given direction and layer mask
	 */

	public GameObject objectHitOnLayer(String[] layerMask, Vector2 direction) {

		if (gameObject.GetComponent(Collider.class) != null) {
			return gameObject.GetComponent(Collider.class).GetInDirection(layerMask, direction);
		} else {
			return null;
		}
	}

	/**
	 * Accelerates the player to the right
	 *
	 */
	public void moveRight() {
		xDir = 1;
		if (vel.x < maxSpeed) {
			vel.x += acceleration * Time.deltaTime;
			if (vel.x > maxSpeed) {
				vel.x = maxSpeed;
			}
		}
	}

	/**
	 * Accelerates the player to the left
	 *
	 */
	public void moveLeft() {
		xDir = -1;
		if (vel.x > -maxSpeed) {
			vel.x -= acceleration * Time.deltaTime;
			if (vel.x < -maxSpeed) {
				vel.x = -maxSpeed;
			}
		}

	}

	/**
	 * Decelerates the player
	 *
	 */
	public void decelerate() {
		if (vel.x < 0) {
			vel.x += 0.5f * acceleration * Time.deltaTime;
			if (vel.x > 0)
				vel.x = 0;
		} else if (vel.x > 0) {
			vel.x -= 0.5f * acceleration * Time.deltaTime;
			if (vel.x < 0)
				vel.x = 0;
		}
	}

	/**
	 * Allows the player to jump
	 */
	public void jump() {
		if (jumpCount < 2) {
			MenuModel.jump.startAudio(); // plays the jump audio if the player jumps
			vel.y = (float) -Math.sqrt(jumpSpeed * 2f * gravity);
			jumpCount++;

		}
	}

	/**
	 * Allows the player to jump while they're on moving ground
	 */
	public void jumpOnMoveGround(){
		if (jumpCountMovingGround < 2) {
			vel.y = (float) -Math.sqrt(jumpSpeed * 2f * gravity);
			jumpCountMovingGround++;
		}
	}

	/**
	 * Double jump operation for players on moving ground
	 * @param grounded boolean determining whether the player is on (moving) ground
	 * @param newAcceleration acceleration of the (moving) ground
	 */
	public void doubleJump(boolean grounded, float newAcceleration) {
		if (!grounded) {
			if (jumpCountMovingGround == 0)
				jumpCountMovingGround = 1;
			acceleration = AIR_ACCELERATION;
			vel.y += gravity * Time.deltaTime;
			if (jumpCountMovingGround == 2) {
			}
		} else {
			jumpCountMovingGround = 0;
			acceleration = newAcceleration;
			vel.y = 0;
		} 
	}

	/**
	 * Moves the player to the right while on moving ground
	 */
	public void moveRightOnMoveGround() {
		gameObject.GetComponent(PlayerController.class).maxSpeed = groundSpeed + 0.2f;
		xDir = 1;
		if (vel.x < maxSpeed) {
			vel.x += acceleration * Time.deltaTime;
			if (vel.x > maxSpeed) {
				vel.x = maxSpeed;
			}
		}
	}

	/**
	 * Moves the player to the left while on moving ground
	 */
	public void moveLeftOnMoveGround() {
		gameObject.GetComponent(PlayerController.class).maxSpeed = groundSpeed + 0.2f;
		xDir = -1;
		if (vel.x > -maxSpeed) {
			vel.x -= acceleration * Time.deltaTime;
			if (vel.x < -maxSpeed) {
				vel.x = -maxSpeed;
			}
		}
	}

	public float getMaxSpeed() {
		return this.maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
}
