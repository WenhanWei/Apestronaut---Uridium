package physics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import components.Colliders.Collider;

/**
 * The manager for all colliders
 */
public class ColliderManager {

	public static HashMap<String, List<Collider>> colliders = new HashMap<String, List<Collider>>();

	public static void AddBoundary(String layer, Collider collider) {
		if (colliders.containsKey(layer) == false) {
			colliders.put(layer, new ArrayList<Collider>());
		}
		colliders.get(layer).add(collider);
	}

	public static void RemoveBounday(String layer, Collider collider) {
		if (colliders.containsKey(layer)) {
			colliders.get(layer).remove(collider);
		}
	}
}
