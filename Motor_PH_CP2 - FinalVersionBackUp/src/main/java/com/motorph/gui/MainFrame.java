package com.motorph.gui;

import com.motorph.backend.*;

import javax.swing.*;
import java.awt.*;

/**
 * Creates and initializes the main application window for the
 * MotorPH Employee Management System.
 *
 * This class is responsible for creating the application's main frame,
 * configuring the CardLayout used for screen navigation, and loading
 * the Login, Employee, and Administrator interfaces.
 */
public class MainFrame {

    // Main application window.
    private JFrame frame;

    // Container that holds all application screens.
    private JPanel containerPanel;

    // Layout manager used to switch between application screens.
    private CardLayout cardLayout;

    /**
     * Creates the main application window and initializes all
     * user interface screens.
     *
     * The constructor configures the frame, registers all application
     * panels with the CardLayout, and displays the Login screen when
     * the application starts.
     */
    public MainFrame() {

        // Create the main application window.
        frame = new JFrame();

        // Create the CardLayout used for screen navigation.
        cardLayout = new CardLayout();

        // Create the container that will hold all application screens.
        containerPanel = new JPanel(cardLayout);

        // Register the Login screen.
        containerPanel.add(
                Login.createLoginPanel(cardLayout, containerPanel),
                "Login_Screen"
        );

        // Register the Employee screen.
        containerPanel.add(
                EmployeeMenu.createEmployeeMenu(cardLayout, containerPanel),
                "Employee_Screen"
        );

        // Register the Administrator screen.
        containerPanel.add(
                AdminMenu.createAdminMenu(cardLayout, containerPanel),
                "Admin_Screen"
        );

        // Configure the main application window.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setResizable(false);

        // Add the screen container to the frame.
        frame.add(containerPanel);

        // Display the application window at the center of the screen.
        frame.setLocationRelativeTo(null);

        // Make the application visible to the user.
        frame.setVisible(true);
    }
}
