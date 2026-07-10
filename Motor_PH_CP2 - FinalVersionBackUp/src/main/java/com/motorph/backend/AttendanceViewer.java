package com.motorph.backend;

import java.io.*;
import java.util.ArrayList;

/**
 * Retrieves employee attendance records for display in a table.
 *
 * This class reads employee and attendance information from CSV files,
 * filters attendance records by employee number, month, and year, and
 * prepares the results as a two-dimensional array for GUI table display.
 */
public class AttendanceViewer {

    // Location of the CSV file containing employee records.
    private static final String EMP_FILE =
            "src/main/resources/MotorPh.csv";

    // Location of the CSV file containing attendance records.
    private static final String ATTENDANCE_FILE =
            "src/main/resources/attendance.csv";

    /**
     * Retrieves attendance records for a selected employee, month, and year.
     *
     * When no employee number is provided, the method includes all employees.
     * Employees with matching attendance records are marked as "Present."
     * Employees without attendance records for the selected period are marked
     * as "No Attendance."
     *
     * @param employeeNo the employee number to filter, or an empty value
     *                   to include all employees
     * @param targetMonth the month to retrieve, represented as a number
     * @param targetYear the year to retrieve
     * @return a two-dimensional String array containing attendance table data
     * @throws IOException if an employee or attendance CSV file cannot be read
     */
    public static String[][] getAttendanceTableData(
            String employeeNo,
            int targetMonth,
            int targetYear
    ) throws IOException {

        // Stores the attendance records that will be displayed in the table.
        ArrayList<String[]> tableRows = new ArrayList<>();

        // Open and read the employee CSV file.
        try (BufferedReader empReader =
                     new BufferedReader(new FileReader(EMP_FILE))) {

            // Skip the employee CSV header row.
            empReader.readLine();
            String empLine;

            // Process each employee record.
            while ((empLine = empReader.readLine()) != null) {

                // Ignore blank rows.
                if (empLine.trim().isEmpty()) {
                    continue;
                }

                // Convert the employee CSV row into an array of values.
                String[] employee = GetList.parseCsvLine(empLine);

                // Skip incomplete employee records.
                if (employee.length < 3) {
                    continue;
                }

                /*
                 * Include the employee when no employee number is entered
                 * or when the employee number matches the selected record.
                 */
                boolean employeeMatches =
                        employeeNo == null
                                || employeeNo.trim().isEmpty()
                                || employee[0].trim().equals(employeeNo.trim());

                // Skip employees that do not match the filter.
                if (!employeeMatches) {
                    continue;
                }

                // Tracks whether the employee has attendance for the selected period.
                boolean hasAttendance = false;

                // Open and read the attendance CSV file.
                try (BufferedReader attReader =
                             new BufferedReader(new FileReader(ATTENDANCE_FILE))) {

                    // Skip the attendance CSV header row.
                    attReader.readLine();
                    String attLine;

                    // Process each attendance record.
                    while ((attLine = attReader.readLine()) != null) {

                        // Ignore blank rows.
                        if (attLine.trim().isEmpty()) {
                            continue;
                        }

                        // Convert the attendance CSV row into an array of values.
                        String[] attendance = GetList.parseCsvLine(attLine);

                        // Skip incomplete attendance records.
                        if (attendance.length < 6) {
                            continue;
                        }

                        // Skip attendance records belonging to another employee.
                        if (!attendance[0].trim().equals(employee[0].trim())) {
                            continue;
                        }

                        // Separate the month, day, and year from the attendance date.
                        String[] dateParts = attendance[3].split("/");

                        int month = Integer.parseInt(dateParts[0]);
                        int year = Integer.parseInt(dateParts[2]);

                        // Include only records that match the selected month and year.
                        if (month == targetMonth && year == targetYear) {

                            hasAttendance = true;

                            // Add the matching attendance record to the table data.
                            tableRows.add(new String[]{
                                    employee[0],
                                    employee[1],
                                    employee[2],
                                    attendance[3],
                                    attendance[4],
                                    attendance[5],
                                    "Present"
                            });
                        }
                    }
                }

                /*
                 * Add a placeholder row when the employee has no attendance
                 * record for the selected month and year.
                 */
                if (!hasAttendance) {
                    tableRows.add(new String[]{
                            employee[0],
                            employee[1],
                            employee[2],
                            "-",
                            "-",
                            "-",
                            "No Attendance"
                    });
                }
            }
        }

        // Convert the list of table rows into a two-dimensional array.
        String[][] data = new String[tableRows.size()][7];

        for (int i = 0; i < tableRows.size(); i++) {
            data[i] = tableRows.get(i);
        }

        return data;
    }
}
