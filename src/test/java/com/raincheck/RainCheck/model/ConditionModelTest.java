package com.raincheck.RainCheck.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ConditionModelTest {

    // Test default constructor
    @Test
    public void testDefaultConstructor() {
        Condition condition = new Condition(); // Instantiate Condition object using default constructor
        assertNull(condition.getId()); // Assert id is null
        assertNull(condition.getName()); // Assert name is null
        assertNull(condition.getWeatherCode()); // Assert weatherCode is null
        assertNull(condition.getWeatherIcon()); // Assert weatherIcon is null
    }

    // Test all-args constructor
    @Test
    public void testAllArgsConstructor() {
        // Instantiate Condition object using all-args constructor
        Condition condition = new Condition(1, "Clear", 0);
        assertEquals(1, condition.getId()); // Assert id is 1
        assertEquals("Clear", condition.getName()); // Assert name is "Clear"
        assertEquals(0, condition.getWeatherCode()); // Assert weatherCode is 0
        assertNull(condition.getWeatherIcon()); // Assert weatherIcon is null initially
    }

    // Test getters and setters
    @Test
    public void testGettersAndSetters() {
        Condition condition = new Condition(); // Instantiate Condition object using default constructor

        // Set values using setters
        condition.setId(1);
        condition.setName("Clear");
        condition.setWeatherCode(0);
        condition.setWeatherIcon("sun.png");

        // Assert values using getters
        assertEquals(1, condition.getId()); // Assert id is 1
        assertEquals("Clear", condition.getName()); // Assert name is "Clear"
        assertEquals(0, condition.getWeatherCode()); // Assert weatherCode is 0
        assertEquals("sun.png", condition.getWeatherIcon()); // Assert weatherIcon is "sun.png"
    }

}