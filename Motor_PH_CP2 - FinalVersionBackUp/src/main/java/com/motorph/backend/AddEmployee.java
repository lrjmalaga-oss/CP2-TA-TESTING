package com.motorph.backend;

import java.io.*;
import java.util.ArrayList;

/**
 * Handles the addition of new employee records to the MotorPH CSV file.
 *
 * This class generates available employee numbers, checks for duplicate
 * employee records, validates employee information, and writes valid
 * employee data to the CSV file.
 */
public class AddEmployee {

    // Location of the CSV file containing the employee records.
    private static final String EMP_FILE = "src/main/resources/MotorPh.csv";

    /**
     * Generates the next available five-digit employee number.
     *
     * The method reads all existing employee numbers from the CSV file,
     * marks the numbers already in use, and returns the first available
     * employee number starting from 10001.
     *
     * @return the next available employee number as a String
     * @throws IOException if the employee CSV file cannot be read
     */
    public static String generateNextEmployeeNumber() throws IOException {

        // Tracks employee numbers that are already assigned.
        boolean[] usedNumbers = new boolean[100000];

        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {

            // Skip the CSV header row.
            br.readLine();
            String line;

            // Read each employee record from the CSV file.
            while ((line = br.readLine()) != null) {

                // Ignore blank rows.
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Parse the CSV row while preserving values containing commas.
                String[] values = GetList.parseCsvLine(line);

                // Check whether the employee number is present and valid.
                if (values.length > 0 && values[0].matches("\\d{5}")) {

                    int employeeNumber = Integer.parseInt(values[0]);

                    // Mark valid employee numbers as already used.
                    if (employeeNumber >= 10001 && employeeNumber <= 99999) {
                        usedNumbers[employeeNumber] = true;
                    }
                }
            }
        }

        // Return the first available employee number.
        for (int i = 10001; i <= 99999; i++) {
            if (!usedNumbers[i]) {
                return String.valueOf(i);
            }
        }

        // Return the maximum employee number if no available number is found.
        return "99999";
    }

    /**
     * Checks whether an employee number already exists in the CSV file.
     *
     * @param employeeNo the employee number to search for
     * @return true if the employee number exists; otherwise, false
     * @throws IOException if the employee CSV file cannot be read
     */
    public static boolean employeeExists(String employeeNo) throws IOException {

        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {

            // Skip the CSV header row.
            br.readLine();
            String line;

            // Search each employee record.
            while ((line = br.readLine()) != null) {

                // Ignore blank rows.
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = GetList.parseCsvLine(line);

                // Compare the stored employee number with the entered value.
                if (values.length > 0 && values[0].trim().equals(employeeNo.trim())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Validates all employee information before it is saved.
     *
     * The method checks required fields, employee number format, birthday,
     * contact information, government identification numbers, and salary
     * values.
     *
     * @param data the employee information entered by the user
     * @return "valid" when all values are acceptable; otherwise, an error message
     */
    public static String validateEmployeeData(String[] data) {

        // Ensure that every required employee field contains a value.
        if (hasEmptyField(data)) {
            return "Please complete all employee fields.";
        }

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

        // Stores all salary-related validation errors.
        String errorMessage = "";

        // Labels corresponding to salary fields stored at indexes 13 to 18.
        String[] salaryLabels = {
                "Basic Salary",
                "Rice Subsidy",
                "Phone Allowance",
                "Clothing Allowance",
                "Gross Semi-monthly Rate",
                "Hourly Rate"
        };

        // Validate that all salary-related values are numeric.
        for (int i = 13; i <= 18; i++) {

            // Remove commas before validating the numeric value.
            String value = data[i].replace(",", "").trim();

            if (!value.matches("\\d+(\\.\\d+)?")) {
                errorMessage += salaryLabels[i - 13] + " must be numeric.\n";
            }
        }

        // Return all collected salary validation errors.
        if (!errorMessage.isEmpty()) {
            return errorMessage;
        }

        return "valid";
    }

    /**
     * Appends a validated employee record to the CSV file.
     *
     * @param data the employee information to save
     * @throws IOException if the employee CSV file cannot be written
     */
    public static void addEmployee(String[] data) throws IOException {

        File file = new File(EMP_FILE);

        // Open the employee file in append mode.
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {

            // Add a line break if the existing file does not end with one.
            if (file.length() > 0 && needsNewLine(file)) {
                bw.newLine();
            }

            // Convert the employee data into CSV format and save it.
            bw.write(GetList.convertToCsvLine(data));
            bw.newLine();
        }
    }

    /**
     * Checks whether the CSV file requires a new line before appending data.
     *
     * @param file the CSV file to inspect
     * @return true if the last character is not a line break; otherwise, false
     * @throws IOException if the file cannot be accessed
     */
    private static boolean needsNewLine(File file) throws IOException {

        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {

            // An empty file does not require an additional line break.
            if (raf.length() == 0) {
                return false;
            }

            // Read the final byte of the file.
            raf.seek(raf.length() - 1);
            int lastByte = raf.read();

            // Determine whether the file already ends with a line break.
            return lastByte != '\n' && lastByte != '\r';
        }
    }

    /**
     * Checks whether any employee field is null or empty.
     *
     * @param data the employee information to inspect
     * @return true if at least one field is empty; otherwise, false
     */
    private static boolean hasEmptyField(String[] data) {

        // Examine every field in the employee data array.
        for (int i = 0; i < data.length; i++) {
            if (data[i] == null || data[i].trim().isEmpty()) {
                return true;
            }
        }

        return false;
    }
}


