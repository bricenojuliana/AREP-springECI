package co.edu.escuelaing.framework.annotations;

import co.edu.escuelaing.framework.SpringECI;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code SpringECIApplication} annotation is used to mark a class as the entry point
 * of a SpringECI application. Classes annotated with this annotation are identified by
 * the framework as the main application class to start the application.
 * <p>
 * This annotation is typically used to indicate the class that contains the {@code main}
 * method which serves as the entry point of the application. The framework will look for
 * this annotation to invoke the {@code main} method to start the application.
 * </p>
 * <p>
 * The annotation has no additional attributes or methods and is retained at runtime.
 * </p>
 *
 * @see SpringECI
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SpringECIApplication {
}
