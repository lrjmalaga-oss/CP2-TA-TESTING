package com.motorph.backend;

import java.io.*;
import java.util.ArrayList;

public class EditEmployee {

    private static final String EMP_FILE = "src/main/resources/MotorPh.csv";

    public static String[] searchEmployee(String employeeNo) throws IOException {

        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {

            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = GetList.parseCsvLine(line);

                if (values.length >= 19 && values[0].trim().equals(employeeNo.trim())) {
                    return values;
                }
            }
        }

        return null;
    }

    public static String validateEditedFields(String[] data) {

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

        for (int i = 13; i <= 18; i++) {
            String number = data[i].replace(",", "");

            if (!number.matches("\\d+(\\.\\d+)?")) {
                return "Invalid Salary Field. Salary values must be numeric.";
            }
        }

        return "valid";
    }

    public static boolean updateEmployee(String[] updatedData) throws IOException {

        ArrayList<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(EMP_FILE))) {

            String header = br.readLine();
            lines.add(header);

            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = GetList.parseCsvLine(line);

                if (values.length >= 19 && values[0].trim().equals(updatedData[0].trim())) {
                    lines.add(GetList.convertToCsvLine(updatedData));
                    found = true;
                } else {
                    lines.add(line);
                }
            }
        }

        if (found) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(EMP_FILE))) {
                for (String line : lines) {
                    bw.write(line);
                    bw.newLine();
                }
            }
        }

        return found;
    }
}