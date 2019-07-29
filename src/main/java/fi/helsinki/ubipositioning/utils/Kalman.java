package fi.helsinki.ubipositioning.utils;

import org.apache.commons.math3.filter.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class Kalman {

    public double Calculate(Double[] measurements) {
        // A = [ 1 ]
        RealMatrix A = new Array2DRowRealMatrix(new double[] { 1d });
        // B = null
        RealMatrix B = null;
        // H = [ 1 ]
        RealMatrix H = new Array2DRowRealMatrix(new double[] { 1d });
        // Q = [ 1e-5 ]
        RealMatrix Q = new Array2DRowRealMatrix(new double[] { 0.008 });
        // P = [ 1 ]
        RealMatrix P0 = new Array2DRowRealMatrix(new double[] { 1d });
        // R = [ 0.1 ]
        RealMatrix R = new Array2DRowRealMatrix(new double[] { 32.9 });

        ProcessModel pm
                = new DefaultProcessModel(A, B, Q, new ArrayRealVector(new double[] { 0 }), null);
        MeasurementModel mm = new DefaultMeasurementModel(H, R);
        KalmanFilter filter = new KalmanFilter(pm, mm);

        RealVector z = new ArrayRealVector(measurements);
        filter.predict();
        filter.correct(z);
        return filter.getStateEstimation()[0];
    }


}
