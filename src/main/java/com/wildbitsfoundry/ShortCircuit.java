package com.wildbitsfoundry;

import com.wildbitsfoundry.etk4j.math.linearalgebra.MatrixSparse;

public class ShortCircuit extends CircuitElement {

    final int index;
    public ShortCircuit(int node1, int node2, int index) {
        super(node1, node2);
        this.index = index;
    }

    @Override
    public void accept(MnaMatrixVisitor visitor, MatrixSparse matrixSparse, double[] rhs, double dt, int extraIndex) {
        visitor.visitShortCircuit(this, matrixSparse, rhs);
    }
}
