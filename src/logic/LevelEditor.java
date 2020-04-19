package logic;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import components.Colliders.BoxCollider;
import components.Graphics.GraphicsComponent;
import components.Graphics.GraphicsComponent_Tileable;
import components.StrengthComponent;
import components.Transform;
import customClasses.Vector2;
import GameObjects.*;
import managers.Engine;
import managers.Input;
import managers.Window;


import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import java.io.FileOutputStream;

import org.w3c.dom.*;

public class LevelEditor {

    /*
    Controls (for now):
        Click: select object (no visual indication rn)
        Left/Right arrow: cycle selected object type
        Up arrow: add object
        Down arrow: delete selected object
        S: save to xml (leScene.xml) (can rename it afterwards so it doesn't get overwritten by next save)
        B: toggle freeze

        Scale platforms with corners
        Moving platforms are grey, normal ones are black
     */

    public static LevelEditor Instance = null;

    public static String currentLevel = "src/resources/leScene";
    public boolean shouldReload = false;

    private List<String> unsavableObjects = Arrays.asList("Debug", "Console");

    enum SpawnableObjects {
        AIPlayer,
        Player,
        Ground,
        MovingGround,
        JumpBooster,
        DamageBooster,
        SpeedBooster,
        StunBooster,
        ObstacleWeapon,
        InvincibleWeapon,
        KnockbackBooster
    }

    SpawnableObjects[] spawnables = SpawnableObjects.values();

    int objectSpawnIndex = 0;

    enum ClickType {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        MIDDLE
    }

    ClickType currentClick = null;

    int cornerSize = 5;
    int minSize = 30;

    Vector2 mousePos;

    public static GameObject selectedObject;

    GameObject heldObject;
    Transform heldObjectTransform;
    BoxCollider heldObjectCollider;
    GraphicsComponent heldObjectGraphics;
    Vector2 origSize;
    Vector2 origPos;
    Vector2 offset;

    boolean frozen = false;

    public LevelEditor() {
        disableAllObjects();
    }

    public void Update() {

        if (shouldReload) {
            reloadLevel();
            shouldReload = false;
        }

        Point mousePosPoint = MouseInfo.getPointerInfo().getLocation();
        mousePos = new Vector2(mousePosPoint.x, mousePosPoint.y);
        if (Input.MouseDown(MouseEvent.BUTTON1)) {
            heldObject = getClickedObject();
            selectedObject = heldObject;
            getHeldObjectComponents();
        }
        if (Input.MouseHold(MouseEvent.BUTTON1) && heldObject != null) {
            if (currentClick != ClickType.MIDDLE) {
                if (heldObjectGraphics instanceof GraphicsComponent_Tileable)
                    scaleWithCorner(currentClick);
            } else {
                heldObjectTransform.pos = mousePos.Add(offset);
                heldObjectTransform.origPos = heldObjectTransform.pos;
            }
        } else {
            clearHeldObject();
        }
        handleKeyEvents();
    }


    private void handleKeyEvents() {
        if (Input.KeyDown(KeyEvent.VK_B)) {
            if (frozen) {
                enableAllObjects();
            } else {
                disableAllObjects();
            }
        }

        if (Input.KeyDown(KeyEvent.VK_K)) {
            Engine.Instance.restart();
        }

//        if (Input.KeyDown(KeyEvent.VK_S)) {
//            saveToXML("src/resources/leScene.xml");
//        }

        if (frozen) {
            if (selectedObject != null) {
                if (Input.KeyDown(KeyEvent.VK_RIGHT)) {
                    cycleObjects(1);
                } else if (Input.KeyDown(KeyEvent.VK_LEFT)) {
                    cycleObjects(-1);
                } else if (Input.KeyDown(KeyEvent.VK_DOWN)) {
                    Engine.Instance.RemoveObject(selectedObject);
                    selectedObject = null;
                }
            }
            if (Input.KeyDown(KeyEvent.VK_UP)) {
                GameObject newObject = new GroundObject();
                newObject.GetTransform().pos = new Vector2(900, 500);
                Engine.Instance.AddObject(newObject);
                selectedObject = newObject;
            }
        }
    }

    private void reloadLevel() {
        Engine.Instance.restartToScene(currentLevel);
    }

    private void cycleObjects(int dir) {
        objectSpawnIndex += dir;
        if (objectSpawnIndex >= spawnables.length) {
            objectSpawnIndex = 0;
        } else if (objectSpawnIndex < 0) {
            objectSpawnIndex = spawnables.length - 1;
        }
        Vector2 spawnPos = selectedObject.GetTransform().pos;
        Engine.Instance.RemoveObject(selectedObject);
        GameObject newObject = null;
        switch (spawnables[objectSpawnIndex]) {
            case AIPlayer:
                newObject = new AIPlayerObject();
                break;
            case Player:
                newObject = new PlayerObject();
                break;
            case Ground:
                newObject = new GroundObject();
                break;
            case MovingGround:
                newObject = new MovingGroundObject();
                break;
            case JumpBooster:
                newObject = new JumpBooster();
                break;
            case DamageBooster:
                newObject = new DamageBooster();
                break;
            case SpeedBooster:
                newObject = new SpeedBooster();
                break;
            case StunBooster:
                newObject = new StunBooster();
                break;
            case ObstacleWeapon:
                newObject = new ObstacleWeapon();
                break;
            case InvincibleWeapon:
                newObject = new InvincibleWeapon();
                break;
            case KnockbackBooster:
                newObject = new KnockbackBooster();
                break;
        }
        if (newObject != null) {
            newObject.GetTransform().pos = spawnPos;
            newObject.setIsInEditor(true);
            Engine.Instance.AddObject(newObject);
        }
        selectedObject = newObject;
    }

    private void getHeldObjectComponents() {
        if (heldObject != null) {
            heldObjectTransform = heldObject.GetTransform();
            heldObjectCollider = heldObject.GetComponent(BoxCollider.class);
            heldObjectGraphics = heldObject.GetComponent(GraphicsComponent.class);
            origPos = heldObjectTransform.pos;
            origSize = heldObjectGraphics.size;
        }
    }

    private void clearHeldObject() {
        heldObject = null;
        heldObjectTransform = null;
        heldObjectCollider = null;
        heldObjectGraphics = null;
        origPos = Vector2.zero();
        origSize = Vector2.zero();
        offset = Vector2.zero();
        currentClick = null;
    }

    private GameObject getClickedObject() {
        for (GameObject obj : Engine.Instance.gameObjects) {
            currentClick = getClickType(obj);
            if (currentClick != null) {
                return obj;
            }
        }
        return null;
    }

    private ClickType getClickType(GameObject obj) {
        if (!isClicked(obj)) {
            return null;
        }
        Vector2 position = obj.GetTransform().pos;
        Vector2 size = obj.GetComponent(GraphicsComponent.class).size;
        if (mousePos.x <= position.x + cornerSize && mousePos.y <= position.y + cornerSize) {
            offset = position.Minus(mousePos);
            return ClickType.TOP_LEFT;
        } else if (mousePos.x >= position.x + size.x - cornerSize && mousePos.y <= position.y + cornerSize) {
            offset = new Vector2(position.x + size.x, position.y).Minus(mousePos);
            return ClickType.TOP_RIGHT;
        } else if (mousePos.x <= position.x + cornerSize && mousePos.y >= position.y + size.y - cornerSize) {
            offset = new Vector2(position.x, position.y + size.y).Minus(mousePos);
            return ClickType.BOTTOM_LEFT;
        } else if (mousePos.x >= position.x + size.x - cornerSize && mousePos.y >= position.y + size.y - cornerSize) {
            offset = new Vector2(position.x + size.x, position.y + size.y).Minus(mousePos);
            return ClickType.BOTTOM_RIGHT;
        } else {
            offset = position.Minus(mousePos);
            return ClickType.MIDDLE;
        }
    }

    private boolean isClicked(GameObject obj) {
        Transform objTransform = obj.GetTransform();
        BoxCollider objCollider = obj.GetComponent(BoxCollider.class);
        if (objTransform == null || objCollider == null) {
            return false;
        }
        if (objTransform.pos.x - cornerSize <= mousePos.x && mousePos.x <= objTransform.pos.x + objCollider.size.x + cornerSize) {
            if (objTransform.pos.y - cornerSize <= mousePos.y && mousePos.y <= objTransform.pos.y + objCollider.size.y + cornerSize) {
                return true;
            }
        }
        return false;
    }

    public void disableAllObjects() {
        GameObject[] objects = new GameObject[Engine.Instance.gameObjects.size()];
        Engine.Instance.gameObjects.toArray(objects);
        for (int i = 0; i < objects.length; i++) {
            objects[i].setIsInEditor(true);
        }
        Window.center = false;
        frozen = true;
    }

    public void enableAllObjects() {
        GameObject[] objects = new GameObject[Engine.Instance.gameObjects.size()];
        Engine.Instance.gameObjects.toArray(objects);
        for (int i = 0; i < objects.length; i++) {
            objects[i].setIsInEditor(false);
        }
        Window.center = true;
        frozen = false;
    }

    private void scaleWithCorner(ClickType cT) {
        if (heldObjectCollider == null || heldObjectGraphics == null) {
            return;
        }
        if (cT == ClickType.TOP_LEFT) {
            Vector2 bottomRight = heldObjectTransform.pos.Add(heldObjectCollider.size);
            Vector2 newSize = new Vector2(bottomRight.x - mousePos.x - offset.x, bottomRight.y - mousePos.y - offset.y);
            heldObjectTransform.pos = mousePos.Add(offset);
            if (newSize.x < minSize) {
                newSize.x = minSize;
                heldObjectTransform.pos.x = bottomRight.x - minSize;
            }
            if (newSize.y < minSize) {
                newSize.y = minSize;
                heldObjectTransform.pos.y = bottomRight.y - minSize;
            }
            heldObjectCollider.size.x = newSize.x;
            heldObjectGraphics.size.x = newSize.x;
        } else if (cT == ClickType.TOP_RIGHT) {
            Vector2 bottomLeft = new Vector2(heldObjectTransform.pos.x, heldObjectTransform.pos.y + heldObjectCollider.size.y);
            Vector2 newSize = new Vector2(mousePos.x - bottomLeft.x + offset.x, bottomLeft.y - mousePos.y - offset.y);
            heldObjectTransform.pos.y = mousePos.y + offset.y;
            if (newSize.x < minSize)
                newSize.x = minSize;
            if (newSize.y < minSize) {
                newSize.y = minSize;
                heldObjectTransform.pos.y = bottomLeft.y - minSize;
            }
            heldObjectCollider.size.x = newSize.x;
            heldObjectGraphics.size.x = newSize.x;
        } else if (cT == ClickType.BOTTOM_LEFT) {
            Vector2 topRight = new Vector2(heldObjectTransform.pos.x + heldObjectCollider.size.x, heldObjectTransform.pos.y);
            Vector2 newSize = new Vector2(topRight.x - mousePos.x - offset.x, mousePos.y - topRight.y + offset.y);
            heldObjectTransform.pos.x = mousePos.x + offset.x;
            if (newSize.x < minSize) {
                newSize.x = minSize;
                heldObjectTransform.pos.x = topRight.x - minSize;
            }
            if (newSize.y < minSize) {
                newSize.y = minSize;
            }
            heldObjectCollider.size.x = newSize.x;
            heldObjectGraphics.size.x = newSize.x;
        } else if (cT == ClickType.BOTTOM_RIGHT) {
            Vector2 newSize = mousePos.Minus(heldObjectTransform.pos).Add(offset);
            if (newSize.x < minSize)
                newSize.x = minSize;
            if (newSize.y < minSize)
                newSize.y = minSize;
            heldObjectCollider.size.x = newSize.x;
            heldObjectGraphics.size.x = newSize.x;
        }
    }

    public void saveToXML(String fileName) {
        Document dom;
        Element e = null;

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.newDocument();
            // create the root element
            Element rootEle = dom.createElement("Scene");
            // for each obj in scene, create a node
            for(GameObject obj : Engine.Instance.gameObjects) {
                e = objectElement(obj, dom);
                if (e != null)
                    rootEle.appendChild(e);
            }
            dom.appendChild(rootEle);

            try {
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

                tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(fileName)));

            } catch (TransformerException | FileNotFoundException err) {
                System.out.println(err.getMessage());
            }
        } catch (ParserConfigurationException pce) {
            System.out.println("Error trying to instantiate DocumentBuilder " + pce);
        }
    }

    private Element objectElement(GameObject obj, Document dom) {
        String objName = obj.getClass().getSimpleName();
        if (unsavableObjects.contains(objName)) {
            return null;
        }

        Element root = dom.createElement("GameObject");
        root.setAttribute("type", objName);

        List<String> groundObjects = Arrays.asList("GroundObject", "MovingGroundObject");

        // Add transform element (all objects)
        root.appendChild(transformElement(obj, dom));

        // Add object-specific component elements
        if (groundObjects.contains(objName)) {
            root.appendChild(colliderElement(obj, dom));
            root.appendChild(graphicsElement(obj, dom));
            root.appendChild(strengthElement(obj, dom));
        }

        return root;
    }

    private Element graphicsElement(GameObject obj, Document dom) {
        if (obj.GetComponent(GraphicsComponent_Tileable.class) == null) {
            return null;
        }
        Vector2 graphicsSize = obj.GetComponent(GraphicsComponent_Tileable.class).size;
        Element root = dom.createElement("Component");
        root.setAttribute("type", "GraphicsComponent_Tileable");
        Element sizeX = dom.createElement("sizeX");
        Element sizeY = dom.createElement("sizeY");
        sizeX.appendChild(dom.createTextNode(""+(int)graphicsSize.x));
        sizeY.appendChild(dom.createTextNode(""+(int)graphicsSize.y));

        root.appendChild(sizeX);
        root.appendChild(sizeY);

        return root;
    }

    private Element colliderElement(GameObject obj, Document dom) {
        if (obj.GetComponent(BoxCollider.class) == null) {
            return null;
        }
        Vector2 colliderSize = obj.GetComponent(BoxCollider.class).size;
        Element root = dom.createElement("Component");
        root.setAttribute("type", "Collider");
        Element sizeX = dom.createElement("sizeX");
        Element sizeY = dom.createElement("sizeY");
        sizeX.appendChild(dom.createTextNode(""+(int)colliderSize.x));
        sizeY.appendChild(dom.createTextNode(""+(int)colliderSize.y));

        root.appendChild(sizeX);
        root.appendChild(sizeY);

        return root;
    }

    private Element strengthElement(GameObject obj, Document dom) {
        if (obj.GetComponent(StrengthComponent.class) == null) {
            return null;
        }
        //int strength = obj.GetComponent(StrengthComponent.class).getStrength();
        int strength = 100000;
        Element root = dom.createElement("Component");
        root.setAttribute("type", "StrengthComponent");
        Element strengthNode = dom.createElement("strength");
        strengthNode.appendChild(dom.createTextNode(""+strength));

        root.appendChild(strengthNode);

        return root;
    }

    private Element transformElement(GameObject obj, Document dom) {
        if (obj.GetTransform() == null) {
            return null;
        }
        Vector2 objPos = obj.GetTransform().pos;
        Element root = dom.createElement("Component");
        root.setAttribute("type", "Transform");
        Element xPos = dom.createElement("x");
        Element yPos = dom.createElement("y");
        xPos.appendChild(dom.createTextNode(""+(int)objPos.x));
        yPos.appendChild(dom.createTextNode(""+(int)objPos.y));

        root.appendChild(xPos);
        root.appendChild(yPos);

        return root;
    }

}
