package ui;

import back.Function;
import back.OrdinaryDifferentialEquation;
import back.OrdinaryDifferentialEquations;
import back.Point;
import back.exception.InvalidValueException;
import back.exception.NotAllowedScopeException;
import back.exception.UnavailableCodeException;
import back.solution.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private static final double EPS = 1e-9;

    @FXML
    private Label helpPane;

    @FXML
    private VBox odePane;

    @FXML
    private AnchorPane result;

    @FXML
    private Label error;

    @FXML
    private LineChart<Double, Double> chart;
    private Graph mathGraph;

    @FXML
    private NumberAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    @FXML
    private ChoiceBox<OrdinaryDifferentialEquationMethodType> method;

    @FXML
    private ChoiceBox<OrdinaryDifferentialEquation> equation;

    @FXML
    private TextField x0;

    private double getX0() throws InvalidValueException {
        try {
            return Double.parseDouble(x0.getText());
        } catch (NumberFormatException e) {
            throw new InvalidValueException("Invalid x0 value!!!");
        }
    }

    @FXML
    private TextField xn;

    private double getXn() throws InvalidValueException {
        try {
            return Double.parseDouble(xn.getText());
        } catch (NumberFormatException e) {
            throw new InvalidValueException("Invalid xn value!!!");
        }
    }

    @FXML
    private TextField y0;

    private double getY0() throws InvalidValueException {
        try {
            return Double.parseDouble(y0.getText());
        } catch (NumberFormatException e) {
            throw new InvalidValueException("Invalid y0 value!!!");
        }
    }

    @FXML
    private TextField accuracy;

    private double getAccuracy() throws InvalidValueException {
        try {
            return Double.parseDouble(accuracy.getText());
        } catch (NumberFormatException e) {
            throw new InvalidValueException("Invalid accuracy value!!!");
        }
    }

    @FXML
    private TextField xResult;

    private double getXResult() throws InvalidValueException {
        try {
            return Double.parseDouble(xResult.getText());
        } catch (NumberFormatException e) {
            throw new InvalidValueException("Invalid X value!!!");
        }
    }

    @FXML
    private TextField yResult;

    private Function resultFunction;
    private ArrayList<Point> points;

    @FXML
    private void setVisibleHelpPane() {
        helpPane.setVisible(true);
        odePane.setVisible(false);
        error.setVisible(false);
        result.setVisible(false);

        mathGraph.clear();
    }

    @FXML
    private void setVisibleODEPane() {
        helpPane.setVisible(false);
        odePane.setVisible(true);
        error.setVisible(true);

        mathGraph.clear();

        method.setValue(OrdinaryDifferentialEquationMethodType.ADAMS_METHOD);
        equation.setValue(OrdinaryDifferentialEquations.EQUATION_1);

        setDefaultValues();
    }

    @FXML
    private void calculate() {
        clearError();
        try {
            validateValues();

            OrdinaryDifferentialEquationSolverResult solutionResult
                    = OrdinaryDifferentialEquationSolver.solveODE(
                            method.getValue(),
                            equation.getValue(),
                            new Point(getX0(), getY0()),
                            Math.abs(getXn() - getX0()),
                            getAccuracy()
                    );

            resultFunction = solutionResult.getFunction();
            points = solutionResult.getPoints();

            xResult.setText("0");
            updateYResult();
            result.setVisible(true);

            updateChart();
        } catch (Exception e) {
            result.setVisible(false);
            error.setText(e.getMessage());
        }
    }

    @FXML
    private void updateChart() {
        try {
            mathGraph.clear();

            double left = Math.min(getX0(), getXResult());
            double right = Math.max(getXResult(), getXn());

            drawRealFunction(left, right);
            drawPoints();
            drawResultFunction(left, right);
            drawResultPoint();

            xAxis.setAutoRanging(false);
            double stepX = (right - left) / 10;
            xAxis.setLowerBound(left - stepX);
            xAxis.setUpperBound(right + stepX);
            xAxis.setTickUnit(stepX);

            double up = getUpperBound();
            double low = getLowerBound();

            yAxis.setAutoRanging(false);
            double stepY = (up - low) / 10;
            yAxis.setLowerBound(low - stepY);
            yAxis.setUpperBound(up + stepY);
            yAxis.setTickUnit(stepY);
        } catch (Exception e) {
            error.setText(e.getMessage());
        }
    }

    private double getUpperBound() throws InvalidValueException, UnavailableCodeException, NotAllowedScopeException {
        double res = resultFunction.getValue(getXResult());

        for (Point p : points) {
            res = Math.max(res, p.second);
            res = Math.max(res, equation.getValue().getSpecificSolution(new Point[]{new Point(getX0(), getY0())}).getValue(p.first));
        }

        return res;
    }

    private double getLowerBound() throws InvalidValueException, UnavailableCodeException, NotAllowedScopeException {
        double res = resultFunction.getValue(getXResult());

        for (Point p : points) {
            res = Math.min(res, p.second);
            res = Math.min(res, equation.getValue().getSpecificSolution(new Point[]{new Point(getX0(), getY0())}).getValue(p.first));
        }

        return res;
    }

    private void drawRealFunction(double left, double right) throws UnavailableCodeException, NotAllowedScopeException, InvalidValueException {
        mathGraph.plotLine(equation.getValue().getSpecificSolution(new Point[]{new Point(getX0(), getY0())}), left, right);
    }

    private void drawPoints() {
        mathGraph.plotLine(points);
    }

    private void drawResultFunction(double left, double right) throws UnavailableCodeException, NotAllowedScopeException {
        mathGraph.plotLine(resultFunction, left, right);
    }

    private void drawResultPoint() throws InvalidValueException, UnavailableCodeException, NotAllowedScopeException {
        mathGraph.plotPoint(getXResult(), resultFunction.getValue(getXResult()));
    }

    private void intInit() {
        ObservableList<OrdinaryDifferentialEquationMethodType> methods
                = FXCollections.observableArrayList(
                        OrdinaryDifferentialEquationMethodType.EULER_METHOD,
                        OrdinaryDifferentialEquationMethodType.IMPROVED_EULER_METHOD,
                        OrdinaryDifferentialEquationMethodType.RUNGE_KUTTA_METHOD,
                        OrdinaryDifferentialEquationMethodType.ADAMS_METHOD,
                        OrdinaryDifferentialEquationMethodType.MILNE_METHOD
        );

        method.setItems(methods);
        method.setValue(OrdinaryDifferentialEquationMethodType.ADAMS_METHOD);

        ObservableList<OrdinaryDifferentialEquation> equations = FXCollections.observableArrayList(
                OrdinaryDifferentialEquations.EQUATION_1,
                OrdinaryDifferentialEquations.EQUATION_2,
                OrdinaryDifferentialEquations.EQUATION_3
        );
        equation.setItems(equations);
        equation.setValue(OrdinaryDifferentialEquations.EQUATION_1);

        setDefaultValues();

        method.getSelectionModel().
                selectedItemProperty().
                addListener(
                (observable, oldValue, newValue) -> {
                    setDefaultValues();
                    calculate();
                });

        equation.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    setDefaultValues();
                    calculate();
                });

        x0.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("-?\\d{0,2}([.]\\d{0,4})?")) {
                x0.setText(oldValue);
            } else {
                calculate();
            }
        });

        y0.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("-?\\d{0,2}([.]\\d{0,4})?")) {
                y0.setText(oldValue);
            } else {
                calculate();
            }
        });

        xn.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("-?\\d{0,2}([.]\\d{0,4})?")) {
                xn.setText(oldValue);
            } else {
                calculate();
            }
        });

        accuracy.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("-?\\d?([.]\\d{0,5})?")) {
                accuracy.setText(oldValue);
            } else {
                calculate();
            }
        });

        xResult.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("-?\\d{0,2}([.]\\d{0,4})?")) {
                xResult.setText(oldValue);
            } else {
                try {
                    clearError();
                    updateYResult();
                    updateChart();
                } catch (InvalidValueException | UnavailableCodeException | NotAllowedScopeException e) {
                    error.setText(e.getMessage());
                }
            }
        });
    }

    private void updateYResult()
            throws InvalidValueException, UnavailableCodeException, NotAllowedScopeException {
        yResult.setText(String.format("%.4f", resultFunction.getValue(getXResult())).replace(',', '.'));
    }

    private void validateValues() throws InvalidValueException {
        if (getXn() - getX0() < EPS) {
            throw new InvalidValueException("x0 should be less than xn");
        }

        if (getAccuracy() < EPS) {
            throw new InvalidValueException("Accuracy should be more than 0");
        }
    }

    private void setDefaultValues() {
        x0.setText("0");
        y0.setText("0");
        xn.setText("9");
        accuracy.setText("0.001");

        clearError();
        result.setVisible(false);
    }

    private void clearError() {
        error.setText("");
    }

    private void chartInit() {
        mathGraph = new Graph(chart);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        intInit();
        chartInit();
        setVisibleHelpPane();
    }
}
