package co.edu.escuelaing.framework;

import co.edu.escuelaing.framework.annotations.GetMapping;
import co.edu.escuelaing.framework.annotations.RequestMapping;
import co.edu.escuelaing.framework.annotations.RestController;
import co.edu.escuelaing.framework.annotations.SpringECIApplication;
import co.edu.escuelaing.framework.enums.RequestMethod;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * The SpringECI class is a custom lightweight framework that mimics some functionality of
 * popular frameworks like Spring. It automatically discovers and loads classes annotated
 * with specific annotations such as {@link RestController}, {@link GetMapping}, and {@link SpringECIApplication}.
 * The framework registers services defined in the controllers and starts the application.
 */
public class SpringECI {

    /**
     * The entry point of the SpringECI framework. This method initializes the framework, scans
     * the specified package for classes with the appropriate annotations, registers RESTful
     * services, and starts the web server.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        try {
            // Cargar clases en el paquete
            URL[] urls = {SpringECI.class.getProtectionDomain().getCodeSource().getLocation()};
            URLClassLoader classLoader = new URLClassLoader(urls);
            Set<Class<?>> classes = getClassesInPackage(classLoader, "co.edu.escuelaing");

            // Mapa para almacenar rutas y métodos asociados
            Map<String, Map<RequestMethod, Method>> services = new HashMap<>();

            // Escanear clases para encontrar controladores y métodos mapeados
            for (Class<?> c : classes) {
                if (c.isAnnotationPresent(RestController.class)) {
                    for (Method method : c.getMethods()) {
                        if (method.isAnnotationPresent(RequestMapping.class)) {
                            RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                            String route = mapping.value();
                            RequestMethod httpMethod = mapping.method();
                            services.computeIfAbsent(route, k -> new HashMap<>()).put(httpMethod, method);
                        }
                        if (method.isAnnotationPresent(GetMapping.class)) {
                            GetMapping mapping = method.getAnnotation(GetMapping.class);
                            String route = mapping.value();
                            services.computeIfAbsent(route, k -> new HashMap<>()).put(RequestMethod.GET, method);
                        }
                    }
                }
            }

            // Configurar los servicios en el servidor web
            WebServer.getInstance();
            WebServer.setServices(services);


            // Ejecutar la aplicación anotada con @SpringECIApplication
            for (Class<?> c : classes) {
                if (c.isAnnotationPresent(SpringECIApplication.class)) {
                    Method mainMethod = c.getMethod("main", String[].class);
                    mainMethod.setAccessible(true);
                    String[] argsMain = new String[0]; // Crear un arreglo vacío para argumentos
                    mainMethod.invoke(null, (Object) argsMain); // Pasar el arreglo vacío como argumentos
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Scans the specified package for all classes and returns a set of those classes.
     *
     * @param classLoader The class loader used to load the classes
     * @param packageName The package name to scan for classes
     * @return A set of all classes found in the specified package
     * @throws IOException            If an I/O error occurs while reading resources
     * @throws ClassNotFoundException If a class cannot be located
     */
    static Set<Class<?>> getClassesInPackage(URLClassLoader classLoader, String packageName) throws IOException, ClassNotFoundException {
        Set<Class<?>> classes = new HashSet<>();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();

            // Handle JAR files
            if (resource.getProtocol().equals("jar")) {
                JarURLConnection connection = (JarURLConnection) resource.openConnection();
                JarFile jarFile = connection.getJarFile();
                Enumeration<JarEntry> entries = jarFile.entries();

                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    if (name.startsWith(path) && name.endsWith(".class")) {
                        String className = name.substring(0, name.length() - 6).replace('/', '.');
                        classes.add(Class.forName(className));
                    }
                }
            } else {
                // Handle file system directories
                File directory = new File(resource.getFile());
                if (directory.exists()) {
                    File[] files = directory.listFiles();
                    for (File file : files) {
                        if (file.isDirectory()) {
                            classes.addAll(getClassesInPackage(classLoader, packageName + "." + file.getName()));
                        } else if (file.getName().endsWith(".class")) {
                            String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                            classes.add(Class.forName(className));
                        }
                    }
                }
            }
        }
        return classes;
    }
}
