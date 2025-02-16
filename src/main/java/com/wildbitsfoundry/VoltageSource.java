package com.wildbitsfoundry;

import com.wildbitsfoundry.etk4j.math.linearalgebra.MatrixSparse;

public class VoltageSource extends CircuitElement {

    double voltage;
    int index;

    public VoltageSource(int node1, int node2, double voltage, int index) {
        super(node1, node2);
        this.voltage = voltage;
        this.index = index;
    }

    @Override
    public void accept(MnaMatrixVisitor visitor, MatrixSparse matrixSparse, double[] rhs, double dt, int voltageIndex) {
        visitor.visitVoltageSource(this, matrixSparse, rhs);
    }
}
