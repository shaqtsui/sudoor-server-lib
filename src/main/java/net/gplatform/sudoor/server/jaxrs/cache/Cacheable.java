package net.gplatform.sudoor.server.jaxrs.cache;
import java.lang.annotation.*;
import javax.ws.rs.NameBinding;

@NameBinding
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Cacheable {
        String cc() default "public, must-revalidate"; 
}

