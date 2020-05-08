package back;

import java.util.ArrayList;

public enum OrdinaryDifferentialEquations implements OrdinaryDifferentialEquation {
    EQUATION_1 {    //y' = 2x^2
        @Override
        public double getValue(double xValue, double yValue) {
            return 2 * Math.pow(xValue, 2);
        }

        @Override
        public ConstEquation getGeneralSolution() {
            return new ConstEquation() {
                @Override
                public DerivativeFunc getFunction(ArrayList<Double> consts) {
                    return new DerivativeFunc() {
                        @Override
                        public double get(double argument) {
                            return 2 * Math.pow(argument, 3) / 3 + consts.get(0);
                        }

                        @Override
                        public Interval[] getNotAllowedScope() {
                            return new Interval[0];
                        }
                    };
                }

                @Override
                public ArrayList<Double> resolveConsts(Point[] points) {
                    ArrayList<Double> consts = new ArrayList<>();

                    //FIXME(Not for University course): work only for one point now
                    consts.add(points[0].second - 2 * Math.pow(points[0].first, 3) / 3);

                    return consts;
                }
            };
        }

        @Override
        public String toString() {
            return "y' = 2x^2";
        }
    },
    EQUATION_2 {    //y' = x - y
        @Override
        public double getValue(double xValue, double yValue) {
            return xValue - yValue;
        }

        @Override
        public ConstEquation getGeneralSolution() {
            return new ConstEquation() {
                @Override
                public DerivativeFunc getFunction(ArrayList<Double> consts) {
                    return new DerivativeFunc() {
                        @Override
                        public double get(double argument) {
                            return consts.get(0) * Math.exp(-argument) + argument - 1;
                        }

                        @Override
                        public Interval[] getNotAllowedScope() {
                            return new Interval[0];
                        }
                    };
                }

                @Override
                public ArrayList<Double> resolveConsts(Point[] points) {
                    ArrayList<Double> consts = new ArrayList<>();

                    //FIXME(Not for University course): work only for one point now
                    consts.add((points[0].second + 1 - points[0].first) * Math.exp(points[0].first));

                    return consts;
                }
            };
        }

        @Override
        public String toString() {
            return "y' = x - y";
        }
    },
    EQUATION_3 {    //y' = sin(x) + y
        @Override
        public double getValue(double xValue, double yValue) {
            return Math.sin(xValue) + yValue;
        }

        @Override
        public ConstEquation getGeneralSolution() {
            return new ConstEquation() {
                @Override
                public DerivativeFunc getFunction(ArrayList<Double> consts) {
                    return new DerivativeFunc() {
                        @Override
                        public double get(double argument) {
                            return consts.get(0) * Math.exp(argument) - Math.sin(argument) / 2 - Math.cos(argument) / 2;
                        }

                        @Override
                        public Interval[] getNotAllowedScope() {
                            return new Interval[0];
                        }
                    };
                }

                @Override
                public ArrayList<Double> resolveConsts(Point[] points) {
                    ArrayList<Double> consts = new ArrayList<>();

                    //FIXME(Not for University course): work only for one point now
                    consts.add((points[0].second + Math.sin(points[0].first) / 2 + Math.cos(points[0].first) / 2) * Math.exp(-points[0].first));

                    return consts;
                }
            };
        }

        @Override
        public String toString() {
            return "y' = sin(x) + y";
        }
    }
}
