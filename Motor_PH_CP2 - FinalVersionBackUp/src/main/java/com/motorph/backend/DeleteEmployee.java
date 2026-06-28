package com.motorph.backend;

import java.io.*;
import java.util.ArrayList;

public class DeleteEmployee {

    private static final String EMP_FILE = "src/main/resources/MotorPh.csv";

    public static String validateDeleteInput(
            String employeeNo,
            String lastName,
            String firstName,
            String birthday
    ) {

        boolean hasEmployeeNo = employeeNo != null && !employeeNo.trim().isEmpty();
        boolean hasLastName = lastName != null && !lastName.trim().isEmpty();
        boolean hasFirstName = firstName != null && !firstName.trim().isEmpty();
        boolean hasBirthday = birthday != null
                && !birthday.trim().isEmpty()
                && !birthday.contains("_");

        if (!hasEmployeeNo && !hasLastName && !hasFirstName && !hasBirthday) {
            return "Please enter at least one search field.";
        }

        if (hasEmployeeNo && !employeeNo.matches("\\d{5}")) {
            return "Invalid Employee #. Must be exactly 5 digits.";
        }

        if (hasEmployeeNo && Integer.parseInt(employeeNo) < 10001) {
            return "Invalid Employee #. Must be 10001 or higher.";
        }

        if (hasBirthday && !birthday.matches("\\d{2}/\\d{2}/\\d{4}")) {
            return "Invalid Birthday. Format must be MM/DD/YYYY.";
        }

        return "valid";
    }

    public static String[] searchEmployee(
            String employeeNo,
            String lastName,
            String firstName,
            String birthday
    ) throws IOException {

        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {

            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = GetList.parseCsvLine(line);

                if (values.length >= 19) {

                    boolean match = true;

                    if (employeeNo != null && !employeeNo.trim().isEmpty()) {
                        match = match && values[0].trim().equals(employeeNo.trim());
                    }

                    if (lastName != null && !lastName.trim().isEmpty()) {
                        match = match && values[1].trim().equalsIgnoreCase(lastName.trim());
                    }

                    if (firstName != null && !firstName.trim().isEmpty()) {
                        match = match && values[2].trim().equalsIgnoreCase(firstName.trim());
                    }

                    if (birthday != null
                            && !birthday.trim().isEmpty()
                            && !birthday.contains("_")) {
                        match = match && values[3].trim().equals(birthday.trim());
                    }

                    if (match) {
                        return values;
                    }
                }
            }
        }

        return null;
    }

    public static boolean deleteEmployee(String employeeNo) throws IOException {

        ArrayList<String> lines = new ArrayList<>();
        boolean deleted = false;

        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {

            String header = br.readLine();
            lines.add(header);

            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = GetList.parseCsvLine(line);

                if (values.length > 0 && values[0].trim().equals(employeeNo.trim())) {
                    deleted = true;
                } else {
                    lines.add(line);
                }
            }
        }

        if (deleted) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(EMP_FILE))) {
                for (String line : lines) {
                    bw.write(line);
                    bw.newLine();
                }
            }
        }

        return deleted;
    }
    
        public static boolean deleteEmployeeAttendance(String employeeNo) throws IOException {

        String attendanceFile = "src/main/resources/attendance.csv";

        ArrayList<String> lines = new ArrayList<>();
        boolean deletedAttendance = false;

        try (BufferedReader br = new BufferedReader(new FileReader(attendanceFile))) {

            String header = br.readLine();

            if (header != null) {
                lines.add(header);
            }

            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = GetList.parseCsvLine(line);

                if (values.length > 0 && values[0].trim().equals(employeeNo.trim())) {
                    deletedAttendance = true;
                } else {
                    lines.add(line);
                }
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(attendanceFile))) {

            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        }

        return deletedAttendance;
    }
            public static ArrayList<String[]> searchMatchingEmployees(
            String empNo,
            String lastName,
            String firstName,
            String birthday
    ) throws IOException {

        ArrayList<String[]> matches = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {

            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = GetList.parseCsvLine(line);

                if (values.length < 19) {
                    continue;
                }

                boolean match = true;

                if (!empNo.trim().isEmpty()) {
                    match = values[0].trim().equalsIgnoreCase(empNo.trim());
                }

                if (!lastName.trim().isEmpty()) {
                    match = match && values[1].trim().equalsIgnoreCase(lastName.trim());
                }

                if (!firstName.trim().isEmpty()) {
                    match = match && values[2].trim().equalsIgnoreCase(firstName.trim());
                }

                if (!birthday.replace("_", "").replace("/", "").trim().isEmpty()) {
                    match = match && values[3].trim().equalsIgnoreCase(birthday.trim());
                }

                if (match) {
                    matches.add(values);
                }
            }
        }

        return matches;
    }
}