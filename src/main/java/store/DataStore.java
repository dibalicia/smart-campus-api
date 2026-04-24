package store;

import model.Room;
import model.Sensor;
import model.SensorReading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DataStore {

    private static final Map<String, Room> rooms = new HashMap<>();
    private static final Map<String, Sensor> sensors = new HashMap<>();
    private static final Map<String, List<SensorReading>> readings = new HashMap<>();

    static {
       
        Room lib301 = new Room("LIB-301", "Library Quiet Study", 50);
        Room csLab  = new Room("CS-101", "Computer Science Lab", 30);
        Room confA  = new Room("CONF-A", "Conference Room A", 20);

        rooms.put(lib301.getId(), lib301);
        rooms.put(csLab.getId(),  csLab);
        rooms.put(confA.getId(),  confA);

   
        Sensor temp01 = new Sensor("TEMP-001", "Temperature", "ACTIVE",    22.5, "LIB-301");
        Sensor co2_01 = new Sensor("CO2-001",  "CO2",         "ACTIVE",   400.0, "LIB-301");
        Sensor occ01  = new Sensor("OCC-001",  "Occupancy",   "MAINTENANCE", 0.0, "CS-101");

        sensors.put(temp01.getId(), temp01);
        sensors.put(co2_01.getId(), co2_01);
        sensors.put(occ01.getId(),  occ01);

       
        lib301.getSensorIds().add(temp01.getId());
        lib301.getSensorIds().add(co2_01.getId());
        csLab.getSensorIds().add(occ01.getId());

        readings.put(temp01.getId(), new ArrayList<>());
        readings.put(co2_01.getId(), new ArrayList<>());
        readings.put(occ01.getId(),  new ArrayList<>());
    }

   
    public static Map<String, Room> getRooms()   { return rooms;   }
    public static Map<String, Sensor> getSensors() { return sensors; }
    public static Map<String, List<SensorReading>> getReadings() { return readings; }
}
