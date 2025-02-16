package com.wildbitsfoundry;

import com.wildbitsfoundry.etk4j.math.linearalgebra.MatrixSparse;

public class Resistor extends CircuitElement {
    double resistance;

    public Resistor(int node1, int node2, double resistance) {
        super(node1, node2);
        this.resistance = resistance;
    }

    @Override
    public void accept(MnaMatrixVisitor visitor, MatrixSparse matrixSparse, double[] rhs, double dt, int voltageIndex) {
        visitor.visitResistor(this, matrixSparse, rhs);
    }
}
