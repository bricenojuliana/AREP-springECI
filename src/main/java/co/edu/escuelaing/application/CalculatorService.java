package co.edu.escuelaing.application;

import co.edu.escuelaing.framework.annotations.RequestMapping;
import co.edu.escuelaing.framework.annotations.RequestParam;
import co.edu.escuelaing.framework.annotations.RestController;
import co.edu.escuelaing.framework.enums.RequestMethod;

@RestController
public class CalculatorService {

    @RequestMapping(value = "/greet", method = RequestMethod.GET)
    public String greet(@RequestParam(value = "name", defaultValue = "Guest") String name) {
        return "Hello, " + name + "!";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(@RequestParam("a") int a, @RequestParam("b") int b) {
        return "Sum: " + a + " + " + b + " = " + (a + b);
    }

    @RequestMapping(value = "/subtract", method = RequestMethod.GET)
    public String subtract(@RequestParam("a") int a, @RequestParam("b") int b) {
        return "Difference: " + a + " - " + b + " = " + (a - b);
    }

    @RequestMapping(value = "/multiply", method = RequestMethod.GET)
    public String multiply(@RequestParam("a") int a, @RequestParam("b") int b) {
        return "Product: " + a + " * " + b + " = " + (a * b);
    }

    @RequestMapping(value = "/divide", method = RequestMethod.GET)
    public String divide(@RequestParam("a") int a, @RequestParam("b") int b) {
        if (b == 0) {
            return "Error: Division by zero is undefined.";
        }
        return "Quotient: " + a + " / " + b + " = " + (a / b);
    }

    @RequestMapping(value = "/power", method = RequestMethod.GET)
    public String power(@RequestParam("base") int base, @RequestParam("exponent") int exponent) {
        return "Result: " + base + " ^ " + exponent + " = " + Math.pow(base, exponent);
    }

    @RequestMapping(value = "/sqrt", method = RequestMethod.GET)
    public String sqrt(@RequestParam("number") double number) {
        if (number < 0) {
            return "Error: Square root of a negative number is undefined.";
        }
        return "Square root: sqrt(" + number + ") = " + Math.sqrt(number);
    }
}
