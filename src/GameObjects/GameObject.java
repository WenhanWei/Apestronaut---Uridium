package GameObjects;

import GameDatabase.PlayerAccount;
import components.Component;
import components.Graphics.GraphicsComponent;
import components.Transform;
import customClasses.Vector2;
import debugging.CommandListener;
import debugging.Console;
import debugging.Debug;
import debugging.ConsoleController.Commands;

import java.util.concurrent.ConcurrentHashMap;

/**
 * The base class for all GameObjects
 */
public class GameObject implements CommandListener {

	boolean isInEditor = false;
	/**
	 * The child components attached to this GameObject - <COMPONENT_NAME,
	 * Component>
	 */

	public ConcurrentHashMap<String, Component> components = new ConcurrentHashMap<String, Component>();
	/**
	 * Is this GameObject active for updating
	 */
	private boolean isActive = true;

	public double ID;

	public PlayerAccount thisAccount = new PlayerAccount();

	/**
	 * Adds a Transform with default values
	 */
	public GameObject() {
		Console.AddListener(this);
		AddComponent(Transform.class, Transform.COMPONENT_NAME);
	}

	/**
	 * Adds a transform at a defined position
	 * 
	 * @param pos the location of the transform to be added
	 */
	public GameObject(Vector2 pos) {
		Console.AddListener(this);
		AddComponent(Transform.class, Transform.COMPONENT_NAME).pos = pos;
	}

	/**
	 * Get the Transform component
	 * 
	 * @return the transform component
	 */
	public Transform GetTransform() {
		return GetComponent(Transform.COMPONENT_NAME);
	}

	public void setIsActive(boolean active) {
		if (active != this.isActive) {
			this.isActive = active;
			for (Component component : components.values()) {
				if (getIsActive()) {
					component.Enable();
				} else {
					component.Disable();
				}
			}
		}
	}

	public void setIsInEditor(boolean inEditor) {
		if (inEditor != isInEditor) {
			isInEditor = inEditor;
			for (Component component : components.values()) {
				if (isInEditor && !(component instanceof GraphicsComponent)) {
					component.Disable();
				} else {
					component.Enable();
				}
			}
		}
	}

	public boolean getIsActive() {
		return this.isActive;
	}

	/**
	 * Retrieve a child component from the GameObject
	 * 
	 * @param <T> The type of component
	 * @param cls The class of the componnt i.e. Tranform.class
	 * @return The child component, if it exists, or null
	 */
	public <T extends Component> T GetComponent(Class<T> cls) {
		for (Object c : components.values().toArray()) {
			if (cls.isInstance(c)) {
				return (T) c;
			}
		}
		return null;
	}

	/**
	 * Retrieve a child component from the GameObject
	 * 
	 * @param <T>           The type of the component
	 * @param componentName The name of the component type
	 * @return The child component, if it exists, or null
	 */
	public <T extends Component> T GetComponent(String componentName) {
		int setSize = components.keySet().size();
		String componentKeys[] = new String[setSize];
		componentKeys = components.keySet().toArray(componentKeys);
		if (componentName != "") {
			for (int i = 0; i < componentKeys.length; i++) {
				if (componentKeys[i].toUpperCase().equals(componentName.toUpperCase())) {
					return (T) components.get(componentKeys[i]);
				}
			}
			Debug.Warning(
					"Trying to access component " + componentName + " when no component is on " + this.toString());
		} else {
			Debug.Warning("No component name for " + componentName);
		}
		return null;
	}

	/**
	 * Add a new component to the GameObject
	 * 
	 * @param <T>           The type of component
	 * @param cls           The class of the componnt i.e. Tranform.class
	 * @param componentName The name of the component type
	 * @return The component that has been added to the GameObject
	 */
	public <T extends Component> T AddComponent(Class<T> cls, String componentName) {
		try {
			T component = cls.newInstance();
			components.put(componentName, component);
			component.Start(this);
			return component;
		} catch (InstantiationException | IllegalAccessException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Add a pre-instantiated component to the GameObject
	 * 
	 * @param <T>           The type of component
	 * @param component     The component to be added
	 * @param componentName The name of the component type
	 * @return The component that has been added to the GameObject
	 */
	public <T extends Component> T AddComponent(T component, String componentName) {
		components.put(componentName, component);
		component.Start(this);
		return component;
	}

	public <T extends Component> T RemoveComponent(T component) {
		if (components.containsValue(component)) {
			//return (T) components.remove(component);
			component.Remove();
			return (T) component;

		}
		return null;
	}

	public void Remove() {
		Console.RemoveListener(this);
		for (Component component : components.values()) {
			component.Remove();
		}

	}

	/**
	 * Updates all child components on the GameObject
	 */
	public void Update() {
		if (isActive) {
			for (Component component : components.values()) {
				if (component.isActive) {
					component.Update();
				}
			}

		//	}
		}
	}

	@Override
	public void CommandRecieved(String[] command) {
		if (Commands.LIST_GAME_OBJECTS.equals(command[0])) {
			// Debug.Log(this.toString().split("@", 0)[0] + " : " + ID + " - "
			// + (this.isActive ? "active" : "inactive"));
			Debug.Log(this.toString() + " - " + (this.isActive ? "active" : "inactive"));
		}
		if (command.length > 1) {
			if (CheckCommandIsForMe(command)) {
				if (Commands.LIST_GAME_OBJECT_COMPONENTS.equals(command[0])) {
					ListGameObjectComponents(command);
				}
				if (Commands.GET_TRANSFORM.equals(command[0])) {
					GetTransform(command);
				}
				if (Commands.SET_TRANSFORM.equals(command[0])) {
					SetTransform(command);
				}
				if (Commands.SET_OBJECT_ENABLED.equals(command[0])) {
					if (command[1].equals("TRUE") || command[1].equals("T")) {
						this.setIsActive(true);
					}
					if (command[1].equals("FALSE") || command[1].equals("F")) {
						this.setIsActive(false);
					}
				}
				if (Commands.SET_COMPONENT_ENABLED.equals(command[0])) {
					if (command.length > 3) {
						if (GetComponent(command[2]) != null) {
							if (command[3].equals("TRUE") || command[3].equals("T")) {
								GetComponent(command[2]).Enable();
							}
							if (command[3].equals("FALSE") || command[3].equals("F")) {
								GetComponent(command[2]).Disable();
							}
						}
					}
				}
			}
		}
	}

	@Override
	public String toString() {
		return (this.getClass().getSimpleName() + " : " + ID);
	}

	private boolean CheckCommandIsForMe(String[] command) {
		return (Integer.parseInt(command[1]) == ID);
	}

	private void ListGameObjectComponents(String[] command) {
		Debug.Log("Components for " + this.toString());
		for (String name : components.keySet()) {
			Debug.Log(name + " - " + (components.get(name).isActive ? "active" : "inactive"));
		}
	}

	private void GetTransform(String[] command) {
		Debug.Log(this.toString() + " - transform: " + GetTransform().pos.toString());
	}

	private void SetTransform(String[] command) {
		if (command.length > 3) {
			try {
				float locX = Float.parseFloat(command[2]);
				float locY = Float.parseFloat(command[3]);
				GetTransform().pos = new Vector2(locX, locY);
			} catch (Exception e) {

			}
		}
	}

}
