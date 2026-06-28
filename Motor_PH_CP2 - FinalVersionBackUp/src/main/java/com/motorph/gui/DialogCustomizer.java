package com.motorph.gui;

import javax.swing.*;
import java.awt.*;

public class DialogCustomizer {

 public static void show(String message)  {
 	JOptionPane optionPane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE);

 	JDialog dialog = optionPane.createDialog(null, "System Messge");

    dialog.setResizable(false);
    dialog.setLocationRelativeTo(null);
    dialog.setVisible(true);
    
 }
}
