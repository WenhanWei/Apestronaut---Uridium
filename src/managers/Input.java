package managers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * A class for input management
 */
public class Input implements KeyListener, MouseListener {

	public static Input Instance;

	public static Object controller = null;
	private static boolean keyPress[] = new boolean[65536];
	private static boolean keyRelease[] = new boolean[65536];
	private static boolean keys[] = new boolean[65536];

	private static boolean mousePress[] = new boolean[65536];
	private static boolean mouse[] = new boolean[65536];

	public static boolean flag; // indicates whether the key was pressed or not (used within the controls settings)
	public static String key;
	public static int keyEvent;

	/**
	 * Retrieve if a key was pressed once (resets after detection) - only to be used
	 * by controller
	 * 
	 * @param e The key code to check
	 * @return Whether the key has been pressed
	 */
	public static boolean KeyDown(int e, Object requester) {
		if (controller != null && requester.equals(controller)) {
			if (keyPress[e]) {
				keyPress[e] = false;
				return true;
			}
		}
		return false;
	}

	/**
	 * Retrieve if a key is being held - only to be used by controller
	 * 
	 * @param e The key code to check
	 * @return Whether the key is being held
	 */
	public static boolean KeyHold(int e, Object requester) {
		if (controller == null || requester.equals(controller) == false)
			return false;
		return keys[e];
	}

	/**
	 * Retrieve if a key was pressed once (resets after detection) - only to be used
	 * by controller
	 * 
	 * @param e The key code to check
	 * @return Whether the key has been pressed
	 */
	public static boolean KeyDown(int e) {
		if (controller == null) {
			if (keyPress[e]) {
				keyPress[e] = false;
				return true;
			}
		}
		return false;
	}

	/**
	 * Retrieve if a key was released (resets after detection) - only to be used
	 * by controller
	 *
	 * @param e The key code to check
	 * @return Whether the key has been pressed
	 */
	public static boolean KeyUp(int e) {
		if (controller == null) {
			if (keyRelease[e]) {
				keyRelease[e] = false;
				return true;
			}
		}
		return false;
	}

	/**
	 * Retrieve if a key is being held
	 * 
	 * @param e The key code to check
	 * @return Whether the key is being held
	 */
	public static boolean KeyHold(int e) {
		if (controller != null)
			return false;
		return keys[e];
	}

	public Input() {
		Instance = this;
	}

	/**
	 * Unused
	 */
	@Override
	public void keyTyped(KeyEvent e) {

	}

	/**
	 * DO NOT CALL unless you wish to artificially simualte key presses. Logs a key
	 * as pressed
	 * 
	 * @param e The key to log as being pressed
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (!keys[e.getKeyCode()]) {
			keyPress[e.getKeyCode()] = true;
			keys[e.getKeyCode()] = true;
			if (LabelListener.flag) { // update the key & keyEvent so that the control can be updated on screen
				keyEvent = e.getKeyCode();
				key = KeyEvent.getKeyText(e.getKeyCode()).toUpperCase();
				flag = true;
			}
		}
	}

	/**
	 * DO NOT CALL unless you wish to artificially simualte key presses. Logs a key
	 * as released
	 * 
	 * @param e The key to log as being released
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		keyPress[e.getKeyCode()] = false;
		keyRelease[e.getKeyCode()] = true;
		keys[e.getKeyCode()] = false;
	}

	/*
	 * The following is the mouse listener component
	 */

	public static boolean MouseDown (int e) {
		if (controller == null) {
			if (mousePress[e]) {
				mousePress[e] = false;
				return true;
			}
		}
		return false;
	}

	public static boolean MouseHold (int e) {
		if (controller != null)
			return false;
		return mouse[e];
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!mouse[e.getButton()]) {
			mousePress[e.getButton()] = true;
			mouse[e.getButton()] = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mousePress[e.getButton()] = false;
		mouse[e.getButton()] = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

}
