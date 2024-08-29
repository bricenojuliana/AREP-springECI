package co.edu.escuelaing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class SpringECI
{
    public static void main( String[] args ) throws ClassNotFoundException, MalformedURLException, InvocationTargetException, IllegalAccessException {
        Class c = Class.forName(args[0]);
        Map<String, Method> services = new HashMap<>();

        // Cargar components
        if(c.isAnnotationPresent(RestController.class)) {
            Method[] methods = c.getMethods();
            for (Method m : methods) {
                if(m.isAnnotationPresent(GetMapping.class)){
                    String key = m.getAnnotation(GetMapping.class).value();
                    services.put(key, m);
                }
            }
        }

        // Código quemado para el ejercicio

        // Hello World
        URL serviceurl = new URL("http://localhost:8080/App/hello");
        String path = serviceurl.getPath();
        System.out.println("Path: " + path);
        String serviceName = path.substring(4);
        System.out.println("Service name: " + serviceName);

        Method ms = services.get(serviceName);
        System.out.println("Respuesta servicio: " + ms.invoke(null));

        // Pi
        serviceurl = new URL("http://localhost:8080/App/pi");
        path = serviceurl.getPath();
        System.out.println("Path: " + path);
        serviceName = path.substring(4);
        System.out.println("Service name: " + serviceName);

        ms = services.get(serviceName);
        System.out.println("Respuesta servicio: " + ms.invoke(null));

        // Now
        serviceurl = new URL("http://localhost:8080/App/now");
        path = serviceurl.getPath();
        System.out.println("Path: " + path);
        serviceName = path.substring(4);
        System.out.println("Service name: " + serviceName);

        ms = services.get(serviceName);
        System.out.println("Respuesta servicio: " + ms.invoke(null));

        // UUID
        serviceurl = new URL("http://localhost:8080/App/uuid");
        path = serviceurl.getPath();
        System.out.println("Path: " + path);
        serviceName = path.substring(4);
        System.out.println("Service name: " + serviceName);

        ms = services.get(serviceName);
        System.out.println("Respuesta servicio: " + ms.invoke(null));

        // Bye
        serviceurl = new URL("http://localhost:8080/App/bye");
        path = serviceurl.getPath();
        System.out.println("Path: " + path);
        serviceName = path.substring(4);
        System.out.println("Service name: " + serviceName);

        ms = services.get(serviceName);
        System.out.println("Respuesta servicio: " + ms.invoke(null));



    }
}
