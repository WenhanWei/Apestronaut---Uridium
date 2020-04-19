package GameObjects;

import components.CharacterController;
import components.Colliders.BoxCollider;
import components.Colliders.Collider;
import components.Graphics.GraphicsComponent_Image;
import components.MeteorController;
import customClasses.Vector2;

/**
 * Falling meteors in the game space
 * If a player collides with a meteor, they become frozen
 */
public class Meteor extends GameObject {
    final int WIDTH = 30;
    final int HEIGHT = 62;

    /**
     * Determines whether the meteor was part of the original game scene (true) or was added by a weapon (false)
     */
    boolean initialScene = true;

    public Meteor() {
        super(new Vector2(0, 0));
        AddComponent(new GraphicsComponent_Image(new Vector2(WIDTH, HEIGHT),
                        "src/resources/sprites/Space Suit Fighter Assets Pack/meteor.png",
                        -1),
                GraphicsComponent_Image.COMPONENT_NAME);
        BoxCollider collider = new BoxCollider("Meteor", new Vector2(WIDTH, HEIGHT));
        AddComponent(collider, Collider.COMPONENT_NAME);
        AddComponent(CharacterController.class, CharacterController.COMPONENT_NAME);
        AddComponent(MeteorController.class, MeteorController.COMPONENT_NAME);
        initialScene = true;
    }

    public Meteor(Vector2 size, boolean initialScene) {
        super(new Vector2(0, 0));
        AddComponent(new GraphicsComponent_Image(new Vector2(WIDTH, HEIGHT),
                        "src/resources/sprites/Space Suit Fighter Assets Pack/meteor.png",
                        -1),
                GraphicsComponent_Image.COMPONENT_NAME);
        BoxCollider collider = new BoxCollider("Meteor", size);
        AddComponent(collider, Collider.COMPONENT_NAME);
        AddComponent(CharacterController.class, CharacterController.COMPONENT_NAME);
        AddComponent(MeteorController.class, MeteorController.COMPONENT_NAME);
        this.initialScene = initialScene;
    }

    public Meteor(Vector2 size, Vector2 pos, boolean initialScene) {
        super(pos);
        AddComponent(new GraphicsComponent_Image(new Vector2(WIDTH, HEIGHT),
                        "src/resources/sprites/Space Suit Fighter Assets Pack/meteor.png",
                        -1),
                GraphicsComponent_Image.COMPONENT_NAME);
        BoxCollider collider = new BoxCollider("Meteor", size);
        AddComponent(collider, Collider.COMPONENT_NAME);
        AddComponent(CharacterController.class, CharacterController.COMPONENT_NAME);
        AddComponent(MeteorController.class, MeteorController.COMPONENT_NAME);
        this.initialScene = initialScene;
    }

    @Override
    public String toString() {
        return "There are many dangers you'll encounter in this galaxy, like these meteors. Be careful - if you collide with one, you'll be temporarily frozen.";
    }
}
