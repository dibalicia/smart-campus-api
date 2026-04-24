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


@Path("/rooms")
public class RoomResource {

   
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRooms() {
        List<Room> roomList = new ArrayList<>(DataStore.getRooms().values());
        return Response.ok(roomList).build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRoom(Room room) {

      
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


        if (DataStore.getRooms().containsKey(room.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorResponse("Conflict",
                            "A room with ID '" + room.getId() + "' already exists.", 409))
                    .build();
        }

        DataStore.getRooms().put(room.getId(), room);

       
        return Response.status(Response.Status.CREATED).entity(room).build();
    }


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
