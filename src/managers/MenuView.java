package managers;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * The MenuView is responsible for displaying all components to the user.
 */
public class MenuView extends View {
    private JButton playBtn;
    private JButton statsBtn;
    private JButton workshopBtn;
    private JButton optionsBtn;
    private JButton exitBtn;

    public MenuView() {
        playBtn = new JButton("PLAY");
        statsBtn = new JButton("STATS");
        workshopBtn = new JButton("WORKSHOP");
        optionsBtn = new JButton("OPTIONS");
        exitBtn = new JButton("EXIT");

        createAndShowGUI();
    }

    /**
     * Responsible for creating all components and visually structuring them.
     */
    private void createAndShowGUI() {
        JImage menuItems = super.loadResources();

        menuItems.setLayout(new BoxLayout(menuItems, BoxLayout.Y_AXIS)); // set a BoxLayout for the menu items
        menuItems.add(Box.createRigidArea(new Dimension(0, 50)));
        menuItems.add(super.getTitle());
        menuItems.add(Box.createVerticalGlue()); // adds spacing between each menu item component

        File path = new File("src/resources/images/button.png");
        super.loadImage(path);

        super.addButton(playBtn, menuItems, true);
        super.addButton(statsBtn, menuItems, true);
        super.addButton(workshopBtn,menuItems,true);
        super.addButton(optionsBtn, menuItems, true);
        super.addButton(exitBtn, menuItems, true);

        menuItems.add(Box.createVerticalGlue()); // adds spacing between each menu item component
    }

    public JButton getPlayBtn() {
        return playBtn;
    }

    public JButton getStatsBtn() {
        return statsBtn;
    }

    public JButton getWorkshopBtn() {
        return workshopBtn;
    }

    public JButton getOptionsBtn() {
        return optionsBtn;
    }

    public JButton getExitBtn() {
        return exitBtn;
    }
}
