package filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Request and response logging filter.
 *
 * Implements both ContainerRequestFilter and ContainerResponseFilter so it
 * can log details about every incoming request and every outgoing response
 * in a single class.
 *
 * The @Provider annotation means JAX-RS automatically picks this up and
 * runs it for every request without any changes to resource methods.
 */
@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger LOGGER = Logger.getLogger(LoggingFilter.class.getName());

    /**
     * Runs BEFORE the request reaches a resource method.
     * Logs the HTTP method and full request URI.
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        LOGGER.info(">>> Incoming Request");
        LOGGER.info("    Method : " + requestContext.getMethod());
        LOGGER.info("    URI    : " + requestContext.getUriInfo().getAbsolutePath());
    }

    /**
     * Runs AFTER the resource method has produced a response.
     * Logs the HTTP status code that was sent back to the client.
     */
    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {
        LOGGER.info("<<< Outgoing Response");
        LOGGER.info("    Status : " + responseContext.getStatus());
    }
}
