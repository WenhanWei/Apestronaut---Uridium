package components;

import org.w3c.dom.Element;

import debugging.CommandListener;
import debugging.Console;
import GameObjects.GameObject;

/**
 * The base class of all components
 */
public abstract class Component implements CommandListener {

	/**
	 * The name of the component type - can be used to access the component from a
	 * GameObject
	 */
	public static String COMPONENT_NAME = "Component";

	/**
	 * The parent GameObject to which this component is attached
	 */
	public GameObject gameObject;
	/**
	 * Defines whether the component should be updated or not
	 */
	public boolean isActive;

	/**
	 * Runs when the component is added to a GameObject
	 */
	public void Start(GameObject parent) {
		gameObject = parent;
		if (COMPONENT_NAME == "Component")
			COMPONENT_NAME = this.getClass().getSimpleName();
		Start();
	}

	/**
	 * Runs when the components starts - can be separate to it being added to a
	 * GameObject
	 */
	public void Start() {
		this.Enable();
	}

	public void Remove() {
		this.Disable();
	}

	/**
	 * Convert XML encoding of scene to class data
	 */
	public void ResolveXML(Element element) {

	}

	/**
	 * Convert XML encoding of scene to class data
	 */
	protected String ResolveData(Element element, String flag) {
		if (element.getElementsByTagName(flag).getLength() > 0)
			return element.getElementsByTagName(flag).item(0).getTextContent();
		return null;
	}

	/**
	 * Runs every tick to update component values
	 */
	public void Update() {

	}

	/**
	 * Runs to enable the component
	 */
	public void Enable() {
		if (!isActive) {
			isActive = true;
			Console.AddListener(this);
			OnEnable();
		}
	}

	/**
	 * Runs to disable the component
	 */
	public void Disable() {
		if (isActive) {
			isActive = false;
			Console.RemoveListener(this);
			OnDisable();
		}
	}

	/**
	 * Runs when the component is enabled
	 */
	protected void OnEnable() {

	}

	/**
	 * Runs when the component is disabled
	 */
	public void OnDisable() {

	}

	@Override
	public void CommandRecieved(String[] command) {

	}
}
