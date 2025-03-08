package com.wildbitsfoundry;

import com.wildbitsfoundry.etk4j.math.linearalgebra.MatrixSparse;
import com.wildbitsfoundry.etk4j.util.DoubleArrays;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class TestCKT {

    @Test
    public void testVoltageDivider() {
        List<CircuitElement> circuitElementList = new ArrayList<>();
        circuitElementList.add(new VoltageSource("V1", 1, 0, 10, 0));
        circuitElementList.add(new Resistor("R1", 1, 2, 1));
        circuitElementList.add(new Resistor("R2", 2, 0, 1));

        int numNodes = 2;
        int numVoltageSources = 1;
        int size = numNodes + numVoltageSources;

        MatrixSparse mnaMatrix = new MatrixSparse(size, size);
        double[] rhs = new double[size];

        for(CircuitElement element : circuitElementList) {
            element.stamp(mnaMatrix, rhs);
        }

        double[] actual = mnaMatrix.solve(rhs).getArrayDense();
        double[] expected = {10, 5, -5};

        assertArrayEquals(expected, actual, 1e-12);
    }

    @Test
    public void testCurrentDivider() {
        List<CircuitElement> circuitElementList = new ArrayList<>();
        circuitElementList.add(new CurrentSource("I1", 0, 1, 1));
        circuitElementList.add(new Resistor("R1", 1, 0, 1));
        circuitElementList.add(new Resistor("R2", 1, 0, 1));

        int numNodes = 1;
        int numVoltageSources = 0;
        int size = numNodes + numVoltageSources;

        MatrixSparse mnaMatrix = new MatrixSparse(size, size);
        double[] rhs = new double[size];

        for(CircuitElement element : circuitElementList) {
            element.stamp(mnaMatrix, rhs);
        }

        double[] actual = mnaMatrix.solve(rhs).getArrayDense();
        double[] expected = {0.5};

        assertArrayEquals(expected, actual, 1e-12);
    }

    @Test
    public void testRCCircuit() {
        /*
                List<CircuitElement> elementList = new ArrayList<>();
        elementList.add(new CurrentSource("I1", 0, 1, 1));
        elementList.add(new Resistor("R1", 1, 0, 1));
        elementList.add(new Resistor("R2", 1, 2, 2));
        elementList.add(new Inductor("L", 2, 3, 3, 0, 0));
        elementList.add(new Resistor("R3", 3, 0, 4));
        elementList.add(new Capacitor("C", 3, 0, 5, 0));
         */
        List<CircuitElement> elementList = new ArrayList<>();
        elementList.add(new VoltageSource("V1", 1, 0, 10, 0));
        elementList.add(new Resistor("R1", 1, 2, 1000));
        elementList.add(new Capacitor("C1", 2, 0, 1e-6, 0));

        int numNodes = 2;
        int numVoltageSources = 1;
        int size = numNodes + numVoltageSources;
        double dt = 1e-6;
        double[] time = {0.0001};//DoubleArrays.linSpace(0, 10e-3, 10);
        IntegrationMethod integrationMethod = IntegrationMethod.GEAR_2ND_ORDER;
        List<Double> capVoltage = new ArrayList<>();
        List<Double> times = new ArrayList<>();

        double tstart = 0;
        double tend = 10e-3;
        double h = 1e-4;
        int no_of_sols = (int) Math.floor((tend - tstart) / h);
        for(int i = 1; i <= no_of_sols; i++) {
            double t = tstart + i * h;
            MatrixSparse mnaMatrix = new MatrixSparse(size, size);
            double[] rhs = new double[size];


            for(CircuitElement element : elementList) {
                if(element instanceof ReactiveElement) {
                    ((ReactiveElement) element).stamp(mnaMatrix, rhs, h, integrationMethod);
                } else {
                    element.stamp(mnaMatrix, rhs);
                }
            }

            double[] solution = mnaMatrix.solve(rhs).getArrayDense();
            capVoltage.add(solution[1]);
            times.add(t);

            for(CircuitElement element : elementList) {
                if(element instanceof ReactiveElement) {
                    ((ReactiveElement) element).updateMemory(solution, h, integrationMethod);
                }
            }
        }
//        for(int i = 0; i < time.length; i++) {
//
//        }
        System.out.println(times);
        System.out.println(capVoltage);
    }
}
