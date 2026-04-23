package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a physical room in the Smart Campus.
 * Rooms contain sensors which are tracked via their IDs.
 */
public class Room {

    private String id;        // e.g. "LIB-301"
    private String name;      // e.g. "Library Quiet Study"
    private int capacity;     // maximum occupancy for safety
    private List<String> sensorIds = new ArrayList<>(); // IDs of sensors in this room

    // Default constructor required by Jackson for JSON deserialisation
    public Room() {}

    public Room(String id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public List<String> getSensorIds() { return sensorIds; }
    public void setSensorIds(List<String> sensorIds) { this.sensorIds = sensorIds; }
}
