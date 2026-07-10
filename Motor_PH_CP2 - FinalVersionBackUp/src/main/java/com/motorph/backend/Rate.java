package com.motorph.backend;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Retrieves an employee's hourly rate from the employee CSV file.
 *
 * This class searches the employee records using the employee number
 * and returns the corresponding hourly rate used for payroll
 * computations.
 */
public class Rate {

    /**
     * Retrieves the hourly rate of a specific employee.
     *
     * The method searches the employee CSV file for the matching
     * employee number and extracts the hourly rate stored in
     * column 18.
     *
     * @param id_Number the employee number to search for
     * @return the employee's hourly rate; returns 0.0 if the employee
     *         is not found
     * @throws IOException if the employee CSV file cannot be read
     */
    public static double getRate(String id_Number) throws IOException {

        // Location of the CSV file containing employee records.
        String empFile = "src/main/resources/MotorPh.csv";

        String line = "";

        // Stores the employee's hourly rate.
        double rate = 0.0;

        // Open the employee CSV file for reading.
        try (BufferedReader br = new BufferedReader(new FileReader(empFile))) {

            // Skip the CSV header row.
            br.readLine();

            // Read each employee record.
            while ((line = br.readLine()) != null) {

                /*
                 * Split the CSV row while preserving commas enclosed
                 * within quotation marks.
                 */
                String[] values =
                        line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                // Return the hourly rate when the employee number matches.
                if (id_Number.equals(values[0])) {

                    // Remove quotation marks before converting the value.
                    rate = Double.parseDouble(values[18].replace("\"", ""));

                    return rate;
                }
            }
        }

        // Return the default value when no matching employee is found.
        return rate;
    }
}
