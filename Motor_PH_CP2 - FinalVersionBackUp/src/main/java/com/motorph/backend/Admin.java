package com.motorph.backend;

public class Admin {

    private String cutOff;
    private double totalHours;
	private double grossSalary;
    private double netSalary;
	
	private String cutOff2;
	private double totalHours2;
	private double grossSalary2;
	private double SSS;
	private double philHealth;
	private double pagIbig;
	private double tax;
	private double netSalary2;

	public Admin(String cutOff, double totalHours, double grossSalary, double netSalary, 
	             String cutOff2, double totalHours2, double grossSalary2, double SSS,
	             double philHealth, double pagIbig, double tax, double netSalary2 ) {

           this.cutOff = cutOff;
           this.totalHours = totalHours;
           this.grossSalary = grossSalary; 
           this.netSalary = netSalary;
           
           this.cutOff2 = cutOff2;  
           this.totalHours2 = totalHours2;
           this.grossSalary2 = grossSalary2;
           this.SSS = SSS;
           this.philHealth = philHealth;
           this.pagIbig = pagIbig;
           this.tax = tax;
           this.netSalary2 = netSalary2;           
	       }
	       
    public String getcutOff() { return cutOff; }
    public double gettotalHours() { return totalHours; }
    public double getgrossSalary() { return grossSalary; }
    public double getnetSalary() { return netSalary; }
    
    public String getcutOff2() { return cutOff2; }
    public double gettotalHours2() { return totalHours2; }
    public double getgrossSalary2() { return grossSalary2; }
    public double getSSS() { return SSS; }
    public double getphilHealth() { return philHealth; }
    public double getpagIbig() { return pagIbig; }
    public double gettax() { return tax; }
    public double getnetSalary2() { return netSalary2; }	
	
}
