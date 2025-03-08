package com.wildbitsfoundry;

import com.wildbitsfoundry.etk4j.math.linearalgebra.MatrixSparse;

public class VCVS extends CircuitElement {

    int node3;
    int node4;
    double gain;
    int index;

    public VCVS(String id, int node1, int node2, int node3, int node4, double gain, int index) {
        super(id, node1, node2);
        this.node3 = node3;
        this.node4 = node4;
        this.gain = gain;
        this.index = index;
    }

    @Override
    public void stamp(MatrixSparse mnaMatrix, double[] solutionVector) {
        int n1 = node1;
        int n2 = node2;
        int n3 = node3;
        int n4 = node4;

        int row = mnaMatrix.getRowCount() - 1 - index;
        if (n1 != 0) {
            mnaMatrix.unsafeSet(row, n1 - 1, 1);
            mnaMatrix.unsafeSet(n1 - 1, row, 1);
        }
        if (n2 != 0) {
            mnaMatrix.unsafeSet(row, n2 - 1, -1);
            mnaMatrix.unsafeSet(n2 - 1, row, -1);
        }
        if(n3 != 0) {
            mnaMatrix.unsafeSet(row, n3 - 1, -gain);
        }
        if(n4 != 0) {
            mnaMatrix.unsafeSet(row, n4 - 1, gain);
        }
        solutionVector[row] = 0;
    }
}
