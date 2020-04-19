package components;

import GameDatabase.GameAccountOperations;
import components.Colliders.BoxCollider;
import components.Colliders.Collider;
import components.Graphics.GraphicsComponent_Image;
import components.Network.Command;
import components.Network.UDPReceive3;
import customClasses.Vector2;
import debugging.Debug;
import GameObjects.*;
import logic.Time;
import managers.*;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Constantin Onofras
 * @date 2020/03/30 - 2020
 * Based on PlayerController
 * This class is used to control the 4-th player in the multiplayer mode
 */

public class MPOpponentController4Players extends PlayerController {

    public static Command command;

    private static ExecutorService service = Executors.newFixedThreadPool(1);

    private static DatagramSocket receiveSocket;

    /**
     * Determines whether the player can progress to the next level of the game depending on their totalScore (after 3 runs of the game)
     * @param totalScore the player's total score
     * @param levelNum the current level that the player is on
     * @return true if the player has a high enough score to reach the next level
     */
    public boolean progressNextLevelMP(int totalScore, int levelNum) {
        switch (levelNum) {
            case 1:
                if(totalScore>2000){
                    return true;
                }
                else return false;
            case 2:
                if (totalScore > 5000) {
                    GameAccountOperations.tryAddMultiPlayerScore(totalScore, GameAccountOperations.playerUsername);
                    return true;

                }
                else {
                    return false;
                }
            case 3:
                if (totalScore > 5500) {
                    GameAccountOperations.tryAddMultiPlayerScore(totalScore, GameAccountOperations.playerUsername);
                    return true;
                }
                else {
                    return false;
                }
            case 4:
                if (totalScore > 6000) {
                    GameAccountOperations.tryAddMultiPlayerScore(totalScore, GameAccountOperations.playerUsername);
                    return true;
                }
                else {
                    return false;
                }
            case 5:
                if (totalScore > 7000) {
                    GameAccountOperations.tryAddMultiPlayerScore(totalScore, GameAccountOperations.playerUsername);
                    return true;
                } else {
                    return false;
                }
            default:
                Debug.Log("level: " + levelNum + " not handled in switch statement");
                return false;
        }
    }

    static {
        try {
            receiveSocket = new DatagramSocket(Engine.Instance.my_port3);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public boolean exit = false;
    Date startDate;
    boolean paused = false;
    public static String COMPONENT_NAME = "MPOpponentController4";


    @Override
    public void Update() {
        if(Engine.Instance.player4_exit){
            Engine.Instance.RemoveObject(gameObject);
        }
        service.execute(new UDPReceive3(receiveSocket,Engine.Instance.my_port3));

        if(command == null){
            command=new Command("Nothing");
        }
        if(command.toString().equals("Next Level")){
            if(!Engine.Instance.new_level) {
                Engine.Instance.newLevel();
            }
            Engine.Instance.new_level = true;
            Time.timeScale = 0f; // freezes the game
            MenuController.levelView.nextLevelMenu.setVisible(true);
        }
        if (progressNextLevelMP(totalScore,Engine.Instance.currentMultiplayerLevelNum) && health > 0) {
            Engine.Instance.newLevel();
            Time.timeScale = 0f; // freezes the game
            MenuController.levelView.nextLevelMenu.setVisible(true);
        }
        if(command.toString().equals("Exit")){
            Engine.Instance.player4_exit = true;
            MultiplayerPauseView.pause.setVisible(false);
            paused=false;
            Time.timeScale = 1f;
            Engine.Instance.RemoveObject(gameObject);
        }
        if(command.toString().equals("Pause")){
            startDate = new Date();
//            System.out.println("Should Pause");
            Time.timeScale = 0f; // freezes the game
            paused = true;
//            Engine.Instance.remove_pause = false;

            MultiplayerPauseView.pause.setVisible(true);
        }
        else if(command.toString().equals("Resume")){
            MultiplayerPauseView.pause.setVisible(false);
            paused=false;
            Time.timeScale = 1f;
        }
        else if(Time.timeScale == 0f && paused){
            Date endDate = new Date();
            int numSeconds = (int)((endDate.getTime() - startDate.getTime()) / 1000);
            if(numSeconds >= 15){
                Engine.Instance.player2_exit=true;
                Engine.Instance.paused = false;
                MultiplayerPauseView.pause.setVisible(false);

                Time.timeScale = 1f;
            }
//            System.out.println("Time elapsed: "+numSeconds + " in Opponent");
        }


        if (!boosters.empty()) {

        } else if (boosters.empty()) { // if the player has no boosters equipped then we adjust this accordingly on the GUI

        }

        if (start) {
            // this can probably be cleaned up later and put into one function where the input is a weapon component, whether that be on the player at the start of the game or collected in the game space

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

        if (freezeTimer < 0) {
            frozen = false;
        }

        if (invincibleTimer < 0 && invincible) {
            invincible = false;
        }

        if (squashTimer < 0 && squashed) {
            squashed = false;
            gameObject.GetTransform().scale = new Vector2(1f, 1f);
            gameObject.GetComponent(PlayerController.class).maxSpeed = INITIAL_MAX_SPEED;
            gameObject.GetComponent(PlayerController.class).jumpSpeed = INITIAL_JUMP_SPEED;
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

        if (command.toString().startsWith("Right") && !frozen) {
            String[] holder = command.toString().split(";");
            String[] holding_x = holder[1].split("X:");
            String[] holding_y = holder[2].split("Y:");
            moveRight(); /// UPDATE THE POSITION ONLY WHEN YOU MOVE
            gameObject.GetTransform().pos.setX(Float.parseFloat(holding_x[1]));
            gameObject.GetTransform().pos.setY(Float.parseFloat(holding_y[1]));


        } else if (command.toString().startsWith("Left") && !frozen) {
            String[] holder = command.toString().split(";");
            String[] holding_x = holder[1].split("X:");
            String[] holding_y = holder[2].split("Y:");
            moveLeft();
            gameObject.GetTransform().pos.setX(Float.parseFloat(holding_x[1]));
            gameObject.GetTransform().pos.setY(Float.parseFloat(holding_y[1]));

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

        if (command.toString().equals("Kick or Squash") && !frozen) {
            if (!hasKickCooldown) {
                hasKickCooldown = true;
                if(kick()){
                    System.out.println("PLayer 4: Kicked");
                }
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

        if (command.toString().equals("Jump") && !frozen) {
            jump();
        }

        PickupBooster();
        pickUpWeapon();
        pickUpCoin();

        if (isGrounded && !invincible) {
            breakGround();
        }

        if (isGroundedOnMove) {
            moveWithGround();

        } else {
            if (!squashed) {
                maxSpeed = INITIAL_MAX_SPEED;
            }
        }

        if (meteorCollision(gameObject) && !invincible ||command.toString().equals("Frozen")) {
            frozen = true;
        }

        if (vel.x != 0 || vel.y != 0)
            gameObject.GetComponent(CharacterController.class).Move(vel.Mult(speedMultiplier * Time.deltaTime));


        if (command.toString().equals("USE BOOSTER") && !frozen) {
            if (!boosters.isEmpty()) {
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


        if (!onScreen() && gameObject.GetTransform().pos.y > screenDownLimitY) {
            gameObject.GetComponent(PlayerController.class).health -= (2
                    * gameObject.GetComponent(PlayerController.class).damage);
            Debug.Log("health after offscreen: " + health);
        }


        if (restartGame()||command.toString().equals("Restart")) {
            Engine.Instance.restarted = true;
//            MenuModel.gameOver.startAudio(); // plays the gameOver audio file if the player dies

            Engine.Instance.restart();
//            Debug.Log("gameRuns: " + Engine.Instance.gameRuns);
//            if (Engine.Instance.gameRuns < 3) {
//                Debug.Log("total score before restart: " + totalScore);
//                Engine.Instance.restart();
//                Debug.Log("total score after restart: " + totalScore);
//
//            } else {
////                if (!(progressNextLevelMP(totalScore,Engine.Instance.currentMultiplayerLevelNum) && health > 0)) {
////                    exit=true;
////                }
//            }

        }
        command=new Command("Nothing");
    }

    /**
     * Pick up a weapon from the game space and adds it to the weapons stack, increasing the health of the player and the player's score
     */
    @Override
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
//                GameAccountOperations.tryAddSinglePlayerScores(200, GameAccountOperations.username);
                weapon.RemoveBooster();
            }
        }
    }
    /**
     * Squashes the opponent player when the specific key is pressed
     * @param current  the current player
     * @return  true if the squash operation is successful
     */
    @Override
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
//            GameAccountOperations.tryAddSinglePlayerScores(700, GameAccountOperations.username);
            return true;
        }
        return false;
    }

    /**
     * Picks up a booster from the game space and adds it to the boosters stack,
     * which increases the health of the player and their score
     *
     */
    @Override
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
//            GameAccountOperations.tryAddSinglePlayerScores(100, GameAccountOperations.username);
            booster.RemoveBooster();
            pickUpCount++;

            MenuModel.booster.startAudio(); // plays the booster audio if the player picks up a booster
        }


    }

    /**
     * Picks up a coin from the game space, which increases the player's total score
     * @return value of the coin collected, returns -1 if no coin was collected
     */
    @Override
    public int pickUpCoin() {
        String[] coinLayerMask = {"Coin"};
        Vector2 boostVec = new Vector2(Math.signum(vel.x) * 20, Math.signum(vel.y) * 20);
        BaseBooster coin = (CoinObject) objectHitOnLayer(coinLayerMask, boostVec);

        if (coin != null && coin.getIsActive()) {
            currency += coin.GetComponent(MoneyComponent.class).value;
            coin.RemoveBooster();
            totalScore += coin.GetComponent(MoneyComponent.class).value;
//            GameAccountOperations.tryAddSinglePlayerScores(coin.GetComponent(MoneyComponent.class).value, GameAccountOperations.username);
            currency += coin.GetComponent(MoneyComponent.class).value;
            coin.RemoveBooster();

            MenuModel.coin.startAudio(); // plays the coin audio if the player picks up the coin

            return coin.GetComponent(MoneyComponent.class).value;
        }

        return -1;
    }

    /**
     * Performs a kick operation on the opponent player
     *
     * @return boolean checks the kick operation has been successful
     */
    @Override
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
//            GameAccountOperations.tryAddSinglePlayerScores(500, GameAccountOperations.username);
            MenuModel.kick.startAudio(); // plays the kick audio if the player kicks
            return true;
        } else {
            return false;
        }
    }

}
