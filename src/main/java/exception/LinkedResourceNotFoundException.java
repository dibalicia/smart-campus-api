package exception;

/**
 * Thrown when a resource references another resource (e.g., a roomId inside a
 * sensor POST body) that does not exist in the system.
 * Mapped to HTTP 422 Unprocessable Entity by LinkedResourceNotFoundExceptionMapper.
 */
public class LinkedResourceNotFoundException extends RuntimeException {

    public LinkedResourceNotFoundException(String message) {
        super(message);
    }
}
