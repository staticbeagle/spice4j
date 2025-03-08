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
    public void stamp(MatrixSparse mnaMatrix, double[] solutionVector, double h, IntegrationMethod integrationMethod) {
        int n1 = node1;
        int n2 = node2;
        double zL = 0;
        double vEq = 0;
        switch (integrationMethod) {
            case BACKWARD_EULER -> {
                zL = inductance / h;
                vEq = zL * previousCurrent1;
            }
            case TRAPEZOIDAL -> {
                zL =  2 * inductance / h;
                vEq = 2 * inductance / h * previousCurrent1 + previousVoltage;
            }
            case GEAR_2ND_ORDER -> {
                zL = 3 * inductance / (2 * h);
                vEq = 2 * inductance * previousCurrent1 / h - inductance * previousCurrent2 / (2 * h);
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
        mnaMatrix.unsafeSet(row, row, -zL);
        solutionVector[row] -= vEq;
    }

    @Override
    public void updateMemory(double[] solutionVector, double h, IntegrationMethod integrationMethod) {
        switch (integrationMethod) {
            case BACKWARD_EULER -> {
                int row = solutionVector.length - 1 - index;
                previousCurrent1 = solutionVector[row];
            }
            case TRAPEZOIDAL -> {
                int n1 = node1;
                int n2 = node2;
                double voltageDifference = 0;
                if(n1 != 0 && n2 != 0) {
                    voltageDifference = solutionVector[n1 - 1] - solutionVector[n2 - 1];
                } else if(n1 != 0) {
                    voltageDifference = solutionVector[n1 - 1];
                } else {
                    voltageDifference = -solutionVector[n2 - 1];
                }
                int row = solutionVector.length - 1 - index;
                previousCurrent2 = previousCurrent1;
                previousCurrent1 = solutionVector[row];
                double currentDifference = previousCurrent1 - previousCurrent2;
                previousVoltage = 2 * inductance / h * currentDifference - voltageDifference;
            }
            case GEAR_2ND_ORDER -> {
                int row = solutionVector.length - 1 - index;
                previousCurrent2 = previousCurrent1;
                previousCurrent1 = solutionVector[row];
                previousVoltage = 2 * inductance / h * previousCurrent1 + inductance / (2 * h) * previousCurrent2;
            }
        }
    }
}
