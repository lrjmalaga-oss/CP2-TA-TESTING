package com.motorph.gui;

import com.motorph.backend.*;

import javax.swing.*;
import java.awt.*;

public class Login {

    public static JPanel createLoginPanel(CardLayout cardLayout, JPanel containerPanel) {

        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(Color.BLACK);

        JPanel leftPanel = createInputPanel(cardLayout, containerPanel);
        JPanel rightPanel = createRightPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        gbc.weighty = 1.0;
        loginPanel.add(leftPanel, gbc);

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

    private static JPanel createInputPanel(CardLayout cardLayout, JPanel containerPanel) {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel userLabel = new JLabel("Username");
        JLabel passLabel = new JLabel("Password");

        userLabel.setFont(new Font("Comic Sans", Font.BOLD, 15));
        passLabel.setFont(new Font("Comic Sans", Font.BOLD, 15));

        JTextField usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(300, 30));
        usernameField.setFont(new Font("Comic Sans", Font.BOLD, 15));

        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(300, 30));
        passwordField.setFont(new Font("Comic Sans", Font.BOLD, 15));

        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(200, 30));
        loginButton.setFocusable(false);
        loginButton.setBackground(Color.WHITE);
        loginButton.setFont(new Font("Comic Sans", Font.BOLD, 15));

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(userLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(passwordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(loginButton, gbc);

        loginButton.addActionListener(e -> {

            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            String verifyResult = Verification.verify(username, password);

            switch (verifyResult) {

                case "failed":
                    DialogCustomizer.show("Please Enter Username and Password");
                    break;

                case "admin":
                    cardLayout.show(containerPanel, "Admin_Screen");
                    usernameField.setText("");
                    passwordField.setText("");
                    break;

                case "employee":
                    cardLayout.show(containerPanel, "Employee_Screen");
                    usernameField.setText("");
                    passwordField.setText("");
                    break;

                case "incorrect":
                    DialogCustomizer.show("Incorrect Password and Username");
                    usernameField.setText("");
                    passwordField.setText("");
                    break;
            }
        });

        return panel;
    }

    private static JPanel createRightPanel() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        return panel;
    }
}
	

