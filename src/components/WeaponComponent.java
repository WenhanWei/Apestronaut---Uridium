package components;

import debugging.Debug;
import GameObjects.*;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Weapon component, determining which weapons the player can buy according to the player's currency earned through picking up coins in the game.
 *
 */

public class WeaponComponent extends Component {

    public static String COMPONENT_NAME = "WeaponComponent";
    String weaponName;
    /**
     * The new damage value for the player if they have this weapon, must be in the range 1 to 3
     * Currently set to the default damage value for a player without weapons
     */
    int damagePower = 1;
    /**
     * The cost (in coins/currency) to the player if they want to own this weapon
     * Currently set to 0 to assume the player has no weapons
     */
    int cost = 0;
    /**
     * Determines whether the player has the weapon (this attribute might not be needed);
     */
    boolean owned = true;
    /**
     * ArrayList of additional boosters that come with the weapon; initially empty
     */
    Collection<BaseBooster> boosters = new ArrayList<BaseBooster>();
    /**
     * Type of additional boosters that the weapon may have
     */
    BaseBooster boosterType;
    /**
     * Number of additional boosters the weapon may have
     */
    int boosterCount = 0;


    /**
     * A default weapon component ie the player has no weapon
     */
    public WeaponComponent() {
        weaponName = "none";
        owned = false;
        damagePower = 1;
    }

    public WeaponComponent(String weaponName, int damage, int cost) {
        this.weaponName = weaponName;
        if (damage >= 1 && damage <= 3){
            damagePower = damage;
        } else {
            Debug.Warning("damage out of range");
            damagePower = 1;
        }
        this.cost = cost;
        owned = true;
    }

    public WeaponComponent(String weaponName, int cost, BaseBooster booster) {
        this.weaponName = weaponName;
        this.cost = cost;
        this.boosterType = booster;
        owned = true;
    }

    public WeaponComponent(String weaponName, BaseBooster booster, int boosterCount, int cost) {
        this.weaponName = weaponName;
        this.boosterCount = boosterCount;
        this.cost = cost;
        owned = true;
        int i = 0;
        while (i < boosterCount) {
            boosters.add(booster);
            i++;
        }
      //  Debug.Log("weapon boosters array list size: " + boosters.size());
    }

    @Override
    public void ResolveXML(Element element) {
        if (ResolveData(element, "weaponName") != null) {
            this.weaponName = ResolveData(element, "weaponName");
        }
        if (ResolveData(element, "cost") != null) {
            this.cost = Integer.parseInt(ResolveData(element, "cost"));
        }
        if (ResolveData(element, "damage") != null) {
            this.damagePower = Integer.parseInt(ResolveData(element, "damage"));
        }
        if (ResolveData(element, "boosterCount") != null) {
            this.boosterCount = Integer.parseInt(ResolveData(element, "boosterCount"));
        }

        if (ResolveData(element, "boosterType") != null) {
            String boosterObject = ResolveData(element, "boosterType");
            int i = 0;
            if (boosterObject.equals("DamageBooster")) {
                this.boosterType = new DamageBooster();
                while (i < this.boosterCount) {
                    boosters.add(this.boosterType);
                    i++;
                }
            } else if (boosterObject.equals("JumpBooster")) {
                this.boosterType = new JumpBooster();
                while (i < this.boosterCount) {
                    boosters.add(this.boosterType);
                    i++;
                }
            } else if (boosterObject.equals("SpeedBooster")) {
                this.boosterType = new SpeedBooster();
                while (i < this.boosterCount) {
                    boosters.add(this.boosterType);
                    i++;
                }
            } else if (boosterObject.equals("StunBooster")) {
                this.boosterType = new StunBooster();
                while (i < this.boosterCount) {
                    boosters.add(this.boosterType);
                    i++;
                }
            } else {
                this.boosterType = new BaseBooster();
                while (i < this.boosterCount) {
                    boosters.add(this.boosterType);
                    i++;
                }
            }

        }
    }


}
