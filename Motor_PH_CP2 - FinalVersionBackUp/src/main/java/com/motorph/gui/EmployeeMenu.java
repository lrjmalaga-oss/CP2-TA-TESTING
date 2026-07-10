package com.motorph.gui;

import com.motorph.backend.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Creates and manages the employee interface of the MotorPH application.
 *
 * This class allows an employee user to search for an employee record,
 * view employee information, and log out of the system.
 */
public class EmployeeMenu {

    /**
     * Creates the main employee menu panel.
     *
     * The method initializes the employee number input field, result display,
     * logout button, search button, and the required panel layouts.
     *
     * @param cardLayout the main layout used to switch application screens
     * @param containerPanel the main panel containing the application screens
     * @return the completed employee menu panel
     */
    public static JPanel createEmployeeMenu(
            CardLayout cardLayout,
            JPanel containerPanel
    ) {

        // Main panel containing the employee menu components.
        JPanel employeeMenuPanel = new JPanel(new GridBagLayout());
        employeeMenuPanel.setBackground(Color.BLACK);

        // Field used to enter the employee number.
        JTextField employeeNumberField = new JTextField();

        // Area used to display employee information.
        JTextArea resultArea = new JTextArea();

        // Create the employee menu buttons.
        JButton logoutButton = new JButton("Log out");
        JButton enterButton = new JButton("Enter");

        // Create the navigation, search, and result panels.
        JPanel leftPanel = createLeftPanel(logoutButton);
        JPanel topPanel = createTopPanel(employeeNumberField, enterButton);
        JPanel resultPanel = createResultPanel(resultArea);

        // Add all panels to the main employee menu.
        addPanelsToMainPanel(
                employeeMenuPanel,
                leftPanel,
                topPanel,
                resultPanel
        );

        // Assign actions to the Enter and Logout buttons.
        addButtonActions(
                cardLayout,
                containerPanel,
                employeeNumberField,
                enterButton,
                logoutButton,
                resultArea
        );

        return employeeMenuPanel;
    }

    /**
     * Creates the left navigation panel containing the Logout button.
     *
     * @param logoutButton the button used to return to the login screen
     * @return the completed left navigation panel
     */
    private static JPanel createLeftPanel(JButton logoutButton) {

        // Panel containing the employee navigation controls.
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        // Configure spacing for the navigation components.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 5, 10);

        // Configure the Logout button.
        logoutButton.setPreferredSize(new Dimension(100, 30));
        logoutButton.setFocusable(false);
        logoutButton.setBackground(Color.GRAY);
        logoutButton.setFont(new Font("Comic Sans", Font.BOLD, 15));

        // Place the Logout button at the top of the panel.
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(logoutButton, gbc);

        // Push the Logout button toward the top of the panel.
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(Box.createVerticalGlue(), gbc);

        return panel;
    }

    /**
     * Creates the top search panel.
     *
     * The panel contains the employee number label, employee number input
     * field, and Enter button.
     *
     * @param employeeNumberField field used to enter an employee number
     * @param enterButton button used to search for the employee record
     * @return the completed top search panel
     */
    private static JPanel createTopPanel(
            JTextField employeeNumberField,
            JButton enterButton
    ) {

        // Panel containing the employee search controls.
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        // Configure spacing for the search controls.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 0);

        // Create and configure the employee number label.
        JLabel label = new JLabel("Enter Employee No. :");
        label.setFont(new Font("Comic Sans", Font.BOLD, 15));

        // Configure the employee number input field.
        employeeNumberField.setPreferredSize(new Dimension(200, 30));
        employeeNumberField.setFont(new Font("Comic Sans", Font.BOLD, 15));

        // Configure the Enter button.
        enterButton.setPreferredSize(new Dimension(100, 30));
        enterButton.setFocusable(false);
        enterButton.setBackground(Color.WHITE);
        enterButton.setFont(new Font("Comic Sans", Font.BOLD, 15));

        // Align the search controls to the left.
        gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.WEST;

        // Add the employee number label.
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(label, gbc);

        // Add the employee number input field.
        gbc.gridx = 1;
        panel.add(employeeNumberField, gbc);

        // Add the Enter button.
        gbc.gridx = 2;
        panel.add(enterButton, gbc);

        // Add horizontal space after the search controls.
        gbc.weightx = 1.0;
        gbc.gridx = 3;
        panel.add(Box.createHorizontalGlue(), gbc);

        return panel;
    }

    /**
     * Creates the panel used to display employee information.
     *
     * The employee information is shown in a non-editable text area
     * placed inside a scroll pane.
     *
     * @param resultArea the text area used to display employee information
     * @return the completed employee result panel
     */
    private static JPanel createResultPanel(JTextArea resultArea) {

        // Panel containing the employee information display.
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        // Configure the employee information text area.
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Comic Sans", Font.BOLD, 18));
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);

        // Allow the result area to be scrolled when necessary.
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // Configure the result display to occupy the available space.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        panel.add(scrollPane, gbc);

        return panel;
    }

    /**
     * Adds the navigation, search, and result panels to the main employee
     * menu using GridBagLayout.
     *
     * @param employeeMenuPanel the main employee menu panel
     * @param leftPanel the navigation panel
     * @param topPanel the employee search panel
     * @param resultPanel the employee information display panel
     */
    private static void addPanelsToMainPanel(
            JPanel employeeMenuPanel,
            JPanel leftPanel,
            JPanel topPanel,
            JPanel resultPanel
    ) {

        // Configure and add the left navigation panel.
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.1;
        gbc.weighty = 1.0;
        gbc.gridheight = 2;
        employeeMenuPanel.add(leftPanel, gbc);

        // Configure and add the top search panel.
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 0, 5);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.9;
        gbc.weighty = 0.0;
        employeeMenuPanel.add(topPanel, gbc);

        // Configure and add the employee result panel.
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.9;
        gbc.weighty = 1.0;
        employeeMenuPanel.add(resultPanel, gbc);
    }

    /**
     * Assigns actions to the Enter and Logout buttons.
     *
     * The Enter button searches for an employee record and displays
     * the result. The Logout button returns the user to the login screen.
     *
     * @param cardLayout the main layout used to switch application screens
     * @param containerPanel the main panel containing application screens
     * @param employeeNumberField field used to enter an employee number
     * @param enterButton button used to search for an employee
     * @param logoutButton button used to return to the login screen
     * @param resultArea text area used to display employee information
     */
    private static void addButtonActions(
            CardLayout cardLayout,
            JPanel containerPanel,
            JTextField employeeNumberField,
            JButton enterButton,
            JButton logoutButton,
            JTextArea resultArea
    ) {

        // Search for and display the entered employee record.
        enterButton.addActionListener(e -> {

            // Retrieve the employee number entered by the user.
            String input = employeeNumberField.getText();

            try {

                // Require an employee number before performing the search.
                if (input == null || input.trim().isEmpty()) {
                    DialogCustomizer.show("Please Enter Employee Number!");
                    return;
                }

                // Search for the employee record in the CSV file.
                String[] record = EditEmployee.searchEmployee(input);

                if (record != null) {

                    // Display the employee information when a record is found.
                    resultArea.setText(formatEmployeeInformation(record));

                    // Clear the employee number field after the search.
                    employeeNumberField.setText("");

                } else {

                    // Notify the user when the employee number is not found.
                    DialogCustomizer.show("Employee Number Not Found!");

                    // Clear the search field and result display.
                    employeeNumberField.setText("");
                    resultArea.setText("");
                }

            } catch (IOException ex) {

                // Notify the user when the employee file cannot be accessed.
                DialogCustomizer.show("Something Went Wrong!");

                // Clear the search field and result display.
                employeeNumberField.setText("");
                resultArea.setText("");
            }
        });

        // Return the employee user to the login screen.
        logoutButton.addActionListener(e -> {

            // Display the login screen.
            cardLayout.show(containerPanel, "Login_Screen");

            // Clear all employee menu values.
            employeeNumberField.setText("");
            resultArea.setText("");
        });
    }

    /**
     * Formats an employee record for display in the result area.
     *
     * The formatted output includes personal details, employment details,
     * supervisor information, and government identification numbers.
     *
     * @param record the employee record retrieved from the CSV file
     * @return the formatted employee information
     */
    private static String formatEmployeeInformation(String[] record) {

        // Build and return the complete employee information display.
        return "EMPLOYEE INFORMATION\n" +
                "====================================\n" +
                "Employee ID:   " + record[0] + "\n" +
                "Full Name  :   " + record[2] + " " + record[1] + "\n" +
                "Birth Date :   " + record[3] + "\n" +
                "Address    :   " + record[4] + "\n" +
                "Phone Number:  " + record[5] + "\n\n" +

                "====================================\n" +
                "Status: " + record[10] + "\n" +
                "Position: " + record[11] + "\n" +
                "Immediate Supervisor: " + record[12] + "\n\n" +

                "====================================\n" +
                "GOVERNMENT INFORMATION\n" +
                "====================================\n" +
                "SSS Number: " + record[6] + "\n" +
                "PhilHealth Number: " + record[7] + "\n" +
                "TIN Number: " + record[8] + "\n" +
                "Pag-IBIG Number: " + record[9];
    }
}
