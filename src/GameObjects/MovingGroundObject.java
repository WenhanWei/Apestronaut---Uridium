package GameObjects;

import components.CharacterController;
import components.Graphics.GraphicsComponent_Tileable;
import components.MovingGroundController;
import components.StrengthComponent;
import components.Colliders.BoxCollider;
import components.Colliders.Collider;
import components.Graphics.GraphicsComponent_Square;
import customClasses.Vector2;
import java.awt.Color;

/**
 * Platforms that can move horizontally in the game
 */
public class MovingGroundObject extends GameObject {

    Vector2 size = new Vector2(200,32);

    public MovingGroundObject() {
        super(new Vector2(500, 100));
        AddComponent(new GraphicsComponent_Tileable(size,
                        "src/resources/sprites/Space Suit Fighter Assets Pack/platform_moving.png",
                        0),
                GraphicsComponent_Tileable.COMPONENT_NAME);
        AddComponent(MovingGroundController.class, MovingGroundController.COMPONENT_NAME);
        AddComponent(CharacterController.class, CharacterController.COMPONENT_NAME);
        BoxCollider collider = new BoxCollider("MovingGroundObject", size);
        AddComponent(collider, Collider.COMPONENT_NAME);
        AddComponent(StrengthComponent.class, StrengthComponent.COMPONENT_NAME);
    }

    public MovingGroundObject(int strength) {
        this();
        StrengthComponent groundStrength = new StrengthComponent(strength);
        AddComponent(groundStrength, StrengthComponent.COMPONENT_NAME);
    }

    public MovingGroundObject(Vector2 pos, Vector2 size) {
        super(pos);
        AddComponent(new GraphicsComponent_Square(size, Color.black, 0), GraphicsComponent_Square.COMPONENT_NAME);
        AddComponent(StrengthComponent.class, StrengthComponent.COMPONENT_NAME);
        AddComponent(MovingGroundController.class, MovingGroundController.COMPONENT_NAME);
        AddComponent(CharacterController.class, CharacterController.COMPONENT_NAME);
        BoxCollider collider = new BoxCollider("MovingGroundObject", size);
        AddComponent(collider, Collider.COMPONENT_NAME);
    }

    public MovingGroundObject(Vector2 pos, Vector2 size, int strength) {
        this(pos, size);
        StrengthComponent groundStrength = new StrengthComponent(strength);
        AddComponent(groundStrength, StrengthComponent.COMPONENT_NAME);
    }

    @Override
    public String toString() {
        return "If you land on this other worldly surface, you can escape your enemies and travel through dimensions.";
    }

}
