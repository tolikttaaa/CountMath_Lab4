package back;

public interface OrdinaryDifferentialEquation {
    //y' = f(x, y)
    double getValue(double xValue, double yValue);
    ConstEquation getGeneralSolution();

    default Function getSpecificSolution(Point[] points) {
        return getGeneralSolution().getFunction(points);
    }
}
