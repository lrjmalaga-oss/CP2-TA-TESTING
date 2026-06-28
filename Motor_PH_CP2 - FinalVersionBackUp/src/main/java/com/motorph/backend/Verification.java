package com.motorph.backend;

import java.io.IOException;

public class Verification {

	 public static String verify(String name, String pass){

	if(name == null || name.trim().isEmpty() || pass == null || pass.trim().isEmpty()) {
		return "failed";
	} 

  	if("admin".equalsIgnoreCase(name) && "12345".equalsIgnoreCase(pass)){
  	   return "admin";
  	}

  	else if ("employee".equalsIgnoreCase(name) && "12345".equalsIgnoreCase(pass)){
       return "employee";	     
  	}
  	else { 
  	      return "incorrect";
  	}
  	
  	
  	}
  }
 
