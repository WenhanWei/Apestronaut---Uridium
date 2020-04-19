
package logic;

import components.Component;
import managers.Engine;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import GameObjects.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


/**
 * A Scene - a collection of GameObject that can be loaded into the engine from
 * an XML file
 */
public class Scene {

	public static boolean sceneLoaded = false;

	/**
	 * All GameObject contained within the scene
	 */
	public static List<GameObject> gameObjects = new ArrayList<GameObject>();

	public static List<Component> allComponents = new ArrayList<Component>();

	/**
	 * Loads in the GameObjects from an XML file
	 * 
	 * @param fileName the file name of the scene data (without the .xml)
	 */
	public Scene(String fileName) {
		String filePath = fileName + ".xml";
		File xmlFile = new File(filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;

		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document document = dBuilder.parse(xmlFile);
			document.getDocumentElement().normalize();
			NodeList nodeList = document.getElementsByTagName("GameObject");

			for (int i = 0; i < nodeList.getLength(); i++) {
				gameObjects.add(resolveXMLNode(nodeList.item(i)));
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Resolves an XML node to create a GameObject
	 * 
	 * @param node The Node to be resolved
	 * @return The GameObject created
	 */
	private GameObject resolveXMLNode(Node node) {
		GameObject gO = null;

		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element element = (Element) node;

			switch (element.getAttribute("type")) {

				case "MPOpponentObject4":
					gO = new MPOpponentObject4();
					break;
				case "MPOpponentObject3":
					gO = new MPOpponentObject3();
					break;
				case "MPPlayerObject":
					gO = new MPPlayerObject();
					break;
				case "MPOpponentObject":
					gO = new MPOpponentObject();
					break;
				case "PlayerObject":
					gO = new PlayerObject();
					break;
				case "GroundObject":
					gO = new GroundObject();
					break;
				case "AIPlayerObject":
					gO = new AIPlayerObject();
					break;
				case "EnemyObject":
					gO = new EnemyObject();
					break;
				case "DamageBooster":
					gO = new DamageBooster();
					break;
				case "SpeedBooster":
					gO = new SpeedBooster();
					break;
				case "JumpBooster":
					gO = new JumpBooster();
					break;
				case "BaseBooster":
					gO = new BaseBooster();
					break;
				case "MovingObstacle":
					gO = new MovingObstacle();
					break;
				case "StunBooster":
					gO = new StunBooster();
					break;
				case "MovingGroundObject":
					gO = new MovingGroundObject();
					break;
				case "CoinObject":
					gO = new CoinObject();
					break;
				case "Meteor":
					gO = new Meteor();
					break;
				case "InvincibleWeapon":
					gO = new InvincibleWeapon();
					break;
				case "ObstacleWeapon":
					gO = new ObstacleWeapon();
					break;
				case "KnockbackBooster":
					gO = new KnockbackBooster();
					break;

			}

			if (gO != null) {
				NodeList components = element.getElementsByTagName("Component");
				for (int i = 0; i < components.getLength(); i++) {
					Element component = (Element) components.item(i);
					gO.GetComponent(component.getAttribute("type")).ResolveXML(component);
				}
			}
		}

		return gO;
	}

	/**
	 * Loads the scene's GameObjects into the engine
	 */
	public void Load() {
		sceneLoaded = false;

		for (int i = 0 ; i < gameObjects.size();i++) {
			GameObject gO = gameObjects.get(i);
			Engine.Instance.AddObject(gO);

		}
		Engine.Instance.currentScenes.add(this);
		sceneLoaded = true;
	}
	/**
	 * Unloads the scene's GameObjects from the scene
	 */
	public void Unload() {
		sceneLoaded = false;
		for (GameObject gO : gameObjects) {
			Engine.Instance.RemoveObject(gO);
		}
		Engine.Instance.currentScenes.remove(this);
	}

	// don't think this works
	public static void disableComponents() {
		for (GameObject gO : gameObjects) {
			for (Component component : gO.components.values()) {
				allComponents.add(component);
			}
		}

		for (Component component : allComponents) {
			component.Remove();
		}
	}
	

}

