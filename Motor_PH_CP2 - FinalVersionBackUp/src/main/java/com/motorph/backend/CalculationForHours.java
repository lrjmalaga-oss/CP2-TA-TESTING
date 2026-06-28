package com.motorph.backend;

public class CalculationForHours {

    public static double totalHours(String rawIn, String rawOut) {

        double decimalTimeIn = convertTimeToDecimalHours(rawIn);
        double decimalTimeOut = convertTimeToDecimalHours(rawOut);

        if (decimalTimeIn < 8.0) {
            decimalTimeIn = 8.0;
        }

        if (decimalTimeOut > 17.0) {
            decimalTimeOut = 17.0;
        }

        decimalTimeIn = applyThirtyMinuteGraceRule(decimalTimeIn);

        double totalHoursWorked = decimalTimeOut - decimalTimeIn - 1.0;

        return Math.max(0.0, totalHoursWorked);
    }

    private static double convertTimeToDecimalHours(String rawTimeValue) {

        String[] timeComponents = rawTimeValue.split(":");

        int parsedHours = Integer.parseInt(timeComponents[0].trim());
        int parsedMinutes = Integer.parseInt(timeComponents[1].trim());

        return parsedHours + (parsedMinutes / 60.0);
    }

    private static double applyThirtyMinuteGraceRule(double calculatedTimeIn) {

        int baseHourComponent = (int) calculatedTimeIn;
        double remainingMinutesFraction = calculatedTimeIn - baseHourComponent;

        if (remainingMinutesFraction < 0.5) {
            return baseHourComponent;
        }

        return baseHourComponent + 0.5;
    }
}
