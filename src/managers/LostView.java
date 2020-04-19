package managers;

import logic.LevelEditor;
import logic.Time;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

/**
 * The LostView is to be presented to the user if they die to early in the game.
 */
public class LostView extends PauseView {

    public JDialog gameOverMenu;

    /**
     * The LostView is a JDialog that is displayed above the game.
     * It will contain a similar visual structure to the PauseView which is why we extend it.
     */
    public LostView() {
        gameOverMenu = new JDialog();
        gameOverMenu.setSize(300, 300);
        gameOverMenu.setResizable(false);
        gameOverMenu.setUndecorated(true);
        gameOverMenu.setLocationRelativeTo(Window.frame); // centers the pause menu

        JPanel dialogPanel = new JPanel(new MigLayout("align 50% 50%"));;
        dialogPanel.setBackground(Color.decode("#7597E3"));
        dialogPanel.setBorder(new CompoundBorder( // CompoundBorder allows us to add two borders to our panel
                BorderFactory.createMatteBorder(10, 10, 10, 10, Color.decode("#6668CC")),
                BorderFactory.createMatteBorder(10, 10, 10, 10, Color.decode("#AF8FE4"))));

        JLabel gameCompleted = new PauseView.Label("Sorry!");
        JLabel gameCompleted2 = new PauseView.Label("YOU DIED");
        JLabel gameCompleted3 = new PauseView.Label("APESTRONAUT");
        gameCompleted2.setFont(gameCompleted2.getFont().deriveFont(15f));
        gameCompleted3.setFont(gameCompleted2.getFont().deriveFont(15f));
        JButton exitToMenu = new PauseView.Button("EXIT TO MENU");
        JButton quit = new PauseView.Button("QUIT");
        dialogPanel.add(gameCompleted, "center, wrap");
        dialogPanel.add(gameCompleted2, "center, wrap");
        dialogPanel.add(gameCompleted3, "center, wrap");
        dialogPanel.add(exitToMenu, "growx, wrap");
        dialogPanel.add(quit, "growx, wrap");

        gameOverMenu.add(dialogPanel);

        // if the exit to menu button is clicked, then dispose of the game objects.
        exitToMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
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
                gameOverMenu.setVisible(false);
                MenuController.viewing = true;
                MenuController.engine_started = false;
//                MenuController.close_update = false;
                MenuController.close_update = true;
                MenuController.lobbyView.getCardLayout().show(MenuController.lobbyView.getLobbyContainer(), "Main Lobby");
                MenuController.cardLayout.show(MenuController.jPanel, "Play View");
                try {
                    MenuController.connection.Back();
                }catch (Exception ce){

                }
                Window.running = false;
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
                Window.frame.dispatchEvent(new WindowEvent(Window.frame, WindowEvent.WINDOW_CLOSING));
            }
        });

    }
}
