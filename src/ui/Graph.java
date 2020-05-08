package ui;

import back.Function;
import back.exception.NotAllowedScopeException;
import back.exception.UnavailableCodeException;
import back.Point;
import javafx.scene.chart.XYChart;

import java.util.List;

public class Graph {
    private static final double EPS = 1e-9d;

    private final XYChart<Double, Double> graph;

    public Graph(final XYChart<Double, Double> graph) {
        this.graph = graph;
    }

    public void plotLine(final Function function, final double lowerBound, final double upperBound)
            throws NotAllowedScopeException, UnavailableCodeException {
        final XYChart.Series<Double, Double> series = new XYChart.Series<>();

        int i = 0;
        for (double x = lowerBound; i < 1001; x += (upperBound - lowerBound) / 1000d) {
            plotPoint(x, function.getValue(x), series);
            i++;
        }

        graph.getData().add(series);
    }

    public void plotLine(List<Point> points) {
        final XYChart.Series<Double, Double> series = new XYChart.Series<>();

        for (Point p : points) {
            plotPoint(p.first, p.second, series);
        }

        graph.getData().add(series);
    }

    private void plotPoint(final double x, final double y,
                           final XYChart.Series<Double, Double> series) {
        series.getData().add(new XYChart.Data<>(x, y));
    }

    public void clear() {
        graph.getData().clear();
    }

    public void plotPoint(final double x, final double y) {
        final XYChart.Series<Double, Double> series = new XYChart.Series<>();

        plotPoint(x, y, series);

        graph.getData().add(series);
    }
}