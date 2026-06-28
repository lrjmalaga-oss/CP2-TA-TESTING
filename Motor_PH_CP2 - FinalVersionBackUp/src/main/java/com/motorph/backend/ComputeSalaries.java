package com.motorph.backend;

import java.io.*;
import java.util.ArrayList;

public class ComputeSalaries {

    private static final String EMP_FILE = "src/main/resources/MotorPh.csv";

    public static String computeAllEmployeeSalaries(
            int monthValue,
            int yearValue
    ) throws IOException {

        ArrayList<String[]> employeeRows = new ArrayList<>();
        String header;

        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {

            header = br.readLine();
            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = GetList.parseCsvLine(line);

                if (values.length >= 19) {
                    employeeRows.add(values);
                }
            }
        }

        String validationMessage = validateEmployeeRows(employeeRows);

        if (!validationMessage.equals("valid")) {
            return validationMessage;
        }

        header = updateHeader(header);

        ArrayList<String[]> updatedRows = new ArrayList<>();

        for (String[] employee : employeeRows) {

            String employeeNo = employee[0];

            Admin payroll = ProcessPayroll.admin(
                    employeeNo,
                    monthValue,
                    yearValue
            );

            String[] updatedEmployee = resizeRow(employee, 26);

            if (payroll != null) {

                double totalHours =
                        payroll.gettotalHours() +
                        payroll.gettotalHours2();

                double totalGross =
                        payroll.getgrossSalary() +
                        payroll.getgrossSalary2();

                double totalDeductions =
                        payroll.getSSS() +
                        payroll.getphilHealth() +
                        payroll.getpagIbig() +
                        payroll.gettax();

                double netSalary =
                        payroll.getnetSalary() +
                        payroll.getgrossSalary();

                updatedEmployee[19] = ProcessPayroll.month(monthValue);
                updatedEmployee[20] = String.valueOf(yearValue);
                updatedEmployee[21] = String.format("%.2f", totalHours);
                updatedEmployee[22] = String.format("%.2f", totalGross);
                updatedEmployee[23] = String.format("%.2f", totalDeductions);
                updatedEmployee[24] = String.format("%.2f", netSalary);
                updatedEmployee[25] = "Computed";

            } else {

                updatedEmployee[19] = ProcessPayroll.month(monthValue);
                updatedEmployee[20] = String.valueOf(yearValue);
                updatedEmployee[21] = "0";
                updatedEmployee[22] = "0";
                updatedEmployee[23] = "0";
                updatedEmployee[24] = "0";
                updatedEmployee[25] = "No Attendance";
            }

            updatedRows.add(updatedEmployee);
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(EMP_FILE))) {

            bw.write(header);
            bw.newLine();

            for (String[] row : updatedRows) {
                bw.write(GetList.convertToCsvLine(row));
                bw.newLine();
            }
        }

        GetList.sortCsvByEmployeeNumber();

        return "success";
    }

    public static String computeAllEmployeeSalaries(int monthValue)
            throws IOException {

        return computeAllEmployeeSalaries(monthValue, 2024);
    }

    private static String validateEmployeeRows(ArrayList<String[]> rows) {

        String errorMessage = "";

        String[] salaryLabels = {
                "Basic Salary",
                "Rice Subsidy",
                "Phone Allowance",
                "Clothing Allowance",
                "Gross Semi-monthly Rate",
                "Hourly Rate"
        };

        for (String[] row : rows) {

            String employeeNo = row[0];

            for (int i = 13; i <= 18; i++) {

                String value = row[i]
                        .replace(",", "")
                        .replace("\"", "")
                        .trim();

                if (!value.matches("\\d+(\\.\\d+)?")) {
                    errorMessage += "Employee #" + employeeNo + " - "
                            + salaryLabels[i - 13]
                            + " must be numeric.\n";
                }
            }
        }

        if (!errorMessage.isEmpty()) {
            return errorMessage;
        }

        return "valid";
    }

    private static String updateHeader(String header) {

        String[] headers = GetList.parseCsvLine(header);

        if (headers.length >= 26) {
            return header;
        }

        return header +
                ",Computed Month" +
                ",Computed Year" +
                ",Computed Total Hours" +
                ",Computed Gross Salary" +
                ",Computed Total Deductions" +
                ",Computed Net Salary" +
                ",Computation Status";
    }

    private static String[] resizeRow(String[] oldRow, int newSize) {

        String[] newRow = new String[newSize];

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