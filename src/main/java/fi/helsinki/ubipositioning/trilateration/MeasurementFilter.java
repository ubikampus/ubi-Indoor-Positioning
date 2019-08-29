package fi.helsinki.ubipositioning.trilateration;

import org.apache.commons.math3.filter.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * Class to smooth out some of the inaccuracy in the gotten data using Apache commons Kalman filter.
 *
 * @see KalmanFilter
 */
class MeasurementFilter implements IMeasurementFilter {
    // A = [ 1 ]
    private RealMatrix matrixA = new Array2DRowRealMatrix(new double[] { 1d });
    // B = null
    private RealMatrix matrixB = null;
    // H = [ 1 ]
    private RealMatrix matrixH = new Array2DRowRealMatrix(new double[] { 1d });
    // Q = [ 1e-5 ]
    private RealMatrix matrixQ = new Array2DRowRealMatrix(new double[] { 0.008 });
    // R = [ 0.1 ]
    private RealMatrix matrixR = new Array2DRowRealMatrix(new double[] { 32.9 });

    // static filter
    private KalmanFilter filter;

    /**
     * Smooth out the inaccuracy using Kalman filter.
     *
     * @param measurements values that need to be smoothed.
     *
     * @return Estimation of what real value should be according to the measurements.
     */
    @Override
    public double smooth(Double[] measurements) {
        if (filter == null) {
            ProcessModel pm = new DefaultProcessModel(matrixA, matrixB, matrixQ, new ArrayRealVector(new double[] { 0 }), null);
            MeasurementModel mm = new DefaultMeasurementModel(matrixH, matrixR);
            filter = new KalmanFilter(pm, mm);
        }

        // Ensures that Kalman filter can get used to to the new data as it is progressive analysis.
        int rounds = 2000 / measurements.length + 1;

        for (int i = 0; i < rounds; i++) {
            for (Double measurement : measurements) {
                filter.predict();
                filter.correct(new double[]{measurement});
            }
        }

        return filter.getStateEstimation()[0];
    }
}
