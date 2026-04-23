package resource;

import exception.LinkedResourceNotFoundException;
import model.ErrorResponse;
import model.Sensor;
import store.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/sensors")
public class SensorResource {

    // GET /api/v1/sensors
    // Optional query parameter ?type=CO2 filters results by sensor type.
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSensors(@QueryParam("type") String type) {
        List<Sensor> sensorList = new ArrayList<>(DataStore.getSensors().values());

        if (type != null && !type.trim().isEmpty()) {
            List<Sensor> filtered = new ArrayList<>();
            for (Sensor s : sensorList) {
                if (s.getType().equalsIgnoreCase(type)) {
                    filtered.add(s);
                }
            }
            return Response.ok(filtered).build();
        }

        return Response.ok(sensorList).build();
    }

    // GET /api/v1/sensors/{sensorId}
    // Returns a single sensor by ID
    @GET
    @Path("/{sensorId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSensorById(@PathParam("sensorId") String sensorId) {
        Sensor sensor = DataStore.getSensors().get(sensorId);
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Not Found",
                            "Sensor with ID '" + sensorId + "' does not exist.", 404))
                    .build();
        }
        return Response.ok(sensor).build();
    }

    // PUT /api/v1/sensors/{sensorId}
    // Updates an existing sensor (useful for changing status)
    @PUT
    @Path("/{sensorId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSensor(@PathParam("sensorId") String sensorId, Sensor updated) {
        Sensor existing = DataStore.getSensors().get(sensorId);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Not Found",
                            "Sensor with ID '" + sensorId + "' does not exist.", 404))
                    .build();
        }
        if (updated.getStatus() != null && !updated.getStatus().trim().isEmpty()) {
            existing.setStatus(updated.getStatus());
        }
        if (updated.getType() != null && !updated.getType().trim().isEmpty()) {
            existing.setType(updated.getType());
        }
        if (updated.getCurrentValue() != 0) {
            existing.setCurrentValue(updated.getCurrentValue());
        }
        return Response.ok(existing).build();
    }

    // POST /api/v1/sensors
    // Registers a new sensor. The roomId must reference an existing room.
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSensor(Sensor sensor) {

        if (sensor.getId() == null || sensor.getId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Bad Request", "Sensor 'id' field is required.", 400))
                    .build();
        }
        if (sensor.getType() == null || sensor.getType().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Bad Request", "Sensor 'type' field is required.", 400))
                    .build();
        }

        if (sensor.getRoomId() == null || sensor.getRoomId().trim().isEmpty()) {
            throw new LinkedResourceNotFoundException(
                    "The 'roomId' field is required when registering a sensor.");
        }
        if (!DataStore.getRooms().containsKey(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException(
                    "The roomId '" + sensor.getRoomId()
                    + "' does not reference an existing room. Please create the room first.");
        }

        if (DataStore.getSensors().containsKey(sensor.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorResponse("Conflict",
                            "A sensor with ID '" + sensor.getId() + "' already exists.", 409))
                    .build();
        }

        if (sensor.getStatus() == null || sensor.getStatus().trim().isEmpty()) {
            sensor.setStatus("ACTIVE");
        }

        DataStore.getSensors().put(sensor.getId(), sensor);
        DataStore.getRooms().get(sensor.getRoomId()).getSensorIds().add(sensor.getId());
        DataStore.getReadings().put(sensor.getId(), new ArrayList<>());

        return Response.status(Response.Status.CREATED).entity(sensor).build();
    }

    // Sub-resource locator for sensor readings.
    // No HTTP method annotation - intentional for sub-resource locator pattern.
    @Path("{sensorId}/readings")
    public SensorReadingResource getReadingsResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }
}