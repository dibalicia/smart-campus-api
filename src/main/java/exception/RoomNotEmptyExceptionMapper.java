package exception;

import model.ErrorResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Maps RoomNotEmptyException to an HTTP 409 Conflict response.
 *
 * The @Provider annotation tells JAX-RS to automatically register this
 * mapper. Whenever RoomNotEmptyException is thrown anywhere in the application,
 * this mapper intercepts it and converts it into a structured JSON response.
 */
@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {

    @Override
    public Response toResponse(RoomNotEmptyException ex) {
        ErrorResponse error = new ErrorResponse(
                "Conflict",
                ex.getMessage(),
                409
        );

        return Response
                .status(Response.Status.CONFLICT)  // 409
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
