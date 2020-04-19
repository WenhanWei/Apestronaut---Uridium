package managers;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Each submenu will extend from this class as each submenu will perform the same GUI operations.
 */
public class View {
    private JLabel title;
    private JImage panel;
    private JButton backBtn;

    public static Font font;
    protected BufferedImage image;

    public View() {
        File path = new File("src/resources/icons/back.png");
        loadImage(path);
        backBtn = new JButton("BACK", new ImageIcon(image));
        backBtn.setHorizontalTextPosition(SwingConstants.RIGHT);
        backBtn.setIconTextGap(20);
    }

    /**
     * Loads all resources associated with each submenu.
     * @return the JImage (extends JPanel) for each view.
     */
    protected JImage loadResources() {
        title = new JLabel("APESTRONAUT");

        // loads and sets the background image for the panel
        loadImage(new File("src/resources/images/background.png"));
        panel = new JImage(image);

        // creates a font object from the specified font and applies a font size to it
        // the first font is applied to the title only
        loadFont(new File("src/resources/font/Neutronium.ttf"));
        font = font.deriveFont(120f);
        title.setFont(font);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(Color.WHITE);

        // this font is applied to all other components
        loadFont(new File("src/resources/font/Orbitron-Bold.ttf"));
        font = font.deriveFont(38f);

        return panel;
    }

    /**
     * If the button is clicked then execute the lambda expression supplied in the MenuController.
     * @param btn the button to be clicked.
     * @param actionListener is the action listener object.
     * @param mouseListener is the mouse listener object.
     */
    protected void addListener(JButton btn, ActionListener actionListener, MouseListener mouseListener) {
        btn.addActionListener(actionListener);
        btn.addMouseListener(mouseListener);
    }

    /**
     * Adds a menu item (button) to a panel and customises it.
     * @param btn the menu item to be added.
     * @param panel the panel for the button to be added on to.
     * @param flag if this is declared as true then add an image icon to the button.
     */
    protected void addButton(JButton btn, JComponent panel, Boolean flag) {
        if (flag) {
            btn.setIcon(new ImageIcon(image));
            btn.setVerticalTextPosition(SwingConstants.CENTER);
            btn.setHorizontalTextPosition(SwingConstants.CENTER);
        }

        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFont(font);
        btn.setForeground(Color.WHITE);

        // makes the button transparent
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btn);
    }

    /**
     * Creates a title panel that includes the title and two buttons.
     * @param backBtn the button that allows the user to go back to the previous screen.
     * @param viewBtn the second button which describes the current view.
     */
    protected JPanel createTitlePanel(JButton backBtn, JButton viewBtn) {
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS)); // set a BoxLayout for the titlePanel (in the x axis)

        font = font.deriveFont(30f);
        titlePanel.add(Box.createHorizontalGlue()); // adds spacing between each component
        addButton(backBtn, titlePanel, false);
        titlePanel.add(Box.createRigidArea(new Dimension(50, 0)));
        titlePanel.add(title);
        titlePanel.add(Box.createRigidArea(new Dimension(50, 0)));
        addButton(viewBtn, titlePanel, false);
        titlePanel.add(Box.createHorizontalGlue()); // adds spacing between each component
        
        return titlePanel;
    }

    /**
     * Loads an image and stores in the image variabke.
     * @param file the image to be loaded.
     */
    protected void loadImage(File file) {
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a font and stores it in the font variable.
     * @param file the font to be loaded.
     */
    protected void loadFont(File file) {
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, file);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Label class that extends JLabel, we can customise all of our labels here.
     */
    public class Label extends JLabel {
        public Label(String text, Font font) {
            super(text);
            setFont(font);
            setForeground(Color.WHITE);
        }
    }

    public JButton getBackBtn() {
        return backBtn;
    }

    public JLabel getTitle() {
        return title;
    }

    public JImage getPanel() {
        return panel;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }
}
