package co.edu.escuelaing.framework;

import co.edu.escuelaing.framework.enums.RequestMethod;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class WebServerTest {

    @Test
    public void testServerInitialization() {
        WebServer server = WebServer.getInstance();
        assertNotNull(server);
        WebServer anotherServer = WebServer.getInstance();
        assertSame(server, anotherServer); // Asegurar que es un singleton
    }


    @Test
    public void testSetServices() {
        Map<String, Map<RequestMethod, Method>> servicesMap = new HashMap<>();
        WebServer.setServices(servicesMap);
        assertNotNull(WebServer.getServices()); // Asegura que los servicios han sido asignados correctamente
        assertSame(servicesMap, WebServer.getServices());
    }

    @Test
    public void testParseQueryParams() {
        String queryString = "name=John&age=25";
        Map<String, String> params = WebServer.parseQueryParams(queryString);

        assertEquals("John", params.get("name"));
        assertEquals("25", params.get("age"));
    }

    @Test
    public void testParseEmptyQueryParams() {
        String queryString = "";
        Map<String, String> params = WebServer.parseQueryParams(queryString);

        assertTrue(params.isEmpty());
    }

    @Test
    public void testHandleStaticFileRequest() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String path = "/index.html";  // Debes tener un archivo 'index.html' en la carpeta de archivos estáticos
        WebServer.handleStaticFileRequest(path, out);

        String response = out.toString();
        assertTrue(response.contains("HTTP/1.1 200 OK"));
        assertTrue(response.contains("Content-Type: text/html"));
    }

    @Test
    public void testSendResponse() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String body = "Hello World";

        WebServer.sendResponse(out, "200 OK", "text/plain", body.getBytes());

        String response = out.toString();
        assertTrue(response.contains("HTTP/1.1 200 OK"));
        assertTrue(response.contains("Content-Type: text/plain"));
        assertTrue(response.contains("Content-Length: " + body.length()));
        assertTrue(response.contains(body));
    }


    @Test
    public void testInvokeMethodWithParams() throws Exception {
        Method greetMethod = TestService.class.getDeclaredMethod("greet", String.class);

        Map<String, String> params = new HashMap<>();
        params.put("name", "Alice");

        Object result = WebServer.invokeMethodWithParams(greetMethod, params);
        assertEquals("Hello Alice", result);
    }

    @Test
    public void testHandleServiceRequest_NotFound() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        WebServer.setServices(new HashMap<>());  // Mapa vacío de servicios

        WebServer.handleServiceRequest("/App/nonexistent", "GET", out);

        String response = out.toString();
        assertTrue(response.contains("HTTP/1.1 404 Not Found"));
        assertTrue(response.contains("Service not found"));
    }




}


