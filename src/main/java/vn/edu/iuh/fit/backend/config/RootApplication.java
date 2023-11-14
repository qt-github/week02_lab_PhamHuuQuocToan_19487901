package vn.edu.iuh.fit.backend.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import vn.edu.iuh.fit.backend.converts.ObjectMapperContextResolver;
import vn.edu.iuh.fit.backend.resources.*;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationPath("/api")
public class RootApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        return Stream.of(
                ObjectMapperContextResolver.class,
                CustomerResource.class,
                ProductResource.class,
                EmployeeResource.class,
                OrderResource.class,
                OrderDetailResource.class,
                ProductImageResource.class,
                ProductPriceResource.class
        ).collect(Collectors.toSet());
    }
}