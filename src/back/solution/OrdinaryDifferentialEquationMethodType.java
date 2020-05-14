package back.solution;

public enum OrdinaryDifferentialEquationMethodType {
    EULER_METHOD(
            "Euler's method",
            1
    ),
    IMPROVED_EULER_METHOD(
            "Improved Euler's method",
            2
    ),
    RUNGE_KUTTA_METHOD(
            "Runge-Kutta method",
            4
    ),
    ADAMS_METHOD(
            "Adams' method",
            5
    ),
    MILNE_METHOD(
            "Milne's method",
            5
    );

    private final String methodName;
    private final int accuracyOrder;

    OrdinaryDifferentialEquationMethodType(String methodName, int accuracyOrder) {
        this.methodName = methodName;
        this.accuracyOrder = accuracyOrder;
    }

    public int getPointsCount(double length, double accuracy) {
        int res = (int) (length / (Math.pow(accuracy, 1.0d / accuracyOrder)) + (1.0d - 1e-9d));

        return Math.max(10, res);
    }

    @Override
    public String toString() {
        return methodName;
    }
}
