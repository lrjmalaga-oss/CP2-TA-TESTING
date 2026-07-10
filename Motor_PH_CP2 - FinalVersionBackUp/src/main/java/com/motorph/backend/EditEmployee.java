package com.motorph.backend;

import java.io.*;
import java.util.ArrayList;

/**
 * Handles employee record searching, validation, and updating.
 *
 * This class reads employee information from the MotorPH CSV file,
 * validates edited employee details, and replaces the corresponding
 * employee record when changes are saved.
 */
public class EditEmployee {

    // Location of the CSV file containing employee records.
    private static final String EMP_FILE = "src/main/resources/MotorPh.csv";

    /**
     * Searches for an employee using the employee number.
     *
     * The method reads each employee record from the CSV file and returns
     * the complete record when the employee number matches.
     *
     * @param employeeNo the employee number to search for
     * @return the employee record as a String array, or null if not found
     * @throws IOException if the employee CSV file cannot be read
     */
    public static String[] searchEmployee(String employeeNo) throws IOException {

        // Open the employee CSV file for reading.
        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {

            // Skip the CSV header row.
            br.readLine();
            String line;

            // Read and examine each employee record.
            while ((line = br.readLine()) != null) {

                // Ignore blank rows.
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Convert the CSV row into an array of employee values.
                String[] values = GetList.parseCsvLine(line);

                // Return the record when the employee number matches.
                if (values.length >= 19 && values[0].trim().equals(employeeNo.trim())) {
                    return values;
                }
            }
        }

        // Return null when no matching employee record is found.
        return null;
    }

    /**
     * Validates employee information before an edited record is saved.
     *
     * The method checks the required formats for the employee number,
     * birthday, contact details, government identification numbers,
     * and salary-related fields.
     *
     * @param data the edited employee information to validate
     * @return "valid" when all fields are acceptable;
     *         otherwise, an error message
     */
    public static String validateEditedFields(String[] data) {

        // Employee number must contain exactly five digits.
        if (!data[0].matches("\\d{5}")) {
            return "Invalid Employee #. Must be exactly 5 digits.";
        }

        // Employee numbers must begin from 10001.
        if (Integer.parseInt(data[0]) < 10001) {
            return "Invalid Employee #. Must be 10001 or higher.";
        }

        // Validate the employee birthday format.
        if (!data[3].matches("\\d{2}/\\d{2}/\\d{4}")) {
            return "Invalid Birthday. Format must be MM/DD/YYYY.";
        }

        // Validate the employee phone number format.
        if (!data[5].matches("\\d{3}-\\d{3}-\\d{3}")) {
            return "Invalid Phone Number. Format must be 000-000-000.";
        }

        // Validate the SSS number format.
        if (!data[6].matches("\\d{2}-\\d{7}-\\d")) {
            return "Invalid SSS Number. Format must be 00-0000000-0.";
        }

        // PhilHealth number must contain exactly 12 digits.
        if (!data[7].matches("\\d{12}")) {
            return "Invalid PhilHealth Number. Must be 12 digits.";
        }

        // Validate the TIN number format.
        if (!data[8].matches("\\d{3}-\\d{3}-\\d{3}-\\d{3}")) {
            return "Invalid TIN Number. Format must be 000-000-000-000.";
        }

        // Pag-IBIG number must contain exactly 12 digits.
        if (!data[9].matches("\\d{12}")) {
            return "Invalid Pag-IBIG Number. Must be 12 digits.";
        }

        // Validate all salary-related fields.
        for (int i = 13; i <= 18; i++) {

            // Remove commas before checking whether the value is numeric.
            String number = data[i].replace(",", "");

            if (!number.matches("\\d+(\\.\\d+)?")) {
                return "Invalid Salary Field. Salary values must be numeric.";
            }
        }

        return "valid";
    }

    /**
     * Updates an existing employee record in the CSV file.
     *
     * The method reads all employee records, replaces the matching record
     * with the edited data, and rewrites the CSV file when the employee is
     * successfully found.
     *
     * @param updatedData the edited employee information to save
     * @return true if the employee record was found and updated;
     *         otherwise, false
     * @throws IOException if the employee CSV file cannot be read or written
     */
    public static boolean updateEmployee(String[] updatedData) throws IOException {

        // Stores the CSV header and all employee records.
        ArrayList<String> lines = new ArrayList<>();

        // Tracks whether the target employee record is found.
        boolean found = false;

        // Read all records from the employee CSV file.
        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {

            // Preserve the CSV header row.
            String header = br.readLine();
            lines.add(header);

            String line;

            // Process each employee record.
            while ((line = br.readLine()) != null) {

                // Ignore blank rows.
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Convert the current CSV row into an array of values.
                String[] values = GetList.parseCsvLine(line);

                // Replace the matching employee record with the edited data.
                if (values.length >= 19 && values[0].trim().equals(updatedData[0].trim())) {
                    lines.add(GetList.convertToCsvLine(updatedData));
                    found = true;
                } else {

                    // Preserve records belonging to other employees.
                    lines.add(line);
                }
            }
        }

        // Rewrite the file only when the employee record was found.
        if (found) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(EMP_FILE))) {

                // Write the header and all updated employee records.
                for (String line : lines) {
                    bw.write(line);
                    bw.newLine();
                }
            }
        }

        return found;
    }
}
