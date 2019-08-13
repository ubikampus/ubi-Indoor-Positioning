package fi.helsinki.ubipositioning.utils;

/**
 * Custom exception class for situations where something can't be recognized.
 */
public class UnknownDeviceException extends RuntimeException {
    public UnknownDeviceException(String message) {
        super(message);
    }
}
