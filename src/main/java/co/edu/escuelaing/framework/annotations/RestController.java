package co.edu.escuelaing.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to indicate that a class serves the role of a controller in a Spring application.
 * <p>
 * Classes annotated with {@code @RestController} are used to handle HTTP requests in a Spring application.
 * They typically contain methods annotated with {@code @GetMapping}, {@code @PostMapping}, {@code @PutMapping},
 * or {@code @DeleteMapping} to map HTTP requests to Java methods. This annotation is a specialization of
 * {@code @Controller} and is typically used in combination with {@code @ResponseBody} to indicate that the
 * response from the methods should be serialized directly into the HTTP response body.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RestController {
}
