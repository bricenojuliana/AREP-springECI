package co.edu.escuelaing.framework;

import co.edu.escuelaing.framework.annotations.RequestParam;

public class TestService {
    public String greet(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello " + name;
    }
}
