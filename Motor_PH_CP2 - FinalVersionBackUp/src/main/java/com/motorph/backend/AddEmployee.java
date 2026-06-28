package com.motorph.backend;

import java.io.*;
import java.util.ArrayList;

public class AddEmployee {

    private static final String EMP_FILE = "src/main/resources/MotorPh.csv";

    public static String generateNextEmployeeNumber() throws IOException {

        boolean[] usedNumbers = new boolean[100000];

        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {

            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = GetList.parseCsvLine(line);

                if (values.length > 0 && values[0].matches("\\d{5}")) {

                    int employeeNumber = Integer.parseInt(values[0]);

                    if (employeeNumber >= 10001 && employeeNumber <= 99999) {
                        usedNumbers[employeeNumber] = true;
                    }
                }
            }
        }

        for (int i = 10001; i <= 99999; i++) {
            if (!usedNumbers[i]) {
                return String.valueOf(i);
            }
        }

        return "99999";
    }

    public static boolean employeeExists(String employeeNo) throws IOException {

        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {

            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = GetList.parseCsvLine(line);

                if (values.length > 0 && values[0].trim().equals(employeeNo.trim())) {
                    return true;
                }
            }
        }

        return false;
    }

    public static String validateEmployeeData(String[] data) {

        if (hasEmptyField(data)) {
            return "Please complete all employee fields.";
        }

        if (!data[0].matches("\\d{5}")) {
            return "Invalid Employee #. Must be exactly 5 digits.";
        }

        if (Integer.parseInt(data[0]) < 10001) {
            return "Invalid Employee #. Must be 10001 or higher.";
        }

        if (!data[3].matches("\\d{2}/\\d{2}/\\d{4}")) {
            return "Invalid Birthday. Format must be MM/DD/YYYY.";
        }

        if (!data[5].matches("\\d{3}-\\d{3}-\\d{3}")) {
            return "Invalid Phone Number. Format must be 000-000-000.";
        }

        if (!data[6].matches("\\d{2}-\\d{7}-\\d")) {
            return "Invalid SSS Number. Format must be 00-0000000-0.";
        }

        if (!data[7].matches("\\d{12}")) {
            return "Invalid PhilHealth Number. Must be 12 digits.";
        }

        if (!data[8].matches("\\d{3}-\\d{3}-\\d{3}-\\d{3}")) {
            return "Invalid TIN Number. Format must be 000-000-000-000.";
        }

        if (!data[9].matches("\\d{12}")) {
            return "Invalid Pag-IBIG Number. Must be 12 digits.";
        }

     String errorMessage = "";

     String[] salaryLabels = {
             "Basic Salary",
             "Rice Subsidy",
             "Phone Allowance",
             "Clothing Allowance",
             "Gross Semi-monthly Rate",
             "Hourly Rate"
     };

     for (int i = 13; i <= 18; i++) {

         String value = data[i].replace(",", "").trim();

         if (!value.matches("\\d+(\\.\\d+)?")) {
             errorMessage += salaryLabels[i - 13] + " must be numeric.\n";
         }
     }

     if (!errorMessage.isEmpty()) {
         return errorMessage;
     }

        return "valid";
    }

    public static void addEmployee(String[] data) throws IOException {

        File file = new File(EMP_FILE);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {

            if (file.length() > 0 && needsNewLine(file)) {
                bw.newLine();
            }

            bw.write(GetList.convertToCsvLine(data));
            bw.newLine();
        }
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

    private static boolean hasEmptyField(String[] data) {

        for (int i = 0; i < data.length; i++) {
            if (data[i] == null || data[i].trim().isEmpty()) {
                return true;
            }
        }

        return false;
    }
}

