package com.wildbitsfoundry;

import com.wildbitsfoundry.etk4j.math.linearalgebra.MatrixSparse;

public interface MnaMatrixVisitor {
    void visitResistor(Resistor resistor, MatrixSparse matrix, double[] rhs);
    void visitVoltageSource(VoltageSource voltageSource, MatrixSparse matrix, double[] rhs);
    void visitShortCircuit(ShortCircuit shortCircuit, MatrixSparse matrixSparse, double[] rhs);
}
