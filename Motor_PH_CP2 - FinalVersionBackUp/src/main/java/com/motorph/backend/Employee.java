package com.motorph.backend;

/**
 * Represents a basic employee record.
 *
 * This class stores the essential employee information used throughout
 * the MotorPH Employee Application, including the employee number,
 * first name, last name, and birthday.
 */
public class Employee {

    // Employee number.
    private String id;

    // Employee first name.
    private String Fname;

    // Employee last name.
    private String Lname;

    // Employee birthday.
    private String Bday;

    /**
     * Creates an Employee object using the provided employee information.
     *
     * @param id the employee number
     * @param Fname the employee's first name
     * @param Lname the employee's last name
     * @param Bday the employee's birthday
     */
    public Employee(String id, String Fname, String Lname, String Bday) {

        // Initialize the employee information.
        this.id = id;
        this.Fname = Fname;
        this.Lname = Lname;
        this.Bday = Bday;

    }

    /**
     * Returns the employee number.
     *
     * @return the employee number
     */
    public String getid() {
        return id;
    }

    /**
     * Returns the employee's first name.
     *
     * @return the first name
     */
    public String getFname() {
        return Fname;
    }

    /**
     * Returns the employee's last name.
     *
     * @return the last name
     */
    public String getLname() {
        return Lname;
    }

    /**
     * Returns the employee's birthday.
     *
     * @return the employee's birthday
     */
    public String getBday() {
        return Bday;
    }

}
