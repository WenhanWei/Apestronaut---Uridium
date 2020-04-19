package managers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * ButtonListener extends the MouseAdapter class which allows the user to receive visual feedback during certain mouse events.
 */
public class ButtonListener extends MouseAdapter {
    private JButton btn;

    public ButtonListener(JButton btn) {
        this.btn = btn;
    }

    /**
     * Changes the foreground colour of a button.
     * @param e the MouseEvent.
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        btn.setForeground(Color.decode("#AF8FE4"));
    }

    /**
     * Reverts back to the original foreground colour of a button if the mouse exits the button.
     * @param e the MouseEvent.
     */
    @Override
    public void mouseExited(MouseEvent e) {
        btn.setForeground(Color.WHITE);
    }
}
