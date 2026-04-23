package exception;

import model.ErrorResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;

/**
 * Global "catch-all" exception mapper.
 *
 * Catches any Throwable that was not handled by a more specific mapper.
 * This ensures the API never returns a raw Java stack trace or a default
 * Tomcat error page to external clients - a key security requirement.
 *
 * The actual exception details are logged server-side for debugging,
 * but only a generic message is sent in the response.
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable ex) {
        // Log the real error on the server so developers can investigate
        LOGGER.severe("Unhandled exception: " + ex.getClass().getName()
                + " - " + ex.getMessage());

        // Return a generic, safe message to the client
        // Never include internal class names or stack traces in the response
        ErrorResponse error = new ErrorResponse(
                "Internal Server Error",
                "An unexpected error occurred on the server. "
                + "Please contact the administrator if this persists.",
                500
        );

        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)  // 500
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
