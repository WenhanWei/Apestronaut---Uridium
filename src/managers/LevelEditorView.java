package managers;

import customClasses.Vector2;
import GameObjects.*;
import logic.LevelEditor;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

/**
 * The LevelEditorView is responsible for adding UI to the level editor in our game.
 */
public class LevelEditorView extends View {
    private JPanel levelEditorPanel;

    private JLabel AIPlayer;
    private JLabel player;
    private JLabel ground;
    private JLabel movingGround;
    private JLabel baseBooster;
    private JLabel jumpBooster;
    private JLabel damageBooster;
    private JLabel speedBooster;
    private JLabel stunBooster;
    private JLabel obstacleWeapon;
    private JLabel invincibleWeapon;
    private JLabel knockbackBooster;

    private JTextField saveLevel;
    private JTextField loadLevel;
    private JButton loadButton;
    private JButton saveButton;

    private JButton start;
    private JButton restart;
    private JButton deleteObject;

    /**
     * Initialises a panel that will contain our UI components.
     */
    public LevelEditorView() {
        levelEditorPanel = new JPanel(new FlowLayout());
        levelEditorPanel.setBackground(Color.decode("#AF8FE4"));

        createAndShowGUI();
    }

    /**
     * Populates our level editor panel with the necessary components.
     * We use an ImageIcon to represent our objects as opposed to text.
     */
    private void createAndShowGUI() {
        AIPlayer = new Label(new ImageIcon(Window.aiPlayer));
        player = new Label(new ImageIcon(Window.player));
        ground = new Label(new ImageIcon(Window.ground));
        movingGround = new Label(new ImageIcon(Window.movingGround));
        baseBooster = new Label(new ImageIcon(Window.baseBoost));
        jumpBooster = new Label(new ImageIcon(Window.jumpBoost));
        damageBooster = new Label(new ImageIcon(Window.damageBoost));
        speedBooster = new Label(new ImageIcon(Window.speedBoost));
        stunBooster = new Label(new ImageIcon(Window.stunBoost));
        obstacleWeapon = new Label(new ImageIcon(Window.obstacleWeapon));
        invincibleWeapon = new Label(new ImageIcon(Window.invincibleWeapon));
        knockbackBooster = new Label(new ImageIcon(Window.knockbackBooster));

        saveLevel = new JTextField(10);
        loadLevel = new JTextField(10);
        saveButton = new JButton("Save");
        loadButton = new JButton("Load");

        start = new JButton("Start");
        restart = new JButton("Restart");
        deleteObject = new JButton("Delete Object");

        addListeners();

        levelEditorPanel.add(start);
        levelEditorPanel.add(restart);
        levelEditorPanel.add(deleteObject);
        levelEditorPanel.add(AIPlayer);
        levelEditorPanel.add(player);
        levelEditorPanel.add(ground);
        levelEditorPanel.add(movingGround);
        levelEditorPanel.add(baseBooster);
        levelEditorPanel.add(jumpBooster);
        levelEditorPanel.add(damageBooster);
        levelEditorPanel.add(speedBooster);
        levelEditorPanel.add(stunBooster);
        levelEditorPanel.add(obstacleWeapon);
        levelEditorPanel.add(invincibleWeapon);
        levelEditorPanel.add(knockbackBooster);
        levelEditorPanel.add(saveLevel);
        levelEditorPanel.add(saveButton);
        levelEditorPanel.add(loadLevel);
        levelEditorPanel.add(loadButton);
    }

    /**
     * Adds listeners to the objects presented in the level editor panel.
     * Also changes the cursor when hovering above an object to indicate that it is clickable.
     */
    private void addListeners() {
        start.addActionListener(e -> {
            LevelEditor.Instance.enableAllObjects();
        });
        restart.addActionListener(e -> {
            LevelEditor.Instance.shouldReload = true;
        });
        deleteObject.addActionListener(e -> {
            Engine.Instance.RemoveObject(LevelEditor.selectedObject);
            LevelEditor.selectedObject = null;
        });

        AIPlayer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                addObject(new AIPlayerObject());
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                AIPlayer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });
        player.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                addObject(new PlayerObject());
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                player.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });
        ground.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                addObject(new GroundObject());
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                ground.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });
        movingGround.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                addObject(new MovingGroundObject());
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                movingGround.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });
        baseBooster.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                addObject(new BaseBooster());
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                jumpBooster.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });
        jumpBooster.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                addObject(new JumpBooster());
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                jumpBooster.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });
        damageBooster.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                addObject(new DamageBooster());
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                damageBooster.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });
        speedBooster.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                addObject(new SpeedBooster());
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                speedBooster.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });
        stunBooster.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                addObject(new StunBooster());
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                stunBooster.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });
        obstacleWeapon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                addObject(new ObstacleWeapon());
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                obstacleWeapon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });
        invincibleWeapon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                addObject(new InvincibleWeapon());
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                invincibleWeapon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });
        knockbackBooster.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                addObject(new KnockbackBooster());
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                knockbackBooster.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });
        saveButton.addActionListener(e -> {
            String savePath = null;
            try{
                savePath = showFileSaveDialog(levelEditorPanel).getPath();
            }catch (Exception ec){
                //no file save
            }
            if(savePath!=null){
                LevelEditor.Instance.saveToXML(savePath);
                saveLevel.setText(savePath.substring(0,savePath.length()-4));
                LevelEditor.currentLevel = saveLevel.getText();
            }
        });

        loadButton.addActionListener(e -> {
            String loadPath = null;
             try {
                 loadPath = showFileOpenDialog(levelEditorPanel).getPath();
             }catch (Exception ec){
                 //no file load
             }
            if(loadPath!=null){
                loadLevel.setText(loadPath.substring(0,loadPath.length()-4));
                LevelEditor.Instance.currentLevel = loadLevel.getText();
                LevelEditor.Instance.shouldReload = true;
            }
        });
    }

    /**
     * Load File
     * @param parent current panel
     * @return File Object
     */
    private static File showFileOpenDialog(Component parent) {
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setCurrentDirectory(new File("."));

        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        fileChooser.setMultiSelectionEnabled(false);

        fileChooser.setFileFilter(new FileNameExtensionFilter("xml(*.xml)", "xml"));

        int result = fileChooser.showOpenDialog(parent);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            return file;
        }
        return null;
    }

    /**
     * Save File
     * @param parent current panel
     * @return File Object
     */
    private static File showFileSaveDialog(Component parent) {

        JFileChooser fileChooser = new JFileChooser();

        FileSystemView fsv = FileSystemView.getFileSystemView();
        //System.out.println(fsv.getHomeDirectory());
        fileChooser.setCurrentDirectory(fsv.getHomeDirectory());

        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        fileChooser.setMultiSelectionEnabled(false);

        fileChooser.setFileFilter(new FileNameExtensionFilter("xml(*.xml)", "xml"));

        fileChooser.setSelectedFile(new File("MyLevel.xml"));

        int result = fileChooser.showSaveDialog(parent);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            return file;
        }
        return null;
    }

    /**
     * Adds the specified object to the level editor space.
     * @param newObject the object to be added.
     */
    private void addObject(GameObject newObject) {
        newObject.GetTransform().pos = new Vector2(900, 500);
        newObject.setIsInEditor(true);
        Engine.Instance.AddObject(newObject);
    }

    public JPanel getLevelEditorPanel() {
        return levelEditorPanel;
    }

    private class Label extends JLabel {
        public Label(ImageIcon image) {
            setIcon(image);
        }
    }
}
