package com.wildbitsfoundry;

import com.wildbitsfoundry.etk4j.math.linearalgebra.MatrixSparse;

public interface ReactiveElement {
    void updateMemory(double[] solutionVector, double h, IntegrationMethod integrationMethod);
    void stamp(MatrixSparse mnaMatrix, double[] solutionVector, double h, IntegrationMethod integrationMethod);
}
