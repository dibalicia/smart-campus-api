package exception;

import model.ErrorResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


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

    
        return Response
                .status(422)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
