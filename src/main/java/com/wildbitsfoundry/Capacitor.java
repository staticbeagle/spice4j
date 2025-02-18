package com.wildbitsfoundry;

import com.wildbitsfoundry.etk4j.math.linearalgebra.MatrixSparse;

public class Capacitor extends CircuitElement {

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
    public void stamp(MatrixSparse mnaMatrix, double[] solutionVector, double dt) {
        int n1 = node1;
        int n2 = node2;
        //double gC  = 2 * capacitance / dt;
        double gC = capacitance / dt;//method == 0 ? (3 * C) / (2 * dt) : C / dt;

        if(n1 != 0) {
            mnaMatrix.unsafeSet(n1 - 1, n1 -1, mnaMatrix.unsafeGet(n1 - 1, n1 - 1) + gC);
        }
        if(n2 != 0) {
            mnaMatrix.unsafeSet(n2 - 1, n2 -1, mnaMatrix.unsafeGet(n2 - 1, n2 - 1) + gC);
        }
        if(n1 != 0 && n2 != 0) {
            mnaMatrix.unsafeSet(n1 - 1, n2 -1, mnaMatrix.unsafeGet(n1 - 1, n2 - 1) - gC);
            mnaMatrix.unsafeSet(n2 - 1, n1 -1, mnaMatrix.unsafeGet(n2 - 1, n1 - 1) - gC);
        }

        System.out.println(mnaMatrix.toDense());

        double iEq = gC * previousVoltage1; // + previousCurrent;
        //double iEq = gC * previousVoltage1;// - previousCurrent;// + capacitor.previousVoltage1;// method == 0 ? (2 * C / dt * capacitor.previousVoltage1) - (C / (2 * dt) * capacitor.previousVoltage2) : gC * capacitor.previousVoltage1;
        //previousVoltage2 = previousVoltage1;
        //previousCurrent = iEq - previousCurrent;

        if(n1 != 0) {
            solutionVector[n1 - 1] = iEq;
            previousVoltage1 = iEq / gC;
        }
        if(n2 != 0) {
            solutionVector[n2 - 1] -= iEq;
        }
        if(n1 != 0 && n2 != 0) {
            previousVoltage1 = (solutionVector[n1 - 1] - solutionVector[n2 - 1]) / gC;
        }
    }

    @Override
    public void updateMemory(double[] solutionVector) {
        int n1 = node1;
        int n2 = node2;

        if(n1 != 0) {
            previousVoltage1 = solutionVector[n1 - 1];
        }
        if(n2 != 0) {
            previousVoltage1 = solutionVector[n2 - 1];
        }
        if(n1 != 0 && n2 != 0) {
            previousVoltage1 = solutionVector[n1 - 1] - solutionVector[n2 - 1];
        }

    }
}
