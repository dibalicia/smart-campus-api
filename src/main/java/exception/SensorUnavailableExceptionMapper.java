package exception;

import model.ErrorResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Maps SensorUnavailableException to an HTTP 403 Forbidden response.
 *
 * 403 Forbidden is appropriate here because the sensor exists, the request
 * is valid, but the current state of the sensor (MAINTENANCE/OFFLINE) means
 * the operation is not permitted.
 */
@Provider
public class SensorUnavailableExceptionMapper
        implements ExceptionMapper<SensorUnavailableException> {

    @Override
    public Response toResponse(SensorUnavailableException ex) {
        ErrorResponse error = new ErrorResponse(
                "Forbidden",
                ex.getMessage(),
                403
        );

        return Response
                .status(Response.Status.FORBIDDEN)  // 403
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
