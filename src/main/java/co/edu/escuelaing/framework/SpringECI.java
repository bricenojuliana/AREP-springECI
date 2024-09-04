package co.edu.escuelaing.framework;

import co.edu.escuelaing.framework.annotations.GetMapping;
import co.edu.escuelaing.framework.annotations.RestController;
import co.edu.escuelaing.framework.annotations.SpringECIApplication;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The {@code SpringECI} class is responsible for initializing the framework by loading
 * controller classes, registering services, and starting the web server.
 * <p>
 * The {@code main} method performs the following tasks:
 * <ul>
 *   <li>Loads all classes from the specified classpath.</li>
 *   <li>Identifies classes annotated with {@code @RestController} and methods
 *       annotated with {@code @GetMapping}, and registers these methods as services.</li>
 *   <li>Starts the web server and sets the registered services.</li>
 *   <li>Looks for a class annotated with {@code @SpringECIApplication} and invokes its
 *       {@code main} method to kick off the application.</li>
 * </ul>
 * </p>
 */
public class SpringECI {

    /**
     * The entry point of the application. It initializes the framework, loads the classes,
     * and configures the web server.
     *
     * @param args command-line arguments (not used in this implementation)
     */
    public static void main(String[] args) {
        try {
            // Define the URL class loader for loading classes from the target directory
            URL[] urls = {new URL("file:./target/classes/")}; // Assumes classes are in target/classes
            try (URLClassLoader classLoader = new URLClassLoader(urls)) {
                // Retrieve all classes in the specified package
                Set<Class<?>> classes = getClassesInPackage(classLoader, "co.edu.escuelaing");

                // Map to store the registered services
                Map<String, Method> services = new HashMap<>();
                for (Class<?> c : classes) {
                    // Check if the class is a REST controller
                    if (c.isAnnotationPresent(RestController.class)) {
                        for (Method method : c.getMethods()) {
                            // Check if the method is annotated with @GetMapping
                            if (method.isAnnotationPresent(GetMapping.class)) {
                                String key = method.getAnnotation(GetMapping.class).value();
                                services.put(key, method);
                            }
                        }
                    }
                }

                // Initialize the web server and set registered services
                WebServer.getInstance();
                WebServer.setServices(services);

                // Find and invoke the main method of the class annotated with @SpringECIApplication
                for (Class<?> c : classes) {
                    if (c.isAnnotationPresent(SpringECIApplication.class)) {
                        Method mainMethod = c.getMethod("main", String[].class);
                        mainMethod.invoke(null, (Object) new String[]{});
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all classes in the specified package by scanning the classpath.
     *
     * @param classLoader the class loader used to load classes
     * @param packageName the name of the package to scan
     * @return a set of classes found in the specified package
     * @throws IOException if an I/O error occurs while accessing the file system
     * @throws ClassNotFoundException if a class cannot be found
     */
    private static Set<Class<?>> getClassesInPackage(URLClassLoader classLoader, String packageName)
            throws IOException, ClassNotFoundException {
        // Convert the package name to a file path
        String path = packageName.replace('.', '/');
        URL resource = classLoader.getResource(path);
        if (resource == null) {
            return Collections.emptySet();
        }

        Path dir = Paths.get(resource.getPath());
        if (!Files.isDirectory(dir)) {
            return Collections.emptySet();
        }

        try (java.util.stream.Stream<Path> stream = Files.walk(dir)) {
            return stream.filter(Files::isRegularFile)
                    .map(Path::toString)
                    .filter(f -> f.endsWith(".class"))
                    .map(f -> f.substring(dir.toString().length() + 1)) // Remove base directory path
                    .map(f -> f.replace('\\', '/')) // Replace backslashes with forward slashes
                    .map(f -> f.replace('/', '.')) // Replace slashes with dots
                    .map(f -> f.replace(".class", "")) // Remove .class extension
                    .map(className -> {
                        try {
                            return classLoader.loadClass(packageName + "." + className);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toSet());
        }
    }

}
