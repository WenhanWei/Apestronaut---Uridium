package debugging;

import GameObjects.GameObject;
import physics.Ray;

/**
 * A custom debug class
 */
public class Debug extends GameObject {

	public static GraphicalDebugger_Ray graphicalDebugger_Ray = new GraphicalDebugger_Ray();

	public Debug() {
		super();
		AddComponent(graphicalDebugger_Ray, GraphicalDebugger_Ray.COMPONENT_NAME);
	}

	public static void DrawRay(Ray ray) {
		graphicalDebugger_Ray.rays.add(new Ray(ray.origin, ray.direction));
	}

	/**
	 * Prints no modifiers
	 * 
	 * @param msg the text to be displayed
	 */
	public static void Log(String msg) {
		System.out.println(msg);
		Console.logMessages.add(msg);
	}

	/**
	 * Prints with a prepended [Warning] modifier
	 * 
	 * @param msg the text to be displayed
	 */
	public static void Warning(String msg) {
		System.out.println("[Warning] " + msg);
		Console.logMessages.add("[Warning] " + msg);
	}

	/**
	 * Prints with a prepended [Error] modifier
	 * 
	 * @param msg the text to be displayed
	 */
	public static void Error(String msg) {
		System.out.println("[Error] " + msg);
		Console.logMessages.add("[Error] " + msg);
	}

}
