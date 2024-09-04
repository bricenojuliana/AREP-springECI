package co.edu.escuelaing.framework;

import co.edu.escuelaing.framework.annotations.GetMapping;
import co.edu.escuelaing.framework.annotations.RestController;
import co.edu.escuelaing.framework.annotations.SpringECIApplication;

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
            // Get the URL for the current class location
            URL[] urls = {SpringECI.class.getProtectionDomain().getCodeSource().getLocation()};
            URLClassLoader classLoader = new URLClassLoader(urls);

            // Scan for all classes in the specified package
            Set<Class<?>> classes = getClassesInPackage(classLoader, "co.edu.escuelaing");

            // Map to store RESTful services
            Map<String, Method> services = new HashMap<>();

            // Iterate over classes to find those annotated with @RestController and register services
            for (Class<?> c : classes) {
                if (c.isAnnotationPresent(RestController.class)) {
                    for (Method method : c.getMethods()) {
                        if (method.isAnnotationPresent(GetMapping.class)) {
                            String key = method.getAnnotation(GetMapping.class).value();
                            services.put(key, method);
                        }
                    }
                }
            }

            // Initialize the web server and set the registered services
            WebServer.getInstance();
            WebServer.setServices(services);

            // Start the application by invoking the main method of the class annotated with @SpringECIApplication
            for (Class<?> c : classes) {
                if (c.isAnnotationPresent(SpringECIApplication.class)) {
                    Method mainMethod = c.getMethod("main", String[].class);
                    mainMethod.setAccessible(true);
                    String[] argsMain = new String[0]; // Create an empty string array as arguments
                    mainMethod.invoke(null, (Object) argsMain); // Pass the empty string array as arguments
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
    private static Set<Class<?>> getClassesInPackage(URLClassLoader classLoader, String packageName) throws IOException, ClassNotFoundException {
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
