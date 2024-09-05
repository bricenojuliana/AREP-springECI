package co.edu.escuelaing.application;

import co.edu.escuelaing.framework.annotations.RequestMapping;
import co.edu.escuelaing.framework.annotations.RequestParam;
import co.edu.escuelaing.framework.annotations.RestController;
import co.edu.escuelaing.framework.enums.RequestMethod;

/**
 * {@code CalculatorService} is a REST controller that provides basic mathematical operations.
 * It uses custom annotations to handle HTTP GET requests and accept parameters via the URL.
 */
@RestController
public class CalculatorService {

    /**
     * Handles GET requests to greet the user.
     *
     * @param name The name of the user. If not provided, "Guest" is used as the default value.
     * @return A personalized greeting message for the user.
     */
    @RequestMapping(value = "/greet", method = RequestMethod.GET)
    public String greet(@RequestParam(value = "name", defaultValue = "Guest") String name) {
        return "Hello, " + name + "!";
    }

    /**
     * Handles GET requests to add two numbers.
     *
     * @param a The first number to add.
     * @param b The second number to add.
     * @return The sum of the two numbers as a string.
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(@RequestParam("a") int a, @RequestParam("b") int b) {
        return "Sum: " + a + " + " + b + " = " + (a + b);
    }

    /**
     * Handles GET requests to subtract two numbers.
     *
     * @param a The number from which to subtract.
     * @param b The number to subtract.
     * @return The difference between the two numbers as a string.
     */
    @RequestMapping(value = "/subtract", method = RequestMethod.GET)
    public String subtract(@RequestParam("a") int a, @RequestParam("b") int b) {
        return "Difference: " + a + " - " + b + " = " + (a - b);
    }

    /**
     * Handles GET requests to multiply two numbers.
     *
     * @param a The first number to multiply.
     * @param b The second number to multiply.
     * @return The product of the two numbers as a string.
     */
    @RequestMapping(value = "/multiply", method = RequestMethod.GET)
    public String multiply(@RequestParam("a") int a, @RequestParam("b") int b) {
        return "Product: " + a + " * " + b + " = " + (a * b);
    }

    /**
     * Handles GET requests to divide two numbers.
     *
     * @param a The dividend.
     * @param b The divisor.
     * @return The quotient of the division as a string. If the divisor is zero, returns an error message.
     */
    @RequestMapping(value = "/divide", method = RequestMethod.GET)
    public String divide(@RequestParam("a") int a, @RequestParam("b") int b) {
        if (b == 0) {
            return "Error: Division by zero is undefined.";
        }
        return "Quotient: " + a + " / " + b + " = " + (a / b);
    }

    /**
     * Handles GET requests to calculate the square root of a number.
     *
     * @param number The number for which to calculate the square root.
     * @return The square root of the number as a string. If the number is negative, returns an error message.
     */
    @RequestMapping(value = "/sqrt", method = RequestMethod.GET)
    public String sqrt(@RequestParam("number") double number) {
        if (number < 0) {
            return "Error: Square root of a negative number is undefined.";
        }
        return "Square root: sqrt(" + number + ") = " + Math.sqrt(number);
    }
}
