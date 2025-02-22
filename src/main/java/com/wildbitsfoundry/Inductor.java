package com.wildbitsfoundry;

import com.wildbitsfoundry.etk4j.math.linearalgebra.MatrixSparse;

public class Inductor extends CircuitElement implements ReactiveElement {

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
    public void stamp(MatrixSparse mnaMatrix, double[] solutionVector) {

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
            case TRAPEZOIDAL -> {
                gL = 2 * inductance / dt;
                vEq = gL * previousCurrent1 + previousVoltage;
            }
            case BDF_2ND_ORDER -> {
                gL = 3 * inductance / (2 * dt);
                vEq = 2 * inductance * previousCurrent1 / dt - inductance * previousCurrent2 / (2 * dt);
            }
        }

        int row = mnaMatrix.getRowCount() - 1 - index;
        if(n1 != 0) {
            mnaMatrix.unsafeSet(row, n1 - 1, 1);
            mnaMatrix.unsafeSet(n1 - 1, row, 1);
        }
        if(n2 != 0) {
            mnaMatrix.unsafeSet(row, n2 - 1, -1);
            mnaMatrix.unsafeSet(n2 - 1, row, -1);
        }
        mnaMatrix.unsafeSet(row, row, -gL);
        solutionVector[row] = -vEq;

    }

    @Override
    public void updateMemory(double[] solutionVector, double h, IntegrationMethod integrationMethod) {
        double gL;
        switch (integrationMethod) {
            case BACKWARDS_EULER -> {
                int row = solutionVector.length - 1 - index;
                previousCurrent1 = solutionVector[row];
            }
            case TRAPEZOIDAL -> {
                gL = 2 * inductance / h;
                int row = solutionVector.length - 1 - index;
                previousCurrent1 = solutionVector[row];
                previousVoltage = previousVoltage - previousCurrent1;
            }
            case BDF_2ND_ORDER -> {
                gL = 2 * inductance / (h);
                int row = solutionVector.length - 1 - index;
                previousCurrent2 = previousCurrent1;
                previousCurrent1 = solutionVector[row];
                previousVoltage = gL * previousCurrent1 + inductance / (2 * h) * previousCurrent2;
            }
        }
    }
}
