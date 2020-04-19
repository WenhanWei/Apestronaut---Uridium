package debugging;

import java.util.ArrayList;
import java.util.List;

import GameObjects.GameObject;

public class Console extends GameObject {

    public static List<String> logMessages = new ArrayList<String>();
    public static List<CommandListener> listeners = new ArrayList<CommandListener>();

    public static void AddListener(CommandListener listener) {
        listeners.add(listener);
    }

    public static void RemoveListener(CommandListener listener) {
        listeners.add(listener);
    }

    public static void Invoke(String[] command) {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).CommandRecieved(command);
        }
    }

    public Console() {
        super();
        AddComponent(ConsoleController.class, ConsoleController.COMPONENT_NAME);
    }

}