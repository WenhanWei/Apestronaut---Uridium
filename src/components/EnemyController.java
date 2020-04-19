package components;

import java.awt.event.KeyEvent;
import java.util.Collection;

import components.Colliders.BoxCollider;
import components.Graphics.GraphicsComponent_Image;
import customClasses.Vector2;
import debugging.Debug;
import GameObjects.BaseBooster;
import logic.Time;
import managers.*;

/**
 * The main controller component for an enemy player
 * This class is a placeholder for an opponent player
 */
public class EnemyController extends PlayerController {

	@Override
	public void Update() {
		Window.health = health;
		Window.totalScore = totalScore;
		Window.currency = currency;

		if (!boosters.empty()) {
			Window.booster = boosters.peek().toString();
		} else if (boosters.empty()) { // if the player has no boosters equipped then we adjust this accordingly on the GUI
			Window.booster = "";
		}

		// Initialises a player with any weapons it may have selected before the game begins
		if (start) {
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
				kickCooldown = COOLDOWN;
				hasKickCooldown = false;
			}
		}

		if (Input.KeyDown(KeyEvent.VK_D) && !frozen) {
			moveRight();
			center = true;

		} else if (Input.KeyDown(KeyEvent.VK_A) && !frozen) {
			moveLeft();
			center = true;

		} else {
			decelerate();
		}

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

		if (Input.KeyDown(KeyEvent.VK_F) && !frozen) {
			if (!hasKickCooldown) {
				hasKickCooldown = true;
				kick();
			}
			squash(gameObject);
		}


		gameObject.GetComponent(GraphicsComponent_Image.class).lookDir = xDir;

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

		if (Input.KeyDown(KeyEvent.VK_W) && !frozen) {
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

		if (centerX) {
			Window.Center(new Vector2((-gameObject.GetTransform().pos.x) + 500, 0));
		}

		if (Input.KeyDown(KeyEvent.VK_Q) && !frozen) {
			if (!boosters.isEmpty()) {
				Debug.Log("about to use: " + boosters.peek().toString());
				switch (boosters.peek().toString()) { // plays the respective audio for when the player uses a booster
					case "Base Boost":
						MenuModel.health.startAudio();
						break;
					case "Jump Boost":
						MenuModel.jumpBoost.startAudio();
						break;
					case "Speed Boost":
						MenuModel.speed.startAudio();
						break;
					case "Stun Boost":
						MenuModel.stun.startAudio();
						break;
					case "Damage Boost":
						MenuModel.damage.startAudio();
						break;
					default:
						break;
				}
				boosters.pop().useBooster(gameObject);
			}
		}

		if (!weapons.isEmpty() && !frozen) {
			if (isGrounded) {
				weapons.pop().useBooster(gameObject);
			}

		}

		if (!onScreen() && gameObject.GetTransform().pos.y > screenDownLimitY) { // used to be &&
			gameObject.GetComponent(PlayerController.class).health -= (2
					* gameObject.GetComponent(PlayerController.class).damage);
			Debug.Log("health after offscreen: " + health);
		}


		if (restartGame()) {
			Window.health = 0; // resets all in-game GUI if the game restarts
			Window.totalScore = 0;
			Window.currency = 900;
			Window.booster = "";

			MenuModel.gameOver.startAudio(); // plays the gameOver audio file if the player dies
			Engine.Instance.restart();
		}
	}



}
