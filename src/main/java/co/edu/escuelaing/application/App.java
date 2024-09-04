package co.edu.escuelaing.application;

import co.edu.escuelaing.framework.WebServer;
import co.edu.escuelaing.framework.annotations.SpringECIApplication;

import static co.edu.escuelaing.framework.FrameworkConfig.staticfiles;

/**
 * The {@code App} class serves as the entry point for the SpringECI framework application.
 * It is responsible for initializing the web server and configuring static file locations.
 *
 * <p>This class is annotated with {@code @SpringECIApplication} to indicate that it is the
 * main application class for the custom SpringECI framework. It sets up the necessary configurations
 * and starts the web server to handle incoming HTTP requests.</p>
 *
 * <p>Usage:</p>
 * <pre>
 * {@code
 * public class Main {
 *     public static void main(String[] args) {
 *         App.main(args);
 *     }
 * }
 * }
 * </pre>
 *
 * <p>In the {@code main} method:</p>
 * <ul>
 *     <li>{@code staticfiles("src/main/resources")}: Configures the directory where static files are located.</li>
 *     <li>{@code WebServer.getInstance()}: Initializes the singleton instance of the web server.</li>
 *     <li>{@code WebServer.startServer()}: Starts the web server to listen for and handle incoming HTTP requests.</li>
 * </ul>
 */
@SpringECIApplication
public class App {
    /**
     * The main entry point of the application.
     *
     * <p>This method sets up the static file directory and starts the web server.</p>
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        // Configure static file directory
        staticfiles("src/main/resources");

        // Initialize and start the web server
        WebServer.getInstance();
        WebServer.startServer();
    }
}
