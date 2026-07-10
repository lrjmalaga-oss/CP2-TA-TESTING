package com.motorph.backend;

/**
 * Represents a calendar month used throughout the MotorPH application.
 *
 * This class stores both the display name and the corresponding numeric
 * value of a month. It is primarily used to populate month selection
 * components such as combo boxes.
 */
public class Month {

    // Display name of the month (e.g., January).
    private String name;

    // Numeric representation of the month (e.g., 1 for January).
    private int value;

    /**
     * Creates a Month object using the specified name and numeric value.
     *
     * @param name the display name of the month
     * @param value the numeric value of the month
     */
    public Month(String name, int value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Returns the numeric value of the month.
     *
     * @return the month number
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the display name of the month.
     *
     * This method is automatically used by Swing components such as
     * JComboBox to display the month name instead of the object reference.
     *
     * @return the display name of the month
     */
    @Override
    public String toString() {
        return name;
    }
}
