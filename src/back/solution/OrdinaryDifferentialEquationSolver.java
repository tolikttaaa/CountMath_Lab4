package back.solution;

import back.OrdinaryDifferentialEquation;
import back.Point;
import back.exception.InvalidValueException;
import back.exception.NotImplementedSolutionException;

import java.util.ArrayList;

public class OrdinaryDifferentialEquationSolver {
    static public OrdinaryDifferentialEquationSolverResult solveODE(
            OrdinaryDifferentialEquationMethodType methodType,
            OrdinaryDifferentialEquation equation,
            Point point0,
            double xn,
            double accuracy
    ) throws NotImplementedSolutionException, InvalidValueException {
        int count = methodType.getPointsCount(xn - point0.first, accuracy);
        double h = (xn - point0.first) / (count - 1);

        ArrayList<Point> points;
        switch (methodType) {
            case EULER_METHOD:
                points = eulerMethodSolution(equation, point0, h, count);
                break;
            case IMPROVED_EULER_METHOD:
                points = improvedEulerMethodSolution(equation, point0, h, count);
                break;
            case RUNGE_KUTTA_METHOD:
                points = rungeKuttaMethodSolution(equation, point0, h, count);
                break;
            case ADAMS_METHOD:
                points = adamsMethodSolution(equation, point0, h, count, accuracy);
                break;
            case MILNE_METHOD:
            default:
                throw new NotImplementedSolutionException();
        }

        System.err.println(count);

        if (count > 80) {
            throw new InvalidValueException("Can't solve ODE on this bounds with this accuracy");
        } else {
            return new OrdinaryDifferentialEquationSolverResult(
                    InterpolationSolver.solveInterpolation(InterpolationMethodType.NEWTON_POLYNOMIAL, points),
                    points
            );
        }
    }

    private static ArrayList<Point> eulerMethodSolution(
            OrdinaryDifferentialEquation equation,
            Point point0,
            double h,
            int count
    ) {
        ArrayList<Point> points = new ArrayList<>();
        points.add(point0);

        double x = point0.first;
        double y = point0.second;

        for (int i = 1; i < count; i++) {
            y = y + h * equation.getValue(x, y);
            x += h;

            points.add(new Point(x, y));
        }

        return points;
    }

    private static ArrayList<Point> improvedEulerMethodSolution(
            OrdinaryDifferentialEquation equation,
            Point point0,
            double h,
            int count
    ) {
        ArrayList<Point> points = new ArrayList<>();
        points.add(point0);

        double x = point0.first;
        double y = point0.second;

        for (int i = 1; i < count; i++) {
            double stepY = y + h * equation.getValue(x, y);
            y = y + h * (equation.getValue(x, y) + equation.getValue(x + h, stepY)) / 2;
            x += h;

            points.add(new Point(x, y));
        }

        return points;
    }

    private static ArrayList<Point> rungeKuttaMethodSolution(
            OrdinaryDifferentialEquation equation,
            Point point0,
            double h,
            int count
    ) {
        ArrayList<Point> points = new ArrayList<>();
        points.add(point0);

        double x = point0.first;
        double y = point0.second;

        for (int i = 1; i < count; i++) {
            double k0 = h * equation.getValue(x, y);
            double k1 = h * equation.getValue(x + h / 2, y + k0 / 2);
            double k2 = h * equation.getValue(x + h / 2, y + k1 / 2);
            double k3 = h * equation.getValue(x + h, y + k2);

            y = y + (k0 + 2 * k1 + 2 * k2 + k3) / 6;
            x += h;

            points.add(new Point(x, y));
        }

        return points;
    }

    private static ArrayList<Point> adamsMethodSolution(
            OrdinaryDifferentialEquation equation,
            Point point0,
            double h,
            int count,
            double accuracy
    ) {
        ArrayList<Point> points = new ArrayList<>(rungeKuttaMethodSolution(equation, point0, h, 4));

        double[] yDerivative = new double[count];

        for (int i = 0; i < 4; i++) {
            yDerivative[i] = equation.getValue(points.get(i).first, points.get(i).second);
        }

        double x = points.get(3).first;
        double y = points.get(3).second;

        for (int i = 4; i < count; i++) {
            double yPredicted = y + h * (55 * yDerivative[i - 1] - 59 * yDerivative[i - 2]
                    + 37 * yDerivative[i - 3] - 9 * yDerivative[i - 4]) / 24;
            x += h;

            double prevY;
            double newY = yPredicted;

            do {
                prevY = newY;
                yDerivative[i] = equation.getValue(x, prevY);
                newY = y + h * (9 * yDerivative[i] + 19 * yDerivative[i - 1]
                        - 5 * yDerivative[i - 2] + yDerivative[i - 3]) / 24;
            } while (Math.abs(prevY - newY) > accuracy);

            y = newY;

            points.add(new Point(x, y));
        }

        return points;
    }
}
