package fi.helsinki.ubipositioning.utils;

import org.apache.commons.math3.filter.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * Class to smooth out some of the inaccuracy in the gotten data using Apache commons Kalman filter.
 *
 * @see KalmanFilter
 */
public class MeasurementFilter implements IMeasurementFilter {
    // A = [ 1 ]
    private static final RealMatrix matrixA = new Array2DRowRealMatrix(new double[] { 1d });
    // B = null
    private static final RealMatrix matrixB = null;
    // H = [ 1 ]
    private static final RealMatrix matrixH = new Array2DRowRealMatrix(new double[] { 1d });
    // Q = [ 1e-5 ]
    private static final RealMatrix matrixQ = new Array2DRowRealMatrix(new double[] { 0.008 });
    // R = [ 0.1 ]
    private static final RealMatrix matrixR = new Array2DRowRealMatrix(new double[] { 32.9 });

    @Override
    public double smooth(Double[] measurements) {
        ProcessModel pm = new DefaultProcessModel(matrixA, matrixB, matrixQ, new ArrayRealVector(new double[] { 0 }), null);
        MeasurementModel mm = new DefaultMeasurementModel(matrixH, matrixR);
        KalmanFilter filter = new org.apache.commons.math3.filter.KalmanFilter(pm, mm);

        RealVector z = new ArrayRealVector(measurements);
        filter.predict();
        filter.correct(z);
        return filter.getStateEstimation()[0];
    }
}
