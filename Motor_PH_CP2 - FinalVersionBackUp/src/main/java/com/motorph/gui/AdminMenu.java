package com.motorph.gui;

import com.motorph.backend.*;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class AdminMenu {

    private static final Color NORMAL_BUTTON = Color.GRAY;
    private static final Color ACTIVE_BUTTON = new Color(70, 130, 180);

    public static JPanel createAdminMenu(CardLayout cardLayout, JPanel containerPanel) {

        JPanel adminMenuPanel = new JPanel(new GridBagLayout());
        adminMenuPanel.setBackground(Color.BLACK);

        JButton allEmployeeButton = new JButton("All Employee");
        JButton addEmployeeButton = new JButton("Add Employee");
        JButton editEmployeeButton = new JButton("Edit Employee");
        JButton deleteEmployeeButton = new JButton("Delete Employee");
        JButton computeSalaryButton = new JButton("Compute Salaries");
        JButton logoutButton = new JButton("Logout");
        JButton enterButton = new JButton("Enter");
        JButton computeSalariesButton = new JButton("Compute Salaries");
        
        

        JTextField employeeNumberField = new JTextField();
        JTextArea textArea = new JTextArea(10, 18);

        JComboBox<String> monthBox = new JComboBox<>(new String[]{
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        });;

        JComboBox<String> yearBox = new JComboBox<>(new String[]{
                "2024", "2025", "2026", "2027", "2028"
        });

        CardLayout contentCardLayout = new CardLayout();
        JPanel contentPanel = new JPanel(contentCardLayout);

        JPanel salaryPanel = new JPanel(new BorderLayout());
        salaryPanel.setBackground(Color.WHITE);

        JPanel topPanel = createTopPanel(employeeNumberField, monthBox, yearBox, enterButton);
        JPanel employeeListPanel = createEmployeeListPanel(
                monthBox,
                yearBox,
                employeeNumberField,
                textArea,
                contentCardLayout,
                contentPanel
        );

        salaryPanel.add(topPanel, BorderLayout.NORTH);
        salaryPanel.add(employeeListPanel, BorderLayout.CENTER);

        contentPanel.add(salaryPanel, "Salary_Report");
        contentPanel.add(createCenterPanel(textArea), "Salary_Breakout");

        JPanel leftPanel = createLeftPanel(
        allEmployeeButton,
        addEmployeeButton,
        editEmployeeButton,
        deleteEmployeeButton,
        computeSalaryButton,
        logoutButton
        );

        addPanelsToAdminMenu(adminMenuPanel, leftPanel, contentPanel);

        setActiveButton(
                allEmployeeButton,
                allEmployeeButton,
                addEmployeeButton,
                editEmployeeButton,
                deleteEmployeeButton,
                computeSalaryButton
        );

        addButtonActions(
                cardLayout,
                containerPanel,
                employeeNumberField,
                monthBox,
                yearBox,
                enterButton,
                logoutButton,
                allEmployeeButton,
                addEmployeeButton,
                editEmployeeButton,
                deleteEmployeeButton,
                computeSalaryButton,
                textArea,
                contentCardLayout,
                contentPanel
        );

        return adminMenuPanel;
    }

    private static JPanel createLeftPanel(
        JButton allEmployeeButton,
        JButton addEmployeeButton,
        JButton editEmployeeButton,
        JButton deleteEmployeeButton,
        JButton computeSalaryButton,
        JButton logoutButton
        ) {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        setupSideButton(allEmployeeButton);
        setupSideButton(addEmployeeButton);
        setupSideButton(editEmployeeButton);
        setupSideButton(deleteEmployeeButton);
        setupSideButton(computeSalaryButton);
        setupSideButton(logoutButton);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.anchor = GridBagConstraints.NORTH;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(allEmployeeButton, gbc);

        gbc.gridy = 1;
        panel.add(addEmployeeButton, gbc);

        gbc.gridy = 2;
        panel.add(editEmployeeButton, gbc);

        gbc.gridy = 3;
        panel.add(deleteEmployeeButton, gbc);

        gbc.gridy = 4;
        panel.add(computeSalaryButton, gbc);

        gbc.gridy = 5;
        panel.add(logoutButton, gbc);

        gbc.weighty = 1.0;
        gbc.gridy = 6;
        panel.add(Box.createVerticalGlue(), gbc);

        return panel;
    }

    private static void setupSideButton(JButton button) {
        button.setPreferredSize(new Dimension(150, 30));
        button.setFocusable(false);
        button.setBackground(NORMAL_BUTTON);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Comic Sans", Font.BOLD, 13));
    }

private static void setActiveButton(
        JButton activeButton,
        JButton allEmployeeButton,
        JButton addEmployeeButton,
        JButton editEmployeeButton,
        JButton deleteEmployeeButton,
        JButton computeSalaryButton
) {

    allEmployeeButton.setBackground(NORMAL_BUTTON);
    addEmployeeButton.setBackground(NORMAL_BUTTON);
    editEmployeeButton.setBackground(NORMAL_BUTTON);
    deleteEmployeeButton.setBackground(NORMAL_BUTTON);
    computeSalaryButton.setBackground(NORMAL_BUTTON);

    allEmployeeButton.setForeground(Color.BLACK);
    addEmployeeButton.setForeground(Color.BLACK);
    editEmployeeButton.setForeground(Color.BLACK);
    deleteEmployeeButton.setForeground(Color.BLACK);
    computeSalaryButton.setForeground(Color.BLACK);

    activeButton.setBackground(ACTIVE_BUTTON);
    activeButton.setForeground(Color.WHITE);
}

    private static JPanel createTopPanel(
            JTextField employeeNumberField,
            JComboBox<String> monthBox,
            JComboBox<String> yearBox,
            JButton enterButton
        ) {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel("Please Enter Employee No. :");
        label.setFont(new Font("Comic Sans", Font.BOLD, 15));

        employeeNumberField.setPreferredSize(new Dimension(200, 30));
        employeeNumberField.setFont(new Font("Comic Sans", Font.BOLD, 15));

        monthBox.setFont(new Font("Comic Sans", Font.BOLD, 15));
        yearBox.setFont(new Font("Comic Sans", Font.BOLD, 15));

        enterButton.setPreferredSize(new Dimension(100, 30));
        enterButton.setFocusable(false);
        enterButton.setBackground(Color.WHITE);
        enterButton.setFont(new Font("Comic Sans", Font.BOLD, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(employeeNumberField, gbc);

        gbc.gridx = 2;
        panel.add(monthBox, gbc);

        gbc.gridx = 3;
        panel.add(yearBox, gbc);

        gbc.gridx = 4;
        panel.add(enterButton, gbc);

        gbc.weightx = 1.0;
        gbc.gridx = 5;
        panel.add(Box.createHorizontalGlue(), gbc);

        return panel;
    }

    private static JPanel createCenterPanel(JTextArea textArea) {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        textArea.setEditable(false);
        textArea.setFont(new Font("Comic Sans", Font.BOLD, 20));

        JScrollPane scrollPane = new JScrollPane(textArea);

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

    private static void addPanelsToAdminMenu(
            JPanel adminMenuPanel,
            JPanel leftPanel,
            JPanel contentPanel
    ) {

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.1;
        gbc.weighty = 1.0;
        adminMenuPanel.add(leftPanel, gbc);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.9;
        gbc.weighty = 1.0;
        adminMenuPanel.add(contentPanel, gbc);
    }

    private static void addButtonActions(
            CardLayout cardLayout,
            JPanel containerPanel,
            JTextField employeeNumberField,
            JComboBox<String> monthBox,
            JComboBox<String> yearBox,
            JButton enterButton,
            JButton logoutButton,
            JButton allEmployeeButton,
            JButton addEmployeeButton,
            JButton editEmployeeButton,
            JButton deleteEmployeeButton,
            JButton computeSalaryButton,
            JTextArea textArea,
            CardLayout contentCardLayout,
            JPanel contentPanel
    ) {

        enterButton.addActionListener(e -> {

            String input = employeeNumberField.getText();

            if (input == null || input.trim().isEmpty()) {
                DialogCustomizer.show("Please Enter Employee Number!");
                return;
            }

        int monthValue = getMonthValue((String) monthBox.getSelectedItem());
        int yearValue = Integer.parseInt((String) yearBox.getSelectedItem());

      

        showSalaryReport(
            input,
            monthValue,
            yearValue,
            textArea
        );
            contentCardLayout.show(contentPanel, "Salary_Breakout");

            setActiveButton(
                    allEmployeeButton,
                    allEmployeeButton,
                    addEmployeeButton,
                    editEmployeeButton,
                    deleteEmployeeButton,
                    computeSalaryButton
            );
        });

         allEmployeeButton.addActionListener(e -> {

    setActiveButton(
        allEmployeeButton,
        allEmployeeButton,
        addEmployeeButton,
        editEmployeeButton,
        deleteEmployeeButton,
        computeSalaryButton
        );

    contentPanel.removeAll();

    JPanel salaryPanel = new JPanel(new BorderLayout());
    salaryPanel.setBackground(Color.WHITE);

    JPanel topPanel = createTopPanel(employeeNumberField, monthBox, yearBox, enterButton);

    JPanel employeeListPanel = createEmployeeListPanel(
            monthBox,
            yearBox,
            employeeNumberField,
            textArea,
            contentCardLayout,
            contentPanel
    );

    salaryPanel.add(topPanel, BorderLayout.NORTH);
    salaryPanel.add(employeeListPanel, BorderLayout.CENTER);

    contentPanel.add(salaryPanel, "Salary_Report");
    contentPanel.add(createCenterPanel(textArea), "Salary_Breakout");

    contentPanel.revalidate();
    contentPanel.repaint();

    contentCardLayout.show(contentPanel, "Salary_Report");
});
     
     

        addEmployeeButton.addActionListener(e -> {

        setActiveButton(
            addEmployeeButton,
            allEmployeeButton,
            addEmployeeButton,
            editEmployeeButton,
            deleteEmployeeButton,
            computeSalaryButton
        );

            JPanel addPanel = createAddEmployeePanel();
            contentPanel.add(addPanel, "Add_Employee");
            contentCardLayout.show(contentPanel, "Add_Employee");
        });

        editEmployeeButton.addActionListener(e -> {

            setActiveButton(
                    editEmployeeButton,
                    allEmployeeButton,
                    addEmployeeButton,
                    editEmployeeButton,
                    deleteEmployeeButton,
                    computeSalaryButton
            );

            JPanel editPanel = createEditEmployeePanel();
            contentPanel.add(editPanel, "Edit_Employee");
            contentCardLayout.show(contentPanel, "Edit_Employee");
        });

        deleteEmployeeButton.addActionListener(e -> {

            setActiveButton(
                    deleteEmployeeButton,
                    allEmployeeButton,
                    addEmployeeButton,
                    editEmployeeButton,
                    deleteEmployeeButton,
                    computeSalaryButton
            );

            JPanel deletePanel = createDeleteEmployeePanel();
            contentPanel.add(deletePanel, "Delete_Employee");
            contentCardLayout.show(contentPanel, "Delete_Employee");
        });
        
        computeSalaryButton.addActionListener(e -> {

            setActiveButton(
                    computeSalaryButton,
                    allEmployeeButton,
                    addEmployeeButton,
                    editEmployeeButton,
                    deleteEmployeeButton,
                    computeSalaryButton
            );

            JPanel computePanel = createComputeSalariesPanel(
                    monthBox,
                    yearBox,
                    textArea,
                    contentCardLayout,
                    contentPanel
            );

            contentPanel.add(computePanel, "Compute_Salaries");
            contentCardLayout.show(contentPanel, "Compute_Salaries");
        });

        

        logoutButton.addActionListener(e -> {
            cardLayout.show(containerPanel, "Login_Screen");
            employeeNumberField.setText("");
            textArea.setText("");
            contentCardLayout.show(contentPanel, "Salary_Report");

            setActiveButton(
                addEmployeeButton,
                allEmployeeButton,
                addEmployeeButton,
                editEmployeeButton,
                deleteEmployeeButton,
                computeSalaryButton
        );
        });
    }
    private static JPanel createAllEmployeePanel(
            int monthValue,
            JTextField employeeNumberField,
            JTextArea textArea,
            CardLayout contentCardLayout,
            JPanel contentPanel
    ) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        try {
            String[] columnNames = {
                    "ID", "Last Name", "First Name", "Birth Date",
                    "Hours", "Gross Salary", "Net Salary", "Tax"
            };

            String[][] employeeData = GetList.getTableData(monthValue);

            JTable table = new JTable(employeeData, columnNames);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.setFont(new Font("Arial", Font.PLAIN, 13));
            table.setRowHeight(22);

            JScrollPane scrollPane = new JScrollPane(table);
            JButton viewButton = new JButton("View Selected Employee Salary Breakout");

            panel.add(scrollPane, BorderLayout.CENTER);
            panel.add(viewButton, BorderLayout.SOUTH);

            viewButton.addActionListener(e -> {

                int selectedRow = table.getSelectedRow();

                if (selectedRow == -1) {
                    DialogCustomizer.show("Please select an employee first.");
                    return;
                }

                String employeeId = table.getValueAt(selectedRow, 0).toString();

                employeeNumberField.setText(employeeId);
                showSalaryReport(employeeId, monthValue, 2024, textArea);
                contentCardLayout.show(contentPanel, "Salary_Report");
            });

        } catch (IOException ex) {
            DialogCustomizer.show("Unable to load employee payroll list.");
        }

        return panel;
    }

private static JPanel createAddEmployeePanel() {

    JPanel mainPanel = new JPanel(new GridBagLayout());
    mainPanel.setBackground(Color.WHITE);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(4, 20, 4, 10);
    gbc.anchor = GridBagConstraints.WEST;

    JButton saveButton = new JButton("Add Employee");

    JComponent[] fields = createEmployeeFields(false);
    String[] labels = getAddEmployeeInputLabels();

    try {
        setComponentValue(fields[0], AddEmployee.generateNextEmployeeNumber());
        fields[0].setEnabled(false);
    } catch (IOException ex) {
        DialogCustomizer.show("Unable to generate Employee Number.");
    }

    makeSalaryAutoComputed(fields);

    for (int i = 0; i < labels.length; i++) {
        gbc.gridx = 0;
        gbc.gridy = i;
        mainPanel.add(new JLabel(labels[i]), gbc);

        gbc.gridx = 1;
        mainPanel.add(fields[i], gbc);
    }

    gbc.gridx = 1;
    gbc.gridy = labels.length;
    mainPanel.add(saveButton, gbc);

    saveButton.addActionListener(e -> {

        updateComputedSalaryFields(fields);

        String[] newEmployeeData = getFieldData(fields);

        String validationMessage = AddEmployee.validateEmployeeData(newEmployeeData);

        if (!validationMessage.equals("valid")) {
            DialogCustomizer.show(validationMessage);
            return;
        }

        try {
            if (AddEmployee.employeeExists(newEmployeeData[0])) {
                DialogCustomizer.show("Employee Already Exists.");
                return;
            }

            AddEmployee.addEmployee(newEmployeeData);
            GetList.sortCsvByEmployeeNumber();

            JOptionPane.showMessageDialog(
                    null,
                    "Employee Added Successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            clearFields(fields);

            setComponentValue(fields[0], AddEmployee.generateNextEmployeeNumber());
            fields[0].setEnabled(false);

            makeSalaryAutoComputed(fields);

        } catch (IOException ex) {
            DialogCustomizer.show("Something went wrong while adding employee.");
        }
    });

    return mainPanel;
}

   private static JPanel createEditEmployeePanel() {

    JPanel mainPanel = new JPanel(new GridBagLayout());
    mainPanel.setBackground(Color.WHITE);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(4, 20, 4, 10);
    gbc.anchor = GridBagConstraints.WEST;

    JTextField searchField = new JTextField();
    searchField.setPreferredSize(new Dimension(220, 23));

    JButton searchButton = new JButton("Search");
    JButton saveButton = new JButton("Save Changes");
    saveButton.setEnabled(false);

    JComponent[] fields = createEmployeeFieldsWithoutEmployeeNumber(true);
    String[] labels = getEditEmployeeInputLabels();

    makeSalaryAutoComputedForEdit(fields);

    gbc.gridx = 0;
    gbc.gridy = 0;
    mainPanel.add(new JLabel("Enter Employee # (5 digits):"), gbc);

    gbc.gridx = 1;
    mainPanel.add(searchField, gbc);

    gbc.gridx = 2;
    mainPanel.add(searchButton, gbc);

    for (int i = 0; i < labels.length; i++) {
        gbc.gridx = 0;
        gbc.gridy = i + 1;
        mainPanel.add(new JLabel(labels[i]), gbc);

        gbc.gridx = 1;
        mainPanel.add(fields[i], gbc);
    }

    gbc.gridx = 1;
    gbc.gridy = labels.length + 1;
    mainPanel.add(saveButton, gbc);

    searchButton.addActionListener(e -> {

        String empNo = searchField.getText();

        if (!empNo.matches("\\d{5}")) {
            DialogCustomizer.show("Invalid ID. Employee # must be exactly 5 digits.");
            return;
        }

        try {
            String[] record = EditEmployee.searchEmployee(empNo);

            if (record == null) {
                DialogCustomizer.show("Employee Not Found.");
                return;
            }

            for (int i = 0; i < fields.length; i++) {
                setComponentValue(fields[i], record[i + 1]);
                fields[i].setEnabled(true);
            }

            makeSalaryAutoComputedForEdit(fields);
            updateComputedSalaryFieldsForEdit(fields);

            saveButton.setEnabled(true);

        } catch (IOException ex) {
            DialogCustomizer.show("Something went wrong while searching employee.");
        }
    });

    saveButton.addActionListener(e -> {

        updateComputedSalaryFieldsForEdit(fields);

        String[] updatedData = new String[19];
        updatedData[0] = searchField.getText();

        for (int i = 0; i < fields.length; i++) {
            updatedData[i + 1] = getComponentValue(fields[i]);
        }

        String validationMessage = EditEmployee.validateEditedFields(updatedData);

        if (!validationMessage.equals("valid")) {
            DialogCustomizer.show(validationMessage);
            return;
        }

        try {
            boolean updated = EditEmployee.updateEmployee(updatedData);

            if (updated) {
                GetList.sortCsvByEmployeeNumber();

                JOptionPane.showMessageDialog(
                        null,
                        "Employee Updated Successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                DialogCustomizer.show("Employee Not Found.");
            }

        } catch (IOException ex) {
            DialogCustomizer.show("Something went wrong while updating employee.");
        }
    });

    return mainPanel;
}

    private static JPanel createDeleteEmployeePanel() {

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 20, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField empNoField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField firstNameField = new JTextField();
        JFormattedTextField birthdayField = createMaskedField("##/##/####");

        empNoField.setPreferredSize(new Dimension(220, 25));
        lastNameField.setPreferredSize(new Dimension(220, 25));
        firstNameField.setPreferredSize(new Dimension(220, 25));
        birthdayField.setPreferredSize(new Dimension(220, 25));

        JTextArea resultArea = new JTextArea(10, 35);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Comic Sans", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(resultArea);

        JButton searchButton = new JButton("Search Employee");
        JButton deleteButton = new JButton("Delete Employee");
        deleteButton.setEnabled(false);

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Employee # (5 digits):"), gbc);

        gbc.gridx = 1;
        mainPanel.add(empNoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Last Name:"), gbc);

        gbc.gridx = 1;
        mainPanel.add(lastNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("First Name:"), gbc);

        gbc.gridx = 1;
        mainPanel.add(firstNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("Birthday (MM/DD/YYYY):"), gbc);

        gbc.gridx = 1;
        mainPanel.add(birthdayField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        mainPanel.add(searchButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(scrollPane, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(deleteButton, gbc);

        final String[] selectedEmployeeNumber = {""};
        JTable matchTable = new JTable();

        searchButton.addActionListener(e -> {

            String empNo = empNoField.getText();
            String lastName = lastNameField.getText();
            String firstName = firstNameField.getText();
            String birthday = birthdayField.getText();

            String validationMessage = DeleteEmployee.validateDeleteInput(
                    empNo,
                    lastName,
                    firstName,
                    birthday
            );

            if (!validationMessage.equals("valid")) {
                DialogCustomizer.show(validationMessage);
                deleteButton.setEnabled(false);
                resultArea.setText("");
                selectedEmployeeNumber[0] = "";
                return;
            }

            try {
                ArrayList<String[]> matches =
                        DeleteEmployee.searchMatchingEmployees(
                                empNo,
                                lastName,
                                firstName,
                                birthday
                        );

                if (matches.isEmpty()) {
                    DialogCustomizer.show("Employee Not Found.");
                    deleteButton.setEnabled(false);
                    resultArea.setText("");
                    selectedEmployeeNumber[0] = "";
                    return;
                }

                if (matches.size() == 1) {

                    String[] record = matches.get(0);
                    selectedEmployeeNumber[0] = record[0];

                    resultArea.setText(
                            "Employee Found\n" +
                            "============================\n" +
                            "Employee #: " + record[0] + "\n" +
                            "Last Name: " + record[1] + "\n" +
                            "First Name: " + record[2] + "\n" +
                            "Birthday: " + record[3] + "\n" +
                            "Address: " + record[4] + "\n" +
                            "Phone Number: " + record[5] + "\n" +
                            "Status: " + record[10] + "\n" +
                            "Position: " + record[11] + "\n" +
                            "Immediate Supervisor: " + record[12]
                    );

                    deleteButton.setEnabled(true);
                    return;
                }

                String[] columns = {
                        "Employee #",
                        "Last Name",
                        "First Name",
                        "Birthday",
                        "Status",
                        "Position"
                };

                String[][] data = new String[matches.size()][6];

                for (int i = 0; i < matches.size(); i++) {
                    String[] record = matches.get(i);

                    data[i][0] = record[0];
                    data[i][1] = record[1];
                    data[i][2] = record[2];
                    data[i][3] = record[3];
                    data[i][4] = record[10];
                    data[i][5] = record[11];
                }

                matchTable.setModel(
                        new javax.swing.table.DefaultTableModel(data, columns)
                );

                int selectedOption = JOptionPane.showConfirmDialog(
                        null,
                        new JScrollPane(matchTable),
                        "Multiple Employees Found - Select One",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

                if (selectedOption == JOptionPane.OK_OPTION) {

                    int selectedRow = matchTable.getSelectedRow();

                    if (selectedRow == -1) {
                        DialogCustomizer.show("Please select an employee from the table.");
                        deleteButton.setEnabled(false);
                        return;
                    }

                    selectedEmployeeNumber[0] =
                            matchTable.getValueAt(selectedRow, 0).toString();

                    resultArea.setText(
                            "Selected Employee for Deletion\n" +
                            "============================\n" +
                            "Employee #: " + matchTable.getValueAt(selectedRow, 0) + "\n" +
                            "Last Name: " + matchTable.getValueAt(selectedRow, 1) + "\n" +
                            "First Name: " + matchTable.getValueAt(selectedRow, 2) + "\n" +
                            "Birthday: " + matchTable.getValueAt(selectedRow, 3) + "\n" +
                            "Status: " + matchTable.getValueAt(selectedRow, 4) + "\n" +
                            "Position: " + matchTable.getValueAt(selectedRow, 5)
                    );

                    deleteButton.setEnabled(true);
                }

            } catch (IOException ex) {
                DialogCustomizer.show("Something went wrong while searching employee.");
            }
        });

        deleteButton.addActionListener(e -> {

            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Are you sure you want to delete Employee # " +
                            selectedEmployeeNumber[0] + "?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                DialogCustomizer.show("Delete operation cancelled.");
                return;
            }

            try {
                boolean deleted = DeleteEmployee.deleteEmployee(selectedEmployeeNumber[0]);
                
                DeleteEmployee.deleteEmployeeAttendance(selectedEmployeeNumber[0]);

                if (deleted) {
                    
                    GetList.sortCsvByEmployeeNumber();
                    JOptionPane.showMessageDialog(
                    null,
                    "Employee Deleted Successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                     );

                    empNoField.setText("");
                    lastNameField.setText("");
                    firstNameField.setText("");
                    birthdayField.setText("");
                    resultArea.setText("");
                    deleteButton.setEnabled(false);
                    selectedEmployeeNumber[0] = "";

                } else {
                    DialogCustomizer.show("Employee Not Found.");
                }

            } catch (IOException ex) {
                DialogCustomizer.show("Something went wrong while deleting employee.");
            }
        });

        return mainPanel;
    }
    private static JFormattedTextField createMaskedField(String mask) {

        try {
            MaskFormatter formatter = new MaskFormatter(mask);
            formatter.setPlaceholderCharacter('_');

            JFormattedTextField field =
                    new JFormattedTextField(formatter);

            field.setPreferredSize(new Dimension(220,25));

            return field;

        } catch (ParseException ex) {
            return new JFormattedTextField();
        }
    }

    private static String getComponentValue(JComponent component) {

        if(component instanceof JComboBox) {
            return String.valueOf(
                    ((JComboBox<?>) component).getSelectedItem()
            );
        }

        if(component instanceof JTextField) {
            return ((JTextField) component).getText();
        }

        return "";
    }

    private static void setComponentValue(
            JComponent component,
            String value
    ) {

        if(component instanceof JComboBox) {
            ((JComboBox<?>) component).setSelectedItem(value);
        }

        if(component instanceof JTextField) {
            ((JTextField) component).setText(value);
        }
    }

    private static void clearFields(JComponent[] fields) {

        for(JComponent field : fields) {

            if(field instanceof JTextField) {
                ((JTextField) field).setText("");
            }

            if(field instanceof JComboBox) {
                ((JComboBox<?>) field).setSelectedIndex(0);
            }
        }
    }

    private static int getMonthValue(String month) {

        switch(month) {
            case "January": return 1;
            case "February": return 2;
            case "March": return 3;
            case "April": return 4;
            case "May": return 5;
            case "June": return 6;
            case "July": return 7;
            case "August": return 8;
            case "September": return 9;
            case "October": return 10;
            case "November": return 11;
            case "December": return 12;
            default: return 1;
        }
    }

    private static void showSalaryReport(
            String employeeNumber,
            int monthValue,
            int yearValue,
            JTextArea textArea
    ) {
        try {
            String[] record = EditEmployee.searchEmployee(employeeNumber);

            if (record == null) {
                DialogCustomizer.show("Employee Number Not Found!");
                return;
            }

            Admin payroll = ProcessPayroll.admin(employeeNumber, monthValue, yearValue);

            String employeeInfo =
                    "EMPLOYEE INFORMATION\n" +
                    "====================================\n" +
                    "Employee ID          : " + record[0] + "\n" +
                    "Full Name            : " + record[2] + " " + record[1] + "\n" +
                    "Birth Date           : " + record[3] + "\n" +
                    "Address              : " + record[4] + "\n" +
                    "Phone Number         : " + record[5] + "\n" +
                    "Status               : " + record[10] + "\n" +
                    "Position             : " + record[11] + "\n" +
                    "Immediate Supervisor : " + record[12] + "\n\n";

            if (payroll == null) {
                textArea.setText(
                        employeeInfo +
                        "PAYROLL REPORT\n" +
                        "====================================\n" +
                        "Selected Period      : " + ProcessPayroll.month(monthValue) + " " + yearValue + "\n" +
                        "No attendance/payroll record found for this employee in the selected period."
                );
                return;
            }

            double totalDeductions =
                payroll.getSSS()
                + payroll.getphilHealth()
                + payroll.getpagIbig()
                + payroll.gettax();

            double firstNetSalary =
                payroll.getgrossSalary();

            double secondNetSalary =
                payroll.getgrossSalary2() - totalDeductions;

            double totalNetSalary =
                    firstNetSalary + secondNetSalary;

        textArea.setText(
                employeeInfo +
                "PAYROLL REPORT\n" +
                "====================================\n" +
                "Selected Period      : " + ProcessPayroll.month(monthValue) + " " + yearValue + "\n\n" +

                "FIRST CUTOFF\n" +
                "====================================\n" +
                "Cutoff Date          : " + payroll.getcutOff() + "\n" +
                "Total Hours Worked   : " + String.format("%.2f", payroll.gettotalHours()) + "\n" +
                "Gross Salary         : " + String.format("%.2f", payroll.getgrossSalary()) + "\n" +
                "Net Salary           : " + String.format("%.2f", firstNetSalary) + "\n\n" +

                "SECOND CUTOFF\n" +
                "====================================\n" +
                "Cutoff Date          : " + payroll.getcutOff2() + " (Second payout includes all deductions)\n" +
                "Total Hours Worked   : " + String.format("%.2f", payroll.gettotalHours2()) + "\n" +
                "Gross Salary         : " + String.format("%.2f", payroll.getgrossSalary2()) + "\n\n" +

                "DEDUCTIONS\n" +
                "====================================\n" +
                "SSS                  : " + String.format("%.2f", payroll.getSSS()) + "\n" +
                "PhilHealth           : " + String.format("%.2f", payroll.getphilHealth()) + "\n" +
                "Pag-IBIG             : " + String.format("%.2f", payroll.getpagIbig()) + "\n" +
                "Tax                  : " + String.format("%.2f", payroll.gettax()) + "\n" +
                "Total Deductions     : " + String.format("%.2f", totalDeductions) + "\n\n" +

                "Net Salary           : " + String.format("%.2f", secondNetSalary) + "\n\n" +

                "MONTHLY SUMMARY\n" +
                "====================================\n" +
                "Total Net Salary     : " + String.format("%.2f", totalNetSalary)
        );
        } catch (IOException ex) {
            DialogCustomizer.show("Unable to load employee information.");
        }
    }

    private static String[] getAddEmployeeInputLabels() {

        return new String[]{
                "Employee # (Auto-generated):",
                "Last Name:",
                "First Name:",
                "Birthday (MM/DD/YYYY):",
                "Address:",
                "Phone Number:",
                "SSS Number:",
                "PhilHealth Number:",
                "TIN Number:",
                "Pag-IBIG Number:",
                "Status:",
                "Position:",
                "Immediate Supervisor:",
                "Basic Salary:",
                "Rice Subsidy:",
                "Phone Allowance:",
                "Clothing Allowance:",
                "Gross Semi-monthly Rate:",
                "Hourly Rate:"
            };
        }
    private static String[] getEditEmployeeInputLabels() {

        return new String[]{
            "Last Name:",
            "First Name:",
            "Birthday:",
            "Address:",
            "Phone Number:",
            "SSS Number:",
            "PhilHealth Number:",
            "TIN Number:",
            "Pag-IBIG Number:",
            "Status:",
            "Position:",
            "Immediate Supervisor:",
            "Basic Salary:",
            "Rice Subsidy:",
            "Phone Allowance:",
            "Clothing Allowance:",
            "Gross Semi-monthly Rate:",
            "Hourly Rate:"
        };
    }
    
    private static String[] getFieldData(JComponent[] fields) {

        String[] data = new String[fields.length];

        for (int i = 0; i < fields.length; i++) {
            data[i] = getComponentValue(fields[i]);
        }

        return data;
    }
    
    private static JComponent[] createEmployeeFields(boolean locked) {

        JComponent[] fields = new JComponent[19];

        fields[0] = new JTextField();
        fields[1] = new JTextField();
        fields[2] = new JTextField();
        fields[3] = createMaskedField("##/##/####");
        fields[4] = new JTextField();
        fields[5] = createMaskedField("###-###-###");
        fields[6] = createMaskedField("##-#######-#");
        fields[7] = createMaskedField("############");
        fields[8] = createMaskedField("###-###-###-###");
        fields[9] = createMaskedField("############");
        fields[10] = new JComboBox<>(getStatusOptions());
        fields[11] = new JComboBox<>(getPositionOptions());
        fields[12] = new JComboBox<>(getSupervisorOptions());
        fields[13] = new JTextField();
        fields[14] = new JTextField();
        fields[15] = new JTextField();
        fields[16] = new JTextField();
        fields[17] = new JTextField();
        fields[18] = new JTextField();

        for (int i = 0; i < fields.length; i++) {
            fields[i].setPreferredSize(new Dimension(220, 23));
            fields[i].setEnabled(!locked);
        }

        return fields;
    }
    
    private static JComponent[] createEmployeeFieldsWithoutEmployeeNumber(boolean locked) {

        JComponent[] fields = new JComponent[18];

        fields[0] = new JTextField();
        fields[1] = new JTextField();
        fields[2] = createMaskedField("##/##/####");
        fields[3] = new JTextField();
        fields[4] = createMaskedField("###-###-###");
        fields[5] = createMaskedField("##-#######-#");
        fields[6] = createMaskedField("############");
        fields[7] = createMaskedField("###-###-###-###");
        fields[8] = createMaskedField("############");
        fields[9] = new JComboBox<>(getStatusOptions());
        fields[10] = new JComboBox<>(getPositionOptions());
        fields[11] = new JComboBox<>(getSupervisorOptions());
        fields[12] = new JTextField();
        fields[13] = new JTextField();
        fields[14] = new JTextField();
        fields[15] = new JTextField();
        fields[16] = new JTextField();
        fields[17] = new JTextField();

        for (int i = 0; i < fields.length; i++) {
            fields[i].setPreferredSize(new Dimension(220, 23));
            fields[i].setEnabled(!locked);
        }

        return fields;
    }
    
    private static String[] getStatusOptions() {

        try {
            return GetList.getUniqueCsvValues(10);
        } catch (IOException ex) {
            return new String[]{"Regular", "Probationary"};
        }
    }

    private static String[] getPositionOptions() {

        try {
            return GetList.getUniqueCsvValues(11);
        } catch (IOException ex) {
            return new String[]{"N/A"};
        }
    }

    private static String[] getSupervisorOptions() {

        try {
            return GetList.getSupervisorNames();
        } catch (IOException ex) {
            return new String[]{"N/A"};
        }
    }

    private static JPanel createEmployeeListPanel(
            JComboBox<String> monthBox,
            JComboBox<String> yearBox,
            JTextField employeeNumberField,
            JTextArea textArea,
            CardLayout contentCardLayout,
            JPanel contentPanel
    ) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        try {
            String[] columnNames = {
                    "ID",
                    "Last Name",
                    "First Name",
                    "Birth Date",
                    "Status",
                    "Position"
            };

            String[][] tableData = GetList.getBasicEmployeeTableData();

            JTable table = new JTable(tableData, columnNames);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.setFont(new Font("Arial", Font.PLAIN, 13));
            table.setRowHeight(22);

            JScrollPane scrollPane = new JScrollPane(table);

            JButton viewButton = new JButton("View Selected Employee Salary Breakout");

            panel.add(scrollPane, BorderLayout.CENTER);
            panel.add(viewButton, BorderLayout.SOUTH);

            viewButton.addActionListener(e -> {

                int selectedRow = table.getSelectedRow();

                if (selectedRow == -1) {
                    DialogCustomizer.show("Please select an employee first.");
                    return;
                }

                String employeeId = table.getValueAt(selectedRow, 0).toString();
                int monthValue = getMonthValue((String) monthBox.getSelectedItem());
                int yearValue = Integer.parseInt((String) yearBox.getSelectedItem());

                employeeNumberField.setText(employeeId);
               

            showSalaryReport(
                employeeId,
                monthValue,
                yearValue,
                textArea
            );
              contentCardLayout.show(contentPanel, "Salary_Breakout");
            });

        } catch (IOException ex) {
            DialogCustomizer.show("Unable to load employee list.");
        }

        return panel;
    }
    
    private static void makeSalaryAutoComputed(JComponent[] fields) {

    JTextField basicSalaryField = (JTextField) fields[13];
    JTextField grossSemiMonthlyField = (JTextField) fields[17];
    JTextField hourlyRateField = (JTextField) fields[18];

    grossSemiMonthlyField.setEditable(false);
    hourlyRateField.setEditable(false);
    grossSemiMonthlyField.setBackground(new Color(235, 235, 235));
    hourlyRateField.setBackground(new Color(235, 235, 235));

    basicSalaryField.getDocument().addDocumentListener(new DocumentListener() {
        public void insertUpdate(DocumentEvent e) {
            updateComputedSalaryFields(fields);
        }

        public void removeUpdate(DocumentEvent e) {
            updateComputedSalaryFields(fields);
        }

        public void changedUpdate(DocumentEvent e) {
            updateComputedSalaryFields(fields);
        }
    });
}

private static void makeSalaryAutoComputedForEdit(JComponent[] fields) {

    JTextField basicSalaryField = (JTextField) fields[12];
    JTextField grossSemiMonthlyField = (JTextField) fields[16];
    JTextField hourlyRateField = (JTextField) fields[17];

    grossSemiMonthlyField.setEditable(false);
    hourlyRateField.setEditable(false);
    grossSemiMonthlyField.setBackground(new Color(235, 235, 235));
    hourlyRateField.setBackground(new Color(235, 235, 235));

    basicSalaryField.getDocument().addDocumentListener(new DocumentListener() {
        public void insertUpdate(DocumentEvent e) {
            updateComputedSalaryFieldsForEdit(fields);
        }

        public void removeUpdate(DocumentEvent e) {
            updateComputedSalaryFieldsForEdit(fields);
        }

        public void changedUpdate(DocumentEvent e) {
            updateComputedSalaryFieldsForEdit(fields);
        }
    });
}

private static void updateComputedSalaryFields(JComponent[] fields) {

    JTextField basicSalaryField = (JTextField) fields[13];
    JTextField grossSemiMonthlyField = (JTextField) fields[17];
    JTextField hourlyRateField = (JTextField) fields[18];

    String basicSalaryText = basicSalaryField.getText().replace(",", "").trim();

    if (!basicSalaryText.matches("\\d+(\\.\\d+)?")) {
        grossSemiMonthlyField.setText("");
        hourlyRateField.setText("");
        return;
    }

    double basicSalary = Double.parseDouble(basicSalaryText);
    double grossSemiMonthly = basicSalary / 2.0;
    double hourlyRate = basicSalary / 176.0;

    grossSemiMonthlyField.setText(String.format("%.2f", grossSemiMonthly));
    hourlyRateField.setText(String.format("%.2f", hourlyRate));
}

private static void updateComputedSalaryFieldsForEdit(JComponent[] fields) {

    JTextField basicSalaryField = (JTextField) fields[12];
    JTextField grossSemiMonthlyField = (JTextField) fields[16];
    JTextField hourlyRateField = (JTextField) fields[17];

    String basicSalaryText = basicSalaryField.getText().replace(",", "").trim();

    if (!basicSalaryText.matches("\\d+(\\.\\d+)?")) {
        grossSemiMonthlyField.setText("");
        hourlyRateField.setText("");
        return;
    }

    double basicSalary = Double.parseDouble(basicSalaryText);
    double grossSemiMonthly = basicSalary / 2.0;
    double hourlyRate = basicSalary / 176.0;

    grossSemiMonthlyField.setText(String.format("%.2f", grossSemiMonthly));
    hourlyRateField.setText(String.format("%.2f", hourlyRate));
}

private static JPanel createComputeSalariesPanel(
        JComboBox<String> monthBox,
        JComboBox<String> yearBox,
        JTextArea textArea,
        CardLayout contentCardLayout,
        JPanel contentPanel
) {

    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBackground(Color.WHITE);

    JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    topButtonPanel.setBackground(Color.WHITE);

    JButton attendanceEntryButton = new JButton("Attendance Entry");
    JButton attendanceViewerButton = new JButton("Attendance Viewer");
    JButton computeAllButton = new JButton("Compute All Salaries");

    JComboBox<String> localMonthBox = new JComboBox<>(new String[]{
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    });

    JComboBox<String> localYearBox = new JComboBox<>(new String[]{
            "2024", "2025", "2026", "2027", "2028"
    });

    topButtonPanel.add(new JLabel("Month:"));
    topButtonPanel.add(localMonthBox);
    topButtonPanel.add(new JLabel("Year:"));
    topButtonPanel.add(localYearBox);
    topButtonPanel.add(attendanceEntryButton);
    topButtonPanel.add(attendanceViewerButton);
    topButtonPanel.add(computeAllButton);

    CardLayout computeCardLayout = new CardLayout();
    JPanel computeContentPanel = new JPanel(computeCardLayout);

    JPanel welcomePanel = new JPanel(new GridBagLayout());
    welcomePanel.setBackground(Color.WHITE);

    JLabel welcomeLabel = new JLabel(
            "<html><h2>Compute Salaries Module</h2>" +
                    "<p>Select Attendance Entry, Attendance Viewer, or Compute All Salaries.</p></html>"
    );

    welcomePanel.add(welcomeLabel);

    computeContentPanel.add(welcomePanel, "Welcome");
    computeContentPanel.add(createAttendanceEntryPanel(), "Attendance_Entry");

    attendanceEntryButton.addActionListener(e -> {
        computeCardLayout.show(computeContentPanel, "Attendance_Entry");
    });

    attendanceViewerButton.addActionListener(e -> {

        int selectedMonth = getMonthValueFull((String) localMonthBox.getSelectedItem());
        int selectedYear = Integer.parseInt((String) localYearBox.getSelectedItem());

        JPanel viewerPanel = createAttendanceViewerPanel(
                selectedMonth,
                selectedYear
        );

        computeContentPanel.add(viewerPanel, "Attendance_Viewer");
        computeCardLayout.show(computeContentPanel, "Attendance_Viewer");
    });
            ActionListener refreshAttendanceViewer = e -> {

            if (!attendanceViewerButton.isEnabled()) {
                return;
            }

            int selectedMonth = getMonthValueFull(
                    (String) localMonthBox.getSelectedItem());

            int selectedYear = Integer.parseInt(
                    (String) localYearBox.getSelectedItem());

            JPanel viewerPanel = createAttendanceViewerPanel(
                    selectedMonth,
                    selectedYear
            );

            computeContentPanel.add(viewerPanel, "Attendance_Viewer");
            computeCardLayout.show(computeContentPanel, "Attendance_Viewer");
        };

        localMonthBox.addActionListener(refreshAttendanceViewer);
        localYearBox.addActionListener(refreshAttendanceViewer);
     

    computeAllButton.addActionListener(e -> {

        int selectedMonth = getMonthValueFull((String) localMonthBox.getSelectedItem());
        int selectedYear = Integer.parseInt((String) localYearBox.getSelectedItem());

        try {

            String result = ComputeSalaries.computeAllEmployeeSalaries(
                    selectedMonth,
                    selectedYear
            );

            if (result.equals("success")) {

                JOptionPane.showMessageDialog(
                        null,
                        "Salary computation completed and saved successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                JPanel salaryResultPanel =
                createComputedSalaryResultPanel(selectedMonth, selectedYear);

                computeContentPanel.add(salaryResultPanel, "Computed_Salary_Result");
                computeCardLayout.show(computeContentPanel, "Computed_Salary_Result");

            } else {

                JOptionPane.showMessageDialog(
                        null,
                        result,
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (IOException ex) {

            JOptionPane.showMessageDialog(
                    null,
                    "Unable to compute salaries.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    });

    mainPanel.add(topButtonPanel, BorderLayout.NORTH);
    mainPanel.add(computeContentPanel, BorderLayout.CENTER);

    return mainPanel;
}
 
    
        
        private static JPanel createAttendanceEntryPanel() {

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(Color.WHITE);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(6,10,6,10);
            gbc.anchor = GridBagConstraints.WEST;

            JTextField employeeNoField = new JTextField();
            employeeNoField.setPreferredSize(new Dimension(220,25));

            JFormattedTextField dateField =
                    createMaskedField("##/##/####");

            JFormattedTextField timeInField =
                    createMaskedField("##:##");

            JFormattedTextField timeOutField =
                    createMaskedField("##:##");

            JButton saveButton = new JButton("Add Attendance");

            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(new JLabel("Employee #:"), gbc);

            gbc.gridx = 1;
            panel.add(employeeNoField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            panel.add(new JLabel("Date (MM/DD/YYYY):"), gbc);

            gbc.gridx = 1;
            panel.add(dateField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            panel.add(new JLabel("Time In (HH:MM):"), gbc);

            gbc.gridx = 1;
            panel.add(timeInField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            panel.add(new JLabel("Time Out (HH:MM):"), gbc);

            gbc.gridx = 1;
            panel.add(timeOutField, gbc);

            gbc.gridx = 1;
            gbc.gridy = 4;
            panel.add(saveButton, gbc);

            saveButton.addActionListener(e -> {

                try {

                    String validation =
                            AttendanceEntry.validateAttendance(
                                    employeeNoField.getText(),
                                    dateField.getText(),
                                    timeInField.getText(),
                                    timeOutField.getText()
                            );

                    if(!validation.equals("valid")) {

                        JOptionPane.showMessageDialog(
                                null,
                                validation,
                                "Validation Error",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }

                    AttendanceEntry.addAttendance(
                            employeeNoField.getText(),
                            dateField.getText(),
                            timeInField.getText(),
                            timeOutField.getText()
                    );

                    JOptionPane.showMessageDialog(
                            null,
                            "Attendance added successfully.",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    employeeNoField.setText("");
                    dateField.setText("");
                    timeInField.setText("");
                    timeOutField.setText("");

                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(
                            null,
                            "Unable to save attendance.\n" + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            });

            return panel;
        }
        
        private static JPanel createAttendanceViewerPanel(
            int monthValue,
            int yearValue
            ) {

            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(Color.WHITE);

            JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            filterPanel.setBackground(Color.WHITE);

            JTextField employeeNoField = new JTextField();
            employeeNoField.setPreferredSize(new Dimension(160, 25));

            JButton filterButton = new JButton("Filter");

            filterPanel.add(new JLabel("Employee # (optional):"));
            filterPanel.add(employeeNoField);
            filterPanel.add(filterButton);

            String[] columns = {
                "Employee #",
                "Last Name",
                "First Name",
                "Date",
                "Time In",
                "Time Out",
                "Status"
            };

            JTable table = new JTable();
            JScrollPane scrollPane = new JScrollPane(table);

            filterButton.addActionListener(e -> {

                try {

                    String[][] data = AttendanceViewer.getAttendanceTableData(
                            employeeNoField.getText(),
                            monthValue,
                            yearValue
                    );

                    table.setModel(new javax.swing.table.DefaultTableModel(data, columns));

                } catch (IOException ex) {

                    JOptionPane.showMessageDialog(
                            null,
                            "Unable to load attendance records.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            });

            try {

                String[][] data = AttendanceViewer.getAttendanceTableData(
                        "",
                        monthValue,
                        yearValue
                );

                table.setModel(new javax.swing.table.DefaultTableModel(data, columns));

            } catch (IOException ex) {

                JOptionPane.showMessageDialog(
                        null,
                        "Unable to load attendance records.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

            panel.add(filterPanel, BorderLayout.NORTH);
            panel.add(scrollPane, BorderLayout.CENTER);

            return panel;
        }
        
        private static int getMonthValueFull(String month) {

    switch (month) {
            case "January": return 1;
            case "February": return 2;
            case "March": return 3;
            case "April": return 4;
            case "May": return 5;
            case "June": return 6;
            case "July": return 7;
            case "August": return 8;
            case "September": return 9;
            case "October": return 10;
            case "November": return 11;
            case "December": return 12;
            default: return 1;
        }
    }
        private static JPanel createComputedSalaryResultPanel(
        int monthValue,
        int yearValue) {

            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(Color.WHITE);

            String[] columns = {
                    "Employee #",
                    "Last Name",
                    "First Name",
                    "Month",
                    "Year",
                    "Total Hours",
                    "Gross Salary",
                    "Deductions",
                    "Net Salary",
                    "Status"
            };

            try {
                String[][] data = GetList.getComputedSalaryTableData(
                        monthValue,
                        yearValue
                );

                JTable table = new JTable(data, columns);
                table.setFont(new Font("Arial", Font.PLAIN, 13));
                table.setRowHeight(22);

                panel.add(new JScrollPane(table), BorderLayout.CENTER);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(
                        null,
                        "Unable to load computed salary results.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

            return panel;
        }
        
        
}
    

    
