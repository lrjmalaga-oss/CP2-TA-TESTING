package com.motorph.gui;

import com.motorph.backend.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class EmployeeMenu {

    public static JPanel createEmployeeMenu(CardLayout cardLayout, JPanel containerPanel) {

        JPanel employeeMenuPanel = new JPanel(new GridBagLayout());
        employeeMenuPanel.setBackground(Color.BLACK);

        JTextField employeeNumberField = new JTextField();
        JTextArea resultArea = new JTextArea();

        JButton logoutButton = new JButton("Log out");
        JButton enterButton = new JButton("Enter");

        JPanel leftPanel = createLeftPanel(logoutButton);
        JPanel topPanel = createTopPanel(employeeNumberField, enterButton);
        JPanel resultPanel = createResultPanel(resultArea);

        addPanelsToMainPanel(employeeMenuPanel, leftPanel, topPanel, resultPanel);

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

    private static JPanel createLeftPanel(JButton logoutButton) {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 5, 10);

        logoutButton.setPreferredSize(new Dimension(100, 30));
        logoutButton.setFocusable(false);
        logoutButton.setBackground(Color.GRAY);
        logoutButton.setFont(new Font("Comic Sans", Font.BOLD, 15));

        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(logoutButton, gbc);

        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(Box.createVerticalGlue(), gbc);

        return panel;
    }

    private static JPanel createTopPanel(JTextField employeeNumberField, JButton enterButton) {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 0);

        JLabel label = new JLabel("Enter Employee No. :");
        label.setFont(new Font("Comic Sans", Font.BOLD, 15));

        employeeNumberField.setPreferredSize(new Dimension(200, 30));
        employeeNumberField.setFont(new Font("Comic Sans", Font.BOLD, 15));

        enterButton.setPreferredSize(new Dimension(100, 30));
        enterButton.setFocusable(false);
        enterButton.setBackground(Color.WHITE);
        enterButton.setFont(new Font("Comic Sans", Font.BOLD, 15));

        gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(employeeNumberField, gbc);

        gbc.gridx = 2;
        panel.add(enterButton, gbc);

        gbc.weightx = 1.0;
        gbc.gridx = 3;
        panel.add(Box.createHorizontalGlue(), gbc);

        return panel;
    }

    private static JPanel createResultPanel(JTextArea resultArea) {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        resultArea.setEditable(false);
        resultArea.setFont(new Font("Comic Sans", Font.BOLD, 18));
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(resultArea);

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

    private static void addPanelsToMainPanel(
            JPanel employeeMenuPanel,
            JPanel leftPanel,
            JPanel topPanel,
            JPanel resultPanel
    ) {

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.1;
        gbc.weighty = 1.0;
        gbc.gridheight = 2;
        employeeMenuPanel.add(leftPanel, gbc);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 0, 5);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.9;
        gbc.weighty = 0.0;
        employeeMenuPanel.add(topPanel, gbc);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.9;
        gbc.weighty = 1.0;
        employeeMenuPanel.add(resultPanel, gbc);
    }

    private static void addButtonActions(
            CardLayout cardLayout,
            JPanel containerPanel,
            JTextField employeeNumberField,
            JButton enterButton,
            JButton logoutButton,
            JTextArea resultArea
    ) {

        enterButton.addActionListener(e -> {

            String input = employeeNumberField.getText();

            try {

                if (input == null || input.trim().isEmpty()) {
                    DialogCustomizer.show("Please Enter Employee Number!");
                    return;
                }

                String[] record = EditEmployee.searchEmployee(input);

                if (record != null) {

                    resultArea.setText(formatEmployeeInformation(record));
                    employeeNumberField.setText("");

                } else {

                    DialogCustomizer.show("Employee Number Not Found!");
                    employeeNumberField.setText("");
                    resultArea.setText("");
                }

            } catch (IOException ex) {

                DialogCustomizer.show("Something Went Wrong!");
                employeeNumberField.setText("");
                resultArea.setText("");
            }
        });

        logoutButton.addActionListener(e -> {

            cardLayout.show(containerPanel, "Login_Screen");
            employeeNumberField.setText("");
            resultArea.setText("");
        });
    }

   private static String formatEmployeeInformation(String[] record) {

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