package com.motorph.backend;

public class Employee { 

 private String id;
 private String Fname;
 private String Lname;
 private String Bday;
 
 public Employee(String id, String Fname, String Lname, String Bday){
 
  this.id = id;
  this.Fname = Fname;
  this.Lname = Lname;
  this.Bday = Bday;
 	
 }	

   public String getid() { return id; } 
   public String getFname() { return Fname; }  
   public String getLname() {return Lname; }
   public String getBday() {return Bday; }
 
	
}
