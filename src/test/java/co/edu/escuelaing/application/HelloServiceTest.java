package co.edu.escuelaing.application;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HelloServiceTest {

    @Test
    public void testHelloWithName() {
        String result = HelloService.hello("Alice");
        assertEquals("Hello Alice!", result);
    }

    @Test
    public void testHelloWithoutName() {
        String result = HelloService.hello(null);
        assertEquals("Hello null!", result);
    }

    @Test
    public void testPi() {
        String result = HelloService.pi();
        assertEquals("PI: " + Math.PI, result);
    }

    @Test
    public void testUuid() {
        String result1 = HelloService.uuid();
        String result2 = HelloService.uuid();
        assertNotEquals(result1, result2);  // Should be different because UUIDs are random
    }

    @Test
    public void testBye() {
        String result = HelloService.bye();
        assertEquals("Bye!", result);
    }

    @Test
    public void testRandomWithinRange() {
        double min = 1.0;
        double max = 10.0;
        String result = HelloService.random(min, max);
        double randomValue = Double.parseDouble(result.split(": ")[1]);
        assertTrue(randomValue >= min && randomValue <= max);
    }

    @Test
    public void testPower() {
        String result = HelloService.pow(2, 3);
        assertEquals("Pow: " + Math.pow(2, 3), result);
    }
}
