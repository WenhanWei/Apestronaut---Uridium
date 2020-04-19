package GameObjects;

import components.Colliders.Collider;
import components.PlayerController;
import components.WeaponComponent;
import customClasses.Vector2;
import managers.Engine;

/**
 * Weapon that releases meteors into the game when it's collected
 */
public class ObstacleWeapon extends BaseBooster {

    Vector2 meteorSize = new Vector2(30, 40);

    public ObstacleWeapon() {
        super("src/resources/sprites/booster/obstacle_weapon.png","ObstacleWeapon", 30);
        WeaponComponent obstacleWeapon = new WeaponComponent("ObstacleWeapon", 3, 0);
        AddComponent(obstacleWeapon, WeaponComponent.COMPONENT_NAME);
    }

    // Adds the meteors to the game such that they surround the nearest opponent player
    @Override
    public boolean useBooster(GameObject gameObject) {
        float meteorXPos1 = gameObject.GetTransform().pos.x - 300;
        float meteorXPos2 = gameObject.GetTransform().pos.x + 300;
        Meteor meteor1 = new Meteor(meteorSize, new Vector2(meteorXPos1, 50), false);
        meteor1.GetTransform().origPos = new Vector2(meteorXPos1, 50);
        Meteor meteor2 = new Meteor(meteorSize, new Vector2(meteorXPos2, 50), false);
        meteor2.GetTransform().origPos =  new Vector2(meteorXPos2, 50);
        Engine.Instance.AddObject(meteor1);
        Engine.Instance.AddObject(meteor2);
        String[] playerLayerMask = { "Player" };
        GameObject opponent = null;
        attackVec = new Vector2(attackDist * gameObject.GetComponent(PlayerController.class).xDir, 0);
        if (gameObject.GetComponent(Collider.class) != null) {
            opponent = (GameObject) gameObject.GetComponent(Collider.class).GetInDirection(playerLayerMask, attackVec);
            if (opponent != null) {
                float opponentX = opponent.GetTransform().pos.x;
                Vector2 meteorPos = new Vector2(opponentX, 50);
                Meteor meteor3 = new Meteor(meteorSize, meteorPos, false);
                meteor3.GetTransform().origPos = new Vector2(opponentX, 50);
                Engine.Instance.AddObject(meteor3);
                return true;
            }
        }
        return false;
    }


    @Override
    public String toString() {
        return "You've found the obstacles weapon. Prepare for your opponents to be stunned when meteors start appearing.";

    }
}
