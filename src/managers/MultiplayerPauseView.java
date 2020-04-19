package managers;

import components.Network.Command;
import components.Network.UDPReceive;
import components.Network.UDPSend;
import logic.LevelEditor;
import logic.Time;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The PauseView is to be displayed when the user presses the pause key.
 */
public class MultiplayerPauseView {

    public static JDialog pause = MultiplayerPauseView();

    /**
     * The PauseView is a JDialog that is displayed above the game.
     * It contains a 'PAUSED' message as well as 3 different buttons.
     * @return the JDialog (our PauseView).
     */
    public static JDialog MultiplayerPauseView() {
        JDialog pauseMenu = new JDialog();
        pauseMenu.setSize(300, 300);
        pauseMenu.setResizable(false);
        pauseMenu.setUndecorated(true);
        pauseMenu.setLocationRelativeTo(Window.frame); // centers the pause menu

        JPanel dialogPanel = new JPanel(new MigLayout("align 50% 50%"));;
        dialogPanel.setBackground(Color.decode("#7597E3"));
        dialogPanel.setBorder(new CompoundBorder( // CompoundBorder allows us to add two borders to our panel
                BorderFactory.createMatteBorder(10, 10, 10, 10, Color.decode("#6668CC")),
                BorderFactory.createMatteBorder(10, 10, 10, 10, Color.decode("#AF8FE4"))));

        Label pause = new Label("PAUSED");
        Button resume = new Button("RESUME");
        Button exitToMenu = new Button("EXIT TO MENU");
        Button quit = new Button("QUIT");
        dialogPanel.add(pause, "center, wrap");
        dialogPanel.add(resume, "growx, wrap");
        dialogPanel.add(exitToMenu, "growx, wrap");
        dialogPanel.add(quit, "growx, wrap");

        pauseMenu.add(dialogPanel);

        // if the resume button is clicked, then resume the game.
        resume.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
//                pauseMenu.setVisible(false);
//                Time.timeScale = 1f;
            }
        });

        // if the exit to menu button is clicked, then dispose of the game objects.
        exitToMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Command commandToBeSend = new Command("Exit");
                ExecutorService service = Executors.newFixedThreadPool(1);
                DatagramSocket sendSocket = null;
                try {
                    Random r=new Random();
                    int port = r.nextInt(10000-1024)+1024;
                    while(port == Engine.Instance.my_port1 || port == Engine.Instance.my_port2 || port == Engine.Instance.my_port3){
                        port = r.nextInt(10000-1024)+1024;
                    }
                    sendSocket = new DatagramSocket();
                } catch (SocketException se) {
                    se.printStackTrace();
                }
                if(Engine.Instance.players == 4) {
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(2), Engine.Instance.port2,commandToBeSend));
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(3), Engine.Instance.port3,commandToBeSend));
                }
                else if (Engine.Instance.players == 3){
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(2), Engine.Instance.port2,commandToBeSend));
                }
                else{
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                }
//                MenuController.connection.Clear_Names();
                if(Engine.Instance.players == 4){
                    MenuController.lobbyView.player1_4Player.setText("Player 1");
                    MenuController.lobbyView.player1_4Player.setForeground(Color.WHITE);
                    MenuController.lobbyView.player2_4Player.setText("Player 2");
                    MenuController.lobbyView.player2_4Player.setForeground(Color.WHITE);
                    MenuController.lobbyView.player2Score_4.setText("score");
                    MenuController.lobbyView.player3_4Player.setText("Player 3");
                    MenuController.lobbyView.player3_4Player.setForeground(Color.WHITE);
                    MenuController.lobbyView.player3Score_4.setText("score");
                    MenuController.lobbyView.player4_4Player.setText("Player 4");
                    MenuController.lobbyView.player4_4Player.setForeground(Color.WHITE);
                    MenuController.lobbyView.player4Score_4.setText("score");
                    Engine.Instance.player2_exit = false;
                    Engine.Instance.player3_exit = false;
                    Engine.Instance.player4_exit = false;
                }
                else if(Engine.Instance.players == 3){
                    MenuController.lobbyView.player1_3Player.setText("Player 1");
                    MenuController.lobbyView.player1_3Player.setForeground(Color.WHITE);
                    MenuController.lobbyView.player2_3Player.setText("PLayer 2");
                    MenuController.lobbyView.player2_3Player.setForeground(Color.WHITE);
                    MenuController.lobbyView.player2Score_3.setText("score");
                    MenuController.lobbyView.player3_3Player.setText("Player 3");
                    MenuController.lobbyView.player3_3Player.setForeground(Color.WHITE);
                    MenuController.lobbyView.player3Score_3.setText("score");
                    Engine.Instance.player2_exit = false;
                    Engine.Instance.player3_exit = false;
                }
                else if(Engine.Instance.players == 2){
                    MenuController.lobbyView.player1_2Player.setText("Player 1");
                    MenuController.lobbyView.player1_2Player.setForeground(Color.WHITE);
                    MenuController.lobbyView.player2_2Player.setText("Player 2");
                    MenuController.lobbyView.player2_2Player.setForeground(Color.WHITE);
                    MenuController.lobbyView.player2Score.setText("score");
                    Engine.Instance.player2_exit = false;
                }
                Engine.Instance.unloadAllScenes();
                Window.running = false;
                Engine.Instance.running = false;
                Window.frame.dispose();
                pauseMenu.setVisible(false);
                MenuController.jFrame.setVisible(true);
                LevelEditor.Instance = null;
                Time.timeScale = 1f;
            }
        });

        // if the quit button is clicked, then close everything.
        quit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Command commandToBeSend = new Command("Exit");
                ExecutorService service = Executors.newFixedThreadPool(1);
                DatagramSocket sendSocket = null;
                try {
                    Random r=new Random();
                    int port = r.nextInt(10000-1024)+1024;
                    while(port == Engine.Instance.my_port1 || port == Engine.Instance.my_port2 || port == Engine.Instance.my_port3){
                        port = r.nextInt(10000-1024)+1024;
                    }
                    sendSocket = new DatagramSocket();
                } catch (SocketException se) {
                    se.printStackTrace();
                }
                if(Engine.Instance.players == 4) {
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(2), Engine.Instance.port2,commandToBeSend));
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(3), Engine.Instance.port3,commandToBeSend));
                }
                else if (Engine.Instance.players == 3){
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(2), Engine.Instance.port2,commandToBeSend));
                }
                else{
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                }
                Window.frame.dispatchEvent(new WindowEvent(Window.frame, WindowEvent.WINDOW_CLOSING));
            }
        });

        return pauseMenu;
    }

    /**
     * Button is a class that extends JButton and allows us to set the default
     * appearance/properties of a JButton without having to do it manually for each button.
     */
    protected static class Button extends JButton {
        public Button(String text) {
            super(text);
            setAlignmentX(Component.CENTER_ALIGNMENT);
            setFont(getFont().deriveFont(20f));
            setForeground(Color.WHITE);
            setBackground(Color.decode("#6668CC"));
        }
    }

    /**
     * Label is a class that is similar to Button, but instead extends JLabel.
     */
    protected static class Label extends JLabel {
        public Label(String text) {
            super(text);
            setForeground(Color.WHITE);
            setFont(getFont().deriveFont(20f));
        }
    }

}
