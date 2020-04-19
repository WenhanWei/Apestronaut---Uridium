package managers;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;

/**
 * SignInView is the first view that is presented to the user which allows them to sign in.
 */
public class SignInView extends View {
    private JButton signIn;
    private JButton resetPassword;
    private JLabel signUp;

    private JPanel panel;

    private static JTextField usernameField;
    private static JPasswordField passwordField;

    public SignInView() {
        JImage background = loadResources(); // panel which includes our background image
        background.setLayout(new BoxLayout(background, BoxLayout.Y_AXIS));

        panel = new JPanel(); // panel which includes our sign up UI components
        panel.setLayout(new MigLayout("center"));
        panel.setMaximumSize(new Dimension(600, 500));
        panel.setBackground(Color.decode("#7597E3"));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBorder(new CompoundBorder( // CompoundBorder allows us to add two borders to our panel
                BorderFactory.createMatteBorder(20, 20, 20, 20, Color.decode("#6668CC")),
                BorderFactory.createMatteBorder(20, 20, 20, 20, Color.decode("#AF8FE4"))));

        signInPanel();
        background.add(Box.createVerticalGlue());
        background.add(getTitle());
        background.add(Box.createVerticalStrut(100));

        background.add(panel);
        background.add(Box.createVerticalGlue());
    }

    /**
     * Adds all UI components that will be displayed in the sign-in view.
     */
    private void signInPanel() {
        signIn = new JButton("Sign In"); // sign-in button
        signIn.setPreferredSize(new Dimension(210, 100));
        signIn.setFont(getFont().deriveFont(25f));
        signIn.setForeground(Color.decode("#AF8EE4"));
        signIn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        resetPassword = new JButton("Reset Password"); // reset password button
        resetPassword.setPreferredSize(new Dimension(210, 100));
        resetPassword.setFont(getFont().deriveFont(25f));
        resetPassword.setForeground(Color.decode("#AF8EE4"));
        resetPassword.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel username = new JLabel("Username");
        username.setFont(getFont().deriveFont(40f));
        username.setForeground(Color.WHITE);
        usernameField = new JTextField("Username", 20);
        usernameField.setFont(getFont().deriveFont(25f));
        usernameField.setBorder(null);

        JLabel password = new JLabel("Password");
        password.setFont(getFont().deriveFont(40f));
        password.setForeground(Color.WHITE);
        passwordField = new JPasswordField("Password", 20);
        passwordField.setFont(getFont().deriveFont(25f));
        passwordField.setBorder(null);

        JLabel signUpText = new JLabel("Not an Apestronaut?");
        signUpText.setFont(getFont().deriveFont(20f));
        signUpText.setForeground(Color.WHITE);
        signUp = new JLabel("Sign Up");
        signUp.setFont(getFont().deriveFont(20f));
        signUp.setForeground(Color.BLACK);
        signUp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        panel.add(username, "gaptop 20, wrap");
        panel.add(usernameField, "wrap");
        panel.add(password, "gaptop 81, wrap");
        panel.add(passwordField, "wrap");
        panel.add(signIn, "gaptop 81, split 2, center");
        panel.add(resetPassword, "wrap");
        panel.add(signUpText, "gaptop 10, split 2, center");
        panel.add(signUp);
    }

    public static JTextField getUsernameField() {
        return usernameField;
    }

    public static JPasswordField getPasswordField() {
        return passwordField;
    }

    public JButton getSignIn() {
        return signIn;
    }

    public JButton getResetPassword() {
        return resetPassword;
    }

    public JLabel getSignUp() {
        return signUp;
    }

}
