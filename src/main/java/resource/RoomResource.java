package resource;

import exception.RoomNotEmptyException;
import model.ErrorResponse;
import model.Room;
import store.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Resource class for managing Rooms.
 *
 * Handles:
 *   GET    /api/v1/rooms           - list all rooms
 *   POST   /api/v1/rooms           - create a new room
 *   GET    /api/v1/rooms/{roomId}  - get a specific room
 *   DELETE /api/v1/rooms/{roomId}  - delete a room (blocked if sensors exist)
 */
@Path("/rooms")
public class RoomResource {

    // GET /api/v1/rooms
    // Returns the full list of all rooms currently in the system
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRooms() {
        List<Room> roomList = new ArrayList<>(DataStore.getRooms().values());
        return Response.ok(roomList).build();
    }

    // POST /api/v1/rooms
    // Creates a new room and stores it in the DataStore
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRoom(Room room) {

        // Basic validation - id and name are required
        if (room.getId() == null || room.getId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Bad Request", "Room 'id' field is required.", 400))
                    .build();
        }
        if (room.getName() == null || room.getName().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Bad Request", "Room 'name' field is required.", 400))
                    .build();
        }

        // Check for duplicate ID
        if (DataStore.getRooms().containsKey(room.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorResponse("Conflict",
                            "A room with ID '" + room.getId() + "' already exists.", 409))
                    .build();
        }

        DataStore.getRooms().put(room.getId(), room);

        // 201 Created with the newly created room in the response body
        return Response.status(Response.Status.CREATED).entity(room).build();
    }

    // GET /api/v1/rooms/{roomId}
    // Returns the details for a single room by its ID
    @GET
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room room = DataStore.getRooms().get(roomId);

        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Not Found",
                            "Room with ID '" + roomId + "' does not exist.", 404))
                    .build();
        }

        return Response.ok(room).build();
    }

    // DELETE /api/v1/rooms/{roomId}
    // Removes a room - but ONLY if it has no sensors still assigned to it.
    // Throws RoomNotEmptyException (mapped to HTTP 409) if sensors exist.
    @DELETE
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.getRooms().get(roomId);

        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("Not Found",
                            "Room with ID '" + roomId + "' does not exist.", 404))
                    .build();
        }

        // Safety check: prevent orphaned sensors
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(
                "Cannot delete room '" + roomId + "'. It still has "
                + room.getSensorIds().size()
                + " sensor(s) assigned to it. Please remove or reassign "
                + "the sensors before deleting this room."
            );
        }

        DataStore.getRooms().remove(roomId);

        return Response.ok(
                new ErrorResponse("Success", "Room '" + roomId + "' has been successfully deleted.", 200)
        ).build();
    }
}
