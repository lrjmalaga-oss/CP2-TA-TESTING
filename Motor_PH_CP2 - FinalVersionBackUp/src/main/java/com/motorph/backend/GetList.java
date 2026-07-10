package com.motorph.backend;

import java.io.*;
import java.util.ArrayList;

/**
 * Provides utility methods for reading, organizing, converting,
 * and displaying employee information stored in the MotorPH CSV file.
 *
 * This class retrieves employee records, prepares table data,
 * extracts unique values, handles CSV formatting, sorts employee records,
 * and retrieves previously computed salary information.
 */
public class GetList {

    // Location of the CSV file containing employee and payroll records.
    private static final String EMP_FILE = "src/main/resources/MotorPh.csv";

    /**
     * Reads the employee CSV file and creates a list of Employee objects.
     *
     * Only the employee number, last name, first name, and birthday
     * are retrieved for each Employee object.
     *
     * @return an ArrayList containing all valid employee records
     * @throws IOException if the employee CSV file cannot be read
     */
    public static ArrayList<Employee> GetList() throws IOException {

        // Stores all employee objects created from the CSV records.
        ArrayList<Employee> employeeList = new ArrayList<>();

        // Open the employee CSV file for reading.
        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {

            // Skip the CSV header row.
            br.readLine();
            String line;

            // Read each employee record.
            while ((line = br.readLine()) != null) {

                // Ignore blank rows.
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Convert the CSV row into an array of values.
                String[] values = parseCsvLine(line);

                // Create an Employee object when the required fields are present.
                if (values.length >= 4) {
                    employeeList.add(
                            new Employee(
                                    values[0],
                                    values[1],
                                    values[2],
                                    values[3]
                            )
                    );
                }
            }
        }

        return employeeList;
    }

    /**
     * Prepares employee and payroll information for display in a table.
     *
     * The method processes payroll for every employee using the selected
     * month. When no payroll record is available, the salary values are
     * displayed as zero.
     *
     * @param monthValue the selected payroll month represented as a number
     * @return a two-dimensional array containing employee and payroll data
     * @throws IOException if an error occurs while reading employee
     *                     or attendance records
     */
    public static String[][] getTableData(int monthValue) throws IOException {

        // Retrieve the basic employee list.
        ArrayList<Employee> list = GetList();

        // Create an eight-column table for employee and payroll information.
        String[][] tableData = new String[list.size()][8];

        // Prepare the table row for each employee.
        for (int i = 0; i < list.size(); i++) {

            Employee emp = list.get(i);

            // Process the employee's payroll for the selected month.
            Admin payroll = ProcessPayroll.admin(emp.getid(), monthValue);

            // Store the employee's basic information.
            tableData[i][0] = emp.getid();
            tableData[i][1] = emp.getLname();
            tableData[i][2] = emp.getFname();
            tableData[i][3] = emp.getBday();

            // Store the computed payroll information when available.
            if (payroll != null) {
                tableData[i][4] = String.valueOf(payroll.gettotalHours());
                tableData[i][5] = String.valueOf(payroll.getgrossSalary());
                tableData[i][6] = String.valueOf(payroll.getnetSalary());
                tableData[i][7] = String.valueOf(payroll.gettax());
            } else {

                // Use zero values when payroll cannot be generated.
                tableData[i][4] = "0";
                tableData[i][5] = "0";
                tableData[i][6] = "0";
                tableData[i][7] = "0";
            }
        }

        return tableData;
    }

    /**
     * Retrieves all unique values from a selected CSV column.
     *
     * This method may be used to populate selection components such as
     * combo boxes for employee status, position, or other employee fields.
     *
     * @param columnIndex the index of the CSV column to retrieve
     * @return an array containing the unique non-empty values
     * @throws IOException if the employee CSV file cannot be read
     */
    public static String[] getUniqueCsvValues(int columnIndex) throws IOException {

        // Stores unique values found in the selected CSV column.
        ArrayList<String> valuesList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {

            // Skip the CSV header row.
            br.readLine();
            String line;

            // Read each employee record.
            while ((line = br.readLine()) != null) {

                // Ignore blank rows.
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = parseCsvLine(line);

                // Ensure that the requested column exists.
                if (values.length > columnIndex) {

                    String value = values[columnIndex].trim();

                    // Add only non-empty values that are not already stored.
                    if (!value.isEmpty() && !valuesList.contains(value)) {
                        valuesList.add(value);
                    }
                }
            }
        }

        // Convert the list of unique values into a String array.
        return valuesList.toArray(new String[0]);
    }

    /**
     * Retrieves the names of employees who may be selected as supervisors.
     *
     * Employees whose position does not contain "rank and file" are included
     * in the supervisor list. The default value "N/A" is also included.
     *
     * @return an array containing available supervisor names
     * @throws IOException if the employee CSV file cannot be read
     */
    public static String[] getSupervisorNames() throws IOException {

        // Stores the available supervisor names.
        ArrayList<String> supervisorList = new ArrayList<>();

        // Add a default option for employees without a supervisor.
        supervisorList.add("N/A");

        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {

            // Skip the CSV header row.
            br.readLine();
            String line;

            // Read each employee record.
            while ((line = br.readLine()) != null) {

                // Ignore blank rows.
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = parseCsvLine(line);

                // Ensure that the position column exists.
                if (values.length > 11) {

                    String position = values[11].trim();

                    // Exclude employees whose position is rank and file.
                    if (!position.toLowerCase().contains("rank and file")) {

                        // Combine the employee's first and last names.
                        String fullName =
                                values[2].trim() + " " + values[1].trim();

                        // Prevent duplicate names from being added.
                        if (!supervisorList.contains(fullName)) {
                            supervisorList.add(fullName);
                        }
                    }
                }
            }
        }

        return supervisorList.toArray(new String[0]);
    }

    /**
     * Parses a single CSV row into individual values.
     *
     * Commas enclosed within quotation marks are treated as part of the
     * field value instead of column separators.
     *
     * @param line the CSV row to parse
     * @return an array containing the separated CSV values
     */
    public static String[] parseCsvLine(String line) {

        // Stores the values extracted from the CSV row.
        ArrayList<String> values = new ArrayList<>();

        // Builds the current field value one character at a time.
        StringBuilder currentValue = new StringBuilder();

        // Tracks whether the current character is inside quotation marks.
        boolean insideQuotes = false;

        // Examine each character in the CSV row.
        for (int i = 0; i < line.length(); i++) {

            char currentChar = line.charAt(i);

            // Toggle the quotation state when a quotation mark is found.
            if (currentChar == '"') {
                insideQuotes = !insideQuotes;

            // Treat a comma outside quotation marks as a column separator.
            } else if (currentChar == ',' && !insideQuotes) {
                values.add(currentValue.toString());
                currentValue.setLength(0);

            } else {

                // Add the current character to the current field value.
                currentValue.append(currentChar);
            }
        }

        // Add the final field after the loop finishes.
        values.add(currentValue.toString());

        return values.toArray(new String[0]);
    }

    /**
     * Converts an array of values into a properly formatted CSV row.
     *
     * Values containing commas or quotation marks are enclosed in quotation
     * marks, and existing quotation marks are escaped.
     *
     * @param row the array of values to convert
     * @return the values formatted as one CSV row
     */
    public static String convertToCsvLine(String[] row) {

        // Builds the complete CSV row.
        StringBuilder line = new StringBuilder();

        // Convert each array element into a CSV field.
        for (int i = 0; i < row.length; i++) {

            String value = row[i];

            // Escape values containing commas or quotation marks.
            if (value.contains(",") || value.contains("\"")) {
                value = "\"" + value.replace("\"", "\"\"") + "\"";
            }

            line.append(value);

            // Add a comma between values, except after the final field.
            if (i < row.length - 1) {
                line.append(",");
            }
        }

        return line.toString();
    }

    /**
     * Retrieves basic employee information for display in a table.
     *
     * The returned table includes the employee number, last name,
     * first name, birthday, employment status, and position.
     *
     * @return a two-dimensional array containing basic employee information
     * @throws IOException if the employee CSV file cannot be read
     */
    public static String[][] getBasicEmployeeTableData() throws IOException {

        // Stores employee records containing the required columns.
        ArrayList<String[]> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {

            // Skip the CSV header row.
            br.readLine();
            String line;

            // Read each employee record.
            while ((line = br.readLine()) != null) {

                // Ignore blank rows.
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = parseCsvLine(line);

                // Include only records containing the required employee fields.
                if (values.length >= 12) {
                    list.add(values);
                }
            }
        }

        // Create a six-column table for basic employee information.
        String[][] tableData = new String[list.size()][6];

        // Copy the required employee fields into the table.
        for (int i = 0; i < list.size(); i++) {

            String[] emp = list.get(i);

            tableData[i][0] = emp[0];
            tableData[i][1] = emp[1];
            tableData[i][2] = emp[2];
            tableData[i][3] = emp[3];
            tableData[i][4] = emp[10];
            tableData[i][5] = emp[11];
        }

        return tableData;
    }

    /**
     * Sorts all employee records in ascending order by employee number.
     *
     * The method preserves the CSV header, sorts the employee records,
     * and rewrites the employee CSV file using the new order.
     *
     * @throws IOException if the employee CSV file cannot be read or written
     */
    public static void sortCsvByEmployeeNumber() throws IOException {

        // Stores all employee records before sorting.
        ArrayList<String[]> employeeRows = new ArrayList<>();

        // Stores the CSV header row.
        String header;

        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {

            // Preserve the CSV header.
            header = br.readLine();
            String line;

            // Read every employee record.
            while ((line = br.readLine()) != null) {

                // Ignore blank rows.
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = parseCsvLine(line);

                // Include rows containing at least an employee number.
                if (values.length > 0) {
                    employeeRows.add(values);
                }
            }
        }

        // Sort the records numerically using the employee number.
        employeeRows.sort((a, b) ->
                Integer.compare(
                        Integer.parseInt(a[0].trim()),
                        Integer.parseInt(b[0].trim())
                )
        );

        // Rewrite the CSV file using the sorted employee records.
        try (BufferedWriter bw =
                     new BufferedWriter(new FileWriter(EMP_FILE))) {

            // Write the original CSV header.
            bw.write(header);
            bw.newLine();

            // Write each sorted employee record.
            for (String[] row : employeeRows) {
                bw.write(convertToCsvLine(row));
                bw.newLine();
            }
        }
    }

    /**
     * Retrieves stored salary computations for a selected month and year.
     *
     * Only employee records whose computed month and year match the selected
     * values are included in the returned table.
     *
     * @param monthValue the selected payroll month represented as a number
     * @param yearValue the selected payroll year
     * @return a two-dimensional array containing computed salary information
     * @throws IOException if the employee CSV file cannot be read
     */
    public static String[][] getComputedSalaryTableData(
            int monthValue,
            int yearValue
    ) throws IOException {

        // Stores records matching the selected computation period.
        ArrayList<String[]> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {

            // Skip the CSV header row.
            br.readLine();
            String line;

            // Read each employee record.
            while ((line = br.readLine()) != null) {

                // Ignore blank rows.
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = parseCsvLine(line);

                // Process only records containing computed salary columns.
                if (values.length >= 26) {

                    // Convert the selected month number into its month name.
                    String monthName = ProcessPayroll.month(monthValue);

                    // Include records matching the selected month and year.
                    if (values[19].equals(monthName)
                            && values[20].equals(String.valueOf(yearValue))) {
                        list.add(values);
                    }
                }
            }
        }

        // Create a ten-column table for computed salary information.
        String[][] tableData = new String[list.size()][10];

        // Copy the required employee and payroll fields into the table.
        for (int i = 0; i < list.size(); i++) {

            String[] row = list.get(i);

            tableData[i][0] = row[0];
            tableData[i][1] = row[1];
            tableData[i][2] = row[2];
            tableData[i][3] = row[19];
            tableData[i][4] = row[20];
            tableData[i][5] = row[21];
            tableData[i][6] = row[22];
            tableData[i][7] = row[23];
            tableData[i][8] = row[24];
            tableData[i][9] = row[25];
        }

        return tableData;
    }
}

            return tableData;
        }
    
    
}
