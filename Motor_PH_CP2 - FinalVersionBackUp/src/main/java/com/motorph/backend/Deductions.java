package com.motorph.backend;

/**
 * Performs the computation of mandatory government deductions
 * based on the MotorPH payroll requirements.
 *
 * This class calculates the following deductions:
 * - SSS contribution
 * - PhilHealth contribution
 * - Pag-IBIG contribution
 * - Monthly withholding tax
 * - Total deductions
 */
public class Deductions {

    /**
     * Computes the employee's SSS contribution based on the
     * monthly gross salary.
     *
     * The computation follows the salary bracket table used
     * by the MotorPH payroll system.
     *
     * @param baseGrossSalary the employee's monthly gross salary
     * @return the corresponding SSS contribution
     */
    public static double computeSSS(double baseGrossSalary) {

        // Minimum SSS contribution.
        if (baseGrossSalary < 3250) {
            return 135.0;
        }

        // Maximum SSS contribution.
        if (baseGrossSalary >= 24750) {
            return 1125.0;
        }

        // Determine the employee's salary bracket.
        int targetBracketIndex = (int) ((baseGrossSalary - 3250) / 500);

        // Calculate the contribution based on the salary bracket.
        return 157.5 + (targetBracketIndex * 22.5);
    }

    /**
     * Computes the employee's PhilHealth contribution.
     *
     * Employees earning between ₱10,000 and ₱59,999 contribute
     * 3% of their salary, shared equally by the employee and employer.
     * Employees earning ₱60,000 and above contribute the full 3%.
     *
     * @param baseGrossSalary the employee's monthly gross salary
     * @return the PhilHealth contribution
     */
    public static double computePhilhealth(double baseGrossSalary) {

        // Compute the employee's share of the PhilHealth contribution.
        if (baseGrossSalary >= 10000 && baseGrossSalary < 60000) {
            return (baseGrossSalary * 0.03) / 2.0;
        }

        // Compute the maximum PhilHealth contribution.
        if (baseGrossSalary >= 60000) {
            return baseGrossSalary * 0.03;
        }

        // Employees earning below the minimum threshold have no contribution.
        return 0.0;
    }

    /**
     * Computes the employee's Pag-IBIG contribution.
     *
     * Contribution rates:
     * - 1% for salaries between ₱1,000 and ₱1,500
     * - 2% for salaries above ₱1,500
     * - Maximum employee contribution is capped at ₱100
     *
     * @param baseGrossSalary the employee's monthly gross salary
     * @return the Pag-IBIG contribution
     */
    public static double computePagibig(double baseGrossSalary) {

        // Stores the calculated Pag-IBIG contribution.
        double evaluatedContribution = 0.0;

        // Apply the appropriate contribution rate.
        if (baseGrossSalary >= 1000 && baseGrossSalary <= 1500) {
            evaluatedContribution = baseGrossSalary * 0.01;
        } else if (baseGrossSalary > 1500) {
            evaluatedContribution = baseGrossSalary * 0.02;
        }

        // Ensure the contribution does not exceed the maximum limit.
        return Math.min(evaluatedContribution, 100.0);
    }

    /**
     * Computes the employee's monthly withholding tax based on
     * taxable income.
     *
     * The computation follows the Philippine TRAIN Law tax brackets.
     *
     * @param netTaxableIncome the employee's taxable monthly income
     * @return the monthly withholding tax
     */
    public static double computeMonthlyTax(double netTaxableIncome) {

        // Tax-exempt income.
        if (netTaxableIncome < 20833) {
            return 0.0;
        }

        // Tax bracket: ₱20,833 to ₱33,332.
        if (netTaxableIncome < 33333) {
            return (netTaxableIncome - 20833) * 0.20;
        }

        // Tax bracket: ₱33,333 to ₱66,666.
        if (netTaxableIncome < 66667) {
            return 2500.0 + ((netTaxableIncome - 33333) * 0.20);
        }

        // Tax bracket: ₱66,667 to ₱166,666.
        if (netTaxableIncome < 166667) {
            return 10833.0 + ((netTaxableIncome - 66667) * 0.30);
        }

        // Tax bracket: ₱166,667 to ₱666,666.
        if (netTaxableIncome < 666667) {
            return 40833.33 + ((netTaxableIncome - 166667) * 0.32);
        }

        // Tax bracket: Above ₱666,666.
        return 200833.33 + ((netTaxableIncome - 666667) * 0.35);
    }

    /**
     * Computes the employee's total payroll deductions.
     *
     * The total deductions consist of:
     * - SSS
     * - PhilHealth
     * - Pag-IBIG
     * - Monthly withholding tax
     *
     * @param sss the SSS contribution
     * @param philHealth the PhilHealth contribution
     * @param pagIbig the Pag-IBIG contribution
     * @param tax the monthly withholding tax
     * @return the total payroll deductions
     */
    public static double computeTotalDeductions(
            double sss,
            double philHealth,
            double pagIbig,
            double tax
    ) {

        // Return the combined value of all payroll deductions.
        return sss + philHealth + pagIbig + tax;
    }
}
