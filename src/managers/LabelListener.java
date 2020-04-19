package managers;

import customClasses.InputKeys;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * LabelListener extends the MouseAdapter class which allows the user to receive visual feedback during certain mouse events.
 */
public class LabelListener extends MouseAdapter {
    private JLabel label;
    private int id; // represents each control declared from InputKeys
    private JLabel[] controls;
    private JFrame jFrame;

    public static boolean flag = false; // indicates whether the user has clicked on a control to change it

    public LabelListener(JLabel label, int id, JLabel[] controls, JFrame jFrame) {
        this.label = label;
        this.id = id;
        this.controls = controls;
        this.jFrame = jFrame;
    }

    /**
     * If the user clicks on a control then provide visual feedback.
     * @param e the MouseEvent.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        label.setBackground(Color.decode("#AF8FE4"));
        flag = true;
    }

    /**
     * If the user hovers over a control then change the cursor to indicate that they can click on it.
     * @param e the MouseEvent.
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        super.mouseEntered(e);
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /**
     * Once the user has exited the control box then we can update the respective controls.
     * @param e the MouseEvent.
     */
    @Override
    public void mouseExited(MouseEvent e) {
        if (!flag) {
            return;
        }

        label.setBackground(Color.decode("#6668CC"));
        label.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        if (flag && Input.flag) {
            for (JLabel label : controls) {
                // displays a pop up message to indicate that the key is already assigned to another key.
                if (label.getText().equals(Input.key)) {
                    JOptionPane.showMessageDialog(jFrame, "This key has already been assigned, please choose a different key.");
                    flag = false;
                    Input.flag = false;
                    return;
                }
            }

            label.setText(Input.key);
            switch (id) {
                case 0:
                    InputKeys.UP = Input.keyEvent;
                    break;
                case 1:
                    InputKeys.LEFT = Input.keyEvent;
                    break;
                case 2:
                    InputKeys.RIGHT = Input.keyEvent;
                    break;
                case 3:
                    InputKeys.KICK_OR_SQUASH = Input.keyEvent;
                    break;
                case 4:
                    InputKeys.BOOSTER = Input.keyEvent;
                    break;
                case 5:
                    InputKeys.PAUSE = Input.keyEvent;
                    break;
            }
        }

        flag = false;
        Input.flag = false;

    }

}