package components;

import GameDatabase.GameAccountOperations;
import components.Colliders.BoxCollider;
import components.Colliders.Collider;
import components.Graphics.GraphicsComponent_Image;
import components.Network.Command;
import components.Network.UDPSend;
import customClasses.InputKeys;
import customClasses.Vector2;
import debugging.Debug;
import GameObjects.*;
import logic.LevelEditor;
import logic.Time;
import managers.*;
import managers.Window;

import java.awt.*;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Constantin Onofras and Wenhan Wei
 * @date 2020/03/16
 * Based on PlayerController
 *
 * This class controls the local player's character in the multiplayer mode
 */

public class MPPlayerController extends PlayerController {
    private static boolean score_added = false;

    public static Command commandToBeSend;

    /**
     * Total score for the player, which will be added to the leaderboard (and therefore stored in the database)
     */
    public int totalScore = 0;

    private static ExecutorService service = Executors.newFixedThreadPool(1);

    private static DatagramSocket sendSocket;
//    private int health = INITIAL;
//    Vector2 attackVec;

    static {
        try {
            Random r=new Random();
            int port = r.nextInt(10000-1024)+1024;
            while(port == Engine.Instance.my_port1 || port == Engine.Instance.my_port2 || port == Engine.Instance.my_port3){
                port = r.nextInt(10000-1024)+1024;
            }
            sendSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public static String COMPONENT_NAME = "PlayerController";


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
                if (totalScore > 3000) {
                    GameAccountOperations.tryAddMultiPlayerScore(totalScore, GameAccountOperations.playerUsername);
                    return true;

                }
                else {
                    return false;
                }
            case 3:
                if (totalScore > 3500) {
                    GameAccountOperations.tryAddMultiPlayerScore(totalScore, GameAccountOperations.playerUsername);
                    return true;
                }
                else {
                    return false;
                }
            case 4:
                if (totalScore > 4000) {
                    GameAccountOperations.tryAddMultiPlayerScore(totalScore, GameAccountOperations.playerUsername);
                    return true;
                }
                else {
                    return false;
                }
            case 5:
                if (totalScore > 4500) {
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

    @Override
    public void Update() {
        if(Engine.Instance.restarted){
            totalScore = Engine.Instance.getHold_score();
            Engine.Instance.restarted = false;
        }
        else {
            Engine.Instance.setHold_score(totalScore);
        }
        Window.health = health; // will draw the relevant component to the screen
        Window.totalScore = totalScore;
        Window.currency = currency;
        if (Engine.Instance.players == 4){
            if (Engine.Instance.player3_exit & Engine.Instance.player2_exit & Engine.Instance.player4_exit) {
                Time.timeScale = 0f; // freezes the game
                if(!score_added) {
                    totalScore = Engine.Instance.getHold_score();
                    GameAccountOperations.tryAddMultiPlayerScore(totalScore, GameAccountOperations.playerUsername);
                    score_added = true;
                    Engine.Instance.player2_exit=false;
                    Engine.Instance.player3_exit=false;
                    Engine.Instance.player4_exit=false;
                }
                MenuController.gameOverView.gameOverMenu.setVisible(true);
            }
        }
        else if(Engine.Instance.players == 3){
            if(Engine.Instance.player2_exit & Engine.Instance.player3_exit){
                Time.timeScale = 0f; // freezes the game
                if(!score_added) {
                    totalScore = Engine.Instance.getHold_score();
                    GameAccountOperations.tryAddMultiPlayerScore(totalScore, GameAccountOperations.playerUsername);
                    score_added = true;
                    Engine.Instance.player2_exit = false;
                    Engine.Instance.player3_exit = false;
                }
                MenuController.gameOverView.gameOverMenu.setVisible(true);
            }
        }
        else if(Engine.Instance.players==2){
            if(Engine.Instance.player2_exit){
                Time.timeScale = 0f; // freezes the game
                if(!score_added) {
//                    totalScore = Engine.Instance.getHold_score();
                    GameAccountOperations.tryAddMultiPlayerScore(totalScore, GameAccountOperations.playerUsername);
                    System.out.println("Score Added: "+totalScore);
                    score_added = true;
                    Engine.Instance.player2_exit=false;
                }
                try {
                    PauseView.pauseMenu.setVisible(false);
                }catch (Exception e){

                }
                MenuController.gameOverView.gameOverMenu.setVisible(true);
            }
        }
        if (!boosters.empty()) {
            Window.booster = boosters.peek().toString();
        } else if (boosters.empty()) { // if the player has no boosters equipped then we adjust this accordingly on the GUI
            Window.booster = "";
        }
        if(Time.timeScale ==0f & Engine.Instance.paused){
            Date endDate = new Date();
            int numSeconds = (int)((endDate.getTime() - Window.pauseDate.getTime()) / 1000);
            if(numSeconds >= 30){
                Engine.Instance.unloadAllScenes();
                Window.running = false;
                Engine.Instance.paused = false;
                Engine.Instance.running = false;
                Window.frame.dispose();
                PauseView.pauseMenu.setVisible(false);
                if(Engine.Instance.players == 4){
                    MenuController.lobbyView.player1_4Player.setText("Player 1");
                    MenuController.lobbyView.player1_4Player.setForeground(Color.WHITE);
                    MenuController.lobbyView.player2_4Player.setText("Player 2");
                    MenuController.lobbyView.player2_4Player.setForeground(Color.WHITE);
                    MenuController.lobbyView.player2Score_4.setText("score");
                    MenuController.lobbyView.player3_4Player.setText("Player 3");
                    MenuController.lobbyView.player3_4Player.setForeground(Color.WHITE);
                    MenuController.lobbyView.player3Score_4.setText("score");
                    MenuController.lobbyView.player4_4Player.setText("Player 4");
                    MenuController.lobbyView.player4_4Player.setForeground(Color.WHITE);
                    MenuController.lobbyView.player4Score_4.setText("score");
                    Engine.Instance.player2_exit = false;
                    Engine.Instance.player3_exit = false;
                    Engine.Instance.player4_exit = false;
                }
                else if(Engine.Instance.players == 3){
                    MenuController.lobbyView.player1_3Player.setText("Player 1");
                    MenuController.lobbyView.player1_3Player.setForeground(Color.WHITE);
                    MenuController.lobbyView.player2_3Player.setText("PLayer 2");
                    MenuController.lobbyView.player2_3Player.setForeground(Color.WHITE);
                    MenuController.lobbyView.player2Score_3.setText("score");
                    MenuController.lobbyView.player3_3Player.setText("Player 3");
                    MenuController.lobbyView.player3_3Player.setForeground(Color.WHITE);
                    MenuController.lobbyView.player3Score_3.setText("score");
                    Engine.Instance.player2_exit = false;
                    Engine.Instance.player3_exit = false;
                }
                else if(Engine.Instance.players == 2){
                    MenuController.lobbyView.player1_2Player.setText("Player 1");
                    MenuController.lobbyView.player1_2Player.setForeground(Color.WHITE);
                    MenuController.lobbyView.player2_2Player.setText("Player 2");
                    MenuController.lobbyView.player2_2Player.setForeground(Color.WHITE);
                    MenuController.lobbyView.player2Score.setText("score");
                    Engine.Instance.player2_exit = false;
                }
                MenuController.viewing = true;
                MenuController.engine_started = false;
//                MenuController.close_update = false;
                MenuController.close_update = true;
                MenuController.lobbyView.getCardLayout().show(MenuController.lobbyView.getLobbyContainer(), "Main Lobby");
                MenuController.cardLayout.show(MenuController.jPanel, "Play View");
                try {
                    MenuController.connection.Back();
                }catch (Exception ce){

                }
                MenuController.jFrame.setVisible(true);
                LevelEditor.Instance = null;
                Time.timeScale = 1f;
            }
            System.out.println("Time elapsed: "+numSeconds);
        }

        if (start) {
            // this can probably be cleaned up later and put into one function where the input is a weapon component,
            // whether that be on the player at the start of the game or collected in the game space
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

        if (center) {
            Window.Center(new Vector2((-gameObject.GetTransform().pos.x) + 500, (-gameObject.GetTransform().pos.y) + 500)); //770 for pos.y
        }

        if (freezeTimer < 0) {
            frozen = false;
            freezeTimer=0;
        }

        if (invincibleTimer < 0 && invincible) {
            commandToBeSend = new Command("Invincible False");
            if(Engine.Instance.players == 4) {
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(2), Engine.Instance.port2,commandToBeSend));
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(3), Engine.Instance.port3,commandToBeSend));
            }
            else if (Engine.Instance.players == 3){
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(2), Engine.Instance.port2,commandToBeSend));
            }
            else{
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
            }
            invincible = false;
        }

        if (squashTimer < 0 && squashed) {
            commandToBeSend = new Command("Squash False");
            if(Engine.Instance.players == 4) {
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(2), Engine.Instance.port2,commandToBeSend));
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(3), Engine.Instance.port3,commandToBeSend));
            }
            else if (Engine.Instance.players == 3){
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(2), Engine.Instance.port2,commandToBeSend));
            }
            else{
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
            }
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


        if (Input.KeyHold(InputKeys.RIGHT) && !frozen) {
            commandToBeSend = new Command("Right; X:"+gameObject.GetTransform().pos.x +";Y:"+gameObject.GetTransform().pos.y);
            if(Engine.Instance.players == 4) {
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(2), Engine.Instance.port2,commandToBeSend));
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(3), Engine.Instance.port3,commandToBeSend));
            }
            else if (Engine.Instance.players == 3){
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(2), Engine.Instance.port2,commandToBeSend));
            }
            else{
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
            }
            moveRight();
            center = true;

        } else if (Input.KeyHold(InputKeys.LEFT) && !frozen) {
            commandToBeSend = new Command("Left; X:"+gameObject.GetTransform().pos.x+";Y:"+gameObject.GetTransform().pos.y);
            if(Engine.Instance.players == 4) {
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(2), Engine.Instance.port2,commandToBeSend));
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(3), Engine.Instance.port3,commandToBeSend));
            }
            else if (Engine.Instance.players == 3){
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(2), Engine.Instance.port2,commandToBeSend));
            }
            else{
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
            }
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

        if (Input.KeyDown(InputKeys.KICK_OR_SQUASH) && !frozen) {
            commandToBeSend = new Command("Kick or Squash");
            if(Engine.Instance.players == 4) {
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(2), Engine.Instance.port2,commandToBeSend));
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(3), Engine.Instance.port3,commandToBeSend));
            }
            else if (Engine.Instance.players == 3){
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(2), Engine.Instance.port2,commandToBeSend));
            }
            else{
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
            }
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

        if (Input.KeyDown(InputKeys.UP) && !frozen) {

            commandToBeSend = new Command("Jump");
            if(Engine.Instance.players == 4) {
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(2), Engine.Instance.port2,commandToBeSend));
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(3), Engine.Instance.port3,commandToBeSend));
            }
            else if (Engine.Instance.players == 3){
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(2), Engine.Instance.port2,commandToBeSend));
            }
            else{
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
            }
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
            }
        }

        if (meteorCollision(gameObject) && !invincible) {
            frozen = true;
            commandToBeSend = new Command("Frozen");
            if(Engine.Instance.players == 4) {
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(2), Engine.Instance.port2,commandToBeSend));
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(3), Engine.Instance.port3,commandToBeSend));
            }
            else if (Engine.Instance.players == 3){
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(2), Engine.Instance.port2,commandToBeSend));
            }
            else{
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
            }
        }

        if (vel.x != 0 || vel.y != 0)
            gameObject.GetComponent(CharacterController.class).Move(vel.Mult(speedMultiplier * Time.deltaTime));

        if (centerX) {
            Window.Center(new Vector2((-gameObject.GetTransform().pos.x) + 500, 0));
        }


        if (Input.KeyDown(InputKeys.BOOSTER) && !frozen) {
            commandToBeSend = new Command("USE BOOSTER");
            if(Engine.Instance.players == 4) {
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(2), Engine.Instance.port2,commandToBeSend));
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(3), Engine.Instance.port3,commandToBeSend));
            }
            else if (Engine.Instance.players == 3){
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(2), Engine.Instance.port2,commandToBeSend));
            }
            else{
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
            }
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
                System.out.println(gameObject.toString());
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


        if (progressNextLevelMP(totalScore, Engine.Instance.currentMultiplayerLevelNum) && health > 0) {
            if(!Engine.Instance.new_level) {
                Engine.Instance.newLevel();
            }
            Time.timeScale = 0f; // freezes the game
            commandToBeSend = new Command("Next Level");
            if(Engine.Instance.players == 4) {
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(2), Engine.Instance.port2,commandToBeSend));
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(3), Engine.Instance.port3,commandToBeSend));
            }
            else if (Engine.Instance.players == 3){
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(2), Engine.Instance.port2,commandToBeSend));
            }
            else{
                service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
            }

            MenuController.levelView.nextLevelMenu.setVisible(true);
        }

        if (restartGame()) {

            Engine.Instance.setHold_score(totalScore);
            System.out.println("Hold Score: "+Engine.Instance.getHold_score());

            if(totalScore<2000 && Engine.Instance.players ==2){
//                MenuModel.gameOver.startAudio(); // plays the gameOver audio file if the player dies
                commandToBeSend = new Command("Exit");
                if(Engine.Instance.players == 4) {
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(2), Engine.Instance.port2,commandToBeSend));
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(3), Engine.Instance.port3,commandToBeSend));
                }
                else if (Engine.Instance.players == 3){
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(2), Engine.Instance.port2,commandToBeSend));
                }
                else{
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                }
//                MenuModel.gameOver.startAudio(); // plays the gameOver audio file if the player dies
                Time.timeScale = 0f;
                MenuController.lostView.gameOverMenu.setVisible(true);
            }
            else if(totalScore<3000 && Engine.Instance.players ==3){
//                MenuModel.gameOver.startAudio(); // plays the gameOver audio file if the player dies
                commandToBeSend = new Command("Exit");
                if(Engine.Instance.players == 4) {
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(2), Engine.Instance.port2,commandToBeSend));
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(3), Engine.Instance.port3,commandToBeSend));
                }
                else if (Engine.Instance.players == 3){
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(2), Engine.Instance.port2,commandToBeSend));
                }
                else{
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                }
//                MenuModel.gameOver.startAudio(); // plays the gameOver audio file if the player dies
                Time.timeScale = 0f;
                MenuController.lostView.gameOverMenu.setVisible(true);
            }
            else if(totalScore<4000 && Engine.Instance.players ==4){
//                MenuModel.gameOver.startAudio(); // plays the gameOver audio file if the player dies
                commandToBeSend = new Command("Exit");
                if(Engine.Instance.players == 4) {
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(2), Engine.Instance.port2,commandToBeSend));
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(3), Engine.Instance.port3,commandToBeSend));
                }
                else if (Engine.Instance.players == 3){
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(2), Engine.Instance.port2,commandToBeSend));
                }
                else{
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                }
//                MenuModel.gameOver.startAudio(); // plays the gameOver audio file if the player dies
                Time.timeScale = 0f;
                MenuController.lostView.gameOverMenu.setVisible(true);
            }
            else {

                Window.health = INITIAL; // resets all in-game GUI if the game restarts
                Window.totalScore = totalScore;
                Window.currency = 900;
                Window.booster = "";
                commandToBeSend = new Command("Restart");
                if(Engine.Instance.players == 4) {
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(2), Engine.Instance.port2,commandToBeSend));
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(3), Engine.Instance.port3,commandToBeSend));
                }
                else if (Engine.Instance.players == 3){
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(2), Engine.Instance.port2,commandToBeSend));
                }
                else{
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                }
                if (Engine.gamemode == "Level Editor") {
                    LevelEditor.Instance.shouldReload = true;
                } else {
                    Engine.Instance.restart();
                }
            }

        }
//        commandToBeSend=new Command("Nothing");
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
                gameObject.GetComponent(MPPlayerController.class).totalScore += 200;
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
            current.GetComponent(MPPlayerController.class).totalScore += 700;
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
            gameObject.GetComponent(MPPlayerController.class).totalScore += 100;
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
            gameObject.GetComponent(MPPlayerController.class).totalScore += 500;
//            GameAccountOperations.tryAddSinglePlayerScores(500, GameAccountOperations.username);
            MenuModel.kick.startAudio(); // plays the kick audio if the player kicks
            return true;
        } else {
            return false;
        }
    }



}