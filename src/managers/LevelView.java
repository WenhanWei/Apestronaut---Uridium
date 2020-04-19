package managers;

import components.Network.Command;
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
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * The LevelView is to be displayed in-between each level.
 */
public class LevelView extends PauseView {

    public JDialog nextLevelMenu;

    /**
     * The LevelView is a JDialog that is displayed above the game.
     * It will contain a similar visual structure to the PauseView which is why we extend it.
     */
    public LevelView() {
        nextLevelMenu = new JDialog();
        nextLevelMenu.setSize(300, 300);
        nextLevelMenu.setResizable(false);
        nextLevelMenu.setUndecorated(true);
        nextLevelMenu.setLocationRelativeTo(Window.frame); // centers the pause menu

        JPanel dialogPanel = new JPanel(new MigLayout("align 50% 50%"));;
        dialogPanel.setBackground(Color.decode("#7597E3"));
        dialogPanel.setBorder(new CompoundBorder( // CompoundBorder allows us to add two borders to our panel
                BorderFactory.createMatteBorder(10, 10, 10, 10, Color.decode("#6668CC")),
                BorderFactory.createMatteBorder(10, 10, 10, 10, Color.decode("#AF8FE4"))));

        JLabel levelCompleted = new PauseView.Label("LEVEL COMPLETED");
        JButton nextLevel = new PauseView.Button("CONTINUE");
        JButton exitToMenu = new PauseView.Button("EXIT TO MENU");
        JButton quit = new PauseView.Button("QUIT");
        dialogPanel.add(levelCompleted, "center, wrap");
        dialogPanel.add(nextLevel, "growx, wrap");
        dialogPanel.add(exitToMenu, "growx, wrap");
        dialogPanel.add(quit, "growx, wrap");

        nextLevelMenu.add(dialogPanel);

        // if the nextLevel button is clicked, then allow the user to play the new level.
        // the nextLevel will be loaded in for the user to decide whether to continue or not.
        nextLevel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                Engine.Instance.new_level = false;

                nextLevelMenu.setVisible(false);
                Time.timeScale = 1f;
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
                }
                else if (Engine.Instance.players == 3){
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(2), Engine.Instance.port2,commandToBeSend));
                    MenuController.lobbyView.player1_3Player.setText("Player 1");
                    MenuController.lobbyView.player1_3Player.setForeground(Color.WHITE);
                    MenuController.lobbyView.player2_3Player.setText("PLayer 2");
                    MenuController.lobbyView.player2_3Player.setForeground(Color.WHITE);
                    MenuController.lobbyView.player2Score_3.setText("score");
                    MenuController.lobbyView.player3_3Player.setText("Player 3");
                    MenuController.lobbyView.player3_3Player.setForeground(Color.WHITE);
                    MenuController.lobbyView.player3Score_3.setText("score");
                }
                else{
                    service.execute(new UDPSend(sendSocket, Engine.Instance.getIP(1), Engine.Instance.port1,commandToBeSend));
                    MenuController.lobbyView.player1_2Player.setText("Player 1");
                    MenuController.lobbyView.player1_2Player.setForeground(Color.WHITE);
                    MenuController.lobbyView.player2_2Player.setText("Player 2");
                    MenuController.lobbyView.player2_2Player.setForeground(Color.WHITE);
                    MenuController.lobbyView.player2Score.setText("score");
                }
                Engine.Instance.unloadAllScenes();
                Window.running = false;
                Engine.Instance.running = false;
                Window.frame.dispose();
                nextLevelMenu.setVisible(false);
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

    }

}
