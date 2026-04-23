package exception;

import model.ErrorResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Maps LinkedResourceNotFoundException to an HTTP 422 Unprocessable Entity response.
 *
 * 422 is used rather than 404 because the endpoint itself is valid - the problem
 * is that the referenced resource ID inside the request body does not exist.
 * The request was understood and well-formed JSON, but semantically invalid.
 */
@Provider
public class LinkedResourceNotFoundExceptionMapper
        implements ExceptionMapper<LinkedResourceNotFoundException> {

    @Override
    public Response toResponse(LinkedResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                "Unprocessable Entity",
                ex.getMessage(),
                422
        );

        // Response.Status doesn't have a constant for 422 in JAX-RS 2.x,
        // so we pass the integer directly
        return Response
                .status(422)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
