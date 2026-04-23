package model;

/**s
 * Represents a single historical reading captured by a sensor.
 * Each reading has a unique ID, an epoch timestamp, and the recorded value.
 */
public class SensorReading {

    private String id;        // UUID - unique reading event identifier
    private long timestamp;   // epoch time in milliseconds
    private double value;     // the actual measurement recorded

    // Default constructor required by Jackson
    public SensorReading() {}

    public SensorReading(String id, long timestamp, double value) {
        this.id = id;
        this.timestamp = timestamp;
        this.value = value;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }
}
