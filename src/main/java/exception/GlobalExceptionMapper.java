package exception;

import model.ErrorResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;


@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable ex) {
        
        LOGGER.severe("Unhandled exception: " + ex.getClass().getName()
                + " - " + ex.getMessage());

        ErrorResponse error = new ErrorResponse(
                "Internal Server Error",
                "An unexpected error occurred on the server. "
                + "Please contact the administrator if this persists.",
                500
        );

        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)  
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
