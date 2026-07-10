package com.motorph.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Provides a reusable dialog utility for displaying application
 * error messages using a customized JOptionPane.
 *
 * This class centralizes the creation of system dialogs to ensure
 * consistent appearance throughout the MotorPH application.
 */
public class DialogCustomizer {

    /**
     * Displays a non-resizable error dialog containing the specified message.
     *
     * The dialog is centered on the screen and uses the title
     * "System Message".
     *
     * @param message the message to display in the dialog
     */
    public static void show(String message) {

        // Create an error message dialog using JOptionPane.
        JOptionPane optionPane =
                new JOptionPane(message, JOptionPane.ERROR_MESSAGE);

        // Create the dialog window with the specified title.
        JDialog dialog =
                optionPane.createDialog(null, "System Message");

        // Prevent the dialog window from being resized.
        dialog.setResizable(false);

        // Display the dialog at the center of the screen.
        dialog.setLocationRelativeTo(null);

        // Show the dialog to the user.
        dialog.setVisible(true);
    }
}
