package back;

import java.util.ArrayList;

public interface ConstEquation {
    default Function getFunction(Point[] points) {
        return getFunction(resolveConsts(points));
    }

    DerivativeFunc getFunction(ArrayList<Double> consts);
    ArrayList<Double> resolveConsts(Point[] points);    //TODO(Not for University course): automatically resolve consts
}
