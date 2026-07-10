package com.motorph.backend;

/**
 * Calculates the total number of hours worked by an employee.
 *
 * This class converts the employee's time-in and time-out into decimal
 * hours, applies company attendance rules, deducts the standard one-hour
 * lunch break, and returns the total payable working hours.
 */
public class CalculationForHours {

    /**
     * Calculates the employee's total working hours for the day.
     *
     * The calculation applies the following company policies:
     * - Time-in earlier than 8:00 AM is treated as 8:00 AM.
     * - Time-out later than 5:00 PM is treated as 5:00 PM.
     * - Time-in is rounded using the 30-minute grace rule.
     * - A one-hour lunch break is deducted from the total working time.
     *
     * @param rawIn the employee's recorded time-in
     * @param rawOut the employee's recorded time-out
     * @return the total payable working hours
     */
    public static double totalHours(String rawIn, String rawOut) {

        // Convert the recorded time-in and time-out into decimal hour values.
        double decimalTimeIn = convertTimeToDecimalHours(rawIn);
        double decimalTimeOut = convertTimeToDecimalHours(rawOut);

        // Employees are credited no earlier than 8:00 AM.
        if (decimalTimeIn < 8.0) {
            decimalTimeIn = 8.0;
        }

        // Employees are credited no later than 5:00 PM.
        if (decimalTimeOut > 17.0) {
            decimalTimeOut = 17.0;
        }

        // Apply the company's 30-minute grace rule to the time-in.
        decimalTimeIn = applyThirtyMinuteGraceRule(decimalTimeIn);

        // Deduct the one-hour lunch break from the total working hours.
        double totalHoursWorked = decimalTimeOut - decimalTimeIn - 1.0;

        // Prevent negative working hours from being returned.
        return Math.max(0.0, totalHoursWorked);
    }

    /**
     * Converts a time value from HH:MM format into decimal hours.
     *
     * Example:
     * 08:30 becomes 8.5
     * 13:15 becomes 13.25
     *
     * @param rawTimeValue the time in HH:MM format
     * @return the equivalent decimal hour value
     */
    private static double convertTimeToDecimalHours(String rawTimeValue) {

        // Separate the hour and minute components.
        String[] timeComponents = rawTimeValue.split(":");

        int parsedHours = Integer.parseInt(timeComponents[0].trim());
        int parsedMinutes = Integer.parseInt(timeComponents[1].trim());

        // Convert minutes into their decimal-hour equivalent.
        return parsedHours + (parsedMinutes / 60.0);
    }

    /**
     * Applies the company's 30-minute grace rule to the employee's time-in.
     *
     * Time-ins before 30 minutes past the hour are rounded down to the hour,
     * while time-ins at or after 30 minutes past the hour are rounded to the
     * half-hour.
     *
     * Examples:
     * 8.10 → 8.0
     * 8.29 → 8.0
     * 8.30 → 8.5
     * 8.45 → 8.5
     *
     * @param calculatedTimeIn the employee's decimal time-in
     * @return the adjusted decimal time-in after applying the grace rule
     */
    private static double applyThirtyMinuteGraceRule(double calculatedTimeIn) {

        // Extract the hour component.
        int baseHourComponent = (int) calculatedTimeIn;

        // Determine the fractional minute component.
        double remainingMinutesFraction = calculatedTimeIn - baseHourComponent;

        // Round down to the nearest hour if before 30 minutes.
        if (remainingMinutesFraction < 0.5) {
            return baseHourComponent;
        }

        // Otherwise, round to the nearest half-hour.
        return baseHourComponent + 0.5;
    }
}
