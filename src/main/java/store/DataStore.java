package store;

import model.Room;
import model.Sensor;
import model.SensorReading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Central in-memory data store for the Smart Campus API.
 *
 * All data is stored in static HashMaps so it is shared across every resource
 * class instance. This is necessary because JAX-RS creates a new resource
 * instance for each request - if data were stored as instance fields it would
 * be lost after every single request.
 *
 * The static initialiser block pre-loads a small set of sample data so
 * the API is not empty when the server first starts up.
 */
public class DataStore {

    // Static maps act as our in-memory "database"
    private static final Map<String, Room> rooms = new HashMap<>();
    private static final Map<String, Sensor> sensors = new HashMap<>();
    private static final Map<String, List<SensorReading>> readings = new HashMap<>();

    // Pre-load sample data at class load time
    static {
        // --- Sample Rooms ---
        Room lib301 = new Room("LIB-301", "Library Quiet Study", 50);
        Room csLab  = new Room("CS-101", "Computer Science Lab", 30);
        Room confA  = new Room("CONF-A", "Conference Room A", 20);

        rooms.put(lib301.getId(), lib301);
        rooms.put(csLab.getId(),  csLab);
        rooms.put(confA.getId(),  confA);

        // --- Sample Sensors ---
        Sensor temp01 = new Sensor("TEMP-001", "Temperature", "ACTIVE",    22.5, "LIB-301");
        Sensor co2_01 = new Sensor("CO2-001",  "CO2",         "ACTIVE",   400.0, "LIB-301");
        Sensor occ01  = new Sensor("OCC-001",  "Occupancy",   "MAINTENANCE", 0.0, "CS-101");

        sensors.put(temp01.getId(), temp01);
        sensors.put(co2_01.getId(), co2_01);
        sensors.put(occ01.getId(),  occ01);

        // Link sensors to their rooms
        lib301.getSensorIds().add(temp01.getId());
        lib301.getSensorIds().add(co2_01.getId());
        csLab.getSensorIds().add(occ01.getId());

        // Initialise empty reading history for each sensor
        readings.put(temp01.getId(), new ArrayList<>());
        readings.put(co2_01.getId(), new ArrayList<>());
        readings.put(occ01.getId(),  new ArrayList<>());
    }

    // Accessors - resource classes call these to get or modify data
    public static Map<String, Room> getRooms()   { return rooms;   }
    public static Map<String, Sensor> getSensors() { return sensors; }
    public static Map<String, List<SensorReading>> getReadings() { return readings; }
}
