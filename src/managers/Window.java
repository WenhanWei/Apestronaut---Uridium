package managers;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.geom.AffineTransform;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.imageio.ImageIO;
import javax.swing.*;
import components.Graphics.GraphicsComponent;
import components.Graphics.Layer;
import components.Network.Command;
import components.Network.UDPSend;
import customClasses.InputKeys;
import customClasses.Vector2;
import debugging.Debug;
import logic.Time;

/**
 * The main class responsible for graphical managing
 */
public class Window extends Canvas implements Runnable {

	public static Window Instance;
	public static JFrame frame = null;
	public static final int WINDOW_WIDTH = 1920;
	public static final int WINDOW_HEIGHT = 1080;

	public static int xLoc = WINDOW_WIDTH / 2;
	public static int yLoc = WINDOW_HEIGHT / 2;
	// private static float zoom;
	public static boolean center = true;
	public static boolean showDebug = false;

	public static Color healthBarColour = Color.GREEN;
	public static boolean fullscreenFlag = true;

	public static int health;
	public static int totalScore;
	public static int currency;
	public static String booster;

	/* Rendering Loop */
	public static boolean running = true;
	long lastFrameTime = System.nanoTime();
	final long FPS_TARGET = 120;
	final long OPTIMAL_FRAME_LENGTH = 1000000000 / FPS_TARGET;
	int fps;
	int lastFps;
	int lastTime;

	static Image bg;
	static Image scoreImage;
	public static Image baseBoost;
	public static Image jumpBoost;
	public static Image speedBoost;
	public static Image stunBoost;
	public static Image damageBoost;

	public static Image aiPlayer;
	public static Image player;
	public static Image ground;
	public static Image movingGround;
	public static Image obstacleWeapon;
	public static Image invincibleWeapon;
	public static Image knockbackBooster;

	public static Date pauseDate;

	/**
	 * All graphical components being painted
	 */
	public HashMap<Layer, List<GraphicsComponent>> graphicsManagers = new HashMap<Layer, List<GraphicsComponent>>();
	public List<GraphicsComponent> graphicsManagersToRemove = new ArrayList<GraphicsComponent>();

	public static void LoadWindow() {
		frame = new JFrame("Engine");

		Instance = new Window();
		Instance.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));

		try {
			bg = ImageIO.read(new File("src/resources/images/bg.png"));
			scoreImage = ImageIO.read(new File("src/resources/sprites/Gold/gold1.png"));
			baseBoost = ImageIO.read(new File("src/resources/sprites/boosters/basebooster.png"));
			jumpBoost = ImageIO.read(new File("src/resources/sprites/boosters/jumpbooster.png"));
			speedBoost = ImageIO.read(new File("src/resources/sprites/boosters/speedbooster.png"));
			stunBoost = ImageIO.read(new File("src/resources/sprites/boosters/stunbooster.png"));
			damageBoost = ImageIO.read(new File("src/resources/sprites/boosters/damagebooster.png"));

			aiPlayer = ImageIO.read(new File("src/resources/sprites/player/ai_monkey.png"));
			player = ImageIO.read(new File("src/resources/sprites/player/player_monkey.png"));
			ground = ImageIO.read(new File("src/resources/sprites/Space Suit Fighter Assets Pack/platform_regular.png"));
			movingGround = ImageIO.read(new File("src/resources/sprites/Space Suit Fighter Assets Pack/platform_moving.png"));
			obstacleWeapon = ImageIO.read(new File("src/resources/sprites/booster/obstacle_weapon.png"));
			invincibleWeapon = ImageIO.read(new File("src/resources/sprites/boosters/invincible.png"));
			knockbackBooster = ImageIO.read(new File("src/resources/sprites/booster/bat.png"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		// zoom = 1;

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIgnoreRepaint(true);
		frame.setResizable(false);
		if (fullscreenFlag) {
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame.setUndecorated(true);
		}

		if (Engine.gamemode == "Level Editor") {
			LevelEditorView levelEditorView = new LevelEditorView();
			frame.add(levelEditorView.getLevelEditorPanel(), BorderLayout.PAGE_END);
		}

		frame.add(Instance);
		frame.pack();
		frame.setVisible(true);

		Instance.setFocusable(true); // allows the user to start the game w/o clicking on the window
		Instance.setFocusTraversalKeysEnabled(true);
		Instance.createBufferStrategy(2);
		Instance.addKeyListener(new Input());
		Instance.addMouseListener(new Input());
	}

	public Window() {
		Debug.Log("Starting Window");
		Thread renderingThread = new Thread(this);
		renderingThread.start();
	}
	
	public static void Center(Vector2 loc) {
		xLoc = (int) (loc.x + WINDOW_WIDTH / 2);
		yLoc = (int) (loc.y + WINDOW_HEIGHT / 2);
	}

	@Override
	public void run() {
		while (running) {
			long now = System.nanoTime();
			long timeSinceLast = now - lastFrameTime;
			lastFrameTime = now;

			fps += 1;
			lastTime += timeSinceLast;

			if (lastTime >= 1000000000) {
				lastTime = 0;
				lastFps = fps;
				fps = 0;
			}

			render();

			try {
				long timeoutValue = (lastFrameTime - System.nanoTime() + OPTIMAL_FRAME_LENGTH) / 1000000;
				if (timeoutValue > 0) {
					Thread.sleep(timeoutValue);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (Input.KeyDown(InputKeys.PAUSE)) {

				if(Engine.gamemode.startsWith("Multi") & Engine.Instance.times_paused <= 3) {
					Time.timeScale = 0f; // freezes the game
					pauseDate = new Date();
					ExecutorService service = Executors.newFixedThreadPool(1);
					DatagramSocket sendSocket = null;
					try {
						sendSocket = new DatagramSocket();
					} catch (Exception ce) {
						ce.printStackTrace();
					}
					Command commandToBeSend = new Command("Pause");
					if (Engine.Instance.players == 4) {
						service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1, commandToBeSend));
						service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(2), Engine.Instance.port2, commandToBeSend));
						service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(3), Engine.Instance.port3, commandToBeSend));
					} else if (Engine.Instance.players == 3) {
						service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1, commandToBeSend));
						service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(2), Engine.Instance.port2, commandToBeSend));
					} else {
						service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1, commandToBeSend));
					}
					Engine.Instance.paused = true;
					PauseView.PauseView().setVisible(true);
					Engine.Instance.times_paused++;
				}
				else if(Engine.gamemode.startsWith("Multi") & Engine.Instance.times_paused > 3){
					PauseView.PauseView().setVisible(true);
					Engine.Instance.times_paused++;
				}
				else {
					Time.timeScale = 0f;
					PauseView.PauseView().setVisible(true);
				}
			}


		}
	}

	public static void generatePracticeInfo() {
		if (Engine.Instance.getGameMode().equals("Practice")) {
			Time.timeScale = 0f; // freezes the game
			PracticeView.PracticeView().setVisible(true);
		}
	}


	public static void Exit() {
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}

	/**
	 * Used to calculate the transform to be applied to the graphics
	 * 
	 * @param dir Whether this is for transformation, or for undoing a
	 *            transformation
	 * @return The AffineTransform object defining the transform to occur
	 */
	private AffineTransform GetTransform(float dir) {
		double width = WINDOW_WIDTH;
		double height = WINDOW_HEIGHT;

		double xAnchor = (-width) / 2;
		double yAnchor = (-height) / 2;

		AffineTransform at = new AffineTransform();
		if (center) {
			at.translate(dir * xAnchor, dir * yAnchor);
			at.translate(dir * xLoc, dir * yLoc);
		}

		return at;
	}

	/**
	 * Perform rendering of graphics component
	 */
	public void render() {
		BufferStrategy buffer = this.getBufferStrategy();

		if (buffer != null) {
			Graphics2D g = (Graphics2D) buffer.getDrawGraphics();
			g.setColor(Color.white);
			g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
			g.drawImage(bg, 0, 0, getWidth(), getHeight(), this); // background image

			g.setColor(Color.WHITE);
			g.setFont(View.font);
			g.drawImage(scoreImage, 880, 1025, 40, 40, this);
			g.drawString(" X " + totalScore, 920, 1055);

			if (booster != null) {
				switch (booster) {
					case "Psychic":
						g.drawString(booster + "", 1660, 35); // draws the current booster that the player has picked up
						g.drawImage(baseBoost, 1600, 5, 45, 45, this);
						break;
					case "Super Leaping":
						g.drawString(booster + "", 1660, 35);
						g.drawImage(jumpBoost, 1600, 5, 45, 45, this);
						break;
					case "Radiant Bolt":
						g.drawString(booster + "", 1660, 35);
						g.drawImage(speedBoost, 1600, 5, 45, 45, this);
						break;
					case "Chrono Fusion":
						g.drawString(booster + "", 1660, 35);
						g.drawImage(stunBoost, 1600, 5, 45, 45, this);
						break;
					case "Berserker":
						g.drawString(booster + "", 1660, 35);
						g.drawImage(damageBoost, 1600, 5, 45, 45, this);
						break;
					default:
						break;
				}
			}

			g.setColor(Color.BLACK);
			g.fillRect(835, 5, 250, 50);
			g.setColor(healthBarColour);
			g.fillRect(835, 5, health * 25, 50); // multiply by 25 so that the health initially fills up the entire bar
			g.setColor(Color.WHITE);
			g.drawRect(835, 5, 250, 50);

			g.setFont(View.font.deriveFont(20f));
			if (showDebug) {
				g.drawString("[FPS] " + lastFps, 3, 15);
			}

			g.transform(GetTransform(1));

			RenderLayer(Layer.Default, g);
			RenderLayer(Layer.UI, g);

			g.transform(GetTransform(-1));

			g.dispose();

			buffer.show();
		}
	}

	/**
	 * Render an individual graphics layer
	 * 
	 * @param layer The layer to render
	 * @param g     The graphics object to render to
	 */
	private void RenderLayer(Layer layer, Graphics2D g) {
		if (graphicsManagers.containsKey(layer)) {
			for (int i = 0; i < graphicsManagers.get(layer).size(); i++) {
				if (graphicsManagersToRemove.contains(graphicsManagers.get(layer).get(i))) {
					RemoveGraphicsComponent(graphicsManagers.get(layer).get(i));
				} else {
					if (graphicsManagers.get(layer) != null && graphicsManagers.get(layer).get(i) != null) {
						graphicsManagers.get(layer).get(i).paint(g);
					}
				}
			}
		}
	}

	/**
	 * Add a graphics component as one to be rendered
	 * 
	 * @param gC The graphics component to be added
	 */
	public void AddGraphicsComponent(GraphicsComponent gC) {
		if (!graphicsManagers.containsKey(gC.layer)) {
			graphicsManagers.put(gC.layer, new ArrayList<GraphicsComponent>());
		}

		int loc = 0;
		for (int i = 0; i < graphicsManagers.get(gC.layer).size(); i++) {
			if (i < graphicsManagers.get(gC.layer).size()) {
				if (graphicsManagers.get(gC.layer).get(i).orderInLayer < gC.orderInLayer) {
					loc++;
				} else {
					break;
				}
			}
		}

		if (loc <= graphicsManagers.get(gC.layer).size()) {
			graphicsManagers.get(gC.layer).add(loc, gC);
		}

	}

	/**
	 * Add a graphics component as one to be removed
	 * 
	 * @param gC The graphics component to be removed
	 */
	public void RemoveGraphicsComponent(GraphicsComponent gC) {
		if (graphicsManagers.containsKey(gC.layer)) {
			graphicsManagers.get(gC.layer).remove(gC);
		}
	}

}
