package com.wildbitsfoundry;

import com.wildbitsfoundry.etk4j.math.linearalgebra.MatrixSparse;

public abstract class CircuitElement {
    int node1;
    int node2;

    String id;

    public CircuitElement(String id, int node1, int node2) {
        this.node1 = node1;
        this.node2 = node2;
    }

    public abstract void stamp(MatrixSparse mnaMatrix, double[] solutionVector, double dt, IntegrationMethod integrationMethod);

    public void stamp(MatrixSparse mnaMatrix, double[] solutionVector) {
        stamp(mnaMatrix, solutionVector, 0, null);
    }

    public void updateMemory(double[] solutionVector, double dt, IntegrationMethod integrationMethod) {}
}
