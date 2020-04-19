package GameObjects;

import components.PlayerController;
import components.WeaponComponent;

/**
 * The invincible weapon
 * If the player has this weapon, immediately after they pick it up they will not be defeated by obstacles or any opponent action for a set number of ticks
 */
public class InvincibleWeapon extends BaseBooster {

        public InvincibleWeapon() {
            super("src/resources/sprites/boosters/invincible.png","InvincibleWeapon", 15);
            WeaponComponent invincible = new WeaponComponent("InvincibleWeapon", 2, 0);
            AddComponent(invincible, WeaponComponent.COMPONENT_NAME);
        }

        @Override
        public boolean useBooster(GameObject gameObject) {
          gameObject.GetComponent(PlayerController.class).invincible = true;
          return true;
        }

        @Override
        public String toString() {
            return "Look at you go - you've found the weapon of invincibility. Nothing can defeat you now, you're unstoppable.";
        }
    }


