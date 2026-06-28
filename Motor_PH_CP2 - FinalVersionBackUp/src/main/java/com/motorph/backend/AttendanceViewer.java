package com.motorph.backend;

import java.io.*;
import java.util.ArrayList;

public class AttendanceViewer {

    private static final String EMP_FILE =
            "src/main/resources/MotorPh.csv";

    private static final String ATTENDANCE_FILE =
            "src/main/resources/attendance.csv";

    public static String[][] getAttendanceTableData(
            String employeeNo,
            int targetMonth,
            int targetYear
    ) throws IOException {

        ArrayList<String[]> tableRows = new ArrayList<>();

        try (BufferedReader empReader =
                     new BufferedReader(new FileReader(EMP_FILE))) {

            empReader.readLine();
            String empLine;

            while ((empLine = empReader.readLine()) != null) {

                if (empLine.trim().isEmpty()) {
                    continue;
                }

                String[] employee = GetList.parseCsvLine(empLine);

                if (employee.length < 3) {
                    continue;
                }

                boolean employeeMatches =
                        employeeNo == null
                                || employeeNo.trim().isEmpty()
                                || employee[0].trim().equals(employeeNo.trim());

                if (!employeeMatches) {
                    continue;
                }

                boolean hasAttendance = false;

                try (BufferedReader attReader =
                             new BufferedReader(new FileReader(ATTENDANCE_FILE))) {

                    attReader.readLine();
                    String attLine;

                    while ((attLine = attReader.readLine()) != null) {

                        if (attLine.trim().isEmpty()) {
                            continue;
                        }

                        String[] attendance = GetList.parseCsvLine(attLine);

                        if (attendance.length < 6) {
                            continue;
                        }

                        if (!attendance[0].trim().equals(employee[0].trim())) {
                            continue;
                        }

                        String[] dateParts = attendance[3].split("/");

                        int month = Integer.parseInt(dateParts[0]);
                        int year = Integer.parseInt(dateParts[2]);

                        if (month == targetMonth && year == targetYear) {

                            hasAttendance = true;

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

        String[][] data = new String[tableRows.size()][7];

        for (int i = 0; i < tableRows.size(); i++) {
            data[i] = tableRows.get(i);
        }

        return data;
    }
}