package com.wildbitsfoundry;

import com.wildbitsfoundry.etk4j.math.linearalgebra.MatrixSparse;

public class VCCS extends CircuitElement {

    int node3;
    int node4;
    double transconductance;

    public VCCS(String id, int node1, int node2, int node3, int node4, double transconductance) {
        super(id, node1, node2);
        this.node3 = node3;
        this.node4 = node4;
        this.transconductance = transconductance;
    }

    @Override
    public void stamp(MatrixSparse mnaMatrix, double[] solutionVector) {
        int n1 = node1;
        int n2 = node2;
        int n3 = node3;
        int n4 = node4;

        if (n1 != 0 && n3 != 0) {
            mnaMatrix.unsafeSet(n1 - 1, n3 - 1, transconductance);
        }
        if (n1 != 0 && n4 != 0) {
            mnaMatrix.unsafeSet(n1 - 1, n4 - 1, -transconductance);
        }
        if (n2 != 0 && n3 != 0) {
            mnaMatrix.unsafeSet(n2 - 1, n3 - 1, -transconductance);
        }
        if (n2 != 0 && n4 != 0) {
            mnaMatrix.unsafeSet(n2 - 1, n4 - 1, transconductance);
        }
    }
}
