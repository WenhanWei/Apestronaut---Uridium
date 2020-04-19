package GameObjects;

import java.awt.image.BufferedImage;

import components.*;
import components.Colliders.BoxCollider;
import components.Colliders.Collider;
import components.Graphics.*;
import customClasses.Vector2;

/**
 * @author Constantin Onofras
 * @date 2020/03/30 - 2020
 * Based on PlayerObject and AIPlayerObject
 */
public class MPOpponentObject4 extends GameObject {

    final int WIDTH = 74, HEIGHT = 74;

    BufferedImage defaultSprite = GraphicsComponent.imageFromPath("src/resources/sprites/player/ai_monkey.png");
    BufferedImage crouchSprite = GraphicsComponent.imageFromPath("src/resources/sprites/player/ai_monkey_c.png");
    BufferedImage holdingBatSprite = GraphicsComponent.imageFromPath("src/resources/sprites/player/ai_monkey_bat.png");

    Animation kickAnimation = new Animation(new String[] {"src/resources/sprites/player/ai_monkey_kick_0.png",
            "src/resources/sprites/player/ai_monkey_kick.png"}, new float[] { 5f, 15f });
    public Animation batSwingAnimation = new Animation(new String[] {"src/resources/sprites/player/ai_monkey_bat_hit_0.png",
            "src/resources/sprites/player/ai_monkey_bat_hit_1.png",
            "src/resources/sprites/player/ai_monkey_bat_hit_2.png",
            "src/resources/sprites/player/ai_monkey_bat_hit_3.png",
            "src/resources/sprites/player/ai_monkey_bat_hit_4.png",}, new float[] { 2f, 2f, 2f, 3f, 25f });

//    final int WIDTH = 74, HEIGHT = 74;
 //Check this
    public MPOpponentObject4() {
        super(new Vector2(0, 0));
        PlayerObject.players.add(this);
        AddComponent(
                new GraphicsComponent_Image(new Vector2(WIDTH, HEIGHT),
                        "src/resources/sprites/player/ai_monkey.png",
                        -1),
                GraphicsComponent_Image.COMPONENT_NAME);
        BoxCollider collider = new BoxCollider("Player", new Vector2(WIDTH, HEIGHT));
        AddComponent(collider, Collider.COMPONENT_NAME);
        AddComponent(CharacterController.class, CharacterController.COMPONENT_NAME);
        MPOpponentController4Players controller =  AddComponent(MPOpponentController4Players.class, MPOpponentController4Players.COMPONENT_NAME);
        controller.defaultSprite = defaultSprite;
        controller.crouchSprite = crouchSprite;
        controller.holdingBatSprite = holdingBatSprite;
        controller.kickAnimation = kickAnimation;
        controller.batSwingAnimation = batSwingAnimation;
    }

}
