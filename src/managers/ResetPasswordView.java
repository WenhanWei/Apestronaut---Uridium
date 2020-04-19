package managers;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;

/**
 * ResetPasswordView allows the user to reset their password.
 */
public class ResetPasswordView extends View {
    private JButton submit;
    private JLabel signIn;
    private JPanel panel;

    private static JTextField usernameField;
    private static JPasswordField passwordField;
    private static JTextField dateOfBirthField;

    public ResetPasswordView() {
        JImage background = loadResources(); // panel which includes our background image
        background.setLayout(new BoxLayout(background, BoxLayout.Y_AXIS));

        panel = new JPanel(); // panel which includes our reset password UI components
        panel.setLayout(new MigLayout("center"));
        panel.setMaximumSize(new Dimension(600, 500));
        panel.setBackground(Color.decode("#7597E3"));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBorder(new CompoundBorder( // CompoundBorder allows us to add two borders to our panel
                BorderFactory.createMatteBorder(20, 20, 20, 20, Color.decode("#6668CC")),
                BorderFactory.createMatteBorder(20, 20, 20, 20, Color.decode("#AF8FE4"))));

        resetPasswordPanel();
        background.add(Box.createVerticalGlue());
        background.add(getTitle());
        background.add(Box.createVerticalStrut(100));
        background.add(panel);
        background.add(Box.createVerticalGlue());
    }

    /**
     * Adds all UI components that will be displayed in the reset password view.
     */
    private void resetPasswordPanel() {
        submit = new JButton("Reset Password"); // reset password button
        submit.setPreferredSize(new Dimension(210, 100));
        submit.setFont(getFont().deriveFont(25f));
        submit.setForeground(Color.decode("#AF8EE4"));
        submit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel username = new JLabel("Username");
        username.setFont(getFont().deriveFont(40f));
        username.setForeground(Color.WHITE);
        usernameField = new JTextField("Username", 20);
        usernameField.setFont(getFont().deriveFont(25f));
        usernameField.setBorder(null);

        JLabel password = new JLabel("New Password");
        password.setFont(getFont().deriveFont(40f));
        password.setForeground(Color.WHITE);
        passwordField = new JPasswordField("Password", 20);
        passwordField.setFont(getFont().deriveFont(25f));
        passwordField.setBorder(null);

        JLabel dateOfBirth = new JLabel("Date of Birth");
        dateOfBirth.setFont(getFont().deriveFont(40f));
        dateOfBirth.setForeground(Color.WHITE);
        dateOfBirthField = new JTextField("yyyy-MM-dd", 20);
        dateOfBirthField.setFont(getFont().deriveFont(25f));
        dateOfBirthField.setBorder(null);

        JLabel forgotPassword = new JLabel("Forgot your password?");
        forgotPassword.setFont(getFont().deriveFont(30f));
        forgotPassword.setForeground(Color.WHITE);

        JLabel goBack = new JLabel("Remember you password?");
        goBack.setFont(getFont().deriveFont(20f));
        goBack.setForeground(Color.WHITE);
        signIn = new JLabel("Sign In");
        signIn.setFont(getFont().deriveFont(20f));
        signIn.setForeground(Color.BLACK);
        signIn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        panel.add(forgotPassword, "gaptop 20, center, wrap");
        panel.add(username, "gaptop 20, wrap");
        panel.add(usernameField, "wrap");
        panel.add(password, "gaptop 20, wrap");
        panel.add(passwordField, "wrap");
        panel.add(dateOfBirth, "gaptop 20, wrap");
        panel.add(dateOfBirthField, "wrap");
        panel.add(submit, "gaptop 30, center, wrap");
        panel.add(goBack, "gaptop 10, split 2, center");
        panel.add(signIn);
    }

    public JButton getSubmit() {
        return submit;
    }

    public JLabel getSignIn(){
        return signIn;
    }

    public static JTextField getUsernameField() {
        return usernameField;
    }

    public static JTextField getDateOfBirthField() {
        return dateOfBirthField;
    }

    public static JPasswordField getPasswordField() {
        return passwordField;
    }
}
