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
 * {@code SpringECI} is a custom lightweight framework that provides functionalities similar to popular frameworks
 * like Spring. It is responsible for discovering and loading classes annotated with specific annotations
 * (e.g., {@link RestController}, {@link GetMapping}, {@link SpringECIApplication}), registering RESTful services,
 * and starting the application.
 */
public class SpringECI {

    /**
     * The entry point of the SpringECI framework. This method initializes the framework by performing the following:
     * <ul>
     * <li>Loading classes from the specified package.</li>
     * <li>Scanning for classes with the {@link RestController} annotation and methods with {@link RequestMapping}
     * or {@link GetMapping} annotations.</li>
     * <li>Registering RESTful services in the web server.</li>
     * <li>Executing the {@code main} method of the class annotated with {@link SpringECIApplication}.</li>
     * </ul>
     *
     * @param args Command line arguments passed to the application.
     */
    public static void main(String[] args) {
        try {
            // Load classes from the package
            URL[] urls = {SpringECI.class.getProtectionDomain().getCodeSource().getLocation()};
            URLClassLoader classLoader = new URLClassLoader(urls);
            Set<Class<?>> classes = getClassesInPackage(classLoader, "co.edu.escuelaing");

            // Map to store routes and associated methods
            Map<String, Map<RequestMethod, Method>> services = new HashMap<>();

            // Scan classes to find controllers and mapped methods
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

            // Configure services in the web server
            WebServer.getInstance();
            WebServer.setServices(services);


            // Execute the application annotated with @SpringECIApplication
            for (Class<?> c : classes) {
                if (c.isAnnotationPresent(SpringECIApplication.class)) {
                    Method mainMethod = c.getMethod("main", String[].class);
                    mainMethod.setAccessible(true);
                    String[] argsMain = new String[0]; // Create an empty array for arguments
                    mainMethod.invoke(null, (Object) argsMain); // Pass the empty array as arguments
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Scans the specified package for all classes and returns a set of those classes.
     * The method handles both JAR files and file system directories.
     *
     * @param classLoader The class loader used to load the classes.
     * @param packageName The package name to scan for classes.
     * @return A set of all classes found in the specified package.
     * @throws IOException            If an I/O error occurs while reading resources.
     * @throws ClassNotFoundException If a class cannot be located.
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
