package com.wildbitsfoundry;

import com.wildbitsfoundry.etk4j.math.linearalgebra.MatrixSparse;
import com.wildbitsfoundry.etk4j.util.DoubleArrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        ckt1();
//        ckt2();
//        ckt3();
//        ckt4();
//        ckt5();
//        ckt6();
//        ckt7();
//        ckt8();
//        ckt9();
//        ckt10();
        ckt11();
    }

    public static void ckt1() {
        List<CircuitElement> elementList = new ArrayList<>();
        elementList.add(new VoltageSource("V1", 2, 1, 32, 0));
        elementList.add(new Resistor("R1", 1, 0, 2));
        elementList.add(new Resistor("R2", 2, 0, 8));
        elementList.add(new Resistor("R3", 2, 3, 4));
        elementList.add(new VoltageSource("V2", 3, 0, 20, 1));

        int numNodes = 3;
        int numVoltageSources = 2;
        int size = numNodes + numVoltageSources;

        MatrixSparse mnaMatrix = new MatrixSparse(size, size);
        double[] rhs = new double[size];

        for(CircuitElement element : elementList) {
            element.stamp(mnaMatrix, rhs);
        }

        double[] solution = mnaMatrix.solve(rhs).toDense().getCol(0);
        System.out.println("Node voltages: " + Arrays.toString(solution));
    }

    public static void ckt2() {
        List<CircuitElement> elementList = new ArrayList<>();
        elementList.add(new VoltageSource("V1", 1, 0, 10, 0));
        elementList.add(new Resistor("R1", 1, 2, 10));
        elementList.add(new Resistor("R2", 2, 0, 10));

        int numNodes = 2;
        int numVoltageSources = 1;
        int size = numNodes + numVoltageSources;

        MatrixSparse mnaMatrix = new MatrixSparse(size, size);
        double[] rhs = new double[size];

        for(CircuitElement element : elementList) {
            element.stamp(mnaMatrix, rhs);
        }

        double[] solution = mnaMatrix.solve(rhs).toDense().getCol(0);
        System.out.println("Node voltages: " + Arrays.toString(solution));
    }

    public static void ckt3() {
        List<CircuitElement> elementList = new ArrayList<>();
        elementList.add(new VoltageSource("V1", 1, 0, 10, 0));
        elementList.add(new Resistor("R1", 1, 2, 10));
        elementList.add(new Resistor("R2", 2, 0, 10));

        int numNodes = 2;
        int numVoltageSources = 1;
        int size = numNodes + numVoltageSources;

        MatrixSparse mnaMatrix = new MatrixSparse(size, size);
        double[] rhs = new double[size];

        for(CircuitElement element : elementList) {
            element.stamp(mnaMatrix, rhs);
        }

        System.out.println(Arrays.toString(rhs));
        System.out.println((mnaMatrix.toDense()));

        double[] solution = mnaMatrix.solve(rhs).toDense().getCol(0);
        System.out.println("Node voltages: " + Arrays.toString(solution));
    }

    public static void ckt4() {
        List<CircuitElement> elementList = new ArrayList<>();
        elementList.add(new VoltageSource("V1", 1, 0, 10, 0));
        elementList.add(new Resistor("R1", 1, 2, 10));
        elementList.add(new Resistor("R2", 2, 0, 10));
        elementList.add(new VoltageSource("V2", 2, 3, 0, 1));
        elementList.add(new Resistor("R3", 3, 0, 10));

        int numNodes = 3;
        int numVoltageSources = 2;
        int size = numNodes + numVoltageSources;

        MatrixSparse mnaMatrix = new MatrixSparse(size, size);
        double[] rhs = new double[size];

        for(CircuitElement element : elementList) {
            element.stamp(mnaMatrix, rhs);
        }

        double[] solution = mnaMatrix.solve(rhs).toDense().getCol(0);
        System.out.println("Node voltages: " + Arrays.toString(solution));
    }

    public static void ckt5() {
        List<CircuitElement> elementList = new ArrayList<>();
        elementList.add(new VoltageSource("V1", 1, 0, 10, 0));
        elementList.add(new Resistor("R1", 2, 0, 10));

        int numNodes = 2;
        int numVoltageSources = 1;
        int size = numNodes + numVoltageSources;

        MatrixSparse mnaMatrix = new MatrixSparse(size, size);
        double[] rhs = new double[size];

        for(CircuitElement element : elementList) {
            element.stamp(mnaMatrix, rhs);
        }

        double[] solution = mnaMatrix.solve(rhs).toDense().getCol(0);
        System.out.println("Node voltages: " + Arrays.toString(solution));
    }

    private static void ckt6() {
        List<CircuitElement> elementList = new ArrayList<>();
        elementList.add(new VoltageSource("V1", 1, 0, 10, 0));
        elementList.add(new Resistor("R1", 1, 2, 1000));
        elementList.add(new Capacitor("C1", 2, 0, 1e-6, 5));

        int numNodes = 2;
        int numVoltageSources = 1;
        int size = numNodes + numVoltageSources;
        double dt = 1e-6;
        double tol = 1e-3;
        double maxDt = 0.1;
        double minDt = 1e-6;
        double maxSteps = 100;  // Find better name
        double[] time = DoubleArrays.linSpace(0, 10e-3, 10);
        IntegrationMethod integrationMethod = IntegrationMethod.GEAR_2ND_ORDER;
        double[] capVoltage = new double[time.length];

        for(int i = 0; i < time.length; i++) {
            MatrixSparse mnaMatrix = new MatrixSparse(size, size);
            double[] rhs = new double[size];

            double t = time[i] + dt;
            for(CircuitElement element : elementList) {
                if(element instanceof ReactiveElement) {
                    ((ReactiveElement) element).stamp(mnaMatrix, rhs, t, integrationMethod);
                } else {
                    element.stamp(mnaMatrix, rhs);
                }
            }

            double[] solution = mnaMatrix.solve(rhs).toDense().getCol(0);
            capVoltage[i] = solution[1];

            for(CircuitElement element : elementList) {
                if(element instanceof ReactiveElement) {
                    ((ReactiveElement) element).updateMemory(solution, t, integrationMethod);
                }
            }
        }
        System.out.println("Cap voltage: " + Arrays.toString(capVoltage));
    }

    public static void ckt7() {
        List<CircuitElement> elementList = new ArrayList<>();
        elementList.add(new CurrentSource("I1", 0, 1, 1));
        elementList.add(new CurrentSource("I2", 0, 1, 1));
        elementList.add(new Resistor("R1", 1, 0, 1));
        elementList.add(new Resistor("R2", 1, 2, 1));
        elementList.add(new VoltageSource("V1", 2, 0, 0, 0));

        int numNodes = 2;
        int numVoltageSources = 1;
        int size = numNodes + numVoltageSources;

        MatrixSparse mnaMatrix = new MatrixSparse(size, size);
        double[] rhs = new double[size];

        for(CircuitElement element : elementList) {
            element.stamp(mnaMatrix, rhs);
        }

        double[] solution = mnaMatrix.solve(rhs).toDense().getCol(0);
        System.out.println("Node voltages: " + Arrays.toString(solution));
    }

    public static void ckt8() {
        List<CircuitElement> elementList = new ArrayList<>();
        elementList.add(new CurrentSource("I1", 0, 1, 1));
        elementList.add(new Resistor("R1", 1, 2, 1000));
        elementList.add(new Inductor("L1", 2, 0, 1e-3, 0, 0));
        //elementList.add(new VoltageSource("V1", 3, 0, 0, 0));

        int numNodes = 2;
        int numVoltageSources = 1;
        int size = numNodes + numVoltageSources;
        IntegrationMethod integrationMethod = IntegrationMethod.BACKWARDS_EULER;
        double dt = 1e-4;
        double[] time = DoubleArrays.linSpace(0, 10e-3, 10);
        double[] inductorCurrent = new double[time.length];
        double[] inductorVoltage = new double[time.length];

        for(int i = 0; i < time.length; i++) {
            MatrixSparse mnaMatrix = new MatrixSparse(size, size);
            double[] rhs = new double[size];

            double t = time[i] + dt;
            for (CircuitElement element : elementList) {
                if(element instanceof ReactiveElement) {
                    ((ReactiveElement) element).stamp(mnaMatrix, rhs, t, integrationMethod);
                } else {
                    element.stamp(mnaMatrix, rhs);
                }
            }

            System.out.println("RHS: " + Arrays.toString(rhs));
            System.out.println((mnaMatrix.toDense()));
            double[] solution = mnaMatrix.solve(rhs).toDense().getCol(0);
            inductorCurrent[i] = solution[solution.length - 1];
            inductorVoltage[i] = solution[solution.length - 2];

            System.out.println("Solution:" + Arrays.toString(solution));

            for (CircuitElement element : elementList) {
                if(element instanceof ReactiveElement) {
                    ((ReactiveElement) element).updateMemory(solution, t, integrationMethod);
                }
            }
        }
        System.out.println("Inductor current: " + Arrays.toString(inductorCurrent));
        System.out.println("Inductor Voltage: " + Arrays.toString(inductorVoltage));
    }

    public static void ckt9() {
        List<CircuitElement> elementList = new ArrayList<>();
        elementList.add(new CurrentSource("I1", 0, 1, 1));
        elementList.add(new Resistor("R1", 1, 2, 1000));
        elementList.add(new Inductor("L1", 2, 3, 1e-3, 1e-3, 1));
        elementList.add(new VoltageSource("V1", 3, 0, 0, 0));

        int numNodes = 3;
        int numVoltageSources = 2;
        int size = numNodes + numVoltageSources;
        IntegrationMethod integrationMethod = IntegrationMethod.BACKWARDS_EULER;
        double dt = 1e-4;
        double[] time = DoubleArrays.linSpace(0, 10e-3, 10);
        double[] inductorCurrent = new double[time.length];
        double[] inductorVoltage = new double[time.length];

        for(int i = 0; i < time.length; i++) {
            MatrixSparse mnaMatrix = new MatrixSparse(size, size);
            double[] rhs = new double[size];

            double t = time[i] + dt;
            for (CircuitElement element : elementList) {
                if(element instanceof ReactiveElement) {
                    ((ReactiveElement) element).stamp(mnaMatrix, rhs, t, integrationMethod);
                } else {
                    element.stamp(mnaMatrix, rhs);
                }
            }

            System.out.println("RHS: " + Arrays.toString(rhs));
            System.out.println((mnaMatrix.toDense()));
            double[] solution = mnaMatrix.solve(rhs).toDense().getCol(0);
            inductorCurrent[i] = solution[solution.length - 1];
            inductorVoltage[i] = solution[solution.length - 3];

            System.out.println("Solution:" + Arrays.toString(solution));

            for (CircuitElement element : elementList) {
                if(element instanceof ReactiveElement) {
                    ((ReactiveElement) element).updateMemory(solution, t, integrationMethod);
                }
            }
        }
        System.out.println("Inductor current: " + Arrays.toString(inductorCurrent));
        System.out.println("Inductor Voltage: " + Arrays.toString(inductorVoltage));
    }

    public static void ckt10() {
        List<CircuitElement> elementList = new ArrayList<>();
        elementList.add(new CurrentSource("I1", 0, 1, 1));
        elementList.add(new Resistor("R1", 1, 2, 1000));
        elementList.add(new Inductor("L1", 2, 3, 1e-3, 0, 1));
        elementList.add(new VoltageSource("V1", 3, 0, 0, 0));

        int numNodes = 3;
        int numVoltageSources = 2;
        int size = numNodes + numVoltageSources;
        IntegrationMethod integrationMethod = IntegrationMethod.GEAR_2ND_ORDER;
        double dt = 1e-4;
        double[] time = DoubleArrays.linSpace(0, 10e-3, 10);
        double[] inductorCurrent = new double[time.length];
        double[] inductorVoltage = new double[time.length];

        for(int i = 0; i < time.length; i++) {
            MatrixSparse mnaMatrix = new MatrixSparse(size, size);
            double[] rhs = new double[size];

            double t = time[i] + dt;
            for (CircuitElement element : elementList) {
                if(element instanceof ReactiveElement) {
                    ((ReactiveElement) element).stamp(mnaMatrix, rhs, t, integrationMethod);
                } else {
                    element.stamp(mnaMatrix, rhs);
                }
            }

            System.out.println("RHS: " + Arrays.toString(rhs));
            System.out.println((mnaMatrix.toDense()));
            double[] solution = mnaMatrix.solve(rhs).toDense().getCol(0);
            inductorCurrent[i] = solution[solution.length - 2];
            inductorVoltage[i] = solution[solution.length - 3];

            System.out.println("Solution:" + Arrays.toString(solution));

            for (CircuitElement element : elementList) {
                if(element instanceof ReactiveElement) {
                    ((ReactiveElement) element).updateMemory(solution, t, integrationMethod);
                }
            }
        }
        System.out.println("Inductor current: " + Arrays.toString(inductorCurrent));
        System.out.println("Inductor Voltage: " + Arrays.toString(inductorVoltage));
    }

    public static void ckt11() {
        List<CircuitElement> elementList = new ArrayList<>();
        elementList.add(new CurrentSource("I1", 0, 1, 1));
        elementList.add(new Resistor("R1", 1, 2, 1000));
        elementList.add(new Inductor("L1", 2, 3, 1e-3, 0, 1));
//        elementList.add(new Capacitor("C1", 2, 3, 1e-6, 0));
        elementList.add(new VoltageSource("V1", 3, 0, 0, 0));

        int numNodes = 3;
        int numVoltageSources = 2;
        int size = numNodes + numVoltageSources;
        IntegrationMethod integrationMethod = IntegrationMethod.TRAPEZOIDAL;
        double dt = 1e-4;
        double[] time = DoubleArrays.linSpace(0, 10e-3, 10);
        double[] inductorCurrent = new double[time.length];
        double[] inductorVoltage = new double[time.length];

        for(int i = 0; i < time.length; i++) {
            MatrixSparse mnaMatrix = new MatrixSparse(size, size);
            double[] rhs = new double[size];

            double t = time[i] + dt;
            for (CircuitElement element : elementList) {
                if(element instanceof ReactiveElement) {
                    ((ReactiveElement) element).stamp(mnaMatrix, rhs, t, integrationMethod);
                } else {
                    element.stamp(mnaMatrix, rhs);
                }
            }

            System.out.println("RHS: " + Arrays.toString(rhs));
            System.out.println((mnaMatrix.toDense()));
            double[] solution = mnaMatrix.solve(rhs).toDense().getCol(0);
            inductorCurrent[i] = solution[solution.length - 2];
            inductorVoltage[i] = solution[solution.length - 3];

            System.out.println("Solution:" + Arrays.toString(solution));

            for (CircuitElement element : elementList) {
                if(element instanceof ReactiveElement) {
                    ((ReactiveElement) element).updateMemory(solution, t, integrationMethod);
                }
            }
        }
        System.out.println("Inductor current: " + Arrays.toString(inductorCurrent));
        System.out.println("Inductor Voltage: " + Arrays.toString(inductorVoltage));
    }
}