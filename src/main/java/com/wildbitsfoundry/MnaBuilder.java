package com.wildbitsfoundry;

import com.wildbitsfoundry.etk4j.math.linearalgebra.MatrixSparse;

public class MnaBuilder implements MnaMatrixVisitor {

    @Override
    public void visitResistor(Resistor resistor, MatrixSparse matrix, double[] rhs) {
        int n1 = resistor.node1;
        int n2 = resistor.node2;
        double G = 1.0 / resistor.resistance;

        if(n1 != 0) {
            matrix.unsafeSet(n1 - 1, n1 - 1, matrix.unsafeGet(n1 - 1, n1 - 1) + G);
        }
        if(n2 != 0) {
            matrix.unsafeSet(n2 - 1, n2 - 1, matrix.unsafeGet(n2 - 1, n2 - 1) + G);
        }
        if(n1 != 0 && n2 != 0) {
            matrix.unsafeSet(n1 - 1, n2 - 1, matrix.unsafeGet(n1 - 1, n2 - 1) - G);
            matrix.unsafeSet(n2 - 1, n1 - 1, matrix.unsafeGet(n2 - 1, n1 - 1) - G);
        }
    }

    @Override
    public void visitVoltageSource(VoltageSource voltageSource, MatrixSparse matrixSparse, double[] rhs) {
        int n1 = voltageSource.node1;
        int n2 = voltageSource.node2;
        int index = voltageSource.index;
        double voltage = voltageSource.voltage;

        int row = matrixSparse.getRowCount() - 1 - index;
        if(n1 != 0) {
            matrixSparse.unsafeSet(row, n1 - 1, 1);
            matrixSparse.unsafeSet(n1 - 1, row, 1);
        }
        if(n2 != 0) {
            matrixSparse.unsafeSet(row, n2 - 1, -1);
            matrixSparse.unsafeSet(n2 - 1, row, -1);
        }
        rhs[row] = voltage;
    }

    @Override
    public void visitShortCircuit(ShortCircuit shortCircuit, MatrixSparse matrixSparse, double[] rhs) {
        int n1 = shortCircuit.node1;
        int n2 = shortCircuit.node2;
        int extraIndex = shortCircuit.index;

        int row = matrixSparse.getRowCount() - 1 - extraIndex;
        if(n1 != 0) {
            matrixSparse.unsafeSet(row, n1 - 1, 1);
            matrixSparse.unsafeSet(n1 - 1, row, 1);
        }
        if(n2 != 0) {
            matrixSparse.unsafeSet(row, n2 - 1, -1);
            matrixSparse.unsafeSet(n2 - 1, row, -1);
        }
        rhs[row] = 0;
    }
}
