package com.motorph.backend;

public class Deductions {

    public static double computeSSS(double baseGrossSalary) {

        if (baseGrossSalary < 3250) {
            return 135.0;
        }

        if (baseGrossSalary >= 24750) {
            return 1125.0;
        }

        int targetBracketIndex = (int) ((baseGrossSalary - 3250) / 500);

        return 157.5 + (targetBracketIndex * 22.5);
    }

    public static double computePhilhealth(double baseGrossSalary) {

        if (baseGrossSalary >= 10000 && baseGrossSalary < 60000) {
            return (baseGrossSalary * 0.03) / 2.0;
        }

        if (baseGrossSalary >= 60000) {
            return baseGrossSalary * 0.03;
        }

        return 0.0;
    }

    public static double computePagibig(double baseGrossSalary) {

        double evaluatedContribution = 0.0;

        if (baseGrossSalary >= 1000 && baseGrossSalary <= 1500) {
            evaluatedContribution = baseGrossSalary * 0.01;
        } else if (baseGrossSalary > 1500) {
            evaluatedContribution = baseGrossSalary * 0.02;
        }

        return Math.min(evaluatedContribution, 100.0);
    }

    public static double computeMonthlyTax(double netTaxableIncome) {

        if (netTaxableIncome < 20833) {
            return 0.0;
        }

        if (netTaxableIncome < 33333) {
            return (netTaxableIncome - 20833) * 0.20;
        }

        if (netTaxableIncome < 66667) {
            return 2500.0 + ((netTaxableIncome - 33333) * 0.20);
        }

        if (netTaxableIncome < 166667) {
            return 10833.0 + ((netTaxableIncome - 66667) * 0.30);
        }

        if (netTaxableIncome < 666667) {
            return 40833.33 + ((netTaxableIncome - 166667) * 0.32);
        }

        return 200833.33 + ((netTaxableIncome - 666667) * 0.35);
    }

    public static double computeTotalDeductions(
            double sss,
            double philHealth,
            double pagIbig,
            double tax
    ) {
        return sss + philHealth + pagIbig + tax;
    }
}