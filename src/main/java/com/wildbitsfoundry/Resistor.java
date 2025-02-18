package com.wildbitsfoundry;

import com.wildbitsfoundry.etk4j.math.linearalgebra.MatrixSparse;

public class Resistor extends CircuitElement {
    double resistance;

    public Resistor(String id, int node1, int node2, double resistance) {
        super(id, node1, node2);
        this.resistance = resistance;
    }

    @Override
    public void stamp(MatrixSparse mnaMatrix, double[] solutionVector, double dt) {
        int n1 = node1;
        int n2 = node2;
        double G = 1.0 / resistance;

        if(n1 != 0) {
            mnaMatrix.unsafeSet(n1 - 1, n1 - 1, mnaMatrix.unsafeGet(n1 - 1, n1 - 1) + G);
        }
        if(n2 != 0) {
            mnaMatrix.unsafeSet(n2 - 1, n2 - 1, mnaMatrix.unsafeGet(n2 - 1, n2 - 1) + G);
        }
        if(n1 != 0 && n2 != 0) {
            mnaMatrix.unsafeSet(n1 - 1, n2 - 1, mnaMatrix.unsafeGet(n1 - 1, n2 - 1) - G);
            mnaMatrix.unsafeSet(n2 - 1, n1 - 1, mnaMatrix.unsafeGet(n2 - 1, n1 - 1) - G);
        }
    }
}
