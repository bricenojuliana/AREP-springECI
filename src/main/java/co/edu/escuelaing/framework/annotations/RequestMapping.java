package co.edu.escuelaing.framework.annotations;

import co.edu.escuelaing.framework.enums.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code RequestMapping} is an annotation used to map HTTP requests to handler methods in a REST controller.
 * It specifies the URL path and HTTP method that should be handled by the annotated method.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestMapping {

    /**
     * The URL path to which the method should be mapped.
     *
     * @return The URL path as a string.
     */
    String value();

    /**
     * The HTTP method to which the method should respond. The default value is {@code RequestMethod.GET}.
     *
     * @return The HTTP method as an enumeration value.
     */
    RequestMethod method() default RequestMethod.GET;
}
