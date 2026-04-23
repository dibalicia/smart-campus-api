package resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Discovery endpoint at the root of the API.
 *
 * Returns metadata about the API and links to the main resource collections.
 * This is a basic implementation of the HATEOAS principle - clients can
 * start here and discover where to go next without reading external docs.
 */
@Path("/")
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getApiInfo() {

        // Build the response as a map so Jackson serialises it to JSON
        Map<String, Object> response = new HashMap<>();
        response.put("name",        "Smart Campus Sensor & Room Management API");
        response.put("version",     "1.0");
        response.put("description", "A RESTful API for managing campus rooms and IoT sensors.");
        response.put("contact",     "admin@smartcampus.westminster.ac.uk");

        // Links to the primary resource collections (HATEOAS-style navigation)
        Map<String, String> resources = new HashMap<>();
        resources.put("rooms",   "/api/v1/rooms");
        resources.put("sensors", "/api/v1/sensors");
        response.put("resources", resources);

        return Response.ok(response).build();
    }
}
