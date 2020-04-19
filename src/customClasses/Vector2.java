package customClasses;

/**
 * A class defining 2D vectors
 */
public class Vector2 {

	public static Vector2 zero() {
		return new Vector2(0, 0);
	}

	public static Vector2 up() {
		return new Vector2(0, -1);
	}

	public static Vector2 down() {
		return new Vector2(0, 1);
	}

	public static Vector2 left() {
		return new Vector2(-1, 0);
	}

	public static Vector2 right() {
		return new Vector2(1, 0);
	}
	/**
	 * Returns the distance between two vectors
	 */
	public static float distance(Vector2 v1, Vector2 v2) {
		return v1.Minus(v2).length();
	}

	public float x;
	public float y;

	public float length() {
		return (float) Math.sqrt((x * x) + (y * y));
	}

	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return ("Vector2 (" + x + "," + y + ")");
	}

	public Vector2 Add(Vector2 vec2) {
		return new Vector2(x + vec2.x, y + vec2.y);
	}

	public Vector2 Minus(Vector2 vec2) {
		return new Vector2(x - vec2.x, y - vec2.y);
	}

	public Vector2 Mult(float mult) {
		return new Vector2(x * mult, y * mult);
	}

	/**
	 * @return the current Vector2 scaled to have length 1
	 */
	public Vector2 Normalize() {
		return (this.Mult(1 / this.length()));
	}

	/**
	 * @return a vector perpendicular to the current Vector2
	 */
	public Vector2 Perp() {
		return new Vector2(y, -x);
	}
	
	// added by Lauren
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}

}
