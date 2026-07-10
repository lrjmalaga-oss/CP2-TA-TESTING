package com.motorph.backend;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Handles employee lookup and payroll processing for the MotorPH application.
 *
 * This class retrieves employee details, calculates hours worked for each
 * payroll cut-off, computes gross salary, applies deductions, calculates
 * net salary, and returns the complete payroll summary.
 */
public class ProcessPayroll {

    /**
     * Retrieves the basic details of an employee using the employee number.
     *
     * The method reads the employee CSV file and returns an Employee object
     * when a matching employee number is found.
     *
     * @param id_Number the employee number to search for
     * @return the matching Employee object, or null if no employee is found
     * @throws IOException if the employee CSV file cannot be read
     */
    public static Employee employeeDetails(String id_Number) throws IOException {

        // Location of the CSV file containing employee records.
        String empFile = "src/main/resources/MotorPh.csv";

        String line;

        // Open the employee CSV file for reading.
        try (BufferedReader br = new BufferedReader(new FileReader(empFile))) {

            // Skip the CSV header row.
            br.readLine();

            // Read each employee record.
            while ((line = br.readLine()) != null) {

                // Ignore blank rows.
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Convert the CSV row into an array of values.
                String[] values = GetList.parseCsvLine(line);

                // Skip incomplete employee records.
                if (values.length < 4) {
                    continue;
                }

                // Return the employee record when the employee number matches.
                if (id_Number.equals(values[0])) {
                    return new Employee(
                            values[0],
                            values[1],
                            values[2],
                            values[3]
                    );
                }
            }
        }

        // Return null when the employee number is not found.
        return null;
    }

    /**
     * Processes an employee's payroll using 2024 as the default year.
     *
     * @param id_Number the employee number
     * @param targetMonth the selected payroll month represented as a number
     * @return the employee's payroll summary, or null if no attendance is found
     * @throws IOException if an employee or attendance file cannot be read
     */
    public static Admin admin(String id_Number, int targetMonth)
            throws IOException {

        // Call the main payroll method using the default year.
        return admin(id_Number, targetMonth, 2024);
    }

    /**
     * Processes the employee's payroll for a selected month and year.
     *
     * Attendance records are divided into two payroll cut-offs:
     * days 1 to 15 and days 16 to 31. The method calculates total hours,
     * gross salary, deductions, and net salary for the selected period.
     *
     * @param id_Number the employee number
     * @param targetMonth the selected payroll month represented as a number
     * @param targetYear the selected payroll year
     * @return an Admin object containing the complete payroll summary,
     *         or null if no matching attendance is found
     * @throws IOException if the attendance or employee files cannot be read
     */
    public static Admin admin(
            String id_Number,
            int targetMonth,
            int targetYear
    ) throws IOException {

        // Location of the CSV file containing attendance records.
        String attendance = "src/main/resources/attendance.csv";

        String line;

        // Store the total hours worked during each payroll cut-off.
        double firstCutoffHours = 0.0;
        double secondCutoffHours = 0.0;

        // Tracks whether matching attendance was found.
        boolean employeeFound = false;

        // Open the attendance CSV file for reading.
        try (BufferedReader br =
                     new BufferedReader(new FileReader(attendance))) {

            // Skip the CSV header row.
            br.readLine();

            // Read each attendance record.
            while ((line = br.readLine()) != null) {

                // Ignore blank rows.
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Convert the attendance CSV row into an array of values.
                String[] values = GetList.parseCsvLine(line);

                // Skip incomplete attendance records.
                if (values.length < 6) {
                    continue;
                }

                // Process only attendance belonging to the selected employee.
                if (id_Number.equals(values[0])) {

                    // Separate the month, day, and year from the attendance date.
                    String[] dateParts = values[3].split("/");

                    int month = Integer.parseInt(dateParts[0]);
                    int day = Integer.parseInt(dateParts[1]);
                    int year = Integer.parseInt(dateParts[2]);

                    // Include only attendance matching the selected month and year.
                    if (month == targetMonth && year == targetYear) {

                        employeeFound = true;

                        // Calculate the payable working hours for the day.
                        double dailyHours =
                                CalculationForHours.totalHours(
                                        values[4],
                                        values[5]
                                );

                        // Add hours worked from days 1 to 15 to the first cut-off.
                        if (day >= 1 && day <= 15) {
                            firstCutoffHours += dailyHours;

                        // Add hours worked from days 16 to 31 to the second cut-off.
                        } else if (day >= 16 && day <= 31) {
                            secondCutoffHours += dailyHours;
                        }
                    }
                }
            }
        }

        // Stop payroll processing when no matching attendance is found.
        if (!employeeFound) {
            return null;
        }

        // Retrieve the employee's hourly rate.
        double hourlyRate = Rate.getRate(id_Number);

        // Create the display labels for both payroll cut-offs.
        String firstCutoff = month(targetMonth) + " 1 - 15";
        String secondCutoff = month(targetMonth) + " 16 - 31";

        // Calculate the gross salary for each cut-off.
        double firstGrossSalary =
                calculateGross(firstCutoffHours, hourlyRate);

        double secondGrossSalary =
                calculateGross(secondCutoffHours, hourlyRate);

        // Calculate the employee's total monthly gross salary.
        double combinedGrossSalary =
                firstGrossSalary + secondGrossSalary;

        // Compute mandatory government deductions.
        double sss =
                Deductions.computeSSS(combinedGrossSalary);

        double philHealth =
                Deductions.computePhilhealth(combinedGrossSalary);

        double pagIbig =
                Deductions.computePagibig(combinedGrossSalary);

        // Calculate taxable income after government contributions.
        double taxableIncome =
                combinedGrossSalary - sss - philHealth - pagIbig;

        // Compute the monthly withholding tax.
        double tax =
                Deductions.computeMonthlyTax(taxableIncome);

        // Combine all payroll deductions.
        double totalDeductions =
                Deductions.computeTotalDeductions(
                        sss,
                        philHealth,
                        pagIbig,
                        tax
                );

        // No deductions are applied to the first cut-off salary.
        double firstNetSalary = firstGrossSalary;

        // Apply all monthly deductions to the second cut-off salary.
        double secondNetSalary =
                calculateNet(secondGrossSalary, totalDeductions);

        // Return the complete payroll summary.
        return new Admin(
                firstCutoff,
                firstCutoffHours,
                firstGrossSalary,
                firstNetSalary,
                secondCutoff,
                secondCutoffHours,
                secondGrossSalary,
                sss,
                philHealth,
                pagIbig,
                tax,
                secondNetSalary
        );
    }

    /**
     * Calculates gross salary using total hours worked and hourly rate.
     *
     * @param totalHoursWorked the total payable hours worked
     * @param baseHourlyRate the employee's hourly pay rate
     * @return the calculated gross salary
     */
    public static double calculateGross(
            double totalHoursWorked,
            double baseHourlyRate
    ) {

        // Multiply total hours worked by the employee's hourly rate.
        return totalHoursWorked * baseHourlyRate;
    }

    /**
     * Calculates net salary after payroll deductions.
     *
     * @param specificCutoffGross the gross salary for the selected cut-off
     * @param totalDeductions the total payroll deductions
     * @return the salary remaining after deductions
     */
    public static double calculateNet(
            double specificCutoffGross,
            double totalDeductions
    ) {

        // Subtract all deductions from the cut-off gross salary.
        return specificCutoffGross - totalDeductions;
    }

    /**
     * Converts a numeric month value into its corresponding month name.
     *
     * @param m the numeric month value from 1 to 12
     * @return the corresponding month name, or "Invalid Month"
     *         when the value is outside the valid range
     */
    public static String month(int m) {

        // Return the month name matching the provided numeric value.
        switch (m) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return "Invalid Month";
        }
    }
}
