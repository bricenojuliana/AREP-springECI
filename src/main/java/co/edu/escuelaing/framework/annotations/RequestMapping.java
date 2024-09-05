package co.edu.escuelaing.framework.annotations;

import co.edu.escuelaing.framework.enums.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestMapping {
    String value(); // Ruta a mapear
    RequestMethod method() default RequestMethod.GET; // Método HTTP
}
