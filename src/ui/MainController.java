package ui;

import back.Function;
import back.exception.InvalidValueException;
import back.exception.NotAllowedScopeException;
import back.exception.UnavailableCodeException;
import back.solution.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
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
    private ChoiceBox<OrdinaryDifferentialEquationSolutionType> method;

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
    private TextField xCount;

    private int getXCount() throws InvalidValueException {
        try {
            return Integer.parseInt(xCount.getText());
        } catch (NumberFormatException e) {
            throw new InvalidValueException("Invalid points count value!!!");
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

        //FiXME: set default values
//        method.setValue(InterpolationMethodType.NEWTON_POLYNOMIAL);
//        equation.setValue(Functions.FUNCTION_2);

        setDefaultValues();
    }

    @FXML
    private void calculate() {
        //TODO: calculate
    }

//    private void intCalculate() {
//        try {
//            if (intPoints.getItems().isEmpty()) {
//                throw new InvalidValueException("Please generate points before calculating");
//            }
//
//            resultFunction = InterpolationSolver.solveInterpolation(intMethod.getValue(), intPoints.getItems());
//
//            resultXCoord.setText("0");
//            updateYResult();
//            result.setVisible(true);
//
//            updateIntChart();
//        } catch (Exception e) {
//            result.setVisible(false);
//            error.setText(e.getMessage());
//        }
//    }

    @FXML
    private void updateChart() {
        //TODO: update chart
//        try {
//            mathGraph.clear();
//
//            if (result.isVisible()) {
//                double left = Math.min(getLeftBound(), getResultXCoord());
//                double right = Math.max(getResultXCoord(), getRightBound());
//                drawFunction(left, right);
//                drawPoints();
//                drawResultFunction(left, right);
//                drawResultPoint();
//            } else {
//                drawFunction(getLeftBound(), getRightBound());
//                drawPoints();
//            }
//        } catch (Exception e) {
//            error.setText(e.getMessage());
//        }
    }

    private void drawResultFunction(double left, double right) throws UnavailableCodeException, NotAllowedScopeException {
        mathGraph.plotLine(
                resultFunction,
                left,
                right
        );
    }

    private void drawResultPoint() throws InvalidValueException, UnavailableCodeException, NotAllowedScopeException {
        mathGraph.plotPoint(getXResult(), resultFunction.getValue(getXResult()));
    }


    private void intInit() {
//        FIXME
//        ObservableList<InterpolationMethodType> intMethods
//                = FXCollections.observableArrayList(
//                InterpolationMethodType.LAGRANGE_POLYNOMIAL,
//                InterpolationMethodType.NEWTON_POLYNOMIAL,
//                InterpolationMethodType.CUBIC_SPLINE
//        );
//        intMethod.setItems(intMethods);
//        intMethod.setValue(InterpolationMethodType.NEWTON_POLYNOMIAL);

//        FIXME
//        ObservableList<Function> intFunctions = FXCollections.observableArrayList(
//                Functions.FUNCTION_1,
//                Functions.FUNCTION_2,
//                Functions.FUNCTION_3
//        );
//        intFunction.setItems(intFunctions);
//        intFunction.setValue(Functions.FUNCTION_2);

        setDefaultValues();

        method.getSelectionModel().
                selectedItemProperty().
                addListener(
                (observable, oldValue, newValue) -> {
                    setDefaultValues();
                    updateChart();
                });

        equation.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    setDefaultValues();
                    updateChart();
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

        xCount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("-?\\d{0,2}")) {
                xCount.setText(oldValue);
            } else {
                calculate();
            }
        });

        xResult.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("-?\\d{0,2}([.]\\d{0,4})?")) {
                xResult.setText(oldValue);
            } else {
                clearError();

                try {
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
        if (getX0() - getXn() < EPS) {
            throw new InvalidValueException("Left bound should be less than right bound.");
        }

        if (getXCount() < 2) {
            throw new InvalidValueException("Count of Points should be more or equal than 2");
        }

        if (getXCount() > 20) {
            throw new InvalidValueException("Count of Points should be less or equal than 20");
        }
    }

    private void setDefaultValues() {
        x0.setText("0");
        y0.setText("0");
        xn.setText("9");
        accuracy.setText("0.001");
        xCount.setText("10");

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
