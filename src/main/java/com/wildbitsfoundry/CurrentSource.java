package com.wildbitsfoundry;

import com.wildbitsfoundry.etk4j.math.linearalgebra.MatrixSparse;

public class CurrentSource extends CircuitElement {

    double current;

    public CurrentSource(String id, int node1, int node2, int current) {
        super(id, node1, node2);
        this.current = current;
    }

    @Override
    public void stamp(MatrixSparse mnaMatrix, double[] solutionVector, double dt, IntegrationMethod integrationMethod) {
        int n1 = node1;
        int n2 = node2;

        if(n1 != 0) {
            solutionVector[n1 - 1] -= current;
        }
        if(n2 != 0) {
            solutionVector[n2 - 1] += current;
        }
    }
}
