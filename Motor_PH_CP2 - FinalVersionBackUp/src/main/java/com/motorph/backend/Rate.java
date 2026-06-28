package com.motorph.backend;

import java.io.BufferedReader;
import java.io.FileReader; 
import java.io.IOException;

public class Rate {

  public static double getRate (String id_Number) throws IOException {
  
  	String empFile = "src/main/resources/MotorPh.csv";
  	String line = "";
  	double rate = 0.0;

    try(BufferedReader br = new BufferedReader (new FileReader(empFile))) {
    br.readLine();

    while ((line = br.readLine()) !=null ) {
    	String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

        if (id_Number.equals(values[0])) {
            rate = Double.parseDouble(values[18].replace("\"", ""));
            return rate;	  	
        }    	
    }
    }
    return rate;
  
  }	
}
