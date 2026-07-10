package com.motorph.backend;

import java.io.*;
import java.util.ArrayList;

/**
 * Computes and stores the monthly salary information of all employees.
 *
 * This class reads employee records from the CSV file, validates salary
 * values, processes payroll for a selected month and year, and updates
 * each employee record with the computed payroll results.
 */
public class ComputeSalaries {

    // Location of the CSV file containing employee and payroll records.
    private static final String EMP_FILE = "src/main/resources/MotorPh.csv";

    /**
     * Computes the monthly salary of every valid employee record.
     *
     * The method reads all employee records, validates salary-related fields,
     * processes payroll using the selected month and year, updates the CSV
     * columns with payroll results, and rewrites the employee file.
     *
     * @param monthValue the selected payroll month represented as a number
     * @param yearValue the selected payroll year
     * @return "success" if all salaries are computed successfully;
     *         otherwise, a validation error message
     * @throws IOException if an error occurs while reading or writing the CSV file
     */
    public static String computeAllEmployeeSalaries(
            int monthValue,
            int yearValue
    ) throws IOException {

        // Stores all valid employee records read from the CSV file.
        ArrayList<String[]> employeeRows = new ArrayList<>();

        // Stores the CSV header row.
        String header;

        // Read employee records from the CSV file.
        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {

            // Read and store the CSV header.
            header = br.readLine();
            String line;

            // Process each employee record.
            while ((line = br.readLine()) != null) {

                // Ignore blank rows.
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Convert the current CSV row into an array of values.
                String[] values = GetList.parseCsvLine(line);

                // Include only employee records containing the required columns.
                if (values.length >= 19) {
                    employeeRows.add(values);
                }
            }
        }

        // Validate the salary-related fields of all employee records.
        String validationMessage = validateEmployeeRows(employeeRows);

        // Stop the computation if invalid salary information is found.
        if (!validationMessage.equals("valid")) {
            return validationMessage;
        }

        // Add payroll result columns to the header when they are not yet present.
        header = updateHeader(header);

        // Stores the employee records after payroll information is added.
        ArrayList<String[]> updatedRows = new ArrayList<>();

        // Compute payroll for each employee.
        for (String[] employee : employeeRows) {

            // Retrieve the employee number used for payroll processing.
            String employeeNo = employee[0];

            // Process the employee's payroll for the selected month and year.
            Admin payroll = ProcessPayroll.admin(
                    employeeNo,
                    monthValue,
                    yearValue
            );

            // Ensure that the employee row contains space for all payroll columns.
            String[] updatedEmployee = resizeRow(employee, 26);

            // Add computed payroll values when attendance records are available.
            if (payroll != null) {

                // Combine the working hours from both payroll cut-offs.
                double totalHours =
                        payroll.gettotalHours() +
                        payroll.gettotalHours2();

                // Combine the gross salaries from both payroll cut-offs.
                double totalGross =
                        payroll.getgrossSalary() +
                        payroll.getgrossSalary2();

                // Calculate the total government and tax deductions.
                double totalDeductions =
                        payroll.getSSS() +
                        payroll.getphilHealth() +
                        payroll.getpagIbig() +
                        payroll.gettax();

                // Calculate the employee's stored net salary value.
                double netSalary =
                        payroll.getnetSalary() +
                        payroll.getgrossSalary();

                // Store the payroll computation results in the employee row.
                updatedEmployee[19] = ProcessPayroll.month(monthValue);
                updatedEmployee[20] = String.valueOf(yearValue);
                updatedEmployee[21] = String.format("%.2f", totalHours);
                updatedEmployee[22] = String.format("%.2f", totalGross);
                updatedEmployee[23] = String.format("%.2f", totalDeductions);
                updatedEmployee[24] = String.format("%.2f", netSalary);
                updatedEmployee[25] = "Computed";

            } else {

                /*
                 * Store zero values and mark the employee as having no attendance
                 * when payroll information cannot be generated.
                 */
                updatedEmployee[19] = ProcessPayroll.month(monthValue);
                updatedEmployee[20] = String.valueOf(yearValue);
                updatedEmployee[21] = "0";
                updatedEmployee[22] = "0";
                updatedEmployee[23] = "0";
                updatedEmployee[24] = "0";
                updatedEmployee[25] = "No Attendance";
            }

            // Add the updated employee record to the output list.
            updatedRows.add(updatedEmployee);
        }

        // Rewrite the employee CSV file with the updated payroll information.
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(EMP_FILE))) {

            // Write the updated CSV header.
            bw.write(header);
            bw.newLine();

            // Write each updated employee record.
            for (String[] row : updatedRows) {
                bw.write(GetList.convertToCsvLine(row));
                bw.newLine();
            }
        }

        // Sort the updated CSV file by employee number.
        GetList.sortCsvByEmployeeNumber();

        return "success";
    }

    /**
     * Computes all employee salaries using 2024 as the default year.
     *
     * @param monthValue the selected payroll month represented as a number
     * @return "success" if computation is completed;
     *         otherwise, a validation error message
     * @throws IOException if an error occurs while accessing the CSV file
     */
    public static String computeAllEmployeeSalaries(int monthValue)
            throws IOException {

        // Call the main computation method using the default year.
        return computeAllEmployeeSalaries(monthValue, 2024);
    }

    /**
     * Validates the salary-related fields of all employee records.
     *
     * Salary fields must contain positive numeric values, with optional
     * decimal portions. All detected validation errors are combined into
     * one message.
     *
     * @param rows the employee records to validate
     * @return "valid" when all salary fields are numeric;
     *         otherwise, a detailed error message
     */
    private static String validateEmployeeRows(ArrayList<String[]> rows) {

        // Stores all salary validation errors.
        String errorMessage = "";

        // Labels corresponding to the salary fields at indexes 13 to 18.
        String[] salaryLabels = {
                "Basic Salary",
                "Rice Subsidy",
                "Phone Allowance",
                "Clothing Allowance",
                "Gross Semi-monthly Rate",
                "Hourly Rate"
        };

        // Validate the salary fields of each employee record.
        for (String[] row : rows) {

            String employeeNo = row[0];

            for (int i = 13; i <= 18; i++) {

                // Remove formatting characters before validating the value.
                String value = row[i]
                        .replace(",", "")
                        .replace("\"", "")
                        .trim();

                // Record an error when the salary value is not numeric.
                if (!value.matches("\\d+(\\.\\d+)?")) {
                    errorMessage += "Employee #" + employeeNo + " - "
                            + salaryLabels[i - 13]
                            + " must be numeric.\n";
                }
            }
        }

        // Return all collected salary validation errors.
        if (!errorMessage.isEmpty()) {
            return errorMessage;
        }

        return "valid";
    }

    /**
     * Adds payroll computation column names to the CSV header.
     *
     * The existing header is returned unchanged when it already contains
     * at least 26 columns.
     *
     * @param header the current CSV header
     * @return the original or updated CSV header
     */
    private static String updateHeader(String header) {

        // Convert the header into an array to determine its number of columns.
        String[] headers = GetList.parseCsvLine(header);

        // Return the existing header if payroll columns are already present.
        if (headers.length >= 26) {
            return header;
        }

        // Append the required payroll result column names.
        return header +
                ",Computed Month" +
                ",Computed Year" +
                ",Computed Total Hours" +
                ",Computed Gross Salary" +
                ",Computed Total Deductions" +
                ",Computed Net Salary" +
                ",Computation Status";
    }

    /**
     * Creates a new employee row with the required number of columns.
     *
     * Existing values are copied into the new row, while additional
     * positions are initialized with empty strings.
     *
     * @param oldRow the original employee record
     * @param newSize the required number of columns
     * @return a resized copy of the employee record
     */
    private static String[] resizeRow(String[] oldRow, int newSize) {

        // Create a new row using the requested size.
        String[] newRow = new String[newSize];

        // Copy existing values and initialize extra columns.
        for (int i = 0; i < newSize; i++) {

            if (i < oldRow.length) {
                newRow[i] = oldRow[i];
            } else {
                newRow[i] = "";
            }
        }

        return newRow;
    }
}
