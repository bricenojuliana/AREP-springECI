package co.edu.escuelaing.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code GetMapping} annotation is used to map HTTP GET requests onto specific handler methods within a
 * web application. This annotation can be applied to methods in a controller class to specify the URL path
 * that the method will handle.
 *
 * <p>This annotation is a part of the custom web framework and mimics the behavior of Spring's {@code @GetMapping}.
 * It allows the developer to define which method should be invoked when a GET request is made to a particular
 * endpoint.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * @RestController
 * public class MyController {
 *
 *     @GetMapping("/greet")
 *     public String greet() {
 *         return "Hello, World!";
 *     }
 * }
 * }
 * </pre>
 *
 * @see RestController
 * @see RequestParam
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GetMapping {

    /**
     * The URL path to which this method is mapped. When an HTTP GET request is made to this path, the
     * annotated method will be invoked.
     *
     * @return the URL path for this GET request mapping
     */
    String value();
}
