package co.edu.escuelaing.framework;

/**
 * Provides configuration settings for the SpringECI framework.
 * <p>
 * This class allows setting and retrieving the location of static files used by the web server.
 * It provides methods to configure where the static files (e.g., HTML, CSS, JavaScript) are located
 * and to access this configuration at runtime.
 * </p>
 */
public class FrameworkConfig {
    private static String staticFilesLocation = "src/main/resources";

    /**
     * Sets the location of static files for the web server.
     * <p>
     * This method allows the user to specify a different directory for static files.
     * The default location is "src/main/resources".
     * </p>
     *
     * @param location The file path where static files are stored.
     */
    public static void staticfiles(String location) {
        staticFilesLocation = location;
    }

    /**
     * Retrieves the current location of static files.
     * <p>
     * This method returns the directory path that has been set for static files.
     * If the location was not changed from the default, it returns "src/main/resources".
     * </p>
     *
     * @return The file path where static files are located.
     */
    public static String getStaticFilesLocation() {
        return staticFilesLocation;
    }
}
