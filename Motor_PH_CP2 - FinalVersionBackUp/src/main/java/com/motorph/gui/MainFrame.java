package com.motorph.gui;

import com.motorph.backend.*;

import javax.swing.*;
import java.awt.*; 

public class MainFrame {

   private JFrame frame;
   private JPanel containerPanel;
   private CardLayout cardLayout;    

 

   public MainFrame(){
 
     frame = new JFrame();
     cardLayout = new CardLayout();
     containerPanel = new JPanel(cardLayout);
           
    containerPanel.add(Login.createLoginPanel(cardLayout, containerPanel), "Login_Screen");
    containerPanel.add(EmployeeMenu.createEmployeeMenu(cardLayout, containerPanel), "Employee_Screen");
    containerPanel.add(AdminMenu.createAdminMenu(cardLayout, containerPanel), "Admin_Screen");
              
     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     frame.setSize(1200, 800);
     frame.setResizable(false);
     frame.add(containerPanel);
     frame.setLocationRelativeTo(null);
     frame.setVisible(true);
              
     }
                   
     	
}
