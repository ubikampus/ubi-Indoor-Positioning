package fi.helsinki.ubipositioning.utils;

import org.apache.commons.math3.filter.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * Wrapper for Apache commons Kalman filter.
 */
public class Kalman {

    public double calculate(Double[] measurements) {
        // A = [ 1 ]
        RealMatrix matrixA = new Array2DRowRealMatrix(new double[] { 1d });
        // B = null
        RealMatrix matrixB = null;
        // H = [ 1 ]
        RealMatrix matrixH = new Array2DRowRealMatrix(new double[] { 1d });
        // Q = [ 1e-5 ]
        RealMatrix matrixQ = new Array2DRowRealMatrix(new double[] { 0.008 });
        // R = [ 0.1 ]
        RealMatrix matrixR = new Array2DRowRealMatrix(new double[] { 32.9 });

        ProcessModel pm
                = new DefaultProcessModel(matrixA, matrixB, matrixQ, new ArrayRealVector(new double[] { 0 }), null);
        MeasurementModel mm = new DefaultMeasurementModel(matrixH, matrixR);
        KalmanFilter filter = new KalmanFilter(pm, mm);

        RealVector z = new ArrayRealVector(measurements);
        filter.predict();
        filter.correct(z);
        return filter.getStateEstimation()[0];
    }


}
