package components;

import components.Colliders.BoxCollider;
import customClasses.Vector2;
import GameObjects.GroundObject;
import logic.Time;
import managers.Window;

/**
 * A controller for meteors, which are falling game objects
 */

public class MeteorController extends PlayerController {

    public static String COMPONENT_NAME = "MeterController";
    final float INITIAL_SPEED = 0.000002f;
    final float AIR_ACCELERATION = 0.002f;
    final float gravity = 0.05f;

    @Override
    public void Update() {
        setMaxSpeed(INITIAL_SPEED);
        if (vel.x < 0.05f && vel.x > -0.05f)
            vel.x = 0;

        if (vel.y < 0) {
            if (gameObject.GetComponent(BoxCollider.class).CheckDirection(groundLayerMask, Vector2.up()) < groundDist)
                vel.y = 0;
            isGrounded = false;
        } else {
            isGrounded = gameObject.GetComponent(BoxCollider.class).CheckDirection(groundLayerMask,
                    Vector2.down()) < groundDist;
        }

        if (!isGrounded) {
            acceleration = AIR_ACCELERATION;
            vel.y += gravity * Time.deltaTime;
        } else {
            acceleration = GROUND_ACCELERATION; // meteors stop falling when they hit the ground
            vel.y = 0;
        }

        if (isGrounded) {
            gameObject.GetTransform().pos = new Vector2(gameObject.GetTransform().origPos.x, gameObject.GetTransform().origPos.y);
        }

        if (vel.x != 0 || vel.y != 0)
            gameObject.GetComponent(CharacterController.class).Move(vel.Mult(speedMultiplier * Time.deltaTime));

        if (center) {
            Window.xLoc = (int) (-gameObject.GetTransform().pos.x - gameObject.GetComponent(BoxCollider.class).size.x);
            Window.yLoc = (int) (-gameObject.GetTransform().pos.y - gameObject.GetComponent(BoxCollider.class).size.y);
        }


    }

}
