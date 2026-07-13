package com.motorph.gui;

import com.motorph.backend.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.FileReader;
import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Creates and manages the administrator interface of the MotorPH application.
 *
 * This class provides the navigation buttons, salary report interface,
 * employee management screens, payroll computation functions, and logout
 * controls available to administrator users.
 */
public class AdminMenu {

    // Default background color used by inactive navigation buttons.
    private static final Color NORMAL_BUTTON = Color.GRAY;

    // Background color used to highlight the currently active navigation button.
    private static final Color ACTIVE_BUTTON = new Color(70, 130, 180);

    /**
     * Creates the main administrator menu panel.
     *
     * The method initializes the side-navigation buttons, employee number
     * input field, month and year selectors, salary report panels, and
     * corresponding button actions.
     *
     * @param cardLayout the main layout used to switch application screens
     * @param containerPanel the main panel containing the application screens
     * @return the completed administrator menu panel
     */
    public static JPanel createAdminMenu(CardLayout cardLayout, JPanel containerPanel) {

        // Main panel containing the side menu and administrator content area.
        JPanel adminMenuPanel = new JPanel(new GridBagLayout());
        adminMenuPanel.setBackground(Color.BLACK);

        // Create the administrator navigation buttons.
        JButton allEmployeeButton = new JButton("All Employee");
        JButton addEmployeeButton = new JButton("Add Employee");
        JButton editEmployeeButton = new JButton("Edit Employee");
        JButton deleteEmployeeButton = new JButton("Delete Employee");
        JButton computeSalaryButton = new JButton("Compute Salaries");
        JButton logoutButton = new JButton("Logout");

        // Buttons used for submitting employee and salary operations.
        JButton enterButton = new JButton("Enter");
        JButton computeSalariesButton = new JButton("Compute Salaries");
        
        // Input field used to enter an employee number.
        JTextField employeeNumberField = new JTextField();

        // Displays the detailed salary breakdown of a selected employee.
        JTextArea textArea = new JTextArea(10, 18);

        // Month selector used for attendance and payroll filtering.
        JComboBox<String> monthBox = new JComboBox<>(new String[]{
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        });;

        // Year selector used for attendance and payroll filtering.
        JComboBox<String> yearBox = new JComboBox<>(new String[]{
                "2024", "2025", "2026", "2027", "2028"
        });

        /*
         * Card layout used to switch between the salary report and
         * individual salary breakdown screens.
         */
        CardLayout contentCardLayout = new CardLayout();
        JPanel contentPanel = new JPanel(contentCardLayout);

        // Main salary report screen.
        JPanel salaryPanel = new JPanel(new BorderLayout());
        salaryPanel.setBackground(Color.WHITE);

        // Create the salary report control panel.
        JPanel topPanel = createTopPanel(
                employeeNumberField,
                monthBox,
                yearBox,
                enterButton
        );

        // Create the employee salary list panel.
        JPanel employeeListPanel = createEmployeeListPanel(
                monthBox,
                yearBox,
                employeeNumberField,
                textArea,
                contentCardLayout,
                contentPanel
        );

        // Place the controls and employee list inside the salary panel.
        salaryPanel.add(topPanel, BorderLayout.NORTH);
        salaryPanel.add(employeeListPanel, BorderLayout.CENTER);

        // Register the salary report and salary breakdown screens.
        contentPanel.add(salaryPanel, "Salary_Report");
        contentPanel.add(createCenterPanel(textArea), "Salary_Breakout");

        // Create the administrator side-navigation panel.
        JPanel leftPanel = createLeftPanel(
                allEmployeeButton,
                addEmployeeButton,
                editEmployeeButton,
                deleteEmployeeButton,
                computeSalaryButton,
                logoutButton
        );

        // Add the navigation and content panels to the administrator menu.
        addPanelsToAdminMenu(adminMenuPanel, leftPanel, contentPanel);

        // Highlight the All Employee button as the initial active screen.
        setActiveButton(
                allEmployeeButton,
                allEmployeeButton,
                addEmployeeButton,
                editEmployeeButton,
                deleteEmployeeButton,
                computeSalaryButton
        );

        // Assign actions to all administrator menu buttons.
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

    /**
     * Creates the administrator side-navigation panel.
     *
     * The buttons are arranged vertically and provide access to employee
     * management, salary computation, and logout functions.
     *
     * @param allEmployeeButton button used to display all employee records
     * @param addEmployeeButton button used to open the add employee form
     * @param editEmployeeButton button used to open the edit employee form
     * @param deleteEmployeeButton button used to open the delete employee form
     * @param computeSalaryButton button used to open salary computation
     * @param logoutButton button used to exit the administrator account
     * @return the completed side-navigation panel
     */
    private static JPanel createLeftPanel(
        JButton allEmployeeButton,
        JButton addEmployeeButton,
        JButton editEmployeeButton,
        JButton deleteEmployeeButton,
        JButton computeSalaryButton,
        JButton logoutButton
        ) {

        // Panel containing the administrator navigation buttons.
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        // Apply consistent styling to each navigation button.
        setupSideButton(allEmployeeButton);
        setupSideButton(addEmployeeButton);
        setupSideButton(editEmployeeButton);
        setupSideButton(deleteEmployeeButton);
        setupSideButton(computeSalaryButton);
        setupSideButton(logoutButton);

        // Configure the placement of the navigation buttons.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.anchor = GridBagConstraints.NORTH;

        // Place the All Employee button in the first row.
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(allEmployeeButton, gbc);

        // Place the Add Employee button in the second row.
        gbc.gridy = 1;
        panel.add(addEmployeeButton, gbc);

        // Place the Edit Employee button in the third row.
        gbc.gridy = 2;
        panel.add(editEmployeeButton, gbc);

        // Place the Delete Employee button in the fourth row.
        gbc.gridy = 3;
        panel.add(deleteEmployeeButton, gbc);

        // Place the Compute Salaries button in the fifth row.
        gbc.gridy = 4;
        panel.add(computeSalaryButton, gbc);

        // Place the Logout button in the sixth row.
        gbc.gridy = 5;
        panel.add(logoutButton, gbc);

        // Push the navigation buttons toward the top of the panel.
        gbc.weighty = 1.0;
        gbc.gridy = 6;
        panel.add(Box.createVerticalGlue(), gbc);

        return panel;
    }

    /**
     * Applies a consistent size, font, and color to a side-navigation button.
     *
     * @param button the navigation button to style
     */
    private static void setupSideButton(JButton button) {

        // Set the standard dimensions of the side-navigation button.
        button.setPreferredSize(new Dimension(150, 30));

        // Remove the keyboard focus border.
        button.setFocusable(false);

        // Apply the default inactive button colors.
        button.setBackground(NORMAL_BUTTON);
        button.setForeground(Color.BLACK);

        // Apply the standard navigation button font.
        button.setFont(new Font("Comic Sans", Font.BOLD, 13));
    }

    /**
     * Highlights the selected navigation button and resets the remaining
     * navigation buttons to their normal appearance.
     *
     * @param activeButton the navigation button that is currently active
     * @param allEmployeeButton the All Employee navigation button
     * @param addEmployeeButton the Add Employee navigation button
     * @param editEmployeeButton the Edit Employee navigation button
     * @param deleteEmployeeButton the Delete Employee navigation button
     * @param computeSalaryButton the Compute Salaries navigation button
     */
    private static void setActiveButton(
            JButton activeButton,
            JButton allEmployeeButton,
            JButton addEmployeeButton,
            JButton editEmployeeButton,
            JButton deleteEmployeeButton,
            JButton computeSalaryButton
    ) {

        // Reset all navigation button backgrounds.
        allEmployeeButton.setBackground(NORMAL_BUTTON);
        addEmployeeButton.setBackground(NORMAL_BUTTON);
        editEmployeeButton.setBackground(NORMAL_BUTTON);
        deleteEmployeeButton.setBackground(NORMAL_BUTTON);
        computeSalaryButton.setBackground(NORMAL_BUTTON);

        // Reset all navigation button text colors.
        allEmployeeButton.setForeground(Color.BLACK);
        addEmployeeButton.setForeground(Color.BLACK);
        editEmployeeButton.setForeground(Color.BLACK);
        deleteEmployeeButton.setForeground(Color.BLACK);
        computeSalaryButton.setForeground(Color.BLACK);

        // Highlight the currently selected navigation button.
        activeButton.setBackground(ACTIVE_BUTTON);
        activeButton.setForeground(Color.WHITE);
    }

    /**
     * Creates the top control panel of the salary report screen.
     *
     * The panel contains the employee number input field, month selector,
     * year selector, and Enter button.
     *
     * @param employeeNumberField field used to enter an employee number
     * @param monthBox combo box used to select a month
     * @param yearBox combo box used to select a year
     * @param enterButton button used to submit the selected information
     * @return the completed top control panel
     */
    private static JPanel createTopPanel(
            JTextField employeeNumberField,
            JComboBox<String> monthBox,
            JComboBox<String> yearBox,
            JButton enterButton
        ) {

        // Panel containing the salary report search controls.
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        // Label displayed beside the employee number field.
        JLabel label = new JLabel("Please Enter Employee No. :");
        label.setFont(new Font("Comic Sans", Font.BOLD, 15));

        // Configure the employee number input field.
        employeeNumberField.setPreferredSize(new Dimension(200, 30));
        employeeNumberField.setFont(new Font("Comic Sans", Font.BOLD, 15));

        // Configure the month and year selectors.
        monthBox.setFont(new Font("Comic Sans", Font.BOLD, 15));
        yearBox.setFont(new Font("Comic Sans", Font.BOLD, 15));

        // Configure the Enter button.
        enterButton.setPreferredSize(new Dimension(100, 30));
        enterButton.setFocusable(false);
        enterButton.setBackground(Color.WHITE);
        enterButton.setFont(new Font("Comic Sans", Font.BOLD, 15));

        // Configure spacing and alignment for the controls.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Add the employee number label.
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(label, gbc);

        // Add the employee number input field.
        gbc.gridx = 1;
        panel.add(employeeNumberField, gbc);

        // Add the month selector.
        gbc.gridx = 2;
        panel.add(monthBox, gbc);

        // Add the year selector.
        gbc.gridx = 3;
        panel.add(yearBox, gbc);

        // Add the Enter button.
        gbc.gridx = 4;
        panel.add(enterButton, gbc);

        // Add horizontal space after the controls.
        gbc.weightx = 1.0;
        gbc.gridx = 5;
        panel.add(Box.createHorizontalGlue(), gbc);

        return panel;
    }

    /**
     * Creates the salary breakdown display panel.
     *
     * The panel contains a non-editable text area placed inside a scroll pane.
     *
     * @param textArea the text area used to display salary information
     * @return the completed salary breakdown panel
     */
    private static JPanel createCenterPanel(JTextArea textArea) {

        // Panel containing the detailed salary information.
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        // Prevent users from directly editing the displayed information.
        textArea.setEditable(false);

        // Apply the display font to the salary information.
        textArea.setFont(new Font("Comic Sans", Font.BOLD, 20));

        // Allow the salary details to be scrolled when necessary.
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Configure the salary display to occupy the available space.
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
     * Adds the navigation panel and content panel to the main administrator
     * menu using GridBagLayout.
     *
     * @param adminMenuPanel the main administrator menu panel
     * @param leftPanel the side-navigation panel
     * @param contentPanel the panel containing administrator screens
     */
    private static void addPanelsToAdminMenu(
            JPanel adminMenuPanel,
            JPanel leftPanel,
            JPanel contentPanel
    ) {

        // Configure and add the side-navigation panel.
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.1;
        gbc.weighty = 1.0;
        adminMenuPanel.add(leftPanel, gbc);

        // Create separate constraints for the main content panel.
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.9;
        gbc.weighty = 1.0;
        adminMenuPanel.add(contentPanel, gbc);
    }

    /**
     * Assigns the required actions to the administrator menu buttons.
     *
     * This method handles salary report viewing, employee management
     * navigation, salary computation navigation, and administrator logout.
     *
     * @param cardLayout the main layout used to switch application screens
     * @param containerPanel the main panel containing application screens
     * @param employeeNumberField field used to enter an employee number
     * @param monthBox combo box used to select a payroll month
     * @param yearBox combo box used to select a payroll year
     * @param enterButton button used to display an employee salary report
     * @param logoutButton button used to return to the login screen
     * @param allEmployeeButton button used to display the employee list
     * @param addEmployeeButton button used to open the add employee form
     * @param editEmployeeButton button used to open the edit employee form
     * @param deleteEmployeeButton button used to open the delete employee form
     * @param computeSalaryButton button used to open salary computation
     * @param textArea text area used to display salary details
     * @param contentCardLayout layout used to switch administrator content
     * @param contentPanel panel containing administrator screens
     */
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

        // Display the selected employee's salary report.
        enterButton.addActionListener(e -> {

            // Retrieve the employee number entered by the administrator.
            String input = employeeNumberField.getText();

            // Prevent salary processing when no employee number is entered.
            if (input == null || input.trim().isEmpty()) {
                DialogCustomizer.show("Please Enter Employee Number!");
                return;
            }

            // Convert the selected month and year into numeric values.
            int monthValue = getMonthValue((String) monthBox.getSelectedItem());
            int yearValue = Integer.parseInt((String) yearBox.getSelectedItem());

            // Generate the selected employee's salary report.
            showSalaryReport(
                    input,
                    monthValue,
                    yearValue,
                    textArea
            );

            // Switch to the salary breakdown screen.
            contentCardLayout.show(contentPanel, "Salary_Breakout");

            // Keep the All Employee button highlighted.
            setActiveButton(
                    allEmployeeButton,
                    allEmployeeButton,
                    addEmployeeButton,
                    editEmployeeButton,
                    deleteEmployeeButton,
                    computeSalaryButton
            );
        });

        // Rebuild and display the complete employee salary list.
        allEmployeeButton.addActionListener(e -> {

            // Highlight the All Employee navigation button.
            setActiveButton(
                    allEmployeeButton,
                    allEmployeeButton,
                    addEmployeeButton,
                    editEmployeeButton,
                    deleteEmployeeButton,
                    computeSalaryButton
            );

            // Remove the currently displayed administrator content.
            contentPanel.removeAll();

            // Recreate the main salary report panel.
            JPanel salaryPanel = new JPanel(new BorderLayout());
            salaryPanel.setBackground(Color.WHITE);

            // Recreate the salary report controls.
            JPanel topPanel = createTopPanel(
                    employeeNumberField,
                    monthBox,
                    yearBox,
                    enterButton
            );

            // Recreate the employee salary list.
            JPanel employeeListPanel = createEmployeeListPanel(
                    monthBox,
                    yearBox,
                    employeeNumberField,
                    textArea,
                    contentCardLayout,
                    contentPanel
            );

            // Add the controls and employee list to the salary report panel.
            salaryPanel.add(topPanel, BorderLayout.NORTH);
            salaryPanel.add(employeeListPanel, BorderLayout.CENTER);

            // Register the salary report and salary breakdown screens again.
            contentPanel.add(salaryPanel, "Salary_Report");
            contentPanel.add(createCenterPanel(textArea), "Salary_Breakout");

            // Refresh the administrator content panel.
            contentPanel.revalidate();
            contentPanel.repaint();

            // Display the rebuilt salary report screen.
            contentCardLayout.show(contentPanel, "Salary_Report");
        });

        // Open the Add Employee form.
        addEmployeeButton.addActionListener(e -> {

            // Highlight the Add Employee navigation button.
            setActiveButton(
                    addEmployeeButton,
                    allEmployeeButton,
                    addEmployeeButton,
                    editEmployeeButton,
                    deleteEmployeeButton,
                    computeSalaryButton
            );

            // Create and display the Add Employee panel.
            JPanel addPanel = createAddEmployeePanel();
            contentPanel.add(addPanel, "Add_Employee");
            contentCardLayout.show(contentPanel, "Add_Employee");
        });

        // Open the Edit Employee form.
        editEmployeeButton.addActionListener(e -> {

            // Highlight the Edit Employee navigation button.
            setActiveButton(
                    editEmployeeButton,
                    allEmployeeButton,
                    addEmployeeButton,
                    editEmployeeButton,
                    deleteEmployeeButton,
                    computeSalaryButton
            );

            // Create and display the Edit Employee panel.
            JPanel editPanel = createEditEmployeePanel();
            contentPanel.add(editPanel, "Edit_Employee");
            contentCardLayout.show(contentPanel, "Edit_Employee");
        });

        // Open the Delete Employee form.
        deleteEmployeeButton.addActionListener(e -> {

            // Highlight the Delete Employee navigation button.
            setActiveButton(
                    deleteEmployeeButton,
                    allEmployeeButton,
                    addEmployeeButton,
                    editEmployeeButton,
                    deleteEmployeeButton,
                    computeSalaryButton
            );

            // Create and display the Delete Employee panel.
            JPanel deletePanel = createDeleteEmployeePanel();
            contentPanel.add(deletePanel, "Delete_Employee");
            contentCardLayout.show(contentPanel, "Delete_Employee");
        });

        // Open the salary computation screen.
        computeSalaryButton.addActionListener(e -> {

            // Highlight the Compute Salaries navigation button.
            setActiveButton(
                    computeSalaryButton,
                    allEmployeeButton,
                    addEmployeeButton,
                    editEmployeeButton,
                    deleteEmployeeButton,
                    computeSalaryButton
            );

            // Create the salary computation panel.
            JPanel computePanel = createComputeSalariesPanel(
                    monthBox,
                    yearBox,
                    textArea,
                    contentCardLayout,
                    contentPanel
            );

            // Register and display the salary computation screen.
            contentPanel.add(computePanel, "Compute_Salaries");
            contentCardLayout.show(contentPanel, "Compute_Salaries");
        });

        // Return the administrator to the login screen.
        logoutButton.addActionListener(e -> {

            // Switch the main application display to the login screen.
            cardLayout.show(containerPanel, "Login_Screen");

            // Clear the employee number and salary report fields.
            employeeNumberField.setText("");
            textArea.setText("");

            // Reset the administrator content to the salary report screen.
            contentCardLayout.show(contentPanel, "Salary_Report");

            // Reset the highlighted navigation button.
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
    /**
     * Creates a table displaying all employees and their payroll information
     * for the selected month.
     *
     * The panel allows the administrator to select an employee and view
     * the corresponding salary breakdown.
     *
     * @param monthValue the selected payroll month represented as a number
     * @param employeeNumberField field used to store the selected employee number
     * @param textArea text area used to display salary details
     * @param contentCardLayout layout used to switch administrator content
     * @param contentPanel panel containing the administrator screens
     * @return the completed employee payroll table panel
     */
    private static JPanel createAllEmployeePanel(
            int monthValue,
            JTextField employeeNumberField,
            JTextArea textArea,
            CardLayout contentCardLayout,
            JPanel contentPanel
    ) {

        // Main panel containing the employee payroll table.
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        try {

            // Define the column headings of the employee payroll table.
            String[] columnNames = {
                    "ID", "Last Name", "First Name", "Birth Date",
                    "Hours", "Gross Salary", "Net Salary", "Tax"
            };

            // Retrieve employee and payroll information for the selected month.
            String[][] employeeData = GetList.getTableData(monthValue);

            // Create and configure the employee payroll table.
            JTable table = new JTable(employeeData, columnNames);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.setFont(new Font("Arial", Font.PLAIN, 13));
            table.setRowHeight(22);

            // Add the table to a scroll pane.
            JScrollPane scrollPane = new JScrollPane(table);

            // Button used to display the selected employee's salary breakdown.
            JButton viewButton =
                    new JButton("View Selected Employee Salary Breakout");

            // Place the table and button inside the panel.
            panel.add(scrollPane, BorderLayout.CENTER);
            panel.add(viewButton, BorderLayout.SOUTH);

            // Display the salary breakdown of the selected employee.
            viewButton.addActionListener(e -> {

                // Retrieve the currently selected table row.
                int selectedRow = table.getSelectedRow();

                // Require the administrator to select an employee first.
                if (selectedRow == -1) {
                    DialogCustomizer.show("Please select an employee first.");
                    return;
                }

                // Retrieve the employee number from the selected row.
                String employeeId =
                        table.getValueAt(selectedRow, 0).toString();

                // Store the selected employee number in the input field.
                employeeNumberField.setText(employeeId);

                // Generate the selected employee's salary report.
                showSalaryReport(
                        employeeId,
                        monthValue,
                        2024,
                        textArea
                );

                // Switch to the salary report screen.
                contentCardLayout.show(contentPanel, "Salary_Report");
            });

        } catch (IOException ex) {

            // Notify the administrator when payroll records cannot be loaded.
            DialogCustomizer.show("Unable to load employee payroll list.");
        }

        return panel;
    }

    /**
     * Creates the form used to add a new employee record.
     *
     * The panel generates a new employee number, displays all required
     * employee fields, automatically computes salary-related values,
     * validates the entered information, and saves valid records to the
     * employee CSV file.
     *
     * @return the completed Add Employee panel
     */
    private static JPanel createAddEmployeePanel() {

        // Main panel containing the Add Employee form.
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);

        // Configure the spacing and alignment of form components.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 20, 4, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Button used to save the new employee record.
        JButton saveButton = new JButton("Add Employee");

        // Create the employee input fields and corresponding labels.
        JComponent[] fields = createEmployeeFields(false);
        String[] labels = getAddEmployeeInputLabels();

        try {

            // Generate and display the next available employee number.
            setComponentValue(
                    fields[0],
                    AddEmployee.generateNextEmployeeNumber()
            );

            // Prevent manual editing of the generated employee number.
            fields[0].setEnabled(false);

        } catch (IOException ex) {

            // Notify the administrator if the employee number cannot be generated.
            DialogCustomizer.show("Unable to generate Employee Number.");
        }

        // Enable automatic computation of salary-related fields.
        makeSalaryAutoComputed(fields);

        // Add each label and input field to the form.
        for (int i = 0; i < labels.length; i++) {

            // Add the field label in the first column.
            gbc.gridx = 0;
            gbc.gridy = i;
            mainPanel.add(new JLabel(labels[i]), gbc);

            // Add the corresponding input component in the second column.
            gbc.gridx = 1;
            mainPanel.add(fields[i], gbc);
        }

        // Add the save button below the employee fields.
        gbc.gridx = 1;
        gbc.gridy = labels.length;
        mainPanel.add(saveButton, gbc);

        // Validate and save the employee record when the button is clicked.
        saveButton.addActionListener(e -> {

            // Refresh automatically computed salary values before validation.
            updateComputedSalaryFields(fields);

            // Retrieve all entered employee information from the form.
            String[] newEmployeeData = getFieldData(fields);

            // Validate the employee information.
            String validationMessage =
                    AddEmployee.validateEmployeeData(newEmployeeData);

            // Stop the operation when validation fails.
            if (!validationMessage.equals("valid")) {
                DialogCustomizer.show(validationMessage);
                return;
            }

            try {

                // Prevent duplicate employee records.
                if (AddEmployee.employeeExists(newEmployeeData[0])) {
                    DialogCustomizer.show("Employee Already Exists.");
                    return;
                }

                // Save the new employee record to the CSV file.
                AddEmployee.addEmployee(newEmployeeData);

                // Sort the CSV file by employee number after insertion.
                GetList.sortCsvByEmployeeNumber();

                // Confirm that the employee was added successfully.
                JOptionPane.showMessageDialog(
                        null,
                        "Employee Added Successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                // Clear all input fields after a successful save.
                clearFields(fields);

                // Generate the next available employee number.
                setComponentValue(
                        fields[0],
                        AddEmployee.generateNextEmployeeNumber()
                );

                // Keep the generated employee number field disabled.
                fields[0].setEnabled(false);

                // Restore automatic salary computation for the cleared form.
                makeSalaryAutoComputed(fields);

            } catch (IOException ex) {

                // Notify the administrator when the employee cannot be saved.
                DialogCustomizer.show(
                        "Something went wrong while adding employee."
                );
            }
        });

        return mainPanel;
    }

    /**
     * Creates the form used to search for and edit an existing employee.
     *
     * The panel allows the administrator to enter an employee number,
     * retrieve the matching record, modify the employee information,
     * validate the edited values, and save the updated record.
     *
     * @return the completed Edit Employee panel
     */
  private static JPanel createEditEmployeePanel() {
  
      JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
      mainPanel.setBackground(Color.WHITE);
  
      String[] columns = {
              "ID", "Last Name", "First Name", "BirthDate", "Address", "Phone Number",
              "SSS #", "Philhealth#", "TIN #", "Pag-Ibig #", "Status", "Position",
              "Immediate Supervisor", "Basic Salary", "Rice Subsidy", "Phone Allowance",
              "Clothing Allowance", "Gross Semi-monthly Rate", "Hourly Rate"
      };
  
      DefaultTableModel model = new DefaultTableModel(columns, 0) {
          @Override
          public boolean isCellEditable(int row, int column) {
              return false;
          }
      };
  
      JTable table = new JTable(model);
      table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
      table.setRowHeight(30);
      
      int[] widths = {80, 110, 110, 95, 220, 110, 120, 120, 120, 120, 90, 140, 150, 110, 110, 120, 130, 160, 110};
      for (int i = 0; i < widths.length; i++) {
          table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
      }
  
      JScrollPane tableScroll = new JScrollPane(table);
      mainPanel.add(tableScroll, BorderLayout.CENTER);
  
      JPanel editPanel = new JPanel(new GridBagLayout());
      editPanel.setBorder(BorderFactory.createTitledBorder("Edit Employee"));
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(4, 10, 0, 10);
      gbc.anchor = GridBagConstraints.WEST;
      gbc.fill = GridBagConstraints.HORIZONTAL;
  
      JTextField searchField = new JTextField(15);
      JButton searchButton = new JButton("Search");
      JButton saveButton = new JButton("Save Changes");
      saveButton.setEnabled(false);
  
      JTextField txtID = new JTextField(15);
      JTextField txtLastName = new JTextField(15);
      JTextField txtFirstName = new JTextField(15);
      JTextField txtBirthDay = new JTextField(15);
      JTextField txtAddress = new JTextField(15);
      JTextField txtPhoneNumber = new JTextField(15);
      JTextField txtSSS = new JTextField(15);
      JTextField txtPhilhealth = new JTextField(15);
      JTextField txtTIN = new JTextField(15);
      JTextField txtPagibig = new JTextField(15);
      JTextField txtStatus = new JTextField(15);
      JTextField txtPosition = new JTextField(15);
      JTextField txtImmediateSupervisor = new JTextField(15);
      JTextField txtBasicSalary = new JTextField(15);
      JTextField txtRiceSubsidy = new JTextField(15);
      JTextField txtPhoneAllowance = new JTextField(15);
      JTextField txtClothingAllowance = new JTextField(15);
      JTextField txtGrossSemiMon = new JTextField(15);
      JTextField txtHourlyRate = new JTextField(15);
  
      txtID.setEditable(false);
  
      int r = 0;
      gbc.gridx = 0; gbc.gridy = r; editPanel.add(new JLabel("Enter Employee # (5 digits):"), gbc);
      gbc.gridx = 1; editPanel.add(searchField, gbc);
      gbc.gridx = 2; editPanel.add(searchButton, gbc);
  
      r++;
      addRow(editPanel, gbc, r++, "ID:", txtID);
      addRow(editPanel, gbc, r++, "Last Name:", txtLastName);
      addRow(editPanel, gbc, r++, "First Name:", txtFirstName);
      addRow(editPanel, gbc, r++, "Birth Date:", txtBirthDay);
      addRow(editPanel, gbc, r++, "Address:", txtAddress);
      addRow(editPanel, gbc, r++, "Phone Number:", txtPhoneNumber);
      addRow(editPanel, gbc, r++, "SSS#:", txtSSS);
      addRow(editPanel, gbc, r++, "Philhealth:", txtPhilhealth);
      addRow(editPanel, gbc, r++, "TIN:", txtTIN);
      addRow(editPanel, gbc, r++, "PagIbig:", txtPagibig);
      addRow(editPanel, gbc, r++, "Status:", txtStatus);
      addRow(editPanel, gbc, r++, "Position:", txtPosition);
      addRow(editPanel, gbc, r++, "Immediate Supervisor:", txtImmediateSupervisor);
      addRow(editPanel, gbc, r++, "Basic Salary:", txtBasicSalary);
      addRow(editPanel, gbc, r++, "Rice Subsidy:", txtRiceSubsidy);
      addRow(editPanel, gbc, r++, "Phone Allowance:", txtPhoneAllowance);
      addRow(editPanel, gbc, r++, "Clothing Allowance:", txtClothingAllowance);
      addRow(editPanel, gbc, r++, "Gross Semi-monthly:", txtGrossSemiMon);
      addRow(editPanel, gbc, r++, "Hourly Rate:", txtHourlyRate);
  
      gbc.gridx = 1;
      gbc.gridy = r;
      editPanel.add(saveButton, gbc);
  
      mainPanel.add(editPanel, BorderLayout.EAST);
  
      try {
          loadEmployeeTableData(model);
      } catch (IOException ex) {
          DialogCustomizer.show("Unable to load employee list.");
      }
  
      table.getSelectionModel().addListSelectionListener(e -> {
          if (e.getValueIsAdjusting()) return;
          int row = table.getSelectedRow();
          if (row == -1) return;
  
          txtID.setText(valueAt(model, row, 0));
          txtLastName.setText(valueAt(model, row, 1));
          txtFirstName.setText(valueAt(model, row, 2));
          txtBirthDay.setText(valueAt(model, row, 3));
          txtAddress.setText(valueAt(model, row, 4));
          txtPhoneNumber.setText(valueAt(model, row, 5));
          txtSSS.setText(valueAt(model, row, 6));
          txtPhilhealth.setText(valueAt(model, row, 7));
          txtTIN.setText(valueAt(model, row, 8));
          txtPagibig.setText(valueAt(model, row, 9));
          txtStatus.setText(valueAt(model, row, 10));
          txtPosition.setText(valueAt(model, row, 11));
          txtImmediateSupervisor.setText(valueAt(model, row, 12));
          txtBasicSalary.setText(valueAt(model, row, 13));
          txtRiceSubsidy.setText(valueAt(model, row, 14));
          txtPhoneAllowance.setText(valueAt(model, row, 15));
          txtClothingAllowance.setText(valueAt(model, row, 16));
          txtGrossSemiMon.setText(valueAt(model, row, 17));
          txtHourlyRate.setText(valueAt(model, row, 18));
  
          saveButton.setEnabled(true);
      });
  
      searchButton.addActionListener(e -> {
          String empNo = searchField.getText().trim();
  
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
  
              txtID.setText(record[0]);
              txtLastName.setText(record[1]);
              txtFirstName.setText(record[2]);
              txtBirthDay.setText(record[3]);
              txtAddress.setText(record[4]);
              txtPhoneNumber.setText(record[5]);
              txtSSS.setText(record[6]);
              txtPhilhealth.setText(record[7]);
              txtTIN.setText(record[8]);
              txtPagibig.setText(record[9]);
              txtStatus.setText(record[10]);
              txtPosition.setText(record[11]);
              txtImmediateSupervisor.setText(record[12]);
              txtBasicSalary.setText(record[13]);
              txtRiceSubsidy.setText(record[14]);
              txtPhoneAllowance.setText(record[15]);
              txtClothingAllowance.setText(record[16]);
              txtGrossSemiMon.setText(record[17]);
              txtHourlyRate.setText(record[18]);
  
              saveButton.setEnabled(true);
  
              int foundRow = findRowById(model, empNo);
              if (foundRow != -1) {
                  table.setRowSelectionInterval(foundRow, foundRow);
              }
  
          } catch (IOException ex) {
              DialogCustomizer.show("Something went wrong while searching employee.");
          }
      });
  
      saveButton.addActionListener(e -> {
          String[] updatedData = new String[19];
          updatedData[0] = txtID.getText().trim();
          updatedData[1] = txtLastName.getText().trim();
          updatedData[2] = txtFirstName.getText().trim();
          updatedData[3] = txtBirthDay.getText().trim();
          updatedData[4] = txtAddress.getText().trim();
          updatedData[5] = txtPhoneNumber.getText().trim();
          updatedData[6] = txtSSS.getText().trim();
          updatedData[7] = txtPhilhealth.getText().trim();
          updatedData[8] = txtTIN.getText().trim();
          updatedData[9] = txtPagibig.getText().trim();
          updatedData[10] = txtStatus.getText().trim();
          updatedData[11] = txtPosition.getText().trim();
          updatedData[12] = txtImmediateSupervisor.getText().trim();
          updatedData[13] = txtBasicSalary.getText().trim();
          updatedData[14] = txtRiceSubsidy.getText().trim();
          updatedData[15] = txtPhoneAllowance.getText().trim();
          updatedData[16] = txtClothingAllowance.getText().trim();
          updatedData[17] = txtGrossSemiMon.getText().trim();
          updatedData[18] = txtHourlyRate.getText().trim();
  
          String validation = EditEmployee.validateEditedFields(updatedData);
          if (!validation.equals("valid")) {
              DialogCustomizer.show(validation);
              return;
          }
  
          int confirm = JOptionPane.showConfirmDialog(
                  mainPanel,
                  "Are you sure you want to save these changes?",
                  "Confirm Update",
                  JOptionPane.YES_NO_OPTION
          );
  
          if (confirm != JOptionPane.YES_OPTION) {
              return;
          }
  
          try {
              boolean updated = EditEmployee.updateEmployee(updatedData);
              if (updated) {
                  updateTableRow(model, table.getSelectedRow(), updatedData);
                  JOptionPane.showMessageDialog(
                          mainPanel,
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
  //
  private static void addRow(JPanel panel, GridBagConstraints gbc, int y, String label, JComponent field) {
      gbc.gridx = 0;
      gbc.gridy = y;
      panel.add(new JLabel(label), gbc);
      gbc.gridx = 1;
      panel.add(field, gbc);
  }
  
  private static String valueAt(DefaultTableModel model, int row, int col) {
      Object v = model.getValueAt(row, col);
      return v == null ? "" : v.toString();
  }
  
  private static int findRowById(DefaultTableModel model, String empNo) {
      for (int i = 0; i < model.getRowCount(); i++) {
          if (empNo.equals(model.getValueAt(i, 0).toString().trim())) {
              return i;
          }
      }
      return -1;
  }
  
  private static void updateTableRow(DefaultTableModel model, int row, String[] data) {
      if (row == -1) return;
      for (int i = 0; i < data.length; i++) {
          model.setValueAt(data[i], row, i);
      }
  }
  //
  private static void loadEmployeeTableData(DefaultTableModel model) throws IOException {
      try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/MotorPh.csv"))) {
          br.readLine();
          String line;
          while ((line = br.readLine()) != null) {
              if (line.trim().isEmpty()) continue;
              String[] values = parseCsvLine(line);
              if (values.length >= 19) {
                  model.addRow(values);
              }
          }
      }
  } //
  private static String[] parseCsvLine(String line) {
      ArrayList<String> result = new ArrayList<>();
      StringBuilder current = new StringBuilder();
      boolean inQuotes = false;
  
      for (int i = 0; i < line.length(); i++) {
          char c = line.charAt(i);
          if (c == '"') {
              inQuotes = !inQuotes;
          } else if (c == ',' && !inQuotes) {
              result.add(current.toString().replace("\"", "").trim());
              current.setLength(0);
          } else {
              current.append(c);
          }
      }
      result.add(current.toString().replace("\"", "").trim());
      return result.toArray(new String[0]);
  }
    /**
     * Creates the form used to search for and delete an employee record.
     *
     * The panel allows the administrator to search using one or more employee
     * details, review matching records, select the correct employee when
     * multiple matches are found, and permanently remove the selected
     * employee and attendance records.
     *
     * @return the completed Delete Employee panel
     */
    /**
 * Creates the Delete Employee module.
 *
 * The module displays all employees in a JTable, allows the administrator
 * to select an employee by clicking a table row, displays the complete
 * employee details before deletion, removes the selected employee and
 * related attendance records, and refreshes the table after deletion.
 *
 * @return the completed Delete Employee panel
 */
private static JPanel createDeleteEmployeePanel() {

    JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
    mainPanel.setBackground(Color.WHITE);
    mainPanel.setBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
    );

    /*
     * ------------------------------------------------------------
     * EMPLOYEE DIRECTORY TABLE
     * ------------------------------------------------------------
     */
    String[] columnNames = {
            "Employee #",
            "Last Name",
            "First Name",
            "Birthday",
            "Status",
            "Position"
    };

    JTable employeeTable = new JTable();
    employeeTable.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
    );
    employeeTable.setFont(
            new Font("Consolas", Font.PLAIN, 14)
    );
    employeeTable.setRowHeight(24);
    employeeTable.getTableHeader().setFont(
            new Font("Consolas", Font.BOLD, 14)
    );

    JScrollPane tableScrollPane =
            new JScrollPane(employeeTable);

    tableScrollPane.setPreferredSize(
            new Dimension(900, 300)
    );

    JPanel directoryPanel =
            new JPanel(new BorderLayout(5, 5));

    directoryPanel.setBackground(Color.WHITE);

    JLabel directoryLabel =
            new JLabel("Employee Directory");

    directoryLabel.setFont(
            new Font("Consolas", Font.BOLD, 18)
    );

    directoryPanel.add(
            directoryLabel,
            BorderLayout.NORTH
    );

    directoryPanel.add(
            tableScrollPane,
            BorderLayout.CENTER
    );

    /*
     * ------------------------------------------------------------
     * EMPLOYEE DETAILS AND DELETE CONTROLS
     * ------------------------------------------------------------
     */
    JPanel detailsPanel =
            new JPanel(new BorderLayout(5, 5));

    detailsPanel.setBackground(Color.WHITE);
    detailsPanel.setBorder(
            BorderFactory.createTitledBorder(
                    "Selected Employee Information"
            )
    );

    JTextArea detailsArea = new JTextArea(16, 45);
    detailsArea.setEditable(false);
    detailsArea.setFont(
            new Font("Consolas", Font.BOLD, 14)
    );
    detailsArea.setMargin(
            new Insets(10, 10, 10, 10)
    );

    JScrollPane detailsScrollPane =
            new JScrollPane(detailsArea);

    JButton deleteButton =
            new JButton("Delete Selected Employee");

    deleteButton.setFont(
            new Font("Consolas", Font.BOLD, 14)
    );

    // Disable deletion until an employee is selected.
    deleteButton.setEnabled(false);

    detailsPanel.add(
            detailsScrollPane,
            BorderLayout.CENTER
    );

    detailsPanel.add(
            deleteButton,
            BorderLayout.SOUTH
    );

    /*
     * Split the module into the employee directory and details area.
     */
    JSplitPane splitPane = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT,
            directoryPanel,
            detailsPanel
    );

    splitPane.setResizeWeight(0.45);
    splitPane.setDividerLocation(320);
    splitPane.setOneTouchExpandable(true);

    mainPanel.add(splitPane, BorderLayout.CENTER);

    /*
     * Stores the Employee Number selected from the JTable.
     * A one-element array allows the value to be updated inside listeners.
     */
    final String[] selectedEmployeeNumber = {""};

    /*
     * ------------------------------------------------------------
     * INITIAL TABLE LOADING
     * ------------------------------------------------------------
     */
    refreshDeleteEmployeeTable(
            employeeTable,
            columnNames
    );

    /*
     * ------------------------------------------------------------
     * TABLE ROW SELECTION
     * ------------------------------------------------------------
     */
    employeeTable.getSelectionModel()
            .addListSelectionListener(e -> {

        // Ignore intermediate row-selection events.
        if (e.getValueIsAdjusting()) {
            return;
        }

        int selectedRow =
                employeeTable.getSelectedRow();

        if (selectedRow == -1) {

            selectedEmployeeNumber[0] = "";
            detailsArea.setText("");
            deleteButton.setEnabled(false);
            return;
        }

        String employeeNumber =
                employeeTable.getValueAt(
                        selectedRow,
                        0
                ).toString();

        loadEmployeeForDeletion(
                employeeNumber,
                selectedEmployeeNumber,
                detailsArea,
                deleteButton
        );
    });

    /*
     * ------------------------------------------------------------
     * DELETE SELECTED EMPLOYEE
     * ------------------------------------------------------------
     */
    deleteButton.addActionListener(e -> {

        if (selectedEmployeeNumber[0].isEmpty()) {
            DialogCustomizer.show(
                    "Please select an employee first."
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to delete Employee # "
                        + selectedEmployeeNumber[0]
                        + "?\n\n"
                        + "The employee record and related attendance "
                        + "records will be permanently removed.",
                "Confirm Employee Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            DialogCustomizer.show(
                    "Delete operation cancelled."
            );
            return;
        }

        try {

            boolean deleted =
                    DeleteEmployee.deleteEmployee(
                            selectedEmployeeNumber[0]
                    );

            if (deleted) {

                /*
                 * Remove every attendance entry associated with the
                 * deleted employee.
                 */
                DeleteEmployee.deleteEmployeeAttendance(
                        selectedEmployeeNumber[0]
                );

                // Restore employee-number order in the CSV file.
                GetList.sortCsvByEmployeeNumber();

                JOptionPane.showMessageDialog(
                        null,
                        "Employee and related attendance records "
                                + "were deleted successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                /*
                 * Refresh the JTable so the deleted employee disappears
                 * immediately without reopening the module.
                 */
                refreshDeleteEmployeeTable(
                        employeeTable,
                        columnNames
                );

                // Reset the employee selection and details display.
                employeeTable.clearSelection();
                selectedEmployeeNumber[0] = "";
                detailsArea.setText("");
                deleteButton.setEnabled(false);

            } else {
                DialogCustomizer.show(
                        "Employee Not Found."
                );
            }

        } catch (IOException ex) {
            DialogCustomizer.show(
                    "Something went wrong while deleting employee."
            );
        }
    });

    return mainPanel;
}
/**
 * Loads the selected employee's complete information for review
 * before deletion.
 *
 * @param employeeNumber the employee number selected from the JTable
 * @param selectedEmployeeNumber stores the selected employee number
 * @param detailsArea the area used to display complete employee details
 * @param deleteButton the button used to delete the selected employee
 */
private static void loadEmployeeForDeletion(
        String employeeNumber,
        String[] selectedEmployeeNumber,
        JTextArea detailsArea,
        JButton deleteButton
) {

    try {

        String[] record =
                EditEmployee.searchEmployee(employeeNumber);

        if (record == null) {
            DialogCustomizer.show(
                    "Employee Not Found."
            );

            selectedEmployeeNumber[0] = "";
            detailsArea.setText("");
            deleteButton.setEnabled(false);
            return;
        }

        // Store the selected Employee Number for deletion.
        selectedEmployeeNumber[0] = record[0];

        // Display the complete employee record for confirmation.
        detailsArea.setText(
                "EMPLOYEE SELECTED FOR DELETION\n"
                + "============================================================\n"
                + "Employee #           : " + record[0] + "\n"
                + "Last Name            : " + record[1] + "\n"
                + "First Name           : " + record[2] + "\n"
                + "Birthday             : " + record[3] + "\n"
                + "Address              : " + record[4] + "\n"
                + "Phone Number         : " + record[5] + "\n\n"

                + "GOVERNMENT INFORMATION\n"
                + "============================================================\n"
                + "SSS Number           : " + record[6] + "\n"
                + "PhilHealth Number    : " + record[7] + "\n"
                + "TIN Number           : " + record[8] + "\n"
                + "Pag-IBIG Number      : " + record[9] + "\n\n"

                + "EMPLOYMENT INFORMATION\n"
                + "============================================================\n"
                + "Status               : " + record[10] + "\n"
                + "Position             : " + record[11] + "\n"
                + "Immediate Supervisor : " + record[12] + "\n\n"

                + "SALARY INFORMATION\n"
                + "============================================================\n"
                + "Basic Salary         : " + record[13] + "\n"
                + "Rice Subsidy         : " + record[14] + "\n"
                + "Phone Allowance      : " + record[15] + "\n"
                + "Clothing Allowance   : " + record[16] + "\n"
                + "Gross Semi-monthly   : " + record[17] + "\n"
                + "Hourly Rate          : " + record[18]
        );

        detailsArea.setCaretPosition(0);
        deleteButton.setEnabled(true);

    } catch (IOException ex) {
        DialogCustomizer.show(
                "Something went wrong while loading employee."
        );

        selectedEmployeeNumber[0] = "";
        detailsArea.setText("");
        deleteButton.setEnabled(false);
    }
}
/**
 * Reloads the Delete Employee JTable using the latest CSV records.
 *
 * @param employeeTable the employee directory table to refresh
 * @param columnNames the employee table column headings
 */
private static void refreshDeleteEmployeeTable(
        JTable employeeTable,
        String[] columnNames
) {

    try {

        String[][] tableData =
                GetList.getBasicEmployeeTableData();

        employeeTable.setModel(
                new javax.swing.table.DefaultTableModel(
                        tableData,
                        columnNames
                ) {

                    /**
                     * Prevents direct editing inside the employee table.
                     *
                     * @param row the selected table row
                     * @param column the selected table column
                     * @return false because all table cells are read-only
                     */
                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                }
        );

    } catch (IOException ex) {
        DialogCustomizer.show(
                "Unable to load the employee directory."
        );
    }
}
    
    /**
     * Creates a formatted text field using the specified input mask.
     *
     * The mask controls the format of the value entered by the user.
     * Underscores are displayed as placeholders for missing characters.
     *
     * @param mask the input pattern applied to the formatted text field
     * @return the configured formatted text field
     */
    private static JFormattedTextField createMaskedField(String mask) {

        try {

            // Create a formatter using the provided input pattern.
            MaskFormatter formatter = new MaskFormatter(mask);

            // Display underscores for characters that have not yet been entered.
            formatter.setPlaceholderCharacter('_');

            // Create the formatted text field using the configured formatter.
            JFormattedTextField field =
                    new JFormattedTextField(formatter);

            // Set the standard size of the formatted input field.
            field.setPreferredSize(new Dimension(220,25));

            return field;

        } catch (ParseException ex) {

            // Return a basic formatted text field if the mask is invalid.
            return new JFormattedTextField();
        }
    }

    /**
     * Retrieves the current value of a supported Swing component.
     *
     * Combo box values are taken from the selected item, while text field
     * values are taken from the entered text.
     *
     * @param component the Swing component whose value will be retrieved
     * @return the component value as a String, or an empty String when the
     *         component type is not supported
     */
    private static String getComponentValue(JComponent component) {

        // Retrieve the selected value from a combo box.
        if(component instanceof JComboBox) {
            return String.valueOf(
                    ((JComboBox<?>) component).getSelectedItem()
            );
        }

        // Retrieve the entered value from a text field.
        if(component instanceof JTextField) {
            return ((JTextField) component).getText();
        }

        // Return an empty value for unsupported component types.
        return "";
    }

    /**
     * Assigns a value to a supported Swing component.
     *
     * The method selects the matching item for combo boxes or places the
     * supplied text inside text fields.
     *
     * @param component the Swing component whose value will be updated
     * @param value the value to assign to the component
     */
    private static void setComponentValue(
            JComponent component,
            String value
    ) {

        // Select the matching value when the component is a combo box.
        if(component instanceof JComboBox) {
            ((JComboBox<?>) component).setSelectedItem(value);
        }

        // Display the value when the component is a text field.
        if(component instanceof JTextField) {
            ((JTextField) component).setText(value);
        }
    }

    /**
     * Clears all supported employee input fields.
     *
     * Text fields are emptied, while combo boxes are returned to their
     * first available option.
     *
     * @param fields the employee form components to reset
     */
    private static void clearFields(JComponent[] fields) {

        // Reset each component in the employee form.
        for(JComponent field : fields) {

            // Clear text entered in text fields.
            if(field instanceof JTextField) {
                ((JTextField) field).setText("");
            }

            // Reset combo boxes to their first item.
            if(field instanceof JComboBox) {
                ((JComboBox<?>) field).setSelectedIndex(0);
            }
        }
    }

    /**
     * Converts a month name into its corresponding numeric value.
     *
     * @param month the name of the selected month
     * @return the month number from 1 to 12; returns 1 by default
     */
    private static int getMonthValue(String month) {

        // Return the numeric value corresponding to the selected month.
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

    /**
     * Generates and displays the selected employee's salary report.
     *
     * The method retrieves the employee record and payroll information
     * for the selected month and year. It displays the employee details,
     * cut-off salaries, deductions, and monthly net salary in the provided
     * text area.
     *
     * @param employeeNumber the employee number to search for
     * @param monthValue the selected payroll month represented as a number
     * @param yearValue the selected payroll year
     * @param textArea the text area used to display the salary report
     */
    private static void showSalaryReport(
            String employeeNumber,
            int monthValue,
            int yearValue,
            JTextArea textArea
    ) {

        try {

            // Retrieve the employee record using the entered employee number.
            String[] record = EditEmployee.searchEmployee(employeeNumber);

            // Stop the operation when the employee number does not exist.
            if (record == null) {
                DialogCustomizer.show("Employee Number Not Found!");
                return;
            }

            // Process the employee's payroll for the selected month and year.
            Admin payroll =
                    ProcessPayroll.admin(employeeNumber, monthValue, yearValue);

            // Prepare the employee information section of the report.
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

            /*
             * Display the employee information and a no-record message
             * when no attendance or payroll data is available.
             */
            if (payroll == null) {
                textArea.setText(
                        employeeInfo +
                        "PAYROLL REPORT\n" +
                        "====================================\n" +
                        "Selected Period      : " +
                        ProcessPayroll.month(monthValue) + " " +
                        yearValue + "\n" +
                        "No attendance/payroll record found for this employee in the selected period."
                );
                return;
            }

            // Calculate the combined government and tax deductions.
            double totalDeductions =
                    payroll.getSSS()
                    + payroll.getphilHealth()
                    + payroll.getpagIbig()
                    + payroll.gettax();

            /*
             * The first cut-off net salary is equal to its gross salary
             * because all deductions are applied to the second cut-off.
             */
            double firstNetSalary =
                    payroll.getgrossSalary();

            // Subtract all monthly deductions from the second cut-off salary.
            double secondNetSalary =
                    payroll.getgrossSalary2() - totalDeductions;

            // Calculate the employee's total monthly net salary.
            double totalNetSalary =
                    firstNetSalary + secondNetSalary;

            // Display the complete payroll report.
            textArea.setText(
                    employeeInfo +
                    "PAYROLL REPORT\n" +
                    "====================================\n" +
                    "Selected Period      : " +
                    ProcessPayroll.month(monthValue) + " " +
                    yearValue + "\n\n" +

                    "FIRST CUTOFF\n" +
                    "====================================\n" +
                    "Cutoff Date          : " + payroll.getcutOff() + "\n" +
                    "Total Hours Worked   : " +
                    String.format("%.2f", payroll.gettotalHours()) + "\n" +
                    "Gross Salary         : " +
                    String.format("%.2f", payroll.getgrossSalary()) + "\n" +
                    "Net Salary           : " +
                    String.format("%.2f", firstNetSalary) + "\n\n" +

                    "SECOND CUTOFF\n" +
                    "====================================\n" +
                    "Cutoff Date          : " +
                    payroll.getcutOff2() +
                    " (Second payout includes all deductions)\n" +
                    "Total Hours Worked   : " +
                    String.format("%.2f", payroll.gettotalHours2()) + "\n" +
                    "Gross Salary         : " +
                    String.format("%.2f", payroll.getgrossSalary2()) + "\n\n" +

                    "DEDUCTIONS\n" +
                    "====================================\n" +
                    "SSS                  : " +
                    String.format("%.2f", payroll.getSSS()) + "\n" +
                    "PhilHealth           : " +
                    String.format("%.2f", payroll.getphilHealth()) + "\n" +
                    "Pag-IBIG             : " +
                    String.format("%.2f", payroll.getpagIbig()) + "\n" +
                    "Tax                  : " +
                    String.format("%.2f", payroll.gettax()) + "\n" +
                    "Total Deductions     : " +
                    String.format("%.2f", totalDeductions) + "\n\n" +

                    "Net Salary           : " +
                    String.format("%.2f", secondNetSalary) + "\n\n" +

                    "MONTHLY SUMMARY\n" +
                    "====================================\n" +
                    "Total Net Salary     : " +
                    String.format("%.2f", totalNetSalary)
            );

        } catch (IOException ex) {

            // Notify the administrator when employee data cannot be loaded.
            DialogCustomizer.show("Unable to load employee information.");
        }
    }

    /**
     * Returns the labels used by the Add Employee form.
     *
     * The labels correspond to the input fields required when creating
     * a new employee record.
     *
     * @return an array containing the Add Employee form labels
     */
    private static String[] getAddEmployeeInputLabels() {

        // Return the labels displayed in the Add Employee form.
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

    /**
     * Returns the labels used by the Edit Employee form.
     *
     * The employee number is excluded because it is entered separately
     * when searching for an employee record.
     *
     * @return an array containing the Edit Employee form labels
     */
    private static String[] getEditEmployeeInputLabels() {

        // Return the labels displayed in the Edit Employee form.
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

    /**
     * Retrieves the current values entered in the employee form.
     *
     * The method extracts the value from each form component and stores
     * the results in a String array for validation or database updates.
     *
     * @param fields the employee form components
     * @return an array containing the values entered in each field
     */
    private static String[] getFieldData(JComponent[] fields) {

        // Store the values collected from the employee form.
        String[] data = new String[fields.length];

        // Retrieve the value from each form component.
        for (int i = 0; i < fields.length; i++) {
            data[i] = getComponentValue(fields[i]);
        }

        return data;
    }
    
    /**
     * Creates the complete set of employee input fields used by the
     * Add Employee form.
     *
     * The returned array includes all employee information, including
     * the employee number, personal details, employment information,
     * and salary-related fields.
     *
     * @param locked true to disable all fields; false to enable editing
     * @return an array containing all employee input components
     */
    private static JComponent[] createEmployeeFields(boolean locked) {

        // Create an array to store all employee input components.
        JComponent[] fields = new JComponent[19];

        // Employee identification fields.
        fields[0] = new JTextField();
        fields[1] = new JTextField();
        fields[2] = new JTextField();

        // Employee personal information fields.
        fields[3] = createMaskedField("##/##/####");
        fields[4] = new JTextField();
        fields[5] = createMaskedField("###-###-###");
        fields[6] = createMaskedField("##-#######-#");
        fields[7] = createMaskedField("############");
        fields[8] = createMaskedField("###-###-###-###");
        fields[9] = createMaskedField("############");

        // Employment information fields.
        fields[10] = new JComboBox<>(getStatusOptions());
        fields[11] = new JComboBox<>(getPositionOptions());
        fields[12] = new JComboBox<>(getSupervisorOptions());

        // Salary and allowance fields.
        fields[13] = new JTextField();
        fields[14] = new JTextField();
        fields[15] = new JTextField();
        fields[16] = new JTextField();
        fields[17] = new JTextField();
        fields[18] = new JTextField();

        // Apply a consistent size and enabled state to every component.
        for (int i = 0; i < fields.length; i++) {
            fields[i].setPreferredSize(new Dimension(220, 23));
            fields[i].setEnabled(!locked);
        }

        return fields;
    }

    /**
     * Creates the employee input fields used by the Edit Employee form.
     *
     * Unlike the Add Employee form, the employee number is excluded
     * because it is entered separately when searching for an employee.
     *
     * @param locked true to disable all fields; false to enable editing
     * @return an array containing the editable employee input components
     */
    private static JComponent[] createEmployeeFieldsWithoutEmployeeNumber(boolean locked) {

        // Create an array to store the editable employee components.
        JComponent[] fields = new JComponent[18];

        // Employee personal information fields.
        fields[0] = new JTextField();
        fields[1] = new JTextField();
        fields[2] = createMaskedField("##/##/####");
        fields[3] = new JTextField();
        fields[4] = createMaskedField("###-###-###");
        fields[5] = createMaskedField("##-#######-#");
        fields[6] = createMaskedField("############");
        fields[7] = createMaskedField("###-###-###-###");
        fields[8] = createMaskedField("############");

        // Employment information fields.
        fields[9] = new JComboBox<>(getStatusOptions());
        fields[10] = new JComboBox<>(getPositionOptions());
        fields[11] = new JComboBox<>(getSupervisorOptions());

        // Salary and allowance fields.
        fields[12] = new JTextField();
        fields[13] = new JTextField();
        fields[14] = new JTextField();
        fields[15] = new JTextField();
        fields[16] = new JTextField();
        fields[17] = new JTextField();

        // Apply a consistent size and enabled state to every component.
        for (int i = 0; i < fields.length; i++) {
            fields[i].setPreferredSize(new Dimension(220, 23));
            fields[i].setEnabled(!locked);
        }

        return fields;
    }
    
    /**
     * Retrieves the available employee status options from the CSV file.
     *
     * If the employee records cannot be read, the method returns a
     * predefined set of default employment status values.
     *
     * @return an array containing available employee status options
     */
    private static String[] getStatusOptions() {

        try {

            // Retrieve unique values from the employee status column.
            return GetList.getUniqueCsvValues(10);

        } catch (IOException ex) {

            // Return default status options when the CSV file cannot be read.
            return new String[]{"Regular", "Probationary"};
        }
    }

    /**
     * Retrieves the available employee position options from the CSV file.
     *
     * If the employee records cannot be read, the method returns "N/A"
     * as the default position option.
     *
     * @return an array containing available employee position options
     */
    private static String[] getPositionOptions() {

        try {

            // Retrieve unique values from the employee position column.
            return GetList.getUniqueCsvValues(11);

        } catch (IOException ex) {

            // Return a default value when the position list cannot be loaded.
            return new String[]{"N/A"};
        }
    }

    /**
     * Retrieves the available supervisor names from the employee records.
     *
     * If the supervisor list cannot be generated, the method returns "N/A"
     * as the default option.
     *
     * @return an array containing available supervisor names
     */
    private static String[] getSupervisorOptions() {

        try {

            // Retrieve the list of employees who may serve as supervisors.
            return GetList.getSupervisorNames();

        } catch (IOException ex) {

            // Return a default value when supervisor records cannot be loaded.
            return new String[]{"N/A"};
        }
    }

    /**
     * Creates the employee list panel displayed in the All Employee screen.
     *
     * The panel displays basic employee information in a table and allows
     * the administrator to select an employee and open the corresponding
     * salary breakdown for the selected month and year.
     *
     * @param monthBox combo box used to select the payroll month
     * @param yearBox combo box used to select the payroll year
     * @param employeeNumberField field used to store the selected employee number
     * @param textArea text area used to display the salary report
     * @param contentCardLayout layout used to switch administrator content
     * @param contentPanel panel containing the administrator screens
     * @return the completed employee list panel
     */
    private static JPanel createEmployeeListPanel(
            JComboBox<String> monthBox,
            JComboBox<String> yearBox,
            JTextField employeeNumberField,
            JTextArea textArea,
            CardLayout contentCardLayout,
            JPanel contentPanel
    ) {

        // Main panel containing the employee table and view button.
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        try {

            // Define the column headings of the employee table.
            String[] columnNames = {
                    "ID",
                    "Last Name",
                    "First Name",
                    "Birth Date",
                    "Status",
                    "Position"
            };

            // Retrieve the basic employee information from the CSV file.
            String[][] tableData =
                    GetList.getBasicEmployeeTableData();

            // Create and configure the employee table.
            JTable table = new JTable(tableData, columnNames);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.setFont(new Font("Arial", Font.PLAIN, 13));
            table.setRowHeight(22);

            // Place the employee table inside a scroll pane.
            JScrollPane scrollPane = new JScrollPane(table);

            // Button used to view the selected employee's salary breakdown.
            JButton viewButton =
                    new JButton("View Selected Employee Salary Breakout");

            // Add the table and button to the panel.
            panel.add(scrollPane, BorderLayout.CENTER);
            panel.add(viewButton, BorderLayout.SOUTH);

            // Display the salary breakdown of the selected employee.
            viewButton.addActionListener(e -> {

                // Retrieve the currently selected employee row.
                int selectedRow = table.getSelectedRow();

                // Require the administrator to select an employee first.
                if (selectedRow == -1) {
                    DialogCustomizer.show(
                            "Please select an employee first."
                    );
                    return;
                }

                // Retrieve the employee number from the selected row.
                String employeeId =
                        table.getValueAt(selectedRow, 0).toString();

                // Convert the selected month and year into numeric values.
                int monthValue =
                        getMonthValue(
                                (String) monthBox.getSelectedItem()
                        );

                int yearValue =
                        Integer.parseInt(
                                (String) yearBox.getSelectedItem()
                        );

                // Store the selected employee number in the input field.
                employeeNumberField.setText(employeeId);

                // Generate the selected employee's salary report.
                showSalaryReport(
                        employeeId,
                        monthValue,
                        yearValue,
                        textArea
                );

                // Switch to the salary breakdown screen.
                contentCardLayout.show(
                        contentPanel,
                        "Salary_Breakout"
                );
            });

        } catch (IOException ex) {

            // Notify the administrator when the employee list cannot be loaded.
            DialogCustomizer.show("Unable to load employee list.");
        }

        return panel;
    }
    
    /**
     * Enables automatic computation of salary-related fields in the
     * Add Employee form.
     *
     * The Gross Semi-monthly Rate and Hourly Rate fields are made
     * read-only and are automatically updated whenever the Basic Salary
     * field changes.
     *
     * @param fields the employee form components
     */
    private static void makeSalaryAutoComputed(JComponent[] fields) {

        // Retrieve the salary-related text fields.
        JTextField basicSalaryField = (JTextField) fields[13];
        JTextField grossSemiMonthlyField = (JTextField) fields[17];
        JTextField hourlyRateField = (JTextField) fields[18];

        // Prevent manual editing of computed salary values.
        grossSemiMonthlyField.setEditable(false);
        hourlyRateField.setEditable(false);

        // Visually indicate that these fields are read-only.
        grossSemiMonthlyField.setBackground(new Color(235, 235, 235));
        hourlyRateField.setBackground(new Color(235, 235, 235));

        // Recalculate salary values whenever the Basic Salary changes.
        basicSalaryField.getDocument().addDocumentListener(new DocumentListener() {

            /**
             * Invoked when text is inserted into the Basic Salary field.
             */
          
            public void insertUpdate(DocumentEvent e) {
                updateComputedSalaryFields(fields);
            }

            /**
             * Invoked when text is removed from the Basic Salary field.
             */
           
            public void removeUpdate(DocumentEvent e) {
                updateComputedSalaryFields(fields);
            }

            /**
             * Invoked when the document attributes change.
             * Included to satisfy the DocumentListener interface.
             */
          
            public void changedUpdate(DocumentEvent e) {
                updateComputedSalaryFields(fields);
            }
        });
    }
    /**
     * Enables automatic computation of salary-related fields in the
     * Edit Employee form.
     *
     * The Gross Semi-monthly Rate and Hourly Rate fields are made
     * read-only and are recalculated whenever the Basic Salary changes.
     *
     * @param fields the employee form components used by the Edit Employee form
     */
    private static void makeSalaryAutoComputedForEdit(JComponent[] fields) {

        // Retrieve the salary-related fields from the edit form.
        JTextField basicSalaryField = (JTextField) fields[12];
        JTextField grossSemiMonthlyField = (JTextField) fields[16];
        JTextField hourlyRateField = (JTextField) fields[17];

        // Prevent manual editing of automatically computed values.
        grossSemiMonthlyField.setEditable(false);
        hourlyRateField.setEditable(false);

        // Visually indicate that the computed fields are read-only.
        grossSemiMonthlyField.setBackground(new Color(235, 235, 235));
        hourlyRateField.setBackground(new Color(235, 235, 235));

        // Recalculate salary values whenever the Basic Salary field changes.
        basicSalaryField.getDocument().addDocumentListener(new DocumentListener() {

            // Recalculate when text is inserted.
            public void insertUpdate(DocumentEvent e) {
                updateComputedSalaryFieldsForEdit(fields);
            }

            // Recalculate when text is removed.
            public void removeUpdate(DocumentEvent e) {
                updateComputedSalaryFieldsForEdit(fields);
            }

            // Recalculate when the document attributes change.
            public void changedUpdate(DocumentEvent e) {
                updateComputedSalaryFieldsForEdit(fields);
            }
        });
    }

    /**
     * Calculates the Gross Semi-monthly Rate and Hourly Rate for the
     * Add Employee form using the entered Basic Salary.
     *
     * The Gross Semi-monthly Rate is calculated by dividing the Basic Salary
     * by two, while the Hourly Rate is calculated using 176 monthly work hours.
     *
     * @param fields the employee form components used by the Add Employee form
     */
    private static void updateComputedSalaryFields(JComponent[] fields) {

        // Retrieve the salary-related fields from the add form.
        JTextField basicSalaryField = (JTextField) fields[13];
        JTextField grossSemiMonthlyField = (JTextField) fields[17];
        JTextField hourlyRateField = (JTextField) fields[18];

        // Remove commas and surrounding spaces before validation.
        String basicSalaryText =
                basicSalaryField.getText().replace(",", "").trim();

        // Clear computed values when the Basic Salary is not numeric.
        if (!basicSalaryText.matches("\\d+(\\.\\d+)?")) {
            grossSemiMonthlyField.setText("");
            hourlyRateField.setText("");
            return;
        }

        // Convert the validated Basic Salary into a numeric value.
        double basicSalary = Double.parseDouble(basicSalaryText);

        // Compute the employee's semi-monthly gross salary.
        double grossSemiMonthly = basicSalary / 2.0;

        // Compute the employee's hourly rate based on 176 monthly work hours.
        double hourlyRate = basicSalary / 176.0;

        // Display the computed values using two decimal places.
        grossSemiMonthlyField.setText(
                String.format("%.2f", grossSemiMonthly)
        );

        hourlyRateField.setText(
                String.format("%.2f", hourlyRate)
        );
    }

    /**
     * Calculates the Gross Semi-monthly Rate and Hourly Rate for the
     * Edit Employee form using the employee's Basic Salary.
     *
     * The Gross Semi-monthly Rate is calculated by dividing the Basic Salary
     * by two, while the Hourly Rate is calculated using 176 monthly work hours.
     *
     * @param fields the employee form components used by the Edit Employee form
     */
    private static void updateComputedSalaryFieldsForEdit(JComponent[] fields) {

        // Retrieve the salary-related fields from the edit form.
        JTextField basicSalaryField = (JTextField) fields[12];
        JTextField grossSemiMonthlyField = (JTextField) fields[16];
        JTextField hourlyRateField = (JTextField) fields[17];

        // Remove commas and surrounding spaces before validation.
        String basicSalaryText =
                basicSalaryField.getText().replace(",", "").trim();

        // Clear computed values when the Basic Salary is not numeric.
        if (!basicSalaryText.matches("\\d+(\\.\\d+)?")) {
            grossSemiMonthlyField.setText("");
            hourlyRateField.setText("");
            return;
        }

        // Convert the validated Basic Salary into a numeric value.
        double basicSalary = Double.parseDouble(basicSalaryText);

        // Compute the employee's semi-monthly gross salary.
        double grossSemiMonthly = basicSalary / 2.0;

        // Compute the employee's hourly rate based on 176 monthly work hours.
        double hourlyRate = basicSalary / 176.0;

        // Display the computed values using two decimal places.
        grossSemiMonthlyField.setText(
                String.format("%.2f", grossSemiMonthly)
        );

        hourlyRateField.setText(
                String.format("%.2f", hourlyRate)
        );
    }

    /**
     * Creates the main salary computation panel.
     *
     * The panel provides access to attendance entry, attendance viewing,
     * and salary computation for all employees. It also allows the
     * administrator to select the target month and year.
     *
     * @param monthBox the month selector from the main administrator screen
     * @param yearBox the year selector from the main administrator screen
     * @param textArea the text area used for salary report display
     * @param contentCardLayout the layout used to switch administrator screens
     * @param contentPanel the panel containing the administrator screens
     * @return the completed Compute Salaries panel
     */
    private static JPanel createComputeSalariesPanel(
            JComboBox<String> monthBox,
            JComboBox<String> yearBox,
            JTextArea textArea,
            CardLayout contentCardLayout,
            JPanel contentPanel
    ) {

        // Main panel containing the salary computation controls and content area.
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Top panel containing month, year, and module navigation controls.
        JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topButtonPanel.setBackground(Color.WHITE);

        // Create the buttons for the available salary computation functions.
        JButton attendanceEntryButton = new JButton("Attendance Entry");
        JButton attendanceViewerButton = new JButton("Attendance Viewer");
        JButton computeAllButton = new JButton("Compute All Salaries");

        // Month selector used within the Compute Salaries module.
        JComboBox<String> localMonthBox = new JComboBox<>(new String[]{
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        });

        // Year selector used within the Compute Salaries module.
        JComboBox<String> localYearBox = new JComboBox<>(new String[]{
                "2024", "2025", "2026", "2027", "2028"
        });

        // Add the selection controls and module buttons to the top panel.
        topButtonPanel.add(new JLabel("Month:"));
        topButtonPanel.add(localMonthBox);
        topButtonPanel.add(new JLabel("Year:"));
        topButtonPanel.add(localYearBox);
        topButtonPanel.add(attendanceEntryButton);
        topButtonPanel.add(attendanceViewerButton);
        topButtonPanel.add(computeAllButton);

        /*
         * Card layout used to switch between the welcome screen,
         * attendance entry, attendance viewer, and computed salary results.
         */
        CardLayout computeCardLayout = new CardLayout();
        JPanel computeContentPanel = new JPanel(computeCardLayout);

        // Create the default welcome screen of the module.
        JPanel welcomePanel = new JPanel(new GridBagLayout());
        welcomePanel.setBackground(Color.WHITE);

        JLabel welcomeLabel = new JLabel(
                "<html><h2>Compute Salaries Module</h2>" +
                        "<p>Select Attendance Entry, Attendance Viewer, or Compute All Salaries.</p></html>"
        );

        welcomePanel.add(welcomeLabel);

        // Register the initial module screens.
        computeContentPanel.add(welcomePanel, "Welcome");
        computeContentPanel.add(createAttendanceEntryPanel(), "Attendance_Entry");

        // Display the Attendance Entry screen.
        attendanceEntryButton.addActionListener(e -> {
            computeCardLayout.show(computeContentPanel, "Attendance_Entry");
        });

        // Display attendance records for the selected month and year.
        attendanceViewerButton.addActionListener(e -> {

            // Convert the selected month and year into numeric values.
            int selectedMonth =
                    getMonthValueFull(
                            (String) localMonthBox.getSelectedItem()
                    );

            int selectedYear =
                    Integer.parseInt(
                            (String) localYearBox.getSelectedItem()
                    );

            // Create the attendance viewer using the selected period.
            JPanel viewerPanel = createAttendanceViewerPanel(
                    selectedMonth,
                    selectedYear
            );

            // Register and display the attendance viewer screen.
            computeContentPanel.add(viewerPanel, "Attendance_Viewer");
            computeCardLayout.show(computeContentPanel, "Attendance_Viewer");
        });

        /*
         * Refreshes the attendance viewer whenever the selected month
         * or year changes.
         */
        ActionListener refreshAttendanceViewer = e -> {

            // Stop the refresh if the Attendance Viewer button is disabled.
            if (!attendanceViewerButton.isEnabled()) {
                return;
            }

            // Retrieve the newly selected month and year.
            int selectedMonth = getMonthValueFull(
                    (String) localMonthBox.getSelectedItem());

            int selectedYear = Integer.parseInt(
                    (String) localYearBox.getSelectedItem());

            // Recreate the viewer using the updated period.
            JPanel viewerPanel = createAttendanceViewerPanel(
                    selectedMonth,
                    selectedYear
            );

            // Replace the displayed attendance viewer.
            computeContentPanel.add(viewerPanel, "Attendance_Viewer");
            computeCardLayout.show(computeContentPanel, "Attendance_Viewer");
        };

        // Refresh attendance results when the month or year selection changes.
        localMonthBox.addActionListener(refreshAttendanceViewer);
        localYearBox.addActionListener(refreshAttendanceViewer);

        // Compute and save salaries for all employees.
        computeAllButton.addActionListener(e -> {

            // Retrieve the selected payroll month and year.
            int selectedMonth =
                    getMonthValueFull(
                            (String) localMonthBox.getSelectedItem()
                    );

            int selectedYear =
                    Integer.parseInt(
                            (String) localYearBox.getSelectedItem()
                    );

            try {

                // Process salary calculations for every employee.
                String result = ComputeSalaries.computeAllEmployeeSalaries(
                        selectedMonth,
                        selectedYear
                );

                // Display the computed salary results when processing succeeds.
                if (result.equals("success")) {

                    JOptionPane.showMessageDialog(
                            null,
                            "Salary computation completed and saved successfully.",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    // Create the table containing the saved salary computations.
                    JPanel salaryResultPanel =
                            createComputedSalaryResultPanel(
                                    selectedMonth,
                                    selectedYear
                            );

                    // Register and display the computed salary result screen.
                    computeContentPanel.add(
                            salaryResultPanel,
                            "Computed_Salary_Result"
                    );

                    computeCardLayout.show(
                            computeContentPanel,
                            "Computed_Salary_Result"
                    );

                } else {

                    // Display salary field validation errors.
                    JOptionPane.showMessageDialog(
                            null,
                            result,
                            "Validation Error",
                            JOptionPane.WARNING_MESSAGE
                    );
                }

            } catch (IOException ex) {

                // Notify the administrator when salary computation fails.
                JOptionPane.showMessageDialog(
                        null,
                        "Unable to compute salaries.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // Add the module controls and content area to the main panel.
        mainPanel.add(topButtonPanel, BorderLayout.NORTH);
        mainPanel.add(computeContentPanel, BorderLayout.CENTER);

        return mainPanel;
    }
 
    
        
    /**
     * Creates the form used to add a new employee attendance record.
     *
     * The panel collects the employee number, attendance date,
     * time-in, and time-out. It validates the entered information
     * before saving the attendance record.
     *
     * @return the completed Attendance Entry panel
     */
    private static JPanel createAttendanceEntryPanel() {

        // Main panel containing the attendance input form.
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        // Configure spacing and alignment for the form components.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,10,6,10);
        gbc.anchor = GridBagConstraints.WEST;

        // Field used to enter the employee number.
        JTextField employeeNoField = new JTextField();
        employeeNoField.setPreferredSize(new Dimension(220,25));

        // Masked field used to enter the attendance date.
        JFormattedTextField dateField =
                createMaskedField("##/##/####");

        // Masked field used to enter the employee's time-in.
        JFormattedTextField timeInField =
                createMaskedField("##:##");

        // Masked field used to enter the employee's time-out.
        JFormattedTextField timeOutField =
                createMaskedField("##:##");

        // Button used to validate and save the attendance record.
        JButton saveButton = new JButton("Add Attendance");

        // Add the employee number label and field.
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Employee #:"), gbc);

        gbc.gridx = 1;
        panel.add(employeeNoField, gbc);

        // Add the attendance date label and field.
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Date (MM/DD/YYYY):"), gbc);

        gbc.gridx = 1;
        panel.add(dateField, gbc);

        // Add the time-in label and field.
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Time In (HH:MM):"), gbc);

        gbc.gridx = 1;
        panel.add(timeInField, gbc);

        // Add the time-out label and field.
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Time Out (HH:MM):"), gbc);

        gbc.gridx = 1;
        panel.add(timeOutField, gbc);

        // Add the save button below the attendance fields.
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(saveButton, gbc);

        // Validate and save the attendance record when the button is clicked.
        saveButton.addActionListener(e -> {

            try {

                // Validate the entered attendance information.
                String validation =
                        AttendanceEntry.validateAttendance(
                                employeeNoField.getText(),
                                dateField.getText(),
                                timeInField.getText(),
                                timeOutField.getText()
                        );

                // Display the validation message when the input is invalid.
                if(!validation.equals("valid")) {

                    JOptionPane.showMessageDialog(
                            null,
                            validation,
                            "Validation Error",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                // Save the validated attendance record.
                AttendanceEntry.addAttendance(
                        employeeNoField.getText(),
                        dateField.getText(),
                        timeInField.getText(),
                        timeOutField.getText()
                );

                // Confirm that the attendance record was added successfully.
                JOptionPane.showMessageDialog(
                        null,
                        "Attendance added successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                // Clear the form after a successful save.
                employeeNoField.setText("");
                dateField.setText("");
                timeInField.setText("");
                timeOutField.setText("");

            } catch (Exception ex) {

                // Notify the administrator when the attendance cannot be saved.
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
        
            /**
     * Creates the attendance viewer panel for the selected month and year.
     *
     * The panel displays employee attendance records in a table and allows
     * the administrator to optionally filter the results using an employee
     * number.
     *
     * @param monthValue the selected attendance month represented as a number
     * @param yearValue the selected attendance year
     * @return the completed Attendance Viewer panel
     */
    private static JPanel createAttendanceViewerPanel(
            int monthValue,
            int yearValue
    ) {

        // Main panel containing the attendance filter and results table.
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Panel containing the optional employee number filter.
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(Color.WHITE);

        // Field used to filter attendance by employee number.
        JTextField employeeNoField = new JTextField();
        employeeNoField.setPreferredSize(new Dimension(160, 25));

        // Button used to apply the employee number filter.
        JButton filterButton = new JButton("Filter");

        // Add the filter controls to the panel.
        filterPanel.add(new JLabel("Employee # (optional):"));
        filterPanel.add(employeeNoField);
        filterPanel.add(filterButton);

        // Define the columns displayed in the attendance table.
        String[] columns = {
                "Employee #",
                "Last Name",
                "First Name",
                "Date",
                "Time In",
                "Time Out",
                "Status"
        };

        // Create the attendance table and place it inside a scroll pane.
        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);

        // Apply the employee number filter when the Filter button is clicked.
        filterButton.addActionListener(e -> {

            try {

                // Retrieve attendance records matching the selected period
                // and optional employee number.
                String[][] data = AttendanceViewer.getAttendanceTableData(
                        employeeNoField.getText(),
                        monthValue,
                        yearValue
                );

                // Load the filtered attendance records into the table.
                table.setModel(
                        new javax.swing.table.DefaultTableModel(
                                data,
                                columns
                        )
                );

            } catch (IOException ex) {

                // Notify the administrator when attendance data cannot be loaded.
                JOptionPane.showMessageDialog(
                        null,
                        "Unable to load attendance records.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        try {

            // Load all attendance records for the selected month and year.
            String[][] data = AttendanceViewer.getAttendanceTableData(
                    "",
                    monthValue,
                    yearValue
            );

            // Display the initial attendance records in the table.
            table.setModel(
                    new javax.swing.table.DefaultTableModel(
                            data,
                            columns
                    )
            );

        } catch (IOException ex) {

            // Notify the administrator when the initial records cannot be loaded.
            JOptionPane.showMessageDialog(
                    null,
                    "Unable to load attendance records.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        // Add the filter controls and attendance table to the main panel.
        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Converts a month name into its corresponding numeric value.
     *
     * @param month the selected month name
     * @return the month number from 1 to 12; returns 1 by default
     */
    private static int getMonthValueFull(String month) {

        // Return the numeric value corresponding to the month name.
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

    /**
     * Creates a table displaying the saved salary computation results
     * for the selected month and year.
     *
     * The table includes employee information, total hours, gross salary,
     * deductions, net salary, and the computation status.
     *
     * @param monthValue the selected payroll month represented as a number
     * @param yearValue the selected payroll year
     * @return the completed computed salary result panel
     */
    private static JPanel createComputedSalaryResultPanel(
            int monthValue,
            int yearValue
    ) {

        // Main panel containing the computed salary table.
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Define the columns displayed in the salary result table.
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

            // Retrieve the saved salary computations for the selected period.
            String[][] data = GetList.getComputedSalaryTableData(
                    monthValue,
                    yearValue
            );

            // Create and configure the computed salary table.
            JTable table = new JTable(data, columns);
            table.setFont(new Font("Arial", Font.PLAIN, 13));
            table.setRowHeight(22);

            // Add the table to the panel inside a scroll pane.
            panel.add(
                    new JScrollPane(table),
                    BorderLayout.CENTER
            );

        } catch (IOException ex) {

            // Notify the administrator when salary results cannot be loaded.
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
    

    
