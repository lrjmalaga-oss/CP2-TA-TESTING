package com.motorph.backend;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AttendanceEntry {

    private static final String ATTENDANCE_FILE = "src/main/resources/attendance.csv";

    public static String validateAttendance(
            String employeeNo,
            String date,
            String timeIn,
            String timeOut
    ) throws IOException {

        if (employeeNo == null || employeeNo.trim().isEmpty()) {
            return "Please enter Employee Number.";
        }

        if (!employeeNo.matches("\\d{5}")) {
            return "Employee Number must be exactly 5 digits.";
        }

        if (EditEmployee.searchEmployee(employeeNo) == null) {
            return "Employee Number Not Found.";
        }

        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate.parse(date, dateFormatter);
        } catch (DateTimeParseException ex) {
            return "Invalid date. Use MM/DD/YYYY.";
        }

        try {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
            LocalTime in = LocalTime.parse(timeIn, timeFormatter);
            LocalTime out = LocalTime.parse(timeOut, timeFormatter);

            if (!out.isAfter(in)) {
                return "Time Out must be later than Time In.";
            }

        } catch (DateTimeParseException ex) {
            return "Invalid time. Use HH:MM format.";
        }

        if (attendanceExists(employeeNo, date)) {
            return "Attendance already exists for this employee and date.";
        }

        return "valid";
    }

    public static void addAttendance(
            String employeeNo,
            String date,
            String timeIn,
            String timeOut
    ) throws IOException {

        String[] employee = EditEmployee.searchEmployee(employeeNo);

        String lastName = employee[1];
        String firstName = employee[2];

        String[] row = {
                employeeNo,
                lastName,
                firstName,
                date,
                timeIn,
                timeOut
        };

        File file = new File(ATTENDANCE_FILE);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {

            if (file.length() > 0 && needsNewLine(file)) {
                bw.newLine();
            }

            bw.write(GetList.convertToCsvLine(row));
            bw.newLine();
        }
    }

    private static boolean attendanceExists(String employeeNo, String date)
            throws IOException {

        try (BufferedReader br = new BufferedReader(new FileReader(ATTENDANCE_FILE))) {

            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = GetList.parseCsvLine(line);

                if (values.length >= 4
                        && values[0].trim().equals(employeeNo.trim())
                        && values[3].trim().equals(date.trim())) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean needsNewLine(File file) throws IOException {

        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {

            if (raf.length() == 0) {
                return false;
            }

            raf.seek(raf.length() - 1);
            int lastByte = raf.read();

            return lastByte != '\n' && lastByte != '\r';
        }
    }
}