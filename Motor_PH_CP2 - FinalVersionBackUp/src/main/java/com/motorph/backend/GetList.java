package com.motorph.backend;

import java.io.*;
import java.util.ArrayList;

public class GetList {

    private static final String EMP_FILE = "src/main/resources/MotorPh.csv";

    public static ArrayList<Employee> GetList() throws IOException {

        ArrayList<Employee> employeeList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {

            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = parseCsvLine(line);

                if (values.length >= 4) {
                    employeeList.add(new Employee(values[0], values[1], values[2], values[3]));
                }
            }
        }

        return employeeList;
    }

    public static String[][] getTableData(int monthValue) throws IOException {

        ArrayList<Employee> list = GetList();
        String[][] tableData = new String[list.size()][8];

        for (int i = 0; i < list.size(); i++) {

            Employee emp = list.get(i);
            Admin payroll = ProcessPayroll.admin(emp.getid(), monthValue);

            tableData[i][0] = emp.getid();
            tableData[i][1] = emp.getLname();
            tableData[i][2] = emp.getFname();
            tableData[i][3] = emp.getBday();

            if (payroll != null) {
                tableData[i][4] = String.valueOf(payroll.gettotalHours());
                tableData[i][5] = String.valueOf(payroll.getgrossSalary());
                tableData[i][6] = String.valueOf(payroll.getnetSalary());
                tableData[i][7] = String.valueOf(payroll.gettax());
            } else {
                tableData[i][4] = "0";
                tableData[i][5] = "0";
                tableData[i][6] = "0";
                tableData[i][7] = "0";
            }
        }

        return tableData;
    }

    public static String[] getUniqueCsvValues(int columnIndex) throws IOException {

        ArrayList<String> valuesList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {

            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = parseCsvLine(line);

                if (values.length > columnIndex) {

                    String value = values[columnIndex].trim();

                    if (!value.isEmpty() && !valuesList.contains(value)) {
                        valuesList.add(value);
                    }
                }
            }
        }

        return valuesList.toArray(new String[0]);
    }

    public static String[] getSupervisorNames() throws IOException {

        ArrayList<String> supervisorList = new ArrayList<>();
        supervisorList.add("N/A");

        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {

            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = parseCsvLine(line);

                if (values.length > 11) {

                    String position = values[11].trim();

                    if (!position.toLowerCase().contains("rank and file")) {

                        String fullName = values[2].trim() + " " + values[1].trim();

                        if (!supervisorList.contains(fullName)) {
                            supervisorList.add(fullName);
                        }
                    }
                }
            }
        }

        return supervisorList.toArray(new String[0]);
    }

    public static String[] parseCsvLine(String line) {

        ArrayList<String> values = new ArrayList<>();
        StringBuilder currentValue = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {

            char currentChar = line.charAt(i);

            if (currentChar == '"') {
                insideQuotes = !insideQuotes;
            } else if (currentChar == ',' && !insideQuotes) {
                values.add(currentValue.toString());
                currentValue.setLength(0);
            } else {
                currentValue.append(currentChar);
            }
        }

        values.add(currentValue.toString());

        return values.toArray(new String[0]);
    }

    public static String convertToCsvLine(String[] row) {

        StringBuilder line = new StringBuilder();

        for (int i = 0; i < row.length; i++) {

            String value = row[i];

            if (value.contains(",") || value.contains("\"")) {
                value = "\"" + value.replace("\"", "\"\"") + "\"";
            }

            line.append(value);

            if (i < row.length - 1) {
                line.append(",");
            }
        }

        return line.toString();
    }
    
    public static String[][] getBasicEmployeeTableData() throws IOException {

        ArrayList<String[]> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {

            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = parseCsvLine(line);

                if (values.length >= 12) {
                    list.add(values);
                }
            }
        }

        String[][] tableData = new String[list.size()][6];

        for (int i = 0; i < list.size(); i++) {

            String[] emp = list.get(i);

            tableData[i][0] = emp[0];
            tableData[i][1] = emp[1];
            tableData[i][2] = emp[2];
            tableData[i][3] = emp[3];
            tableData[i][4] = emp[10];
            tableData[i][5] = emp[11];
        }

        return tableData;
    }
        public static void sortCsvByEmployeeNumber() throws IOException {

        ArrayList<String[]> employeeRows = new ArrayList<>();
        String header;

        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {

            header = br.readLine();
            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = parseCsvLine(line);

                if (values.length > 0) {
                    employeeRows.add(values);
                }
            }
        }

        employeeRows.sort((a, b) ->
                Integer.compare(
                        Integer.parseInt(a[0].trim()),
                        Integer.parseInt(b[0].trim())
                )
        );

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(EMP_FILE))) {

            bw.write(header);
            bw.newLine();

            for (String[] row : employeeRows) {
                bw.write(convertToCsvLine(row));
                bw.newLine();
            }
        }
    }
    
        public static String[][] getComputedSalaryTableData(
                int monthValue,
                int yearValue
            ) throws IOException {

            ArrayList<String[]> list = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {

                br.readLine();
                String line;

                while ((line = br.readLine()) != null) {

                    if (line.trim().isEmpty()) {
                        continue;
                    }

                    String[] values = parseCsvLine(line);

                    if (values.length >= 26) {

                        String monthName = ProcessPayroll.month(monthValue);

                        if (values[19].equals(monthName)
                                && values[20].equals(String.valueOf(yearValue))) {
                            list.add(values);
                        }
                    }
                }
            }

            String[][] tableData = new String[list.size()][10];

            for (int i = 0; i < list.size(); i++) {

                String[] row = list.get(i);

                tableData[i][0] = row[0];
                tableData[i][1] = row[1];
                tableData[i][2] = row[2];
                tableData[i][3] = row[19];
                tableData[i][4] = row[20];
                tableData[i][5] = row[21];
                tableData[i][6] = row[22];
                tableData[i][7] = row[23];
                tableData[i][8] = row[24];
                tableData[i][9] = row[25];
            }

            return tableData;
        }
    
    
}