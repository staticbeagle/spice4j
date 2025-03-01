package com.wildbitsfoundry;

import com.wildbitsfoundry.etk4j.math.linearalgebra.MatrixSparse;

public class Capacitor extends CircuitElement implements ReactiveElement {

    double capacitance;
    double previousVoltage1;
    double previousVoltage2;
    double previousCurrent;

    public Capacitor(String id, int node1, int node2, double capacitance, double initialVoltage) {
        super(id, node1, node2);
        this.capacitance = capacitance;
        this.previousVoltage1 = initialVoltage;
        this.previousVoltage2 = initialVoltage;
    }

    @Override
    public void stamp(MatrixSparse mnaMatrix, double[] solutionVector) {

    }

    @Override
    public void stamp(MatrixSparse mnaMatrix, double[] solutionVector, double h, IntegrationMethod integrationMethod) {
        int n1 = node1;
        int n2 = node2;
        double gC = 0;
        double iEq = 0;
        switch (integrationMethod) {
            case BACKWARDS_EULER -> {
                gC = capacitance / h;
                iEq = gC * previousVoltage1;
            }
            case TRAPEZOIDAL -> {
                gC = 2 * capacitance / h;
                iEq = gC * previousVoltage1 + previousCurrent;
            }
            case GEAR_2ND_ORDER -> {
                gC = 3 * capacitance / (2 * h);
                iEq = 2 * capacitance * previousVoltage1 / h - capacitance * previousVoltage2 / (2 * h);
            }
        }

        if (n1 != 0) {
            mnaMatrix.unsafeSet(n1 - 1, n1 - 1, mnaMatrix.unsafeGet(n1 - 1, n1 - 1) + gC);
            solutionVector[n1 - 1] += iEq;
        }
        if (n2 != 0) {
            mnaMatrix.unsafeSet(n2 - 1, n2 - 1, mnaMatrix.unsafeGet(n2 - 1, n2 - 1) + gC);
            solutionVector[n2 - 1] = iEq;
        }
        if (n1 != 0 && n2 != 0) {
            mnaMatrix.unsafeSet(n1 - 1, n2 - 1, mnaMatrix.unsafeGet(n1 - 1, n2 - 1) - gC);
            mnaMatrix.unsafeSet(n2 - 1, n1 - 1, mnaMatrix.unsafeGet(n2 - 1, n1 - 1) - gC);
            solutionVector[n1 - 1] = iEq;
            solutionVector[n2 - 1] = -iEq;
        }

    }

    @Override
    public void updateMemory(double[] solutionVector, double h, IntegrationMethod integrationMethod) {
        int n1 = node1;
        int n2 = node2;
        double gC = 0;
        switch (integrationMethod) {
            case BACKWARDS_EULER -> gC = capacitance / h;
            case TRAPEZOIDAL -> gC = 2 * capacitance / h;
            case GEAR_2ND_ORDER -> gC = 3 * capacitance / (2 * h);
        }

        double voltageAcrossTheCapacitor;
        if (n1 != 0 && n2 != 0) {
            voltageAcrossTheCapacitor = solutionVector[n1 - 1] - solutionVector[n2 - 1];
        } else if (n1 != 0) {
            voltageAcrossTheCapacitor = solutionVector[n1 - 1];
        } else {
            voltageAcrossTheCapacitor = solutionVector[n2 - 1];
        }

        previousVoltage2 = previousVoltage1;
        previousVoltage1 = voltageAcrossTheCapacitor;
        previousCurrent = gC * (previousVoltage1 - previousVoltage2) - previousCurrent; // only used in trapezoidal
    }
}
