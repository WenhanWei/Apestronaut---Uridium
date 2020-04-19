package components;

import java.util.ArrayList;
import components.Colliders.BoxCollider;
import components.Colliders.Collider;
import components.Graphics.GraphicsComponent_Image;
import customClasses.Vector2;
import customClasses.AIObjectives.AttackObjective;
import customClasses.AIObjectives.Objective;
import debugging.Debug;
import GameObjects.GameObject;
import GameObjects.PlayerObject;
import logic.Scene;
import logic.Time;
import managers.Engine;
import managers.MenuController;

/**
 * The main controller component for an AI player
 */
public class AIPlayerController extends PlayerController {


	public Objective currentObjective = null;

	public int damage = 1;

	public static String COMPONENT_NAME = "AIPlayerController";
	/**
	 * List of other players in the scene (AI and non-AI)
	 */
	private ArrayList<GameObject> players = new ArrayList<GameObject>();

	/**
	 * The player that this AI is currently targeting
	 */
	public GameObject targetPlayer = null;
	/**
	 * The distance the AI will trail behind a target player
	 */
	int followDistance = 175;
	
	boolean started = false;

	Transform transform;


	public void initialise() {
		transform = gameObject.GetTransform();
		currentObjective = new AttackObjective(gameObject);
	}

	/**
	 * Contains the main logic for the AI
	 *
	 */

	@Override
	public void Update() {

		if (!started) {
			if (Scene.sceneLoaded) {
				initialise();
				started = true;
			}
		}

		if (freezeTimer < 0) {
			frozen = false;
			freezeTimer = 800;
		}

		if (frozen) {
			freezeTimer -= 1;
		}

		if (squashTimer < 0 && squashed) {
			squashed = false;
			squashTimer = 1000;
			gameObject.GetTransform().scale = new Vector2(1f, 1f);
			gameObject.GetComponent(PlayerController.class).maxSpeed = INITIAL_MAX_SPEED;
			gameObject.GetComponent(PlayerController.class).jumpSpeed = INITIAL_JUMP_SPEED;
		}

		if (squashed) {
			squashTimer -= 1;
		}

		if (invincibleTimer < 0 && invincible) {
			//gameObject.GetTransform().scale = new Vector2(1f, 1f);
			invincible = false;
			invincibleTimer = 800;
		}

		if (speedTimer < 0 && hasSpeedBooster) {
			hasSpeedBooster = false;
			speedTimer = 1000;
			gameObject.GetComponent(PlayerController.class).maxSpeed = INITIAL_MAX_SPEED;
			gameObject.GetComponent(PlayerController.class).acceleration = AIR_ACCELERATION;
		}

		if (hasSpeedBooster) {
			speedTimer -= 1;
		}

		if (invincible) {
			invincibleTimer -= 1;
		}

		if (!frozen) {
			if (vel.y < 0) {
				if (gameObject.GetComponent(BoxCollider.class).CheckDirection(groundLayerMask, Vector2.up()) < groundDist)
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

			if (!onScreen() && gameObject.GetTransform().pos.y > screenDownLimitY) {
				gameObject.GetComponent(PlayerController.class).health -= (2
						* gameObject.GetComponent(PlayerController.class).damage);
			}

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
			if (hasKickCooldown) {
				kickCooldown -= Time.deltaTime;
				if (kickCooldown <= 0) {
					kickCooldown = COOLDOWN; // time between kicks
					hasKickCooldown = false;
				}
			}

			if (isGrounded && !invincible) {
				breakGround();
			}

			if (isGroundedOnMove) {
				moveWithGround();
			} else {
				if (!squashed) {
					maxSpeed = INITIAL_MAX_SPEED;
					jumpSpeed = INITIAL_JUMP_SPEED;
				}
			}

			if (currentObjective != null && !frozen) {
				Objective newObjective = currentObjective.getNewObjective();
				if (newObjective != null) {
					currentObjective = newObjective;
				}
				currentObjective.Update();
			}



			if (vel.x != 0 || vel.y != 0) {
				gameObject.GetComponent(CharacterController.class).Move(vel.Mult(speedMultiplier * Time.deltaTime));

			}

			gameObject.GetComponent(GraphicsComponent_Image.class).lookDir = xDir;

			PickupBooster();
			pickUpWeapon();
			pickUpCoin();
			checkTopBooster();


			if (!weapons.isEmpty() && !frozen) {
				if (isGrounded) {
					weapons.pop().useBooster(gameObject);
				}

			}

			if (!boosters.isEmpty() && !frozen) {
				boosters.pop().useBooster(gameObject);
				Debug.Log("AI uses booster");
			}

			if (meteorCollision(gameObject) && !invincible) {
				frozen = true;
			}

			if (restartGame()) {
				Engine.Instance.restart();
				Engine.Instance.gameRuns++;
				if (Engine.Instance.gameRuns < 3) {
					Engine.Instance.restart();
				} else {
					Engine.Instance.gameRuns = 0;
					Engine.Instance.newLevel();
					Time.timeScale = 0f; // freezes the game
					MenuController.levelView.nextLevelMenu.setVisible(true);
				}
			}
		}
	}

	/**
	 * Checks if there is a platform above the AI
	 */
	public boolean checkPlatformAbove() {
		String[] platformMask = { "Ground" };
		Vector2 checkVec = new Vector2(0, -200);
		if (gameObject.GetComponent(Collider.class) != null) {
			GameObject platform = (GameObject) gameObject.GetComponent(Collider.class).GetInDirection(platformMask,
					checkVec);
			if (platform != null) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * Finds and returns the nearest player
	 * 
	 * @return the nearest player
	 */
	public GameObject chooseTargetPlayer() {
		GameObject targetPlayer = null;
		Vector2 currentPos = transform.pos;
		float shortestDist = Float.MAX_VALUE;
		float tempDist;
		for (GameObject target : PlayerObject.players) {
			if (target == gameObject)
				continue;
			if ((tempDist = Vector2.distance(currentPos, target.GetTransform().pos)) < shortestDist) {
				shortestDist = tempDist;
				targetPlayer = target;
			}
		}
		return targetPlayer;
	}

	@Override
	public boolean kick() {
		if (!hasKickCooldown) {
			hasKickCooldown = true;
			return super.kick();
		} else {
			return false;
		}
	}
}
