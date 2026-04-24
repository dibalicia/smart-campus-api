package exception;

import model.ErrorResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


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
