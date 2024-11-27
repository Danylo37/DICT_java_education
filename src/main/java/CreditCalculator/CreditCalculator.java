package CreditCalculator;

public class CreditCalculator {
    private final String PAYMENT_TYPE;
    private final Double PAYMENT;
    private final Double PRINCIPAL;
    private final Integer PERIODS;
    private final Double INTEREST;

    public CreditCalculator(String paymentType, Double payment, Double principal, Integer periods, Double interest) {
        this.PAYMENT_TYPE = paymentType;
        this.PAYMENT = payment;
        this.PRINCIPAL = principal;
        this.PERIODS = periods;
        this.INTEREST = interest;

        checkNegativeParams();
    }

    public void start() {
        if ("annuity".equals(PAYMENT_TYPE) && PAYMENT == null && PRINCIPAL != null && PERIODS != null) {
            calculateAnnuity();
        } else if ("annuity".equals(PAYMENT_TYPE) && PAYMENT != null && PRINCIPAL == null && PERIODS != null) {
            calculatePrincipal();
        } else if ("annuity".equals(PAYMENT_TYPE) && PAYMENT != null && PRINCIPAL != null && PERIODS == null) {
            calculatePeriods();
        } else if ("diff".equals(PAYMENT_TYPE) && PAYMENT == null && PRINCIPAL != null && PERIODS != null) {
            calculateDiff();
        } else {
            System.out.println("Incorrect parameters");
        }
    }

    private void checkNegativeParams() {
        if ((PAYMENT != null && PAYMENT < 0) ||
                (PRINCIPAL != null && PRINCIPAL < 0) ||
                (PERIODS != null && PERIODS < 0) ||
                (INTEREST == null || INTEREST < 0)) {
            System.out.println("Incorrect parameters");
            System.exit(0);
        }
    }

    private void calculateAnnuity() {
        double nominalInterest = getNominalInterest();
        double numerator = nominalInterest * Math.pow(1 + nominalInterest, PERIODS);
        double denominator = Math.pow(1 + nominalInterest, PERIODS) - 1;

        int annuity = (int) Math.ceil(PRINCIPAL * (numerator / denominator));

        System.out.println("Your annuity payment = " + annuity + "!");
    }

    private void calculateDiff() {
        double nominalInterest = getNominalInterest();
        int totalOverpayment = 0;

        for (int month = 1; month <= PERIODS; month++) {
            double diffPayment = (PRINCIPAL / PERIODS) + nominalInterest * (PRINCIPAL - (PRINCIPAL * (month - 1) / PERIODS));
            int payment = (int) Math.ceil(diffPayment);
            totalOverpayment += payment;
            System.out.println("Month " + month + ": payment is " + payment);
        }

        int overpayment = totalOverpayment - PRINCIPAL.intValue();
        System.out.println("Overpayment = " + overpayment);
    }

    private void calculatePrincipal() {
        double nominalInterest = getNominalInterest();
        double numerator = nominalInterest * Math.pow(1 + nominalInterest, PERIODS);
        double denominator = Math.pow(1 + nominalInterest, PERIODS) - 1;

        int principal = (int) Math.floor(PAYMENT / (numerator / denominator));
        System.out.println("Your loan principal = " + principal + "!");
    }

    private void calculatePeriods() {
        double nominalInterest = getNominalInterest();
        double base = 1 + nominalInterest;

        double calculations = PAYMENT / (PAYMENT - nominalInterest * PRINCIPAL);
        double periodsRaw = Math.log(calculations) / Math.log(base);

        int totalPeriods = (int) Math.ceil(periodsRaw);
        int years = totalPeriods / 12;
        int months = totalPeriods % 12;

        if (years == 0) {
            System.out.println("It will take " + months + " month" + (months == 1 ? "" : "s") + " to repay this loan!");
        } else if (months == 0) {
            System.out.println("It will take " + years + " year" + (years == 1 ? "" : "s") + " to repay this loan!");
        } else {
            System.out.println("It will take " + years + " year" + (years == 1 ? "" : "s") + " and " + months + " month" + (months == 1 ? "" : "s") + " to repay this loan!");
        }
    }

    private double getNominalInterest() {
        return INTEREST / (12 * 100);
    }

    private static Double getDoubleProperty(String property) {
        String value = System.getProperty(property);
        if (value != null && !value.isEmpty()) {
            return Double.valueOf(value);
        }
        return null;
    }

    private static Integer getIntegerProperty(String property) {
        String value = System.getProperty(property);
        if (value != null && !value.isEmpty()) {
            return Integer.valueOf(value);
        }
        return null;
    }

    public static void main(String[] args) {
        String type = System.getProperty("type");
        Double principal = getDoubleProperty("principal");
        Double payment = getDoubleProperty("payment");
        Integer periods = getIntegerProperty("periods");
        Double interest = getDoubleProperty("interest");

        CreditCalculator calculator = new CreditCalculator(type, payment, principal, periods, interest);
        calculator.start();
    }
}
