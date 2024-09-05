package co.edu.escuelaing.application;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorServiceTest {

    CalculatorService calculatorService = new CalculatorService();

    @Test
    public void testGreetWithName() {
        String result = calculatorService.greet("John");
        assertEquals("Hello, John!", result);
    }

    @Test
    public void testGreetWithoutName() {
        String result = calculatorService.greet(null);
        assertEquals("Hello, null!", result);
    }

    @Test
    public void testAddition() {
        String result = calculatorService.add(2, 3);
        assertEquals("Sum: 2 + 3 = 5", result);
    }

    @Test
    public void testSubtraction() {
        String result = calculatorService.subtract(5, 3);
        assertEquals("Difference: 5 - 3 = 2", result);
    }

    @Test
    public void testMultiplication() {
        String result = calculatorService.multiply(2, 3);
        assertEquals("Product: 2 * 3 = 6", result);
    }

    @Test
    public void testDivision() {
        String result = calculatorService.divide(6, 3);
        assertEquals("Quotient: 6 / 3 = 2", result);
    }

    @Test
    public void testDivisionByZero() {
        String result = calculatorService.divide(6, 0);
        assertEquals("Error: Division by zero is undefined.", result);
    }

    @Test
    public void testPower() {
        String result = calculatorService.power(2, 3);
        assertEquals("Result: 2 ^ 3 = 8.0", result);
    }

    @Test
    public void testSquareRoot() {
        String result = calculatorService.sqrt(4);
        assertEquals("Square root: sqrt(4.0) = 2.0", result);
    }

    @Test
    public void testSquareRootNegativeNumber() {
        String result = calculatorService.sqrt(-4);
        assertEquals("Error: Square root of a negative number is undefined.", result);
    }
}

