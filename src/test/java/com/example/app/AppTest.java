package com.example.app;

import org.junit.Test;
import static org.junit.Assert.*;

public class AppTest {
    
    @Test
    public void testGetGreeting() {
        App app = new App();
        assertEquals("Hello Jenkins CI/CD!", app.getGreeting());
    }
    
    @Test
    public void testAdd() {
        App app = new App();
        assertEquals(5, app.add(2, 3));
        assertEquals(0, app.add(-1, 1));
    }
    
    @Test
    public void testMultiply() {
        App app = new App();
        assertEquals(6, app.multiply(2, 3));
        assertEquals(0, app.multiply(0, 5));
    }
}
