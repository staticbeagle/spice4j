package com.wildbitsfoundry;

import com.wildbitsfoundry.etk4j.math.MathETK;
import com.wildbitsfoundry.etk4j.math.linearalgebra.MatrixSparse;
import com.wildbitsfoundry.etk4j.util.ComplexArrays;
import com.wildbitsfoundry.etk4j.util.DoubleArrays;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        ckt1();
//        ckt2();
//        ckt3();
        ckt4();
//        ckt5();
        ckt6();
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

        MatrixSparse manMatrix = new MatrixSparse(size, size);
        double[] rhs = new double[size];

        for(CircuitElement element : elementList) {
            element.stamp(manMatrix, rhs);
        }

        double[] solution = manMatrix.solve(rhs).toDense().getCol(0);
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

        MatrixSparse manMatrix = new MatrixSparse(size, size);
        double[] rhs = new double[size];

        for(CircuitElement element : elementList) {
            element.stamp(manMatrix, rhs);
        }

        double[] solution = manMatrix.solve(rhs).toDense().getCol(0);
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

        MatrixSparse manMatrix = new MatrixSparse(size, size);
        double[] rhs = new double[size];

        for(CircuitElement element : elementList) {
            element.stamp(manMatrix, rhs);
        }

        double[] solution = manMatrix.solve(rhs).toDense().getCol(0);
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

        MatrixSparse manMatrix = new MatrixSparse(size, size);
        double[] rhs = new double[size];

        for(CircuitElement element : elementList) {
            element.stamp(manMatrix, rhs);
        }

        double[] solution = manMatrix.solve(rhs).toDense().getCol(0);
        System.out.println("Node voltages: " + Arrays.toString(solution));
    }

    public static void ckt5() {
        List<CircuitElement> elementList = new ArrayList<>();
        elementList.add(new VoltageSource("V1", 1, 0, 10, 0));
        elementList.add(new Resistor("R1", 2, 0, 10));

        int numNodes = 2;
        int numVoltageSources = 1;
        int size = numNodes + numVoltageSources;

        MatrixSparse manMatrix = new MatrixSparse(size, size);
        double[] rhs = new double[size];

        for(CircuitElement element : elementList) {
            element.stamp(manMatrix, rhs);
        }

        double[] solution = manMatrix.solve(rhs).toDense().getCol(0);
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
        double dt = 0.1;
        double tol = 1e-3;
        double maxDt = 0.1;
        double minDt = 1e-6;
        double maxSteps = 100;  // Find better name
        double[] time = DoubleArrays.linSpace(0, 10e-3, 10);
        double[] capVoltage = new double[time.length];

        double previousError = 0;
        for(int i = 0; i < time.length; i++) {
            MatrixSparse mnaMatrix = new MatrixSparse(size, size);
            double[] rhs = new double[size];

            double t = time[i] + dt;
            for(CircuitElement element : elementList) {
                element.stamp(mnaMatrix, rhs, t);
            }

            System.out.println(mnaMatrix.toDense());
            System.out.println(Arrays.toString(rhs));
            // Gear-2 solution
            double[] solutionGear2 = mnaMatrix.solve(rhs).toDense().getCol(0);
            capVoltage[i] = solutionGear2[1];

            System.out.println(Arrays.toString(solutionGear2));


            // Backwards Euler (Gear - 1)
//            MatrixSparse mnaMatrixBe = new MatrixSparse(size, size);
//            double[] rhsBe = new double[size];

//            for(CircuitElement element : elementList) {
//                element.accept(visitor, mnaMatrixBe, rhsBe, dt, 0);
//            }
//
//            double[] solutionGear1 = mnaMatrixBe.solve(rhsBe).toDense().getCol(0);
//            //double error = DoubleArrays.norm(DoubleArrays.subtractElementWise(solutionGear2, solutionGear1));
//            double error = Math.abs(solutionGear2[1] - solutionGear1[1]);
//            if(error > tol) {
//                dt = Math.max(dt / 2, minDt);
//            } else if(error < tol / 10) {
//                dt = Math.min(dt * 2, maxDt);
//            }

            for(CircuitElement element : elementList) {
                element.updateMemory(solutionGear2);
            }

            //System.out.printf("Step %d: dt = %.6f, Error = %.6f, V2 = %.6f%n", i, dt, 0.1, solutionGear2[1]);

//            if(Math.abs(error - previousError) < 1e-6) {
//                //break;
//            }
//            previousError = error;
        }
        System.out.println("Cap voltage: " + Arrays.toString(capVoltage));
    }
}