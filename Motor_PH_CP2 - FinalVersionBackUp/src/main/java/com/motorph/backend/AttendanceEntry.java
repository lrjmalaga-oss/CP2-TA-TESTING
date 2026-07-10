package com.motorph.backend;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Handles the validation and recording of employee attendance entries.
 *
 * This class checks employee numbers, validates attendance dates and times,
 * prevents duplicate attendance records, and saves valid attendance data
 * to the attendance CSV file.
 */
public class AttendanceEntry {

    // Location of the CSV file containing attendance records.
    private static final String ATTENDANCE_FILE = "src/main/resources/attendance.csv";

    /**
     * Validates the attendance information entered by the user.
     *
     * The method verifies that the employee number exists, checks the date
     * and time formats, confirms that the time-out is later than the time-in,
     * and prevents duplicate attendance entries for the same employee and date.
     *
     * @param employeeNo the employee number entered by the user
     * @param date the attendance date in MM/DD/YYYY format
     * @param timeIn the employee's time-in in HH:MM format
     * @param timeOut the employee's time-out in HH:MM format
     * @return "valid" if all attendance information is acceptable;
     *         otherwise, an error message
     * @throws IOException if an error occurs while reading employee or
     *                     attendance records
     */
    public static String validateAttendance(
            String employeeNo,
            String date,
            String timeIn,
            String timeOut
    ) throws IOException {

        // Ensure that an employee number has been entered.
        if (employeeNo == null || employeeNo.trim().isEmpty()) {
            return "Please enter Employee Number.";
        }

        // Employee number must contain exactly five digits.
        if (!employeeNo.matches("\\d{5}")) {
            return "Employee Number must be exactly 5 digits.";
        }

        // Confirm that the employee number exists in the employee records.
        if (EditEmployee.searchEmployee(employeeNo) == null) {
            return "Employee Number Not Found.";
        }

        try {
            // Validate the attendance date using the required format.
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate.parse(date, dateFormatter);
        } catch (DateTimeParseException ex) {
            return "Invalid date. Use MM/DD/YYYY.";
        }

        try {
            // Validate and convert the time-in and time-out values.
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
            LocalTime in = LocalTime.parse(timeIn, timeFormatter);
            LocalTime out = LocalTime.parse(timeOut, timeFormatter);

            // Ensure that the employee's time-out is later than the time-in.
            if (!out.isAfter(in)) {
                return "Time Out must be later than Time In.";
            }

        } catch (DateTimeParseException ex) {
            return "Invalid time. Use HH:MM format.";
        }

        // Prevent duplicate attendance records for the same employee and date.
        if (attendanceExists(employeeNo, date)) {
            return "Attendance already exists for this employee and date.";
        }

        return "valid";
    }

    /**
     * Adds a valid attendance entry to the attendance CSV file.
     *
     * The method retrieves the employee's name using the employee number,
     * prepares the attendance record, and appends it to the CSV file.
     *
     * @param employeeNo the employee number
     * @param date the attendance date
     * @param timeIn the employee's time-in
     * @param timeOut the employee's time-out
     * @throws IOException if an error occurs while reading employee records
     *                     or writing to the attendance file
     */
    public static void addAttendance(
            String employeeNo,
            String date,
            String timeIn,
            String timeOut
    ) throws IOException {

        // Retrieve the complete employee record using the employee number.
        String[] employee = EditEmployee.searchEmployee(employeeNo);

        // Extract the employee's last name and first name.
        String lastName = employee[1];
        String firstName = employee[2];

        // Prepare the attendance data in the required CSV column order.
        String[] row = {
                employeeNo,
                lastName,
                firstName,
                date,
                timeIn,
                timeOut
        };

        File file = new File(ATTENDANCE_FILE);

        // Open the attendance file in append mode.
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {

            // Add a line break if the current file does not end with one.
            if (file.length() > 0 && needsNewLine(file)) {
                bw.newLine();
            }

            // Convert the attendance data into CSV format and save it.
            bw.write(GetList.convertToCsvLine(row));
            bw.newLine();
        }
    }

    /**
     * Checks whether an attendance record already exists for the same
     * employee number and date.
     *
     * @param employeeNo the employee number to search for
     * @param date the attendance date to search for
     * @return true if a matching attendance record exists; otherwise, false
     * @throws IOException if the attendance CSV file cannot be read
     */
    private static boolean attendanceExists(String employeeNo, String date)
            throws IOException {

        try (BufferedReader br = new BufferedReader(new FileReader(ATTENDANCE_FILE))) {

            // Skip the CSV header row.
            br.readLine();
            String line;

            // Search each attendance record in the file.
            while ((line = br.readLine()) != null) {

                // Ignore blank rows.
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Parse the current attendance record.
                String[] values = GetList.parseCsvLine(line);

                // Compare the employee number and attendance date.
                if (values.length >= 4
                        && values[0].trim().equals(employeeNo.trim())
                        && values[3].trim().equals(date.trim())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks whether the attendance CSV file requires a new line before
     * another record is appended.
     *
     * @param file the attendance CSV file to inspect
     * @return true if the file does not end with a line break;
     *         otherwise, false
     * @throws IOException if the file cannot be accessed
     */
    private static boolean needsNewLine(File file) throws IOException {

        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {

            // An empty file does not require an additional line break.
            if (raf.length() == 0) {
                return false;
            }

            // Move to the last byte of the file.
            raf.seek(raf.length() - 1);
            int lastByte = raf.read();

            // Check whether the file already ends with a line break.
            return lastByte != '\n' && lastByte != '\r';
        }
    }
}
