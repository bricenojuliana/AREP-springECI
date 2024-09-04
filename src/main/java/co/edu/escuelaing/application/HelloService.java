package co.edu.escuelaing.application;

import co.edu.escuelaing.framework.annotations.GetMapping;
import co.edu.escuelaing.framework.annotations.RequestParam;
import co.edu.escuelaing.framework.annotations.RestController;

/**
 * The {@code HelloService} class provides RESTful endpoints for various utilities.
 * This class contains methods annotated with {@code @GetMapping} that handle HTTP GET requests.
 * It demonstrates how to use {@code @RequestParam} to handle request parameters with default values and conversions.
 * <p>
 * Each method in this class corresponds to a specific URL endpoint and returns a result based on the request parameters
 * or performs a simple computation.
 * </p>
 */
@RestController
public class HelloService {

    /**
     * Returns a greeting message.
     * <p>
     * This endpoint responds to GET requests at {@code /hello}. It takes an optional {@code name} parameter
     * from the request, with a default value of "World" if not provided, and returns a greeting message.
     * </p>
     *
     * @param name the name to greet. Defaults to "World" if not provided in the request.
     * @return a greeting message including the provided name.
     */
    @GetMapping("/hello")
    public static String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello " + name + "!";
    }

    /**
     * Returns the value of PI.
     * <p>
     * This endpoint responds to GET requests at {@code /pi} and returns the value of PI.
     * </p>
     *
     * @return a string representation of the value of PI.
     */
    @GetMapping("/pi")
    public static String pi() {
        return "PI: " + Math.PI;
    }

    /**
     * Returns a UUID.
     * <p>
     * This endpoint responds to GET requests at {@code /uuid} and returns a randomly generated UUID.
     * </p>
     *
     * @return a string representation of a randomly generated UUID.
     */
    @GetMapping("/uuid")
    public static String uuid() {
        return "UUID: " + java.util.UUID.randomUUID();
    }

    /**
     * Returns a goodbye message.
     * <p>
     * This endpoint responds to GET requests at {@code /bye} and returns a simple goodbye message.
     * </p>
     *
     * @return a goodbye message.
     */
    @GetMapping("/bye")
    public static String bye() {
        return "Bye!";
    }

    /**
     * Returns a random number within the specified range.
     * <p>
     * This endpoint responds to GET requests at {@code /random}. It takes {@code min} and {@code max}
     * parameters from the request to generate and return a random number within the specified range.
     * </p>
     *
     * @param min the minimum value for the random number.
     * @param max the maximum value for the random number.
     * @return a string representation of a random number within the specified range.
     */
    @GetMapping("/random")
    public static String random(@RequestParam("min") double min, @RequestParam("max") double max) {
        return "Random: " + (Math.random() * (max - min) + min);
    }

    /**
     * Returns the power of a base raised to an exponent.
     * <p>
     * This endpoint responds to GET requests at {@code /pow}. It takes {@code base} and {@code exponent}
     * parameters from the request and returns the result of raising the base to the power of the exponent.
     * </p>
     *
     * @param base     the base value.
     * @param exponent the exponent value.
     * @return a string representation of the result of {@code base} raised to {@code exponent}.
     */
    @GetMapping("/pow")
    public static String pow(@RequestParam("base") double base, @RequestParam("exponent") double exponent) {
        return "Pow: " + Math.pow(base, exponent);
    }


}
