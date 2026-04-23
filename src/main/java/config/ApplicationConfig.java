package config;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api/v1")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();

        // Resource classes
        classes.add(resource.DiscoveryResource.class);
        classes.add(resource.RoomResource.class);
        classes.add(resource.SensorResource.class);

        // Exception Mappers
        classes.add(exception.RoomNotEmptyExceptionMapper.class);
        classes.add(exception.LinkedResourceNotFoundExceptionMapper.class);
        classes.add(exception.SensorUnavailableExceptionMapper.class);
        classes.add(exception.GlobalExceptionMapper.class);

        // Filters
        classes.add(filter.LoggingFilter.class);

        return classes;
    }
}
