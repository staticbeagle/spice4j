package com.wildbitsfoundry;

import com.wildbitsfoundry.etk4j.math.linearalgebra.MatrixSparse;

public abstract class CircuitElement {
    int node1;
    int node2;

    public CircuitElement(int node1, int node2) {
        this.node1 = node1;
        this.node2 = node2;
    }

    public abstract void accept(MnaMatrixVisitor visitor, MatrixSparse matrixSparse, double[] rhs, double dt, int voltageIndex);
}
