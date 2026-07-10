package com.motorph.backend;

/**
 * Represents the payroll summary for an employee.
 *
 * This class stores payroll information for both the first and second
 * payroll cut-offs, including total hours worked, gross salary,
 * deductions, and net salary.
 */
public class Admin {

    // First payroll cut-off information.
    private String cutOff;
    private double totalHours;
    private double grossSalary;
    private double netSalary;

    // Second payroll cut-off information.
    private String cutOff2;
    private double totalHours2;
    private double grossSalary2;

    // Government deductions applied during the second cut-off.
    private double SSS;
    private double philHealth;
    private double pagIbig;
    private double tax;

    // Final net salary after all deductions.
    private double netSalary2;

    /**
     * Constructs an Admin payroll object containing payroll details
     * for both payroll cut-offs.
     *
     * @param cutOff First payroll cut-off period
     * @param totalHours Total hours worked during the first cut-off
     * @param grossSalary Gross salary for the first cut-off
     * @param netSalary Net salary for the first cut-off
     * @param cutOff2 Second payroll cut-off period
     * @param totalHours2 Total hours worked during the second cut-off
     * @param grossSalary2 Gross salary for the second cut-off
     * @param SSS SSS deduction
     * @param philHealth PhilHealth deduction
     * @param pagIbig Pag-IBIG deduction
     * @param tax Withholding tax deduction
     * @param netSalary2 Net salary after deductions for the second cut-off
     */
    public Admin(String cutOff, double totalHours, double grossSalary, double netSalary,
                 String cutOff2, double totalHours2, double grossSalary2, double SSS,
                 double philHealth, double pagIbig, double tax, double netSalary2) {

        // Initialize first cut-off payroll information.
        this.cutOff = cutOff;
        this.totalHours = totalHours;
        this.grossSalary = grossSalary;
        this.netSalary = netSalary;

        // Initialize second cut-off payroll information.
        this.cutOff2 = cutOff2;
        this.totalHours2 = totalHours2;
        this.grossSalary2 = grossSalary2;

        // Initialize government deductions.
        this.SSS = SSS;
        this.philHealth = philHealth;
        this.pagIbig = pagIbig;
        this.tax = tax;

        // Store the final net salary after deductions.
        this.netSalary2 = netSalary2;
    }

    /**
     * Returns the first payroll cut-off period.
     *
     * @return first payroll cut-off
     */
    public String getcutOff() { return cutOff; }

    /**
     * Returns the total hours worked during the first cut-off.
     *
     * @return total hours worked
     */
    public double gettotalHours() { return totalHours; }

    /**
     * Returns the gross salary for the first cut-off.
     *
     * @return gross salary
     */
    public double getgrossSalary() { return grossSalary; }

    /**
     * Returns the net salary for the first cut-off.
     *
     * @return net salary
     */
    public double getnetSalary() { return netSalary; }

    /**
     * Returns the second payroll cut-off period.
     *
     * @return second payroll cut-off
     */
    public String getcutOff2() { return cutOff2; }

    /**
     * Returns the total hours worked during the second cut-off.
     *
     * @return total hours worked
     */
    public double gettotalHours2() { return totalHours2; }

    /**
     * Returns the gross salary for the second cut-off.
     *
     * @return gross salary
     */
    public double getgrossSalary2() { return grossSalary2; }

    /**
     * Returns the SSS deduction.
     *
     * @return SSS deduction
     */
    public double getSSS() { return SSS; }

    /**
     * Returns the PhilHealth deduction.
     *
     * @return PhilHealth deduction
     */
    public double getphilHealth() { return philHealth; }

    /**
     * Returns the Pag-IBIG deduction.
     *
     * @return Pag-IBIG deduction
     */
    public double getpagIbig() { return pagIbig; }

    /**
     * Returns the withholding tax deduction.
     *
     * @return tax deduction
     */
    public double gettax() { return tax; }

    /**
     * Returns the final net salary for the second cut-off.
     *
     * @return net salary after deductions
     */
    public double getnetSalary2() { return netSalary2; }

}
