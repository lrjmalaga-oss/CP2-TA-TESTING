package com.motorph.backend;

/**
 * Verifies user login credentials.
 *
 * This class authenticates users based on the username and password
 * entered during login and determines the appropriate user role.
 */
public class Verification {

    /**
     * Verifies the entered username and password.
     *
     * The method checks for empty credentials and validates whether
     * the user is an administrator or employee based on the predefined
     * login credentials.
     *
     * @param name the username entered by the user
     * @param pass the password entered by the user
     * @return "admin" if administrator credentials are valid,
     *         "employee" if employee credentials are valid,
     *         "failed" if either field is empty,
     *         or "incorrect" if the credentials do not match
     */
    public static String verify(String name, String pass) {

        // Ensure that both username and password have been entered.
        if (name == null || name.trim().isEmpty()
                || pass == null || pass.trim().isEmpty()) {
            return "failed";
        }

        // Verify administrator credentials.
        if ("admin".equalsIgnoreCase(name)
                && "12345".equalsIgnoreCase(pass)) {
            return "admin";
        }

        // Verify employee credentials.
        else if ("employee".equalsIgnoreCase(name)
                && "12345".equalsIgnoreCase(pass)) {
            return "employee";
        }

        // Return an error when the credentials do not match.
        else {
            return "incorrect";
        }
    }
}
 

