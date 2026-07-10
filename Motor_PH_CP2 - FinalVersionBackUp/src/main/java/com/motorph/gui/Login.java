package com.motorph.gui;

import com.motorph.backend.*;

import javax.swing.*;
import java.awt.*;

/**
 * Creates and manages the login interface of the MotorPH application.
 *
 * This class provides the username and password fields, verifies the
 * entered credentials, and directs the user to the appropriate screen
 * based on the identified account role.
 */
public class Login {

    /**
     * Creates the main login panel.
     *
     * The panel is divided into a left section containing the login form
     * and a right section reserved for additional interface content.
     *
     * @param cardLayout the layout used to switch between application screens
     * @param containerPanel the main panel containing all application screens
     * @return the completed login panel
     */
    public static JPanel createLoginPanel(
            CardLayout cardLayout,
            JPanel containerPanel
    ) {

        // Main panel containing the login form and right-side panel.
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(Color.BLACK);

        // Create the login input panel and the right-side display panel.
        JPanel leftPanel = createInputPanel(cardLayout, containerPanel);
        JPanel rightPanel = createRightPanel();

        // Configure the placement of the login input panel.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        gbc.weighty = 1.0;
        loginPanel.add(leftPanel, gbc);

        // Create separate constraints for the right-side panel.
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        gbc.weighty = 1.0;
        loginPanel.add(rightPanel, gbc);

        return loginPanel;
    }

    /**
     * Creates the login input panel.
     *
     * The panel contains the username field, password field, and Login
     * button. Submitted credentials are verified before the user is
     * redirected to the administrator or employee screen.
     *
     * @param cardLayout the layout used to switch application screens
     * @param containerPanel the panel containing all application screens
     * @return the completed login input panel
     */
    private static JPanel createInputPanel(
            CardLayout cardLayout,
            JPanel containerPanel
    ) {

        // Panel containing the login form.
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        // Configure spacing for the login components.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Create the username and password labels.
        JLabel userLabel = new JLabel("Username");
        JLabel passLabel = new JLabel("Password");

        // Apply consistent font styling to the labels.
        userLabel.setFont(new Font("Comic Sans", Font.BOLD, 15));
        passLabel.setFont(new Font("Comic Sans", Font.BOLD, 15));

        // Create and configure the username field.
        JTextField usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(300, 30));
        usernameField.setFont(new Font("Comic Sans", Font.BOLD, 15));

        // Create and configure the password field.
        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(300, 30));
        passwordField.setFont(new Font("Comic Sans", Font.BOLD, 15));

        // Create and configure the Login button.
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(200, 30));
        loginButton.setFocusable(false);
        loginButton.setBackground(Color.WHITE);
        loginButton.setFont(new Font("Comic Sans", Font.BOLD, 15));

        // Add the username label.
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(userLabel, gbc);

        // Add the username field.
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(usernameField, gbc);

        // Add the password label.
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passLabel, gbc);

        // Add the password field.
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(passwordField, gbc);

        // Add the Login button.
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(loginButton, gbc);

        // Verify the entered credentials when the Login button is clicked.
        loginButton.addActionListener(e -> {

            // Retrieve the username and password entered by the user.
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Validate the credentials and identify the user role.
            String verifyResult = Verification.verify(username, password);

            // Perform the appropriate action based on the verification result.
            switch (verifyResult) {

                // Display an error when either login field is empty.
                case "failed":
                    DialogCustomizer.show(
                            "Please Enter Username and Password"
                    );
                    break;

                // Open the administrator screen for valid admin credentials.
                case "admin":
                    cardLayout.show(containerPanel, "Admin_Screen");
                    usernameField.setText("");
                    passwordField.setText("");
                    break;

                // Open the employee screen for valid employee credentials.
                case "employee":
                    cardLayout.show(containerPanel, "Employee_Screen");
                    usernameField.setText("");
                    passwordField.setText("");
                    break;

                // Display an error when the credentials are incorrect.
                case "incorrect":
                    DialogCustomizer.show(
                            "Incorrect Password and Username"
                    );
                    usernameField.setText("");
                    passwordField.setText("");
                    break;
            }
        });

        return panel;
    }

    /**
     * Creates the right-side panel of the login screen.
     *
     * The panel is currently empty and may be used for branding,
     * instructions, or other visual content.
     *
     * @return the completed right-side panel
     */
    private static JPanel createRightPanel() {

        // Create the empty right-side panel.
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        return panel;
    }
}
	

