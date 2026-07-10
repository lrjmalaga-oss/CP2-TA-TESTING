package com.motorph;

import com.motorph.gui.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Main class of the MotorPH Employee Application.
 *
 * This class serves as the entry point of the application.
 * It initializes and launches the main graphical user interface (GUI)
 * by creating an instance of the MainFrame class.
 *
 * @author Group 1
 * @version Milestone 2
 */
public class Main {

    /**
     * Application entry point.
     *
     * Creates and displays the application's main window.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {

        // Launch the main application window.
        new MainFrame();
    }
}
