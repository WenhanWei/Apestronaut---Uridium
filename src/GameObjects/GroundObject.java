package GameObjects;

import java.awt.Color;

import components.Graphics.GraphicsComponent_Tileable;
import components.StrengthComponent;
import components.Colliders.BoxCollider;
import components.Colliders.Collider;
import components.Graphics.GraphicsComponent_Square;
import customClasses.Vector2;
import debugging.Debug;
import managers.Engine;

/**
 * A base GameObject representing the Ground (excludes moving ground)
 */
public class GroundObject extends GameObject {

	Vector2 size = new Vector2(250,32);
	 
	public GroundObject() {
		super(new Vector2(500, 100));
		AddComponent(new GraphicsComponent_Tileable(size,
						"src/resources/sprites/Space Suit Fighter Assets Pack/platform_regular.png",
						0),
				GraphicsComponent_Tileable.COMPONENT_NAME);
		BoxCollider collider = new BoxCollider("Ground", size);
		AddComponent(collider, Collider.COMPONENT_NAME);
		AddComponent(StrengthComponent.class, StrengthComponent.COMPONENT_NAME);

	}
	

	public GroundObject(Vector2 pos, Vector2 size) {
		super(pos);
		AddComponent(new GraphicsComponent_Tileable(size,
						"src/resources/sprites/Space Suit Fighter Assets Pack/platform_regular.png",
						0),
				GraphicsComponent_Tileable.COMPONENT_NAME);
		BoxCollider collider = new BoxCollider("Ground", size);
		
		AddComponent(collider, Collider.COMPONENT_NAME);
		AddComponent(StrengthComponent.class, StrengthComponent.COMPONENT_NAME);
	}

	/**
	 * Breaks a ground object if the strength of the ground object is less than 0. The new ground object will be initialised with the default strength value
	 * A ground object disappears if it's less than the width of a player, rather than being broken further
	 * 
	 * @return true if the ground has broken successfully
	 */
	public boolean splitGround() {
	int currentStrength = this.GetComponent(StrengthComponent.class).getStrength();
	if (currentStrength < 0) {	
        if (this != null) {
            Engine.Instance.RemoveObject(this);
		}
		Vector2 splitSize = new Vector2(this.GetComponent(BoxCollider.class).size.x/3, this.GetComponent(BoxCollider.class).size.y);
		float splitDistance = (this.GetComponent(BoxCollider.class).size.x)/3;
		if (this.GetComponent(BoxCollider.class).size.x <= 50) {
			return true;
		}

		GroundObject split1 = new GroundObject(this.GetTransform().pos, splitSize);
		GroundObject split2 = new GroundObject(new Vector2((this.GetTransform().pos.x - 5) + splitDistance, this.GetTransform().pos.y), splitSize);
		Engine.Instance.AddObject(split1);
		Engine.Instance.AddObject(split2);
		return true;

	}
	return false;
    }

	

}
