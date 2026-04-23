package resource;

import exception.SensorUnavailableException;
import model.ErrorResponse;
import model.Sensor;
import model.SensorReading;
import store.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SensorReadingResource {

    private final String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    // GET /api/v1/sensors/{sensorId}/readings
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReadings() {
        if (!DataStore.getSensors().containsKey(sensorId)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Not Found",
                            "Sensor with ID '" + sensorId + "' does not exist.", 404))
                    .build();
        }
        List<SensorReading> history = DataStore.getReadings().get(sensorId);
        if (history == null) {
            history = new ArrayList<>();
        }
        return Response.ok(history).build();
    }

    // GET /api/v1/sensors/{sensorId}/readings/{readingId}
    @GET
    @Path("/{readingId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReadingById(@PathParam("readingId") String readingId) {
        List<SensorReading> history = DataStore.getReadings().get(sensorId);
        if (history != null) {
            for (SensorReading r : history) {
                if (r.getId().equals(readingId)) {
                    return Response.ok(r).build();
                }
            }
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse("Not Found",
                        "Reading with ID '" + readingId + "' not found.", 404))
                .build();
    }

    // POST /api/v1/sensors/{sensorId}/readings
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReading(SensorReading reading) {

        Sensor sensor = DataStore.getSensors().get(sensorId);
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Not Found",
                            "Sensor with ID '" + sensorId + "' does not exist.", 404))
                    .build();
        }

        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException(
                "Sensor '" + sensorId + "' is currently under MAINTENANCE and "
                + "cannot accept new readings. Please update its status first."
            );
        }

        if ("OFFLINE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException(
                "Sensor '" + sensorId + "' is OFFLINE and cannot accept new readings."
            );
        }

        if (reading.getId() == null || reading.getId().trim().isEmpty()) {
            reading.setId(UUID.randomUUID().toString());
        }

        if (reading.getTimestamp() == 0) {
            reading.setTimestamp(System.currentTimeMillis());
        }

        DataStore.getReadings().computeIfAbsent(sensorId, k -> new ArrayList<>()).add(reading);

        sensor.setCurrentValue(reading.getValue());

        return Response.status(Response.Status.CREATED).entity(reading).build();
    }
}