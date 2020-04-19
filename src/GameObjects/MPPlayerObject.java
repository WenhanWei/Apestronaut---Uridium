package GameObjects;


import java.util.ArrayList;
import java.util.List;
import GameDatabase.PlayerAccount;
import components.CharacterController;
import components.Graphics.GraphicsComponent_Image;
import components.MPPlayerController;
import components.Colliders.BoxCollider;
import components.Colliders.Collider;
import components.Graphics.GraphicsComponent_Animated;
import components.WeaponComponent;
import customClasses.Vector2;

/**
 * A base GameObject representing a player
 */
public class MPPlayerObject extends GameObject {

    /**
     * @author Constantin Onofras
     * @date 2020/03/30 - 2020
     * Based on PlayerObject and AIPlayerObject
     */
    final int WIDTH = 74, HEIGHT = 74;

    public static List<GameObject> players = new ArrayList<GameObject>();

    int range = 100;

    /**
     * Default constructor for multiplayer player object
     */
    public MPPlayerObject() {
        super(new Vector2(0, 0));
        players.add(this);
        ID = Math.random() * range;
        AddComponent(
                new GraphicsComponent_Image(new Vector2(WIDTH, HEIGHT),
                        "src/resources/sprites/player/player_monkey.png",
                        -1),
                GraphicsComponent_Image.COMPONENT_NAME);
        BoxCollider collider = new BoxCollider("Player", new Vector2(WIDTH, HEIGHT));
        AddComponent(collider, Collider.COMPONENT_NAME);
        AddComponent(CharacterController.class, CharacterController.COMPONENT_NAME);
        AddComponent(MPPlayerController.class, MPPlayerController.COMPONENT_NAME);
        WeaponComponent noWeapon = new WeaponComponent();
        AddComponent(noWeapon, WeaponComponent.COMPONENT_NAME);
        PlayerAccount thisAccount = new PlayerAccount();
    }

    /**
     * Constructor for the multiplayer player object where initial position and player size is predetermined
     * @param initialPos starting position of the player
     * @param size width and height of the player (assumes the player has a square graphics component)
     */
    public MPPlayerObject(Vector2 initialPos, int size) {
        super(initialPos);
        players.add(this);
        ID = Math.random() * range;
        AddComponent(
                new GraphicsComponent_Animated(new Vector2(size, size),
                        new String[] { "src/resources/sprites/player/temp.png",
                                "src/resources/sprites/player/temp2.png" },
                        -1, new float[] { 1, 1 }),
                GraphicsComponent_Animated.COMPONENT_NAME);
        BoxCollider collider = new BoxCollider("Player", new Vector2(size, size));
        AddComponent(collider, Collider.COMPONENT_NAME);
        AddComponent(CharacterController.class, CharacterController.COMPONENT_NAME);
        AddComponent(MPPlayerController.class, MPPlayerController.COMPONENT_NAME);
        WeaponComponent noWeapon = new WeaponComponent();
        AddComponent(noWeapon, WeaponComponent.COMPONENT_NAME);
        PlayerAccount thisAccount = new PlayerAccount();
    }

    /**
     * Constructor for the multiplayer player object that owns a weapon
     * @param weaponName
     * @param damage the damage power of the weapon, which will increase the player's damage
     * @param cost the cost of the weapon (in respect to the player's currency value)
     */
    public MPPlayerObject(String weaponName, int damage, int cost) {
        super(new Vector2(0, 0));
        players.add(this);
        ID = Math.random() * range;
        AddComponent(
                new GraphicsComponent_Animated(new Vector2(WIDTH, HEIGHT),
                        new String[] { "src/resources/sprites/player/temp.png",
                                "src/resources/sprites/player/temp2.png" },
                        -1, new float[] { 1, 1 }),
                GraphicsComponent_Animated.COMPONENT_NAME);
        BoxCollider collider = new BoxCollider("Player", new Vector2(WIDTH, HEIGHT));
        AddComponent(collider, Collider.COMPONENT_NAME);
        AddComponent(CharacterController.class, CharacterController.COMPONENT_NAME);
        AddComponent(MPPlayerController.class, MPPlayerController.COMPONENT_NAME);
        WeaponComponent weapon = new WeaponComponent(weaponName, damage, cost);
        AddComponent(weapon, WeaponComponent.COMPONENT_NAME);
        PlayerAccount thisAccount = new PlayerAccount();
    }
}
