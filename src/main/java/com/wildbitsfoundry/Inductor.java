package com.wildbitsfoundry;

import com.wildbitsfoundry.etk4j.math.linearalgebra.MatrixSparse;

public class Inductor extends CircuitElement {

    double inductance;
    double previousCurrent1;
    double previousCurrent2;
    double previousVoltage;
    int index;

    public Inductor(String id, int node1, int node2, double inductance, double initialCurrent, int index) {
        super(id, node1, node2);
        this.inductance = inductance;
        this.previousCurrent1 = initialCurrent;
        this.previousCurrent2 = initialCurrent;
        this.index = index;
    }

    @Override
    public void stamp(MatrixSparse mnaMatrix, double[] solutionVector, double dt, IntegrationMethod integrationMethod) {
        int n1 = node1;
        int n2 = node2;
        double gL = 0;
        double vEq = 0;
        switch (integrationMethod) {
            case BACKWARDS_EULER -> {
                gL = inductance / dt;
                vEq = gL * previousCurrent1;
            }
//            case TRAPEZOIDAL -> {
//                gC = 2 * capacitance / dt;
//                iEq = gC * previousVoltage1 + previousCurrent;
//            }
//            case GEAR_2 -> {
//                gC = 3 * capacitance / (2 * dt);
//                iEq = 2 * capacitance * previousVoltage1 / dt - capacitance * previousVoltage2 / (2 * dt);
//            }
        }

        int row = mnaMatrix.getRowCount() - 1 - index;
        if(n1 != 0) {
            mnaMatrix.unsafeSet(row - 1, row, 1);
            mnaMatrix.unsafeSet(row, row - 1, -1);
        }
        if(n2 != 0) {
            mnaMatrix.unsafeSet(row - 1, row, -1);
            mnaMatrix.unsafeSet(row, row - 1, -1);
        }
        mnaMatrix.unsafeSet(row, row, -gL);
        solutionVector[row] -= vEq;

    }

    @Override
    public void updateMemory(double[] solutionVector, double dt, IntegrationMethod integrationMethod) {
        int n1 = node1;
        int n2 = node2;
        double gC = 0;
        switch (integrationMethod) {
            case BACKWARDS_EULER -> gC = inductance / dt;
//            case TRAPEZOIDAL -> gC = 2 * capacitance / dt;
//            case GEAR_2 -> gC = 3 * capacitance / (2 * dt);
        }

        int row = solutionVector.length - 1 - index;
        previousCurrent2 = previousCurrent1;
        previousCurrent1 = solutionVector[row];
        // previousVoltage = ?
    }
}
