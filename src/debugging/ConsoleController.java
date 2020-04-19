package debugging;

import components.Graphics.GraphicsComponent;
import components.Graphics.Layer;
import customClasses.InputKeys;
import logic.Time;
import managers.Input;
import managers.Window;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class ConsoleController extends GraphicsComponent implements KeyListener {

    public enum Commands {
        QUIT("- quits the game"), HELP("- prints the help page"), CLEAR("- clears the console"),
        SET_TIME_SCALE("[scale] - sets time scale", 1), GET_TIME_SCALE("- prints the current time scale"),
        LIST_GAME_OBJECTS("- lists all loaded game objects"),
        LIST_GAME_OBJECT_COMPONENTS("[id] - lists all components added to the given game object", 1),
        SET_TRANSFORM("[id] [x] [y] - sets the x and y coordinate of the game object", 3),
        GET_TRANSFORM("[id] - prints the current transform of a game object", 1),
        SET_OBJECT_ENABLED("[id] [t/f] - controls whether a game object is enabled", 2),
        SET_COMPONENT_ENABLED("[id] [name] [t/f] - controls whether a component is enabled", 3);

        public final String help;
        public final int numParameters;

        private Commands(String help) {
            this.help = help;
            this.numParameters = 0;
        }

        private Commands(String help, int numParameters) {
            this.help = help;
            this.numParameters = numParameters;
        }

        public static void run(ConsoleController console, String[] input) {
            Commands command = HELP;

            if (input.length > 0) {
                for (Commands c : Commands.values()) {
                    if (c.name().equals(input[0])) {
                        command = c;
                    }
                }

                if (command.numParameters == input.length - 1) {
                    Console.Invoke(input);

                    switch (command) {
                    case SET_TIME_SCALE:
                        if (input.length > 1) {
                            try {
                                console.origTimeScale = Float.parseFloat(input[1]);
                                Debug.Log("Time scale set to " + console.origTimeScale);
                            } catch (Exception e) {
                                Debug.Warning("Incorrect format for time scale");
                                Debug.Log("- set_time_scale [float]");
                            }
                        }
                        break;
                    case GET_TIME_SCALE:
                        Debug.Log("" + console.origTimeScale);
                        break;
                    case QUIT:
                        Window.Instance.Exit();
                        break;
                    case CLEAR:
                        Console.logMessages.clear();
                        break;
                    case HELP:
                        PrintHelp();
                        break;
                    default:

                        break;
                    }
                } else {
                    Debug.Warning("Invalid Command - See Help For More Info");
                }
            }
        }

        private static void PrintHelp() {
            Debug.Log("--- HELP ---");
            Debug.Log("");
            for (Commands commands : Commands.values()) {
                Debug.Log("- " + commands.name() + " " + commands.help);
            }
            Debug.Log("");
        }

        public boolean equals(String text) {
            if (text.toUpperCase().equals(name())) {
                return true;
            }
            return false;
        }

        public static boolean contains(String upperCase) {
            for (Commands c : Commands.values()) {
                if (c.name().equals(upperCase)) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean show = false;
    protected float origTimeScale;
    private String command = "> ";
    private int traceBack;
    private String autoComplete = "";
    private List<String> commands = new ArrayList<String>();

    public ConsoleController() {
        this.layer = Layer.UI;
        this.orderInLayer = 10000;
    }

    @Override
    public void Update() {
        if (show) {
            if (Input.KeyDown(InputKeys.TOGGLE_CONSOLE, this)) {
                show = false;
                Window.Instance.removeKeyListener(this);
                Input.controller = null;
                Time.timeScale = origTimeScale;
            }

            if (Input.KeyDown(InputKeys.TOGGLE_DEBUG, this)) {
                Window.showDebug = !Window.showDebug;
            }
        } else {
            if (Input.KeyDown(InputKeys.TOGGLE_CONSOLE)) {
                show = true;
                Window.Instance.addKeyListener(this);
                Input.controller = this;
                origTimeScale = Time.timeScale;
                Time.timeScale = 0;
            }

            if (Input.KeyDown(InputKeys.TOGGLE_DEBUG)) {
                Window.showDebug = !Window.showDebug;
            }
        }
    }

    @Override
    public void paint(Graphics2D graphics) {
        if (show) {
            graphics.setColor(new Color(0, 0, 0, 0.8f));
            graphics.fillRect(0, 0, Window.WINDOW_WIDTH, Window.WINDOW_HEIGHT);

            for (int i = 0; i < Console.logMessages.size(); i++) {
                graphics.setColor(Color.white);
                if (Console.logMessages.get(i).contains("[Warning]"))
                    graphics.setColor(Color.yellow);
                if (Console.logMessages.get(i).contains("[Error]"))
                    graphics.setColor(Color.red);
                graphics.drawString(Console.logMessages.get(i), 10, Window.WINDOW_HEIGHT - 10
                        - (Console.logMessages.size() - i + 1) * graphics.getFontMetrics().getHeight());
            }
            graphics.setColor(Color.white);
            graphics.drawString(command + "_", 10, Window.WINDOW_HEIGHT - 10);
        }
    }

    private void SendCommand(String s) {
        Debug.Log("> " + s);
        commands.add(s);
        String[] commandStuff = s.toUpperCase().split(" ", 0);
        if (Commands.contains(commandStuff[0])) {
            Commands.run(this, commandStuff);
        } else {
            Debug.Warning("Invalid Command - See Help For More Info");
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            if (command.length() > 2) {
                autoComplete = "";
                command = command.substring(0, command.length() - 1);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (command.length() > 2) {
                SendCommand(command.substring(2, command.length()));
                autoComplete = "";
                traceBack = 0;
                command = "> ";
            }
        } else if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            if (autoComplete == "")
                autoComplete = command.substring(2, command.length());
            if (command.equals("> " + autoComplete)) {
                for (Commands listedCommand : Commands.values()) {
                    if (listedCommand.name().substring(0, autoComplete.length()).equals(autoComplete.toUpperCase())) {
                        command = "> " + listedCommand.name().toLowerCase();
                        break;
                    }
                }
            } else {
                boolean foundItem = false;
                boolean changed = false;
                for (Commands listedCommand : Commands.values()) {
                    if (!foundItem) {
                        if (listedCommand.equals(command.substring(2, command.length()))) {
                            foundItem = true;
                        }
                    } else {
                        if (listedCommand.name().substring(0, autoComplete.length())
                                .equals(autoComplete.toUpperCase())) {
                            command = "> " + listedCommand.name().toLowerCase();
                            changed = true;
                            break;
                        }
                    }
                }
                if (foundItem && !changed) {
                    for (Commands listedCommand : Commands.values()) {
                        if (listedCommand.name().substring(0, autoComplete.length())
                                .equals(autoComplete.toUpperCase())) {
                            command = "> " + listedCommand.name().toLowerCase();
                            break;
                        }
                    }
                }
            }
        } else if (e.getKeyChar() != '\uFFFF' && e.getKeyCode() != KeyEvent.VK_DELETE
                && e.getKeyCode() != KeyEvent.VK_ESCAPE) {
            command += e.getKeyChar();
            autoComplete = "";
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (traceBack < commands.size()) {
                traceBack += 1;
            }
            if (traceBack > 0)
                command = "> " + commands.get(commands.size() - (traceBack));
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (traceBack > 1) {
                traceBack -= 1;
                command = "> " + commands.get(commands.size() - (traceBack));
            } else if (traceBack == 1) {
                traceBack = 0;
                command = "> ";
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}