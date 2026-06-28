package com.motorph.backend;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ProcessPayroll {

    public static Employee employeeDetails(String id_Number) throws IOException {

        String empFile = "src/main/resources/MotorPh.csv";
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(empFile))) {

            br.readLine();

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = GetList.parseCsvLine(line);

                if (values.length < 4) {
                    continue;
                }

                if (id_Number.equals(values[0])) {
                    return new Employee(values[0], values[1], values[2], values[3]);
                }
            }
        }

        return null;
    }

        public static Admin admin(String id_Number, int targetMonth) throws IOException {
         return admin(id_Number, targetMonth, 2024);
     }

        public static Admin admin(String id_Number, int targetMonth, int targetYear) throws IOException {

            String attendance = "src/main/resources/attendance.csv";
            String line;

            double firstCutoffHours = 0.0;
            double secondCutoffHours = 0.0;

            boolean employeeFound = false;

            try (BufferedReader br = new BufferedReader(new FileReader(attendance))) {

                br.readLine();

                while ((line = br.readLine()) != null) {

                    if (line.trim().isEmpty()) {
                        continue;
                    }

                    String[] values = GetList.parseCsvLine(line);

                    if (values.length < 6) {
                        continue;
                    }

                    if (id_Number.equals(values[0])) {

                        String[] dateParts = values[3].split("/");

                        int month = Integer.parseInt(dateParts[0]);
                        int day = Integer.parseInt(dateParts[1]);
                        int year = Integer.parseInt(dateParts[2]);

                        if (month == targetMonth && year == targetYear) {

                            employeeFound = true;

                            double dailyHours = CalculationForHours.totalHours(
                                    values[4],
                                    values[5]
                            );

                            if (day >= 1 && day <= 15) {
                                firstCutoffHours += dailyHours;
                            } else if (day >= 16 && day <= 31) {
                                secondCutoffHours += dailyHours;
                            }
                        }
                    }
                }
            }

            if (!employeeFound) {
                return null;
            }

            double hourlyRate = Rate.getRate(id_Number);

            String firstCutoff = month(targetMonth) + " 1 - 15";
            String secondCutoff = month(targetMonth) + " 16 - 31";

            double firstGrossSalary = calculateGross(firstCutoffHours, hourlyRate);
            double secondGrossSalary = calculateGross(secondCutoffHours, hourlyRate);

            double combinedGrossSalary = firstGrossSalary + secondGrossSalary;

            double sss = Deductions.computeSSS(combinedGrossSalary);
            double philHealth = Deductions.computePhilhealth(combinedGrossSalary);
            double pagIbig = Deductions.computePagibig(combinedGrossSalary);

            double taxableIncome = combinedGrossSalary - sss - philHealth - pagIbig;
            double tax = Deductions.computeMonthlyTax(taxableIncome);

            double totalDeductions = Deductions.computeTotalDeductions(
                    sss,
                    philHealth,
                    pagIbig,
                    tax
            );

            double firstNetSalary = firstGrossSalary;
            double secondNetSalary = calculateNet(secondGrossSalary, totalDeductions);

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

            public static double calculateGross(double totalHoursWorked, double baseHourlyRate) {
                return totalHoursWorked * baseHourlyRate;
            }

            public static double calculateNet(double specificCutoffGross, double totalDeductions) {
                return specificCutoffGross - totalDeductions;
            }

            public static String month(int m) {

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