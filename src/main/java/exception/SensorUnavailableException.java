package exception;

/**
 * Thrown when a POST reading is attempted on a sensor that is in
 * MAINTENANCE or OFFLINE status and therefore cannot accept data.
 * Mapped to HTTP 403 Forbidden by SensorUnavailableExceptionMapper.
 */
public class SensorUnavailableException extends RuntimeException {

    public SensorUnavailableException(String message) {
        super(message);
    }
}
