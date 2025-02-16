package com.wildbitsfoundry;

import com.wildbitsfoundry.etk4j.math.linearalgebra.MatrixSparse;

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
    }

    public static void ckt1() {
        List<CircuitElement> elementList = new ArrayList<>();
        elementList.add(new VoltageSource(2, 1, 32, 0));
        elementList.add(new Resistor(1, 0, 2));
        elementList.add(new Resistor(2, 0, 8));
        elementList.add(new Resistor(2, 3, 4));
        elementList.add(new VoltageSource(3, 0, 20, 1));

        int numNodes = 3;
        int numVoltageSources = 2;
        int size = numNodes + numVoltageSources;

        MatrixSparse manMatrix = new MatrixSparse(size, size);
        double[] rhs = new double[size];

        MnaBuilder visitor = new MnaBuilder();
        int voltageIndex = numNodes;

        for(CircuitElement element : elementList) {
            element.accept(visitor, manMatrix, rhs, 1, voltageIndex);
        }

        double[] solution = manMatrix.solve(rhs).toDense().getCol(0);
        System.out.println("Node voltages: " + Arrays.toString(solution));
    }

    public static void ckt2() {
        List<CircuitElement> elementList = new ArrayList<>();
        elementList.add(new VoltageSource(1, 0, 10, 0));
        elementList.add(new Resistor(1, 2, 10));
        elementList.add(new Resistor(2, 0, 10));

        int numNodes = 2;
        int numVoltageSources = 1;
        int size = numNodes + numVoltageSources;

        MatrixSparse manMatrix = new MatrixSparse(size, size);
        double[] rhs = new double[size];

        MnaBuilder visitor = new MnaBuilder();
        int voltageIndex = numNodes;

        for(CircuitElement element : elementList) {
            element.accept(visitor, manMatrix, rhs, 1, voltageIndex);
        }

        double[] solution = manMatrix.solve(rhs).toDense().getCol(0);
        System.out.println("Node voltages: " + Arrays.toString(solution));
    }

    public static void ckt3() {
        List<CircuitElement> elementList = new ArrayList<>();
        elementList.add(new VoltageSource(1, 0, 10, 0));
        elementList.add(new Resistor(1, 2, 10));
        elementList.add(new Resistor(2, 0, 10));

        int numNodes = 2;
        int numVoltageSources = 1;
        int size = numNodes + numVoltageSources;

        MatrixSparse manMatrix = new MatrixSparse(size, size);
        double[] rhs = new double[size];

        MnaBuilder visitor = new MnaBuilder();
        int voltageIndex = numNodes;

        for(CircuitElement element : elementList) {
            element.accept(visitor, manMatrix, rhs, 1, voltageIndex);
        }

        double[] solution = manMatrix.solve(rhs).toDense().getCol(0);
        System.out.println("Node voltages: " + Arrays.toString(solution));
    }

    public static void ckt4() {
        List<CircuitElement> elementList = new ArrayList<>();
        elementList.add(new VoltageSource(1, 0, 10, 0));
        elementList.add(new Resistor(1, 2, 10));
        elementList.add(new Resistor(2, 0, 10));
        elementList.add(new ShortCircuit(2, 3, 1));
        elementList.add(new Resistor(3, 0, 10));

        int numNodes = 3;
        int numVoltageSources = 1;
        int numShortCircuits = 1;
        int size = numNodes + numVoltageSources + numShortCircuits;

        MatrixSparse manMatrix = new MatrixSparse(size, size);
        double[] rhs = new double[size];

        MnaBuilder visitor = new MnaBuilder();
        int voltageIndex = numNodes;

        for(CircuitElement element : elementList) {
            element.accept(visitor, manMatrix, rhs, 1, voltageIndex++);
        }

        double[] solution = manMatrix.solve(rhs).toDense().getCol(0);
        System.out.println("Node voltages: " + Arrays.toString(solution));
    }

    public static void ckt5() {
        List<CircuitElement> elementList = new ArrayList<>();
        elementList.add(new VoltageSource(1, 0, 10, 0));
        elementList.add(new Resistor(2, 0, 10));

        int numNodes = 2;
        int numVoltageSources = 1;
        int size = numNodes + numVoltageSources;

        MatrixSparse manMatrix = new MatrixSparse(size, size);
        double[] rhs = new double[size];

        MnaBuilder visitor = new MnaBuilder();
        int voltageIndex = numNodes;

        for(CircuitElement element : elementList) {
            element.accept(visitor, manMatrix, rhs, 1, voltageIndex);
        }

        double[] solution = manMatrix.solve(rhs).toDense().getCol(0);
        System.out.println("Node voltages: " + Arrays.toString(solution));
    }
}