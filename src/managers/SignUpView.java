package managers;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;

/**
 * The SignUpView allows the user to create an account if they don't already have one.
 */
public class SignUpView extends View {
    private JButton signUp;
    private JLabel signIn;

    private JPanel panel;

    private static JTextField usernameField;
    private static JPasswordField passwordField;
    private static JTextField dateOfBirthField;

    public SignUpView() {
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

        signUpPanel();
        background.add(Box.createVerticalGlue());
        background.add(getTitle());
        background.add(Box.createVerticalStrut(100));
        background.add(panel);
        background.add(Box.createVerticalGlue());
    }

    /**
     * Adds all UI components that will be displayed in the sign-up view.
     */
    private void signUpPanel() {
        signUp = new JButton("Sign Up"); // sign-up button
        signUp.setPreferredSize(new Dimension(300, 100));
        signUp.setFont(getFont().deriveFont(50f));
        signUp.setForeground(Color.decode("#AF8EE4"));
        signUp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

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

        JLabel dateOfBirth = new JLabel("Date of Birth");
        dateOfBirth.setFont(getFont().deriveFont(40f));
        dateOfBirth.setForeground(Color.WHITE);
        dateOfBirthField = new JTextField("yyyy-MM-dd", 20);
        dateOfBirthField.setFont(getFont().deriveFont(25f));
        dateOfBirthField.setBorder(null);

        JLabel signInText = new JLabel("Already an Apestronaut?");
        signInText.setFont(getFont().deriveFont(20f));
        signInText.setForeground(Color.WHITE);
        signIn = new JLabel("Sign In");
        signIn.setFont(getFont().deriveFont(20f));
        signIn.setForeground(Color.BLACK);
        signIn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        panel.add(username, "gaptop 20, wrap");
        panel.add(usernameField, "wrap");
        panel.add(password, "gaptop 20, wrap");
        panel.add(passwordField, "wrap");
        panel.add(dateOfBirth, "gaptop 20, wrap");
        panel.add(dateOfBirthField, "wrap");
        panel.add(signUp, "gaptop 30, center, wrap");
        panel.add(signInText, "gaptop 10, split 2, center");
        panel.add(signIn);
    }

    public static JTextField getUsernameField() {
        return usernameField;
    }

    public static JPasswordField getPasswordField() {
        return passwordField;
    }

    public static JTextField getDateOfBirthField() {
        return dateOfBirthField;
    }

    public JButton getSignUp() {
        return signUp;
    }

    public JLabel getSignIn() {
        return signIn;
    }

}
