package back.solution;

import back.Function;
import back.Point;

import java.util.ArrayList;

public class OrdinaryDifferentialEquationSolverResult {
    private final ArrayList<Point> points;
    private final Function function;

    public OrdinaryDifferentialEquationSolverResult(Function function, ArrayList<Point> points) {
        this.points = points;
        this.function = function;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public Function getFunction() {
        return function;
    }
}
