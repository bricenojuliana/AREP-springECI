package co.edu.escuelaing.framework;

import co.edu.escuelaing.framework.annotations.RequestParam;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@code WebServer} class is responsible for handling incoming HTTP requests,
 * routing them to the appropriate service methods or serving static files.
 * It operates as a singleton and listens for connections on a predefined port.
 */
public class WebServer {
    private static WebServer instance;
    private static final int PORT = 8080;
    private static Map<String, Method> services = new HashMap<>();

    /**
     * Private constructor to prevent instantiation.
     */
    private WebServer() {}

    /**
     * Returns the singleton instance of {@code WebServer}.
     *
     * @return the singleton instance
     */
    public static WebServer getInstance() {
        if (instance == null) {
            instance = new WebServer();
        }
        return instance;
    }

    /**
     * Sets the map of services to handle requests.
     *
     * @param services a map where the key is the request path and the value is the method to invoke
     */
    public static void setServices(Map<String, Method> services) {
        WebServer.services = services;
    }

    /**
     * Starts the web server, listening for incoming connections on the specified port.
     * Accepts client connections and delegates request handling to {@code handleRequest}.
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
            e.printStackTrace(); // Añadido para ayudar en la depuración
        }
    }

    /**
     * Handles an incoming HTTP request by delegating to either a service method or static file handling.
     *
     * @param clientSocket the socket connected to the client
     * @throws IOException if an I/O error occurs
     */
    private static void handleRequest(Socket clientSocket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        OutputStream out = clientSocket.getOutputStream();
        String inputLine = in.readLine();
        if (inputLine != null) {
            String[] requestParts = inputLine.split(" ");
            String path = requestParts[1];

            if (path.startsWith("/App/")) {
                handleServiceRequest(path, out);
            } else {
                handleStaticFileRequest(path, out);
            }
        }
    }

    /**
     * Handles requests that are routed to service methods.
     * Extracts query parameters, invokes the appropriate service method, and sends the response.
     *
     * @param path the request path
     * @param out the output stream to write the response
     * @throws IOException if an I/O error occurs
     */
    private static void handleServiceRequest(String path, OutputStream out) throws IOException {
        // Eliminar el prefijo "/App" del path
        String[] parts = path.substring("/App".length()).split("\\?");
        String servicePath = parts[0];
        String queryString = parts.length > 1 ? parts[1] : "";

        Method method = services.get(servicePath);
        if (method != null) {
            try {
                // Parsear los parámetros de consulta
                Map<String, String> queryParams = parseQueryParams(queryString);

                // Invocar el metodo con los parámetros adecuados
                Object result = invokeMethodWithParams(method, queryParams);

                sendResponse(out, "200 OK", "text/plain", result.toString().getBytes());
            } catch (Exception e) {
                sendResponse(out, "500 Internal Server Error", "text/plain", "Error processing request".getBytes());
            }
        } else {
            sendResponse(out, "404 Not Found", "text/plain", "Service not found".getBytes());
        }
    }

    /**
     * Parses the query parameters from the query string.
     *
     * @param queryString the query string from the request
     * @return a map of query parameters
     */
    private static Map<String, String> parseQueryParams(String queryString) {
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
     * Invokes a service method with the provided parameters, converting them as needed.
     *
     * @param method the method to invoke
     * @param params a map of parameter names to values
     * @return the result of invoking the method
     * @throws IllegalAccessException if the method is inaccessible
     * @throws InvocationTargetException if the method throws an exception
     * @throws InstantiationException if the class cannot be instantiated
     * @throws NoSuchMethodException if the method cannot be found
     */
    private static Object invokeMethodWithParams(Method method, Map<String, String> params)
            throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] arguments = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            RequestParam requestParam = method.getParameters()[i].getAnnotation(RequestParam.class);
            if (requestParam != null) {
                String paramName = requestParam.value();
                String paramValue = params.getOrDefault(paramName, requestParam.defaultValue());

                // Convert the parameter value to the correct type
                if (parameterTypes[i] == String.class) {
                    arguments[i] = paramValue;
                } else if (parameterTypes[i] == double.class) {
                    arguments[i] = Double.parseDouble(paramValue);
                } else {
                    throw new IllegalArgumentException("Unsupported parameter type: " + parameterTypes[i].getName());
                }
            } else {
                // Handle cases where parameters are not annotated
                if (parameterTypes[i].isPrimitive()) {
                    // Provide default values for primitive types
                    if (parameterTypes[i] == int.class) {
                        arguments[i] = 0;
                    } else if (parameterTypes[i] == double.class) {
                        arguments[i] = 0.0;
                    } else if (parameterTypes[i] == boolean.class) {
                        arguments[i] = false;
                    } else {
                        throw new IllegalArgumentException("Unsupported parameter type: " + parameterTypes[i].getName());
                    }
                } else {
                    // For non-primitive types, you might handle them differently or throw an exception
                    throw new IllegalArgumentException("Missing @RequestParam annotation on parameter: " + method.getParameters()[i].getName());
                }
            }
        }

        return method.invoke(method.getDeclaringClass().getDeclaredConstructor().newInstance(), arguments);
    }


    /**
     * Handles requests for static files by serving them from the configured static files directory.
     *
     * @param path the request path
     * @param out the output stream to write the response
     * @throws IOException if an I/O error occurs
     */
    private static void handleStaticFileRequest(String path, OutputStream out) throws IOException {
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
     * @param out the output stream to write the response
     * @param status the HTTP status code and message
     * @param contentType the MIME type of the response content
     * @param body the response body as a byte array
     * @throws IOException if an I/O error occurs
     */
    private static void sendResponse(OutputStream out, String status, String contentType, byte[] body) throws IOException {
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
     * Determines the content type based on the file extension.
     *
     * @param filePath the path of the file
     * @return the MIME type of the file
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
}
