package managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import debugging.Console;
import debugging.Debug;
import GameObjects.GameObject;
import GameObjects.PlayerObject;
import logic.LevelEditor;
import logic.Scene;
import logic.Time;

/**
 * The main engine class - responsible for the game loop
 */
public class Engine implements Runnable {

	public static Engine Instance;
	/* Game Loop */
	public boolean running = true;
	public float timeScale = 1;
	long lastFrameTime = System.nanoTime();
	final long FPS_TARGET = 120;
	final long OPTIMAL_FRAME_LENGTH = 1000000000 / FPS_TARGET;
	int tps;
	int curTicks;
	int lastTime;
	public static String gamemode;

	public int players;
	protected static String ip1;
	protected static String ip2;
	protected static String ip3;
	public static int my_port1;
	public static int my_port2;
	public static int my_port3;
	public static int port1;
	public static int port2;
	public static int port3;
	public boolean player2_exit = false;
	public boolean player3_exit = false;
	public boolean player4_exit = false;
	public int hold_score;
	public boolean restarted = false;
	public boolean new_level = false;
	public boolean paused = false;
	public int times_paused = 1;

	/* Object Management */
	public int curCode;
	public List<GameObject> gameObjects = new ArrayList<GameObject>();
	public List<Scene> currentScenes = new ArrayList<Scene>();

	/**
	 * Determines how many times the game runs (ie restarts the same scene)
	 */
	public int gameRuns = 0;

	/**
	 * Current level number for singleplayer mode
	 */
	public int currentLevelNum = 0;


	/**
	 * Current level number for multiplayer mode
	 */
	public int currentMultiplayerLevelNum = 0;

	/**
	 * All the levels of the game (as String file paths)
	 */
	public Stack<String> levels = new Stack<String>();

	/**
	 * All the levels of the game (as String file paths) for multiplayer
	 */
	public Stack<String> multiPlayerLevels = new Stack<String>();

	/**
	 * The current singleplayer level the player is playing in (file path for Scene XML)
	 */
	public String currentLevel = "";

	/**
	 * The current multiplayer level the player is playing in (file path for Scene XML)
	 */
	public String currentMultiplayerLevel = "";

	/**
	 * Add levels to the levels stack (higher levels must be added first)
	 */
	public void setLevels() {
		levels.push("src/resources/level5");
		levels.push("src/resources/level4");
		levels.push("src/resources/level3");
		levels.push("src/resources/level2");
		levels.push("src/resources/level1");
		if (!levels.empty()) {
			currentLevel = levels.pop();
			currentLevelNum = 1;
		}
	}

	public void setHold_score(int scoreToSet){
		hold_score = scoreToSet;
	}
	public int getHold_score(){
		return hold_score;
	}
	public void setMultiPlayerLevels() {
		if(players == 2) {

			multiPlayerLevels.push("src/resources/level5m");
			multiPlayerLevels.push("src/resources/level4m");
			multiPlayerLevels.push("src/resources/level3m");
			multiPlayerLevels.push("src/resources/level2m");
			multiPlayerLevels.push("src/resources/level1m");
			if (!multiPlayerLevels.empty()) {
				currentMultiplayerLevel = multiPlayerLevels.pop();
			}

		}else if(players ==3){
			multiPlayerLevels.push("src/resources/level5mthree");
			multiPlayerLevels.push("src/resources/level4mthree");
			multiPlayerLevels.push("src/resources/level3mthree");
			multiPlayerLevels.push("src/resources/level2mthree");
			multiPlayerLevels.push("src/resources/level1mthree");
			if (!multiPlayerLevels.empty()) {
				currentMultiplayerLevel = multiPlayerLevels.pop();
			}
		}
		else if(players ==4){
			multiPlayerLevels.push("src/resources/level5mfour");
			multiPlayerLevels.push("src/resources/level4mfour");
			multiPlayerLevels.push("src/resources/level3mfour");
			multiPlayerLevels.push("src/resources/level2mfour");
			multiPlayerLevels.push("src/resources/level1mfour");
			if (!multiPlayerLevels.empty()) {
				currentMultiplayerLevel = multiPlayerLevels.pop();
			}

		}
	}

	public Engine(String gamemode) {
		this.gamemode = gamemode;

		// CreateObjects();
		Debug.Log("Starting Engine");
		Thread tickThread = new Thread(this);
		tickThread.start();
	}

	public Engine(String gamemode , ArrayList<String> ips,ArrayList<Integer> ports,int players){
		this.gamemode = gamemode;
		if(ips.size() == 3){
			try {
				this.players = players;
				this.my_port1 = ports.get(0);
				this.my_port2 = ports.get(1);
				this.my_port3 = ports.get(2);
				this.ip1 = ips.get(0);
				this.ip2 = ips.get(1);
				this.ip3 = ips.get(2);
				this.port1 = ports.get(3);
				this.port2 = ports.get(4);
				this.port3 = ports.get(5);
			}catch (Exception e){}
		}
		else if(ips.size() == 2){
			try {
				this.players = players;
				this.my_port1 = ports.get(0);
				this.my_port2 = ports.get(1);
				this.ip1 = ips.get(0);
				this.ip2 = ips.get(1);
				this.port1 = ports.get(3);
				this.port2 = ports.get(4);
			}catch (Exception e){}
		}
		else{
			try {
				this.players = players;
				this.my_port1=ports.get(0);
				this.ip1 = ips.get(0);
				this.port1 = ports.get(3);
			}catch (Exception e){

			}

		}

		setMultiPlayerLevels();
		Debug.Log("Starting Engine");
		Thread tickThread = new Thread(this);
		tickThread.start();
	}
	public void setIPs(ArrayList<String> hold){
		System.out.println("Set new IP's");
		if(hold.size() == 1) {
			players=2;
			ip1 = hold.get(0);
		}
		else if(hold.size() == 2){
			players=3;
			ip1=hold.get(0);
			ip2=hold.get(1);
		}
		else if(hold.size() == 3){
			players=4;
			ip1=hold.get(0);
			ip2=hold.get(1);
			ip3=hold.get(2);
		}
	}
	public String getIP(int i){
		if(i==1){
			return ip1;
		}
		else if(i==2){
			return ip2;
		}
		else{
			return ip3;
		}
	}
	public void setPorts(ArrayList<Integer> hold){
		System.out.println("Set new ports");
		if (hold.size() == 2){
			System.out.println("Listening before: " + my_port1 + " should be the same: " + hold.get(0));
			my_port1=hold.get(0);
			System.out.println("Listening: " + my_port1 + " should be the same: " + hold.get(0));
			System.out.println("Sending before: " + port1 + " should be the same: " + hold.get(1));
			port1 = hold.get(1);
			System.out.println("Sending: " + port1 + " should be the same: " + hold.get(1));
		}
		else if(hold.size() == 4){
			my_port1=hold.get(0);
			System.out.println("Listening: " + my_port1 + " should be the same: " + hold.get(0));
			my_port2=hold.get(1);
			System.out.println("Listening: " + my_port2 + " should be the same: " + hold.get(1));
			port1 = hold.get(2);
			System.out.println("Sending: " + port1 + " should be the same: " + hold.get(2));
			port2 = hold.get(3);
			System.out.println("Sending: " + port2 + " should be the same: " + hold.get(3));
		}
		else if(hold.size() ==6){
			my_port1=hold.get(0);
			my_port2=hold.get(1);
			my_port3=hold.get(2);
			port1=hold.get(3);
			port2=hold.get(4);
			port3=hold.get(5);
		}
	}


	public String getGameMode() {
		return gamemode;
	}

	public void AddObject(GameObject gO) {
		if (gO == null)
			Debug.Log("ERROR");
		gO.ID = curCode;
		curCode += 1;
		gameObjects.add(gO);
		if (currentScenes.size() > 0 && !Scene.gameObjects.contains(gO)) {
			Scene.gameObjects.add(gO);
		}
	}

	public void RemoveObject(GameObject gO) {
		if (gameObjects.contains(gO)) {
			gameObjects.remove(gO);
			gO.Remove();
		}
	}

	public void unloadAllScenes() {
		removeAllObjects();
		Scene.gameObjects.clear();
		currentScenes.clear();
	}

	@Override
	public void run() {
		AddObject(new Debug());
		AddObject(new Console());
		setLevels();
		setMultiPlayerLevels();

		if (gamemode.equals("Singleplayer") ) {
			Window.center = true;
			new Scene(currentLevel).Load();
			currentLevelNum = 1;
		} else if (gamemode.equals("Multiplayer") ) {
			Window.center = true;
			new Scene(currentMultiplayerLevel).Load();
			currentMultiplayerLevelNum = 1;
		}else if (gamemode.equals("Multiplayer 3") ) {
			Window.center = true;
			new Scene(currentMultiplayerLevel).Load();
			currentMultiplayerLevelNum = 1;
		}else if (gamemode.equals("Multiplayer 4") ) {
			Window.center = true;
			new Scene(currentMultiplayerLevel).Load();
			currentMultiplayerLevelNum = 1;
		} else if (gamemode.equals("Level Editor") ) {
			new Scene("src/resources/leScene").Load();
			LevelEditor.Instance = new LevelEditor();
		} else if (gamemode.equals("Practice") ) {
			Window.center = true;
			new Scene("src/resources/practice").Load();
		}

		while (running) {
			long now = System.nanoTime();
			float delta = 0;
			long timeSinceLast = now - lastFrameTime;
			if (timeSinceLast != 0) {
				delta = timeSinceLast / 1000000000f;
			}
			lastFrameTime = now;

			curTicks += 1;
			lastTime += timeSinceLast;

			if (lastTime >= 1000000000) {
				lastTime = 0;
				tps = curTicks;
				curTicks = 0;
			}

			/*
			 * if (Input.KeyDown(KeyEvent.VK_T)) { if (Time.timeScale == 1) { Time.timeScale
			 * = 0.1f; Debug.Log("Enabled slow time"); } else { Time.timeScale = 1f;
			 * Debug.Log("Disabled slow time"); } }
			 */

			Time.unscaledDeltaTime = delta * Time.deltaMult;
			Time.deltaTime = delta * Time.timeScale * Time.deltaMult;

			Update();

			if (currentScenes.isEmpty()) {
				if (gamemode.equals("Singleplayer") ) {
					new Scene(currentLevel).Unload();
					currentScenes.add(new Scene(currentLevel));
				} else if (gamemode.equals("Multiplayer") ) {
					new Scene(currentMultiplayerLevel).Unload();
					currentScenes.add(new Scene(currentMultiplayerLevel));
				}else if (gamemode.equals("Multiplayer 3") ) {
					new Scene(currentMultiplayerLevel).Unload();
					currentScenes.add(new Scene(currentMultiplayerLevel));
				}else if (gamemode.equals("Multiplayer 4")) {
					new Scene(currentMultiplayerLevel).Unload();
					currentScenes.add(new Scene(currentMultiplayerLevel));
				}
				else if (gamemode.equals("Level Editor")) {
					new Scene("src/resources/leScene").Unload();
					currentScenes.add(new Scene("src/resources/leScene"));
				}
				else if (gamemode.equals("Practice")) {
					new Scene("src/resources/practice").Unload();
				}
			}

			try {
				long time = (lastFrameTime - System.nanoTime() + OPTIMAL_FRAME_LENGTH) / 1000000;
				if (time > 0)
					Thread.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Removes all game objects that are currently loaded.
	 * This method is called when the user exits back to the main menu.
	 */
	public void removeAllObjects() {
		GameObject[] objects = new GameObject[gameObjects.size()];
		gameObjects.toArray(objects);
		for (GameObject gameObject : objects) {
			RemoveObject(gameObject);
		}
	}

	private void Update() {
		GameObject[] objects = new GameObject[gameObjects.size()];
		gameObjects.toArray(objects);
		for (int i = 0; i < objects.length; i++) {
			objects[i].Update();
		}
		if (LevelEditor.Instance != null) {
			LevelEditor.Instance.Update();
		}
	}

	public void restartToScene(String sceneName) {
		unloadAllScenes();
		PlayerObject.players.clear();
		new Scene(sceneName).Load();

		if (LevelEditor.Instance != null) {
			LevelEditor.Instance = new LevelEditor();
		}
	}

	/**
	 * Reloads the scene for the current level
	 */
	public void restart() {
		if (!currentScenes.isEmpty()) {
			unloadAllScenes();
			PlayerObject.players.clear();
			if (gamemode.equals("Singleplayer") ) {
				new Scene(currentLevel).Load();
			} else if (gamemode.equals("Multiplayer") ) {
				new Scene(currentMultiplayerLevel).Load();
			} else if (gamemode.equals("Multiplayer 3")) {
				new Scene(currentMultiplayerLevel).Load();
			}
			else if (gamemode.equals("Multiplayer 4") ) {
				new Scene(currentMultiplayerLevel).Load();
			} else if (gamemode.equals("Tutorial") ) {
				restartToScene("src/resources/leScene");
			} else if (gamemode.equals("Practice") ) {
				new Scene("src/resources/practice").Load();
			}
		}
	}

	/**
	 * Restarts the game onto the next level
	 * @return the level number that the player will be taken to
	 */
	public int newLevel() {
		int levelNum = -1;
		if (!currentScenes.isEmpty()) {
			currentScenes.get(0).Unload();
			PlayerObject.players.clear();
			if (gamemode.equals("Singleplayer") ) {
				if (!levels.empty()) {
					currentLevel = levels.peek(); //change this to levels.pop()?
					levelNum = Integer.parseInt(currentLevel.replaceAll("[^0-9]", ""));
					currentLevelNum = levelNum;
					new Scene(levels.pop()).Load();
				} else {
					Time.timeScale = 0f;
					MenuController.gameOverView.gameOverMenu.setVisible(true);
				}
			} else if (gamemode.equals("Multiplayer")) {
				if (!multiPlayerLevels.empty()) {
					currentMultiplayerLevel = multiPlayerLevels.pop(); //change this to levels.pop()?
					levelNum = Integer.parseInt(currentMultiplayerLevel.replaceAll("[^0-9]", ""));
					System.out.println("Level: " + levelNum);
					currentMultiplayerLevelNum = levelNum;
					new Scene(multiPlayerLevels.pop()).Load();
				} else {
					Time.timeScale = 0f;
					MenuController.gameOverView.gameOverMenu.setVisible(true);
				}
			}else if (gamemode.equals("Multiplayer 3")) {
				if (!multiPlayerLevels.empty()) {
					currentMultiplayerLevel = multiPlayerLevels.pop(); //change this to levels.pop()?
					levelNum = Integer.parseInt(currentMultiplayerLevel.replaceAll("[^0-9]", ""));
					System.out.println("Level: " + levelNum);
					currentMultiplayerLevelNum = levelNum;
					new Scene(multiPlayerLevels.pop()).Load();
				} else {
					Time.timeScale = 0f;
					MenuController.gameOverView.gameOverMenu.setVisible(true);
				}
			}
			else if (gamemode.equals("Multiplayer 4")) {
				if (!multiPlayerLevels.empty()) {
					currentMultiplayerLevel = multiPlayerLevels.pop(); //change this to levels.pop()?
					//levelNum = Integer.parseInt(currentMultiplayerLevel.replaceAll("[^0-9]", ""));
					//System.out.println("Level: " + levelNum);
					currentMultiplayerLevelNum++;
					new Scene(multiPlayerLevels.pop()).Load();
				} else {
					Time.timeScale = 0f;
					MenuController.gameOverView.gameOverMenu.setVisible(true);
				}
			} else if (gamemode.equals("Practice")) {
				new Scene("src/resources/practice").Load();
			}
		}
		return levelNum;
	}

}
