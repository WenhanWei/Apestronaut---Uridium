package components;

import debugging.Debug;
import org.w3c.dom.Element;

/**
 * The component which determines the value of a coin in the game
 */
public class MoneyComponent extends Component {
    public static String COMPONENT_NAME = "MoneyComponent";
    public int value;

    public MoneyComponent() {
        this.value = 100;
    }

    public MoneyComponent(int value) {
        this.value = value;
    }

    @Override
    public void ResolveXML(Element element) {
        if (ResolveData(element, "value") != null) {
            this.value = Integer.parseInt(ResolveData(element, "value"));
        }
    }

}
