package co.edu.escuelaing.framework;

import co.edu.escuelaing.framework.annotations.RequestParam;
import co.edu.escuelaing.framework.enums.RequestMethod;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * The WebServer class represents a custom web server that handles HTTP requests, serves static files,
 * and dispatches requests to registered services based on specified routes and methods.
 * It is a singleton class that manages the server lifecycle and request handling.
 */
public class WebServer {
    private static final int PORT = 8080;
    private static WebServer instance;
    private static Map<String, Map<RequestMethod, Method>> services;

    private WebServer() {
    }

    /**
     * Returns the singleton instance of the WebServer.
     *
     * @return The singleton instance of WebServer
     */
    public static WebServer getInstance() {
        if (instance == null) {
            instance = new WebServer();
        }
        return instance;
    }

    /**
     * Sets the services map with routes and associated methods.
     *
     * @param services Map of routes to HTTP methods and their corresponding methods
     */
    public static void setServices(Map<String, Map<RequestMethod, Method>> services) {
        WebServer.services = services;
    }

    /**
     * Starts the web server and listens for incoming client connections on the specified port.
     */
    public static void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Listening on port " + PORT);
            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    handleRequest(clientSocket);
                }
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + PORT);
            e.printStackTrace();
        }
    }

    /**
     * Handles incoming HTTP requests, routing them to either service handlers or static file handlers.
     *
     * @param clientSocket The socket connected to the client
     * @throws IOException If an I/O error occurs while reading from or writing to the socket
     */
    private static void handleRequest(Socket clientSocket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        OutputStream out = clientSocket.getOutputStream();
        String inputLine = in.readLine();
        if (inputLine != null) {
            String[] requestParts = inputLine.split(" ");
            String method = requestParts[0];
            String path = requestParts[1];
            String protocol = requestParts[2];

            if (path.startsWith("/App/")) {
                handleServiceRequest(path, method, out);
            } else {
                handleStaticFileRequest(path, out);
            }
        }
    }

    /**
     * Handles requests to registered services by dispatching to the appropriate method.
     *
     * @param path        The request path
     * @param method      The HTTP method (GET, POST, etc.)
     * @param out         The output stream to write the response to
     * @throws IOException If an I/O error occurs while sending the response
     */
    static void handleServiceRequest(String path, String method, OutputStream out) throws IOException {
        String[] parts = path.substring("/App".length()).split("\\?");
        String servicePath = parts[0];
        String queryString = parts.length > 1 ? parts[1] : "";

        Map<RequestMethod, Method> methodMap = services.get(servicePath);
        if (methodMap != null) {
            RequestMethod requestMethod = RequestMethod.valueOf(method.toUpperCase());
            Method serviceMethod = methodMap.get(requestMethod);
            if (serviceMethod != null) {
                try {
                    Map<String, String> queryParams = parseQueryParams(queryString);
                    Object result = invokeMethodWithParams(serviceMethod, queryParams);
                    sendResponse(out, "200 OK", "text/plain", result.toString().getBytes());
                } catch (Exception e) {
                    sendResponse(out, "500 Internal Server Error", "text/plain", "Error processing request".getBytes());
                }
            } else {
                sendResponse(out, "405 Method Not Allowed", "text/plain", "Method not allowed".getBytes());
            }
        } else {
            sendResponse(out, "404 Not Found", "text/plain", "Service not found".getBytes());
        }
    }

    /**
     * Parses query parameters from a query string.
     *
     * @param queryString The query string to parse
     * @return A map of parameter names to values
     */
    static Map<String, String> parseQueryParams(String queryString) {
        Map<String, String> params = new HashMap<>();
        if (queryString != null && !queryString.isEmpty()) {
            String[] pairs = queryString.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    params.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return params;
    }

    /**
     * Invokes a service method with the provided query parameters.
     *
     * @param method The method to invoke
     * @param params The query parameters to pass to the method
     * @return The result of the method invocation
     * @throws IllegalAccessException If the method cannot be accessed
     * @throws InvocationTargetException If an exception is thrown by the method
     * @throws InstantiationException If the method's declaring class cannot be instantiated
     * @throws NoSuchMethodException If the method cannot be found
     */
    static Object invokeMethodWithParams(Method method, Map<String, String> params)
            throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] arguments = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            RequestParam requestParam = method.getParameters()[i].getAnnotation(RequestParam.class);
            if (requestParam != null) {
                String paramName = requestParam.value();
                String paramValue = params.getOrDefault(paramName, requestParam.defaultValue());

                if (parameterTypes[i] == String.class) {
                    arguments[i] = paramValue;
                } else if (parameterTypes[i] == double.class) {
                    arguments[i] = Double.parseDouble(paramValue);
                } else if (parameterTypes[i] == int.class) {
                    arguments[i] = Integer.parseInt(paramValue);
                } else if (parameterTypes[i] == boolean.class) {
                    arguments[i] = Boolean.parseBoolean(paramValue);
                } else {
                    throw new IllegalArgumentException("Unsupported parameter type: " + parameterTypes[i].getName());
                }
            } else {
                // Handle default values for parameters without @RequestParam annotation
                if (parameterTypes[i].isPrimitive()) {
                    if (parameterTypes[i] == int.class) {
                        arguments[i] = 0;
                    } else if (parameterTypes[i] == double.class) {
                        arguments[i] = 0.0;
                    } else if (parameterTypes[i] == boolean.class) {
                        arguments[i] = false;
                    } else {
                        throw new IllegalArgumentException("Unsupported primitive type: " + parameterTypes[i].getName());
                    }
                } else {
                    // For non-primitive types, you might need to handle them differently or throw an exception
                    throw new IllegalArgumentException("Missing @RequestParam annotation on parameter: " + method.getParameters()[i].getName());
                }
            }
        }

        return method.invoke(method.getDeclaringClass().getDeclaredConstructor().newInstance(), arguments);
    }


    /**
     * Handles requests for static files (e.g., HTML, CSS, JS) by serving the requested file from the file system.
     *
     * @param path The request path
     * @param out  The output stream to write the response to
     * @throws IOException If an I/O error occurs while reading the file or sending the response
     */
    static void handleStaticFileRequest(String path, OutputStream out) throws IOException {
        if ("/".equals(path)) {
            path = "/index.html";
        }

        String filePath = FrameworkConfig.getStaticFilesLocation() + path;
        File file = new File(filePath);
        if (file.exists() && !file.isDirectory()) {
            String contentType = getContentType(filePath);
            byte[] fileContent = Files.readAllBytes(file.toPath());
            sendResponse(out, "200 OK", contentType, fileContent);
        } else {
            sendResponse(out, "404 Not Found", "text/plain", "File not found".getBytes());
        }
    }

    /**
     * Sends an HTTP response to the client.
     *
     * @param out        The output stream to write the response to
     * @param status     The HTTP status line (e.g., "200 OK")
     * @param contentType The MIME type of the content (e.g., "text/html")
     * @param body       The response body as a byte array
     * @throws IOException If an I/O error occurs while writing the response
     */
    static void sendResponse(OutputStream out, String status, String contentType, byte[] body) throws IOException {
        try (PrintWriter writer = new PrintWriter(out, true)) {
            writer.println("HTTP/1.1 " + status);
            writer.println("Content-Type: " + contentType);
            writer.println("Content-Length: " + body.length);
            writer.println();
            writer.flush();
            out.write(body);
            out.flush();
        }
    }

    /**
     * Determines the MIME type of a file based on its extension.
     *
     * @param filePath The path to the file
     * @return The MIME type of the file
     */
    private static String getContentType(String filePath) {
        if (filePath.endsWith(".html"))
            return "text/html";
        else if (filePath.endsWith(".css"))
            return "text/css";
        else if (filePath.endsWith(".js"))
            return "application/javascript";
        else if (filePath.endsWith(".png"))
            return "image/png";
        else if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg"))
            return "image/jpeg";
        return "text/plain";
    }

    /**
     * Returns the map of registered services.
     *
     * @return The map of services
     */
    public static Map<String, Map<RequestMethod, Method>> getServices() {
        return services;
    }
}
