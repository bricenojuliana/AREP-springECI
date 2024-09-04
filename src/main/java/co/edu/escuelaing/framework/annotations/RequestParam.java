package co.edu.escuelaing.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to bind a method parameter to a web request parameter.
 * <p>
 * It indicates that a method parameter should be populated with the value of a web request parameter
 * with the specified name. If the request parameter is not provided, a default value can be used.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface RequestParam {

    /**
     * The name of the request parameter to bind to.
     * <p>
     * This is the name of the parameter in the web request that will be mapped to the annotated method parameter.
     * </p>
     *
     * @return the name of the request parameter.
     */
    String value();

    /**
     * The default value to use if the request parameter is not provided.
     * <p>
     * If the request does not include a parameter with the specified name, this default value will be used
     * for the annotated method parameter. The default value is an empty string if not specified.
     * </p>
     *
     * @return the default value to use if the request parameter is not provided.
     */
    String defaultValue() default "";
}
