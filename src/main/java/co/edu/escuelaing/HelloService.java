package co.edu.escuelaing;

@RestController
public class HelloService {
    @GetMapping("/hello")
    public static String hello() {
        return "Hello world!";
    }

    @GetMapping("/pi")
    public static String pi() {
        return "PI: " + Math.PI;
    }

    @GetMapping("/now")
    public static String now() {
        return "Now: " + new java.util.Date();
    }

    @GetMapping("/uuid")
    public static String uuid() {
        return "UUID: " + java.util.UUID.randomUUID();
    }

    @GetMapping("/bye")
    public static String bye() {
        return "Bye!";
    }

}
