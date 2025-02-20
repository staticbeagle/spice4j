package com.wildbitsfoundry;

import com.wildbitsfoundry.etk4j.math.linearalgebra.MatrixSparse;

public class VoltageSource extends CircuitElement {

    double voltage;
    int index;

    public VoltageSource(String id, int node1, int node2, double voltage, int index) {
        super(id, node1, node2);
        this.voltage = voltage;
        this.index = index;
    }

    @Override
    public void stamp(MatrixSparse mnaMatrix, double[] solutionVector, double h, IntegrationMethod integrationMethod) {
        int n1 = node1;
        int n2 = node2;

        int row = mnaMatrix.getRowCount() - 1 - index;
        if(n1 != 0) {
            mnaMatrix.unsafeSet(row - 2, row, 1);
            mnaMatrix.unsafeSet(row, row - 2, 1);
        }
        if(n2 != 0) {
            mnaMatrix.unsafeSet(row - 1, row, -1);
            mnaMatrix.unsafeSet(row, row - 1, -1);
        }
        mnaMatrix.unsafeSet(row, row, 0);
        solutionVector[row] = voltage;
    }
}
