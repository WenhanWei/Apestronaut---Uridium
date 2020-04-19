package components;

import org.w3c.dom.Element;

/**
 * The strength component for a GroundObject or MovingGroundObject
 * This determines how strong the ground object is: for example, 10 being very strong and won't break easily, 0 being very weak and will break when a player lands on it	
 */
public class StrengthComponent extends Component {
    public static String COMPONENT_NAME = "StrengthComponent";
    int strength;

    public StrengthComponent() {
        this.strength = 10000000; // ensures the ground is very unlikely to break
    }
    
    public StrengthComponent(int strength) {
        this.strength = strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getStrength() {
        return this.strength;
    }

    @Override
	public void ResolveXML(Element element) {
		if (ResolveData(element, "strength") != null) {
            this.strength = Integer.parseInt(ResolveData(element, "strength"));
        }
	}

}